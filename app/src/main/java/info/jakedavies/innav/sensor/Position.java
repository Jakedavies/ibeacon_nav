package info.jakedavies.innav.sensor;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by jakedavies on 16-02-02.
 */
public class Position {
    private PositionChangedListener mCallback;
    private SensorManager mSensorManager;

    public interface PositionChangedListener {
        void positionChanged(Point p);
    }
    // get all nearby beacons and return them, using the estimote sdk method
    private void getBeacons() {

    }

    public void registerListener() {
        // begin listening for ibeacons
    }

    public void unregisterListener() {
        // stop listening for ibeacons
    }
    // check a specific beacon and its location from the floorplan
    private Point lookupBeacon(String uuid){

        return new Point(0,0);
    }
    // returns the actual current location
    public Position(Context context, PositionChangedListener headingChangedListener) {
        mCallback = headingChangedListener;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

}
