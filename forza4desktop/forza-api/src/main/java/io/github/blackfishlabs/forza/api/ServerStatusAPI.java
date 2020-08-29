package io.github.blackfishlabs.forza.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.blackfishlabs.forza.ForzaConstants;
import io.github.blackfishlabs.forza.ForzaProperties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

@Path("/status")
public class ServerStatusAPI {

    private static final Logger logger = LogManager.getLogger(ServerStatusAPI.class);

    @GET
    @Path("/server")
    @Produces("text/plain")
    @Consumes("application/x-www-form-urlencoded")
    public Response status() {

        try {
            logger.info("Call status() method");

            String url;
            if ("NAO".equals(ForzaProperties.getInstance().getProduction())) {
                url = ForzaProperties.getInstance().getTestUrl() + "/actuator/health";
            } else {
                url = ForzaConstants.URL_PRODUCTION + "/actuator/health";
            }

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            request.setHeader("Content-Type", "application/json");
            request.setHeader("Cache-Control", "no-store");
            request.setHeader("Pragma", "no-cache");

            HttpResponse response = client.execute(request);

            logger.info("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map;

            // convert JSON string to Map
            map = mapper.readValue(result.toString(), new TypeReference<Map<String, String>>() {
            });
            String status = (String) map.get("status");

            return Response.status(200).entity(status).build();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Response.status(500).entity("Exception: ".concat(ex.getMessage())).build();
        }
    }
}
