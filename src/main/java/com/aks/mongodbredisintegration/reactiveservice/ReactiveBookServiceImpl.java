package com.aks.mongodbredisintegration.reactiveservice;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aks.mongodbredisintegration.document.Book;
import com.aks.mongodbredisintegration.repository.ReactiveBookRepository;

@Service
@RequiredArgsConstructor
public class ReactiveBookServiceImpl implements ReactiveBookService {

	private final ReactiveBookRepository reactiveRepository;

	@Override
	public Mono<Book> findByTitle(String title) {
		return this.reactiveRepository.findByTitle(title);
	}

	@Override
	public Mono<ResponseEntity<Book>> updateBook(String bookId, Book book) {
		return this.reactiveRepository.findById(bookId).flatMap((Book existingBook) -> {
			existingBook.setText(book.getText());
			return this.reactiveRepository.save(existingBook);
		}).map((Book updatedBook) -> new ResponseEntity<>(updatedBook, HttpStatus.OK))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	public Flux<Book> findAllBooks() {
		return this.reactiveRepository.findAll();
	}

	@Override
	public Mono<ResponseEntity<Book>> getBookById(String bookId) {
		return this.reactiveRepository.findById(bookId).map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	public Mono<Book> createBook(Book book) {
		return this.reactiveRepository.save(book);
	}

	@Override
	public Mono<ResponseEntity<Void>> deleteBook(String bookId) {
		return this.reactiveRepository.findById(bookId)
				.flatMap((Book existingBook) -> this.reactiveRepository
						.delete(existingBook)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}