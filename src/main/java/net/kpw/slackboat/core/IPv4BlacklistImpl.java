package net.kpw.slackboat.core;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
class IPv4BlacklistImpl implements IBlacklist {
    private static final Log LOG = LogFactory.getLog(IPv4BlacklistImpl.class);

    @Override
    public boolean isBlacklisted(final String input, final Set<String> strings) {
        LOG.debug(input);
        if (StringUtils.isBlank(input) || CollectionUtils.isEmpty(strings)) {
            return false;
        }
        return !strings.parallelStream().filter(s -> s.trim().toLowerCase().equalsIgnoreCase(input.trim().toLowerCase())).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public List<String> search(String term, Set<String> strings) {
        // TODO Auto-generated method stub
        return null;
    }
}
