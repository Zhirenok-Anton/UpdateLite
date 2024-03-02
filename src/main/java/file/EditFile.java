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

    public void editeFileDbConnection() throws IOException {
        logger.info("Изменение файла для подключения базы данных");
        String search = "jdbc:postgresql://localhost:5432/postgres";
        String replace = "jdbc:postgresql://localhost:5432/postgres-" + projectName;
        Path path = Paths.get(this.path + "\\" + projectName + "\\shop-lite\\config\\db.connection.properties");

        Charset charset = StandardCharsets.UTF_8;

        Files.write(path,
                new String(Files.readAllBytes(path), charset).replace(search, replace)
                        .getBytes(charset));
        logger.info("Файл для подключения базы данных изменен");
    }

    public void editeFileLauncherConf(String zipBaseVersion) throws IOException {
        logger.info("Изменение файла для 'launcher.conf'");
        System.out.println("INFO: изменение фаила конфигурации");
        FileWriter writer = new FileWriter(path + "\\" + projectName + "\\shop-lite\\launcher.conf", true);
        writer.write("\n" +
                "[NO UPDATE]\n" +
                "UpdateServer = " + zipBaseVersion.replace(".zip",""));
        writer.close();
        logger.info("Файл 'launcher.conf' изменен");
    }
}
