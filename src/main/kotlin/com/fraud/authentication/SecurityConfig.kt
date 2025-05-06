package com.fraud.authentication

import com.fraud.authentication.JWT.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.java


@Configuration
@EnableWebSecurity
class SecurityConf (
    private val userDetailsService: UserDetailsService,
    private val jwtAuthFilter: JwtAuthenticationFilter,

    ){

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{ it.disable()}.authorizeHttpRequests {
//            it.requestMatchers("/users/v1/list").permitAll()
//                .anyRequest().authenticated()
//            it.requestMatchers("/users/v1/create/menu").permitAll()
//                .anyRequest().permitAll()
//            it.requestMatchers("/Public/**").permitAll().requestMatchers("/users/v1/**")
//                .authenticated()
//        }
//            .formLogin {
//                it.defaultSuccessUrl("/Public/menu", true)}
//            .userDetailsService(userDetailsService)
//        return http.build()
            it
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/users/register").permitAll()

                .anyRequest().authenticated()
        }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }
}