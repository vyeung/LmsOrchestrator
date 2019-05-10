package com.st.il.orchestratorapp.dto;

import java.sql.Date;

import com.st.il.orchestratorapp.models.Book;
import com.st.il.orchestratorapp.models.Borrower;
import com.st.il.orchestratorapp.models.LibraryBranch;

public class BookLoansDTO {
	
	private Book books;
	private LibraryBranch libBranch;
	private Borrower borrower;
	private Date dateOut;
	private Date dueDate;
	
	public BookLoansDTO(Book books, LibraryBranch libBranch, Borrower borrower, Date dateOut, Date dueDate) {
		this.books = books;
		this.libBranch = libBranch;
		this.borrower = borrower;
		this.dateOut = dateOut;
		this.dueDate = dueDate;
	}


	public Book getBooks() {
		return books;
	}

	public void setBooks(Book books) {
		this.books = books;
	}

	public LibraryBranch getLibBranch() {
		return libBranch;
	}

	public void setLibBranch(LibraryBranch libBranch) {
		this.libBranch = libBranch;
	}

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}