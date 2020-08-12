package io.jenkins.plugins.gcr;

import hudson.FilePath;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.parsers.CloverParser;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.JacocoParser;
import io.jenkins.plugins.gcr.parsers.ParserException;
import java.io.File;
import org.junit.Assert;
import org.junit.Test;

public class ParserTests {

    @Test
    public void testParseClover() throws Exception {
        CloverParser parser = new CloverParser();

        File coverageFile = TestUtils.loadResource("clover-coverage.xml");
        FilePath coverageFilePath = new FilePath(coverageFile);
        Coverage coverage = parser.parse(coverageFilePath);

        Assert.assertEquals(0.55, coverage.getLineRate(), 0.05);
        Assert.assertEquals(0.46, coverage.getBranchRate(), 0.05);
        Assert.assertEquals(0.51, coverage.getOverallRate(), 0.05);
    }


    @Test
    public void testParseCobertura() throws Exception {
        CoberturaParser parser = new CoberturaParser();

        File coverageFile = TestUtils.loadResource("cobertura-coverage.xml");
        FilePath coverageFilePath = new FilePath(coverageFile);
        Coverage coverage = parser.parse(coverageFilePath);

        Assert.assertEquals(0.75, coverage.getLineRate(), 0.05);
        Assert.assertEquals(0.75, coverage.getBranchRate(), 0.05);
        Assert.assertEquals(0.75, coverage.getOverallRate(), 0.05);
    }

    @Test
    public void testParseCoberturaFailure() throws Exception {
        CoberturaParser parser = new CoberturaParser();

        File coverageFile = TestUtils.loadResource("cobertura-coverage-corrupted.xml");
        FilePath coverageFilePath = new FilePath(coverageFile);
        try {
            Coverage coverage = parser.parse(coverageFilePath);
            Assert.fail();
        } catch (ParserException ex) {
            Assert.assertNotNull(ex);
        }
    }

    @Test
    public void testParseJacoco() throws Exception {
        JacocoParser parser = new JacocoParser();

        File coverageFile = TestUtils.loadResource("jacoco-coverage.xml");
        FilePath coverageFilePath = new FilePath(coverageFile);
        Coverage coverage = parser.parse(coverageFilePath);

        Assert.assertEquals(0.22, coverage.getLineRate(), 0.05);
        Assert.assertEquals(0.28, coverage.getBranchRate(), 0.05);
        Assert.assertEquals(0.25, coverage.getOverallRate(), 0.05);
    }

    @Test
    public void testParseJacocoFailure() throws Exception {
        JacocoParser parser = new JacocoParser();

        File coverageFile = TestUtils.loadResource("jacoco-coverage-corrupted.xml");
        FilePath coverageFilePath = new FilePath(coverageFile);
        try {
            Coverage coverage = parser.parse(coverageFilePath);
            Assert.fail();
        } catch (ParserException ex) {
            Assert.assertNotNull(ex);
        }
    }

}
