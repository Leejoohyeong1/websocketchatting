package com.lee.sama.websocketchat.config;


import com.lee.sama.websocketchat.auth.service.PrincipalDetailsService;
import com.lee.sama.websocketchat.jwt.JwtAccessDeniedHandler;
import com.lee.sama.websocketchat.jwt.JwtAuthenticationEntryPoint;
import com.lee.sama.websocketchat.jwt.JwtTokenAuthenticationFilter;
import com.lee.sama.websocketchat.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**")
                .requestMatchers("/docs/**");

    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                if(authentication == null){
                    throw new InternalAuthenticationServiceException("Authentication is null");
                }
                String username = authentication.getName();
                if(authentication.getCredentials() == null){
                    throw new AuthenticationCredentialsNotFoundException("Credentials is null");
                }
                String password = authentication.getCredentials().toString();
                UserDetails loadedUser = principalDetailsService.loadUserByUsername(username);
                if(loadedUser == null){
                    throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
                }
                /* checker */
                if(!loadedUser.isAccountNonLocked()){
//                LockedException : 계정잠김
                    throw new LockedException("User account is locked");
                }
                if(!loadedUser.isEnabled()){
//                DisabledException : 계정비활성화
                    throw new DisabledException("User is disabled");
                }
                if(!loadedUser.isAccountNonExpired()){
//                AccountExpiredException : 계정만료
                    throw new AccountExpiredException("User account has expired");
                }
                /* 실질적인 인증 */
                if(!passwordEncoder().matches(password, loadedUser.getPassword())){
//                BadCredentialsException : 비밀번호불일치
                    throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
                }
                /* checker */
                if(!loadedUser.isCredentialsNonExpired()){
//                CredentialsExpiredException : 비밀번호만료
                    throw new CredentialsExpiredException("User credentials have expired");
                }
                /* 인증 완료 */
                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
                result.setDetails(authentication.getDetails());
                return result;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addExposedHeader("accessToken"); // 노출할 헤더 설정
        config.addExposedHeader("refreshToken"); // 노출할 헤더 설정
        config.setAllowCredentials(true); // json을 자바스크립트에서 처리할 수 있게 설정
        config.addAllowedOriginPattern("*");  // 모든 ip의 응답을 허용
        config.addAllowedHeader("*"); // 모든 header의 응답을 허용
        config.addAllowedMethod("*"); // 모든 post, put 등의 메서드에 응답을 허용
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }

    @Bean
    SecurityFilterChain springWebFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(
                        authorize -> authorize
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilter(corsFilter())
                .addFilterBefore(new JwtTokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
