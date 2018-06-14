package top.xhbeta.fullstack.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.domain.User;
import top.xhbeta.fullstack.demo.repository.ClassroomRepository;
import top.xhbeta.fullstack.demo.repository.UserRepository;
import top.xhbeta.fullstack.demo.support.ClassroomConverter;
import top.xhbeta.fullstack.demo.support.UserConverter;

import java.util.List;

@Service
@Transactional
public class ClassroomService {
  private final ClassroomRepository classroomRepository;
  private final ClassroomConverter classroomConverter;

  public ClassroomService(ClassroomRepository classroomRepository, ClassroomConverter classroomConverter) {
    this.classroomRepository = classroomRepository;
    this.classroomConverter = classroomConverter;
  }

  public Classroom findById(Long id) {
    return classroomRepository.findById(id).get();
  }

  public Page<Classroom> findAll(String name, Pageable pageable) {
    return classroomRepository.findByNameLikeAndState(name, 1, pageable)
      .map(classroomConverter::convertToClassroom);
  }

  public List<Classroom> findAll(String name) {
    return classroomRepository.findByNameLikeAndState(name, 1);
  }

  public Classroom saveClassroom(String name) {
    Classroom classroom = new Classroom(name, 1);
    return classroomRepository.save(classroom);
  }

  public Classroom updateClassroom(Long id, String name) {
    Classroom classroom = classroomRepository.findById(id).get();
    classroom.setName(name);
    return classroomRepository.save(classroom);
  }

  public Classroom deleteClassroom(Long id) {
    Classroom classroom = classroomRepository.findById(id).get();
    classroom.setState(0);
    return classroomRepository.save(classroom);
  }
}
