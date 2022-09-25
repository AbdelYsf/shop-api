package de.imedia24.shop.domain.product

import java.sql.Timestamp
import java.time.ZonedDateTime

data class ErrorResponse(
    val status: Int,
    val message: String?,
    val timestamp: Timestamp = Timestamp.from(ZonedDateTime.now().toInstant()),
)
