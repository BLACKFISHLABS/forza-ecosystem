package io.github.blackfishlabs.forza.core.infra.security;

import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String AUTH_USER = "ROLE_USER";
    private static final String AUTH_ADMIN = "ROLE_ADMIN";
    private static final String AUTH_MANAGER = "ROLE_MANAGER";

    @Autowired
    private UserDetailsService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/components/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("index.html").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/api/v1/receita/**").permitAll()
                .antMatchers("/api/v1/cep/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/company/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/company/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/salesman/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/user/**").permitAll()

                // Global Authority to OPTIONS (permit all).
                .antMatchers(HttpMethod.OPTIONS, ResourcePaths.ALL).permitAll()

                // Public (permit all).
                .antMatchers(ResourcePaths.PUBLIC_PATH + ResourcePaths.ALL).permitAll()

                // Api Permit all Roles
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority(AUTH_ADMIN, AUTH_MANAGER, AUTH_USER)
                .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(AUTH_ADMIN, AUTH_MANAGER)
                .antMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority(AUTH_ADMIN, AUTH_MANAGER)
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority(AUTH_ADMIN, AUTH_MANAGER)

                // Any request only authenticated
                .anyRequest().fullyAuthenticated().and()
                .logout().logoutUrl(ResourcePaths.LOGOUT_PATH).logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler());

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}