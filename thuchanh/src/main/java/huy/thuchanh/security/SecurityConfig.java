package huy.thuchanh.security;

import huy.thuchanh.config.CustomSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/home").access("hasRole('ROLE_user')")
                .antMatchers("/admin/**").access("hasRole('ROLE_admin')")
//                .antMatchers("/huy/**").access("hasAnyRole('role_user','role_huy')")
                .antMatchers("/huy/**").access("hasAnyRole('ROLE_user','ROLE_admin')")
                .and().formLogin().successHandler(new CustomSuccessHandler())
                .usernameParameter("ssoId").passwordParameter("password")
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}123").roles("user");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}123").roles("admin");
        auth.inMemoryAuthentication().withUser("huy").password("{noop}123").roles("admin","user");

    }
}
