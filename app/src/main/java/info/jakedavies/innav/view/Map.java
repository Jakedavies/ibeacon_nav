package info.jakedavies.innav.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jakedavies on 15-11-01.
 */
public class Map extends View {

    int width;
    int height;

    public Map(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private void init(){
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //
    }
}
