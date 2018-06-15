package top.xhbeta.fullstack.demo.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/foo")
public class FooResource {

  @Autowired
  HttpSession httpSession;

  @GetMapping
  public int getSessionNumber() {
    Object number = httpSession.getAttribute("number-test");
    if (number == null) {
      httpSession.setAttribute("number-test", 1);
      return 0;
    } else {
      httpSession.setAttribute("number-test", ((int) number) + 1);
      return (int) number;
    }
  }

}
