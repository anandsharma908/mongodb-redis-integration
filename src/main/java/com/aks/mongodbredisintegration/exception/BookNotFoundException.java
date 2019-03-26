package com.aks.mongodbredisintegration.exception;

public class BookNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String msg;

	public BookNotFoundException(String message) {
		super(message);
		this.msg = message;
	}

	public String getMsg() {
		return this.msg;
	}

}