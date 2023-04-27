package com.scrumpokerpro.jira.service.jira

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    url = "\${scrum-poker-pro-jira.atlassian.url}",
    name = "jira",
    contextId = "jiraTokenClient"
)
interface JiraTokenClient {

    @GetMapping("/oauth/token/accessible-resources")
    fun getAccessibleResource(@RequestHeader("Authorization") token: String): List<AccessibleResource>

    @PostMapping("/oauth/token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): RefreshTokenResponse
}
