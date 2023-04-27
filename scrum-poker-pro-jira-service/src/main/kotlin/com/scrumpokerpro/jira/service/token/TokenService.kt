package com.scrumpokerpro.jira.service.token

import com.scrumpokerpro.jira.entity.Token
import java.util.UUID

interface TokenService {

    fun createToken(accessToken: String, refreshToken: String, userId: UUID): Token

    fun deleteToken(userId: UUID)

    fun findByUserId(userId: UUID): Token?
}
