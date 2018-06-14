package top.xhbeta.fullstack.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.xhbeta.fullstack.demo.domain.Classroom;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
  List<Classroom> findByNameLikeAndState(String name, Integer state);

  Page<Classroom> findByNameLikeAndState(String name, Integer state, Pageable pageable);

}
