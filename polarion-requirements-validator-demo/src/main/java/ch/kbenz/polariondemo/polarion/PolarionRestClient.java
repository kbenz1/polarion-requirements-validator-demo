package ch.kbenz.polariondemo.polarion;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PolarionRestClient implements PolarionClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String baseUrl;
    private final String bearerToken;

    public PolarionRestClient(String baseUrl, String bearerToken) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.bearerToken = bearerToken;
    }

    @Override
    public String getWorkItem(String projectId, String workItemId) throws Exception {
        String project = URLEncoder.encode(projectId, StandardCharsets.UTF_8);
        String item = URLEncoder.encode(workItemId, StandardCharsets.UTF_8);

        URI uri = URI.create(baseUrl
                + "/polarion/rest/v1/projects/"
                + project + "/workitems/" + item);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IllegalStateException(
                    "Polarion returned HTTP " + response.statusCode());
        }
        return response.body();
    }
}
