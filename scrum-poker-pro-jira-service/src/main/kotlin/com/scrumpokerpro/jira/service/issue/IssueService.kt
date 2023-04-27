package com.scrumpokerpro.jira.service.issue

import com.scrumpokerpro.jira.service.jira.Issue
import com.scrumpokerpro.jira.service.jira.SearchIssue
import com.scrumpokerpro.jira.service.jira.UpdateIssue

interface IssueService {

    fun getIssues(searchIssue: SearchIssue): List<Issue>

    fun updateIssue(updateIssue: UpdateIssue)
}
