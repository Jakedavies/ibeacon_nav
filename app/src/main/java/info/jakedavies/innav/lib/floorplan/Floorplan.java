package info.jakedavies.innav.lib.floorplan;

import java.util.ArrayList;

/**
 * Created by jakedavies on 15-11-10.
 */
public class Floorplan {

    ArrayList<FloorplanObject> objects;
    public Floorplan(){
        objects = new ArrayList<>();
    }
    public void addObjectToFloorplan(FloorplanObject o){
        objects.add(o);
    }
    public ArrayList getObjects(){
        return objects;
    }
}
