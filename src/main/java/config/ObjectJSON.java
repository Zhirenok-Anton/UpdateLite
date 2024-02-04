package config;

public class ObjectJSON {
    private String nameShop_Lite;
    private String updateVersion;
    private String path;
    private String baseVersion;
    private DB db;

    public String getNameShop_Lite() {
        return nameShop_Lite;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public String getPath(){
        return path;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public DB getDb() {
        return db;
    }
}
