package io.github.awesomestcode.futurehacks.userinterface;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class RequestReturn {
    public final static String SERVER_BRAND =  "Server: Internal HTTP System : 1.0";
    private ContentType contentType;
    private byte[] content;
    private ReturnStatus returnStatus;

    public RequestReturn(ContentType contentType, String content, ReturnStatus returnStatus) {
        this(contentType, content.getBytes(StandardCharsets.US_ASCII), returnStatus);
    }
    public RequestReturn(@NotNull ContentType contentType, @NotNull byte[] content, ReturnStatus returnStatus) {
        if(contentType == null || content == null) throw new NullPointerException("Null was passed in here.. not sure how");
        this.contentType = contentType;
        this.content = content;
        this.returnStatus = returnStatus;
    }

    private RequestReturn() {}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("HTTP/1.1 ").append(returnStatus).append("\n")
                .append(SERVER_BRAND).append("\n")
                .append("Content-type: ").append(contentType).append("\n")
                 .append("Content-length: ").append(content.length).append("\n\n")
                .append(new String(content, StandardCharsets.US_ASCII));
        return builder.toString();
    }
}
