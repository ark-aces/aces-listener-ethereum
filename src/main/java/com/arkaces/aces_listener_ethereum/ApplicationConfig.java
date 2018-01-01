package com.arkaces.aces_listener_ethereum;

import com.arkaces.aces_server.aces_listener.config.AcesListenerConfig;
import com.arkaces.aces_server.ark_auth.ArkAuthConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@Import({AcesListenerConfig.class, ArkAuthConfig.class})
public class ApplicationConfig {

    @Bean
    public Integer maxScanBlockDepth(Environment environment) {
        return environment.getProperty("maxScanBlockDepth", Integer.class);
    }

    @Bean
    public RestTemplate ethereumRpcRestTemplate(Environment environment) {
        return new RestTemplateBuilder()
            .rootUri(environment.getProperty("ethereumRpc.url"))
            .build();
    }

}
