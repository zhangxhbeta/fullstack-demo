package top.xhbeta.fullstack.demo.web.vm;

import top.xhbeta.fullstack.demo.domain.Classroom;

public class ClassroomVM {
  private Long id;
  private String name;

  public ClassroomVM(Classroom classroom) {
    this.id = classroom.getId();
    this.name = classroom.getName();
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
}
