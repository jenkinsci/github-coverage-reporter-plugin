package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.CoverageType;
import io.jenkins.plugins.gcr.parsers.CloverParser;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.CoverageParser;
import io.jenkins.plugins.gcr.parsers.JacocoParser;
import io.jenkins.plugins.gcr.parsers.ParserFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ParserFactoryTests {

    @Test
    public void testGetCloverParser() {
        ParserFactory factory = ParserFactory.instance;

        CoverageParser parser = factory.parserForType(CoverageType.CLOVER);

        Assert.assertThat(parser, CoreMatchers.instanceOf(CloverParser.class));
    }

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

}
