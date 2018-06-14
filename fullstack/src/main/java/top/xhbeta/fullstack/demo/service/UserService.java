package top.xhbeta.fullstack.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.domain.User;
import top.xhbeta.fullstack.demo.repository.ClassroomRepository;
import top.xhbeta.fullstack.demo.repository.UserRepository;
import top.xhbeta.fullstack.demo.support.UserConverter;

import java.util.Date;

@Service
@Transactional
public class UserService {
  private final UserRepository userRepository;
  private final ClassroomRepository classroomRepository;
  private final UserConverter userConverter;

  public UserService(UserRepository userRepository, ClassroomRepository classroomRepository, UserConverter userConverter) {
    this.userRepository = userRepository;
    this.classroomRepository = classroomRepository;
    this.userConverter = userConverter;
  }

  public User findById(Long id) {
    return userRepository.findById(id).get();
  }

  public Page<User> findAll(String name, Integer classId, Pageable pageable) {
    if (classId == -1) {
      return userRepository.findByNameLikeAndState(name, 1, pageable)
        .map(userConverter::convertToUser);
    } else {
      return userRepository.findByNameLikeAndClassroomIdAndState(name, classId, 1, pageable)
        .map(userConverter::convertToUser);
    }
  }

  public User saveUser(String name, Integer age, Date birthday, Long classId) {
    Classroom classroom = classroomRepository.findById(classId).get();
    User user = new User(name, age, birthday, classroom, 1);
    return userRepository.save(user);
  }

  public User updateUser(Long id, String name, Integer age, Long classId) {
    User user = userRepository.findById(id).get();
    Classroom classroom = classroomRepository.findById(classId).get();
    user.setName(name);
    user.setAge(age);
    user.setClassroom(classroom);
    return userRepository.save(user);
  }

  public User deleteUser(Long id) {
    User user = userRepository.findById(id).get();
    user.setState(0);
    return userRepository.save(user);
  }
}
