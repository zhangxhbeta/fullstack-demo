package top.xhbeta.fullstack.demo.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.xhbeta.fullstack.demo.domain.User;
import top.xhbeta.fullstack.demo.service.UserService;
import top.xhbeta.fullstack.scaffold.web.util.HeaderUtil;
import top.xhbeta.fullstack.scaffold.web.util.PaginationUtil;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/management/users")
public class UserResource {
  @Autowired
  private UserService userService;

  @GetMapping(params = {"name", "classId"})
  public ResponseEntity<List<User>> getAll(
    @RequestParam(value = "name") String name,
    @RequestParam(value = "classId") Integer classId,
    Pageable pageable) {

    Page<User> page = userService.findAll(name, classId, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/users");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @PostMapping(path = "/add", params = {"name", "age", "birthday", "classId"})
  public ResponseEntity<User> saveUser(@RequestParam(value = "name") String name,
                                       @RequestParam(value = "age") Integer age,
                                       @RequestParam(value = "birthday") Date birthday,
                                       @RequestParam(value = "classId") Long classId) {
    User user = userService.saveUser(name, age, birthday, classId);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("user", "/management/users/save");
    return new ResponseEntity<>(user, headers, HttpStatus.OK);
  }

  @GetMapping(path = "/{id:.+}")
  public ResponseEntity<User> getUser(
    @PathVariable Long id) {
    User user = userService.findById(id);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("user", "/management/users/update");
    return new ResponseEntity<>(user, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/update/{id:.+}", params = {"name", "age", "classId"})
  public ResponseEntity<User> saveUser(
    @PathVariable Long id,
    @RequestParam(value = "name") String name,
    @RequestParam(value = "age") Integer age,
    @RequestParam(value = "classId") Long classId) {
    User user = userService.updateUser(id, name, age, classId);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("user", "/management/users/update");
    return new ResponseEntity<>(user, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/delete/{id:.+}", params = {"name", "age", "classId"})
  public ResponseEntity<User> deleteUser(
    @PathVariable Long id) {
    User user = userService.deleteUser(id);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("user", "/management/users/delete");
    return new ResponseEntity<>(user, headers, HttpStatus.OK);
  }
}
