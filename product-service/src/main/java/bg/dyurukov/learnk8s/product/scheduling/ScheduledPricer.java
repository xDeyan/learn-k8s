package bg.dyurukov.learnk8s.product.scheduling;

import bg.dyurukov.learnk8s.product.pricing.PricingClient;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
class ScheduledPricer {

    private static final List<UUID> PRODUCT_IDS = Stream.of(
                "51c3cf58-5187-4a32-9a3d-4223676d0dda",
                "fd95e01a-90ac-4445-bda5-0d4a4a2cdd8c",
                "91167c91-b7bc-421e-be5a-2816f4e55046",
                "e38679bb-80c5-4f02-8cae-2ee2121e7496")
            .map(UUID::fromString)
            .toList();

    private final PricingClient pricingClient;

    private final StreamBridge streamBridge;
    private final S3Template s3Template;

    @Scheduled(fixedRate = 20_000)
    void scheduled() {
        try {
            var price = pricingClient.findPriceByProductId(getRandomProductId());

            log.info("Prices fetched : {}", price);
        } catch (Exception exception) {
            log.error("Error connecting to pricing service: ", exception);
        }
    }

    @Scheduled(fixedRate = 60_000)
    void bingus() {
        streamBridge.send("bingus-out", "bingus");

        String key = UUID.randomUUID().toString();
        String obj = "asdsadds";
        String bucketName = "products";

        S3Resource products = s3Template.store(bucketName, key, obj);

        log.info("Uploaded file to [bucket={}, key={}]", bucketName, key);
    }

    private UUID getRandomProductId() {
        return PRODUCT_IDS.get(new Random().nextInt(0, PRODUCT_IDS.size()));
    }
}
