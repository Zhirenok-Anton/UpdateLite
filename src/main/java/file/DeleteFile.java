package file;

import directory.Directory;
import org.apache.log4j.Logger;

import java.io.File;

public class DeleteFile {

    final static Logger logger = Logger.getLogger(DeleteFile.class);
    private String path;

    public DeleteFile(String path){
        this.path = path;
        delete();
    }
    private void  delete(){
        logger.info("Deleted file" + path);
        new File(path).delete();
    }
}
