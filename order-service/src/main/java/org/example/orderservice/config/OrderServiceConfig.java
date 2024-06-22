package org.example.orderservice.config;

import org.example.libkafka.aop.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceConfig {
  @Bean
  public LoggingAspect loggingAspect() {
    return new LoggingAspect();
  }
}
