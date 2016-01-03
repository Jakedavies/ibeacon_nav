package info.jakedavies.innav.lib.map;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import info.jakedavies.innav.lib.MapByte;
import info.jakedavies.innav.lib.floorplan.Floorplan;

/**
 * Created by jakedavies on 2016-01-02.
        */
public class Map {
    private int width;
    private int height;
    private MapByte[][] floorplan;
    public Map(){

    }

    private List<Feature> features = new ArrayList<Feature>();

    public List<Feature> getFeatures(){
        return features;
    }
    public void addFeature(Feature f){
        features.add(f);
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    // return the vibration for a specific point
    public int getVibrationForPoint(Point p){
        return 50;
    }

    public void buildFloorPlan(){

    }
    public MapByte[][] getFloorPlan(){
        return floorplan;
    }
}
