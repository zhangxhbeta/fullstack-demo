package top.xhbeta.fullstack.demo.support;

import org.springframework.stereotype.Component;
import top.xhbeta.fullstack.demo.domain.User;
import top.xhbeta.fullstack.demo.web.vm.UserVM;

import java.util.*;

@Component
public class UserConverter {

  public List<UserVM> convertToUser(Iterable<User> users) {
    if (users == null) {
      return Collections.emptyList();
    }
    List<UserVM> userList = new ArrayList<>();
    for (User user : users) {
      userList.add(convertToUser(user));
    }
    return userList;
  }

  public UserVM convertToUser(User user) {
    if (user == null) {
      return null;
    }
    return new UserVM(user);
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
