package com.scrumpokerpro.jira

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ScrumPokerProJiraApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<ScrumPokerProJiraApplication>(*args)
}
