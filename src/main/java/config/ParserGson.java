package config;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

public class ParserGson {

    final static Logger logger = Logger.getLogger(ParserGson.class);


    public ObjectJSON pars(String pathConfFile){

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(pathConfFile)){
            logger.info("Parsing file config.json");
            ObjectJSON objectJSON = gson.fromJson(reader,ObjectJSON.class);
            return objectJSON;
        }catch (Exception e){
            logger.error("parsing error " + e);
        }
        return null;
    }
}
