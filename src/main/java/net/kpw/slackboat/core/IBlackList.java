package net.kpw.slackboat.core;

import java.util.Collection;

/**
 * Implementation interface that bridges an abstract blacklist and implementation concrete classes.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
interface IBlackList {
    /**
     * Return true if the given string input is contained in the provided Collection of strings.
     * 
     * @param input The input to test.
     * @param strings The collection.
     * @return True if the collection contains the string.
     */
    public boolean isBlacklisted(final String input, final Collection<String> strings);
}
