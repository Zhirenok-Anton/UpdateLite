package file;

import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EditFile {

    private final String path;
    private final String projectName;

    final static Logger logger = Logger.getLogger(EditFile.class);

    public EditFile(String path, String projectName){
        this.path = path;
        this.projectName = projectName;
    }

    public void editeFileDbConnection() {
        logger.info("Edite File for database connection");
        String search = "jdbc:postgresql://localhost:5432/postgres";
        String replace = "jdbc:postgresql://localhost:5432/postgres-" + projectName;
        Path path = Paths.get(this.path + "\\" + projectName + "\\shop-lite\\config\\db.connection.properties");

        Charset charset = StandardCharsets.UTF_8;

        try {
            Files.write(path,
                    new String(Files.readAllBytes(path), charset).replace(search, replace)
                            .getBytes(charset));
            logger.info("File for database connection edited");
        } catch (IOException e) {
            logger.error("Error when editing file for database connection edited" + e);
        }

    }

    public void editeFileLauncherConf(String zipBaseVersion) throws IOException {
        logger.info("Edite file " + path + "\\" + projectName + "\\shop-lite\\launcher.conf");
        FileWriter writer = null;
        try {
            writer = new FileWriter(path + "\\" + projectName + "\\shop-lite\\launcher.conf", true);
            writer.write("\n" +
                    "[NO UPDATE]\n" +
                    "UpdateServer = " + zipBaseVersion.replace(".zip",""));
            logger.info("file " + path + "\\" + projectName + "\\shop-lite\\launcher.conf" + " edited");
        } catch (IOException e) {
            logger.error("Error when edited file " + path + "\\" + projectName + "\\shop-lite\\launcher.conf " + e);
        }
        finally {
            writer.close();
        }
    }
}
