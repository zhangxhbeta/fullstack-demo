package top.xhbeta.fullstack.demo.respository;

import junit.textui.TestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import top.xhbeta.fullstack.demo.domain.User;
import top.xhbeta.fullstack.demo.repository.UserRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRunner.class)
@Transactional
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User user01;

  @Before
  public void setup() {
      user01=new User();
      user01.setAge(10);
      user01.setName("user01");
      userRepository.save(user01);
  }
  @Test
  public void findByNameLikeTest(){
    List<User> list=userRepository.findByNameLike("01");
    System.out.print(list.size());
  }
}
