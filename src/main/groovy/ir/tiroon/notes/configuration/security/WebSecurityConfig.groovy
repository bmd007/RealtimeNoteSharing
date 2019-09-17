package ir.tiroon.notes.configuration.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService

    @Bean
    AuthenticationManager authenticationManagerBean() throws Exception {
        super.authenticationManagerBean()
    }

    @Bean
    @Primary
    PasswordEncoder passwordEncoder() {
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    @Qualifier("authenticationProvider")
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        authenticationProvider
    }

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
        auth.authenticationProvider(authenticationProvider())
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //notice
        //--> NEVER EVER Implement a servlet(POST) , rest(POST) on Address *'/login'*
        http//add google login here to make this app a recourse app for google
                .authorizeRequests()

                .antMatchers("/login", "/javax.faces.resource/**",
                        "/logout", "/accessDenied", "/register.xhtml/**",
                        "/register.jsf/**", "/resources/**",
                        "/redirectAfterSuccessfulRegister.html").permitAll()

        //add /users here
                .antMatchers("/").access("hasRole('ADMIN')")

                .antMatchers("/notes", "/note/**", "/addNote", "/removeNote/**",
                        "/removeUser/**", "/addCollaboration/**", "/removeCollaboration/**", "/collaborators/**",
                        "/noteAppStopmEndpoint/**", "/noteChanged", "/whoIsNotCollaborating/**"
                        , "/addCollaborationByList/**", "/view/**").access("hasRole('USER')")

                .anyRequest().authenticated()

                .and().formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=1")
                .defaultSuccessUrl("/view/main.html", true)
                .permitAll()

                .and().exceptionHandling().accessDeniedPage("/accessDenied")

                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/logout")

        //since the hole web based front-end in these project is using Ajax, we do not need csrf check.
        //In fact, Cross-origin resource sharing (CORS) defend us against Ajax requests coming from non-native
        // origins.
                .and().csrf().disable()

//                .csrfTokenRepository(httpSessionCsrfTokenRepository())
//                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/register.xhtml")).disable()
    }

//    @Bean
//    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository(){
//        new HttpSessionCsrfTokenRepository()
//    }

}