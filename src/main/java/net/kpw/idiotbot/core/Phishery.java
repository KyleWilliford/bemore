package net.kpw.idiotbot.core;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
public class Phishery {
    private static final Log LOG = LogFactory.getLog(Blacklist.class);

    protected Set<String> urls = new TreeSet<>();

    public Phishery(final Set<String> urls) {
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
    public boolean isURLAPhishery(final String url) {
        LOG.debug(url);
        return this.urls.contains(url);
    }

    /**
     * @param urls
     *            the urls to set
     */
    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
