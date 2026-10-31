package bg.dyurukov.learnk8s.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ListPagingAndSortingRepository<Product, UUID> {

    Optional<Product> findByName(String name);
}
