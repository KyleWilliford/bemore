package net.kpw.slackboat.core;

import java.util.List;
import java.util.Set;

/**
 * Implementation interface that bridges an abstract blacklist and implementation concrete classes.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
interface IBlacklist {
    /**
     * Return true if the given string input is contained in the given set of strings.
     * 
     * @param input
     *            The input to test.
     * @param strings
     *            The collection.
     * @return True if the collection contains the string.
     */
    public boolean isBlacklisted(final String input, final Set<String> strings);

    public List<String> search(final String term, final Set<String> strings);
}
