package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.token.TokenService
import com.scrumpokerpro.jira.utils.userId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/status")
class StatusController(
    val tokenService: TokenService
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getStatus(@AuthenticationPrincipal principal: Jwt): StatusResponse {
        return StatusResponse(connected = tokenService.findByUserId(principal.userId()) != null)
    }
}

data class StatusResponse(
    val connected: Boolean = false
)
