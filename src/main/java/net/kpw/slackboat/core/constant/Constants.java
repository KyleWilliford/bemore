package net.kpw.slackboat.core.constant;

public abstract class Constants {
    public static String DISPOSABLE_DOMAIN_BLACKLISTED_TRUE = "I found that domain name in my database of disposable spam email domains.";
    public static String DISPOSABLE_DOMAIN_BLACKLISTED_FALSE = "I couldn't find that domain in my database of disposable spam email domains.";

    public static String PHISHTANK_URL_BLACKLISTED_TRUE = "I found that URL in my database of PhishTank urls.";
    public static String PHISHTANK_URL_BLACKLISTED_FALSE = "I couldn't find that url in my database of PhishTank urls.";

    public static String OPENPHISH_URL_BLACKLISTED_TRUE = "I found that URL in my database of OpenPhish urls.";
    public static String OPENPHISH_URL_BLACKLISTED_FALSE = "I couldn't find that url in my database of OpenPhish urls.";

    public static String ZEUS_IP_BLACKLISTED_TRUE = "I found that IP address in my database of ZeuS trojan ip addresses.";
    public static String ZEUS_IP_BLACKLISTED_FALSE = "I couldn't find that ip address in my database of ZeuS trojan ip addresses.";
    public static String ZEUS_DOMAIN_BLACKLISTED_TRUE = "I found that domain name in my database of ZeuS trojan domains.";
    public static String ZEUS_DOMAIN_BLACKLISTED_FALSE = "I couldn't find that domain in my database of ZeuS trojan domains.";

    public static final String VERIFICATION_TOKEN_INVALID = "Verification token invalid.";
}
