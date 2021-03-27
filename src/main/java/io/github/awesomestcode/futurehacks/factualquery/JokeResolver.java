package io.github.awesomestcode.futurehacks.factualquery;

import io.github.awesomestcode.futurehacks.BaseAnswerResolver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class JokeResolver implements BaseAnswerResolver {

    private static JokeResolver instance;

    public static JokeResolver getInstance() {
        if(instance == null) instance = new JokeResolver();
        return instance;
    }

    public String resolve(String query) {
        FileReader fin = null;
        try {
            fin = new FileReader("jokes.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Something went wrong. Please try again";
        }
        Scanner src = new Scanner(fin);

        double random = Math.random() * 21;
        double num = Math.floor(random);
        if (num == 0) {
            random = Math.random() * 21;
            num = Math.floor(random);
        }

        int a = 0;
        String line = "";

        while (a != num) {
            line = src.nextLine();
            a++;
        }
        return line;
    }
}
