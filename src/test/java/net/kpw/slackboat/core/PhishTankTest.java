package net.kpw.slackboat.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import net.kpw.slackboat.util.FileParser;

/**
 * Unit tests for {@link PhishTank}
 * 
 * @author kwilliford
 * @created Nov 8, 2017
 *
 */
public class PhishTankTest {
    private static final FileParser fileParser = FileParser.getInstance();
    private final Set<String> urls = fileParser.parseURLsSecondColumn(getClass().getResourceAsStream("/phishtank.csv"));
    private final PhishTank phishTank = new PhishTank(urls);

    @Test
    public void isURLBlacklisted() {
        assertTrue(phishTank.isURLBlacklisted(urls.iterator().next()));
    }

    @Test
    public void setUrls() {
        phishTank.setUrls(urls);
        assertEquals(phishTank.getUrls(), urls);
    }

    @Test
    public void urlsSize() {
        assertEquals(phishTank.getUrls(), urls);
    }
}
