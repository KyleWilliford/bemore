package net.kpw.slackboat.resources;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.junit.ClassRule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;
import net.kpw.slackboat.core.DisposableMalwareDomainList;
import net.kpw.slackboat.core.OpenPhish;
import net.kpw.slackboat.core.PhishTank;
import net.kpw.slackboat.core.ZeuSDomainList;
import net.kpw.slackboat.util.FileParser;

/**
 * @author kwilliford
 * @created Nov 9, 2017
 *
 */
public class SlackBoatResourceSSLCheckTest {

    private static final FileParser fileParser = FileParser.getInstance();
    private static final DisposableMalwareDomainList disposableMalwareDomainList = new DisposableMalwareDomainList(
            fileParser.parseLines(SlackBoatResourceSSLCheckTest.class.getResourceAsStream("/disposable_email_blacklist.conf")));
    private static final PhishTank phishTank = new PhishTank(
            fileParser.parseURLsSecondColumn(SlackBoatResourceSSLCheckTest.class.getResourceAsStream("/phishtank.csv")));
    private static final OpenPhish openPhish = new OpenPhish(
            fileParser.parseLines(SlackBoatResourceSSLCheckTest.class.getResourceAsStream("/openphish.txt")));
    private static final ZeuSDomainList zeusDomainList = new ZeuSDomainList(
            fileParser.parseLines(SlackBoatResourceSSLCheckTest.class.getResourceAsStream("/ZeuS_bad_domains.txt")),
            fileParser.parseLines(SlackBoatResourceSSLCheckTest.class.getResourceAsStream("/ZeuS_ipv4_addresses.txt")));
    private static final String VERIFICATION_TOKEN = "verification_token_test";

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SlackBoatResource(disposableMalwareDomainList, phishTank, openPhish, zeusDomainList, VERIFICATION_TOKEN))
            .build();

    /**
     * Test querying the api to perform an ssl check. https://api.slack.com/slash-commands#ssl
     * 
     * @throws IOException
     */
    @Test
    public void testSSLCheckFindAnyMatch() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        input.param("ssl_check", "1");
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = resources.target("/api/find_any_match").request().post(entity);
        String responseText = IOUtils.toString((ByteArrayInputStream) response.getEntity(), StandardCharsets.UTF_8);
        assertEquals("", responseText);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test querying the api to perform an ssl check. https://api.slack.com/slash-commands#ssl
     * 
     * @throws IOException
     */
    @Test
    public void testSSLCheckOpenPhish() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        input.param("ssl_check", "1");
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = resources.target("/api/is_in_openphish").request().post(entity);
        String responseText = IOUtils.toString((ByteArrayInputStream) response.getEntity(), StandardCharsets.UTF_8);
        assertEquals("", responseText);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test querying the api to perform an ssl check. https://api.slack.com/slash-commands#ssl
     * 
     * @throws IOException
     */
    @Test
    public void testSSLCheckPhishtank() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        input.param("ssl_check", "1");
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = resources.target("/api/is_in_phishtank").request().post(entity);
        String responseText = IOUtils.toString((ByteArrayInputStream) response.getEntity(), StandardCharsets.UTF_8);
        assertEquals("", responseText);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test querying the api to perform an ssl check. https://api.slack.com/slash-commands#ssl
     * 
     * @throws IOException
     */
    @Test
    public void testSSLCheckSpamDomainBlacklist() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        input.param("ssl_check", "1");
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = resources.target("/api/is_spam_domain").request().post(entity);
        String responseText = IOUtils.toString((ByteArrayInputStream) response.getEntity(), StandardCharsets.UTF_8);
        assertEquals("", responseText);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test querying the api to perform an ssl check. https://api.slack.com/slash-commands#ssl
     * 
     * @throws IOException
     */
    @Test
    public void testSSLCheckZeusDomainBlacklist() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        input.param("ssl_check", "1");
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = resources.target("/api/is_zeus_domain").request().post(entity);
        String responseText = IOUtils.toString((ByteArrayInputStream) response.getEntity(), StandardCharsets.UTF_8);
        assertEquals("", responseText);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    /**
     * Test querying the api to perform an ssl check. https://api.slack.com/slash-commands#ssl
     * 
     * @throws IOException
     */
    @Test
    public void testSSLCheckZeusIPv4Blacklist() throws IOException {
        Form input = new Form();
        input.param("text", "");
        input.param("token", VERIFICATION_TOKEN);
        input.param("ssl_check", "1");
        Entity<?> entity = Entity.entity(input, MediaType.APPLICATION_FORM_URLENCODED);
        Response response = resources.target("/api/is_zeus_ipv4").request().post(entity);
        String responseText = IOUtils.toString((ByteArrayInputStream) response.getEntity(), StandardCharsets.UTF_8);
        assertEquals("", responseText);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
}
