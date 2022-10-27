package com.kawsar.knowledgeExchange.controller;

import com.kawsar.knowledgeExchange.exception.NotFoundException;
import com.kawsar.knowledgeExchange.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.kawsar.knowledgeExchange.utils.Url.ERROR_PAGE;

/**
 * @author kawsar.bhuiyan
 * @since 9/129/22
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Exception exception, Model model) {
        model.addAttribute("exceptionMessage", exception.getMessage());

        return ERROR_PAGE;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(Exception exception, Model model) {
        model.addAttribute("exceptionMessage", exception.getMessage());

        return ERROR_PAGE;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        model.addAttribute("exceptionMessage", exception.getMessage());

        return ERROR_PAGE;
    }
}