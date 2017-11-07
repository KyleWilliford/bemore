package net.kpw.slackboat.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import net.kpw.slackboat.core.ZeuS;
import net.kpw.slackboat.util.FileParser;

/**
 * Unit tests for {@link ZeuS}
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public class ZeuSTestCase {
    private static final FileParser fileParser = FileParser.getInstance();
    private final ZeuS zeus = new ZeuS(fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_bad_domains.txt")),
            fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_ipv4_addresses.txt")));
    final Set<String> domains = fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_bad_domains.txt"));
    final Set<String> ipv4Addresses = fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_ipv4_addresses.txt"));

    @Test
    public void domainsSize() {
        assertEquals(zeus.getDomains(), domains);
        assertNotEquals(zeus.getIpv4Addresses(), domains);
    }

    @Test
    public void ipAddressesSize() {
        assertEquals(zeus.getIpv4Addresses(), ipv4Addresses);
        assertNotEquals(zeus.getDomains(), ipv4Addresses);
    }

    @Test
    public void isDomainBlacklisted() {
        assertTrue(zeus.isDomainBlacklisted(domains.iterator().next()));
    }

    @Test
    public void isIPBlacklisted() {
        assertTrue(zeus.isIPBlacklisted(ipv4Addresses.iterator().next()));
    }

    @Test
    public void setDomains() {
        zeus.setDomains(domains);
        assertEquals(zeus.getDomains(), domains);
    }

    @Test
    public void setIpv4Addresses() {
        zeus.setIpv4Addresses(ipv4Addresses);
        assertEquals(zeus.getIpv4Addresses(), ipv4Addresses);
    }
}
