package bg.dyurukov.learnk8s.product.mapper;

import bg.dyurukov.learnk8s.product.api.ProductDto;
import bg.dyurukov.learnk8s.product.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    @Mapping(target = "price", ignore = true)
    ProductDto toDto(Product product);
}
