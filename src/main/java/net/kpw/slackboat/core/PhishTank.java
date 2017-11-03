package net.kpw.slackboat.core;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PhishTank url database.
 * 
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
public class PhishTank extends Phishery {
    private static final Log LOG = LogFactory.getLog(PhishTank.class);

    public PhishTank(final Set<String> urls) {
        super(urls);
        LOG.info("Loaded " + this.urls.size()+ " PhishTank URLs.");
    }
}
