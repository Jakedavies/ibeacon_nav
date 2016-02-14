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
    private int zoom = 1;
    private int phoneXU;
    private int phoneTop;
    private int phoneBottom;
    private int phoneLeft;
    private int phoneRight;
    private int degrees;

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
    public void setZoom(int zoom){
        this.zoom = zoom;
        calculateDims();
    }
    public Point getPhonePosition(){
        return new Point(phoneXPosition, phoneYPosition);
    }
    public void setPhonePixels(int width, int height){
        pixelsWide = width;
        pixelsHigh = height;
        calculateDims();
    }
    private void calculateDims(){
        // TODO: Fix Zoom
        phoneXU = pixelsWide / (zoom * pixelsPerMeter);
        phoneYU = pixelsHigh / (zoom * pixelsPerMeter);
        phoneBottom = (phoneYPosition/zoom);
        phoneLeft = (phoneXPosition/zoom) -(phoneXU/2);
        phoneRight = (phoneXPosition/zoom) +(phoneXU/2);
        phoneTop = (phoneYPosition/zoom) - phoneYU;
    }

    public ArrayList<PaintedRect> getCameraView(){
        ArrayList<PaintedRect> out = new ArrayList<>();
        // we should be able to change this method
        for(int x =0; x < phoneXU; x++){
            for(int y =0; y < phoneYU; y++){
                Point p = getRotatedPoint(phoneLeft+x, phoneBottom+y - phoneYU, degrees);
                Rect r = new Rect(x* pixelsPerMeter, y * pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter);
                if(p.x >= map.getFloorPlan().length || p.x < 0 || p.y < 0 || p.y >= map.getFloorPlan()[0].length){
                    //probably should create a void type here, and use that instead of the current obstacle type we are using
                    out.add(new PaintedRect(r, 0));
                }
                else {
                    if(map.getFloorPlan()[p.x][p.y].isPath()){
                        out.add(new PaintedRect(r, 3));
                    }
                    if(map.getFloorPlan()[p.x][p.y].isObstacle()){
                        out.add(new PaintedRect(r, 0));
                    }
                    if(map.getFloorPlan()[p.x][p.y].isIsle()){
                        out.add(new PaintedRect(r, 1));
                    }
                    if(map.getFloorPlan()[p.x][p.y].isIntersection()){
                        out.add(new PaintedRect(new Rect(x* pixelsPerMeter, y* pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter), 4));
                    }
                    if(map.getFloorPlan()[p.x][p.y].isBeacon()){
                        out.add(new PaintedRect(new Rect(x* pixelsPerMeter, y* pixelsPerMeter, (x+1)* pixelsPerMeter,(y+1)* pixelsPerMeter), 5));
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
