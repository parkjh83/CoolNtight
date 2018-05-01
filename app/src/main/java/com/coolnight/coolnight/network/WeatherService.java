package com.coolnight.coolnight.network;

import com.coolnight.coolnight.network.model.OpenWeatherData;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 주인 on 2018-02-24.
 */

public interface WeatherService {
    // http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid={KEY}
    @GET("weather")
    Flowable<OpenWeatherData> getCurrentLocationWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String key,
            @Query("units") String units,
            @Query("lang") String lang);
}
