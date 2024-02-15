package kz.ipcorp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/open")
public class TestController {

    @Value("${eureka.instance.instance-id}")
    private String prop;

    private final Logger log = LogManager.getLogger(TestController.class);

    @GetMapping
    public String test(HttpServletRequest request) {


        return prop;
    }
}
