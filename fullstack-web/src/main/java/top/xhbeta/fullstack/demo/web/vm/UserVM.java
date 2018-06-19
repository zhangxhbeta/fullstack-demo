package top.xhbeta.fullstack.demo.web.vm;

import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.domain.User;

public class UserVM {
  private Long id;
  private String name;
  private Integer age;
  private Long classroomId;
  private String classroomName;

  public UserVM(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.age = user.getAge();
    Classroom classroom = user.getClassroom();
    this.classroomId = classroom.getId();
    this.classroomName = classroom.getName();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Long getClassroomId() {
    return classroomId;
  }

  public void setClassroomId(Long classroomId) {
    this.classroomId = classroomId;
  }

  public String getClassroomName() {
    return classroomName;
  }

  public void setClassroomName(String classroomName) {
    this.classroomName = classroomName;
  }
}
