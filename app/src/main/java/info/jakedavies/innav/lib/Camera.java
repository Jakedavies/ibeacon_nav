package info.jakedavies.innav.lib;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by jakedavies on 15-11-17.
 */
public class Camera {
    private boolean[][] floorplan = new boolean[51][51];
    private int ppu = 50;
    private int pWidth;
    private int pHeight;
    private int phoneX;
    private int phoneY;
    private int phoneCenterX;
    private int phoneBottomY;

    public Camera(){
        for(int i =0; i < floorplan.length; i++){
            for(int j =0; j < floorplan[i].length; j++){
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
    public Point getRotatedPoint(int x,int y, int degrees){
        Point p = new Point(x,y);
        Matrix m = new Matrix();
        m.setRotate(degrees, phoneCenterX, phoneBottomY);
        float[] points = {(float)p.x, (float)p.y};
        m.mapPoints(points);
        return new Point(Math.round(points[0]), Math.round(points[1]));
    }
}
