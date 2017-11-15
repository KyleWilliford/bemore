package net.kpw.slackboat.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * Unit tests for {@link FileParser}
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public class FileParserTest {
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(FileParserTest.class);

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
        Set<String> urls = fileParser.parseURLsSecondColumn(getClass().getResourceAsStream(TEST_FILE_RESOURCE_PATH));
        assertEquals(urls.size(), 22275);
        for (String url : urls) {
            if (StringUtils.isBlank(url)) {
                fail("There should be no blank or null urls.");
            }
            if (url.contains(",")) {
                fail("There should be no commas - this indicaes that the csv was not parsed correctly.");
            }
        }
    }
}
