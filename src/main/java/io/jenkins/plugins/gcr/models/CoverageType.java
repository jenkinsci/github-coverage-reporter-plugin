package io.jenkins.plugins.gcr.models;

import java.util.Arrays;
import java.util.stream.Stream;

public enum CoverageType {

    JACOCO("jacoco"),
    COBERTURA("cobertura"),
    LCOV("lcov"),
    SONARQUBE("sonarqube");

    private String identifier;

    CoverageType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static CoverageType fromIdentifier(String identifier) {
        Stream<CoverageType> stream = Arrays.stream(CoverageType.values());
        return stream.filter(c -> c.getIdentifier().equals(identifier)).findFirst().orElse(null);
    }
}
