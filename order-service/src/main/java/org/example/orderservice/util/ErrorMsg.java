package org.example.orderservice.util;

import lombok.experimental.UtilityClass;

/**
 * Класс содержит всевозможные строки - сообщения об ошибках
 */
@UtilityClass
public class ErrorMsg {
  public static final String INTERNAL_SERVER_ERROR = "Внутренняя ошибка сервера : {0} : {1}";
  public static final String NO_RESOURCE_FOUND = "Не найден ресурс: {0}";
  public static final String EMPTY_ORDER_STRING = "Строка заказа должна быть задана!";
  public static final String ORDER_LENGTH_INVALID = "Строка заказа должна быть от "
      + StringSizes.ORDER_MIN + " до " + StringSizes.ORDER_MAX + " символов!";
  public static final String EMPTY_QUANTITY = "Количество в заказе должно быть задано!";
  public static final String QUANTITY_NOT_POSITIVE =
      "Количестов в заказе должно быть положительным числом!";
}
