package com.portfolio.pushpendra.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

    @GetMapping({"/", "/index"})
    public String indexPage() {
        return "index"; // Spring maps this to /templates/index.html
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/admin/access-denied"; // return name of your Thymeleaf or JSP page
    }

}
