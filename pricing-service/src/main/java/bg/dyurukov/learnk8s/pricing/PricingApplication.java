package bg.dyurukov.learnk8s.pricing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class PricingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> log.info("Running...");
    }

//    @Bean
//    ServerBaseUrlCustomizer serverBaseUrlCustomizer() {
//        return (serverBaseUrl, request) -> {
//            log.info("Server base URL: {}", serverBaseUrl);
//            String host = request.getHeaders().get(HttpHeaders.HOST).getFirst();
//            log.info("Host: {}", host);
//
//            String transformed = UriComponentsBuilder.fromHttpUrl(serverBaseUrl)
//                    .host(host)
//                    .build()
//                    .toUriString();
//
//            log.info("Transformed base URL from {} to {}", serverBaseUrl, transformed);
//
//            return transformed;
//        };
//    }
}
