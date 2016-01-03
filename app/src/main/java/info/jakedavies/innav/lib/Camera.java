package info.jakedavies.innav.lib;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import info.jakedavies.innav.lib.map.Map;

/**
 * Created by jakedavies on 15-11-17.
 */
public class Camera {

    // all used for config
    Map map;
    private int pixelsPerMeter = 10;
    private int pixelsWide;
    private int pixelsHigh;
    private int phoneXPosition;
    private int phoneYPosition;
    // automatically set under this
    private int phoneYU;
    private int phoneXU;
    private int phoneTop;
    private int phoneBottom;
    private int phoneLeft;
    private int phoneRight;
    private int degrees;
    private PathFinder pathFinder;


    public Camera(Map m){
        this.map = m;
    }

    public void setPhoneRotation(int d){
        degrees = d;
    }
    public void setPhonePosition(int x, int y){
        phoneXPosition = x;
        phoneYPosition = y;
    }
    public Point getPhonePosition(){
        return new Point(phoneXPosition, phoneYPosition);
    }
    public void setPhonePixels(int width, int height){
        pixelsWide = width;
        pixelsHigh = height;
        phoneXU = pixelsWide / pixelsPerMeter;
        phoneYU = pixelsHigh / pixelsPerMeter;
        phoneBottom = phoneYPosition;
        phoneLeft = phoneXPosition -(phoneXU/2);
        phoneRight = phoneXPosition +(phoneXU/2);
        phoneTop = phoneYPosition +phoneYU;
    }

    public ArrayList<PaintedRect> getCameraView(){
        ArrayList<PaintedRect> out = new ArrayList<>();
        // we should be able to change this method
        for(int x =0; x < phoneXU; x++){
            for(int y =0; y < phoneYU; y++){
                Point p = getRotatedPoint(phoneLeft+x, phoneBottom+y, degrees);
                if(p.x >= map.getFloorPlan().length || p.x < 0 || p.y < 0 || p.y >= map.getFloorPlan()[0].length){
                    //probably should create a void type here, and use that instead of the current obstacle type we are using
                    out.add(new PaintedRect(new Rect(x* pixelsPerMeter, y* pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter), 0));
                }
                else {
                    if(map.getFloorPlan()[p.x][p.y].isPath()){
                        out.add(new PaintedRect(new Rect(x* pixelsPerMeter, y* pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter), 3));
                    }
                    if(map.getFloorPlan()[p.x][p.y].isObstacle()){
                        out.add(new PaintedRect(new Rect(x* pixelsPerMeter, y* pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter), 0));
                    }
                    if(map.getFloorPlan()[p.x][p.y].isIsle()){
                        out.add(new PaintedRect(new Rect(x* pixelsPerMeter, y* pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter), 1));
                    }
                }

            }
        }
        return out;
    }
    // use this method on every point currently displayed on the phone, then get what should be in that point
    public Point getRotatedPoint(int x,int y, int degrees){
        Point p = new Point(x,y);
        Matrix m = new Matrix();
        m.setRotate(degrees, phoneLeft+((phoneRight-phoneLeft)/2),phoneBottom);
        float[] points = {(float)p.x, (float)p.y};
        m.mapPoints(points);
        return new Point(Math.round(points[0]), Math.round(points[1]));
    }
    public Point getPointInMapCoords(int x, int y) {
        Point p = getRotatedPoint(phoneLeft+(x/pixelsPerMeter), phoneTop+(y/pixelsPerMeter), degrees);
        return p;
    }
}
