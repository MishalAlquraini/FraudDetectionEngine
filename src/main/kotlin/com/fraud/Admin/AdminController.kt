package com.fraud.Admin

import com.fraud.account.AccountBalance
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts")
    fun getAccounts() :List<AccountData>{
    return adminService.getAccounts()
    }

}