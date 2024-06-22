package org.example.orderservice.mapper.v1;


import org.example.libkafka.model.OrderEvent;
import org.example.orderservice.web.v1.dto.OrderUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
  OrderEvent requestToOrderEvent(OrderUpsertRequest request);
}
