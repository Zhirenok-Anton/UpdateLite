import CMD.CMD;
import file.EditFile;
import JDBC.JdbcRunner;
import config.ObjectJSON;
import config.ParserGson;
import directory.Directory;
import org.apache.log4j.Logger;
import zip.UnpackZip;

public class StartUpdateLite {

    final static Logger logger = Logger.getLogger(StartUpdateLite.class);
    public static void main(String[] args){

        try {
            logger.info("-----------------------------------------START process-----------------------------------------");
            ParserGson parserGson = new ParserGson();
            ObjectJSON conf = parserGson.pars();

            CMD cmd = new CMD();
            Directory directory = new Directory(conf.getPath());
            UnpackZip unpackBaseZip = new UnpackZip(conf.getPath(), conf.getNameShop_Lite());
            UnpackZip unpackUpdateZip = new UnpackZip();
            EditFile editFile = new EditFile(conf.getPath(), conf.getNameShop_Lite());
            JdbcRunner jdbcRunner = new JdbcRunner(conf.getPath(), conf.getNameShop_Lite(),
                    conf.getDb().getConnection().getDbUrl() + "/postgres-" + conf.getNameShop_Lite(),
                    conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass());

            if (directory.existsDirectory(conf.getNameShop_Lite())){
                logger.warn("Такая директория проекта уже существует - " + conf.getNameShop_Lite());
            }else if (unpackUpdateZip.existsDirectory(conf.getUpdateVersion())) {
                logger.warn("Такой архив уже разархивирован - " + conf.getUpdateVersion());
            } else{
                    //создание директорий
                    directory.createDirectors(conf.getNameShop_Lite());
                    //распаковать базовый архив
                    unpackBaseZip.unpack(conf.getBaseVersion());
                    unpackBaseZip.renameBaseFolder();
                    //распаковка версии с обновлением
                    unpackUpdateZip.unpack(conf.getUpdateVersion());
                    //создание БД
                    cmd.createDB(conf.getDb().getPostgresPath(), conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass(), "postgres-" + conf.getNameShop_Lite());
                    cmd.damp(conf.getDb().getPostgresPath(), conf.getDb().getConnection().getUser(), conf.getDb().getConnection().getPass(), "postgres-" + conf.getNameShop_Lite(), conf.getDb().getPostgresDump());
                    jdbcRunner.updateParameterValueLoadDictionaryDirectory();
                    jdbcRunner.updateParameterValueComProServiceIntegrationDirectory();
                    jdbcRunner.updateParameterValuePCardsServiceIntegrationDirectory();
                    jdbcRunner.updateSequenceBasketId();
                    jdbcRunner.updateSequencePortionId();
                    jdbcRunner.updateDB(conf.getDb().getScriptsSQL());
                    editFile.editeFileDbConnection();
                    editFile.editeFileLauncherConf(conf.getUpdateVersion());
            }
        }catch (Exception e){
            logger.error("Что то пошло не так =( ");
            e.printStackTrace();
        }finally {
            logger.info("-----------------------------------------END process-----------------------------------------");
        }
    }
}
