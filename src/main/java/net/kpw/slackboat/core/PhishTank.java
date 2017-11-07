package net.kpw.slackboat.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refined abstraction class in bridge pattern. Represents a PhishTank database.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public final class PhishTank extends BlackList {
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(PhishTank.class);
    private Set<String> urls = new TreeSet<>();
    
    public PhishTank(Set<String> urls) {
        this.urls = urls;
    }

    /**
     * Return true if the url is in the list of phishy urls.
     * 
     * @param url
     *            The url to check.
     * @return True if the url is bad.
     */
    public boolean isURLBlacklisted(final String url) {
        return super.urlBlackListImpl.isBlacklisted(url, this.urls);
    }
}
