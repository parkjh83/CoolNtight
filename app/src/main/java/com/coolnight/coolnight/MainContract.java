package com.coolnight.coolnight;

import android.content.Context;

import com.coolnight.coolnight.network.model.OpenWeatherData;

/**
 * Created by 주인 on 2018-02-24.
 */

public interface MainContract {
    public interface  MainView {
        void showWeatherInfo(OpenWeatherData weather);
    }

    void refreshWeather(Context context);
    void attachView(MainView view);
    void detachView();

}
