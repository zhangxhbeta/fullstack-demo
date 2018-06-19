package top.xhbeta.fullstack.demo.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.xhbeta.fullstack.core.web.errors.BadRequestAlertException;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.service.ClassroomService;
import top.xhbeta.fullstack.demo.support.ClassroomConverter;
import top.xhbeta.fullstack.demo.web.vm.ClassroomVM;
import top.xhbeta.fullstack.scaffold.web.util.HeaderUtil;
import top.xhbeta.fullstack.scaffold.web.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/management/classroom")
public class ClassroomResource {

  private static final String ENTITY_NAME = "classroom";

  private final ClassroomService classroomService;

  private final ClassroomConverter classroomConverter;

  public ClassroomResource(ClassroomService classroomService, ClassroomConverter classroomConverter) {
    this.classroomService = classroomService;
    this.classroomConverter = classroomConverter;
  }

  @GetMapping(params = {"page", "size"})
  public ResponseEntity<List<ClassroomVM>> getAll(
    @RequestParam(value = "name", required = false, defaultValue = "") String name,
    @RequestParam(required = false, defaultValue = "1") Integer page,
    @RequestParam(required = false, defaultValue = "20") Integer size) {
    Page<ClassroomVM> result = classroomService.findAll(name, page, size)
      .map(classroomConverter::convertToClassroom);

    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/api/management/classroom");
    return ResponseEntity
      .ok()
      .headers(headers)
      .body(result.getContent());
  }

  @GetMapping
  public ResponseEntity<List<Classroom>> getAll(
    @RequestParam(value = "name", required = false, defaultValue = "") String name) {

    List<Classroom> classroomList = classroomService.findAll(name);
    return ResponseEntity.ok().body(classroomList);
    // return new ResponseEntity<>(classroomList, HttpStatus.OK);
  }

  @PostMapping(params = {"name"})
  public ResponseEntity<Void> saveClassroom(@RequestParam(value = "name") String name) throws URISyntaxException {
    Classroom classroom = classroomService.saveClassroom(name);
    HttpHeaders headers = HeaderUtil.createEntityCreationAlert(ENTITY_NAME, classroom.getId().toString());
    return ResponseEntity
      .created(new URI("/api/management/classroom/" + classroom.getId()))
      .headers(headers)
      .build();
  }

  @GetMapping(path = "/{id:\\d+}")
  public ResponseEntity<Classroom> getClassroom(
    @PathVariable Long id) {
    Optional<Classroom> classroom = classroomService.findById(id);
    return ResponseUtil.wrapOrNotFound(classroom);
  }

  @PatchMapping(path = "/{id:\\d+}", params = {"name"})
  public void updateClassroom(
    @PathVariable Long id,
    @RequestParam(value = "name") String name) {

    if (id == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
    }

    classroomService.updateClassroom(id, name);
  }

  @DeleteMapping(path = "/{id:\\d+}")
  public void deleteClassroom(
    @PathVariable Long id) {
    classroomService.deleteClassroom(id);
  }
}
