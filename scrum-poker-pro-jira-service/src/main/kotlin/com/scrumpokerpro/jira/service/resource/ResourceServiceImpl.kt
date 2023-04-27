package com.scrumpokerpro.jira.service.resource

import com.scrumpokerpro.jira.exception.UnauthorizedException
import com.scrumpokerpro.jira.repository.TokenRepository
import com.scrumpokerpro.jira.service.jira.JiraTokenClient
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ResourceServiceImpl(
    val jiraTokenClient: JiraTokenClient,
    val tokenRepository: TokenRepository,
) : ResourceService {

    override fun getResources(): List<Resource> {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val token = tokenRepository.findByUserId(UUID.fromString(authentication.token.subject))
            ?: throw UnauthorizedException("You have to authorize ScrumPokerPro for Jira account")
        return jiraTokenClient.getAccessibleResource("Bearer ${token.accessToken}").map {
            Resource(
                cloudId = it.id,
                name = it.name,
                current = it.id == token.cloudId
            )
        }
    }

    @Transactional
    override fun setResource(cloudId: String) {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        tokenRepository.findByUserId(UUID.fromString(authentication.token.subject))?.copy(
            cloudId = cloudId
        )?.also {
            tokenRepository.save(it)
        }
    }
}

data class Resource(
    val cloudId: String,
    val name: String,
    val current: Boolean,
)
