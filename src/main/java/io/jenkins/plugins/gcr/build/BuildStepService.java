package io.jenkins.plugins.gcr.build;

import hudson.FilePath;
import io.jenkins.plugins.gcr.CoverageReportAction;
import io.jenkins.plugins.gcr.github.GithubPayload;
import io.jenkins.plugins.gcr.models.*;
import io.jenkins.plugins.gcr.parsers.CoverageParser;
import io.jenkins.plugins.gcr.parsers.ParserException;
import io.jenkins.plugins.gcr.parsers.ParserFactory;
import io.jenkins.plugins.gcr.sonar.SonarClient;
import io.jenkins.plugins.gcr.sonar.SonarException;

import java.io.File;

public class BuildStepService {

    public BuildStepService() {

    }

    public CoverageReportAction generateCoverageReport(FilePath filepath, ComparisonOption comparisonOption, String coverageType, String coverageRateType) throws ParserException, SonarException {
        CoverageParser parser = ParserFactory.instance.parserForType(CoverageType.fromIdentifier(coverageType));

        Coverage coverage = parser.parse(filepath);
        Coverage expectedCoverage = getExpectedCoverage(comparisonOption);

        // TODO: This should be user selectable
        return new CoverageReportAction(coverage, expectedCoverage, CoverageRateType.fromName(coverageRateType));
    }


    public Coverage getExpectedCoverage(ComparisonOption comparisonOption) throws SonarException {
        Coverage expectedCoverage;
        if (comparisonOption.isTypeSonarProject()) {
            SonarClient client = new SonarClient();
            expectedCoverage = client.getCoverageForProject(comparisonOption.getSonarProject());
        } else if (comparisonOption.isTypeFixedCoverage()) {
            double fixedValue = comparisonOption.fixedCoverageAsDouble();
            // TODO: Have two separate inputs for this
            expectedCoverage = new DefaultCoverage(fixedValue, fixedValue, fixedValue);
        } else {
            expectedCoverage = null;
        }
        return expectedCoverage;
    }

    public GithubPayload generateGithubCovergePayload(CoverageReportAction coverageReport, String targetUrl) {
        String status = coverageReport.getStatusName();
        String description = coverageReport.getStatusDescription();
        String context = "coverage";
        GithubPayload payload = new GithubPayload(status, targetUrl, description, context);

        return payload;
    }

}
