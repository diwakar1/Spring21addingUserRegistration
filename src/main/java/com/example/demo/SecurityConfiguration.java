package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private  SSUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
              .authenticationProvider(authenticationProvider());
             // .userDetailsService(userDetailsServiceBean());


    }
   /* @Bean
    public CustomDa0uthenticationProvider daoAuthenticationProvider() {
        CustomDaoAuthenticationProvider authenticationProvider = new CustomDaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        System.out.println("Using my custom DaoAuthenticationProvider");
        return authenticationProvider;
    }*/


/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
*/



   /* @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .authorizeRequests()
               .antMatchers("/").permitAll()
               .and()
               .authorizeRequests()
               .antMatchers("/h2-console/**").permitAll()
               .and()
               .authorizeRequests()
               .antMatchers("/register").permitAll()
               .anyRequest().authenticated()
               .and()
               .formLogin().loginPage("/login").permitAll()
               .and()
               .logout()
               .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
               .logoutSuccessUrl("/login").permitAll().permitAll()
               .and()
               .httpBasic();
       http
               .csrf().disable();
       http
               .headers().frameOptions().disable();
    }
}
