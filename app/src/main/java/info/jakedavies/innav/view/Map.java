package info.jakedavies.innav.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import info.jakedavies.innav.lib.FloorplanCamera;
import info.jakedavies.innav.lib.floorplan.Floorplan;


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
    FloorplanCamera camera ;
    private Floorplan fp;
    long lastUpdate;
    private long updateFrequency = 100;

    public Map(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        lastUpdate = System.currentTimeMillis();
        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        init();
    }

    private void init(){
        int fpHeight = 1000;
        int fpWidth = 1000;
        fp = new Floorplan(fpWidth, fpHeight);
        for(int i = 0; i< fpWidth; i = i+50){
            for(int j =0; j < fpWidth; j = j+50){
                if(i%100 == 0 && j % 100 == 0)
                    fp.addObjectToFloorplan(new Rect(i, j, i+50, j+50));
            }
        }
        camera = new FloorplanCamera(fp);
        camera.setViewSize(300);
        camera.setPhonePos(500, 500);

    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(Path obstacle : camera.getCameraView()){
            canvas.drawPath(obstacle, paint);
//            canvas.drawRect(obstacle, paint);
        }
        canvas.drawCircle(width/10 *5, height/10 *9, width/20, paint2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        camera.setPhoneDimensions(widthMeasureSpec, height);
        init();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //based on where this touch event was, we can give correct vibrational feedback to the user using our feedback classes
        if(System.currentTimeMillis() - lastUpdate > updateFrequency){
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if(collidesWithObstacle(e.getX(), e.getY())){
                        vibrator.vibrate(50);
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
