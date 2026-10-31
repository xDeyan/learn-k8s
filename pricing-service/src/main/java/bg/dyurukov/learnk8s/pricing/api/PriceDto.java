package bg.dyurukov.learnk8s.pricing.api;

import java.math.BigDecimal;
import java.util.UUID;

public record PriceDto(UUID productId, BigDecimal price) {

    public static PriceDto of(String productId, double price) {
        return new PriceDto(UUID.fromString(productId), BigDecimal.valueOf(price));
    }
}
