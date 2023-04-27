package com.scrumpokerpro.jira.exception

data class RestError(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
    val path: String
)
