package io.jenkins.plugins.gcr.parsers;

import hudson.FilePath;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.models.LcovCoverage;
import io.jenkins.plugins.gcr.models.LcovData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LcovParser implements CoverageParser {
    private static final String FILE_RECORD = "SF";
    private static final String LINE_FOUND_SUMMARY = "LF";
    private static final String LINE_HITS_SUMMARY = "LH";
    private static final String BRANCH_FOUND_SUMMARY = "BRF";
    private static final String BRANCH_HITS_SUMMARY = "BRH";
    private static final String END_OF_RECORD = "end_of_record";

    @Override
    public Coverage parse(FilePath filepath) throws ParserException {
        try (
                final InputStream fileStream = filepath.read();
                final BufferedReader fileContent = new BufferedReader(new InputStreamReader(fileStream))
        ) {
            final LcovData lcovData = new LcovData();

            String line;

            while ((line = fileContent.readLine()) != null) {
                if (line.contains(END_OF_RECORD) || line.startsWith(FILE_RECORD) || line.isEmpty()) continue;

                final int keySeparator = line.indexOf(':');

                final String summaryValue = line.substring(keySeparator + 1).trim().split(",")[0];

                addSummary(lcovData, line.substring(0, keySeparator), summaryValue);
            }

            return new LcovCoverage(lcovData);
        } catch (Exception ex) {
            String message = String.format("Failed to parse lcov coverage for filepath '%s'", filepath);
            throw new ParserException(message, ex);
        }
    }

    private void addSummary(final LcovData lcovData, final String summaryType, final String summaryValue) {
        switch (summaryType) {
            case LINE_FOUND_SUMMARY:
                lcovData.addLineFound(Integer.parseInt(summaryValue));
                break;
            case LINE_HITS_SUMMARY:
                lcovData.addLineCovered(Integer.parseInt(summaryValue));
                break;
            case BRANCH_FOUND_SUMMARY:
                lcovData.addBranchFound(Integer.parseInt(summaryValue));
                break;
            case BRANCH_HITS_SUMMARY:
                lcovData.addBranchCovered(Integer.parseInt(summaryValue));
                break;
        }
    }
}
