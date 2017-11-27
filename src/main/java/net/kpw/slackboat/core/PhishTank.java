package net.kpw.slackboat.core;

import java.util.List;
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
public final class PhishTank extends Blacklist {
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(PhishTank.class);
    private Set<String> urls = new TreeSet<>();

    public PhishTank(Set<String> urls) {
        this.urls = urls;
    }

    /**
     * @return the urls
     */
    public Set<String> getUrls() {
        return urls;
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

    /**
     * @param urls
     *            the urls to set
     */
    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
    
    /**
     * Return a list of strings that contain the given term.
     * @param term The search term.
     * @return A list of matched strings.
     */
    public List<String> searchURLBlacklist(final String term) {
        return super.urlBlackListImpl.search(term, this.urls);
    }
}
