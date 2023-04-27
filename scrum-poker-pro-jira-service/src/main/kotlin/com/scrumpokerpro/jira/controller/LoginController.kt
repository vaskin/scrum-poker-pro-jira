package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.token.TokenService
import com.scrumpokerpro.jira.utils.jwt
import com.scrumpokerpro.jira.utils.logger
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/login")
class LoginController(
    val jwtDecoderByJwkKeySetUri: JwtDecoder,
    val tokenService: TokenService,
    val authorizedClientService: OAuth2AuthorizedClientService
) {

    val log by logger()

    @GetMapping("/jira")
    fun login(
        authentication: OAuth2AuthenticationToken,
        response: HttpServletResponse,
        @RequestParam(value = "access_token") accessToken: String,
        @RequestParam(value = "redirect_uri", required = false) redirectUri: String?
    ) {
        log.info("Jira token: {}", accessToken)
        val jwt = accessToken.jwt(jwtDecoderByJwkKeySetUri)
        val client = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
            authentication.authorizedClientRegistrationId,
            authentication.name
        )
        tokenService.createToken(
            accessToken = client.accessToken.tokenValue,
            refreshToken = client.refreshToken!!.tokenValue,
            userId = UUID.fromString(jwt.subject)
        )
        redirectUri?.also {
            response.sendRedirect(it)
        }
    }
}
