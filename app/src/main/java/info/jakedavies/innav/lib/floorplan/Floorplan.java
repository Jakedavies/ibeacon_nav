package info.jakedavies.innav.lib.floorplan;

import java.util.ArrayList;


public class Floorplan {

    ArrayList<FloorplanObject> objects;
    int floorWidth;
    int floorHeight;

    public Floorplan(int width, int height){
        objects = new ArrayList<>();
    }
    public void addObjectToFloorplan(FloorplanObject o){
        objects.add(o);
    }
    public ArrayList getObjects(){
        return objects;
    }
}
