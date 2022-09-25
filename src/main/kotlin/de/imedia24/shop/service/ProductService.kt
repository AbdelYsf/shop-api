package de.imedia24.shop.service

import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse

interface ProductService{

    fun findProductBySku(sku: Int): ProductResponse

    fun findProductsBySkus(skus: List<Int>) : List<ProductResponse>

    fun addProduct(productRequest: ProductRequest) : ProductResponse

}
