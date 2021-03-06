package com.aks.mongodbredisintegration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.aks.mongodbredisintegration.document.Book;
import com.aks.mongodbredisintegration.exception.BookNotFoundException;
import com.aks.mongodbredisintegration.repository.BookRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	private final MongoTemplate mongoTemplate;

	@Override
	@Cacheable(value = "books", key = "#title", unless = "#result == null")
	public Book findBookByTitle(String title) throws BookNotFoundException {
		log.info("Finding Book by Title :{}", title);
		return this.bookRepository.findBookByTitle(title)
				.orElseThrow(() -> new BookNotFoundException(
						"Book with Title " + title + " NotFound!"));
	}

	@Override
	public Book saveBook(Book book) {
		log.info("Saving book :{}", book.toString());
		return this.bookRepository.save(book);
	}

	@Override
	@CachePut(value = "book", key = "#title")
	public Book updateAuthorByTitle(String title, String author) {
		log.info("Updating Book Author by Title :{} with {}", title, author);
		final Query query = new Query(Criteria.where("title").is(title));
		final Update update = new Update().set("author", author);
		return this.mongoTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true).upsert(false), Book.class);
	}

	@Override
	@CacheEvict(value = "book", key = "#title")
	public void deleteBook(String title) throws BookNotFoundException {
		log.info("deleting Books by title :{}", title);
		Book book = findBookByTitle(title);
		this.bookRepository.delete(book);
	}

	@Override
	@CacheEvict(allEntries = true, value = "book")
	public String deleteAllCache() {
		log.info("Deleting Cache");
		return "Deleted Full Cache";
	}

	@Override
	public long count() {
		return this.bookRepository.count();
	}

	@Override
	public void deleteAll() {
		this.bookRepository.deleteAll();
	}

}