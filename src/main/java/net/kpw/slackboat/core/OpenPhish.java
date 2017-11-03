package net.kpw.slackboat.core;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OpenPhish url database.
 * 
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
public class OpenPhish extends Phishery {
    private static final Log LOG = LogFactory.getLog(OpenPhish.class);

    public OpenPhish(final Set<String> urls) {
        super(urls);
        LOG.info("Loaded " + this.urls.size()+ " OpenPhish URLs.");
    }
}
