package com.scrumpokerpro.jira.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import com.scrumpokerpro.jira.exception.UnauthorizedException
import com.scrumpokerpro.jira.repository.TokenRepository
import com.scrumpokerpro.jira.service.jira.JiraTokenClient
import com.scrumpokerpro.jira.service.jira.RefreshTokenRequest
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

class JiraApiConfig(
    val tokenRepository: TokenRepository,
    val objectMapper: ObjectMapper,
    val jiraTokenClient: JiraTokenClient,
    @Value("\${spring.security.oauth2.client.registration.jira.client-id}") val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.jira.client-secret}") val clientSecret: String
) {

    @Bean
    fun requestInterceptor() = RequestInterceptor { template ->
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val token = tokenRepository.findByUserId(UUID.fromString(authentication.token.subject))
            ?: throw UnauthorizedException("You have to authorize ScrumPokerPro for Jira account")
        val jwt = JWTParser.parse(token.accessToken) as SignedJWT
        val payload: Map<String, Any> = objectMapper.readValue(jwt.payload.toString())
        if (Instant.ofEpochSecond((payload["exp"] as Int).toLong()) < Instant.now()) {
            val response = jiraTokenClient.refreshToken(
                RefreshTokenRequest(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    refreshToken = token.refreshToken
                )
            )
            token.apply {
                this.accessToken = response.accessToken
                this.modified = OffsetDateTime.now()
            }.also {
                tokenRepository.save(it)
            }
        }
        template.uri("/ex/jira/${token.cloudId}${template.path()}")
        template.header("Authorization", "Bearer ${token.accessToken}")
    }
}
