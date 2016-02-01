package com.gcit.training.lms.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.Publisher;

public class PublisherDAO extends AbstractDAO implements ResultSetExtractor<List<Publisher>>{

	@Autowired
	JdbcTemplate template;

	public void create(Publisher a) throws SQLException {
		template.update("insert into tbl_publisher (publisherName, publisherAddress) values (?, ?)",new Object[]{a.getPublisherName()});
	}

	public void update(Publisher a) throws SQLException {
		template.update("update tbl_publisher set publisherName = ? where publisherId = ? " , new Object[]{a.getPublisherName(), a.getPublisherId()});
	}

	public void delete(Publisher a) throws SQLException {
		template.update("delete from tbl_publisher where publishId = ?", new Object[]{a.getPublisherId()});
	}

	public Publisher readOne(int publisherId) throws SQLException {
			List<Publisher> list = (List<Publisher>)template.query("select * from tbl_publisher where publisherId = ?",new Object[]{publisherId},this);
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
	}

	public List<Publisher> readAll() throws SQLException {

		return (List<Publisher>)template.query("select * from tbl_publisher",new Object[]{},this);
	}
	
	public List<Publisher> readByName(String searchString) throws SQLException {
		String qString = "%" + searchString + "%";
		return (List<Publisher>)template.query("select * from tbl_publisher where publisherName like ?", new Object[]{qString},this);	
	}

	public List<Publisher> readByBook(Book book) throws SQLException {
		return (List<Publisher>)template.query("select * from tbl_publisher where publisherId in (select pubId from tbl_book where bookId = ?)", new Object[]{book.getBookId()},this);	
	}
	
	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> aList = new ArrayList<Publisher>();
		while(rs.next()) {
			Publisher a = new Publisher();
			a.setPublisherId(rs.getInt("publisherId"));
			a.setPublisherName(rs.getString("publisherName"));
			
			aList.add(a);
		}
		return aList;
	}
}
