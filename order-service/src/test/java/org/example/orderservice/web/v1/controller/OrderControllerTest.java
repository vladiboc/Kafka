package org.example.orderservice.web.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.libkafka.model.OrderEvent;
import org.example.orderservice.mapper.v1.OrderMapper;
import org.example.orderservice.util.TestStringUtil;
import org.example.orderservice.web.v1.dto.OrderUpsertRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  OrderMapper orderMapper;

  @Test
  void whenCreate_thenReturnOrderEvent() throws Exception {
    final OrderUpsertRequest orderRequest = new OrderUpsertRequest("something", 50);
    final OrderEvent orderEvent = new OrderEvent("something", 50);

    Mockito.when(this.orderMapper.requestToOrderEvent(orderRequest)).thenReturn(orderEvent);

    final String expectedResponse = TestStringUtil
        .readStringFromResource("response/create_order_response.json");
    final String actualResponse = this.mockMvc.perform(post("/api/v1/order")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(orderRequest)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.orderMapper, Mockito.times(1)).requestToOrderEvent(orderRequest);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenEmptyOrder_thenReturnError() throws Exception {
    final OrderUpsertRequest request = new OrderUpsertRequest(null, 13);

    final String expectedResponse = TestStringUtil
        .readStringFromResource("response/error_empty_order.json");
    final String actualResponse = this.mockMvc.perform(post("/api/v1/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenEmptyQuantity_thenReturnError() throws Exception {
    final OrderUpsertRequest request = new OrderUpsertRequest("null", null);

    final String expectedResponse = TestStringUtil
        .readStringFromResource("response/error_empty_quantity.json");
    final String actualResponse = this.mockMvc.perform(post("/api/v1/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @ParameterizedTest
  @MethodSource("invalidOrder")
  void whenInvalidOrder_thenReturnError(String order) throws Exception {
    final OrderUpsertRequest request = new OrderUpsertRequest(order, 10);

    final String expectedResponse = TestStringUtil
        .readStringFromResource("response/error_invalid_order.json");
    final String actualResponse = this.mockMvc.perform(post("/api/v1/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @ParameterizedTest
  @MethodSource("invalidQuantity")
  void whenInvalidQuantity_thenReturnError(int quantity) throws Exception {
    final OrderUpsertRequest request = new OrderUpsertRequest("order", quantity);

    final String expectedResponse = TestStringUtil
        .readStringFromResource("response/error_invalid_quantity.json");
    final String actualResponse = this.mockMvc.perform(post("/api/v1/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  private static Stream<Arguments> invalidOrder() {
    return Stream.of(
        Arguments.of(RandomString.make(1)),
        Arguments.of(RandomString.make(257))
    );
  }

  private static Stream<Arguments> invalidQuantity() {
    return Stream.of(
        Arguments.of((int) (-1000 * Math.random()))
    );
  }
}
