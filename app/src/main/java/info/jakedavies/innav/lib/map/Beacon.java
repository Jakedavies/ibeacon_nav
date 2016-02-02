package info.jakedavies.innav.lib.map;

/**
 * Created by jakedavies on 16-02-02.
 */
public class Beacon {
    private int x = 0;
    private int y = 0;
    private String uuid;

    public Beacon(int x, int y, String uuid){
        this.x = x;
        this.y = y;
        this.uuid = uuid;
    }
    public String getUUID() { return uuid; }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

}
