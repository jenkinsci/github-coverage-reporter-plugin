package io.jenkins.plugins.gcr.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project")
public class CloverProject {

    @XmlElement(name = "metrics")
    public CloverProjectMetrics metrics;
}
