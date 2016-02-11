package info.jakedavies.innav.adapter;

/**
 * Created by jakedavies on 16-02-08.
 */
public class Location {
    String name;
    int id;
    public Location(int id, String name){
        this.id = id;
        this.name = name;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
