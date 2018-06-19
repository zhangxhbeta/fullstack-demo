package top.xhbeta.fullstack.demo.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.xhbeta.fullstack.demo.domain.BaseResult;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.service.ClassroomService;
import top.xhbeta.fullstack.demo.support.ClassroomConverter;
import top.xhbeta.fullstack.demo.web.vm.ClassroomVM;
import top.xhbeta.fullstack.scaffold.web.util.HeaderUtil;
import top.xhbeta.fullstack.scaffold.web.util.PaginationUtil;

import java.util.List;

@RestController
@RequestMapping("/api/management/classroom")
public class ClassroomResource {
  private final ClassroomService classroomService;

  private final ClassroomConverter classroomConverter;
  public ClassroomResource(ClassroomService classroomService,ClassroomConverter classroomConverter) {
    this.classroomService = classroomService;
    this.classroomConverter=classroomConverter;
  }


  @GetMapping
  public ResponseEntity<BaseResult> getAll(
    @RequestParam(value = "name", required = false, defaultValue = "") String name,
    @RequestParam(required = false, defaultValue = "1") Integer pageNo,
    @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
    Page<ClassroomVM> page = classroomService.findAll(name, pageNo, pageSize).map(classroomConverter::convertToClassroom);
    BaseResult baseResult=new BaseResult(page);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/classroom");
    return new ResponseEntity<>(baseResult, headers, HttpStatus.OK);
  }

  @GetMapping(path = "/all")
  public ResponseEntity<List<Classroom>> getAll(
    @RequestParam(value = "name", required = false, defaultValue = "") String name) {

    List<Classroom> classroomList = classroomService.findAll(name);
    return new ResponseEntity<>(classroomList, HttpStatus.OK);
  }

  @PostMapping(path = "/add", params = {"name"})
  public ResponseEntity<Classroom> saveClassroom(@RequestParam(value = "name") String name) {
    Classroom classroom = classroomService.saveClassroom(name);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("classroom", "/management/users/save");
    return new ResponseEntity<>(classroom, headers, HttpStatus.OK);
  }

  @GetMapping(path = "/{id:.+}")
  public ResponseEntity<Classroom> getClassroom(
    @PathVariable Long id) {
    Classroom classroom = classroomService.findById(id);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("classroom", "/management/users/update");
    return new ResponseEntity<>(classroom, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/update/{id:.+}", params = {"name", "age", "classId"})
  public ResponseEntity<Classroom> saveClassroom(
    @PathVariable Long id,
    @RequestParam(value = "name") String name) {
    Classroom classroom = classroomService.updateClassroom(id, name);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("classroom", "/management/users/update");
    return new ResponseEntity<>(classroom, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/delete/{id:.+}", params = {"name", "age", "classId"})
  public ResponseEntity<Classroom> deleteClassroom(
    @PathVariable Long id) {
    Classroom classroom = classroomService.deleteClassroom(id);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert("classroom", "/management/users/delete");
    return new ResponseEntity<>(classroom, headers, HttpStatus.OK);
  }
}
