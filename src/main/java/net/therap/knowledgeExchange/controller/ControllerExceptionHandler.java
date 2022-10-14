package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.exception.NotFoundException;
import net.therap.knowledgeExchange.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static net.therap.knowledgeExchange.utils.Url.ERROR_PAGE;

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
}