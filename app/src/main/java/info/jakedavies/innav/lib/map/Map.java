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
        floorplan = new MapByte[width][height];
        for(int i = 0; i< floorplan.length; i++){
            for(int j = 0; j < floorplan.length; j++){
                floorplan[i][j] = new MapByte();
            }
        }
        for(Feature f : features){
            String type = f.getType();
            for(int i =f.getX(); i < f.getX() + f.getWidth(); i++){
                for(int j =f.getY(); j < f.getY() + f.getHeight(); j++){
                    floorplan[i][j].setType(type);
                }
            }
        }
    }
    public MapByte[][] getFloorPlan(){
        return floorplan;
    }
}
