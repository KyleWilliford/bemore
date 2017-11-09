package net.kpw.slackboat.core;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Concrete implementor for the bridge design pattern. This class is used for domain name related implementation methods.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
class DomainBlacklistImpl implements IBlacklist {
    private static final Log LOG = LogFactory.getLog(DomainBlacklistImpl.class);

    @Override
    public boolean isBlacklisted(final String input, final Collection<String> strings) {
        LOG.debug(input);
        if (StringUtils.isBlank(input) || CollectionUtils.isEmpty(strings)) {
            return false;
        }
        final String inputLowerCase = input.toLowerCase();
        String[] split = inputLowerCase.split("@");
        if (split.length == 0) {
            return false;
        } else if (split.length == 1) {
            LOG.debug(split[0]);
            return strings.contains(split[0]);
        } else if (split.length > 1) {
            LOG.debug(split[1]);
            return strings.contains(split[1]);
        }
        return false;
    }
}
