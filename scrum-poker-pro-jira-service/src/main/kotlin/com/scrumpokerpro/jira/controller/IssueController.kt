package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.issue.IssueService
import com.scrumpokerpro.jira.service.jira.Issue
import com.scrumpokerpro.jira.service.jira.SearchIssue
import com.scrumpokerpro.jira.service.jira.UpdateIssue
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/issues")
class IssueController(
    val issueService: IssueService
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getIssues(searchIssue: SearchIssue): List<Issue> {
        return issueService.getIssues(searchIssue)
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    fun updateIssue(@RequestBody updateIssue: UpdateIssue) {
        issueService.updateIssue(updateIssue)
    }
}
