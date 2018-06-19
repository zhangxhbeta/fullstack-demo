package top.xhbeta.fullstack.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.repository.ClassroomRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class ClassroomService {
  private final ClassroomRepository classroomRepository;

  public ClassroomService(ClassroomRepository classroomRepository) {
    this.classroomRepository = classroomRepository;
  }

  public Optional<Classroom> findById(Long id) {
    return classroomRepository.findById(id);
  }

  public Page<Classroom> findAll(String name, Integer pageNo, Integer pageSize) {
    Sort sort = new Sort(Sort.Direction.ASC, "id");
    Pageable pageable = new PageRequest(pageNo - 1, pageSize, sort);
    return classroomRepository.findByNameLikeAndState(name + "%", 1, pageable);
  }

  public List<Classroom> findAll(String name) {
    return classroomRepository.findByNameLikeAndState(name + "%", 1);
  }

  public Classroom saveClassroom(String name) {
    Classroom classroom = new Classroom(name, 1);
    return classroomRepository.save(classroom);
  }

  public Classroom updateClassroom(Long id, String name) {
    // Classroom classroom = classroomRepository.findById(id).get();
    // classroom.setName(name);
    // return classroomRepository.save(classroom);

    return classroomRepository.findById(id)
      .map(classroom -> {
        classroom.setName(name);
        return classroom;
      })
      .orElseThrow(() -> new NoSuchElementException("Classroom not found"));
  }

  public Classroom deleteClassroom(Long id) {
    return classroomRepository
      .findById(id)
      .map(classroom -> {
        classroom.setState(0);
        return classroom;
      })
      .orElseThrow(() -> new NoSuchElementException("Classroom not found"));


    // Classroom classroom = classroomRepository.findById(id).get();
    // classroom.setState(0);
    // return classroomRepository.save(classroom);
  }
}
