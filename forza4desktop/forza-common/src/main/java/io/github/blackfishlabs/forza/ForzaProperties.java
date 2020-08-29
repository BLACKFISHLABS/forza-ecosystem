package io.github.blackfishlabs.forza;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public final class ForzaProperties implements IPath, IParam {

    private static final String DIR_APP = System.getProperty("user.dir");
    private static final String DIR_PROPERTIES = DIR_APP + File.separator + "properties";
    private static final String FILE_PROPERTIES = DIR_PROPERTIES + File.separator + "forza.properties";

    public static ForzaProperties instance;

    private Properties prop;

    public ForzaProperties() {
        prop = new Properties();
        try {
            prop.load(new FileInputStream(FILE_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ForzaProperties getInstance() {
        return Objects.isNull(instance) ? new ForzaProperties() : instance;
    }

    @Override
    public String getApiKey() {
        return prop.getProperty(ForzaPropertiesConstants.KEY_PARAM_APIKEY);
    }

    @Override
    public String getProduction() {
        return prop.getProperty(ForzaPropertiesConstants.KEY_PARAM_PRODUCTION);
    }

    @Override
    public String getTestUrl() {
        return prop.getProperty(ForzaPropertiesConstants.KEY_PARAM_TEST_URL);
    }

    @Override
    public String getDirApplication() {
        return prop.getProperty(ForzaPropertiesConstants.KEY_DIRECTORY_APPLICATION);
    }

    @Override
    public String getDirImg() {
        return prop.getProperty(ForzaPropertiesConstants.KEY_DIRECTORY_IMG);
    }

    @Override
    public String getDirRet() {
        return prop.getProperty(ForzaPropertiesConstants.KEY_DIRECTORY_RETURN_FILE);
    }
}
