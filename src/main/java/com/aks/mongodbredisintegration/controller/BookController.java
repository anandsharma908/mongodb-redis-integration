package com.aks.mongodbredisintegration.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aks.mongodbredisintegration.document.Book;
import com.aks.mongodbredisintegration.exception.BookNotFoundException;
import com.aks.mongodbredisintegration.service.BookService;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping("/findByTitle/{title}")
	public Book findBookByTitle(@NotNull @PathVariable String title)
			throws BookNotFoundException {
		return this.bookService.findBookByTitle(title);
	}

	@PostMapping("/saveBook")
	public Book saveBook(@Valid @RequestBody Book book) {
		return this.bookService.saveBook(book);
	}

	@PutMapping("/updateByTitle/{title}/{author}")
	public Book updateAuthorByTitle(@PathVariable("title") String title,
			@PathVariable("author") String author) {
		return this.bookService.updateAuthorByTitle(title, author);
	}

	@DeleteMapping("/deleteByTitle/{title}")
	public ResponseEntity<String> deleteBookByTitle(@PathVariable("title") String title)
			throws BookNotFoundException {
		this.bookService.deleteBook(title);
		return ResponseEntity.accepted().body("Book with title " + title + " deleted");
	}

	/**
	 * Deletes all cache.
	 * @return string
	 */
	@GetMapping("/deleteCache")
	public String deleteCache() {
		return this.bookService.deleteAllCache();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private void bookNotFoundHandler(BookNotFoundException ex) {
		log.error("Entering and leaving BookController : bookNotFoundHandler ",
				ex.getMsg());
	}

	public long count() {
		return this.bookService.count();
	}

	public void deleteAll() {
		this.bookService.deleteAll();
	}

}