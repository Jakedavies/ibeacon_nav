package info.jakedavies.innav.lib;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jakedavies on 15-11-17.
 */
public class Camera {
    private boolean[][] floorplan = new boolean[2001][2001];
    private int ppu = 10;
    private int pWidth;
    private int pHeight;
    private int phoneX = 1000;
    private int phoneY = 1000;
    private int phoneYU;
    private int phoneXU;
    private int phoneTop;
    private int phoneBottom;
    private int phoneLeft;
    private int phoneRight;
    private int degrees;

    public Camera(){
        for(int i =0; i < floorplan.length; i++){
            for(int j =0; j < floorplan[i].length; j++){
                if( i % 3 ==0 || i % 4 ==0){
                    floorplan[i][j] = true;
                }
                else{
                    floorplan[i][j] = false;
                }
            }
        }
    }
    public void setPhoneRotation(int d){
        Log.d("CAMERA", "Rotating");
        degrees =d;
    }
    public void setPhonePixels(int width, int height){
        pWidth = width;
        pHeight = height;
        phoneXU = pWidth/ppu;
        phoneYU = pHeight/ppu;
        phoneBottom = phoneY;
        phoneLeft = phoneX-(phoneXU/2);
        phoneRight = phoneX+(phoneXU/2);
        phoneTop = phoneY+phoneYU;

    }
    public ArrayList<Rect> getCameraView(){
        ArrayList<Rect> out = new ArrayList<>();
        for(int x =0; x < phoneXU; x++){
            for(int y =0; y < phoneYU; y++){
                Point p = getRotatedPoint(phoneLeft+x, phoneTop+y, degrees);
                if(floorplan[p.x][p.y]){
                    out.add(new Rect(x*ppu, y*ppu, (x+1)*ppu,(y+1)*ppu));
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
}
