package io.github.awesomestcode.futurehacks.factualquery;

import io.github.awesomestcode.futurehacks.BaseAnswerResolver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class IAResolver implements BaseAnswerResolver {

    private static IAResolver instance;

    OkHttpClient client = new OkHttpClient();

    public static IAResolver getInstance() {
        if(instance == null) instance = new IAResolver();
        return instance;
    }

    @Override
    public String resolve(String query) {
        query = URLEncoder.encode(query, Charset.defaultCharset());
        String queryURL = "https://api.duckduckgo.com/?q=" + query + "&format=json&pretty=1&t=hackathonproject";
        System.out.println("Query URL: " + queryURL);
        Request ddgRequest = new Request.Builder()
                .url(queryURL)
                .header("Content-Type", "application/json")
                .header("User-Agent", "AwesomestHackathon/1.0 (+https://awesomestcode.github.io/FutureHacksProject)")
                .header("Connection", "Keep-Alive")
                .build();

        try {
            Response response = client.newCall(ddgRequest).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error was encountered while processing the query.";
        }
    }
}
