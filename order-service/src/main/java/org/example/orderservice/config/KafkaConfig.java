package org.example.orderservice.config;

import org.example.libkafka.kafka.MsgConsumerFactory;
import org.example.libkafka.kafka.MsgProducerFactory;
import org.example.libkafka.model.OrderEvent;
import org.example.libkafka.model.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${app.kafka.orderStatusGroupId}")
  private String orderStatusGroupId;

  @Bean
  public ProducerFactory<String, OrderEvent> orderEventProducerFactory() {
    return (new MsgProducerFactory<OrderEvent>(this.bootstrapServers)).getFactory();
  }

  @Bean
  public KafkaTemplate<String, OrderEvent> kafkaTemplate(
      ProducerFactory<String, OrderEvent> orderEventProducerFactory) {
    return new KafkaTemplate<>(orderEventProducerFactory);
  }

  @Bean
  public ConsumerFactory<String, OrderStatus> orderStatusConsumerFactory() {
    return (new MsgConsumerFactory<OrderStatus>(
        this.bootstrapServers, this.orderStatusGroupId)).getFactory();
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, OrderStatus>
      orderStatusConcurrentKafkaListenerContainerFactory(
          ConsumerFactory<String, OrderStatus> orderStatusConsumerFactory) {
    final var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderStatus>();
    factory.setConsumerFactory(orderStatusConsumerFactory);

    return factory;
  }
}
