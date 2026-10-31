package bg.dyurukov.learnk8s.product.domain;

import bg.dyurukov.learnk8s.product.api.ProductCreationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(ProductCreationDto product) {
        Product entity = product.toEntity();

        return productRepository.save(entity);
    }
}
