package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.CoverageType;
import io.jenkins.plugins.gcr.parsers.*;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ParserFactoryTests {

    @Test
    public void testGetCoberturaParser() {
        ParserFactory factory = ParserFactory.instance;

        CoverageParser parser = factory.parserForType(CoverageType.COBERTURA);

        Assert.assertThat(parser, CoreMatchers.instanceOf(CoberturaParser.class));
    }

    @Test
    public void testGetJacocoParser() {
        ParserFactory factory = ParserFactory.instance;

        CoverageParser parser = factory.parserForType(CoverageType.JACOCO);

        Assert.assertThat(parser, CoreMatchers.instanceOf(JacocoParser.class));
    }

    @Test
    public void testGetLcovParser() {
        final ParserFactory factory = ParserFactory.instance;

        final CoverageParser parser = factory.parserForType(CoverageType.LCOV);

        Assert.assertThat(parser, CoreMatchers.instanceOf(LcovParser.class));
    }
}
