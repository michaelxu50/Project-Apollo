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

    private static List<String> lines;

    private static JokeResolver instance;

    private static boolean jokesDisabled;

    public static JokeResolver getInstance() {
        if(instance == null) instance = new JokeResolver();
        return instance;
    }

    static {
        URL url = JokeResolver.class.getClassLoader().getResource("jokes.txt");
        Path path = null;

        try {
            final Map<String, String> env = new HashMap<>();
            final String[] array = url.toString().split("!");
            final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
            path = fs.getPath(array[1]);
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something seriously went wrong when loading the jokes. Maybe we could say that a joker messed it up? Anyways, please contact us for help");
            jokesDisabled = true;
        }
    }

    public String resolve(String query) {

        if(jokesDisabled) return "There was an error while loading jokes; as such, jokes have been disabled. Try restarting the program or switching computers.";

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
