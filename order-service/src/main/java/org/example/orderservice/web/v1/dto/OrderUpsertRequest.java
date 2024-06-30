package org.example.orderservice.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.example.orderservice.util.ErrorMsg;
import org.example.orderservice.util.StringSizes;

public record OrderUpsertRequest(
    @NotBlank(message = ErrorMsg.EMPTY_ORDER_STRING)
    @Size(
        min = StringSizes.ORDER_MIN,
        max = StringSizes.ORDER_MAX,
        message = ErrorMsg.ORDER_LENGTH_INVALID)
    String order,
    @NotNull(message = ErrorMsg.EMPTY_QUANTITY)
    @Positive(message = ErrorMsg.QUANTITY_NOT_POSITIVE)
    Integer quantity
) {
}
