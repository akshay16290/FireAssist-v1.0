package com.akshaybhoge.fireassist.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.akshaybhoge.fireassist.R;
import com.akshaybhoge.fireassist.helper.InputValidation;
import com.akshaybhoge.fireassist.model.Place;
import com.akshaybhoge.fireassist.sql.DatabaseHelperPlace;
import com.akshaybhoge.fireassist.sql.DatabaseHelperUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by akshaybhoge on 5/1/17.
 */

public class SaveLocationActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private NestedScrollView nestedScrollView;

    private final AppCompatActivity activity = SaveLocationActivity.this;

    private TextView textViewLatitude;
    private TextView textViewLongitude;
    private TextView textViewAltitude;

    private TextInputLayout textInputLayoutPlaceName;

    private TextInputEditText textInputEditTextPlaceName;

    private AppCompatButton appCompatButtonSave;
    private AppCompatButton appCompatButtonDone;

    private InputValidation inputValidation;
    public DatabaseHelperPlace databaseHelperPlace;
    public Place place;

    private GpsTool gpsTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);

        textViewLatitude = (TextView) this.findViewById(R.id.textViewLatitude);
        textViewLongitude = (TextView) this.findViewById(R.id.textViewLongitude);
        textViewAltitude = (TextView) this.findViewById(R.id.textViewAltitude);

        if (gpsTool == null) {
            gpsTool = new GpsTool(this);
        }

        Location location = gpsTool.getLocation();

        textViewLatitude.setText(String.valueOf(location.getLatitude()));
        textViewLongitude.setText(String.valueOf(location.getLongitude()));
        textViewAltitude.setText(String.valueOf(location.getAltitude()));

        DatabaseHelperUser AddressDatabaseHelperUser = new DatabaseHelperUser(this);
        SQLiteDatabase db = AddressDatabaseHelperUser.getReadableDatabase();


        initViews();
        initListeners();
        initObjects();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gpsTool.startGpsUpdate();
    }


    @Override
    protected void onStop() {
        super.onStop();
        gpsTool.stopGpsUpdates();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonSave:
                postDataToSQLite();
                break;
            case R.id.appCompatButtonDone:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPlaceName, textInputLayoutPlaceName, getString(R.string.error_message_place))) {
            return;
        }

        if (!databaseHelperPlace.checkPlace(textInputEditTextPlaceName.getText().toString().trim())) {

            place.setLatitude(textViewLatitude.getText().toString().trim());
            place.setLongitude(textViewLongitude.getText().toString().trim());
            place.setPlaceName(textInputEditTextPlaceName.getText().toString().trim());

            databaseHelperPlace.addPlace(place);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message_Location), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();

        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_location_save), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        textInputEditTextPlaceName.setText(null);
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutPlaceName = (TextInputLayout) findViewById(R.id.textInputLayoutPlaceName);

        textInputEditTextPlaceName = (TextInputEditText) findViewById(R.id.textInputEditTextPlaceName);

        appCompatButtonSave = (AppCompatButton) findViewById(R.id.appCompatButtonSave);
        appCompatButtonDone = (AppCompatButton) findViewById(R.id.appCompatButtonDone);
    }

    private void initListeners() {
        appCompatButtonSave.setOnClickListener(this);
        appCompatButtonDone.setOnClickListener(this);
    }

    public void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelperPlace = new DatabaseHelperPlace(activity);
        place = new Place();
    }


}