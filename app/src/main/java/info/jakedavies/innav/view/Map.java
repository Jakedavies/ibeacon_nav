package info.jakedavies.innav.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


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
    long lastUpdate;
    private long updateFrequency = 100;

    public Map(Context context) {
        super(context);
        Log.d("HALP", "startng");
        paint = new Paint();
        paint.setColor(Color.BLACK);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        lastUpdate = System.currentTimeMillis();
        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        init();
    }

    private void init(){
        int tenPercentWide = width/10;
        int tenPercentHigh = height/10;
        obstacles.add(new Rect(2 * tenPercentWide, 3 * tenPercentHigh, 4 * tenPercentWide, 4 * tenPercentHigh));
        obstacles.add(new Rect(5 *tenPercentWide, 3*tenPercentHigh, 7*tenPercentWide, 4*tenPercentHigh));
        obstacles.add(new Rect(8 * tenPercentWide, 3 * tenPercentHigh, 9 * tenPercentWide, 4 * tenPercentHigh));

        obstacles.add(new Rect(2*tenPercentWide, 6*tenPercentHigh, 4*tenPercentWide, 7*tenPercentHigh));
        obstacles.add(new Rect(5 * tenPercentWide, 6 * tenPercentHigh, 7 * tenPercentWide, 7 * tenPercentHigh));
        obstacles.add(new Rect(8 * tenPercentWide, 6 * tenPercentHigh, 9 * tenPercentWide, 7 * tenPercentHigh));

        obstacles.add(new Rect(2 * tenPercentWide, 0 * tenPercentHigh, 10 * tenPercentWide, 2 * tenPercentHigh));
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(Rect obstacle : obstacles){
            canvas.drawRect(obstacle, paint);
        }
        canvas.drawCircle(width/10 *5, height/10 *9, width/20, paint2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
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
