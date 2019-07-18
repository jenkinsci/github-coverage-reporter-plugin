package io.jenkins.plugins.gcr.workflow;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import hudson.EnvVars;
import hudson.model.BuildListener;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import hudson.AbortException;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.gcr.GithubCoveragePublisher;

/**
 * Execution for {@link PublishCoverageStep}.
 *
 * @author Nicolas Zin
 */
public class PublishCoverageStepExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {
    private static final long serialVersionUID = 1L;

    @StepContextParameter
    private transient TaskListener listener;

    @StepContextParameter
    private transient FilePath ws;

    @StepContextParameter
    private transient Run build;

    @StepContextParameter
    private transient Launcher launcher;

    @Inject
    private transient PublishCoverageStep step;

    @Override
    protected Void run() throws Exception {
        StepContext context = getContext();
        EnvVars env = context.get(EnvVars.class);

        boolean res = GithubCoveragePublisher.publishCoverage(build, ws, listener, env, step.getFilePath(), step.getCoverageXmlType(), step.getComparisonOption(), step.getCoverageRateType());
        if (!res) {
            throw new AbortException("Cannot publish coverage report");
        }
        return null;
    }

}
