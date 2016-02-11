package info.jakedavies.innav.lib.map;

/**
 * Created by jakedavies on 2016-01-02.
 */
public class Intersection {
    private int x = 0;
    private int y = 0;
    private boolean canBeGoal = false;
    private String name = null;
    public Intersection(int x,int y,boolean canBeGoal){
        this.x = x;
        this.y = y;
        this.canBeGoal = canBeGoal;
    }
    public boolean canBeGoal() {
        return canBeGoal;
    }
    public String getName() { return name; };
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
