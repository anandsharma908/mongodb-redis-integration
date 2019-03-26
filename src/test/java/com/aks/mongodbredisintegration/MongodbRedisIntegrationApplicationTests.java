package com.aks.mongodbredisintegration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aks.mongodbredisintegration.document.Book;

/**
 * Integration test for simple App.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MongodbRedisIntegrationApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getBookByTitle_returnsBookDetails() throws Exception {
		// arrange
		Book book = Book.builder().title("MongoDbCookBook").author("Raja").build();
		ResponseEntity<Book> response = this.restTemplate.postForEntity("/book/saveBook",
				book, Book.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");

		// act
		response = this.restTemplate.getForEntity("/book/findByTitle/MongoDbCookBook",
				Book.class);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getBody().getAuthor()).isEqualTo("Raja");

		// act by Update
		response = this.restTemplate.exchange("/book/updateByTitle/MongoDbCookBook/Raja1",
				HttpMethod.PUT, null, Book.class);

		// assert After Update
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getBody().getAuthor()).isEqualTo("Raja1");

		// act by Delete
		ResponseEntity<String> resp = this.restTemplate.exchange(
				"/book/deleteByTitle/MongoDbCookBook", HttpMethod.DELETE, null,
				String.class);
		assertThat(resp).isNotNull();
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(resp.getBody()).isEqualTo("Book with title MongoDbCookBook deleted");

		response = this.restTemplate.getForEntity("/book/findByTitle/MongoDbCookBook",
				Book.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		resp = this.restTemplate.getForEntity("/book/deleteCache", String.class);
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resp.getBody()).isEqualTo("Deleted Full Cache");
	}

}