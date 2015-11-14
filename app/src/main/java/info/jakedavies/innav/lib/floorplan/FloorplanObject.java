package info.jakedavies.innav.lib.floorplan;

import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by jakedavies on 15-11-10.
 */
public interface FloorplanObject {
    public String toJson();
    public ArrayList<Integer> getCoords();
    public Rect getShape();
}
