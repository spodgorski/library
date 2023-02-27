package com.kata.library.book

import org.springframework.stereotype.Component

interface BookDatabaseRepository {
    fun save(bookEntity: BookEntity): BookEntity
    fun findAllBooks(): List<BookEntity>
    fun deleteAllBooks()
    fun borrowBook(userId: String, bookId: String): BookEntity
    fun getAllBorrowed(userId: String): List<BookEntity>
    fun returnBook(userId: String, bookId: String): BookEntity
}

@Component
class HashMapDatabaseRepository : BookDatabaseRepository {
    override fun save(bookEntity: BookEntity): BookEntity = bookEntity.also { DATABASE[bookEntity.bookId] = bookEntity }

    override fun findAllBooks(): List<BookEntity> = DATABASE.values.toList()
    override fun deleteAllBooks() = DATABASE.clear()


    override fun borrowBook(userId: String, bookId: String): BookEntity =
        DATABASE[bookId]
            ?.also { if (it.userId != null) { throw BorrowBookException(bookId) }}
            ?.also { save(it.copy(userId = userId)) }
            ?: throw BookNotFoundException(bookId)

    override fun returnBook(userId: String, bookId: String): BookEntity = DATABASE[bookId]
        ?.also { if(it.userId != userId) { throw ReturnBookException(bookId) }}
        ?.also { save(it.copy(userId = null)) } ?: throw BookNotFoundException(bookId)

    override fun getAllBorrowed(userId: String): List<BookEntity> {
        return DATABASE.values.filter { it.userId == userId }
    }

    companion object {
        val DATABASE = mutableMapOf<String, BookEntity>()
    }
}
