package info.jakedavies.innav.lib.map;

/**
 * Created by jakedavies on 2016-01-02.
 */
public class Feature {
    private int width = 10;
    private int height = 10;
    private int x = 0;
    private int y = 0;
    private String type = "Isle";

    public Feature(int width, int height,int x,int y, String type){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public Feature(){
    }
}
