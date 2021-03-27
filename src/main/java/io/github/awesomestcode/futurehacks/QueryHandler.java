package io.github.awesomestcode.futurehacks;

import io.github.awesomestcode.futurehacks.factualquery.IAResolver;

public class QueryHandler {
    private static QueryType categorise(String query) {
        return QueryType.INFO;
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
