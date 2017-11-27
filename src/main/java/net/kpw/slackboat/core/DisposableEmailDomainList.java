package net.kpw.slackboat.core;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refined abstraction class in bridge pattern. Represents a disposable malware domains database.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public final class DisposableEmailDomainList extends Blacklist {
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(DisposableEmailDomainList.class);
    private Set<String> domains = new TreeSet<>();

    public DisposableEmailDomainList(Set<String> domains) {
        this.domains = domains;
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
        return super.domainBlackListImpl.isBlacklisted(input, this.domains);
    }

    /**
     * Return a list of strings that contain the given term.
     * 
     * @param term
     *            The search term.
     * @return A list of matched strings.
     */
    public List<String> searchDomainBlacklist(final String term) {
        return super.domainBlackListImpl.search(term, this.domains);
    }

    /**
     * @param domains
     *            the domains to set
     */
    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }
}
