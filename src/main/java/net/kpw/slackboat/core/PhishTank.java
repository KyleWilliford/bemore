package net.kpw.slackboat.core;

import java.util.Set;

/**
 * PhishTank url database.
 * 
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
public class PhishTank extends Phishery {

    public PhishTank(Set<String> urls) {
        super(urls);
    }
}
