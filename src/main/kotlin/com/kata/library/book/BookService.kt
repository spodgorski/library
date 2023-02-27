package com.kata.library.book

import org.springframework.stereotype.Service

@Service
class BookService(val hashMapDatabaseRepository: BookDatabaseRepository) {

    fun add(bookDto: BookDto): BookDto = bookDto
        .validate()
        .toEntity()
        .also { hashMapDatabaseRepository.save(it) }
        .toDto()
}
