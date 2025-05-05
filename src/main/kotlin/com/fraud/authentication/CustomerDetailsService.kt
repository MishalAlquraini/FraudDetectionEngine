package com.fraud.authentication

import com.fraud.User.UserEntity
import com.fraud.User.UserRepository
import org.springframework.security.core.userdetails.User

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomerDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user: UserEntity = userRepository.findByEmail(email) ?:
        throw UsernameNotFoundException("User not found ...")

        return User.builder()
            .username(user.email)
            .password(user.password)
            .build()
    }

}