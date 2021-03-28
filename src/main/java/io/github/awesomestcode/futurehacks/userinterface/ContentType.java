package io.github.awesomestcode.futurehacks.userinterface;

public enum ContentType {
    HTML("text/html"),
    PLAIN_TEXT("text/plain");

    final String mimeType;

    public String toString() {
        return this.mimeType;
    }

    ContentType(String mimeType) {
        this.mimeType = mimeType;
    }
}
