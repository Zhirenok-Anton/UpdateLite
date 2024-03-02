package directory;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Directory {

    final static Logger logger = Logger.getLogger(Directory.class);
    private final String path;

    public Directory(String path){
        this.path = path;
    }

    public void createDirectors(String nameDirectory){
        logger.info("Create directors for RM Lite");
        try {
            new File(path + "\\" + nameDirectory).mkdir();
            new File(path + "\\" + nameDirectory + "\\rmlite").mkdir();
            new File(path + "\\" + nameDirectory + "\\rmlite\\s").mkdir();
            new File(path + "\\" + nameDirectory + "\\rmlite\\r").mkdir();
            new File(path + "\\" + nameDirectory + "\\rmlite\\Compro").mkdir();
            new File(path + "\\" + nameDirectory + "\\rmlite\\Pcards").mkdir();
            logger.info("Directors for RM Lite created");
        }catch (Exception e){
            logger.info("created Directors error " + e);
            e.printStackTrace();
        }
    }

    public boolean existsDirectory(String nameDirectory){
        return new File(path + "\\" + nameDirectory).exists();
    }

}
