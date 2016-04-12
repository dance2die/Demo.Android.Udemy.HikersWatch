package com.dance2die.demoandroidudemyhikerswatch;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private String provider;

    private TextView latTV;
    private TextView lngTV;
    private TextView accuracyTV;
    private TextView speedTV;
    private TextView bearingTV;
    private TextView altitudeTV;
    private TextView addressTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        latTV = (TextView) findViewById(R.id.lat);
        lngTV = (TextView) findViewById(R.id.lng);
        accuracyTV = (TextView) findViewById(R.id.accuracy);
        speedTV = (TextView) findViewById(R.id.speed);
        bearingTV = (TextView) findViewById(R.id.bearing);
        altitudeTV = (TextView) findViewById(R.id.altitude);
        addressTV = (TextView) findViewById(R.id.address);

        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();
        Float bearing = location.getBearing();
        Float speed = location.getSpeed();
        Float accuracy = location.getAccuracy();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0){
                Log.i("Place Info", addresses.get(0).toString());

                String addressHolder = "";
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++){
                    addressHolder += addresses.get(0).getAddressLine(i) + "\n";
                }

                addressTV.setText("Address:\n" + addressHolder);
            } else {
                addressTV.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        latTV.setText("Latitude: " + lat);
        lngTV.setText("Longitude: " + lng);
        altitudeTV.setText("Altitude: " + alt + "m");
        bearingTV.setText("Bearing: " + bearing);
        speedTV.setText("Speed: " + speed + "m/s");
        accuracyTV.setText("Accuracy: " + accuracy + "m");

        Log.i("log:Lat", lat.toString());
        Log.i("log:Lng", lng.toString());
        Log.i("log:alt", alt.toString());
        Log.i("log:bearing", bearing.toString());
        Log.i("log:speed", speed.toString());
        Log.i("log:accuracy", accuracy.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
