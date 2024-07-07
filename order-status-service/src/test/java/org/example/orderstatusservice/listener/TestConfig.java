package org.example.orderstatusservice.listener;

import org.example.libkafka.kafka.MsgProducerFactory;
import org.example.libkafka.model.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class TestConfig {
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;


  @Bean
  public ProducerFactory<String, OrderEvent> orderEventProducerFactory() {
    return (new MsgProducerFactory<OrderEvent>(this.bootstrapServers)).getFactory();
  }

  @Bean
  public KafkaTemplate<String, OrderEvent> kafkaEventTemplate(
      ProducerFactory<String, OrderEvent> orderEventProducerFactory) {
    return new KafkaTemplate<>(orderEventProducerFactory);
  }
}
