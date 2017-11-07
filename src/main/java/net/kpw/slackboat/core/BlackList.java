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
abstract class BlackList {
    protected IBlackList domainBlackListImpl = new DomainBlackListImpl();
    protected IBlackList ipv4BlackListImpl = new DomainBlackListImpl();
    protected IBlackList urlBlackListImpl = new UrlBlackListImpl();
}
