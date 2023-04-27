package com.scrumpokerpro.jira.service.field

import com.scrumpokerpro.jira.service.jira.Field

interface FieldService {

    fun getFields(): List<Field>
}
