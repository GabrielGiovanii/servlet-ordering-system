package com.servlet_ordering_system.config.enums;

public enum HttpVerb {

    GET(1),
    POST(2),
    PUT(3),
    DELETE(4);

    private final int code;

    private HttpVerb(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static HttpVerb valueOf(int code) {
        for(HttpVerb httpVerb : HttpVerb.values()) {
            if(httpVerb.getCode() == code)
                return httpVerb;
        }

        return null;
    }
}
