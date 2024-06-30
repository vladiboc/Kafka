package org.example.orderservice.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.example.libkafka.model.OrderEvent;
import org.example.orderservice.web.v1.dto.OrderUpsertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class OrderMapperTest {
  @Autowired
  OrderMapper orderMapper;
  @Test
  void whenPutOrderRequest_thenGetOrderEvent() {
    final OrderUpsertRequest request = new OrderUpsertRequest("Смартфон Ксяоми", 1);
    final OrderEvent expectedEvent = new OrderEvent("Смартфон Ксяоми", 1);

    final OrderEvent actualEvent = this.orderMapper.requestToOrderEvent(request);

    assertEquals("Проверяем трансформацию объекта OrderUpsertRequest в объект OrderEvent: ",
        expectedEvent, actualEvent);
  }
}
