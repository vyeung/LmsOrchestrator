package com.st.orchestrator.dto;

public class BkAuthPubDTO {

	private int bookId;
	private String bookTitle;
	private String authorName;
	private String publisherName;
	
	public BkAuthPubDTO() {}
	
	public BkAuthPubDTO(int bookId, String bookTitle, String authorName, String publisherName) {
		this.bookId = bookId;
		this.bookTitle = bookTitle;
		this.authorName = authorName;
		this.publisherName = publisherName;
	}

	public int getBookId() {
		return bookId;
	}
	
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	public String getBookTitle() {
		return bookTitle;
	}
	
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public String getPublisherName() {
		return publisherName;
	}
	
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}	
}	