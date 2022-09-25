package de.imedia24.shop.db.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @Column(name = "sku", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sku: Int = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "price", nullable = false)
    val price: BigDecimal,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
)
