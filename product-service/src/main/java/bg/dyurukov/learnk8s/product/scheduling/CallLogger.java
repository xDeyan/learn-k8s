package bg.dyurukov.learnk8s.product.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallLogger {

    public void logCall() {
        log.info("Calling pricing bigusss...");
    }
}
