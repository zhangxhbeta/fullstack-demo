package top.xhbeta.fullstack.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import top.xhbeta.fullstack.demo.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = { "classroom" })
  Page<User> findByNameLikeAndState(String name,Integer state, Pageable pageable);

  @EntityGraph(attributePaths = { "classroom" })
  Page<User> findByNameLikeAndClassroom_IdAndState(String name,Long classId,Integer state, Pageable pageable);

}
