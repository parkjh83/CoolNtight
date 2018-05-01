package com.coolnight.coolnight.location;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.coolnight.coolnight.CoolNightApplication;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class LocationManager {
    private static LocationManager mInstance;
    private android.location.LocationManager mLocationManager;
    private PublishSubject<Location> mLocationChangeSubject;

    private LocationManager(Context context) {
        mLocationManager = (android.location.LocationManager) context.getSystemService((Context.LOCATION_SERVICE));
    }

    public static LocationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationManager(context);
        }
        return mInstance;
    }

    public boolean isPossibleLocation() {
        return mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
    }

    public void setLocationSubject(PublishSubject<Location> subject) {
        mLocationChangeSubject = subject;
    }

    public void startLocationSearch(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<String> providers = mLocationManager.getProviders(true);
        for (String provider : providers) {
            mLocationManager.requestLocationUpdates(provider, 1000, 500, mLocationListener);
        }
    }

    public void stopLocationSearch() {
        mLocationManager.removeUpdates(mLocationListener);
    }

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mLocationChangeSubject != null) {
                mLocationChangeSubject.onNext(location);
                mLocationChangeSubject.onComplete();
            }
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
    };
}
