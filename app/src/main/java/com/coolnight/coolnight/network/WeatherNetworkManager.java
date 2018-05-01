package com.coolnight.coolnight.network;

import android.annotation.SuppressLint;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.coolnight.coolnight.Constants;
import com.coolnight.coolnight.network.model.OpenWeatherData;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import retrofit2.Retrofit;

/**
 * Created by 주인 on 2018-02-24.
 */

public class WeatherNetworkManager {
    private final String WEATHER_MAIN_DOMAIN = "http://api.openweathermap.org/data/2.5/";
    private static WeatherNetworkManager mInstance = null;
    private Retrofit mRetrofit;
    private WeatherService mService;

    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private WeatherNetworkManager() {
        mRetrofit = new RetrofitHelper().createRetrofit(WEATHER_MAIN_DOMAIN);
        mService = mRetrofit.create(WeatherService.class);
    }

    public static WeatherNetworkManager getmInstance() {
        if (mInstance == null) {
            mInstance = new WeatherNetworkManager();
        }
        return mInstance;
    }

    public void getWeatherLocation(double lat, double lon, final Subject<OpenWeatherData> listener) {
        if (mService == null) {
            return ;
        }

        mService.getCurrentLocationWeather(lat, lon, Constants.OPENWEATHERMAP_KEY, Constants.OPENWEATHERMAP_UNIT, Constants.OPENWEATHERMAP_LANG).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(openWeatherData -> {
                    if (listener != null) {
                        listener.onNext(openWeatherData);
                        listener.onComplete();
                    }
                }, e -> {
                    int a = 0;
                    e.printStackTrace();
                });
    }
}