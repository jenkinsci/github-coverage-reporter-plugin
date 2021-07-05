package io.jenkins.plugins.gcr;

import hudson.FilePath;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.parsers.CoberturaParser;
import io.jenkins.plugins.gcr.parsers.JacocoParser;
import io.jenkins.plugins.gcr.parsers.LcovParser;
import io.jenkins.plugins.gcr.parsers.ParserException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ParserTests {

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
        } catch(ParserException ex) {
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
        } catch(ParserException ex) {
            Assert.assertNotNull(ex);
        }
    }

    @Test
    public void testParseLcov() throws Exception {
        final LcovParser parser = new LcovParser();

        final File coverageFile = TestUtils.loadResource("lcov_coverage.info");

        final FilePath coverageFilePath = new FilePath(coverageFile);

        final Coverage coverage = parser.parse(coverageFilePath);

        Assert.assertEquals(0.38, coverage.getLineRate(), 0.05);
        Assert.assertEquals(0.0, coverage.getBranchRate(), 0.05);
        Assert.assertEquals(0.19, coverage.getOverallRate(), 0.05);

        final File coverageFileWithBranch = TestUtils.loadResource("lcov_coverage2.info");

        final FilePath coverageFileWithBranchPath = new FilePath(coverageFileWithBranch);

        final Coverage coverageWithBranch = parser.parse(coverageFileWithBranchPath);

        Assert.assertEquals(0.82, coverageWithBranch.getLineRate(), 0.05);
        Assert.assertEquals(0.95, coverageWithBranch.getBranchRate(), 0.05);
        Assert.assertEquals(0.88, coverageWithBranch.getOverallRate(), 0.05);
    }

    @Test
    public void testParseLcovFailure() throws Exception {
        final LcovParser parser = new LcovParser();

        File coverageFile = TestUtils.loadResource("lcov_coverage_corrupted.info");
        FilePath coverageFilePath = new FilePath(coverageFile);
        try {
            Coverage coverage = parser.parse(coverageFilePath);
            Assert.fail();
        } catch(ParserException ex) {
            Assert.assertNotNull(ex);
        }
    }
}
