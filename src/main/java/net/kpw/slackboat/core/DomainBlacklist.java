package net.kpw.slackboat.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Blacklist domain database.
 * 
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
public class DomainBlacklist {
    private static final Log LOG = LogFactory.getLog(DomainBlacklist.class);

    protected Set<String> domains = new TreeSet<>();

    public DomainBlacklist(final Set<String> domains) {
        this.domains = domains;
        LOG.info("Loaded " + this.domains.size() + " blacklisted domains.");
    }

    /**
     * @return the domains
     */
    public Set<String> getDomains() {
        return domains;
    }

    /**
     * Return true if the given string includes a domain in the blacklist of domains.
     * The input can be an email or domain name.
     * 
     * @param input
     *            An email or domain name.
     * @return True if the string is in the blacklist.
     */
    public boolean isDomainBlacklisted(final String input) {
        LOG.debug(input);
        if (StringUtils.isBlank(input)) {
            return false;
        }
        final String inputLowerCase = input.toLowerCase();
        String[] split = inputLowerCase.split("@");
        if (split.length == 0) {
            return false;
        } else if (split.length == 1) {
            LOG.debug(split[0]);
            return this.domains.contains(split[0]);
        } else if (split.length > 1) {
            LOG.debug(split[1]);
            return this.domains.contains(split[1]);
        }
        return false;
    }

    /**
     * @param domains
     *            the domains to set
     */
    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }
}
