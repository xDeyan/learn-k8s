package bg.dyurukov.learnk8s.product.controller;

import bg.dyurukov.learnk8s.product.api.ProductCreationDto;
import bg.dyurukov.learnk8s.product.api.ProductDto;
import bg.dyurukov.learnk8s.product.domain.Product;
import bg.dyurukov.learnk8s.product.domain.ProductRepository;
import bg.dyurukov.learnk8s.product.domain.ProductService;
import bg.dyurukov.learnk8s.product.mapper.ProductMapper;
import bg.dyurukov.learnk8s.product.pricing.PricingClient;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
class ProductApiV1 {

    private final ProductRepository productRepository;
    private final ProductService productService;

    private final ProductMapper productMapper;

    private final PricingClient pricingClient;

    @PostMapping
    ProductDto create(@RequestBody ProductCreationDto product) {
        Product entity = productService.create(product);

        return productMapper.toDto(entity);
    }

    @GetMapping("/{id}")
    ProductDto findById(@PathVariable UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .map(this::withPrice)
                .orElseThrow(() -> new RuntimeException("Could not find product with id: " + id));
    }

    private ProductDto withPrice(ProductDto pd) {
        return pricingClient.findPriceByProductId(pd.id())
                .map(priceDto -> pd.withPrice(priceDto.price()))
                .orElse(pd);
    }

    @PostMapping("/batch")
    List<ProductDto> create(@RequestBody List<ProductCreationDto> products) {
        List<Product> entities = products.stream()
                .map(ProductCreationDto::toEntity)
                .collect(collectingAndThen(toList(), productRepository::saveAll));

        return entities.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping
    List<ProductDto> find() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @DeleteMapping
    void deleteAll() {
        productRepository.deleteAllInBatch();
    }

    @GetMapping("/appenders")
    void asd() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Set<String> appenders = new HashSet<>();
        for (Logger logger : loggerContext.getLoggerList()) {

            Iterator<Appender<ILoggingEvent>> appenderIterator = logger.iteratorForAppenders();


            while (appenderIterator.hasNext()) {
                Appender<ILoggingEvent> appender = appenderIterator.next();
                appenders.add(String.format("Name: %s, Class: %s", appender.getName(), appender.getClass()));
            }
        }

        log.info("Appenders: {}", String.join("\n", appenders));
    }

    @PostMapping("/generate")
    void generate(@RequestBody int count) {
        List<Product> products = IntStream.range(0, count)
                .mapToObj(i -> Product.builder()
                        .name(UUID.randomUUID().toString())
                        .build())
                .toList();

        long start = System.currentTimeMillis();
        productRepository.saveAll(products);

        log.info("Persisted {} products in {}ms", count, System.currentTimeMillis() - start);
    }
}
