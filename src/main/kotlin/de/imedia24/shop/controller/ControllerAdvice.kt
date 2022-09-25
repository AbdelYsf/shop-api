package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ErrorResponse
import de.imedia24.shop.exception.NoSuchProductFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ControllerAdvice : ResponseEntityExceptionHandler(){


    @ExceptionHandler
    fun handleNoSuchResourceFoundException(exception: NoSuchProductFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.message),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler
    fun handleRuntimeException(exception: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A Technical Exception occurred !!"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}