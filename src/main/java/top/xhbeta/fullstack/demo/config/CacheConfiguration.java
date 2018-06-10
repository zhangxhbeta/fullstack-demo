package top.xhbeta.fullstack.demo.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class})
@AutoConfigureBefore(value = {WebConfigurer.class, DatabaseConfiguration.class})
public class CacheConfiguration {

  private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

  public CacheConfiguration(JHipsterProperties jHipsterProperties) {
    JHipsterProperties.Cache.Ehcache ehcache =
      jHipsterProperties.getCache().getEhcache();

    jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
      CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
        ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
        .build());
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
//            cm.createCache(top.xhbeta.fullstack.demo.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
//            cm.createCache(top.xhbeta.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
//            cm.createCache(top.xhbeta.myapp.domain.User.class.getName(), jcacheConfiguration);
//            cm.createCache(top.xhbeta.myapp.domain.Authority.class.getName(), jcacheConfiguration);
//            cm.createCache(top.xhbeta.myapp.domain.User.class.getName() + ".authorities", jcacheConfiguration);
      // jhipster-needle-ehcache-add-entry
    };
  }
}
