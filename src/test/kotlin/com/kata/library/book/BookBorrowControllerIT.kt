package com.kata.library.book

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID
import kotlin.test.assertNull

@SpringBootTest
@AutoConfigureMockMvc
class BookBorrowControllerIT {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var hashMapDatabaseRepository: BookDatabaseRepository

    @BeforeEach
    fun setup() {
        hashMapDatabaseRepository.deleteAllBooks()
    }

    @Test
    fun shouldBorrow() {
        // given
        val userId = "123"
        val bookEntity = BookEntity(
            DEFAULT_TITLE,
            DEFAULT_AUTHOR,
            UUID.randomUUID().toString(),
            userId = null
        )

        hashMapDatabaseRepository.save(bookEntity)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/book-borrow-api/users/$userId/books/${bookEntity.bookId}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val books = hashMapDatabaseRepository.findAllBooks()
        assertNotNull(books)
        Assertions.assertThat(books).hasSize(1)
        assertEquals(DEFAULT_TITLE, books[0].title)
        assertEquals(DEFAULT_AUTHOR, books[0].author)
        assertEquals(userId, books[0].userId)
        assertNotNull(books[0].bookId)
    }

    @Test
    fun shouldReturnExceptionWhenTryingBorrowBorrowedBookByOtherUser() {
        // given
        val userId = "123"
        val bookEntity = BookEntity(
            DEFAULT_TITLE,
            DEFAULT_AUTHOR,
            UUID.randomUUID().toString(),
            userId = null
        )

        hashMapDatabaseRepository.save(bookEntity)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/book-borrow-api/users/$userId/books/${bookEntity.bookId}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/book-borrow-api/users/otherUser/books/${bookEntity.bookId}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun shouldReturnExceptionWhenTryingBorrowNotExistingBook() {
        // given when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/book-borrow-api/users/userId/books/bookId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun shouldGetAllBorrowedBooksBySpecifiedUser() {
        // given
        val userId = "123"
        val firstBookEntity = BookEntity(
            DEFAULT_TITLE,
            DEFAULT_AUTHOR,
            UUID.randomUUID().toString(),
            userId = userId
        )

        val secondBookEntity = BookEntity(
            DEFAULT_TITLE,
            DEFAULT_AUTHOR,
            UUID.randomUUID().toString(),
            userId = userId
        )

        hashMapDatabaseRepository.save(firstBookEntity)
        hashMapDatabaseRepository.save(secondBookEntity)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/book-borrow-api/users/$userId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(DEFAULT_TITLE))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(DEFAULT_AUTHOR))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(DEFAULT_TITLE))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(DEFAULT_AUTHOR))
    }

    @Test
    fun shouldReturnBorrowedBook() {
        // given
        val userId = "123"
        val bookEntity = BookEntity(
            DEFAULT_TITLE,
            DEFAULT_AUTHOR,
            UUID.randomUUID().toString(),
            userId = userId
        )

        hashMapDatabaseRepository.save(bookEntity)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/book-borrow-api/users/$userId/books/${bookEntity.bookId}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)

        assertThat(hashMapDatabaseRepository.findAllBooks()).hasSize(1)
        assertNull(hashMapDatabaseRepository.findAllBooks()[0].userId)
    }

    @Test
    fun shouldThrowExceptionWhenReturnNotExistingBook() {
        // given
        val userId = "123"

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/book-borrow-api/users/$userId/books/bookId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun shouldThrowExceptionWhenReturnBorrowedBookByOtherUser() {
        // given
        val userId = "123"
        val bookEntity = BookEntity(
            DEFAULT_TITLE,
            DEFAULT_AUTHOR,
            UUID.randomUUID().toString(),
            userId = "otherUser"
        )

        hashMapDatabaseRepository.save(bookEntity)

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/book-borrow-api/users/$userId/books/${bookEntity.bookId}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest)

        assertThat(hashMapDatabaseRepository.findAllBooks()).hasSize(1)
        assertEquals(hashMapDatabaseRepository.findAllBooks()[0].userId, "otherUser")
    }



    companion object {
        const val DEFAULT_TITLE = "title"
        const val DEFAULT_AUTHOR = "author"
    }
}