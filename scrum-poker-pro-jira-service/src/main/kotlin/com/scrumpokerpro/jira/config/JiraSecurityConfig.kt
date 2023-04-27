package com.scrumpokerpro.jira.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService

@Configuration
class JiraSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .requestMatchers().antMatchers("/login/**", "/oauth2/**")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .oauth2Login()
            .loginPage("/oauth2/authorization/jira")
            .redirectionEndpoint()
            .baseUri("/login/oauth/client/*")
            .and()
            .userInfoEndpoint()
            .userService(DefaultOAuth2UserService())
    }
}
