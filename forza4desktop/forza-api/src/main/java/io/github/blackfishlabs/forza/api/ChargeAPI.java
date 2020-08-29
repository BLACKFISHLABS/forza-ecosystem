package io.github.blackfishlabs.forza.api;

import io.github.blackfishlabs.forza.ForzaConstants;
import io.github.blackfishlabs.forza.ForzaHelper;
import io.github.blackfishlabs.forza.ForzaProperties;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

@Path("/carga")
public class ChargeAPI {

    private static final Logger logger = LogManager.getLogger(ChargeAPI.class);

    @POST
    @Path("/upload")
    @Produces("text/plain")
    @Consumes("application/x-www-form-urlencoded")
    public Response upload(@FormParam("json") String json) {
        try {
            logger.info("Call upload() method");

            String newJson = ForzaHelper.validation(json);
            StringEntity body = new StringEntity(newJson, ContentType.APPLICATION_JSON);

            logger.info("JSON de envio:");
            logger.info(newJson);

            String url;
            if ("NAO".equals(ForzaProperties.getInstance().getProduction())) {
                url = ForzaProperties.getInstance().getTestUrl() + ForzaConstants.URL_API_V1 + "/charge";
            } else {
                url = ForzaConstants.URL_PRODUCTION + ForzaConstants.URL_API_V1 + "/charge";
            }

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Basic " + ForzaProperties.getInstance().getApiKey());
            request.setHeader("Cache-Control", "no-store");
            request.setHeader("Pragma", "no-cache");
            request.setEntity(body);

            HttpResponse response = client.execute(request);

            logger.info("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info(result.toString());
                return Response.status(200).entity(result.toString()).build();
            } else {
                logger.error(result.toString());
                return Response.status(500).entity("Exception: ".concat(result.toString())).build();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Response.status(500).entity("Exception: ".concat(ex.getMessage())).build();
        }
    }

    @POST
    @Path("/download")
    @Produces("text/plain")
    @Consumes("application/x-www-form-urlencoded")
    public Response download(@FormParam("cnpj") String cnpj) {
        try {
            logger.info("Call download() method");

            String url;
            if ("NAO".equals(ForzaProperties.getInstance().getProduction())) {
                url = ForzaProperties.getInstance().getTestUrl() + ForzaConstants.URL_API_V1 + "/order/mobile?cnpj=" + cnpj;
            } else {
                url = ForzaConstants.URL_PRODUCTION + ForzaConstants.URL_API_V1 + "/order/mobile?cnpj=" + cnpj;
            }

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "Basic " + ForzaProperties.getInstance().getApiKey());
            request.setHeader("Cache-Control", "no-store");
            request.setHeader("Pragma", "no-cache");

            HttpResponse response = client.execute(request);

            logger.info("Response Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            String lineFeed = Character.toString((char) 13);
            while ((line = rd.readLine()) != null) {
                result.append(line.concat(lineFeed));
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                String filename = UUID.randomUUID().toString().replaceAll("-", "").concat(".RET");
                String path = ForzaProperties.getInstance().getDirRet().concat(ForzaHelper.toDirFormat(new Date()))
                        .concat("/")
                        .concat(filename);
                FileUtils.writeStringToFile(new File(path), result.toString());

                return Response.status(200).entity(path).build();
            } else {
                logger.error(result.toString());
                return Response.status(500).entity("Exception: ".concat(result.toString())).build();
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Response.status(500).entity("Exception: ".concat(ex.getMessage())).build();
        }
    }
}
