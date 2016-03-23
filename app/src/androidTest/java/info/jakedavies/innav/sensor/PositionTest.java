package info.jakedavies.innav.sensor;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by jakedavies on 16-02-25.
 */
public class PositionTest extends TestCase {

    public void testGetCentroid() throws Exception {
        Position position = new Position();

        double [][] beacons = {
                {0,0},
                {5, 10},
                {0, 10},
        };
        double distances[] = {7.07, 5, 7.07};
        double [] output = position.getCentroid(beacons, distances);

        System.out.println(output[0]);
        System.out.println(output[1]);
        Log.d("TEST", output[0] + " : " + output[1]);
        TestCase.assertEquals(5, Math.round(output[0]));
        TestCase.assertEquals(5, Math.round(output[1]));
    }

    public void testGetCentroidTwo() throws Exception {
        Position position = new Position();

        double [][] beacons = {
                {0,0},
                {1, 1},
                {3, 0},
        };
        double distances[] = {1, 1, 2};
        double [] output = position.getCentroid(beacons, distances);

        System.out.println(output[0]);
        System.out.println(output[1]);
        Log.d("TEST", output[0] + " : " + output[1]);
        TestCase.assertTrue((output[1] - 0) < .5);
        TestCase.assertTrue((output[0] - 1) < .5);
    }
    public void testGetCentroidThree() throws Exception {
        Position position = new Position();

        double [][] beacons = {
                {0,0},
                {5, 10},
                {0, 10},
        };
        double distances[] = {8.25, 3.31, 11.31};
        double [] output = position.getCentroid(beacons, distances);
        Log.d("TEST", output[0] + " : " + output[1]);
        TestCase.assertTrue(Math.abs((output[1] - 2)) < .5);
        TestCase.assertTrue(Math.abs((output[0] - 8)) < .5);
    }

}