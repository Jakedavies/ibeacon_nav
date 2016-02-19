package info.jakedavies.innav.lib.map;

import android.graphics.Point;
import android.graphics.Rect;

import org.xguzm.pathfinding.grid.GridCell;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import info.jakedavies.innav.lib.MapByte;
import info.jakedavies.innav.lib.PathFinder;
import info.jakedavies.innav.lib.floorplan.Floorplan;

/**
 * Created by jakedavies on 2016-01-02.
        */
public class Map {
    private int width;
    private int height;
    private String name;
    private MapByte[][] floorplan;
    public Map(){

    }

    private List<Feature> features = new ArrayList<Feature>();
    private ArrayList<Intersection> intersections = new ArrayList<Intersection>();
    private ArrayList<Beacon> beacons = new ArrayList<Beacon>();
    public List<Feature> getFeatures(){
        return features;
    }
    public List<Intersection> getIntersection(){
        return intersections;
    }
    public List<Beacon> getBeacons() {
        return beacons;
    }
    public List<Intersection> getSections() {
        ArrayList<Intersection> sections = new ArrayList<>();
        for(Intersection i : intersections){
            sections.add(i);
        }
        return sections;
    }
    public String getName(){
        return name;
    }
    public void addFeature(Feature f){
        features.add(f);
    }
    public void addIntersection(Intersection i){
        intersections.add(i);
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

    public int start_x, start_y, goal_x = -1, goal_y = -1;

    public void setGoal(String goalName){
        for (Intersection i:
             intersections) {
            if(i.canBeGoal() && i.getName().equals(goalName)){
                goal_x = i.getX();
                goal_y = i.getY();
                break;
            }
        }
    }
    public void setStart(int x, int y){
        start_x = x;
        start_y = y;
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
        for(Intersection intersection : intersections){
            floorplan[intersection.getX()][intersection.getY()].setIntersection(true);
        }
        for(Beacon b : beacons){
            floorplan[b.getX()][b.getY()].setIntersection(true);
        }
        // if goal is set, we calculate path
        if(goal_x > -1 && goal_y > -1){
            PathFinder p = new PathFinder(intersections, floorplan);
            ArrayList<Rect> path = p.reduce(start_x, start_y, goal_x, goal_y);
            for (Rect r : path) {
                for(int i = r.left; i <= r.right; i++){
                    for(int j =r.top; j <= r.bottom; j++){
                        floorplan[i][j].setPath(true);
                    }
                }
            }
        }

    }
    public MapByte[][] getFloorPlan(){
        return floorplan;
    }
}
