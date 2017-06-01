package com.example.hdw.svgsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //在设置Button和TextView的Drawable的时候可能会产生一个Bug,所以需要加上这段静态代码
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setSvgSource(R.raw.china);
    }

    public void china(View view){
        mapView.setSvgSource(R.raw.china);
    }
    public void taiwan(View view){
        mapView.setSvgSource(R.raw.taiwan);
    }
}
