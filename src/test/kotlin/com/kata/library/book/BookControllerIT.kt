package com.kata.library.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var hashMapDatabaseRepository: BookDatabaseRepository

    @Test
    fun shouldCreateBook() {
        // given & when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/book-api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "title": "$DEFAULT_TITLE",
                            "author": "$DEFAULT_AUTHOR"
                        }
                    """.trimIndent()
                )
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val books = hashMapDatabaseRepository.findAllBooks()
        assertNotNull(books)
        assertThat(books).hasSize(1)
        assertEquals(DEFAULT_TITLE, books[0].title)
        assertEquals(DEFAULT_AUTHOR, books[0].author)
        assertNotNull(books[0].bookId)
    }

    companion object {
        const val DEFAULT_TITLE = "title"
        const val DEFAULT_AUTHOR = "author"
    }
}