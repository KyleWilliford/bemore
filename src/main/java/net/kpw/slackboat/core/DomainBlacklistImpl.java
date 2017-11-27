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
 * Concrete implementor for the bridge design pattern. This class is used for domain name related implementation methods.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
class DomainBlacklistImpl implements IBlacklist {
    static final Log LOG = LogFactory.getLog(DomainBlacklistImpl.class);

    @Override
    public boolean isBlacklisted(final String input, final Set<String> strings) {
        LOG.debug(input);
        if (StringUtils.isBlank(input) || CollectionUtils.isEmpty(strings)) {
            return false;
        }
        final String inputLowerCase = input.trim().toLowerCase();
        String[] split = inputLowerCase.split("@");
        if (split.length == 0) {
            return false;
        } else if (split.length == 1) {
            return !strings.parallelStream().filter(s -> s.trim().toLowerCase().equalsIgnoreCase(split[0])).collect(Collectors.toList()).isEmpty();
        } else if (split.length > 1) {
            return !strings.parallelStream().filter(s -> s.trim().toLowerCase().equalsIgnoreCase(split[1])).collect(Collectors.toList()).isEmpty();
        }
        return false;
    }

    @Override
    public List<String> search(final String term, final Set<String> strings) {
        if (StringUtils.isBlank(term) || CollectionUtils.isEmpty(strings)) {
            return new ArrayList<>();
        }
        final String termLC = term.trim().toLowerCase();
        LOG.debug(term);
        return strings.parallelStream().filter(s -> s.contains(termLC) || s.trim().toLowerCase().equalsIgnoreCase(termLC)).map(s -> s.replaceAll("\\.", "[dot]")).collect(Collectors.toList());
    }
}
