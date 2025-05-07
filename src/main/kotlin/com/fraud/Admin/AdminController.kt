package com.fraud.Admin

import com.fraud.transaction.TransactionEntity
import com.fraud.transaction.TransferDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
//@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService
) {
    //better switch to getmapping abd add the input in the url
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/transactions")
    fun listAllTransactions(@RequestBody request: TransactionFilter): List<TransactionEntity?> {

        return adminService.listAllTransactions(request)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts")
    fun getAccounts() :List<AccountData>{
    return adminService.getAccounts()
    }

}