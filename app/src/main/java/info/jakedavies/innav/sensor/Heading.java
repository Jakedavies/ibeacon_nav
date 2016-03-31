package info.jakedavies.innav.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Used as a reference under apache 2.0
 * http://www.netmite.com/android/mydroid/cupcake/development/samples/Compass/src/com/example/android/compass/CompassActivity.java
 */

public class Heading implements SensorEventListener {

    private SensorManager mSensorManager;
    private int numRangeSegments = 8;

    private long mLastUpdate;

    private int mUpdateFrequency = 300; //update the sensor every 300 seconds
    private HeadingChangedListener mCallback;
    int stacksize = 3;

    private float[] mGravity;
    private float[] mMagnetic;
    private LinkedList<Integer> averageStack = new LinkedList<Integer>();

    public interface HeadingChangedListener {
        void headingChanged(int heading);
    }

    public Heading(Context context, HeadingChangedListener headingChangedListener) {
        mCallback = headingChangedListener;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void registerListener() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            mGravity = sensorEvent.values.clone();
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagnetic = sensorEvent.values.clone();
        }
        if (mGravity != null && mMagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mMagnetic);

            if (success) {
                long currentTime = System.currentTimeMillis();

                if ((currentTime - mLastUpdate) > mUpdateFrequency) {
                    mLastUpdate = currentTime;
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    float inRads = orientation[0];
                    int inDegs = (int)(Math.toDegrees(inRads) + 360) % 360;
                    int segmentSizeInDegress = 360 / 8;
                    int segmentNumber = inDegs / segmentSizeInDegress;
                    int outDegs = (segmentNumber * segmentSizeInDegress);
                    mCallback.headingChanged((int) Math.toDegrees(inRads));
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}