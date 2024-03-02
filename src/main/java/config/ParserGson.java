package config;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.FileReader;

public class ParserGson {

    final static Logger logger = Logger.getLogger(ParserGson.class);

    public ObjectJSON pars(){
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("C:\\Users\\AZhirenok\\IdeaProjects\\UpdateLite\\out\\artifacts\\UpdateLite_jar\\config.json")){
            logger.info("Парсим фаил config.json");
            ObjectJSON objectJSON = gson.fromJson(reader,ObjectJSON.class);
            return objectJSON;
        }catch (Exception e){
            logger.error("pars error " + e);
        }
        return null;
    }
}
