package com.kata.library.book

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-api")
class BookController(val bookService: BookService) {

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody bookDto: BookDto): BookDto = bookService.add(bookDto)
}



