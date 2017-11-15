package net.kpw.slackboat.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import net.kpw.slackboat.util.FileParser;

/**
 * Unit tests for {@link OpenPhish}
 * 
 * @author kwilliford
 * @created Nov 8, 2017
 *
 */
public class OpenPhishTestCase {
    private static final FileParser fileParser = FileParser.getInstance();
    final Set<String> urls = fileParser.parseLines(getClass().getResourceAsStream("/openphish.txt"));
    private final OpenPhish openPhish = new OpenPhish(urls);

    @Test
    public void urlsSize() {
        assertEquals(openPhish.getUrls(), urls);
    }

    @Test
    public void isURLBlacklisted() {
        assertTrue(openPhish.isURLBlacklisted(urls.iterator().next()));
    }

    @Test
    public void setUrls() {
        openPhish.setUrls(urls);
        assertEquals(openPhish.getUrls(), urls);
    }
}
