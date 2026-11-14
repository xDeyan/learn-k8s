package bg.dyurukov.learnk8s.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProductServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
