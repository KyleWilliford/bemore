package net.kpw.slackboat.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ZeuS domain/ip database.
 * 
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
public class ZeuSBlacklist extends DomainBlacklist {
    private static final Log LOG = LogFactory.getLog(ZeuSBlacklist.class);

    private Set<String> ipv4Addresses = new TreeSet<>();

    public ZeuSBlacklist(final Set<String> domains, final Set<String> ipv4Addresses) {
        super(domains);
        LOG.info("Loaded " + this.domains.size() + " blacklisted ZeuS domains.");
        this.ipv4Addresses = ipv4Addresses;
        LOG.info("Loaded " + this.ipv4Addresses.size() + " blacklisted ZeuS trojan ipv4 addresses.");
    }

    /**
     * @return the ipv4Addresses
     */
    public Set<String> getIpv4Addresses() {
        return ipv4Addresses;
    }

    /**
     * Return true if the given string includes an ipv4 address in the blacklist of ipv4 addresses.
     * 
     * @param ipv4
     *            The IPv4 address.
     * @return True if the address is in the blacklist.
     */
    public boolean isIPBlacklisted(final String ipv4) {
        LOG.debug(ipv4);
        if (StringUtils.isBlank(ipv4)) {
            return false;
        }
        return this.ipv4Addresses.contains(ipv4);
    }

    /**
     * @param ipv4Addresses
     *            the ipv4Addresses to set
     */
    public void setIpv4Addresses(Set<String> ipv4Addresses) {
        this.ipv4Addresses = ipv4Addresses;
    }
}
