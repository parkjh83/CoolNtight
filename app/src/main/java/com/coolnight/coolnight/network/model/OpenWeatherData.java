package com.coolnight.coolnight.network.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

public class OpenWeatherData {
    public Coord coord;
    public Sys sys;
    public ArrayList<Weather> weather;
    public Main main;
    public Wind wind;
    public JSONObject rain;
    public JSONObject clouds;
    public long dt;
    public long id;
    public String name;
    public long cod;

    public class Coord{
        public double lon;
        public double lat;
    }

    public class Sys {
        public String country;
        public long sunrise;
        public long sunset;
    }

    public class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public class Main {
        public double temp;
        public double humidity;
        public int pressure;
        public double temp_min;
        public double temp_max;
    }

    public class Wind {
        public double speed;
        public double deg;
    }

    public class Rain {
//        (@SerializedName("3h"))
//        public String a3h;
    }
}
