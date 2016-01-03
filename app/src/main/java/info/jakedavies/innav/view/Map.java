package info.jakedavies.innav.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import info.jakedavies.innav.lib.Camera;
import info.jakedavies.innav.lib.PaintedRect;


/**
 * Created by jakedavies on 15-11-01.
 */
public class Map extends View{

    int width;
    int height;
    List<Rect> obstacles = new ArrayList<>();
    Paint paint;
    Paint paint2;
    Context context;
    Vibrator vibrator;
    info.jakedavies.innav.lib.map.Map map;
    Camera c;
    long lastUpdate;
    private long updateFrequency = 100;
    Paint[] paints = new Paint[4];

    public Map(Context context) {
        super(context);

        // initialize the map
        map = new info.jakedavies.innav.lib.map.Map();
        map.buildFloorPlan();
        c = new Camera(map);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        paints[0] = new Paint();
        paints[0].setColor(Color.GREEN);
        paints[0].setAlpha(50);
        paints[0].setStyle(Paint.Style.FILL);
        paints[0].setStrokeWidth(5);

        paints[1] = new Paint();
        paints[1].setColor(Color.BLACK);
        paints[1].setAlpha(50);
        paints[1].setStyle(Paint.Style.FILL);
        paints[1].setStrokeWidth(5);

        paints[2] = new Paint();
        paints[2].setColor(Color.RED);
        paints[2].setAlpha(50);
        paints[2].setStyle(Paint.Style.FILL);
        paints[2].setStrokeWidth(5);

        paints[3] = new Paint();
        paints[3].setColor(Color.BLUE);
        paints[3].setAlpha(50);
        paints[3].setStyle(Paint.Style.FILL);
        paints[3].setStrokeWidth(5);

        lastUpdate = System.currentTimeMillis();
        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        init();
    }

    private void init(){

    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(PaintedRect r : c.getCameraView()){
            canvas.drawRect(r.getRect(), paints[r.getPaint()]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        c.setPhonePixels(xNew, yNew);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //based on where this touch event was, we can give correct vibrational feedback to the user using our feedback classes
        if(System.currentTimeMillis() - lastUpdate > updateFrequency){
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    Point p = c.getPointInMapCoords(Math.round(e.getX()), Math.round(e.getY()));
                    int vibration = map.getVibrationForPoint(p);
                    if(vibration > 0){
                        lastUpdate = System.currentTimeMillis();
                        vibrator.vibrate(vibration);
                    }
                    break;
                default:
                    //idk
                    break;
            }
        }
        return true;
    }
    public boolean translateToPosition(int degrees){
        c.setPhoneRotation(degrees);
        this.invalidate();
        return true;
    }
    private boolean collidesWithObstacle(float x, float y){
        for(Rect obstacle : obstacles){
            if(obstacle.contains((int)x, (int)y)){
                return true;
            }
        }
        return false;
    }
}
