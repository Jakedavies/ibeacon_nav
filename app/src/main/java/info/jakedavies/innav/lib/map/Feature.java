package info.jakedavies.innav.lib.map;

/**
 * Created by jakedavies on 2016-01-02.
 */
public class Feature {
    private int width = 10;
    private int height = 10;
    private String type = "Isle";

    public Feature(int width, int height, String type){
        this.width = width;
        this.height = height;
        this.type = type;
    }
    public Feature(){
    }
}
