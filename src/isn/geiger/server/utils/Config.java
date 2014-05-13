package isn.geiger.server.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author LoadLow
 */
public class Config {

    private XMLConfiguration xmlConfig;

    public Config(String filePath) throws ConfigurationException {
        this.xmlConfig = new XMLConfiguration();
        xmlConfig.setListDelimiter(';');
        xmlConfig.setFileName(filePath);
        xmlConfig.setAttributeSplittingDisabled(true);
        xmlConfig.load();
    }

    public Configuration getConfiguration() {
        return xmlConfig;
    }
}
