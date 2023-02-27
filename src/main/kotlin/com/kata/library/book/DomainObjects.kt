package com.kata.library.book

import java.util.UUID

data class BookDto(
    val title: String,
    val author: String,
    val bookId: String?
)

data class BookEntity(
    val title: String,
    val author: String,
    val bookId: String,
    val userId: String?,
)

fun BookDto.toEntity() = BookEntity(
    this.title,
    this.author,
    bookId = this.bookId ?: UUID.randomUUID().toString(),
    userId = null
)

fun BookDto.validate() =
    this.also { if (this.title.isEmpty() || this.author.isEmpty()) throw InvalidDtoException() }

fun BookEntity.toDto() = BookDto(this.title, this.author, this.bookId)
