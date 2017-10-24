package net.kpw.idiotbot.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
public class Blacklist {
    private static final Log LOG = LogFactory.getLog(Blacklist.class);
    
    private Set<String> domains = new TreeSet<>();
    
    public Blacklist(final Set<String> domains) {
        this.domains = domains;
    }

    public boolean isDomainBlacklisted(final String email) {
        LOG.debug(email);
        if (StringUtils.isBlank(email)) return false;
        String[] split = email.split("@");
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
     * @return the domains
     */
    public Set<String> getDomains() {
        return domains;
    }

    /**
     * @param domains the domains to set
     */
    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }
}
