package io.github.awesomestcode.futurehacks.factualquery;

import io.github.awesomestcode.futurehacks.BaseAnswerResolver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JokeResolver implements BaseAnswerResolver {

    private static JokeResolver instance;

    public static JokeResolver getInstance() {
        if(instance == null) instance = new JokeResolver();
        return instance;
    }

    public String resolve(String query) {
        FileReader fin = null;

        URL url = this.getClass().getClassLoader().getResource("jokes.txt");
        Path path = null;
        List<String> lines;
        try {
            final Map<String, String> env = new HashMap<>();
            final String[] array = url.toString().split("!");
            final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
            path = fs.getPath(array[1]);
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "Uh oh. An unknown error occurred. Please try again.";
        }

        double random = Math.random() * 21;
        double num = Math.floor(random);
        if (num == 0) {
            random = Math.random() * 21;
            num = Math.floor(random);
        }

        int a = 0;
        return lines.get((int) num);
    }
}
