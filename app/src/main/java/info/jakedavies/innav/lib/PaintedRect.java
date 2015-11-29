package info.jakedavies.innav.lib;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by jakedavies on 15-11-29.
 */
public class PaintedRect {
    Rect r;
    int p;

    public PaintedRect(Rect r, int p){
        this.r = r;
        this.p = p;
    }

    public int getPaint(){
        return p;
    }
    public Rect getRect(){
        return r;
    }
}
