package info.jakedavies.innav.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakedavies on 15-11-01.
 */
public class Map extends View {

    int width;
    int height;
    List<Rect> obstacles = new ArrayList<>();
    Paint paint;

    public Map(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    private void init(){
        int tenPercentWide = width/10;
        int tenPercentHigh = width/10;
        obstacles.add(new Rect(2*tenPercentWide, 3*tenPercentHigh, 4*tenPercentWide, 2*tenPercentHigh));
        obstacles.add(new Rect(5*tenPercentWide, 3*tenPercentHigh, 7*tenPercentWide, 2*tenPercentHigh));
        obstacles.add(new Rect(8*tenPercentWide, 3*tenPercentHigh, 9*tenPercentWide, 2*tenPercentHigh));

        obstacles.add(new Rect(2*tenPercentWide, 6*tenPercentHigh, 4*tenPercentWide, 5*tenPercentHigh));
        obstacles.add(new Rect(5*tenPercentWide, 6*tenPercentHigh, 7*tenPercentWide, 5*tenPercentHigh));
        obstacles.add(new Rect(8*tenPercentWide, 6*tenPercentHigh, 9*tenPercentWide, 5*tenPercentHigh));

        obstacles.add(new Rect(2*tenPercentWide, 10*tenPercentHigh, 10*tenPercentWide, 8*tenPercentHigh));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas){
        //
        for(Rect obstacle : obstacles){
            canvas.drawRect(obstacle, paint);
        }
    }
}
