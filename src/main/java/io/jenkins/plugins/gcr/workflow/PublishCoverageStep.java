package io.jenkins.plugins.gcr.workflow;

import hudson.Extension;

import io.jenkins.plugins.gcr.models.ComparisonOption;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * Publishes code coverage to Github in Workflows.
 *
 * @author Nicolas Zin
 */
public class PublishCoverageStep extends AbstractStepImpl {

    private String filepath;

    private String coverageType;

    private String coverageRateType;

    private ComparisonOption comparisonOption;

    @DataBoundConstructor
    public PublishCoverageStep(String filepath, String coverageType, String coverageRateType, ComparisonOption comparisonOption) throws IOException {
        this.filepath = filepath;
        this.coverageType = coverageType;
        this.coverageRateType = coverageRateType;
        this.comparisonOption = comparisonOption;
    }

    public String getFilePath() {
        return filepath;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public String getCoverageRateType() {
        return coverageRateType;
    }

    public ComparisonOption getComparisonOption() {
        return comparisonOption;
    }


    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {

        public DescriptorImpl() {
            super(PublishCoverageStepExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "publishCoverageGithub";
        }

        @Override
        public String getDisplayName() {
            return "Publish Coverage reports to Github";
        }
    }
}
