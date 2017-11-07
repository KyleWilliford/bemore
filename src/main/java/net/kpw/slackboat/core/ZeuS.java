package net.kpw.slackboat.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refined abstraction class in bridge pattern. Represents a ZeuS malware database.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public final class ZeuS extends BlackList {
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(ZeuS.class);
    private Set<String> domains = new TreeSet<>();
    private Set<String> ipv4Addresses = new TreeSet<>();
    
    public ZeuS(Set<String> domains, Set<String> ipv4Addresses) {
        this.domains = domains;
        this.ipv4Addresses = ipv4Addresses;
    }

    /**
     * Return true if the given string includes a domain in the blacklist of domains.
     * The input can be an email or domain name.
     * 
     * @param domain
     *            An email or domain name.
     * @return True if the string is in the blacklist.
     */
    public boolean isDomainBlacklisted(final String domain) {
        return super.domainBlackListImpl.isBlacklisted(domain, this.domains);
    }

    /**
     * Return true if the given string includes an ipv4 address in the blacklist of ipv4 addresses.
     * 
     * @param ipv4
     *            The IPv4 address.
     * @return True if the address is in the blacklist.
     */
    public boolean isIPBlacklisted(final String ipv4) {
        return super.ipv4BlackListImpl.isBlacklisted(ipv4, this.ipv4Addresses);
    }
}
