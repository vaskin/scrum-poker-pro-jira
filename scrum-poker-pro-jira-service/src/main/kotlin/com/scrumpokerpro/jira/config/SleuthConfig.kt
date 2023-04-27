package com.scrumpokerpro.jira.config

import org.springframework.cloud.sleuth.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter
import javax.servlet.http.HttpServletResponse

@Configuration(proxyBeanMethods = false)
class SleuthConfig {

    @Bean
    fun traceIdInResponseFilter(tracer: Tracer): Filter {
        return Filter { request, response, chain ->
            tracer.currentSpan()?.also {
                val resp = response as HttpServletResponse
                resp.addHeader("X-B3-TraceId", it.context().traceId())
                resp.addHeader("X-B3-SpanId", it.context().spanId())
            }
            chain.doFilter(request, response)
        }
    }
}
