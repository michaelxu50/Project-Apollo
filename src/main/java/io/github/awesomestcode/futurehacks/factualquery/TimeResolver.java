package io.github.awesomestcode.futurehacks.factualquery;

import io.github.awesomestcode.futurehacks.BaseAnswerResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeResolver implements BaseAnswerResolver {

    private static TimeResolver instance;

    public static TimeResolver getInstance() {
        if(instance == null) instance = new TimeResolver();
        return instance;
    }

    public String resolve(String query) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mma");

        String formattedDate = time.format(format);

        return "The time is " + formattedDate;
    }
}
