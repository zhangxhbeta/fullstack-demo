package top.xhbeta.fullstack.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * @author zhangxh
 */
@Component
public class OAuthAuthenticationProvider implements AuthenticationProvider {

//  private final UserOperator userOperator;

  private final PasswordEncoder passwordEncoder;

  public OAuthAuthenticationProvider(/*UserOperator userOperator,*/ PasswordEncoder passwordEncoder) {
//    this.userOperator = userOperator;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();

    String authorization = request.getHeader("Authorization");
    if (!authorization.startsWith("Basic") || authorization.length() < 6) {
      return null;
    }

    String str = authorization.substring(6);
    byte[] b = Base64.getDecoder().decode(str);
    String auth = new String(b);
    String client = auth.substring(0, auth.indexOf(':'));


    // 读取用户
    String openId = request.getParameter("open_id");
    String name = authentication.getName();
    Object password = authentication.getCredentials();

    /*
    User user = null;
    if (name != null && password != null) {
      user = userOperator.getUserByUsername(authentication.getName(), true);

      // 用户名密码登录方式要检查用户密码是否匹配
      if (!passwordEncoder.matches(password.toString(), user.getPassword())) {
        throw new UsernameNotFoundException("用户不存在或密码错误");
      }

      // 如果用户传了 openid 过来，执行 openId 绑定操作
      // 这时候如果用户已经绑定某个微信，会覆盖掉
      if (openId != null) {
        // 更新 openId
        user.setOpenId(openId);
        User updated = new User();
        updated.setUsername(user.getUsername());
        updated.setOpenId(openId);
        updated.setUserId(user.getUserId());

        userOperator.modifyUser(updated);
      }

    } else if (openId != null) {
      user = userOperator.getUserByOpenId(openId);
    }

    // 按照用户名查询用户
    if (user == null) {
      throw new UsernameNotFoundException("用户不存在或密码错误");
    }

    if (!user.isAccountNonLocked()) {
      throw new LockedException("用户被锁定");
    }

    if (!user.isAccountNonExpired()) {
      throw new DisabledException("用户已到期");
    }

    if (!user.isEnabled()) {
      throw new DisabledException("用户已停用");
    }

    if (client.equals("report_admin_app") && user.hasAuthority(
      PermissionOperations.PERMISSION_TYPE_ACCESS_POINT + "/access/admin") ||
      client.equals("report_app") && user.hasAuthority(
        PermissionOperations.PERMISSION_TYPE_ACCESS_POINT + "/access/report")) {
      return new UsernamePasswordAuthenticationToken(user,
        authentication.getCredentials(), user.getAuthorities());
    } else {
      return null;
    }
    */

    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
