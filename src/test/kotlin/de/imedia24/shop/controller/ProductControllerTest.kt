package de.imedia24.shop.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ninjasquad.springmockk.MockkBean
import de.imedia24.shop.domain.product.ProductListResponse
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductServiceImpl
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.nio.charset.StandardCharsets

@WebMvcTest
internal class ProductControllerTest(
    @Autowired val mockMvc: MockMvc
) {
    private val objectMapper = ObjectMapper().registerKotlinModule()
    private  val path = "/products"
    private  val produceType = "application/json;charset=utf-8"

    @MockkBean
    lateinit var mockedProductService: ProductServiceImpl

    @Test
    fun getProductsBySkus() {
        // arrange
        val r1 = ProductResponse(123, "tv", "this is a tv", BigDecimal(345), BigDecimal(122))
        val r2 = ProductResponse(563, "iphone 14", "you will run out of money when you buy me :)",
            BigDecimal(345), BigDecimal(122))
        val mockedResponse = listOf(r1, r2)
        val skus = listOf(123, 563)
        val expectedResponse = ProductListResponse(mockedResponse.size, mockedResponse)
        every { mockedProductService.findProductsBySkus(skus) } returns listOf(r1, r2)

        // act & assert
        mockMvc.perform(get("$path?skus=${skus.joinToString(",")}"))
            .andExpect(status().isOk).andExpect(content().contentType(produceType))
            .andExpect { context ->
                val response: ProductListResponse =
                    objectMapper.readValue(context.response.getContentAsString(StandardCharsets.UTF_8), ProductListResponse::class.java)
                assertEquals(response.count, expectedResponse.count)
                assertEquals(response.products, expectedResponse.products)
                assertEquals(response.products[0].name, r1.name)
                assertEquals(response.products[0].description, r1.description)
                assertEquals(response.products[0].price, r1.price)
                assertEquals(response.products[0].stock, r1.stock)
                assertEquals(response.products[1].name, r2.name)
                assertEquals(response.products[1].description, r2.description)
                assertEquals(response.products[1].price, r2.price)
                assertEquals(response.products[1].stock, r2.stock)

            }

        verify(exactly = 1) { mockedProductService.findProductsBySkus(skus) }
    }

    @Test
    fun `getProductsBySkus returns empty`() {
        // arrange
        val skus = listOf(12, 56)

        every { mockedProductService.findProductsBySkus(skus) } returns emptyList()

        // act & assert
        mockMvc.perform(get("$path?skus=${skus.joinToString(",")}"))
            .andExpect(status().isOk).andExpect(content().contentType(produceType))
            .andExpect { context ->
                val response =
                    objectMapper.readValue(context.response.getContentAsString(StandardCharsets.UTF_8), ProductListResponse::class.java)
                assertEquals(response.count, 0)
                assertEquals(response.products.size, 0)
            }

        verify(exactly = 1) { mockedProductService.findProductsBySkus(skus) }
    }

    @Test
    fun `getProductsBySkus returns badRequest`() {
        // arrange
        every { mockedProductService.findProductsBySkus(any()) } returns emptyList()
        // act & assert
        mockMvc.perform(get("$path?bad_param"))
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { mockedProductService.findProductsBySkus(any()) }
    }

    @Test
    fun `getProductsBySkus returns internal server error`() {
        // arrange
        every { mockedProductService.findProductsBySkus(any()) } throws RuntimeException("Bom!")
        // act & assert
        mockMvc.perform(get("$path?skus=123"))
            .andExpect(status().is5xxServerError)

        verify(exactly = 1) { mockedProductService.findProductsBySkus(any()) }
    }

    @Test
    fun updateProduct() {

        // arrange
        val request = ProductRequest("iphone 14", "you will run out of money when you buy me :)", BigDecimal(345), BigDecimal(122))
        val sku = 563
        val mockedResponse =
            ProductResponse(563, "iphone 14", "you will run out of money when you buy me :)", BigDecimal(345), BigDecimal(122))
        every { mockedProductService.updateProduct(request, sku) } returns mockedResponse

        // act & assert
        mockMvc.perform(
            patch("$path/$sku").contentType(produceType)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().is2xxSuccessful).andExpect(content().contentType(produceType))
            .andExpect { context ->
                val response: ProductResponse =
                    objectMapper.readValue(context.response.getContentAsString(StandardCharsets.UTF_8), ProductResponse::class.java)
                assertEquals(response.name, mockedResponse.name)
                assertEquals(response.description, mockedResponse.description)
                assertEquals(response.price, mockedResponse.price)

            }

        verify(exactly = 1) { mockedProductService.updateProduct(request, sku) }
    }


    @Test
    fun `updateProduct returns bad request`() {

        // arrange
        val sku = 563
        val mockedResponse =
            ProductResponse(563, "iphone 14", "you will run out of money when you buy me :)", BigDecimal(345), BigDecimal(122))
        every { mockedProductService.updateProduct(any(), any()) } returns mockedResponse

        // act & assert
        mockMvc.perform(
            patch("$path/$sku").contentType(produceType)
                .content("{}")
        ).andExpect(status().isBadRequest)

        verify(exactly = 0) { mockedProductService.updateProduct(any(), any()) }
    }

    @Test
    fun `updateProduct returns internal server error`() {

        // arrange
        val request = ProductRequest("iphone 14", "you will run out of money when you buy me :)", BigDecimal(345), BigDecimal(122))
        val sku = 563

        every { mockedProductService.updateProduct(any(), any()) } throws RuntimeException("Bom!")

        // act & assert
        mockMvc.perform(
            patch("$path/$sku").contentType(produceType)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().is5xxServerError)

        verify(exactly = 1) { mockedProductService.updateProduct(request, sku) }
    }
}
