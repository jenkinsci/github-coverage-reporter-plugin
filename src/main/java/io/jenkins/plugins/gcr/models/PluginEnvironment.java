package io.jenkins.plugins.gcr.models;

import hudson.EnvVars;
import hudson.model.Run;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginEnvironment {

    private String pullRequestRepository;

    private String gitHash;

    private String buildUrl;

    // Constructor

    public PluginEnvironment(EnvVars env) throws IllegalArgumentException {
        if (env.containsKey("ghprbGhRepository") && env.containsKey("ghprbActualCommit")) {
            pullRequestRepository = get("ghprbGhRepository", env);
            gitHash = get("ghprbActualCommit", env);
        } else{
            String changeUrl = get("CHANGE_URL", env);

            Pattern pattern = Pattern.compile("https://[^/]*/(.*?)/pull/.*");
            Matcher matcher = pattern.matcher(changeUrl);
            if (matcher.find()) {
                pullRequestRepository = matcher.group(1);
            } else {
                throw new IllegalArgumentException(String.format("Can't find the owner/repo from CHANGE_URL environmental variable '%s'", changeUrl));
            }
            gitHash = get("GIT_COMMIT", env);
        }
        buildUrl = get("BUILD_URL", env);
    }

    // Getters / Setters

    public String getGitHash() {
        return gitHash;
    }

    public String getPullRequestRepository() {
        return pullRequestRepository;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    // Helpers

    private String get(String key, EnvVars env) throws IllegalArgumentException {
        if (env.containsKey(key)) {
            return env.get(key);
        } else {
            // TODO: localize
            throw new IllegalArgumentException(String.format("Failed to get required environmental variable '%s'", key));
        }
    }

}
