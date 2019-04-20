package com.st.orchestrator.dto;

import com.st.orchestrator.models.Author;
import com.st.orchestrator.models.Book;
import com.st.orchestrator.models.BookLoans;

public class BkLoansBkAuthDTO {

	private BookLoans bookLoans;
	private Book book;
	private Author author;
	
	public BkLoansBkAuthDTO(BookLoans bookLoans, Book book, Author author) {
		this.bookLoans = bookLoans;
		this.book = book;
		this.author = author;
	}

	public BookLoans getBookLoans() {
		return bookLoans;
	}

	public void setBookLoans(BookLoans bookLoans) {
		this.bookLoans = bookLoans;
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