package de.imedia24.shop.domain.product

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal

data class ProductRequest(val name: String, val description: String?, val price: BigDecimal, val stock: BigDecimal) {

    fun toProductEntity() = ProductEntity(
        name = name,
        description = description ?: "",
        price = price,
        stock = stock
    )
}
