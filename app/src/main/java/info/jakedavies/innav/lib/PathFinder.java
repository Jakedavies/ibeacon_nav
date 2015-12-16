package info.jakedavies.innav.lib;

import android.graphics.Point;
import android.util.Log;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakedavies on 2015-12-13.
 */
public class PathFinder {
    private ArrayList<Point> path;
    private GridCell[][] cells;
    private MapByte[][] map;
    AStarGridFinder<GridCell> finder;

    private Point current;
    private Point destination;
    private List<GridCell> pathToEnd;
    private NavigationGrid<GridCell> navGrid;


    public PathFinder(MapByte[][] map, Point current, Point destination){
        // init map components
        this.map = map;
        this.path = new ArrayList<Point>();
        this.cells = new GridCell[map.length][map[0].length];
        this.current = current;
        this.destination = destination;
        this.finder = new AStarGridFinder(GridCell.class);
        convertCells();
        buildPath();
    }

    private void convertCells(){
        for(int i =0; i <  map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                Log.d("DEBUG", "adding cell" + i +" : " +  j);
                cells[i][j] = new GridCell(i, j, map[i][j].isObstacle());
            }
        }
        navGrid = new NavigationGrid(cells);
    }

    private void buildPath(){
        finder.findPath(current.x, current.y, destination.x, destination.y,navGrid);
    }
    public List<GridCell> getPath(){
        return pathToEnd;
    };

}
