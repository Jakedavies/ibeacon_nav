package info.jakedavies.innav.sensor;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.RemoteException;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

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
    long lastTimeUpdate = System.currentTimeMillis();
    private Region region = new Region("ranged region", "B9407F30-F5F8-466E-AFF9-012345678910", null, null);
    private List<info.jakedavies.innav.lib.map.Beacon> beacons;
    private long updateInterval = 500;

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
                    if(System.currentTimeMillis() > lastTimeUpdate + updateInterval){
                        Point newPosition = trilateralCalc(list.get(0), list.get(1), list.get(2));
                        if(newPosition != null){
                            updatePosition(newPosition);
                        }
                    }
                }
            }

        });
    }

    private void updatePosition(Point newPosition){
        lastTimeUpdate = System.currentTimeMillis();
        mCallback.positionChanged(newPosition);
    }

    private Point trilateralCalc(Beacon a, Beacon b, Beacon c) {
        Point ab, bb, cb;
        ab = lookupBeacon(a.getMajor());
        bb = lookupBeacon(b.getMajor());
        cb = lookupBeacon(c.getMajor());
        if (ab == null || bb == null || cb == null){
            return null;
        }
        double[][] positions = new double[][] { { ab.x, ab.y }, { bb.x, bb.y }, { cb.x, cb.y }};
        double[] distances = new double[] {
                // scale measurments to a tenth of a meter
                calculateAccuracy(a.getMeasuredPower(), a.getRssi()) * 10,
                calculateAccuracy(b.getMeasuredPower(), b.getRssi()) * 10,
                calculateAccuracy(c.getMeasuredPower(), c.getRssi()) * 10,
        };

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        // the answer
        double[] centroid = optimum.getPoint().toArray();

        return new Point((int) centroid[0], (int) centroid[1]);
    }

    // http://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing/20434019#20434019
    private double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            return (0.89976)*Math.pow(ratio,7.7095) + 0.111;
        }
    }
}
