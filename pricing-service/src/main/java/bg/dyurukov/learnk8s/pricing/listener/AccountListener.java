package bg.dyurukov.learnk8s.pricing.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class AccountListener implements Consumer<byte[]> {

    @Override
    public void accept(byte[] bytes) {
        log.info("Received a message.... {}", new String(bytes, UTF_8));
    }
}
