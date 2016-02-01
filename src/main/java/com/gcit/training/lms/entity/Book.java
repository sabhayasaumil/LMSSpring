package com.gcit.training.lms.entity;

import java.util.ArrayList;
import java.util.List;

public class Book {
	
	private int bookId;
	private String title;
	private Publisher publisher;
	private List<Author> authors;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public void addAuthor(Author a) {
		if(this.authors == null)
			this.authors = new ArrayList<Author>();
		this.authors.add(a);
	}
	
}
