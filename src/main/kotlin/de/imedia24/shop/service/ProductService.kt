package de.imedia24.shop.service

import de.imedia24.shop.domain.product.ProductResponse

interface ProductService{

    fun findProductBySku(sku: Int): ProductResponse
}
