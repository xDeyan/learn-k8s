package bg.dyurukov.learnk8s.pricing.controller;

import bg.dyurukov.learnk8s.pricing.api.PriceDto;
import io.micrometer.core.annotation.Timed;
import io.opentelemetry.instrumentation.annotations.AddingSpanAttributes;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static org.springframework.http.HttpHeaders.HOST;

@Slf4j
@RestController
@RequestMapping("/api/v1/pricing")
class PricingController {

    private static final Set<PriceDto> PRICES = Set.of(
            PriceDto.of("51c3cf58-5187-4a32-9a3d-4223676d0dda", 39.40),
            PriceDto.of("fd95e01a-90ac-4445-bda5-0d4a4a2cdd8c", 46.90),
            PriceDto.of("91167c91-b7bc-421e-be5a-2816f4e55046", 674.90),
            PriceDto.of("e38679bb-80c5-4f02-8cae-2ee2121e7496", 558.07)
    );

    @AddingSpanAttributes
    @GetMapping("/{productId}")
    @Timed
    PriceDto getPrice(@SpanAttribute("pricing.product_id") @PathVariable UUID productId, @Parameter(hidden = true) @RequestHeader(HOST) String host) {
        log.info("Request for [productId={}] from [host={}]", productId, host);
        return PRICES.stream()
                .filter(byProductId(productId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND.value(), "No pricing found for product id '" + productId + "'", null));
    }

    private Predicate<PriceDto> byProductId(UUID productId) {
        return price -> price.productId().equals(productId);
    }
}
