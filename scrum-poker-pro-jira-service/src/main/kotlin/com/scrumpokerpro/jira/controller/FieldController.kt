package com.scrumpokerpro.jira.controller

import com.scrumpokerpro.jira.service.field.FieldService
import com.scrumpokerpro.jira.service.jira.Field
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fields")
class FieldController(
    val fieldService: FieldService
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getFields(): List<Field> {
        return fieldService.getFields()
    }
}
