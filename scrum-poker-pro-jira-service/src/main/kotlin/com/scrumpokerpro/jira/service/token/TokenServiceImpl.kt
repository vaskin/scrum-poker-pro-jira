package com.scrumpokerpro.jira.service.token

import com.scrumpokerpro.jira.entity.Token
import com.scrumpokerpro.jira.repository.TokenRepository
import com.scrumpokerpro.jira.service.jira.JiraTokenClient
import com.scrumpokerpro.jira.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@Service
class TokenServiceImpl(
    val tokenRepository: TokenRepository,
    val jiraTokenClient: JiraTokenClient
) : TokenService {

    val log by logger()

    @Transactional
    override fun createToken(accessToken: String, refreshToken: String, userId: UUID): Token {
        val now = OffsetDateTime.now()
        val resources = jiraTokenClient.getAccessibleResource("Bearer $accessToken")

        return tokenRepository.findByUserId(userId)?.apply {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            this.cloudId = resources[0].id
            this.modified = now
        } ?: Token(
            accessToken = accessToken,
            refreshToken = refreshToken,
            cloudId = resources[0].id,
            userId = userId,
            created = now,
            modified = now
        ).also {
            log.info("create new jira token [userId = {}]", userId)
            tokenRepository.save(it)
        }
    }

    override fun findByUserId(userId: UUID): Token? {
        return tokenRepository.findByUserId(userId)
    }

    @Transactional
    override fun deleteToken(userId: UUID) {
        tokenRepository.findByUserId(userId)?.also {
            tokenRepository.delete(it)
        }
    }
}
