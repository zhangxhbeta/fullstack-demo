package top.xhbeta.fullstack.demo.config;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import top.xhbeta.fullstack.demo.aop.logging.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

  @Bean
  @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
  public LoggingAspect loggingAspect(Environment env) {
    return new LoggingAspect(env);
  }
}
