package com.scrumpokerpro.jira.service.field

import com.scrumpokerpro.jira.service.jira.Field
import com.scrumpokerpro.jira.service.jira.JiraApiClient
import org.springframework.stereotype.Service

@Service
class FieldServiceImpl(
    val jiraApiClient: JiraApiClient
) : FieldService {

    override fun getFields(): List<Field> {
        return jiraApiClient.getFields().filter {
            it.schema["type"] == "number" || it.schema["type"] == "string"
        }
    }
}
