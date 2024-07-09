package org.example.orderservice.web.v1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.libkafka.aop.Loggable;
import org.example.orderservice.util.ErrorMsg;
import org.example.orderservice.web.v1.dto.ErrorMsgResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.text.MessageFormat;
import java.util.List;

/**
 * Общий обработчик ошибок
 */
/**
 * Общий обработчик ошибок web-взаимодействия
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
  @Loggable
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorMsgResponse> resourceNotFound(
      NoResourceFoundException e, HttpServletRequest httpRequest) {

    log.error("ExceptionHandlerController.resourceNotFound: {}", ErrorMsg.NO_RESOURCE_FOUND, e);

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMsgResponse(
            MessageFormat.format(ErrorMsg.NO_RESOURCE_FOUND, httpRequest.getRequestURI())
        ));
  }

  @Loggable
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMsgResponse> badRequest(MethodArgumentNotValidException e) {

    log.error("ExceptionHandlerController.badRequest():", e);

    BindingResult bindingResult = e.getBindingResult();
    List<String> errorMessages = bindingResult.getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    String errorMessage = String.join("; ", errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMsgResponse(errorMessage));
  }

  @Loggable
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorMsgResponse> serverError(Throwable e) {

    log.error("ExceptionHandlerController.serverError():", e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorMsgResponse(
            MessageFormat.format(ErrorMsg.INTERNAL_SERVER_ERROR, e.getClass(), e.getMessage())
        ));
  }
}
