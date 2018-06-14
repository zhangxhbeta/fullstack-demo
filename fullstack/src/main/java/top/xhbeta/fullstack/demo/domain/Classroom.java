package top.xhbeta.fullstack.demo.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "lsw_classroom")
public class Classroom implements Serializable {
  private static final long serialVersionUID = 1L;

  public Classroom(String name,Integer state) {
    this.setName(name);
    this.setState(state);
  }

  public Classroom(Long id, String name){
    this.setId(id);
    this.setName(name);
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "classroom_id")
  private Long id;

  @Column(name = "classroom_name")
  private String name;

  @Column(name = "state")
  private Integer state;


  @OneToMany(
    mappedBy = "classroom",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<User> users;

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

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
