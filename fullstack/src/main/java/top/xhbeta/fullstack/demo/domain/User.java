package top.xhbeta.fullstack.demo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lsw_users")
@NamedEntityGraph(name = "User.lazy", attributeNodes = {@NamedAttributeNode("classroom")})
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  public User(){}
  public User(String name, Integer age, Date birthday,Classroom classroom,Integer state) {
    this.setName(name);
    this.setAge(age);
    this.setBirthday(birthday);
    this.setClassroom(classroom);
    this.setState(state);
  }

  public User(Long id, String name, Integer age, Date birthday, String classroomName){
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setBirthday(birthday);
    this.setClassroomName(classroomName);
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @NotNull
  @Column(name = "user_name")
  private String name;

  @Column(name = "user_age")
  private Integer age;

  @Column(name = "user_birthday")
  private Date birthday;

  @Column(name = "state")
  private Integer state;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classroom_id")
  private Classroom classroom;

  @Transient
  private String classroomName;

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

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Classroom getClassroom() {
    return classroom;
  }

  public void setClassroom(Classroom classroom) {
    this.classroom = classroom;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getClassroomName() {
    return classroomName;
  }

  public void setClassroomName(String classroomName) {
    this.classroomName = classroomName;
  }
}
