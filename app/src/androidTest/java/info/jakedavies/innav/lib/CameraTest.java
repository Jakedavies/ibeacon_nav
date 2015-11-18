package info.jakedavies.innav.lib;

import android.graphics.Point;
import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by jakedavies on 15-11-17.
 */
public class CameraTest extends TestCase {


    public void testSetPhonePixels() throws Exception {

    }

    public void testSetPixelsPerUnit() throws Exception {

    }

    public void testSetPhonePosition() throws Exception {

    }

    public void testGetCameraView() throws Exception {

    }
    public void testGetRotatedPoint() throws Exception{
        Camera c = new Camera();

        Point p = c.getRotatedPoint(20, 10, 45);
        Log.d("CameraTest", p.toString());
        p = c.getRotatedPoint(30, 10, 45);
        Log.d("CameraTest", p.toString());

        p = c.getRotatedPoint(20, 10, 180);
        Log.d("CameraTest", p.toString());
        p = c.getRotatedPoint(30, 10, 180);
        Log.d("CameraTest", p.toString());
    }
}