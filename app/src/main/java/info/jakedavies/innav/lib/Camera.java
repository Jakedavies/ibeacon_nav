package info.jakedavies.innav.lib;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import org.xguzm.pathfinding.grid.GridCell;

import java.util.ArrayList;

/**
 * Created by jakedavies on 15-11-17.
 */
public class Camera {
    private MapByte[][] floorplan = new MapByte[2001][2001];
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
    private PathFinder pathFinder;


    public Camera(){
        initFloorplan();
    }

    // Temp method to build a test map
    public void initFloorplan(){
        int blockWidth = 35;
        int blockWidth2 = 25;

        for(int i = 0; i< floorplan.length; i++){
            for(int j = 0; j < floorplan.length; j++){
                floorplan[i][j] = new MapByte();
            }
        }

        for(int i = 0; i< floorplan.length; i++){
            for(int j = 0; j < floorplan.length; j++){
                if((i/blockWidth) % 2 ==0 && (j/blockWidth) % 2 ==0){
                    floorplan[i][j].setObstacle(true);
                }
            }
        }
        for(int i = 0; i< floorplan.length; i++){
            for(int j = 0; j < floorplan.length; j++){
                if(((i/blockWidth2)+blockWidth) % 3 ==0 && (j/blockWidth2) % 3 ==0){
                    floorplan[i][j].setIsle(true);
                }
            }
        }

    }
    public void setPhoneRotation(int d){
        degrees = d;
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
//        pathfinder no worky currently
//        pathFinder = new PathFinder(floorplan, new Point(phoneXU, phoneYU), new Point((int)(floorplan.length*.75) , (int) (floorplan[0].length*.75)));
//        for( GridCell gc : pathFinder.getPath()){
//            int x = gc.getX();
//            int y = gc.getY();
//            floorplan[x][y].setPath(true);
//        }
    }
    public ArrayList<PaintedRect> getCameraView(){
        ArrayList<PaintedRect> out = new ArrayList<>();
        for(int x =0; x < phoneXU; x++){
            for(int y =0; y < phoneYU; y++){
                Point p = getRotatedPoint(phoneLeft+x, phoneTop+y, degrees);
                if(floorplan[p.x][p.y].isPath()){
                    out.add(new PaintedRect(new Rect(x*ppu, y*ppu, (x+1)*ppu,(y+1)*ppu), 3));
                }
                if(floorplan[p.x][p.y].isObstacle()){
                    out.add(new PaintedRect(new Rect(x*ppu, y*ppu, (x+1)*ppu,(y+1)*ppu), 0));
                }
                if(floorplan[p.x][p.y].isIsle()){
                    out.add(new PaintedRect(new Rect(x*ppu, y*ppu, (x+1)*ppu,(y+1)*ppu), 1));
                }
            }
        }
        return out;
    }
    public int blocked(int clickX, int clickY){
        Point p = getRotatedPoint(phoneLeft+(clickX/ppu), phoneTop+(clickY/ppu), degrees);
        if(floorplan[p.x][p.y].isObstacle()){
            return 50;
        }if(floorplan[p.x][p.y].isIsle()){
            return 20;
        }else{
            return -1;
        }
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
