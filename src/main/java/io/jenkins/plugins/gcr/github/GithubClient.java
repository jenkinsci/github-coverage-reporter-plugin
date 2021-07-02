package io.jenkins.plugins.gcr.github;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.jenkins.plugins.gcr.models.PluginEnvironment;

public class GithubClient {

    private PluginEnvironment environment;

    private String accessToken;

    private String githubUrl;

    private HttpClient httpClient;

    public GithubClient(PluginEnvironment environment, String githubUrl, String accessToken) {
        this(environment, githubUrl, accessToken, HttpClientBuilder.create().build());
    }

    public GithubClient(PluginEnvironment environment, String githubUrl, String accessToken, HttpClient httpClient) {
        this.httpClient = httpClient;
        this.environment = environment;
        this.accessToken = accessToken;
        this.githubUrl = githubUrl;
    }

    // this function will call /repos/%s/pulls/%s to get back a Json structure
    // in this structure we are looking for .head.sha which is the git commit id corresponding to the PR
    private String fetchGitHashFromGithub(String repo,String pullid) throws GithubClientException {
        String path = String.format("/repos/%s/pulls/%s", repo, pullid);
        URI uri = buildUri(path);
        HttpUriRequest getRequest = RequestBuilder.get()
          .setUri(uri)
          .setHeader("Authorization", "token " + accessToken)
          .build();

        ResponseHandler<GithubResponse> responseHandler = (HttpResponse response) -> {
            InputStream stream = response.getEntity().getContent();
            String string = IOUtils.toString(stream,"utf-8");
            int status = response.getStatusLine().getStatusCode();

            if(status == HttpStatus.SC_OK) {
                try {
                    // looking for .head.sha
                    JsonObject obj = new JsonParser().parse(string).getAsJsonObject();
                    JsonObject head = obj.getAsJsonObject("head");
                    JsonPrimitive sha=head.getAsJsonPrimitive("sha");
                    String shaString = sha.getAsString();

                    return new GithubResponse(true, shaString);
                } catch (Exception e) {
                    String message = String.format("Not able to find .head.sha when accessing %s into: %s %s", uri ,e.toString(),string);
                    return new GithubResponse(false, message);
                }
            }

            String body = IOUtils.toString(response.getEntity().getContent());
            String message = String.format("[%d] %s", status, body);
            return new GithubResponse(false, message);
        };

        try {
            GithubResponse result = this.httpClient.execute(getRequest, responseHandler);
            if (!result.isSuccess()) {
                String message = String.format("Bad HTTP result for url %s. Error message: %s", getRequest.getURI().toString(),result.getMessage());
                throw new GithubClientException(message);
            }
            // message must contains the commit hash
            return result.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new GithubClientException("IOException during request");
        }
    }

    public void sendCommitStatus(GithubPayload githubPayload) throws GithubClientException {

        String githash = environment.getGitHash();
        // if we are not using ghprb we have to find the commit id
        if (githash == null) {
            githash = fetchGitHashFromGithub(environment.getPullRequestRepository(),environment.getPullId());
        }

        String path = String.format("/repos/%s/statuses/%s", environment.getPullRequestRepository(), githash);


        URI uri = buildUri(path);
        RequestBuilder requestBuilder = RequestBuilder.post()
          .setUri(uri)
          .setHeader("Authorization", "token " + accessToken);

        try {
            StringEntity entity = new StringEntity(githubPayload.toJSONString());
            requestBuilder.setEntity(entity);
        } catch (UnsupportedEncodingException ex) {
            throw new GithubClientException("Issue with encoding of github payload", ex);
        }

        ResponseHandler<GithubResponse> responseHandler = (HttpResponse response) -> {
            InputStream stream = response.getEntity().getContent();
            String string = IOUtils.toString(stream);
            System.out.println(string);

            int status = response.getStatusLine().getStatusCode();

            if(status == HttpStatus.SC_CREATED) {
                return new GithubResponse(true, "");
            }

            String body = IOUtils.toString(response.getEntity().getContent());
            String message = String.format("[%d] %s", status, body);
            return new GithubResponse(false, message);
        };

        try {
            HttpUriRequest postRequest = requestBuilder.build();
            GithubResponse result = this.httpClient.execute(postRequest, responseHandler);
            if (!result.isSuccess()) {
                String message = String.format("Bad HTTP result for url %s. Error message:  ", postRequest.getURI().toString());
                throw new GithubClientException(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new GithubClientException("IOException during request");
        }
    }

    private URI buildUri(String path) throws GithubClientException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");

        if (this.isCustomUrlValid()) {
            builder.setHost(this.cleanedHost());
        } else {
            builder.setHost("api.github.com");
        }

        if (this.isCustomUrlValid()) {
            path = "/api/v3".concat(path);
        }

        builder.setPath(path);

        try {
            return builder.build();
        } catch (URISyntaxException ex) {
            throw new GithubClientException("URI builder syntax was malformed", ex);
        }
    }

    private boolean isCustomUrlValid() {
        return this.githubUrl != null && this.githubUrl.startsWith("https://");
    }

    private String cleanedHost() {
        if (this.githubUrl.startsWith("https://")) {
            return this.githubUrl.replaceFirst("https://", "");
        }
        return this.githubUrl;
    }

}
