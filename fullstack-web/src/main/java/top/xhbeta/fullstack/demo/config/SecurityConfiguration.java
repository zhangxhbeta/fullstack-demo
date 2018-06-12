package top.xhbeta.fullstack.demo.config;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import top.xhbeta.fullstack.demo.security.OAuthAuthenticationProvider;
import top.xhbeta.fullstack.demo.security.jwt.JWTConfigurer;
import top.xhbeta.fullstack.demo.security.jwt.TokenProvider;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;


@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  public static final String ADMIN = "ROLE_ADMIN";

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

//    private final UserDetailsService userDetailsService;

  private final TokenProvider tokenProvider;

  private final CorsFilter corsFilter;

  private final SecurityProblemSupport problemSupport;

  public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, /*UserDetailsService userDetailsService,*/
                               TokenProvider tokenProvider, CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
//        this.userDetailsService = userDetailsService;
    this.tokenProvider = tokenProvider;
    this.corsFilter = corsFilter;
    this.problemSupport = problemSupport;
  }

  @PostConstruct
  public void init() {
    try {
      authenticationManagerBuilder
        .inMemoryAuthentication()
        .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER")
        .and().withUser("user").password(passwordEncoder().encode("user")).roles("USER");

      // AuthProvider
//        .authenticationProvider(
//          oauthAuthenticationProvider(passwordEncoder())
//        );

      // UserDetailService
//       .userDetailsService(userDetailsService)
//       .passwordEncoder(passwordEncoder());
    } catch (Exception e) {
      throw new BeanInitializationException("Security configuration failed", e);
    }
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AuthenticationProvider oauthAuthenticationProvider(/*UserOperator userOperator, */PasswordEncoder passwordEncoder) {
    return new OAuthAuthenticationProvider(/* userOperator, */ passwordEncoder);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
      .antMatchers(HttpMethod.OPTIONS, "/**")
      .antMatchers("/app/**/*.{js,html}")
      .antMatchers("/i18n/**")
      .antMatchers("/content/**")
      .antMatchers("/swagger-ui/index.html")
      .antMatchers("/test/**")
      .antMatchers("/h2-console/**");
  }

  private void jwtConfigure(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(problemSupport)
        .accessDeniedHandler(problemSupport)
      .and()
        .csrf()
        .disable()
        .headers()
        .frameOptions()
        .disable()
      .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .authorizeRequests()
        .antMatchers("/api/register").permitAll()
        .antMatchers("/api/activate").permitAll()
        .antMatchers("/api/authenticate").permitAll()
        .antMatchers("/api/account/reset-password/init").permitAll()
        .antMatchers("/api/account/reset-password/finish").permitAll()
        .antMatchers("/api/**").authenticated()
        .antMatchers("/management/health").permitAll()
        .antMatchers("/management/info").permitAll()
        .antMatchers("/management/**").hasAuthority(ADMIN)
        .antMatchers("/v2/api-docs/**").permitAll()
        .antMatchers("/swagger-resources/configuration/ui").permitAll()
        .antMatchers("/swagger-ui/index.html").hasAuthority(ADMIN)
      .and()
        .apply(securityConfigurerAdapter());
  }

  private void oauthConfigure(HttpSecurity http) throws Exception {
    http
        .httpBasic().realmName("FullstackDemo App")
      .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .requestMatchers()
        .antMatchers("/oauth/authorize")
      .and()
        .authorizeRequests()
        .antMatchers("/oauth/authorize").authenticated();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    oauthConfigure(http);
//    jwtConfigure(http);
  }

  private JWTConfigurer securityConfigurerAdapter() {
    return new JWTConfigurer(tokenProvider);
  }

  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }
}
