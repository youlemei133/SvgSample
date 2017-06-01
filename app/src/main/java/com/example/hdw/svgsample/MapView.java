package com.example.hdw.svgsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Administrator on 2017/06/01 0001.
 */

public class MapView extends View {
    private Context mContext;
    private Handler mHandler;
    private Paint mPaint;
    private float mScale;
    private List<PathBean> pathList;
    private PathBean touchPathBean;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        mScale = 1.3f;
        pathList = new ArrayList<>();

        mHandler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    invalidate();
                }
            }
        };

    }

    public void setSvgSource(final int svgSource){
        if(pathList!=null)
            pathList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                parse(svgSource);
                mHandler.obtainMessage(1).sendToTarget();
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScale, mScale);
        for (PathBean bean : pathList) {
            if (bean != touchPathBean)
                bean.drawNormal(canvas, mPaint);
        }
        if (touchPathBean != null)
            touchPathBean.drawSelected(canvas, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetectorCompat.onTouchEvent(event);
    }

    private void parse(int svgSource) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            InputStream is = getResources().openRawResource(svgSource);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("path");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String pathData = element.getAttribute("android:pathData");
                Path path = PathParser.createPathFromPathData(pathData);
                int color = 0;
                switch (i % 4) {
                    case 0:
                        color = ContextCompat.getColor(mContext, R.color.colorMap1);
                        break;
                    case 1:
                        color = ContextCompat.getColor(mContext, R.color.colorMap2);
                        break;
                    case 2:
                        color = ContextCompat.getColor(mContext, R.color.colorMap3);
                        break;
                    case 3:
                        color = ContextCompat.getColor(mContext, R.color.colorMap4);
                        break;
                }
                PathBean bean = new PathBean(path, color);
                pathList.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    GestureDetectorCompat mGestureDetectorCompat = new GestureDetectorCompat(mContext,
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    boolean flag = false;
                    for (PathBean bean : pathList) {
                        boolean isTouch = bean.isTouch((int) (e.getX() / mScale), (int) (e.getY() / mScale));
                        if (isTouch) {
                            if (flag = (touchPathBean != bean))
                                touchPathBean = bean;
                            break;
                        }
                    }

                    if (flag)
                        postInvalidate();
                    return true;
                }
            });

}
