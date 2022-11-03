package huy.thuchanh2;

import huy.thuchanh2.rest.CustomAccessDeniedHandler;
import huy.thuchanh2.rest.JwtAuthenticationTokenFiter;
import huy.thuchanh2.rest.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@ComponentScan("huy.thuchanh2")
public class SecurityConfige  extends WebSecurityConfigurerAdapter {
    @Bean
    public JwtAuthenticationTokenFiter jwtAuthenticationTokenFiter(){
        return new JwtAuthenticationTokenFiter();
    }
    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }
    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("kai").password("{noop}123").roles("USER").and()
                .withUser("huy").password("{noop}123").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**");
        http.httpBasic().authenticationEntryPoint(restServicesEntryPoint());
        http.authorizeRequests()
                .antMatchers("/","/rest/login").permitAll()
                .antMatchers(HttpMethod.GET,"/rest/**").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/rest/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE,"/rest/**").hasRole("USER")
                .antMatchers(HttpMethod.GET,"/admin/**").hasRole("USER")
                .antMatchers().authenticated()
                .and().csrf().disable();
        http.addFilterBefore(jwtAuthenticationTokenFiter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
    }
}
