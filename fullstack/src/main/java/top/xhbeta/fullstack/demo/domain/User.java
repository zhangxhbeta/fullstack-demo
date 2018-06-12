package top.xhbeta.fullstack.demo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "lsw_users")
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classroom_id")
  private Classroom classroom;

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
}
