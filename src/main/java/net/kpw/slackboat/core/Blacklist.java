package net.kpw.slackboat.core;

/**
 * Abstract class that represents actions that can be enacted on a "blacklist" of strings (domains, urls, ip addresses, etc.).
 * 
 * Abstraction in the bridge pattern.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
abstract class Blacklist {
    protected IBlacklist domainBlackListImpl = new DomainBlacklistImpl();
    protected IBlacklist ipv4BlackListImpl = new IPv4BlacklistImpl();
    protected IBlacklist urlBlackListImpl = new UrlBlacklistImpl();
}
