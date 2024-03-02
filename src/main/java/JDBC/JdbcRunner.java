package JDBC;


import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JdbcRunner {
    private final String url;
    private final String user;
    private final String pass;
    private final String path;
    private final String projectName;

    final static Logger logger = Logger.getLogger(JdbcRunner.class);

    public JdbcRunner(String path, String projectName, String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.path= path;
        this.projectName = projectName;
    }

    public void updateParameterValueLoadDictionaryDirectory(){
        String script = "update parametervalue set value = '"+ path + "\\" + projectName + "\\rmlite' where \"name\" = 'LoadDictionaryDirectory'";
        runScripts(script);
    }

    public void updateParameterValueComProServiceIntegrationDirectory(){
        String script = "update parametervalue set value = '" + path + "\\" + projectName + "\\rmlite\\Compro" +"' where \"name\" = 'ComProServiceIntegrationDirectory'";
        runScripts(script);
    }

    public void updateParameterValuePCardsServiceIntegrationDirectory(){
        String script = "update parametervalue set value = '"+ path + "\\" + projectName + "\\rmlite\\Pcards"+ "' where \"name\" = 'PCardsServiceIntegrationDirectory'";
        runScripts(script);
    }

    public void updateSequenceBasketId(){
        String script = "ALTER SEQUENCE public.basket_num_sequence RESTART " + System.currentTimeMillis()/1000 + ";";
        runScripts(script);
    }

    public void updateSequencePortionId(){
        String script = "ALTER SEQUENCE public.portion_id_sequence RESTART " + System.currentTimeMillis()/1000 + ";";
                runScripts(script);
    }

    public void updateDB(ArrayList<String> listScripts){
        for (String script : listScripts) {
            runScripts(script);
        }
    }

    private void runScripts(String sql) {

        Connection con = null;
        Statement stmt = null;
        try {
            logger.info("Script execution - " + sql);
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, pass);
            stmt = con.createStatement();
            stmt.execute(sql);
            logger.info("Script executed - " + sql);
        } catch (Exception e) {
            logger.error("Error script execution " + e);
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                logger.error("Error close statement");
            }
            try {
                if (con != null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
                logger.error("Error close Connection");
            }
        }
    }
}
