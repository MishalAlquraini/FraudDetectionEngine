package com.fraud.fraudFlag

import org.springframework.data.jpa.repository.JpaRepository

interface FraudFlagRepository : JpaRepository<FraudFlagEntity, Long> {

}
