package bg.dyurukov.learnk8s.product.pricing;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "pricing", types = PricingClient.class)
public class PricingClientConfiguration {
}
