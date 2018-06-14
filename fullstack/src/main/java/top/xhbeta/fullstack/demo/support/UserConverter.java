package top.xhbeta.fullstack.demo.support;

import org.springframework.stereotype.Component;
import top.xhbeta.fullstack.demo.domain.User;

import java.util.*;

@Component
public class UserConverter {

  public List<User> convertToUser(Iterable<User> users) {
    if (users == null) {
      return Collections.emptyList();
    }
    List<User> userList = new ArrayList<>();
    for (User user : users) {
      userList.add(convertToUser(user));
    }
    return userList;
  }

  public User convertToUser(User user) {
    if (user == null) {
      return null;
    }
    return new User(user.getId(), user.getName(), user.getAge(), user.getBirthday(), user.getClassroom().getName());
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
