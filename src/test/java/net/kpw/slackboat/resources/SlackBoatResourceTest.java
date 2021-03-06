package net.kpw.slackboat.resources;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.junit.ClassRule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;
import net.kpw.slackboat.core.DisposableEmailDomainList;
import net.kpw.slackboat.core.OpenPhish;
import net.kpw.slackboat.core.PhishTank;
import net.kpw.slackboat.core.ZeuSDomainList;
import net.kpw.slackboat.core.constant.Constants;
import net.kpw.slackboat.util.FileParser;

/**
 * @author kwilliford
 * @created Nov 9, 2017
 *
 */
public class SlackBoatResourceTest {

    private static final FileParser fileParser = FileParser.getInstance();
    private static final DisposableEmailDomainList disposableEmailDomainList = new DisposableEmailDomainList(
            fileParser.parseLines(SlackBoatResourceTest.class.getResourceAsStream("/disposable_email_blacklist.conf")));
    private static final PhishTank phishTank = new PhishTank(
            fileParser.parseURLsSecondColumn(SlackBoatResourceTest.class.getResourceAsStream("/phishtank.csv")));
    private static final OpenPhish openPhish = new OpenPhish(
            fileParser.parseLines(SlackBoatResourceTest.class.getResourceAsStream("/openphish.txt")));
    private static final ZeuSDomainList zeusDomainList = new ZeuSDomainList(
            fileParser.parseLines(SlackBoatResourceTest.class.getResourceAsStream("/ZeuS_bad_domains.txt")),
            fileParser.parseLines(SlackBoatResourceTest.class.getResourceAsStream("/ZeuS_ipv4_addresses.txt")));
    private static final String VERIFICATION_TOKEN = "verification_token_test";

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SlackBoatResource(disposableEmailDomainList, phishTank, openPhish, zeusDomainList, VERIFICATION_TOKEN))
            .build();

    /**
     * Test querying the api to check if a domain is in the disposable spam email blacklist [success result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testFindAnyMatch() throws IOException {
        // disposable spam emails
        Form input = new Form();
        input.param("token", VERIFICATION_TOKEN);
        input.param("text", disposableEmailDomainList.getDomains().iterator().next());
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/find_any_match").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals("The input was found in these databases:\nThe Disposable Email/Spam Blacklist", responseText);

        // phishtank
        input = new Form();
        input.param("token", VERIFICATION_TOKEN);
        input.param("text", phishTank.getUrls().iterator().next());
        entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/find_any_match").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals("The input was found in these databases:\nThe PhishTank phishing url list.", responseText);
        
        // openphish
        input = new Form();
        input.param("token", VERIFICATION_TOKEN);
        input.param("text", openPhish.getUrls().iterator().next());
        entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/find_any_match").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals("The input was found in these databases:\nThe OpenPhish phishing url list.", responseText);
        
        // ZeuS domain
        input = new Form();
        input.param("token", VERIFICATION_TOKEN);
        input.param("text", zeusDomainList.getDomains().iterator().next());
        entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/find_any_match").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals("The input was found in these databases:\nThe ZeuS trojan domain blacklist.", responseText);
        
        // ZeuS ipv4
        input = new Form();
        input.param("token", VERIFICATION_TOKEN);
        input.param("text", zeusDomainList.getIpv4Addresses().iterator().next());
        entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/find_any_match").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals("The input was found in these databases:\nThe ZeuS trojan ipv4 blacklist.", responseText);
    }

    /**
     * Test querying the api to check if a url is in the openphish blacklist [success result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsInOpenPhish() throws IOException {
        Form input = new Form();
        input.param("text", openPhish.getUrls().iterator().next());
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_in_openphish").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.OPENPHISH_URL_BLACKLISTED_TRUE, responseText);
    }

    /**
     * Test querying the api to check if a url is in the phishtank blacklist [success result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsInPhishTank() throws IOException {
        Form input = new Form();
        input.param("text", phishTank.getUrls().iterator().next());
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_in_phishtank").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.PHISHTANK_URL_BLACKLISTED_TRUE, responseText);
    }

    /**
     * Test querying the api to check if a domain is in the zeus domain blacklist [success result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsInZeusDomainBlacklist() throws IOException {
        Form input = new Form();
        input.param("text", zeusDomainList.getDomains().iterator().next());
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_zeus_domain").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.ZEUS_DOMAIN_BLACKLISTED_TRUE, responseText);
    }

    /**
     * Test querying the api to check if a ip is in the zeus ip blacklist [success result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsInZeusIPBlacklist() throws IOException {
        Form input = new Form();
        input.param("text", zeusDomainList.getIpv4Addresses().iterator().next());
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_zeus_ipv4").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.ZEUS_IP_BLACKLISTED_TRUE, responseText);
    }

    /**
     * Test querying the api to check if a domain is in the disposable spam email blacklist [fail result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsNotBlacklisted() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_spam_domain").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.DISPOSABLE_DOMAIN_BLACKLISTED_FALSE, responseText);
    }

    /**
     * Test querying the api to check if a url is in the openphish blacklist [fail result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsNotInOpenPhish() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_in_openphish").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.OPENPHISH_URL_BLACKLISTED_FALSE, responseText);
    }

    /**
     * Test querying the api to check if a url is in the phishtank blacklist [fail result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsNotInPhishTank() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_in_phishtank").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.PHISHTANK_URL_BLACKLISTED_FALSE, responseText);
    }

    /**
     * Test querying the api to check if a domain is in the zeus domain blacklist [fail result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsNotInZeusDomainBlacklist() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_zeus_domain").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.ZEUS_DOMAIN_BLACKLISTED_FALSE, responseText);
    }

    /**
     * Test querying the api to check if a ip is in the zeus ip blacklist [fail result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsNotInZeusIPBlacklist() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_zeus_ipv4").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.ZEUS_IP_BLACKLISTED_FALSE, responseText);
    }

    /**
     * Test querying the api to check if a domain is in the disposable spam email blacklist [success result].
     * 
     * @throws IOException
     *             if the response byte stream could not be read.
     */
    @Test
    public void testIsSpamDomainBlacklisted() throws IOException {
        Form input = new Form();
        input.param("text", disposableEmailDomainList.getDomains().iterator().next());
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/is_spam_domain").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        assertEquals(Constants.DISPOSABLE_DOMAIN_BLACKLISTED_TRUE, responseText);
    }

    @Test
    public void testSearch() throws IOException {
        Form input = new Form();
        input.param("text", "10mail");
        input.param("token", VERIFICATION_TOKEN);
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        String responseText = IOUtils.toString(
                (ByteArrayInputStream) resources.target("/api/search").request().post(entity).getEntity(), StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        sb.append("You searched for: ").append("10mail").append("\n")
        .append("Note: Some characters are escaped so that Slack will not create hyperlinks.\n");
        final String preamble = sb.toString();
        final String expectedResponse = preamble + "Found some results in the Disposable Email/Spam Blacklist:\n>10mail[dot]com\n>10mail[dot]org";
        assertEquals(expectedResponse, responseText);
    }
}
