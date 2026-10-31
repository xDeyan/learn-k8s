package bg.dyurukov.learnk8s.product.api;

import bg.dyurukov.learnk8s.product.domain.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductCreationDto(@NotNull @NotEmpty @Length(min = 3, max = 32) String name, int quantity) {

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .quantity(quantity)
                .build();
    }
}
