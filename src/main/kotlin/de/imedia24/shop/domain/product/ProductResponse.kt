package de.imedia24.shop.domain.product

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal

data class ProductResponse(
    val sku: Int,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val stock: BigDecimal
) {
    companion object {
        fun ProductEntity.toProductResponse() = ProductResponse(
            sku = sku,
            name = name,
            description = description ?: "",
            price = price,
            stock = stock
        )
    }
}
