package info.jakedavies.innav.lib;

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

        Log.d("FUCKFUCK", ""+c.getRotatedPoint(25, 55, -45).x);
        Log.d("FUCKFUCK", ""+c.getRotatedPoint(25, 55, -45).y);
    }
}