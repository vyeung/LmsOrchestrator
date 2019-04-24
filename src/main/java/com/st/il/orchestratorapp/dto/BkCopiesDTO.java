package com.st.il.orchestratorapp.dto;

import com.st.il.orchestratorapp.models.Author;
import com.st.il.orchestratorapp.models.Book;
import com.st.il.orchestratorapp.models.BookCopies;

public class BkCopiesDTO {
	
	private BookCopies bookCopies;
	private Book book;
	private Author author;
	
	public BkCopiesDTO(BookCopies bookCopies, Book book, Author author) {
		this.bookCopies = bookCopies;
		this.book = book;
		this.author = author;
	}

	public BookCopies getBookCopies() {
		return bookCopies;
	}

	public void setBookCopies(BookCopies bookCopies) {
		this.bookCopies = bookCopies;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
}