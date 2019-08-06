package io.jenkins.plugins.gcr.models;

import hudson.EnvVars;
import hudson.model.Run;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginEnvironment {

    private String pullRequestRepository;

    private String gitHash;

    private String pullId;

    private String buildUrl;

    // Constructor

    public PluginEnvironment(EnvVars env) throws IllegalArgumentException {
        if (env.containsKey("ghprbGhRepository") && env.containsKey("ghprbActualCommit")) {
            pullRequestRepository = get("ghprbGhRepository", env);
            gitHash = get("ghprbActualCommit", env);
        } else{
            // either we receive a http PR (like CHANGE_URL=https://github.com/<org>/<repo>/pull/<id>)
            // or git PR (like GIT_URL=git@github.com:<org>/<repo>.git
            if (env.containsKey("CHANGE_URL")) {

                String changeUrl = get("CHANGE_URL", env);

                Pattern pattern = Pattern.compile("https://[^/]*/(.*?)/pull/(.*)");
                Matcher matcher = pattern.matcher(changeUrl);
                if (matcher.find()) {
                    pullRequestRepository = matcher.group(1);
                    pullId = matcher.group(2);
                } else {
                    throw new IllegalArgumentException(String.format("Can't find the owner/repo from CHANGE_URL environmental variable '%s'", changeUrl));
                }
            } else {
                String gitUrl = get("GIT_URL", env);

                Pattern pattern = Pattern.compile("[^/]*@[^/]*:(.*?)\\.git");
                Matcher matcher = pattern.matcher(gitUrl);
                if (matcher.find()) {
                    pullRequestRepository = matcher.group(1);
                } else {
                    throw new IllegalArgumentException(String.format("Can't find the owner/repo from GIT_URL environmental variable '%s'", gitUrl));
                }
                pullId = get("CHANGE_ID", env);
            }
        }
        buildUrl = get("BUILD_URL", env);
    }

    // Getters / Setters

    public String getGitHash() {
        return gitHash;
    }

    public String getPullId() {
        return pullId;
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
