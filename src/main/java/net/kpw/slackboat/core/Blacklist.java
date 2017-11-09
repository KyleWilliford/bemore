package net.kpw.slackboat.core;

/**
 * Abstraction that represents abstract actions that can be enacted on a blacklist (domains, urls, ip addresses, etc.).
 * 
 * Abstraction in bridge pattern.
 * 
 * @author kwilliford
 * @created Nov 7, 2017
 *
 */
abstract class Blacklist {
    protected IBlacklist domainBlackListImpl = new DomainBlacklistImpl();
    protected IBlacklist ipv4BlackListImpl = new DomainBlacklistImpl();
    protected IBlacklist urlBlackListImpl = new UrlBlacklistImpl();
}
