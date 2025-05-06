package com.fraud.Admin

import com.fraud.Transaction.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AdminController (
    private val adminService: AdminService
){
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/transactions")
    fun listAllTransactions(@RequestBody request: TransferDto): List<TransactionEntity?> {

        return adminService.listAllTransactions(request)
    }
}