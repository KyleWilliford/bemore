package net.kpw.slackboat.resources;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.kpw.slackboat.SlackClientAppConfiguration;

/**
 * Slack authorization resource.
 * 
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
@Path("/auth")
public class SlackOAuthResource {
    private static final Log LOG = LogFactory.getLog(SlackOAuthResource.class);

    private final SlackClientAppConfiguration slackClientAppConfiguration;
    private final HttpClient httpClient;

    public SlackOAuthResource(final SlackClientAppConfiguration slackClientAppConfiguration, final HttpClient httpClient) {
        this.slackClientAppConfiguration = slackClientAppConfiguration;
        this.httpClient = httpClient;
    }

    /**
     * OAuth authorization method.
     * 
     * @param request
     *            The Http request.
     * @return A {@link Response}.
     */
    @GET
    public Response authorize(@Context HttpServletRequest request) {
        final String error = request.getParameter("error");
        if (StringUtils.isNotBlank(error) && "access_denied".equals(error)) {
            LOG.debug(error);
            return Response.ok().entity("Oh, you don't want to install me, huh? Well okay... bye.").build();
        }
        final String code = request.getParameter("code");
        LOG.debug(code);
        if (StringUtils.isBlank(code)) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        StringBuilder sb = new StringBuilder("https://slack.com/api/oauth.access?code=").append(code);
        sb.append("&client_id=").append(slackClientAppConfiguration.getClientID()).append("&client_secret=")
                .append(slackClientAppConfiguration.getClientSecret());
        final String uri = sb.toString();
        LOG.debug(uri);
        if (StringUtils.isBlank(uri)) {
            return Response.serverError().entity("Sorry, something went wrong!").build();
        }
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            StringWriter writer = new StringWriter();
            IOUtils.copy(entity.getContent(), writer, "UTF-8");
            String accessResponse = writer.toString();
            EntityUtils.consume(entity);
            LOG.debug(accessResponse);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(accessResponse);
            Boolean ok = jsonNode.get("ok").asBoolean();
            if (ok) {
                return Response.ok().entity("Successfully installed slackboat to your workspace! Go try it out in your Slack client!")
                        .build();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        LOG.warn("Could not install slackboat to user's workspace.");
        return Response.status(Status.UNAUTHORIZED)
                .entity("Sorry, something went wrong! Slackboat could not be installed to your workspace.").build();
    }
}
