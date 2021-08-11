package io.github.malczuuu.uiot.rules.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class IndexController {

  @GetMapping
  public String get() {
    return "redirect:/swagger-ui/index.html";
  }
}
