package io.jenkins.plugins.gcr;

import io.jenkins.plugins.gcr.models.CoverageType;
import org.junit.Assert;
import org.junit.Test;

public class CoverageTypeTests {

    @Test
    public void testJacocoCoverageTypeFromString() {
        CoverageType type = CoverageType.fromIdentifier("jacoco");
        Assert.assertEquals(type, CoverageType.JACOCO);
        Assert.assertEquals(type.getIdentifier(), CoverageType.JACOCO.getIdentifier());
    }

    @Test
    public void testCoberturaCoverageTypeFromString() {
        CoverageType type = CoverageType.fromIdentifier("cobertura");
        Assert.assertEquals(type, CoverageType.COBERTURA);
        Assert.assertEquals(type.getIdentifier(), CoverageType.COBERTURA.getIdentifier());
    }

    @Test
    public void testCloverCoverageTypeFromString() {
        CoverageType type = CoverageType.fromIdentifier("clover");
        Assert.assertEquals(type, CoverageType.CLOVER);
        Assert.assertEquals(type.getIdentifier(), CoverageType.CLOVER.getIdentifier());
    }

    @Test
    public void testSonarqubeCoverageTypeFromString() {
        CoverageType type = CoverageType.fromIdentifier("sonarqube");
        Assert.assertEquals(type, CoverageType.SONARQUBE);
        Assert.assertEquals(type.getIdentifier(), CoverageType.SONARQUBE.getIdentifier());
    }

    @Test
    public void testNullCoverageTypeFromString() {
        CoverageType type1 = CoverageType.fromIdentifier("");
        CoverageType type2 = CoverageType.fromIdentifier(null);

        Assert.assertNull(type1);
        Assert.assertNull(type2);
    }

}
