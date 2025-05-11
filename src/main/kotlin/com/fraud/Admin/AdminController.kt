package com.fraud.Admin

import com.fraud.account.AccountService
import com.fraud.fraudFlag.FraudFlagService
import com.fraud.fraudFlag.ReviewFlagResponse
import com.fraud.transaction.TransactionEntity
import com.fraud.transaction.TransferDto
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val accountsService: AccountService,
    private val fraudFlagService: FraudFlagService,
) {
    //better switch to getmapping abd add the input in the url
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transactions")
    fun listFlaggedTransactions(): List<TransactionEntity?> {

        return adminService.listFlaggedTransactions()
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts")
    fun getAccounts() :List<AccountData>{
    return adminService.getAccounts()
    }


    //Deactivate account for user
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accounts/{accountNumber}/activate-toggle")
    fun closeAccount (@PathVariable accountNumber: String): ResponseEntity<*> =  accountsService.deactivateAccount(accountNumber)

    // mark a flag as reviewed
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/fraud/flags/{id}/review")
    fun reviewFlag(@PathVariable id: Long): ReviewFlagResponse = fraudFlagService.reviewFlag(id)



}