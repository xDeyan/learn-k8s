package bg.dyurukov.learnk8s.product.pricing;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.service.annotation.GetExchange;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface PricingClient {

    @GetExchange("/api/v1/pricing/{productId}")
    PriceDto getPriceByProductId(@PathVariable UUID productId);

    default Optional<PriceDto> findPriceByProductId(UUID productId) {
        try {
            return Optional.of(getPriceByProductId(productId));
        } catch (NotFound nf) {
            return Optional.empty();
        }
    }

    record PriceDto(BigDecimal price) {}
}
