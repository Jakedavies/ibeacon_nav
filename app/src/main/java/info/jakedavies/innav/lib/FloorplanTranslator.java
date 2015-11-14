package info.jakedavies.innav.lib;

import info.jakedavies.innav.lib.floorplan.Floorplan;

/**
 * Created by jakedavies on 2015-11-14.
 */
public class FloorplanTranslator {
    Floorplan fp;
    int scale;
    int rotation;

    public FloorplanTranslator(Floorplan fp){
        this.fp = fp;
    }

    public void setScaling(int scale){
        this.scale = scale;
    }
    public void setRotation(int rotation){
        this.rotation = rotation;
    }
}
