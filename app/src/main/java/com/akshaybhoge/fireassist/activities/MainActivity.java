package com.akshaybhoge.fireassist.activities;


import android.content.Intent;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;

import com.akshaybhoge.fireassist.R;

/**
 * Created by akshaybhoge on 5/7/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CompassActivity";

    private Compass compass;

    private FloatingActionButton flashButton;
    private Camera caml;
    Camera.Parameters params;
    private boolean isOn;

    private AppCompatImageButton appCompatImageButtonCall;

    private AppCompatImageButton appCompatImageButtonShare;
    Intent shareIntent;

    private AppCompatImageButton appCompatImageButtonSpeaker;

    private GpsTool gpsTool;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caml= Camera.open();

        compass = new Compass(this);
        compass.appCompatImageView= (AppCompatImageView) findViewById(R.id.appCompatImageViewArrow);

        if (gpsTool == null) {
            gpsTool = new GpsTool(this);
        }

        Location location = gpsTool.getLocation();
        Double myLatitude = location.getLatitude();
        Double myLongitude = location.getLongitude();
        Double myAltitude = location.getAltitude();
        StringBuilder msg = new StringBuilder();
        msg.append("Longitude:").append(myLongitude).append(". ");
        msg.append("Latitude:").append(myLatitude).append(". ");
        msg.append("Altitude:").append(myAltitude).append(". ");

        initViews();
        initListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();

    }


    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
        }

    private void initViews() {

        flashButton = (FloatingActionButton) findViewById(R.id.flashButton);
        appCompatImageButtonCall = (AppCompatImageButton) findViewById(R.id.appCompatImageButtonCall);
        appCompatImageButtonShare = (AppCompatImageButton) findViewById(R.id.appCompatImageButtonShare);
        appCompatImageButtonSpeaker = (AppCompatImageButton) findViewById(R.id.appCompatImageButtonSpeaker);
    }

    private void initListeners() {
        flashButton.setOnClickListener(this);
        appCompatImageButtonCall.setOnClickListener(this);
        appCompatImageButtonShare.setOnClickListener(this);
        appCompatImageButtonSpeaker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flashButton:
                if(isOn){
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    caml.setParameters(params);
                    caml.stopPreview();
                    isOn=false;
                }
                else{
                    params=caml.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    caml.setParameters(params);
                    caml.stopPreview();
                    isOn=true;
                }
                break;
            case R.id.appCompatImageButtonCall:
                String number ="5512259133";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
                break;
            case R.id.appCompatImageButtonShare:
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Fire Emergency");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Fire Emergency. My Coordinates are:");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.appCompatImageButtonSpeaker:
                break;
        }
    }

    protected double bearing(double startLat, double startLng, double endLat, double endLng){
        double longitude1 = startLng;
        double longitude2 = endLng;
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }
}

