package com.SpringbootApps.weatherApp.Controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@org.springframework.stereotype.Service
public class WeatherService {
    private final OkHttpClient client;
    private Response response;

    //@Value("${openweathermap.apikey}") // Make sure to inject API key through configuration
    private String apiKey;



    public WeatherService() {
        this.client = new OkHttpClient();
    }

    public JSONObject getWeather(String cityName,String apiKey) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q=" + cityName+"&units=metric"+ "&appid=" + apiKey)
                .build();

        response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }

    public JSONArray returnWeatherArray(String cityName,String apiKey) throws IOException {
        JSONArray weatherJsonArray = getWeather(cityName,apiKey).getJSONArray("weather");
        return weatherJsonArray;
    }

    public  JSONObject returnMain(String cityName,String apiKey) throws IOException {
        JSONObject mainOject = getWeather(cityName,apiKey).getJSONObject("main");
        return  mainOject;
    }

    public  JSONObject returnWind(String cityName,String apiKey) throws IOException {
        JSONObject WindOject = getWeather(cityName,apiKey).getJSONObject("wind");
        return  WindOject;
    }

    public  JSONObject returClouds(String cityName,String apiKey) throws IOException {
        JSONObject CloudsOject = getWeather(cityName,apiKey).getJSONObject("clouds");
        return  CloudsOject;
    }

    public int returnTimezone(String cityName, String apiKey) throws IOException {
        return getWeather(cityName, apiKey).getInt("timezone");
    }

    public  JSONObject returnSys(String cityName,String apiKey) throws IOException {
        JSONObject SysOject = getWeather(cityName,apiKey).getJSONObject("sys");
        return  SysOject;
    }

    public String returnCountryName(String cityName, String apiKey) throws IOException {
        return getWeather(cityName, apiKey).getString("name");
    }

    public int returnSunset(String cityName, String apiKey) throws IOException {
        return getWeather(cityName, apiKey).getInt("sunset");
    }

    public int returnSunrise(String cityName, String apiKey) throws IOException {
        return getWeather(cityName, apiKey).getInt("sunrise");
    }





}

