package com.scrumpokerpro.jira.repository

import com.scrumpokerpro.jira.entity.Token
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TokenRepository : JpaRepository<Token, UUID> {

    fun findByUserId(userId: UUID): Token?
}
