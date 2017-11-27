package net.kpw.slackboat.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public boolean isBlacklisted(final String input, final Set<String> strings) {
        LOG.debug(input);
        if (StringUtils.isBlank(input) || CollectionUtils.isEmpty(strings)) {
            return false;
        }
        return !strings.parallelStream().filter(s -> s.trim().toLowerCase().equalsIgnoreCase(input.trim().toLowerCase())).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public List<String> search(String term, Set<String> strings) {
        if (StringUtils.isBlank(term) || CollectionUtils.isEmpty(strings)) {
            return new ArrayList<>();
        }
        final String termLC = term.trim().toLowerCase();
        LOG.debug(term);
        return strings.parallelStream().filter(s -> s.contains(termLC) || s.trim().toLowerCase().equalsIgnoreCase(termLC)).map(s -> s.replaceAll("\\.", "[dot]")).collect(Collectors.toList());
    }
}
