import CMD.CMD;
import file.DeleteFile;
import file.EditFile;
import JDBC.JdbcRunner;
import config.ObjectJSON;
import config.ParserGson;
import directory.Directory;
import org.apache.log4j.Logger;
import zip.UnpackZip;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

public class StartUpdateLite implements Runnable {

    final static Logger logger = Logger.getLogger(StartUpdateLite.class);
    static ObjectJSON  conf;
    private CMD cmd;
    private  static FileInputStream fis;
    private static Properties property;

    public static void main(String[] args){

        Thread thread = new Thread(new StartUpdateLite());

        try {
            logger.info("-----------------------------------------START process-----------------------------------------");

            property = new Properties();
            ParserGson parserGson = new ParserGson();

            fis = new FileInputStream("properties.properties");
            property.load(fis);

            conf = parserGson.pars(property.getProperty("path.conf"));

            Directory directory = new Directory(conf.getPath());
            UnpackZip unpackBaseZip = new UnpackZip(conf.getPath(), conf.getNameShop_Lite());
            UnpackZip unpackUpdateZip = new UnpackZip();
            EditFile editFile = new EditFile(conf.getPath(), conf.getNameShop_Lite());

            if (directory.existsDirectory(conf.getNameShop_Lite())){
                logger.warn("This project directory already exists - " + conf.getNameShop_Lite());
            //}else if (unpackUpdateZip.existsDirectory(conf.getUpdateVersion())) {
                logger.warn("This archive has already been unzipped - " + conf.getUpdateVersion());
            } else{
                thread.start();//запуск потока на создание БД
                directory.createDirectors(conf.getNameShop_Lite());
                unpackBaseZip.unpack(property.getProperty("path.baseZip"));
                unpackBaseZip.renameBaseFolder();
                unpackUpdateZip.unpack(conf.getUpdateVersion());
                editFile.editeFileDbConnection();
                editFile.editeFileLauncherConf(conf.getUpdateVersion());
                thread.join();
                new DeleteFile(conf.getPath() + "\\" + conf.getNameShop_Lite() + "\\dump.sql");
                dialog();
            }
        }catch (Exception e){
            logger.error("Something went wrong =( " + e);
        }finally {
            logger.info("-----------------------------------------END process-------------------------------------------");
        }
    }

    @Override
    public void run() {
        cmd = new CMD();
        JdbcRunner jdbcRunner = new JdbcRunner(conf.getPath(), conf.getNameShop_Lite(),
                conf.getDb().getConnection().getDbUrl() + "/postgres-" + conf.getNameShop_Lite(),
                conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass());

        cmd.createDB(conf.getDb().getPostgresPath(), conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass(), "postgres-" + conf.getNameShop_Lite());
        cmd.damp(conf.getDb().getPostgresPath(), conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass(), "postgres-" + conf.getNameShop_Lite(), property.getProperty("path.dump"));
        jdbcRunner.updateParameterValueLoadDictionaryDirectory();
        jdbcRunner.updateParameterValueComProServiceIntegrationDirectory();
        jdbcRunner.updateParameterValuePCardsServiceIntegrationDirectory();
        jdbcRunner.updateSequenceBasketId();
        jdbcRunner.updateSequencePortionId();
        jdbcRunner.updateDB(conf.getDb().getScriptsSQL());
    }

    private static void dialog()  {
        Scanner in = new Scanner(System.in);
        CMD cmd = new CMD();
        System.out.print("You running "+ conf.getPath() + "\\" + conf.getNameShop_Lite() + "\\shop-lite\\launcher.exe???" +
                " \n [Y/N] - ");
        String YN = in.next();
        if(YN.equals("Y")){
            cmd.startExe(conf.getPath() + "\\" + conf.getNameShop_Lite() + "\\shop-lite");
        }
        else if (YN.equals("N")){
            logger.warn("automatic RM lite is not running");
        }
        else {
            dialog();
        }
    }
}
