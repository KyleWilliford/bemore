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
        LOG.info("Loaded " + this.domains.size()+ " blacklisted ZeuS domains.");
        this.ipv4Addresses = ipv4Addresses;
        LOG.info("Loaded " + this.ipv4Addresses.size()+ " blacklisted ZeuS trojan ipv4 addresses.");
    }

    /**
     * @return the ipv4Addresses
     */
    public Set<String> getIpv4Addresses() {
        return ipv4Addresses;
    }

    /**
     * Return true if the given string includes a domain in the blacklist of domains.
     * The input can be an email or domain name.
     * 
     * @param input
     *            An email or domain name.
     * @return True if the string is in the blacklist.
     */
    @Override
    public boolean isDomainBlacklisted(final String input) {
        LOG.debug(input);
        if (StringUtils.isBlank(input))
            return false;
        String[] split = input.split("@");
        if (split.length == 0) {
            return false;
        } else if (split.length == 1) {
            LOG.debug(split[0]);
            return domains.contains(split[0]);
        } else if (split.length > 1) {
            LOG.debug(split[1]);
            return domains.contains(split[1]);
        }
        return false;
    }

    /**
     * 
     * @param ipv4
     * @return
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
