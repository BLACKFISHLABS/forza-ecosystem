package io.github.blackfishlabs.forza.core.application.viacep.service;

import com.fasterxml.jackson.jr.ob.JSON;
import io.github.blackfishlabs.forza.core.application.viacep.json.ViaCepJson;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ViaCepService {

    private static final String SERVICE_HOST = "viacep.com.br/ws/";
    protected JSON service = JSON.std;

    public ViaCepJson getAddress(String cep) throws IOException {
        char[] chars = cep.toCharArray();

        StringBuilder builder = new StringBuilder();
        for (char aChar : chars) {
            if (Character.isDigit(aChar)) {
                builder.append(aChar);
            }
        }
        cep = builder.toString();

        if (cep.length() != 8) {
            throw new IllegalArgumentException("CEP inválido - deve conter 8 dígitos: " + cep);
        }

        String urlString = getHost() + cep + "/json/";
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            ViaCepJson obj = getService().beanFrom(ViaCepJson.class, in);
            if (obj == null || obj.getCep() == null) {
                return null;
            }

            return obj;
        } finally {
            urlConnection.disconnect();
        }
    }

    private String getHost() {
        return (isUsingHTTPS() ? "https://" : "http://") + SERVICE_HOST;
    }

    private boolean isUsingHTTPS() {
        return false;
    }

    public JSON getService() {
        return service;
    }
}
