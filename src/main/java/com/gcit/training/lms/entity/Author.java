package com.gcit.training.lms.entity;

import java.util.List;

//tbl_author table
public class Author {
	
	private int authorId;
	private String authorName;
	private List<Book> Books;
	
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public List<Book> getBooks() {
		return Books;
	}
	public void setBooks(List<Book> books) {
		Books = books;
	}

}
