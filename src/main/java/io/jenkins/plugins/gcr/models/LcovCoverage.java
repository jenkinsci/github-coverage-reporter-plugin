package io.jenkins.plugins.gcr.models;

public class LcovCoverage implements Coverage {
    private final LcovData lcovData;

    public LcovCoverage(LcovData lcovData) {
        this.lcovData = lcovData;
    }

    @Override
    public double getLineRate() {
        final double linesFound = lcovData.getLinesFound();

        final double rawLinesCoverageRate = linesFound == 0 ? 0 : lcovData.getLinesCovered() / linesFound;

        return roundToTwoDecimalPoints(rawLinesCoverageRate);
    }

    @Override
    public double getBranchRate() {
        final double branchFound = lcovData.getBranchFound();

        final double rawBranchCoverageFate = branchFound == 0 ? 0 : lcovData.getBranchCovered() / branchFound;

        return roundToTwoDecimalPoints(rawBranchCoverageFate);
    }

    @Override
    public double getOverallRate() {
        final double rawOverallRate = (getLineRate() + getBranchRate()) / 2;

        return roundToTwoDecimalPoints(rawOverallRate);
    }

    private static double roundToTwoDecimalPoints(final double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
