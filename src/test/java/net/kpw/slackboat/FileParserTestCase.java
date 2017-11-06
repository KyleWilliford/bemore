package net.kpw.slackboat;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import net.kpw.slackboat.util.FileParser;

public class FileParserTestCase {
    private static final Log LOG = LogFactory.getLog(FileParserTestCase.class);
    
    
    private final FileParser fileParser = FileParser.getInstance();

    @Test
    public void parseLines() {
        final String TEST_FILE_RESOURCE_PATH = "/disposable_email_blacklist.conf";
        Set<String> lines = fileParser.parseLines(getClass().getResourceAsStream(TEST_FILE_RESOURCE_PATH));
        assertEquals(lines.size(), 2677);
    }
    
    @Test
    public void parseURLsSecondColumn() {
        final String TEST_FILE_RESOURCE_PATH = "/phishtank.csv";
        Set<String> lines = fileParser.parseURLsSecondColumn(getClass().getResourceAsStream(TEST_FILE_RESOURCE_PATH));
        assertEquals(lines.size(), 22373);
    }
}
