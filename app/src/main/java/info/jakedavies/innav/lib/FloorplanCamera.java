package info.jakedavies.innav.lib;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import info.jakedavies.innav.lib.floorplan.Floorplan;

/**
 * Created by jakedavies on 2015-11-14.
 */
public class FloorplanCamera {

    private boolean[][] floorplan = new boolean[51][51];
    private int ppu = 50;
    private int pWidth;
    private int pHeight;
    private int phoneX;
    private int phoneY;
    private int phoneCenterX;
    private int phoneBottomY;

    public FloorplanCamera(){
        for(int i =0; i < floorplan.length; i++){
            for(int j =0; i < floorplan[i].length; j++){
                if( j %2 ==0 && i % 2 == 0){
                    floorplan[i][j] = false;
                }
                else{
                    floorplan[i][j] = true;
                }
            }
        }
    }
    public void setPhonePixels(int width, int height){
        pWidth = width;
        pHeight = height;
    };
    public void setPixelsPerUnit(int ppu){
        this.ppu = ppu;
    };
    public void setPhonePosition(){
        phoneX = 25;
        phoneY = 25;
        phoneCenterX = phoneX;
        phoneBottomY = phoneY + (pHeight/2);
    };
    public ArrayList<Rect> getCameraView(){
        ArrayList<Rect> out = new ArrayList<>();

        return out;
    }
    private Point getRotatedPoint(int x,int y, int degrees){
        Point p = new Point(x,y);
        Matrix m = new Matrix();
        m.setRotate(degrees, phoneCenterX, phoneBottomY);
        float[] points = {(float)p.x, (float)p.y};
        m.mapPoints(points);
        return new Point(Math.round(points[0]), Math.round(points[1]));
    }
}
