package com.scrumpokerpro.jira.service.issue

import com.scrumpokerpro.jira.service.jira.Issue
import com.scrumpokerpro.jira.service.jira.JiraApiClient
import com.scrumpokerpro.jira.service.jira.SearchIssue
import com.scrumpokerpro.jira.service.jira.SearchRequest
import com.scrumpokerpro.jira.service.jira.UpdateIssue
import com.scrumpokerpro.jira.service.jira.UpdateIssueRequest
import feign.FeignException
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor
import org.springframework.stereotype.Service
import java.util.Objects
import java.util.concurrent.CompletableFuture

@Service
class IssueServiceImpl(
    val jiraApiClient: JiraApiClient,
) : IssueService {

    override fun getIssues(searchIssue: SearchIssue): List<Issue> {
        val issues = if (searchIssue.jql) {
            jiraApiClient.getIssues(SearchRequest(jql = searchIssue.text)).issues
        } else {
            val filters = listOf(
                "key = '${searchIssue.text}' ORDER BY created DESC",
                "summary ~ '${searchIssue.text}' ORDER BY created DESC",
                "description ~ '${searchIssue.text}' ORDER BY created DESC",
                "comment ~ '${searchIssue.text}' ORDER BY created DESC",
                "parent = '${searchIssue.text}' ORDER BY created DESC",
                "sprint = '${searchIssue.text}' ORDER BY created DESC"
            )
            val cf = filters.map {
                CompletableFuture.supplyAsync({
                    try {
                        jiraApiClient.getIssues(SearchRequest(jql = it)).issues
                    } catch (e: FeignException) {
                        emptyList()
                    }
                }, customTaskExecutor)
            }
            sequence(cf).get().flatten()
        }
        val serverInfo = jiraApiClient.getServerInfo()

        return issues.map {
            Issue(
                id = it.id,
                parentId = it.fields.get("parent")?.get("id")?.asText(),
                key = it.key,
                title = it.fields.get("summary").asText(),
                link = "${serverInfo.baseUrl}/browse/${it.key}",
                status = it.fields.get("status").get("name").asText(),
                type = it.fields.get("issuetype").get("name").asText(),
                iconUrl = it.fields.get("issuetype").get("iconUrl")?.asText(),
                subtask = it.fields.get("issuetype").get("subtask").asBoolean()
            )
        }.distinctBy { it.id }
    }

    override fun updateIssue(updateIssue: UpdateIssue) {
        val storyPoints = if (updateIssue.schema["type"] == "number") updateIssue.storyPoints.toDouble() else updateIssue.storyPoints
        jiraApiClient.updateIssue(
            issueId = updateIssue.issueId,
            updateIssueRequest = UpdateIssueRequest(
                mapOf(updateIssue.fieldId to storyPoints)
            )
        )
    }

    @Suppress("SpreadOperator")
    private fun <T> sequence(futures: List<CompletableFuture<T>>): CompletableFuture<List<T>> {
        val allDoneFuture = CompletableFuture.allOf(*futures.toTypedArray<CompletableFuture<*>>())
        return allDoneFuture.thenApply {
            futures.map { obj: CompletableFuture<T> -> obj.join() }.filter { Objects.nonNull(it) }
        }
    }

    companion object {
        val customTaskExecutor: DelegatingSecurityContextAsyncTaskExecutor
            get() {
                val delegate = ThreadPoolTaskExecutor()
                delegate.initialize()
                return DelegatingSecurityContextAsyncTaskExecutor(delegate)
            }
    }
}
