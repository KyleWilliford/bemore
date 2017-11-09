package net.kpw.slackboat.core;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Concrete implementor for the bridge design pattern. This class is used for ip address related implementation methods.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
class IPv4BlackListImpl implements IBlacklist {
    private static final Log LOG = LogFactory.getLog(IPv4BlackListImpl.class);

    @Override
    public boolean isBlacklisted(final String input, final Collection<String> strings) {
        LOG.debug(input);
        if (StringUtils.isBlank(input) || CollectionUtils.isEmpty(strings)) {
            return false;
        }
        return strings.contains(input.toLowerCase());
    }
}
