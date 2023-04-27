package com.scrumpokerpro.jira.service.jira

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class AccessibleResource(
    val avatarUrl: String,
    val name: String,
    val url: String,
    val id: String,
    val scopes: List<String>
)

data class RefreshTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    val scope: String?,
    @JsonProperty("expires_in")
    val expiresIn: Double,
    @JsonProperty("token_type")
    val tokenType: String
)

data class RefreshTokenRequest(
    @JsonProperty("grant_type")
    val grantType: String = "refresh_token",
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
    @JsonProperty("refresh_token")
    val refreshToken: String
)

data class SearchIssue(
    val text: String,
    val jql: Boolean = false,
    val project: String? = null,
    val sprint: String? = null,
    val status: String? = null
)

data class SearchRequest(
    val jql: String,
    val expand: List<String> = listOf("names", "schema"),
    val startAt: Int = 0,
    val maxResults: Int = 50
)

data class UpdateIssue(
    val issueId: String,
    val storyPoints: String,
    val fieldId: String,
    val schema: Map<String, Any> = mapOf()
)

data class UpdateIssueRequest(
    val fields: Map<String, Any>
)

data class PageIssue(
    val id: String,
    val key: String,
    val fields: JsonNode
)

data class PageSearch(
    val maxResults: Int,
    val startAt: Int,
    val total: Int,
    val issues: List<PageIssue>
)

data class PageProject(
    val isLast: Boolean,
    val maxResults: Int,
    val startAt: Int,
    val total: Int,
    val values: List<Project>
)

data class Project(
    val id: String,
    val key: String,
    val name: String
)

data class Issue(
    val id: String,
    val parentId: String?,
    val key: String,
    val link: String,
    val title: String,
    val status: String,
    val type: String,
    val iconUrl: String?,
    val subtask: Boolean = false
)

data class ServerInfo(
    val baseUrl: String
)

data class Field(
    val id: String,
    val name: String,
    val schema: Map<String, Any> = mapOf(),
    val custom: Boolean
)
