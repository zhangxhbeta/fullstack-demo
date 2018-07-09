package top.xhbeta.fullstack.demo.web.rest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.repository.ClassroomRepository;
import top.xhbeta.fullstack.demo.service.ClassroomService;
import top.xhbeta.fullstack.demo.support.ClassroomConverter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class ClassroomResourceIntTest {

  @Autowired
  ClassroomService classroomService;

  @Autowired
  ClassroomRepository classroomRepository;

  @Autowired
  private ClassroomConverter classroomConverter;

  @Autowired
  private MockMvc mockMvc;

  public ClassroomResourceIntTest() {
  }

  @Before
  public void setup() {
    ClassroomResource classroomResource = new ClassroomResource(classroomService, classroomConverter);
    // this.mockMvc = MockMvcBuilders
    //   .standaloneSetup(classroomResource)
    //   .setHandlerExceptionResolvers(new ExceptionHandlerExceptionResolver())
    //   .build();

    classroomService.saveClassroom("一班");
    classroomService.saveClassroom("二班");
    classroomService.saveClassroom("三班");

  }

  @Test
  public void testGetClassroomNotExist() throws Exception {
    int idNotExist = 1982343;
    this.mockMvc.perform(get("/api/management/classroom/" + idNotExist))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testUpdateClassroomNotExist() throws Exception {
    int idNotExist = 1982343;
    this.mockMvc.perform(
      patch("/api/management/classroom/" + idNotExist)
        .param("name", "HelloWorld"))
      .andDo(print())
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testUpdateClassroom() throws Exception {
    int idNotExist = 1982343;

    List<Classroom> clsroomes = classroomRepository.findAll();
    assertTrue(clsroomes.size() > 0);

    Classroom cls = clsroomes.get(0);

    String classroomName = "HelloWorld";

    this.mockMvc.perform(
      patch("/api/management/classroom/" + cls.getId())
        .param("name", classroomName))
      .andExpect(status().isOk())
      .andDo(print());


    this.mockMvc.perform(
      get("/api/management/classroom/" + cls.getId())
    ).andExpect(jsonPath("$.name", Matchers.is(classroomName)));
  }

  @Test
  public void testUpdateClassroomIdIsNull() throws Exception {
    this.mockMvc.perform(
      patch("/api/management/classroom/")
        .param("name", "HelloWorld"))
      .andExpect(status().is4xxClientError());
  }
}
