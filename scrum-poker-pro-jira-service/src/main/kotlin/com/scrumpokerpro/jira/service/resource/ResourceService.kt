package com.scrumpokerpro.jira.service.resource

interface ResourceService {

    fun getResources(): List<Resource>

    fun setResource(cloudId: String)
}
