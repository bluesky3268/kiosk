package korIT.kiosk.configure;

import korIT.kiosk.controller.AdminController;
import korIT.kiosk.controller.LoginSuccessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 권한별 페이지 접근 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/admin/supervisor", "/admin/shopList").access("hasRole('ROLE_SUPERVISOR')")
                .antMatchers("/admin/manager").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_SUPERVISOR')")
                .antMatchers("/admin/main", "/admin/join", "/admin/loginForm").permitAll()
                .and()
                .formLogin()
                .loginPage("/admin/loginForm")
                .loginProcessingUrl("/admin/login")
                .successHandler(new LoginSuccessController());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
