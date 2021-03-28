package io.github.awesomestcode.futurehacks.userinterface;

public enum ReturnStatus {
    OK("200 OK"),
    UNAVAILABLE("503 Service Unavailable"),
    BAD_REQUEST("400 Bad Request");

    final String code;

    public String toString() {
        return this.code;
    }

    ReturnStatus(String code) {
        this.code = code;
    }
}
