package net.kpw.slackboat.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import net.kpw.slackboat.util.FileParser;

/**
 * Unit tests for {@link ZeuSDomainList}
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public class ZeuSDomainListTestCase {
    private static final FileParser fileParser = FileParser.getInstance();
    final Set<String> domains = fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_bad_domains.txt"));
    final Set<String> ipv4Addresses = fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_ipv4_addresses.txt"));
    private final ZeuSDomainList zeusDomainList = new ZeuSDomainList(domains, ipv4Addresses);

    @Test
    public void domainsSize() {
        assertEquals(zeusDomainList.getDomains(), domains);
        assertNotEquals(zeusDomainList.getIpv4Addresses(), domains);
    }

    @Test
    public void ipAddressesSize() {
        assertEquals(zeusDomainList.getIpv4Addresses(), ipv4Addresses);
        assertNotEquals(zeusDomainList.getDomains(), ipv4Addresses);
    }

    @Test
    public void isDomainBlacklisted() {
        assertTrue(zeusDomainList.isDomainBlacklisted(domains.iterator().next()));
    }

    @Test
    public void isIPBlacklisted() {
        assertTrue(zeusDomainList.isIPBlacklisted(ipv4Addresses.iterator().next()));
    }

    @Test
    public void setDomains() {
        zeusDomainList.setDomains(domains);
        assertEquals(zeusDomainList.getDomains(), domains);
    }

    @Test
    public void setIpv4Addresses() {
        zeusDomainList.setIpv4Addresses(ipv4Addresses);
        assertEquals(zeusDomainList.getIpv4Addresses(), ipv4Addresses);
    }
}
