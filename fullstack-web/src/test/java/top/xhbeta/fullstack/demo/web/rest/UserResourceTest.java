package top.xhbeta.fullstack.demo.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.domain.User;
import top.xhbeta.fullstack.demo.service.ClassroomService;
import top.xhbeta.fullstack.demo.service.UserService;
import top.xhbeta.fullstack.demo.support.UserConverter;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public class UserResourceTest {
  @Autowired
  private UserService userService;

  @Autowired
  private ClassroomService classroomService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private FormattingConversionService formattingConversionService;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private UserConverter userConverter;

  private MockMvc mockMvc;

  private Classroom classroom01;

  private Classroom classroom02;

  private User user01;
  private User user02;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    UserResource userResource = new UserResource(userService, userConverter);
    this.mockMvc = MockMvcBuilders.standaloneSetup(userResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setConversionService(formattingConversionService)
      .setMessageConverters(jacksonMessageConverter).build();

    classroom01= classroomService.saveClassroom("一班");
    classroom02=classroomService.saveClassroom("二班");

    user01=userService.saveUser("test01",8,new Date("Mon Jun 18 2018 22:36:18 GMT+0800"),classroom01.getId());
    user01=userService.saveUser("test02",11,new Date("Mon Jun 18 2018 22:36:18 GMT+0800"),classroom02.getId());
  }

  @Test
  public void getAll() throws Exception {
    mockMvc.perform(get("/api/management/users?classId="+classroom01.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andDo(MockMvcResultHandlers.print());
  }
}
