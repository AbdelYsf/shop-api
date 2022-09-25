package de.imedia24.shop.service

import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.exception.NoSuchProductFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService{
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly=true)
     override fun findProductBySku(sku: Int): ProductResponse {
        return productRepository.findBySku(sku)?.toProductResponse()
            .also { logger.info("calling findBySku with ksu: $sku") }
            ?: throw NoSuchProductFoundException("Product with sku: $sku not found !!")
                .also { logger.error("Product with sku: $sku not found !!") }
    }

    @Transactional(readOnly=true)
    override fun findProductsBySkus(skus: List<Int>): List<ProductResponse> {
        return productRepository.findAllBySkuIn(skus).map {entity-> entity.toProductResponse()}
    }

    override fun addProduct(productRequest: ProductRequest): ProductResponse {
        return productRepository.save(productRequest.toProductEntity())
            .toProductResponse()
    }



}
