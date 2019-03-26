package com.aks.mongodbredisintegration.reactiveservice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

import com.aks.mongodbredisintegration.document.Book;


public interface ReactiveBookService {

	Mono<Book> findByTitle(String title);

	Mono<ResponseEntity<Book>> updateBook(String bookId, Book book);

	Flux<Book> findAllBooks();

	Mono<ResponseEntity<Book>> getBookById(String bookId);

	Mono<Book> createBook(Book book);

	Mono<ResponseEntity<Void>> deleteBook(String bookId);

}