package com.kata.library.book

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-borrow-api")
class BookBorrowController(val bookBorrowService: BookBorrowService) {

    @PostMapping("/users/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun borrow(@PathVariable userId: String, @PathVariable bookId: String): BookDto = bookBorrowService.borrow(userId, bookId)

    @DeleteMapping("/users/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    fun returnBook(@PathVariable userId: String, @PathVariable bookId: String): BookDto = bookBorrowService.returnBook(userId, bookId)

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAllBorrowed(@PathVariable userId: String): List<BookDto> = bookBorrowService.getAllBorrowed(userId)
}

