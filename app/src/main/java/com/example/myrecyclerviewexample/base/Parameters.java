package com.example.myrecyclerviewexample.base;

public class Parameters {


    public final static String API = "11beed541e7a0fd48076a05030a042ea";

    public final static String LANG = "es";
    public final static String UNITS = "metric";
    public static String IP = "192.168.1.36";
    public static String PORT = "8080";
    public static String PREFIX = IP + ":" + PORT;

    public final static String URL = "http://" + PREFIX + "/api/";
    public final static String URL_OPTIONS = "forecast?appid=" + API + "&lang=" + LANG + "&units=" + UNITS;

    public final static String ICON_URL_PRE = "http://openweathermap.org/img/wn/";
    public static final String ICON_URL_POST = "@2x.png";

}
