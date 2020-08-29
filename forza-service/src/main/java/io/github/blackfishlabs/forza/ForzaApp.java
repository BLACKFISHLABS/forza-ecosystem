package io.github.blackfishlabs.forza;

import io.github.blackfishlabs.forza.core.config.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class ForzaApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForzaApp.class);

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext app = SpringApplication.run(ApplicationConfiguration.class, args);

        String applicationName = app.getEnvironment().getProperty("spring.application.name");
        String contextPath = app.getEnvironment().getProperty("server.contextPath");
        String port = app.getEnvironment().getProperty("server.port");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();

        LOGGER.info("\n|------------------------------------------------------------" +
                "\n|   Application '" + applicationName + "' is running!            " +
                "\n|   Access URLs:                                                 " +
                "\n|   Local:      http://127.0.0.1:" + port + contextPath +
                "\n|   External:   http://" + hostAddress + ":" + port + contextPath +
                "\n|-------------------------------------------------------------");
    }
}