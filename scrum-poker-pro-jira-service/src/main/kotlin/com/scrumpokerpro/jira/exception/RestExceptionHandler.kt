package com.scrumpokerpro.jira.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.scrumpokerpro.jira.utils.logger
import feign.FeignException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestControllerAdvice("com.scrumpokerpro.jira.controller")
class RestExceptionHandler(
    val objectMapper: ObjectMapper
) {

    val log by logger()

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<RestError> {
        val deserializationFailedReason = RestErrorReason.DESERIALIZATION_FAILED.also {
            log.error("${it.status}", ex)
        }

        val missingKotlinParameters =
            (ex.cause as? MissingKotlinParameterException)?.path?.joinToString { it.fieldName }

        return ResponseEntity.status(deserializationFailedReason.status)
            .body(
                RestError(
                    type = deserializationFailedReason.code,
                    title = deserializationFailedReason.message,
                    status = deserializationFailedReason.status.value(),
                    detail = missingKotlinParameters ?: ex.message.orEmpty(),
                    path = getInstanceUriFromRequest()
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<RestError> {
        val validationFailedReason = RestErrorReason.VALIDATION_FAILED.also {
            log.error("${it.status}", ex)
        }

        return ResponseEntity.status(validationFailedReason.status)
            .body(
                RestError(
                    type = validationFailedReason.code,
                    title = validationFailedReason.message,
                    status = validationFailedReason.status.value(),
                    detail = ex.bindingResult.allErrors.joinToString {
                        "${it.defaultMessage} [${(it as FieldError).field}]"
                    },
                    path = getInstanceUriFromRequest()
                )
            )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<RestError> {
        val accessDeniedReason = RestErrorReason.ACCESS_DENIED.also {
            log.error("${it.status}", ex)
        }

        return ResponseEntity.status(accessDeniedReason.status)
            .body(
                RestError(
                    type = accessDeniedReason.code,
                    title = accessDeniedReason.message,
                    status = accessDeniedReason.status.value(),
                    detail = ex.message.orEmpty(),
                    path = getInstanceUriFromRequest()
                )
            )
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<RestError> {
        val accessDeniedReason = RestErrorReason.JIRA_ACCESS_DENIED.also {
            log.error("${it.status}", ex)
        }

        return ResponseEntity.status(accessDeniedReason.status)
            .body(
                RestError(
                    type = accessDeniedReason.code,
                    title = accessDeniedReason.message,
                    status = accessDeniedReason.status.value(),
                    detail = ex.message.orEmpty(),
                    path = getInstanceUriFromRequest()
                )
            )
    }

    @ExceptionHandler(FeignException.BadRequest::class)
    fun handleFeignBadRequestException(ex: FeignException.BadRequest): ResponseEntity<RestError> {
        val jiraBadRequestReason = RestErrorReason.JIRA_BAD_REQUEST.also {
            log.error("${it.status}", ex)
        }

        val detail = if (ex.responseBody().isPresent) {
            val response = objectMapper.readTree(ex.responseBody().get().array())
            val errors = if (response.get("errors")?.elements()?.hasNext() == true) {
                response.get("errors")?.elements()?.next()?.textValue()
            } else null
            val errorMessages = if (response.get("errorMessages")?.elements()?.hasNext() == true) {
                response.get("errorMessages")?.elements()?.next()?.textValue()
            } else null
            errors ?: errorMessages
        } else null

        return ResponseEntity.status(jiraBadRequestReason.status)
            .body(
                RestError(
                    type = jiraBadRequestReason.code,
                    title = jiraBadRequestReason.message,
                    status = jiraBadRequestReason.status.value(),
                    detail = detail ?: ex.message.orEmpty(),
                    path = getInstanceUriFromRequest()
                )
            )
    }

    @ExceptionHandler(FeignException.Forbidden::class)
    fun handleFeignForbiddenException(ex: FeignException.Forbidden): ResponseEntity<RestError> {
        val accessDeniedReason = RestErrorReason.JIRA_ACCESS_DENIED.also {
            log.error("${it.status}", ex)
        }

        return ResponseEntity.status(accessDeniedReason.status)
            .body(
                RestError(
                    type = accessDeniedReason.code,
                    title = accessDeniedReason.message,
                    status = accessDeniedReason.status.value(),
                    detail = ex.message.orEmpty(),
                    path = getInstanceUriFromRequest()
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleBase(ex: Exception): ResponseEntity<RestError> {
        val internalReason = RestErrorReason.INTERNAL.also {
            log.error("${it.status}", ex)
        }

        return ResponseEntity.status(internalReason.status)
            .body(
                RestError(
                    type = internalReason.code,
                    title = internalReason.message,
                    status = internalReason.status.value(),
                    detail = ex.message.orEmpty(),
                    path = ""
                )
            )
    }

    private fun getInstanceUriFromRequest(): String = ServletUriComponentsBuilder.fromCurrentRequest()
        .host(null)
        .scheme(null)
        .build()
        .toUriString()
}
