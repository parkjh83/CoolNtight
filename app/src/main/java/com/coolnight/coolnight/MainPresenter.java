package com.coolnight.coolnight;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.coolnight.coolnight.location.LocationManager;
import com.coolnight.coolnight.network.WeatherNetworkManager;
import com.coolnight.coolnight.network.model.OpenWeatherData;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by 주인 on 2018-02-24.
 */

public class MainPresenter implements MainContract {
    private MainView mView;
    private double [] mLocationData = new double[2];

    @Override
    public void refreshWeather(Context context) {
        getLocationInfo(context);
    }

    @Override
    public void attachView(MainView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    private void getWeather() {
        WeatherNetworkManager manager =  WeatherNetworkManager.getmInstance();
        weatherSubject = PublishSubject.create();
        manager.getWeatherLocation(mLocationData[0], mLocationData[1], weatherSubject);

        weatherSubject.subscribe(new Observer<OpenWeatherData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(OpenWeatherData openWeatherData) {
                if (mView != null) {
                    mView.showWeatherInfo(openWeatherData);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mView != null) {
                    mView.showWeatherInfo(null);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    PublishSubject<Location> mLocationSubject;
    PublishSubject<OpenWeatherData> weatherSubject;

    private void getLocationInfo(final Context context) {
        if (LocationManager.getInstance(context).isPossibleLocation()) {
            mLocationSubject = PublishSubject.create();
            mLocationSubject.subscribe(new Observer<Location>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Location location) {
                    mLocationData[0] = location.getLatitude();
                    mLocationData[1] = location.getLongitude();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    getWeather();
                    LocationManager.getInstance(context).stopLocationSearch();
                }
            });
            LocationManager.getInstance(context).setLocationSubject(mLocationSubject);
            LocationManager.getInstance(context).startLocationSearch(context);
        }
    }
}
