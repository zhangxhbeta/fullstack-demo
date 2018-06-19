package top.xhbeta.fullstack.demo.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.repository.ClassroomRepository;

import javax.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ClassroomServiceIntTest {

  @Autowired
  ClassroomRepository classroomRepository;

  @Autowired
  ClassroomService classroomService;

  private Pageable pageable;

  @Before
  public void setUp() throws Exception {
    Classroom classroom = new Classroom();
    classroom.setName("Test classname");
    classroom.setState(0);
    // classroom.setUsers();

    pageable = PageRequest.of(0, 10);

    classroomRepository.save(classroom);
  }

  @Test
  public void updateClassroom() {
    List<Classroom> clsRooms = classroomRepository.findAll();
    assertEquals(1, clsRooms.size());

    Classroom clsRoom = clsRooms.get(0);

    String className = "Test classname 2";
    classroomService.updateClassroom(clsRoom.getId(), className);

    Optional<Classroom> clsRoomNew = classroomRepository.findById(clsRoom.getId());
    clsRoomNew.ifPresent(classroom -> {
      assertEquals(className, classroom.getName());
    });

    assertTrue(clsRoomNew.isPresent());
  }

  @Test
  public void deleteClassroom() {

    List<Classroom> clsRooms = classroomRepository.findAll();
    assertEquals(1, clsRooms.size());

    Classroom clsRoom = clsRooms.get(0);

    classroomService.deleteClassroom(clsRoom.getId());

    Optional<Classroom> clsRoomNew = classroomRepository.findById(clsRoom.getId());
    clsRoomNew.ifPresent(classroom -> {
      assertEquals(0, (int) classroom.getState());
    });

    assertTrue(clsRoomNew.isPresent());
  }
}
