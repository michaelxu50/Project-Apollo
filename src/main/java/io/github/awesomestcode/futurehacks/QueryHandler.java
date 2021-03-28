package io.github.awesomestcode.futurehacks;

import io.github.awesomestcode.futurehacks.factualquery.*;

import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

public class QueryHandler {
    private static final HashSet<String> mathKeywords = new HashSet<>();
    static {
        mathKeywords.add("add");
        mathKeywords.add("plus");
        mathKeywords.add("sum");
        mathKeywords.add("subtract");
        mathKeywords.add("minus");
        mathKeywords.add("difference");
        mathKeywords.add("multiply");
        mathKeywords.add("multiplied");
        mathKeywords.add("times");
        mathKeywords.add("divide");
        mathKeywords.add("divided");
        mathKeywords.add("power");
    }
    private static QueryType categorise(String query) {
        String test = query.toLowerCase();

        // math operation
        if (containsNumber(test)) {
            StringTokenizer stringTokenizer = new StringTokenizer(test);
            while(stringTokenizer.hasMoreTokens()) {
                if(mathKeywords.contains(stringTokenizer.nextToken())) return QueryType.CALCULATION;
            }
        }

        if (test.contains("define")) return QueryType.INFO; //no time to properly implement joking

        // time, date, and weather
        if (test.contains("time")) return QueryType.TIME;
        if (test.contains("date")) return QueryType.DATE;
        if (test.contains("jokes") || test.contains("joke")) return QueryType.JOKE;
        if (test.contains("weather") || test.contains("forecast")) return QueryType.WEATHER;

        //general questions
        if (test.contains("where")
                || test.contains("what")
                || test.contains("who")
                || test.contains("when")
                || test.contains("why")
                || test.contains("how")
                || test.contains("is")) return QueryType.INFO;
        return QueryType.DEFAULT;
    }

    //checks if var is a number or not
    private static boolean containsNumber(String string) {
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character > '0' && character < '9') {
                return true;
            }
        }
        return false;
    }
    public static String handleQuery(String query) {
        query = query.replace("?", " ")
                .replace(".", " ")
                .replace("!", " ")
                .replace(";", " ")
                .replace("\"", " ")
                .replace(":", " ")
                .replace("squared", "to the power of 2")
                .replace("cubed", "to the power of 3");
        QueryType queryType = categorise(query);
        //noinspection SwitchStatementWithTooFewBranches
        System.out.println("DEBUG Query Type: " + queryType);
        switch(queryType) {
            case INFO:
                return IAResolver.getInstance().resolve(query);
            case CALCULATION:
                return CalculationResolver.getInstance().resolve(query);
            case JOKE:
                return JokeResolver.getInstance().resolve(query);
            case WEATHER:
                return WeatherResolver.getInstance().resolve(query);
            case DATE:
                return DateResolver.getInstance().resolve(query);
            case TIME:
                return TimeResolver.getInstance().resolve(query);
            default:
                throw new RuntimeException("Hmm, something went wrong. Received an invalid query type");
        }
    }
}
