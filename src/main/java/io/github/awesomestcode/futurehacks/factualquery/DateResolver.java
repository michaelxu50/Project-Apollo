package io.github.awesomestcode.futurehacks.factualquery;

import io.github.awesomestcode.futurehacks.BaseAnswerResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateResolver implements BaseAnswerResolver {

    private static DateResolver instance;

    public static DateResolver getInstance() {
        if(instance == null) instance = new DateResolver();
        return instance;
    }

    @Override
    public String resolve(String query) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formated = DateTimeFormatter.ofPattern("EEEE MMMM d u");

        String formattedDate = date.format(formated);

        return "The date is " + formattedDate;
    }
}
