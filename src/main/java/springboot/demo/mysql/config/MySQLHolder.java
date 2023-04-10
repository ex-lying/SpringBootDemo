package springboot.demo.mysql.config;

public class MySQLHolder {
    private static final ThreadLocal<String> dataSources = new ThreadLocal<>();

    public static String getDataSource() {
        return dataSources.get();
    }

    public static void setDataSource(String dataSourceName) {
        dataSources.set(dataSourceName);
    }

    public static void clearDataSource() {
        dataSources.remove();
    }
}
