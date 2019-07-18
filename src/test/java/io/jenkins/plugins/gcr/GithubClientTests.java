package io.jenkins.plugins.gcr;

import com.github.paweladamski.httpclientmock.HttpClientMock;
import com.google.common.io.Files;
import hudson.EnvVars;
import io.jenkins.plugins.gcr.github.GithubClient;
import io.jenkins.plugins.gcr.github.GithubClientException;
import io.jenkins.plugins.gcr.github.GithubPayload;
import io.jenkins.plugins.gcr.models.PluginEnvironment;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class GithubClientTests {

    private static final String COMMIT_HASH = "8c11abea6dfc884b5fcad38026e58214c995b93c";
    private static final String REPO = "jnewc/myrepo";
    private static final String ACCESS_TOKEN = "abc123";

    @Test
    public void testGhprbGithubClient() throws Exception {

        PluginEnvironment mockEnvironment = createGhprbMockEnvironment();
        HttpClientMock mockClient = new HttpClientMock();
        GithubPayload payload = createMockPayload();

        File mockFile = TestUtils.loadResource("github-status.json");

        String url = String.format("https://api.github.com/repos/%s/statuses/%s", REPO, COMMIT_HASH);

        mockClient.onPost(url)
                .withParameter("access_token", ACCESS_TOKEN)
                .doReturnStatus(201)
                .doReturn(Files.toString(mockFile, Charset.forName("utf-8")));

        GithubClient client = new GithubClient(mockEnvironment, null, ACCESS_TOKEN, mockClient);

        try {
            client.sendCommitStatus(payload);
        } catch (GithubClientException ex) {
            Assert.fail();
        }
    }

    @Test
    public void testGithubClient() throws Exception {

        PluginEnvironment mockEnvironment = createMockEnvironment();
        HttpClientMock mockClient = new HttpClientMock();
        GithubPayload payload = createMockPayload();

        File mockFile = TestUtils.loadResource("github-status.json");

        String url = String.format("https://api.github.com/repos/%s/statuses/%s", REPO, COMMIT_HASH);

        mockClient.onPost(url)
                .withParameter("access_token", ACCESS_TOKEN)
                .doReturnStatus(201)
                .doReturn(Files.toString(mockFile, Charset.forName("utf-8")));

        GithubClient client = new GithubClient(mockEnvironment, null, ACCESS_TOKEN, mockClient);

        try {
            client.sendCommitStatus(payload);
        } catch (GithubClientException ex) {
            Assert.fail();
        }
    }

    private GithubPayload createMockPayload() {
        GithubPayload payload = new GithubPayload(
                "success",
                "https://my.target/url",
                "description",
                "context"
        );

        return payload;
    }

    private PluginEnvironment createMockEnvironment() {
        EnvVars envVars = new EnvVars(
                "CHANGE_URL", "https://github.com/" + REPO + "/pull/130",
                "GIT_COMMIT", COMMIT_HASH,
                "BUILD_URL", "https://my.build/url"
        );

        PluginEnvironment environment = new PluginEnvironment(envVars);

        return environment;
    }

    private PluginEnvironment createGhprbMockEnvironment() {
        EnvVars envVars = new EnvVars(
                "ghprbPullId", "34",
                "ghprbGhRepository", REPO,
                "ghprbAuthorRepoGitUrl", "https://api.github.com",
                "ghprbActualCommit", COMMIT_HASH,
                "BUILD_URL", "https://my.build/url"
        );

        PluginEnvironment environment = new PluginEnvironment(envVars);

        return environment;
    }
}
