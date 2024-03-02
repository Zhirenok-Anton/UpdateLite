package file;

import java.io.File;

public class DeleteFile {

    private String path;

    public DeleteFile(String path){
        this.path = path;
        delete();
    }
    private void  delete(){
        new File(path).delete();
    }
}
