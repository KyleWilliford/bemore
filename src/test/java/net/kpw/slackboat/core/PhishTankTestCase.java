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
public class PhishTankTestCase {
    private static final FileParser fileParser = FileParser.getInstance();
    final Set<String> urls = fileParser.parseLines(getClass().getResourceAsStream("/phishtank.csv"));
    private final PhishTank phishTank = new PhishTank(urls);

    @Test
    public void domainsSize() {
        assertEquals(phishTank.getUrls(), urls);
    }

    @Test
    public void isDomainBlacklisted() {
        assertTrue(phishTank.isURLBlacklisted(urls.iterator().next()));
    }

    @Test
    public void setDomains() {
        phishTank.setUrls(urls);
        assertEquals(phishTank.getUrls(), urls);
    }
}
