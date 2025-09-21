package org.example.gamearenax_backend.config;

import org.example.gamearenax_backend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author udarasan
 * @TimeStamp 2023-07-15 15:00
 * @ProjectDetails invoice_service
 */

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/authenticate",
                                "/api/v1/games/*",
                                "/api/v1/games/get/*",
                                "/api/v1/player/getByUsername",
                                "/api/v1/player/updatePlayer",
                                "/api/v1/password/*",
                                "/api/v1/user/register",
                                "/api/v1/user/check-email",
                                "/api/v1/user/check-username",
                                "/api/v1/user/*",
                                "/api/v1/streamers/getAll",
                                "/api/v1/player/getAllPlayers",
                                "/api/v1/tournament/getTournamentById",
                                "/api/v1/tournament/getUpcomingTournaments",
                                "/api/v1/tournament/getByOngoingStatus",
                                "/api/v1/clan/getAll",
                                "/api/v1/player/ban",
                                "/api/v1/player/unban",
                                "/api/v1/auth/verifyOTP",
                                "/api/v1/auth/refreshToken",
                                "/v3/api-docs/",
                                "/swagger-ui/",
                                "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}