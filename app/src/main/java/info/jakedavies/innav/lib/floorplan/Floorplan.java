package info.jakedavies.innav.lib.floorplan;

import android.graphics.Rect;

import java.util.ArrayList;


public class Floorplan {

    ArrayList<Rect> objects;
    int floorWidth;
    int floorHeight;

    public Floorplan(int width, int height){
        objects = new ArrayList<Rect>();
    }
    public void addObjectToFloorplan(Rect o){
        objects.add(o);
    }
    public ArrayList<Rect> getObjects(){
        return objects;
    }
    public int getWidth(){
        return floorWidth;
    }
    public int getHeight(){
        return floorHeight;
    }

}
