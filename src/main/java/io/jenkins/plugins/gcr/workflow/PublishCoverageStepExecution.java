package io.jenkins.plugins.gcr.workflow;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

import hudson.EnvVars;
import hudson.model.BuildListener;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousStepExecution;
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
public class PublishCoverageStepExecution extends AbstractSynchronousStepExecution<Boolean> {
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
    protected Boolean run() throws Exception {
        StepContext context = getContext();
        EnvVars env = context.get(EnvVars.class);

        MutableBoolean codeCoverageAchieved = new MutableBoolean();
        boolean res = GithubCoveragePublisher.publishCoverage(build, ws, listener, env, step.getFilePath(), step.getCoverageXmlType(), step.getComparisonOption(), step.getCoverageRateType(),codeCoverageAchieved);
        if (!res) {
            throw new AbortException("Cannot publish coverage report");
        }
        return codeCoverageAchieved.booleanValue();
    }

}
