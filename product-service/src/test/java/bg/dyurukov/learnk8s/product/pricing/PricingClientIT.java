package bg.dyurukov.learnk8s.product.pricing;

import bg.dyurukov.learnk8s.product.pricing.PricingClient.PriceDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.http.client.autoconfigure.service.HttpServiceClientPropertiesAutoConfiguration;
import org.springframework.boot.restclient.autoconfigure.service.HttpServiceClientAutoConfiguration;
import org.springframework.boot.restclient.test.autoconfigure.AutoConfigureMockRestServiceServer;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(components = PricingClientConfiguration.class)
@AutoConfigureMockRestServiceServer(enabled = false)
@ImportAutoConfiguration({ HttpServiceClientAutoConfiguration.class, HttpServiceClientPropertiesAutoConfiguration.class })
@EnableWireMock(
        @ConfigureWireMock(
            baseUrlProperties = "spring.http.serviceclient.pricing.base-url"
))
class PricingClientIT {

    @Autowired
    PricingClient pricingClient;

    @InjectWireMock
    WireMockServer wireMock;

    @Test
    void shouldReturnObjectOn200Ok() {
        // given
        UUID productId = UUID.randomUUID();

        wireMock.stubFor(get(urlPathTemplate("/api/v1/pricing/{productId}"))
                .withPathParam("productId", equalTo(productId.toString()))
                .willReturn(okForJson(new PriceDto(null))));
        // when
        PriceDto price = pricingClient.getPriceByProductId(productId);

        // then
        assertThat(price).isNotNull();

        wireMock.verify(1, RequestPatternBuilder.allRequests());
    }
}
