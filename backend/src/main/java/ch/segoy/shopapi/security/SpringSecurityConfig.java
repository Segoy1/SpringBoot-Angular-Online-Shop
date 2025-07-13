package ch.segoy.shopapi.security;

import ch.segoy.shopapi.security.JWT.JwtEntryPoint;
import ch.segoy.shopapi.security.JWT.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * Created By Zhu Lin on 1/1/2019.
 */
@Configuration
@EnableWebSecurity
@DependsOn("passwordEncoder")
public class SpringSecurityConfig {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    private JwtEntryPoint accessDenyHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth
            .jdbcAuthentication()
            .usersByUsernameQuery(usersQuery)
            .authoritiesByUsernameQuery(rolesQuery)
            .dataSource(dataSource)
            .passwordEncoder(passwordEncoder);
        return auth.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/cart/**").hasAnyRole("CUSTOMER")
                .requestMatchers("/order/finish/**").hasAnyRole("EMPLOYEE", "MANAGER")
                .requestMatchers("/order/**").authenticated()
                .requestMatchers("/profiles/**").authenticated()
                .requestMatchers("/seller/product/new").hasAnyRole("MANAGER")
                .requestMatchers("/seller/**/delete").hasAnyRole("MANAGER")
                .requestMatchers("/seller/**").hasAnyRole("EMPLOYEE", "MANAGER")
                .anyRequest().permitAll()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(accessDenyHandler))
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.getOrBuild();
    }
}
