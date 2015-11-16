package info.jakedavies.innav.lib;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import info.jakedavies.innav.lib.floorplan.Floorplan;

/**
 * Created by jakedavies on 2015-11-14.
 */
public class FloorplanCamera {
    Floorplan fp;
    int scale;
    int rotation;
    int phonex;
    int phoney;
    int width;
    int height;
    String TAG = "FLOORPLAN_CAMERA";

    public FloorplanCamera(Floorplan fp){
        this.fp = fp;
    }
    private Path rectToPath(Rect rect){
        Path p = new Path();

        p.moveTo(rect.left, rect.top);
        p.lineTo(rect.right, rect.top);
        p.moveTo(rect.right, rect.top);
        p.lineTo(rect.right, rect.bottom);
        p.moveTo(rect.right, rect.bottom);
        p.lineTo(rect.left, rect.bottom);
        p.moveTo(rect.left, rect.bottom);
        p.lineTo(rect.left, rect.top);
        p.close();
        return p;
    };

    public void setPhonePos(int x, int y){
        phonex = x;
        phoney = y;
    };
    public void setPhoneDimensions(int width, int height){
        this.height = height;
        this.width = width;
    }
    public void setViewSize(int scale){
        this.scale = scale;
    }
    private ArrayList<Path> getScaledAndRotatedPaths(){
        ArrayList<Path> scaledAndRotatatedPaths = new ArrayList<>();
        int phoneTop = phoney-scale/2;
        int phoneBottom = phoney+scale/2;
        int phoneLeft = phonex-scale/2;
        int phoneRight = phonex+scale/2;
        Rect phoneRect = new Rect(phoneLeft,phoneTop, phoneRight, phoneBottom);

        for (Rect obstacle: fp.getObjects()) {
            Path obstacleAsPath = rectToPath(obstacle);
            RectF boundingBox = new RectF();
            obstacleAsPath.computeBounds(boundingBox, false);
            int top = Math.round(boundingBox.top);
            int bottom = Math.round(boundingBox.bottom);
            int left = Math.round(boundingBox.left);
            int right = Math.round(boundingBox.right);

            // we need to change the position to a normalized amount
            // 
            int relCenterX = (int) boundingBox.centerX() / ;
            int relCenterY = boundingBox.centerY();
            if(phoneRect.contains(new Rect(left,top, right, bottom))){
                Log.d(TAG, "ADDING OBSTACBLE");
                Matrix m = new Matrix();
                m.setScale(2, 2, relCenterX, relCenterY);
                obstacleAsPath.transform(m);
                Log.d(TAG, "adding obstacle at "+obstacleAsPath);
                scaledAndRotatatedPaths.add(obstacleAsPath);
            }
        }
        return scaledAndRotatatedPaths;
    }
    public ArrayList<Path> getCameraView(){
        return getScaledAndRotatedPaths();
    }
    public void setRotation(int rotation){
        this.rotation = rotation;
    }

}
