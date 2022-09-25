package de.imedia24.shop.db.repository

import de.imedia24.shop.db.entity.ProductEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface ProductRepository : CrudRepository<ProductEntity, Int> {
    fun findBySku(sku: Int): ProductEntity?
    fun findAllBySkuIn(skus: List<Int>): List<ProductEntity>

    @Modifying
    @Query("UPDATE ProductEntity SET price = :price , description = :description, name = :name WHERE sku = :sku")
    fun updateProduct(price: BigDecimal, description: String, name: String, sku: Int)
}
