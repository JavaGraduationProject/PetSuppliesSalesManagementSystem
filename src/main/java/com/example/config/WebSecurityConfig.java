package com.example.config;

import com.example.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/login/**", "/", "/login/registration", "/login.html", "/").permitAll()
                .antMatchers("/user/**").hasRole("1")
                .antMatchers("/admin/**").hasRole("2")
                .anyRequest().permitAll().and()
                .formLogin().loginPage("/login_page").usernameParameter("username")//设置登录账号参数，与表单参数一致
                .passwordParameter("password")//设置登录密码参数，与表单参数一致
                .loginProcessingUrl("/login/login").defaultSuccessUrl("/login/login")//登录成功后默认的跳转页面路径
                .failureUrl("/").and()
                .logout().logoutUrl("/logout").permitAll().and()
                .sessionManagement().invalidSessionUrl("/") //失效后跳转到登陆页面
                .and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/front/**");
    }
}
