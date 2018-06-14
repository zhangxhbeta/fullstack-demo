package top.xhbeta.fullstack.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.xhbeta.fullstack.demo.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  Page<User> findByNameLikeAndState(String name,Integer state, Pageable pageable);

  Page<User> findByNameLikeAndClassroomIdAndState(String name,Integer classId,Integer state, Pageable pageable);

}
