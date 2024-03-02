package config;

import java.util.ArrayList;

public class DB {
    private String postgresPath;
    private String postgresDump;
    private Connection connection;

    private ArrayList<String> scriptsSQL;

    public String getPostgresPath() {
        return postgresPath;
    }

    public Connection getConnection() {
        return connection;
    }

    public ArrayList<String> getScriptsSQL() {
        return scriptsSQL;
    }


}
