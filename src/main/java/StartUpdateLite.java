import CMD.CMD;
import file.DeleteFile;
import file.EditFile;
import JDBC.JdbcRunner;
import config.ObjectJSON;
import config.ParserGson;
import directory.Directory;
import file.OpenFile;
import org.apache.log4j.Logger;
import zip.UnpackZip;

import java.io.IOException;
import java.util.Scanner;

public class StartUpdateLite implements Runnable {

    final static Logger logger = Logger.getLogger(StartUpdateLite.class);
    static ObjectJSON  conf;
    public static void main(String[] args){
        Thread thread = new Thread(new StartUpdateLite());

        try {
            logger.info("-----------------------------------------START process-----------------------------------------");
            ParserGson parserGson = new ParserGson();
            conf = parserGson.pars();

            Directory directory = new Directory(conf.getPath());
            UnpackZip unpackBaseZip = new UnpackZip(conf.getPath(), conf.getNameShop_Lite());
            UnpackZip unpackUpdateZip = new UnpackZip();
            EditFile editFile = new EditFile(conf.getPath(), conf.getNameShop_Lite());

            if (directory.existsDirectory(conf.getNameShop_Lite())){
                logger.warn("Такая директория проекта уже существует - " + conf.getNameShop_Lite());
            //}else if (unpackUpdateZip.existsDirectory(conf.getUpdateVersion())) {
                logger.warn("Такой архив уже разархивирован - " + conf.getUpdateVersion());
            } else{

                //создание директорий
                directory.createDirectors(conf.getNameShop_Lite());
                //распаковать базовый архив
                unpackBaseZip.unpack("C:\\Users\\AZhirenok\\IdeaProjects\\UpdateLite\\out\\artifacts\\UpdateLite_jar\\shop-lite.zip");
                unpackBaseZip.renameBaseFolder();
                thread.start();
                //распаковка версии с обновлением
                unpackUpdateZip.unpack(conf.getUpdateVersion());

                editFile.editeFileDbConnection();
                editFile.editeFileLauncherConf(conf.getUpdateVersion());
                thread.join();
                new DeleteFile(conf.getPath() + "\\" + conf.getNameShop_Lite() + "\\dump.sql");
                dialog();
            }
        }catch (Exception e){
            logger.error("Что то пошло не так =( " + e);
        }finally {
            logger.info("-----------------------------------------END process-----------------------------------------");
        }
    }

    @Override
    public void run() {
        CMD cmd = new CMD();
        JdbcRunner jdbcRunner = new JdbcRunner(conf.getPath(), conf.getNameShop_Lite(),
                conf.getDb().getConnection().getDbUrl() + "/postgres-" + conf.getNameShop_Lite(),
                conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass());

        //создание БД
        cmd.createDB(conf.getDb().getPostgresPath(), conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass(), "postgres-" + conf.getNameShop_Lite());
        cmd.damp(conf.getDb().getPostgresPath(), conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass(), "postgres-" + conf.getNameShop_Lite(), conf.getPath() + "\\" + conf.getNameShop_Lite() + "\\dump.sql");
        jdbcRunner.updateParameterValueLoadDictionaryDirectory();
        jdbcRunner.updateParameterValueComProServiceIntegrationDirectory();
        jdbcRunner.updateParameterValuePCardsServiceIntegrationDirectory();
        jdbcRunner.updateSequenceBasketId();
        jdbcRunner.updateSequencePortionId();
        jdbcRunner.updateDB(conf.getDb().getScriptsSQL());
    }

    private static void dialog() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Запустить обновление лайта? ");
        String YN = in.next();
        if(YN.equals("Y")){
            System.out.println("Обновление запущено");
            new OpenFile(conf.getPath() + "\\" + conf.getNameShop_Lite() + "\\shop-lite\\launcher.exe");
        }
        else if (YN.equals("N")){
            System.out.println("Обновление НЕ запущено");
        }
        else {
            dialog();
        }
    }
}
