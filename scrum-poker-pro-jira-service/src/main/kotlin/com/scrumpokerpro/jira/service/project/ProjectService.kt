package com.scrumpokerpro.jira.service.project

import com.scrumpokerpro.jira.service.jira.Project

interface ProjectService {

    fun getProjects(): List<Project>
}
