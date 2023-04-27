package com.scrumpokerpro.jira.exception

import org.springframework.http.HttpStatus

enum class RestErrorReason(val code: String, val status: HttpStatus, val message: String) {
    DESERIALIZATION_FAILED(
        code = "/deserialization/failed",
        status = HttpStatus.BAD_REQUEST,
        message = "Deserialization failed"
    ),
    VALIDATION_FAILED(
        code = "/validation/failed",
        status = HttpStatus.BAD_REQUEST,
        message = "Validation failed"
    ),
    ACCESS_DENIED(
        code = "/access/denied",
        status = HttpStatus.FORBIDDEN,
        message = "Access denied"
    ),
    JIRA_ACCESS_DENIED(
        code = "/jira/access/denied",
        status = HttpStatus.FORBIDDEN,
        message = "Jira access denied"
    ),
    JIRA_BAD_REQUEST(
        code = "/jira/bad/request",
        status = HttpStatus.BAD_REQUEST,
        message = "Jira bad request"
    ),
    RESOURCE_NOT_FOUND(
        code = "/resource/not_found",
        status = HttpStatus.NOT_FOUND,
        message = "Resource not found"
    ),
    INTERNAL(
        code = "/internal",
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        message = "Internal error"
    )
}
