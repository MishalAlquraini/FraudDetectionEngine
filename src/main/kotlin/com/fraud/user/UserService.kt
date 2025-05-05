package com.fraud.user

import jakarta.inject.Named
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime

const val MIN_CHAR = 6

@Named
class UserService(
    private val userRepository: UserRepository
){
   fun registerUser(@RequestBody request: RegisterUserRequest ): Any{
       if (request.password.length < MIN_CHAR){
           throw IllegalArgumentException("Password must be at least $MIN_CHAR characters")
       }
       val newUser = UserEntity(
           name = request.name,
           email = request.email,
           password = request.password,
           role = Roles.USER,
           created_at = LocalDateTime.now()
       )
       userRepository.save(newUser)
       return "User registered successfully"
   }
}
