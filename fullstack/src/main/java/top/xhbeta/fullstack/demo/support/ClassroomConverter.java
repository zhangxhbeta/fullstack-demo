package top.xhbeta.fullstack.demo.support;

import org.springframework.stereotype.Component;
import top.xhbeta.fullstack.demo.domain.Classroom;
import top.xhbeta.fullstack.demo.domain.User;

import java.util.*;

@Component
public class ClassroomConverter {

  public List<Classroom> convertToClassroom(Iterable<Classroom> classrooms) {
    if (classrooms == null) {
      return Collections.emptyList();
    }
    List<Classroom> classroomList = new ArrayList<>();
    for (Classroom classroom : classrooms) {
      classroomList.add(convertToClassroom(classroom));
    }
    return classroomList;
  }

  public Classroom convertToClassroom(Classroom classroom) {
    if (classroom == null) {
      return null;
    }
    return new Classroom(classroom.getId(), classroom.getName());
  }

  public Map<String, Object> convertDataToObjects(Map<String, String> data) {
    Map<String, Object> results = new HashMap<>();

    if (data != null) {
      for (Map.Entry<String, String> entry : data.entrySet()) {
        results.put(entry.getKey(), entry.getValue());
      }
    }
    return results;
  }

}
