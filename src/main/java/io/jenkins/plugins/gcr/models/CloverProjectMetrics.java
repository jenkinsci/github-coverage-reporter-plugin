package io.jenkins.plugins.gcr.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "metrics")
public class CloverProjectMetrics {
    @XmlAttribute
    public Integer loc;

    @XmlAttribute
    public Integer ncloc;

    @XmlAttribute
    public Integer files;

    @XmlAttribute
    public Integer classes;

    @XmlAttribute
    public Integer methods;

    @XmlAttribute(name = "coveredmethods")
    public Integer coveredMethods;

    @XmlAttribute
    public Integer conditionals;

    @XmlAttribute(name = "coveredconditionals")
    public Integer coveredConditionals;

    @XmlAttribute
    public Integer statements;

    @XmlAttribute(name = "coveredstatements")
    public Integer coveredStatements;

    @XmlAttribute
    public Integer elements;

    @XmlAttribute(name = "coveredelements")
    public Integer coveredElements;


}
