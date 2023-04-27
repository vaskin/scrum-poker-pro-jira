package com.scrumpokerpro.jira.service.project

import com.scrumpokerpro.jira.service.jira.JiraApiClient
import com.scrumpokerpro.jira.service.jira.Project
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    val jiraApiClient: JiraApiClient
) : ProjectService {

    override fun getProjects(): List<Project> {
        return jiraApiClient.getProjects().values
    }
}
