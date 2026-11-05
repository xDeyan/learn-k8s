package bg.dyurukov.learnk8s.product;

import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

import static org.springframework.boot.actuate.autoconfigure.web.ManagementContextType.CHILD;
import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

@ManagementContextConfiguration(CHILD)
class ManagementWebConfiguration {

    @Bean
    @ConditionalOnCloudPlatform(KUBERNETES)
    ForwardedHeaderFilter forwardedHeaderFilter() {
        ForwardedHeaderFilter forwardedHeaderFilter = new ForwardedHeaderFilter(false);
        forwardedHeaderFilter.setUseForwardedPrefix(true);
        return forwardedHeaderFilter;
    }
}
