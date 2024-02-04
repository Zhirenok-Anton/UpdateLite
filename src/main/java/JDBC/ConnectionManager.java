package JDBC;

import org.apache.log4j.Logger;

public class ConnectionManager {

    final static Logger logger = Logger.getLogger(ConnectionManager.class);

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    private static void loadDriver() {

        try {
            Class.forName("org.postgresql.Driver");
            logger.info("Загрузка драйвера org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Драйвер org.postgresql.Driver не загружен");
            throw new RuntimeException(e);
        }
    }

}
