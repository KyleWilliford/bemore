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
public class OpenPhish {
    private static final Log LOG = LogFactory.getLog(OpenPhish.class);
    
    private Set<String> urls = new TreeSet<>();
    
    public OpenPhish(final Set<String> urls) {
        this.urls = urls;
    }

    public boolean isURLAPhishery(final String url) {
        LOG.debug(url);
        return this.urls.contains(url);
    }

    /**
     * @return the urls
     */
    public Set<String> getUrls() {
        return urls;
    }

    /**
     * @param urls the urls to set
     */
    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }
}
