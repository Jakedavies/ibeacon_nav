package info.jakedavies.innav.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import info.jakedavies.innav.lib.Camera;


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
    Camera c = new Camera();
    long lastUpdate;
    private long updateFrequency = 100;

    public Map(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        lastUpdate = System.currentTimeMillis();
        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        init();
    }

    private void init(){

    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(Rect r : c.getCameraView()){
            canvas.drawRect(r, paint);
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
