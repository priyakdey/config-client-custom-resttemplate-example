package io.priyak.demo.config;

import io.priyak.demo.interceptor.CustomClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class to instantiate the {@link ConfigServicePropertySourceLocator} and inject a
 * custom {@link RestTemplate}. This gives us an handle to intercept the request and
 * modify headers, ex: adding special authentication tokens
 *
 * See {@link CustomClientHttpRequestInterceptor}
 *
 * spring.main.allow-bean-definition-overriding needs to be set to true to override the bean definition
 * declared in {@link org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration}
 * This feature needs to be explicitly set to true since SpringBoot 2.1
 *
 * @author priyakdey
 */
@Configuration
public class CustomConfigServiceBootstrapConfiguration {
    private final Environment environment;
    private final String username;
    private final String password;

    @Autowired
    public CustomConfigServiceBootstrapConfiguration(Environment environment,
                                                     @Value("${config-server.username}") String username,
                                                     @Value("${config-server.password}") String password) {
        this.environment = environment;
        this.username = username;
        this.password = password;
    }

    /**
     * Bean definition for {@link ConfigClientProperties}
     * @return ConfigClientProperties
     */
    @Bean
    public ConfigClientProperties configClientProperties() {
        final ConfigClientProperties configClientProperties = new ConfigClientProperties(environment);
        configClientProperties.setEnabled(false);
        return configClientProperties;
    }

    @Bean
    public ConfigServicePropertySourceLocator configServicePropertySourceLocator() {
        final ConfigServicePropertySourceLocator configServicePropertySourceLocator =
                new ConfigServicePropertySourceLocator(configClientProperties());
        configServicePropertySourceLocator.setRestTemplate(restTemplate());
        return configServicePropertySourceLocator;
    }

    private RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .additionalInterceptors(new CustomClientHttpRequestInterceptor(username, password))
                .build();
    }
}
