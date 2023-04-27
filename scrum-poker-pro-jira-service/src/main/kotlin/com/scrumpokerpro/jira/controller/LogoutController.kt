package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.token.TokenService
import com.scrumpokerpro.jira.utils.jwt
import com.scrumpokerpro.jira.utils.logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/logout")
class LogoutController(
    val tokenService: TokenService,
    val jwtDecoderByJwkKeySetUri: JwtDecoder
) {

    val log by logger()

    @GetMapping("/jira")
    fun logout(
        session: HttpSession,
        response: HttpServletResponse,
        @RequestParam(value = "access_token") accessToken: String,
        @RequestParam(value = "redirect_uri", required = false) redirectUri: String?
    ) {
        val jwt = accessToken.jwt(jwtDecoderByJwkKeySetUri)
        log.info("delete jira token [userId = {}]", jwt.subject)
        tokenService.deleteToken(UUID.fromString(jwt.subject))
        SecurityContextHolder.clearContext()
        session.invalidate()
        redirectUri?.also {
            response.sendRedirect(it)
        }
    }
}
