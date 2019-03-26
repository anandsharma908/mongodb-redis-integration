package com.aks.mongodbredisintegration.repository;

import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.aks.mongodbredisintegration.document.Book;


public interface BookRepository extends MongoRepository<Book, String> {

	Optional<Book> findBookByTitle(String titleName);

}