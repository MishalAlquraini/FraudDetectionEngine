package com.fraud.Admin

import com.fraud.account.AccountService
import com.fraud.fraudFlag.FraudFlagService
import com.fraud.fraudFlag.ReviewFlagResponse
import com.fraud.transaction.TransactionEntity
import com.fraud.transaction.TransferDto
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val accountsService: AccountService,
    private val fraudFlagService: FraudFlagService,
) {
    //better switch to getmapping abd add the input in the url
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/transactions")
    fun showFlaggedTransactions(@RequestBody request: TransactionFilter): List<TransactionEntity?> {

        return adminService.listFlaggedTransactions(request)
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