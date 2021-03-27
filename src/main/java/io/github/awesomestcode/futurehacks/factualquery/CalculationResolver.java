package io.github.awesomestcode.futurehacks.factualquery;

import io.github.awesomestcode.futurehacks.BaseAnswerResolver;

import java.util.StringTokenizer;

public class CalculationResolver implements BaseAnswerResolver {
    private static CalculationResolver instance;

    public static CalculationResolver getInstance() {
        if(instance == null) instance = new CalculationResolver();
        return instance;
    }

    public String resolve(String query) {
        int[] operands = getNumber(query);
        int answer = 1;
        if (query.contains("add") || query.contains("sum") || query.contains("plus")) {
            System.out.println("DEBUG: RUNNING SUM");
            answer = operands[0] + operands[1];
        }
        if (query.contains("subtract") || query.contains("minus") || query.contains("difference")) {
            System.out.println("DEBUG: RUNNING SUBTRACT");
            answer = operands[0] - operands[1];
        }
        if (query.contains("multiply") || query.contains("multiplied") || query.contains("times")) {
            System.out.println("DEBUG: RUNNING MULTIPLICATION");
            answer = operands[0] * operands[1];
        }
        if (query.contains("divide")) {
            System.out.println("DEBUG: RUNNING DIVISION");
            if(operands[1] == 0) return "You can't divide by 0 (or, maybe the answer is ∞)?!";
            answer = operands[0] / operands[1];
        }
        if (query.contains("to the power of")) {
            System.out.println("DEBUG: RUNNING EXPONENT");
            answer = (int) Math.pow(operands[0], operands[1]);
        }
        return Integer.toString(answer);
    }
    private static int[] getNumber(String string) {
        String holder = "";
        int i = 0;
        int[] num = new int[2];
        StringTokenizer st = new StringTokenizer(string);
        try {
            while (st.hasMoreTokens()) {
                holder = st.nextToken();
                try {
                    num[i] = Integer.parseInt(holder);
                    i++;
                }
                catch (NumberFormatException e) {
                    continue;
                }
                if (i==1) {
                    num[1] = 0;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Please do not exceed 2 numbers.");
            System.exit(1);
        }
        return num;
    }
}
