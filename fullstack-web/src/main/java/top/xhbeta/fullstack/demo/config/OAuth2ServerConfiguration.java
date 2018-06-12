package top.xhbeta.fullstack.demo.config;


import io.github.jhipster.security.AjaxLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.sql.DataSource;

@Configuration
public class OAuth2ServerConfiguration {

  private final DataSource dataSource;

  @SuppressWarnings("SpringJavaAutowiringInspection")
  public OAuth2ServerConfiguration(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  public JdbcTokenStore tokenStore() {
    return new JdbcTokenStore(dataSource);
  }

  @Configuration
  @EnableResourceServer
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    private final SecurityProblemSupport problemSupport;

    private final AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    private final CorsFilter corsFilter;

    public ResourceServerConfiguration(TokenStore tokenStore, SecurityProblemSupport problemSupport,
                                       AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler, CorsFilter corsFilter) {

      this.tokenStore = tokenStore;
      this.problemSupport = problemSupport;
      this.ajaxLogoutSuccessHandler = ajaxLogoutSuccessHandler;
      this.corsFilter = corsFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      // @formatter:off
      http
          .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
          .exceptionHandling()
          .authenticationEntryPoint(problemSupport)
          .accessDeniedHandler(problemSupport)
        .and()
          .logout()
          .logoutUrl("/api/logout")
          .logoutSuccessHandler(ajaxLogoutSuccessHandler)
        .and()
          .csrf()
          .disable()
          // .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
          .headers()
          .frameOptions().disable()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .authorizeRequests()
          .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
          .antMatchers("/api/authenticate").permitAll()
          .antMatchers("/api/register").permitAll()
          .antMatchers("/api/profile-info").permitAll()
          .antMatchers("/api/**").authenticated()
          .antMatchers("/management/**").hasRole("ADMIN")
          .antMatchers("/v2/api-docs/**").permitAll()
          .antMatchers("/swagger-resources/configuration/ui").permitAll()
          .antMatchers("/swagger-ui/index.html").hasRole("ADMIN");
      // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
      resources.resourceId("res_fullstack_demo").tokenStore(tokenStore);
    }
  }

  @Configuration
  @EnableAuthorizationServer
  protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final TokenStore tokenStore;

    private final DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public AuthorizationServerConfiguration(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
                                            TokenStore tokenStore, DataSource dataSource, PasswordEncoder passwordEncoder) {

      this.authenticationManager = authenticationManager;
      this.tokenStore = tokenStore;
      this.dataSource = dataSource;
      this.passwordEncoder = passwordEncoder;
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
      return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
      return new JdbcApprovalStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
      throws Exception {
      endpoints
        .authorizationCodeServices(authorizationCodeServices())
        .approvalStore(approvalStore())
        .tokenStore(tokenStore)
        .authenticationManager(authenticationManager)
        //.userDetailsService()
        .reuseRefreshTokens(false);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
      oauthServer.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      //clients.jdbc(dataSource);
      String defaultScope = "default";
      clients
        .inMemory()
          .withClient("private")
          .secret(passwordEncoder.encode("client_secret"))
          .authorizedGrantTypes("authorization_code", "password", "refresh_token")
          .accessTokenValiditySeconds(60 * 60 * 24 * 7)
          .refreshTokenValiditySeconds(60 * 60 * 24 * 8)
          .scopes(defaultScope)
          .redirectUris("http://localhost:8080/#/")
          .autoApprove(defaultScope)
        .and()
          .withClient("public")
          .secret(passwordEncoder.encode("client_secret"))
          .authorizedGrantTypes("authorization_code", "refresh_token")
          .accessTokenValiditySeconds(60 * 60 * 24 * 7)
          .refreshTokenValiditySeconds(60 * 60 * 24 * 8)
          .scopes(defaultScope)
          .redirectUris("http://localhost:8080/#/")
          .autoApprove(defaultScope);
    }
  }

  @Bean
  public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
    return new AjaxLogoutSuccessHandler();
  }

}
