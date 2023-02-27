package com.kata.library.book

import org.springframework.stereotype.Service

@Service
class BookBorrowService(val hashMapDatabaseRepository: BookDatabaseRepository) {

    fun borrow(userId: String, bookId: String): BookDto = hashMapDatabaseRepository.borrowBook(userId, bookId).toDto()
    fun returnBook(userId: String, bookId: String): BookDto = hashMapDatabaseRepository.returnBook(userId, bookId).toDto()
    fun getAllBorrowed(userId: String): List<BookDto> {
        return hashMapDatabaseRepository.getAllBorrowed(userId).map { it.toDto() }
    }
}
