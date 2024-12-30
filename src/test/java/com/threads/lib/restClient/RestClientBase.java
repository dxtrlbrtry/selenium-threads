package com.threads.lib.restClient;

import java.net.http.HttpClient;

public abstract class RestClientBase {
    protected final HttpClient client;
    protected final String baseUrl;

    protected RestClientBase(String baseUrl) {
        this.client = HttpClient.newBuilder().build();
        this.baseUrl = baseUrl;
    }

}
