package io.github.awesomestcode.futurehacks.factualquery;

import com.google.gson.Gson;
import io.github.awesomestcode.futurehacks.BaseAnswerResolver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.TreeMap;

public class WeatherResolver implements BaseAnswerResolver {

    private static WeatherResolver instance;

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    public static WeatherResolver getInstance() {
        if(instance == null) instance = new WeatherResolver();
        return instance;
    }

    public String resolve(String query) {
        query = URLEncoder.encode(query, Charset.defaultCharset());
        String queryURL = "https://api.openweathermap.org/data/2.5/weather?q=New York City&appid=0a5798ac354b656fbbe8d81785084e3c";
        @SuppressWarnings("DuplicatedCode")
        Request weatherRequest = new Request.Builder()
                .url(queryURL)
                .header("Content-Type", "application/json")
                .header("User-Agent", "AwesomestHackathon/1.0 (+https://awesomestcode.github.io/FutureHacksProject)")
                .header("Connection", "Keep-Alive")
                .build();

        try {
            Response response = client.newCall(weatherRequest).execute();
            return gson.fromJson(response.body().string(), QueryResponse.class).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error was encountered while processing the query.";
        }
    }
    private static class QueryResponse {
        MainResponse main;
        WeatherResponse[] weather;
        private class MainResponse {
            float temp;
            float feels_like;
            float humidity;
        }
        private class WeatherResponse {
            String main;
            String description;
        }
        String name;
        @Override
        public String toString() {
            return "In " + name + ", there are " + weather[0].description.replace("sky", "skies") + ". It's currently " + Math.round((main.temp - 273.15)) + "°C outside and it feels like " + Math.round((main.feels_like - 273.15)) + "°C. The humidity is currently " + main.humidity +"%.";
        }
    }
}
