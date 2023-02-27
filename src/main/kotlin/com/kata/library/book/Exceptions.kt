package com.kata.library.book

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    private val logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

    @ExceptionHandler(LibraryException::class)
    fun handleLibraryException(ex: LibraryException): ResponseEntity<Error> {
        logger.warn(ex.message)
        return ResponseEntity(
            Error(ex.message ?: "Not specified client error."),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFoundException(ex: BookNotFoundException): ResponseEntity<Error> {
        logger.warn(ex.message)
        return ResponseEntity(
            Error(ex.message ?: "Book with id ${ex.bookId} not found"),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(BorrowBookException::class)
    fun handleBookBorrowException(ex: BorrowBookException): ResponseEntity<Error> {
        logger.warn(ex.message)
        return ResponseEntity(
            Error(ex.message ?: "Book with id ${ex.bookId} is already borrowed!"),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(ReturnBookException::class)
    fun handleReturnBookException(ex: ReturnBookException): ResponseEntity<Error> {
        logger.warn(ex.message)
        return ResponseEntity(
            Error(ex.message ?: "Return book with id ${ex.bookId} exception!"),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(InvalidDtoException::class)
    fun handleInvalidDtoException(ex: InvalidDtoException): ResponseEntity<Error> {
        logger.warn(ex.message)
        return ResponseEntity(
            Error(ex.message ?: "Invalid dto provided!"),
            HttpStatus.BAD_REQUEST
        )
    }
}

class BookNotFoundException(val bookId: String) : LibraryException("Book with id $bookId not found!")
class BorrowBookException(val bookId: String) : LibraryException("Book with id $bookId is already borrowed!")
class ReturnBookException(val bookId: String) : LibraryException("Could not return book with id $bookId!")
class InvalidDtoException : LibraryException("Invalid dto provided!")

open class LibraryException(message: String) : RuntimeException(message)

data class Error(
    val message: String
)
