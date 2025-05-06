package com.fraud.User

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name="users")
data class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
    val name: String,
    val email: String,
    val password: String,
    val created_at: LocalDateTime,

    @Enumerated(EnumType.STRING)
    val role: Roles
){
    constructor() : this(null, "", "", "",
        LocalDateTime.now(), Roles.USER)
}
enum class Roles {
    USER, ADMIN
}