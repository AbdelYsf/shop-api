package de.imedia24.shop.domain.product

import java.sql.Timestamp
import java.time.Instant

data class ProductListResponse(
    val count: Int,
    val products: List<ProductResponse>,
    val timestamp: Timestamp = Timestamp.from(Instant.now()),
)