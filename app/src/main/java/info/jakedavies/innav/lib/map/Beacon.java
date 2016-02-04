package info.jakedavies.innav.lib.map;

/**
 * Created by jakedavies on 16-02-02.
 */
public class Beacon {
    private int x = 0;
    private int y = 0;
    private int id = 0;

    public Beacon(int x, int y, int id){
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public int getID() { return id; }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

}
