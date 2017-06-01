package com.example.hdw.svgsample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by Administrator on 2017/06/01 0001.
 *
 */

public class PathBean {
    private Path path;
    private int color;

    public PathBean(Path path, int color) {
        this.path = path;
        this.color = color;
    }

    public void drawNormal(Canvas canvas, Paint paint){
        paint.clearShadowLayer();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path,paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawPath(path,paint);
    }

    public void drawSelected(Canvas canvas, Paint paint){
        paint.clearShadowLayer();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShadowLayer(8,0,0,Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawPath(path,paint);

        paint.clearShadowLayer();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawPath(path,paint);
    }

    public boolean isTouch(int x,int y){
        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        Region region = new Region();
        region.setPath(path,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        return region.contains(x,y);
    }

}
