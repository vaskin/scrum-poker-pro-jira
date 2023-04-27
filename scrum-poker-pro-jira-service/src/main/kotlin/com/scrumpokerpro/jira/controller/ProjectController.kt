package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.jira.Project
import com.scrumpokerpro.jira.service.project.ProjectService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectService: ProjectService
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getProjects(): List<Project> {
        return projectService.getProjects()
    }
}
