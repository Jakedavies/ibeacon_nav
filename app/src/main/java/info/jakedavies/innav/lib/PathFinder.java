package info.jakedavies.innav.lib;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import info.jakedavies.innav.lib.map.Intersection;

/**
 * Created by jakedavies on 2015-12-13.
 */
public class PathFinder {
    private ArrayList<Intersection> intersections;
    private HashMap<String, Intersection> intersectionHash = new HashMap<>();
    private HashMap<String, Intersection> trueCoordinateHash = new HashMap<>();
    private ArrayList<Integer> xPoints = new ArrayList<Integer>();
    private ArrayList<Integer> yPoints  = new ArrayList<Integer>();;
    private int halfPathWidth =1;
    public PathFinder(ArrayList<Intersection> intersections){
        this.intersections = intersections;
    }
    public ArrayList<Rect> reduce(){
        for(Intersection i : intersections) {
            if(!xPoints.contains(i.getX())){
                xPoints.add(i.getX());
            }
            if(!yPoints.contains(i.getY())){
                yPoints.add(i.getY());
            }
            intersectionHash.put(hash(i.getX(), i.getY()), i);
        }
        Collections.sort(xPoints);
        Collections.sort(yPoints);
        GridCell[][] reducedPointGrid = new GridCell[xPoints.size()][yPoints.size()];
        for(int i = 0; i < reducedPointGrid.length; i++){
            for(int j = 0; j < reducedPointGrid[i].length; j++){
                reducedPointGrid[i][j] = new GridCell();
                reducedPointGrid[i][j].setX(i);
                reducedPointGrid[i][j].setY(j);
                reducedPointGrid[i][j].setWalkable(intersectionHash.containsKey(hash(xPoints.get(i),yPoints.get(j))));
            }
        }

        NavigationGrid<GridCell> navGrid = new NavigationGrid(reducedPointGrid);

        //create a finder either using the default options
        //or create your own pathfinder options:
        GridFinderOptions opt = new GridFinderOptions();
        opt.allowDiagonal = false;

        AStarGridFinder<GridCell> finder = new AStarGridFinder(GridCell.class, opt);
        List<GridCell> pathToEnd = finder.findPath(0, 0, 2, 1, navGrid);


        ArrayList<Rect> output = new ArrayList<Rect>();
        GridCell start = new GridCell();
        start.setX(0);
        start.setY(0);
        start.setWalkable(true);
        pathToEnd.add(0, start);
        for (GridCell gc: pathToEnd) {
            int nextIndex = pathToEnd.indexOf(gc) + 1;
            if(pathToEnd.size() > nextIndex){
                GridCell nextGC = pathToEnd.get(nextIndex);
                Rect r = pointsToRect(new Point(gc.getX(), gc.getY()), new Point(nextGC.getX(), nextGC.getY()));
                output.add(r);
            }
        }
        return output;
    }
    private Rect pointsToRect(Point p1, Point p2){
        Intersection i1 = intersectionHash.get(hash(xPoints.get(p1.x), yPoints.get(p1.y)));
        Intersection i2 = intersectionHash.get(hash(xPoints.get(p2.x), yPoints.get(p2.y)));
        return new Rect(i1.getX() - halfPathWidth, i1.getY() - halfPathWidth, i2.getX() + halfPathWidth, i2.getY() + halfPathWidth);
    }
    private String hash(int x, int y){
        return "" + x +"" + y;
    }

}
