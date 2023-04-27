package com.scrumpokerpro.jira.config

import com.scrumpokerpro.jira.config.ApiSecurityConfig.Companion.CONFIG_ORDER
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
@Order(CONFIG_ORDER)
class ApiSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .requestMatchers().antMatchers("/issues/**", "/projects/**", "/fields/**", "/status/**", "/resources/**")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt().jwtAuthenticationConverter(RealmRoleConverter())
    }

    @Bean
    fun methodInvokingFactoryBean(): MethodInvokingFactoryBean {
        return MethodInvokingFactoryBean().apply {
            this.targetClass = SecurityContextHolder::class.java
            this.targetMethod = "setStrategyName"
            this.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
        }
    }

    companion object {
        const val CONFIG_ORDER = 99
    }
}
