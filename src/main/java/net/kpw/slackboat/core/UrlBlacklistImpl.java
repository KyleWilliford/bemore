package net.kpw.slackboat.core;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Concrete implementor for the bridge design pattern. This class is used for url related implementation methods.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
class UrlBlacklistImpl implements IBlacklist {
    private static final Log LOG = LogFactory.getLog(UrlBlacklistImpl.class);

    @Override
    public boolean isBlacklisted(final String input, final Collection<String> strings) {
        LOG.debug(input);
        if (StringUtils.isBlank(input) || CollectionUtils.isEmpty(strings)) {
            return false;
        }
        return strings.contains(input.toLowerCase());
    }
}
