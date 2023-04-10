package springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @className: SpringBootDemo
 * @author: Lying
 * @description: TODO
 * @date: 2022/11/17 上午10:31
 */
@EntityScan({ "thcl.data.model"})
@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}