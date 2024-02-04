package zip;

import CMD.CMD;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnpackZip {

    private final String path;
    private final String nameDirectory;
    private String nameFirstFolder;
    private File destDir;
    final static Logger logger = Logger.getLogger(CMD.class);

    public UnpackZip(String path, String nameDirectory){
        this.path = path;
        this.nameDirectory = nameDirectory;
    }

    public UnpackZip() {

        path = null;
        nameDirectory= null;
    }

    public void unpack(String zipVersion) throws IOException {
        logger.info("Распаковка архива - " + zipVersion);
        if (path!=null && nameDirectory != null){
            destDir = new File(path + "\\" + nameDirectory);
        }else {
            String path = zipVersion.replace(".zip",""); //обрезание ".zip" в конце строки
            destDir = new File(path);
        }

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(zipVersion)));
        ZipEntry zipEntry = zis.getNextEntry();
        assert zipEntry != null;
        nameFirstFolder = zipEntry.getName();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        logger.info("Архив Распакован - " + zipVersion);
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public void renameBaseFolder(){
        logger.info("Преименовать папку с версией");
        String[] parts = nameFirstFolder.split("/");
        new File(destDir.getPath() +"\\"+ parts[0]).
                renameTo(new File(destDir.getPath() + "\\shop-lite"));
    }

    public boolean existsDirectory(String nameDirectory){
        return new File(nameDirectory.replace(".zip","")).exists();
    }

}
