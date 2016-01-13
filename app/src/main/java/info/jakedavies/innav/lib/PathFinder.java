package info.jakedavies.innav.lib;

import android.graphics.Point;
import android.util.Log;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.jakedavies.innav.lib.map.Intersection;

/**
 * Created by jakedavies on 2015-12-13.
 */
public class PathFinder {
    private ArrayList<Intersection> intersections;
    private HashMap<String, Intersection> intersectionHash = new HashMap<>();
    private ArrayList<Integer> xPoints;
    private ArrayList<Integer> yPoints;
    public PathFinder(ArrayList<Intersection> intersections){
        this.intersections = intersections;
    }
    public void reduce(){
        Intersection[][] pointToIntersectionMapping = new int[][yPoints.size()];
        for(Intersection i : intersections) {
            if(!xPoints.contains(i.getX())){
                xPoints.add(i.getX());
            }
            if(!yPoints.contains(i.getY())){
                yPoints.add(i.getY());
            }
            intersectionHash.put(hash(i.getX(), i.getY()), i);
        }
        GridCell[][] reducedPointGrid = new GridCell[xPoints.size()][yPoints.size()];
        for(int i = 0; i < reducedPointGrid.length; i++){
            for(int j = 0; i < reducedPointGrid[i].length; j++){
                reducedPointGrid[i][j] = new GridCell();
                reducedPointGrid[i][j].setWalkable(intersectionHash.containsKey(hash(i,j)));
            }
        }
    }
    private String hash(int x, int y){
        return "" + x +"" + y;
    }

}
