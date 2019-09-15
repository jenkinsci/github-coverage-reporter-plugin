package io.jenkins.plugins.gcr.parsers;

import io.jenkins.plugins.gcr.models.CoverageType;

import java.util.HashMap;
import java.util.Map;

public class ParserFactory {

    public static final ParserFactory instance = new ParserFactory();

    private Map<CoverageType, CoverageParser> parsers = new HashMap<>();

    private ParserFactory() {
        parsers.put(CoverageType.COBERTURA, new CoberturaParser());
        parsers.put(CoverageType.JACOCO, new JacocoParser());
        parsers.put(CoverageType.LCOV, new LcovParser());
    }

    public CoverageParser parserForType(CoverageType type) {
        return parsers.get(type);
    }

}
