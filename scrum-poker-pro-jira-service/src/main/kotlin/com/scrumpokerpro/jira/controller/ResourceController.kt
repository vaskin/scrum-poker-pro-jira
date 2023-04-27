package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.resource.Resource
import com.scrumpokerpro.jira.service.resource.ResourceService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/resources")
class ResourceController(
    val resourceService: ResourceService,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getResources(): List<Resource> {
        return resourceService.getResources()
    }

    @PutMapping("/{cloudId}")
    @PreAuthorize("hasRole('USER')")
    fun setResource(@PathVariable cloudId: String) {
        resourceService.setResource(cloudId)
    }
}
