package tn.esprit.PFE.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tn.esprit.PFE.security.jwt.AuthEntryPointJwt;
import tn.esprit.PFE.security.jwt.AuthTokenFilter;
import tn.esprit.PFE.security.services.UserDetailsServiceImpl;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;





@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
             .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
             .authorizeRequests().antMatchers("/api/**",
                    "/api/admin/retrieve-user-by-sexe/{user-sexe}",
                    "/retrieve-user-by-company/{company-name}",
                    "/api/retrieve-user-by-adress/{user-adress}",
                    "/api/register-CONTRIBUTEUR",
                    "/api/register-USER",
                    "/api/register-Administrateur",
                    "/api//save/Moderateur",
                    "api/rest-password/{name}",
                    "/api/confirm-password/{new}/{confirm}",
                    "/api/listNews",
                    "/api/delete/{newsId}",
                    "/api/active-account",
                    "/api/commentair/{idu}/{idp}",
                    "/retrieve-news-by-title/{news-title}",
                    "/company-name",
                    "/api/confirm-account",
                    "/api/compagny/**",
                    "api/token/refresh/**",
                    "/api/email/save ",
                    " /ws-notification/**",
                    "/topic/notif",
                    "/topic/notif/***"

            ).permitAll();
    http.authorizeRequests().antMatchers(GET, "/api/users/**", "api/signin/**").hasAnyAuthority("ROLE_USER");
    http.authorizeRequests().antMatchers(POST, "/api/company/**", "api/signin/**").hasAnyAuthority("ROLE_COMPANY");
    http .authorizeRequests().antMatchers(GET, "/api/admin/**", "api/signin/**").hasAnyAuthority("ROLE_ADMIN");
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:4200");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.addExposedHeader("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
