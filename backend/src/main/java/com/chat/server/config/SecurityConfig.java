package com.chat.server.config;

import com.chat.server.filters.SimpleCORSFilter;
import com.chat.server.oauth2.service.OAuth2Filter;
import com.chat.server.security.UnauthorizedEntryPoint;
import com.chat.server.security.basic.AuthenticationManagerImpl;
import com.chat.server.security.basic.CustomAuthenticationProvider;
import com.chat.server.security.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.jboss.logging.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
/**
 * Created on 05.11.2015.
 */
@Configuration
//@ComponentScan(basePackages = {"com.chat.server.oauth2.service"})
@EnableWebSecurity
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = Logger.getLogger(SecurityConfig.class);

    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Autowired
    private OAuth2Filter oAuth2Filter;
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;
    @Autowired
    private SimpleCORSFilter simpleCORSFilter;

    protected AuthenticationProvider authenticationProvider;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/access/login").permitAll()
                .antMatchers("/api/access/refresh").permitAll()
                .antMatchers("/api/access/nickname").permitAll()
                .antMatchers("/api/access/register").permitAll()
                .and()
                .requestMatchers().antMatchers("/api/**")
                .and()
                .addFilterAfter(oAuth2Filter, BasicAuthenticationFilter.class)
                .addFilterBefore(simpleCORSFilter, ChannelProcessingFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**")).disable()
                .httpBasic()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
    }

    @Bean
    public AuthenticationManager authenticationManager()
    {
        return new AuthenticationManagerImpl();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        authenticationProvider = new CustomAuthenticationProvider();
        return authenticationProvider;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder;
//    }
}
