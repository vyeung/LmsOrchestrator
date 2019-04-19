package com.st.orchestrator.models;

import java.io.Serializable;

@SuppressWarnings({"unused", "serial"})
public class BookCopiesPrimaryKey implements Serializable {
	private Integer bookId;
	private Integer branchId;
	
	public BookCopiesPrimaryKey() {}
	
	public BookCopiesPrimaryKey(Integer bookId, Integer branchId) {
		this.bookId = bookId;
		this.branchId = branchId;
	}
}