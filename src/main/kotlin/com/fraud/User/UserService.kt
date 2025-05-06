package com.fraud.User

import com.sun.net.httpserver.Request
import jakarta.inject.Named
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime

const val MIN_CHAR = 6

@Named
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
){
   fun registerUser(@RequestBody request: RegisterUserRequest ): Any{
       if (request.password.length < MIN_CHAR){
           throw IllegalArgumentException("Password must be at least $MIN_CHAR characters")
       }
       val hashedPassword = passwordEncoder.encode(request.password)

       val newUser = UserEntity(
           name = request.name,
           email = request.email,
           password = hashedPassword,
           role = Roles.USER,
           created_at = LocalDateTime.now()
       )
       userRepository.save(newUser)
       return "User registered successfully"
   }

}
