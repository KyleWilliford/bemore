package net.kpw.slackboat.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Refined abstraction class in bridge pattern. Represents an OpenPhish database.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
public final class OpenPhish extends BlackList {
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(OpenPhish.class);
    private Set<String> urls = new TreeSet<>();

    public OpenPhish(Set<String> urls) {
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
}
