package file;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OpenFile {

    String path ;
    public OpenFile(String path) throws IOException {
        this.path = path;
        startLauncher();
    }

    public void startLauncher() throws IOException {

            Desktop.getDesktop().open(new File(path));

    }
}
