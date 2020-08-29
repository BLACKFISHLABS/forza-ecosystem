package io.github.blackfishlabs.forza.api;

import io.github.blackfishlabs.forza.ForzaConstants;
import io.github.blackfishlabs.forza.ForzaHelper;
import io.github.blackfishlabs.forza.ForzaProperties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Path("/order")
public class OrderAPI {

    private static final Logger logger = LogManager.getLogger(ChargeAPI.class);

    @POST
    @Path("/status/invoiced")
    @Produces("text/plain")
    @Consumes("application/x-www-form-urlencoded")
    public Response changeStatusInvoiced(@FormParam("order") String order) {
        try {
            logger.info("Call changeStatus() method");

            String url;
            if ("NAO".equals(ForzaProperties.getInstance().getProduction())) {
                url = ForzaProperties.getInstance().getTestUrl() + ForzaConstants.URL_API_V1 + "/order/" + order;
            } else {
                url = ForzaConstants.URL_PRODUCTION + ForzaConstants.URL_API_V1 + "/order/" + order;
            }

            HttpResponse response = executeGet(url);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                if ("NAO".equals(ForzaProperties.getInstance().getProduction())) {
                    url = ForzaProperties.getInstance().getTestUrl() + ForzaConstants.URL_API_V1 + "/order";
                } else {
                    url = ForzaConstants.URL_PRODUCTION + ForzaConstants.URL_API_V1 + "/order";
                }

                logger.info(result.toString());
                String json = result.toString()
                        .replace("\"status\":0", "\"status\":5")
                        .replace("\"status\":1", "\"status\":5")
                        .replace("\"status\":2", "\"status\":5");

                HttpClient client = HttpClientBuilder.create().build();
                HttpPut request = new HttpPut(url);

                String newJson = ForzaHelper.validation(json);
                StringEntity body = new StringEntity(newJson, ContentType.APPLICATION_JSON);

                request.setHeader("Accept", "application/json");
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Authorization", "Basic " + ForzaProperties.getInstance().getApiKey());
                request.setHeader("Cache-Control", "no-store");
                request.setHeader("Pragma", "no-cache");
                request.setEntity(body);

                HttpResponse httpResponse = client.execute(request);

                logger.info("Response Code : " + httpResponse.getStatusLine().getStatusCode());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

                StringBuilder builder = new StringBuilder();
                String lineAdd;
                while ((lineAdd = bufferedReader.readLine()) != null) {
                    builder.append(lineAdd);
                }

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    return Response.status(200).entity("[OK] - Status convertido para Faturado.").build();
                } else {
                    logger.error(builder.toString());
                    return Response.status(500).entity("Exception: ".concat(builder.toString())).build();
                }
            } else {
                logger.error(result.toString());
                return Response.status(500).entity("Exception: ".concat(result.toString())).build();
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Response.status(500).entity("Exception: ".concat(ex.getMessage())).build();
        }
    }

    private HttpResponse executeGet(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Basic " + ForzaProperties.getInstance().getApiKey());
        request.setHeader("Cache-Control", "no-store");
        request.setHeader("Pragma", "no-cache");

        HttpResponse response = client.execute(request);
        logger.info("Response Code : " + response.getStatusLine().getStatusCode());

        return response;
    }
}
