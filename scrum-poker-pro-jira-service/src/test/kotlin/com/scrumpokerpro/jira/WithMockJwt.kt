package com.scrumpokerpro.jira

import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.lang.annotation.Inherited

@kotlin.annotation.Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Inherited
@WithSecurityContext(factory = JwtAuthFactory::class)
annotation class WithMockJwt

class JwtAuthFactory : WithSecurityContextFactory<WithMockJwt> {
    override fun createSecurityContext(withUser: WithMockJwt): SecurityContext {
        val jwt = Jwt
            .withTokenValue("token")
            .claim("sub", "a8c87ad7-e7e9-4d45-a7dc-e6c2a8dae4fa")
            .header("alg", "none")
            .build()
        val authorities = AuthorityUtils.createAuthorityList("ROLE_USER")
        val authentication = JwtAuthenticationToken(jwt, authorities)

        return SecurityContextHolder.createEmptyContext().apply {
            this.authentication = authentication
        }
    }
}
