package io.github.awesomestcode.futurehacks;

import io.github.awesomestcode.futurehacks.userinterface.WebInterfaceServer;

import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("DEBUG: Running on Java " + System.getProperty("java.version"));
        System.out.println("DEBUG: Flag \"IgnoreJavaVersion\" is set to " + System.getProperty("IgnoreJavaVersion"));

        if(!System.getProperty("java.version").equals("11.0.10") && System.getProperty("IgnoreJavaVersion") == null) {
            System.err.println("We haven't tested the program on this Java version! We highly recommend you install OpenJDK 11 from here: https://adoptopenjdk.net/. While it should work for the most part, unexpected errors may arise on untested versions.");
            System.out.println("Do you wish to continue (y/n)?");
            String response = scanner.nextLine();
            switch(response) {
                case "n":
                    System.out.println("Exiting...");
                    System.exit(1);
                    break;
                case "y":
                    System.out.println("Proceeding as normal. Please note that any errors that occur are not supported.");
                    System.out.println("Note: to launch as normal, add -DIgnoreJavaVersion to your JVM args.");
                    break;
                default:
                    System.out.println("That's not a valid option. Aborting execution.");
                    System.exit(1);
            }
        }
        System.out.println("Launching Web Server...");
        new Thread(new WebServerLauncher()).start();
        System.out.println("Launched web server.");
        System.out.println("Please input a query:");
        System.out.println(QueryHandler.handleQuery(scanner.nextLine()));
    }
    private static class WebServerLauncher implements Runnable {
        @Override
        public void run() {
            WebInterfaceServer.main();
        }
    }
}
