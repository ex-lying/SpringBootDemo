package springboot.demo.mysql.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings(value = "unused")
@Primary
@Component
public class MySQLDataSourceConfig extends AbstractRoutingDataSource {
    @Autowired
    MySQLWriteConfig mySQLWriteConfig;

    @Autowired
    MySQLReadConfig mySQLReadConfig;

    @Override
    protected Object determineCurrentLookupKey() {
        return MySQLHolder.getDataSource();
    }

    @PostConstruct
    public void init() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(MySQLReadAndWriteMethodName.WRITE, mySQLWriteConfig.writeDataSource());
        targetDataSources.put(MySQLReadAndWriteMethodName.READ, mySQLReadConfig.readDataSource());

        this.setTargetDataSources(targetDataSources);
        this.setDefaultTargetDataSource(mySQLReadConfig.readDataSource());
    }
}
