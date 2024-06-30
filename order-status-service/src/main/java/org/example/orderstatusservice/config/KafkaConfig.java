package org.example.orderstatusservice.config;

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

  @Value("${app.kafka.orderEventGroupId}")
  private String orderEventGroupId;

  @Bean
  public ProducerFactory<String, OrderStatus> orderStatusProducerFactory() {
    return (new MsgProducerFactory<OrderStatus>(this.bootstrapServers)).getFactory();
  }

  @Bean
  public KafkaTemplate<String, OrderStatus> kafkaTemplate(
      ProducerFactory<String, OrderStatus> orderStatusProducerFactory) {
    return new KafkaTemplate<>(orderStatusProducerFactory);
  }

  @Bean
  public ConsumerFactory<String, OrderEvent> orderEventConsumerFactory() {
    return (new MsgConsumerFactory<OrderEvent>(
        this.bootstrapServers, this.orderEventGroupId)).getFactory();
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, OrderEvent>
      orderEventConcurrentKafkaListenerContainerFactory(
          ConsumerFactory<String, OrderEvent> orderEventConsumerFactory) {
    final var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderEvent>();
    factory.setConsumerFactory(orderEventConsumerFactory);

    return factory;
  }
}
