package com.scrumpokerpro.jira.service.jira

import com.scrumpokerpro.jira.config.JiraApiConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    url = "\${scrum-poker-pro-jira.atlassian.url}",
    name = "jira",
    contextId = "jiraApiClient",
    configuration = [
        JiraApiConfig::class
    ]
)
interface JiraApiClient {

    @PostMapping("/rest/api/2/search")
    fun getIssues(search: SearchRequest): PageSearch

    @GetMapping("/rest/api/2/project/search")
    fun getProjects(): PageProject

    @GetMapping("/rest/api/2/serverInfo")
    fun getServerInfo(): ServerInfo

    @GetMapping("/rest/api/2/field")
    fun getFields(): List<Field>

    @PutMapping("/rest/api/2/issue/{issueId}")
    fun updateIssue(
        @PathVariable issueId: String,
        @RequestBody updateIssueRequest: UpdateIssueRequest
    ): ResponseEntity<Any>
}
