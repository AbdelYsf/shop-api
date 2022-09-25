package de.imedia24.shop.controller

import de.imedia24.shop.controller.ProductController.Companion.PATH
import de.imedia24.shop.controller.ProductController.Companion.PRODUCE_TYPE
import de.imedia24.shop.domain.product.ProductListResponse
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping(PATH, produces = [PRODUCE_TYPE])
class ProductController(private val productService: ProductService) {

    companion object {
        const val PATH = "/products"
        const val PRODUCE_TYPE = "application/json;charset=utf-8"
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{sku}")
    fun findProductsBySku(@PathVariable("sku") sku: Int): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.findProductBySku(sku))
            .also { logger.info("Request for product of sku: $sku") }
    }

    @GetMapping
    fun getProductsBySkus(@RequestParam(value = "skus", required = true) skus: List<Int>)
        : ResponseEntity<ProductListResponse> {
        return productService.findProductsBySkus(skus)
            .let {foundProducts ->
                ResponseEntity.ok(ProductListResponse(foundProducts.size, foundProducts))
            }.also {
                logger.info("Request for products of skus: $skus")
            }
    }

    @PostMapping
    fun addProduct(@RequestBody productRequest: ProductRequest): ResponseEntity<ProductResponse> {
        return productService.addProduct(productRequest).let { createdProduct ->
            ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{createdSku}").build(createdProduct.sku)
            ).body(createdProduct) }
            .also { logger.info("Request for saving new product")}
    }

    @PatchMapping("/{sku}")
    fun updateProduct(
        @RequestBody productRequest: ProductRequest,
        @PathVariable("sku") sku: Int
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.updateProduct(productRequest, sku))
            .also { logger.info("Request for partially updating product of sku: $sku") }
    }
}
