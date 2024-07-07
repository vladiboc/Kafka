package org.example.orderstatusservice.listener;

import org.assertj.core.api.Assertions;
import org.example.libkafka.model.OrderEvent;
import org.example.orderstatusservice.service.OrderEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
class OrderEventListenerTest {
  @Container
  static final KafkaContainer kafka = new KafkaContainer(
      DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
  );

  @DynamicPropertySource
  static void registryKafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Value("${app.kafka.orderTopic}")
  private String topicName;

  @Autowired
  private KafkaTemplate<String, OrderEvent> kafkaEventTemplate;

  @Autowired
  private OrderEventService orderEventService;

  @Test
  void whenSendOrderEvent_thenHandleOrderEventByListener() {
    final OrderEvent event = new OrderEvent("Important item", 1);
    final String key = UUID.randomUUID().toString();

    this.kafkaEventTemplate.send(this.topicName, key, event);

    await()
        .pollInterval(Duration.ofSeconds(3))
        .atMost(10, TimeUnit.SECONDS)
        .untilAsserted(() -> {
          Optional<OrderEvent> mayBeOrderEvent =
              this.orderEventService.getEventByOrder("Important item");

          Assertions.assertThat(mayBeOrderEvent).isPresent();

          OrderEvent orderEvent = mayBeOrderEvent.get();

          Assertions.assertThat(orderEvent.quantity()).isEqualTo(1);
        });
  }
}
