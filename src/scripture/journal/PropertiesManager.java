/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author David
 */
public class PropertiesManager {
    
    public static final String PROP_FILENAME = "Properties.properties";
    
    public Properties getProperties() throws IOException {
        Properties result = new Properties();
        InputStream iStream = getClass().getResourceAsStream(PROP_FILENAME);
        
        if (iStream == null) {
            throw new FileNotFoundException("property file not found");
        }
        try {
            result.load(iStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
}
