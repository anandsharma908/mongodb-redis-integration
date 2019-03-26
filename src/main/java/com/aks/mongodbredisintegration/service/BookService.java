package com.aks.mongodbredisintegration.service;


import com.aks.mongodbredisintegration.document.Book;
import com.aks.mongodbredisintegration.exception.BookNotFoundException;


public interface BookService {

	Book findBookByTitle(String titleName) throws BookNotFoundException;

	Book saveBook(Book book);

	Book updateAuthorByTitle(String title, String author);

	void deleteBook(String title) throws BookNotFoundException;

	String deleteAllCache();

	long count();

	void deleteAll();

}