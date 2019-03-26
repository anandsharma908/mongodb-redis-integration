package com.aks.mongodbredisintegration.repository;

import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.aks.mongodbredisintegration.document.Book;


public interface ReactiveBookRepository extends ReactiveMongoRepository<Book, String> {

	Mono<Book> findByTitle(String bookTitle);

}