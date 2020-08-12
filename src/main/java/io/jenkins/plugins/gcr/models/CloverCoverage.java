package io.jenkins.plugins.gcr.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "coverage")
public final class CloverCoverage extends XmlCoverage {
    @XmlElement(name = "project")
    public CloverProject project;

    @Override
    public double getLineRate() {
        return (double)
                this.project.metrics.loc
                /
                (this.project.metrics.loc + this.project.metrics.ncloc);
    }

    @Override
    public double getBranchRate() {
        return (double) this.project.metrics.coveredStatements / this.project.metrics.statements;
    }

    @Override
    public String toString() {
        return "[ " +
                String.format("lineRate=%f, branchRate=%f", this.getLineRate(), this.getBranchRate()) +
                " ]";
    }
}
