package info.jakedavies.innav.sensor;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.RemoteException;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.lemmingapex.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by jakedavies on 16-02-02.
 */
public class Position {
    private PositionChangedListener mCallback;
    private SensorManager mSensorManager;
    private BeaconManager mBeaconManager;
    private Region region = new Region("ranged region", "B9407F30-F5F8-466E-AFF9-25556B57FE6D", null, null);
    private List<info.jakedavies.innav.lib.map.Beacon> beacons;

    public interface PositionChangedListener {
        void positionChanged(Point p);
    }

    public void registerListener() {
        // begin listening for ibeacons
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    mBeaconManager.startRanging(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void unregisterListener() {
        // stop listening for ibeacons
        try {
            mBeaconManager.stopRanging(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    // check a specific beacon and its location from the floorplan
    private Point lookupBeacon(int major){
        // check list of beacons, find where this one is, return that point
        for(info.jakedavies.innav.lib.map.Beacon b : beacons){
            if(b.getID() == major){
                return new Point(b.getX(), b.getY());
            }
        }
        return null;
    }

    // returns the actual current location
    public Position(Context context, PositionChangedListener headingChangedListener, List<info.jakedavies.innav.lib.map.Beacon> beacons) {
        mCallback = headingChangedListener;
        mBeaconManager = new BeaconManager(context);
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.beacons = beacons;
        // add this below:
        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (list.size() >=  3) {
                    trilateralCalc(list.get(0), list.get(1), list.get(2));
                }
            }

        });
    }

    private void trilateralCalc(Beacon a, Beacon b, Beacon c) {
        Point ab, bb, cb;
        ab = lookupBeacon(a.getMajor());
        bb = lookupBeacon(b.getMajor());
        cb = lookupBeacon(c.getMajor());
        double[][] positions = new double[][] { { 5.0, -6.0 }, { 13.0, -15.0 }, { 21.0, -3.0 }, { 12.4, -21.2 } };
        double[] distances = new double[] { 8.06, 13.97, 23.32, 15.31 };

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        Optimum optimum = solver.solve();

// the answer
        double[] centroid = optimum.getPoint().toArray();

// error and geometry information; may throw SingularMatrixException depending the threshold argument provided
        RealVector standardDeviation = optimum.getSigma(0);
        RealMatrix covarianceMatrix = optimum.getCovariances(0);
        // perform 3 quadratics and then we solve for x...

    }
}
