package com.fraud.User

import jakarta.inject.Named
import org.springframework.data.jpa.repository.JpaRepository


@Named
interface UserRepository : JpaRepository<UserEntity, Long>{
    fun findByEmail(email: String): UserEntity?
}