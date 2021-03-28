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

public class IAResolver implements BaseAnswerResolver {

    private static IAResolver instance;

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    public static IAResolver getInstance() {
        if(instance == null) instance = new IAResolver();
        return instance;
    }

    public String resolve(String query) {
        query = query.replace("What's", "What is");
        query = query.replace("define ", "What is ");
        query = query.replace("what's the definition of ", "what is");
        query = URLEncoder.encode(query, Charset.defaultCharset());
        String queryURL = "https://api.duckduckgo.com/?q=" + query + "&format=json&pretty=1&t=hackathonproject&no_redirect=true&no_html=1&skip_disambig=1";
        System.out.println("Query URL: " + queryURL);
        Request ddgRequest = new Request.Builder()
                .url(queryURL)
                .header("Content-Type", "application/json")
                .header("User-Agent", "AwesomestHackathon/1.0 (+https://awesomestcode.github.io/FutureHacksProject)")
                .header("Connection", "Keep-Alive")
                .build();

        try {
            Response response = client.newCall(ddgRequest).execute();
            return (String) gson.fromJson(response.body().string(), TreeMap.class).get("Abstract");
        } catch (IOException e) {
            e.printStackTrace();
            return "An error was encountered while processing the query.";
        }
    }
}
