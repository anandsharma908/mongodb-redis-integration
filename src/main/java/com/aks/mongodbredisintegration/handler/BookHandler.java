package com.aks.mongodbredisintegration.handler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.aks.mongodbredisintegration.document.Book;
import com.aks.mongodbredisintegration.repository.ReactiveBookRepository;

/**
 * Handler which handles Routes.
 *
 * @author Raja Kolli
 * @since 0.2.1
 */
@Component
public class BookHandler {

	private final ReactiveBookRepository bookReactiveRepository;

	/**
	 * <p>
	 * Constructor for BookHandler.
	 * </p>
	 * @param repository a
	 * {@link com.ReactiveBookRepository.mongodbredisintegration.repository.BookReactiveRepository}
	 * object.
	 */
	@Autowired
	public BookHandler(ReactiveBookRepository repository) {
		this.bookReactiveRepository = repository;
	}

	/**
	 * GET ALL Books.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> getAll(ServerRequest request) {
		// fetch all books from repository
		Flux<Book> books = this.bookReactiveRepository.findAll();

		// build response
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(books,
				Book.class);
	}

	/**
	 * GET a Book by ID.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> getBook(ServerRequest request) {
		// parse path-variable
		String bookId = request.pathVariable("id");

		// build notFound response
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get book from repository
		Mono<Book> bookMono = this.bookReactiveRepository.findById(bookId);

		// build response
		return bookMono.flatMap(
				(Book book) -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(book)))
				.switchIfEmpty(notFound);
	}

	/**
	 * POST a Book.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> postBook(ServerRequest request) {
		Mono<Book> monoBook = request.bodyToMono(Book.class);
		return ServerResponse.ok()
				.build(monoBook.doOnNext(this.bookReactiveRepository::save).then());
	}

	/**
	 * PUT a Book.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> putBook(ServerRequest request) {
		// parse id from path-variable
		String bookId = request.pathVariable("id");

		// get book data from request object
		Mono<Book> monoBook = request.bodyToMono(Book.class);
		monoBook.doOnNext((Book b) -> b.setBookId(bookId)).then();

		// get book from repository
		Mono<Book> responseMono = monoBook.doOnNext(this.bookReactiveRepository::save);

		// build response
		return responseMono.flatMap(
				(Book book) -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromObject(book)));
	}

	/**
	 * DELETE a Book.
	 * @param request a ServerRequest object
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> deleteBook(ServerRequest request) {
		// parse id from path-variable
		String bookId = request.pathVariable("id");

		this.bookReactiveRepository.deleteById(bookId);
		// get book from repository
		Mono<String> responseMono = Mono.just("Delete Succesfully!");

		// build response
		return responseMono.flatMap((String strMono) -> ServerResponse.accepted()
				.contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject(strMono)));

	}

}
