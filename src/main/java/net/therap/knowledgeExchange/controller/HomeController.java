package net.therap.knowledgeExchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.therap.knowledgeExchange.utils.Url.HOME_PAGE;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String view() {
        return HOME_PAGE;
    }
}