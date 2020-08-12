package io.jenkins.plugins.gcr.parsers;

import hudson.FilePath;
import io.jenkins.plugins.gcr.models.CloverCoverage;
import io.jenkins.plugins.gcr.models.Coverage;
import io.jenkins.plugins.gcr.utils.XmlUtils;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

public final class CloverParser implements CoverageParser {

    @Override
    public Coverage parse(final FilePath filepath) throws ParserException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CloverCoverage.class);
            SAXSource source = XmlUtils.getSAXSource(filepath);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Coverage coverage = (CloverCoverage) jaxbUnmarshaller.unmarshal(source);

            return coverage;
        } catch (Exception ex) {
            String message = String.format("Failed to parse Clover coverage for filepath '%s'. Nested Exception message: '%s'", filepath, ex.getMessage());
            throw new ParserException(message, ex);
        }
    }
}
