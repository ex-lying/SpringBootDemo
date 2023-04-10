package springboot.demo.mysql.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.read")
public class MySQLReadConfig extends HikariConfig {
    @Bean
    public DataSource readDataSource() {
        return new HikariDataSource(this);
    }
}
