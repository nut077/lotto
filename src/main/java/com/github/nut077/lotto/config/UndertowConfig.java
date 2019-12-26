package com.github.nut077.lotto.config;

//@Configuration(proxyBeanMethods = false)
public class UndertowConfig {

  /*@Bean
  UndertowServletWebServerFactory embeddedServletContainerFactory() {
    UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
    factory.addBuilderCustomizers(
      builder ->
        builder
          .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
          .setServerOption(UndertowOptions.HTTP2_SETTINGS_ENABLE_PUSH, true)
          .setIoThreads(Runtime.getRuntime().availableProcessors() * 2)
          .setWorkerThreads(((Runtime.getRuntime().availableProcessors() * 2) * 10))
          .setBufferSize(
            (1024 * 16)
              - 20) // the 20 is to allow some space for protocol headers, see
    );
    return factory;
  }*/

}
