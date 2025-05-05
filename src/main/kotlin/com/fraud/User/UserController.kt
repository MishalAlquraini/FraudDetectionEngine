package com.fraud.User

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController (
    val userService: UserService
){

    @PostMapping("/users/register")
    fun registerUser(@RequestBody request: RegisterUserRequest): Any {
        try {
            return userService.registerUser(request)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }

    }
}