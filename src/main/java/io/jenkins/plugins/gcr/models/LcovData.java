package io.jenkins.plugins.gcr.models;

public class LcovData {
    private double linesFound = 0;
    private double linesCovered = 0;
    private double branchFound = 0;
    private double branchCovered = 0;

    public void addLineFound(final int count) {
        linesFound += count;
    }

    public void addLineCovered(final int count) {
        linesCovered += count;
    }

    public void addBranchFound(final int count) {
        branchFound += count;
    }

    public void addBranchCovered(final int count) {
        branchCovered += count;
    }

    public double getLinesFound() {
        return linesFound;
    }

    public double getLinesCovered() {
        return linesCovered;
    }

    public double getBranchFound() {
        return branchFound;
    }

    public double getBranchCovered() {
        return branchCovered;
    }
}
