package com.github.nut077.lotto.config;

import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UndertowConfig {

    @Bean
    UndertowServletWebServerFactory embeddedServletContainerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addBuilderCustomizers(
                builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true),
                builder -> builder.setIoThreads(Runtime.getRuntime().availableProcessors() * 2),
                builder -> builder.setWorkerThreads((Runtime.getRuntime().availableProcessors() * 10))
        );
        return factory;
    }

}
