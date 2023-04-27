package com.scrumpokerpro.jira.entity

import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Token(
    @Id
    val id: UUID = UUID.randomUUID(),
    var accessToken: String,
    var refreshToken: String,
    var cloudId: String,
    val userId: UUID,
    val created: OffsetDateTime,
    var modified: OffsetDateTime
)
