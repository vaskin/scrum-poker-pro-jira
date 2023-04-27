package com.scrumpokerpro.jira.utils

import com.scrumpokerpro.jira.exception.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidationException
import java.util.UUID

fun Jwt.userId(): UUID = UUID.fromString(subject)

fun String.jwt(jwtDecoderByJwkKeySetUri: JwtDecoder): Jwt {
    return try {
        jwtDecoderByJwkKeySetUri.decode(this)
    } catch (e: JwtValidationException) {
        SecurityContextHolder.clearContext()
        throw UnauthorizedException(e.message)
    } catch (e: BadJwtException) {
        SecurityContextHolder.clearContext()
        throw UnauthorizedException(e.message)
    }
}
