package de.imedia24.shop.controller

import de.imedia24.shop.controller.ProductController.Companion.PATH
import de.imedia24.shop.controller.ProductController.Companion.PRODUCE_TYPE

import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

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
}
