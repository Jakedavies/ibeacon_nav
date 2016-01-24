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
    private MapByte[][] map;
    private HashMap<String, Intersection> intersectionHash = new HashMap<>();
    private HashMap<String, Intersection> tempIntersectionHash = new HashMap<>();
    private HashMap<String, Intersection> trueCoordinateHash = new HashMap<>();
    private ArrayList<Integer> xPoints = new ArrayList<Integer>();
    private ArrayList<Integer> yPoints  = new ArrayList<Integer>();;
    private int halfPathWidth =1;
    public PathFinder(ArrayList<Intersection> intersections, MapByte[][] map){
        this.intersections = intersections;
        this.map = map;
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
        ArrayList<Integer> halfXPoints = new ArrayList<Integer>();
        ArrayList<Integer> halfYPoints  = new ArrayList<Integer>();;
        for(int i = 0; i < xPoints.size()-1; i++){
            int distance = xPoints.get(i+1) - xPoints.get(i);
            distance = distance/2;
            halfXPoints.add(xPoints.get(i)+distance);
        }
        for(int i = 0; i < yPoints.size() - 1; i++){
            int distance = yPoints.get(i+1) - yPoints.get(i);
            distance = distance/2;
            halfYPoints.add(yPoints.get(i)+distance);
        }
        xPoints.addAll(halfXPoints);
        yPoints.addAll(halfYPoints);

        Collections.sort(xPoints);
        Collections.sort(yPoints);



        GridCell[][] reducedPointGrid = new GridCell[xPoints.size()][yPoints.size()];
        for(int i = 0; i < reducedPointGrid.length; i++){
            for(int j = 0; j < reducedPointGrid[i].length; j++){
                int xCoord = xPoints.get(i);
                int yCoord = yPoints.get(j);
                reducedPointGrid[i][j] = new GridCell();
                reducedPointGrid[i][j].setX(i);
                reducedPointGrid[i][j].setY(j);
                if(intersectionHash.containsKey(hash(xCoord,yCoord))) {
                    reducedPointGrid[i][j].setWalkable(true);
                } else {
                    // we need to ray trace from point below to point above
                    // and then also from left to right
                    // to see if this point is walkable
                    tempIntersectionHash.put(hash(xCoord, yCoord), new Intersection(xCoord, yCoord, false));
                    // first trace from point x-1 to x +1
                    if(i >= 0 && j >= 0 && j < yPoints.size() && i < xPoints.size() ){
                        int previousX;
                        int nextX;
                        int previousY;
                        int nextY;

                        if(i > 0){
                            previousX = xPoints.get(i-1);
                        } else {
                            previousX = xCoord;
                        }

                        if(i < xPoints.size() -1){
                            nextX = xPoints.get(i+1);
                        } else {
                            nextX = xCoord;
                        }

                        if(j > 0){
                            previousY = yPoints.get(j-1);
                        } else {
                            previousY = yCoord;
                        }
                        if(j < yPoints.size()-1){
                            nextY = yPoints.get(j+1);
                        } else {
                            nextY = yCoord;
                        }

                        boolean xBlocked = false;
                        boolean yBlocked = false;

                        for(int x = previousX; x < nextX; x++){
                            if(!map[x][yCoord].isWalkable()) {
                                xBlocked = true;
                            }
                        }
                        for(int y = previousY; y < nextY; y++){
                            if(!map[xCoord][y].isWalkable()) {
                                yBlocked = true;
                            }
                        }
                        // if both pass, point is walkwable
                        reducedPointGrid[i][j].setWalkable(!(xBlocked && yBlocked));
                    } else {
                        reducedPointGrid[i][j].setWalkable(true);
                    }

                }

            }
        }

        NavigationGrid<GridCell> navGrid = new NavigationGrid(reducedPointGrid);

        //create a finder either using the default options
        //or create your own pathfinder options:
        GridFinderOptions opt = new GridFinderOptions();
        opt.allowDiagonal = false;

        AStarGridFinder<GridCell> finder = new AStarGridFinder(GridCell.class, opt);
        List<GridCell> pathToEnd = finder.findPath(0, 0, xPoints.size()-1, yPoints.size()-1, navGrid);


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
        String p1Hash = hash(xPoints.get(p1.x), yPoints.get(p1.y));
        String p2Hash = hash(xPoints.get(p2.x), yPoints.get(p2.y));
        Intersection i1 = intersectionHash.get(p1Hash);
        Intersection i2 = intersectionHash.get(p2Hash);
        if(i1 == null){
            i1 = tempIntersectionHash.get(p1Hash);
        }
        if(i2 == null){
            i2 = tempIntersectionHash.get(p2Hash);
        }
        return new Rect(i1.getX() - halfPathWidth, i1.getY() - halfPathWidth, i2.getX() + halfPathWidth, i2.getY() + halfPathWidth);
    }
    private String hash(int x, int y){
        return "" + x +"" + y;
    }

}
