package service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String apiKey;
    private final String baseUrl;

    public KVTaskClient(String url) throws URISyntaxException, IOException, InterruptedException {
        baseUrl = url;
        client = HttpClient.newHttpClient();
        apiKey = register();
    }

    private String register() throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder(new URI(baseUrl + "/register")).GET().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void put(String key, String value) throws URISyntaxException, IOException, InterruptedException {
        var body = HttpRequest.BodyPublishers.ofString(value);
        var request = HttpRequest.newBuilder(new URI(baseUrl + "/put/" + key + "?API_KEY=" + apiKey)).POST(body).build();
        var response = client.send(request, HttpResponse.BodyHandlers.discarding());

    }


    public String load(String key) throws URISyntaxException, IOException, InterruptedException {
        var request = HttpRequest.newBuilder(new URI(baseUrl + "/load/" + key + "?API_KEY=" + apiKey)).GET().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
