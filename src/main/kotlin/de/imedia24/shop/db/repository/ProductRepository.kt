package de.imedia24.shop.db.repository

import de.imedia24.shop.db.entity.ProductEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CrudRepository<ProductEntity, Int> {
    fun findBySku(sku: Int): ProductEntity?
    fun findAllBySkuIn(skus: List<Int>): List<ProductEntity>

}
