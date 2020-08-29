package io.github.blackfishlabs.forza.core.application.receita.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.core.application.receita.json.DadosReceitaJson;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ReceitaWSService {

    private static final String URL_SEFAZ = "http://www.receitaws.com.br/v1/cnpj/";

    public DadosReceitaJson getCNPJ(String cnpj) throws RuntimeException {
        if (!cnpj.isEmpty() || !cnpj.equals("")) {
            try {
                URL url = new URL(URL_SEFAZ + cnpj);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                String content = convertToInputStreamParaString(connection.getInputStream());
                connection.disconnect();

                return convertToObject(content);

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Passe o numero do cnpj valido para pesquisar");
        }
    }

    private static String convertToInputStreamParaString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    private static DadosReceitaJson convertToObject(String content) throws IOException {
        if (Strings.isNullOrEmpty(content) || content.contains("ERROR")) {
            throw new IllegalArgumentException("Não existe dados para o cnpj informado");
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            return mapper.readValue(content, DadosReceitaJson.class);
        }
    }
}
