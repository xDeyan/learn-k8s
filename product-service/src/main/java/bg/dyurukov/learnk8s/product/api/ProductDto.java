package bg.dyurukov.learnk8s.product.api;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(UUID id, String name, int quantity, BigDecimal price) {

    public ProductDto withPrice(BigDecimal price) {
        return new ProductDto(id, name, quantity, price);
    }
}
