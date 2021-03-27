package io.github.awesomestcode.futurehacks;

import io.github.awesomestcode.futurehacks.factualquery.IAResolver;

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
        mathKeywords.add("to the power");
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

        if (test.contains("define")) return QueryType.DICTIONARY;

        // time, date, and weather
        if (test.contains("time")) return QueryType.TIME;
        if (test.contains("date")) return QueryType.DATE;
        if (test.contains("jokes")) return QueryType.JOKE;
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
        QueryType queryType = categorise(query);
        //noinspection SwitchStatementWithTooFewBranches
        switch(queryType) {
            case INFO:
                return IAResolver.getInstance().resolve(query);
            default:
                throw new RuntimeException("Hmm, something went wrong. Received an invalid query type");
        }
    }
}
