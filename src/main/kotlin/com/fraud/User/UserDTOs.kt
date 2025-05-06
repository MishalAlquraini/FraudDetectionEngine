package com.fraud.User


data class RegisterUserRequest(
    val name: String,
    val email: String,
    val password: String
)

data class RegisterAdminRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: Roles = Roles.ADMIN
)

data class AdminResponse(
    val name: String,
    val email: String,
    val role: Roles = Roles.ADMIN
)