package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
//@ComponentScan(basePackages = "com.thu")
@EnableAutoConfiguration
@EntityScan(basePackages="domain")
@EnableJpaRepositories(basePackages="domain")
@ComponentScan(basePackages = "web")
@ComponentScan(basePackages = "hello")

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
