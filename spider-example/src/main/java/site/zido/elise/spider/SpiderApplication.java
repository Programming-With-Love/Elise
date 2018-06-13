package site.zido.elise.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpiderApplication
 *
 * @author zido
 */
@SpringBootApplication
@EnableScheduling
public class SpiderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class);
    }
}
