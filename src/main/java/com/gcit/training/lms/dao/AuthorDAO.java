package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;

public class AuthorDAO extends AbstractDAO implements ResultSetExtractor<List<Author>>
{

	@Autowired
	JdbcTemplate template;

	@Autowired
	BookDAO bookDao;

	public void create(Author a) throws SQLException
	{
		template.update("insert into tbl_author (authorName) values (?)", new Object[] { a.getAuthorName() });
	}

	public void update(Author a) throws SQLException
	{
		template.update("update tbl_author set authorName = ? where authorId = ?", new Object[] { a.getAuthorName(), a.getAuthorId() });
	}

	public void delete(Author a) throws SQLException
	{
		template.update("delete from tbl_author where authorId = ?", new Object[] { a.getAuthorId() });
	}

	public Author readOne(int authorId) throws SQLException
	{
		List<Author> list = (List<Author>) template.query("select * from tbl_author where authorId = ?", new Object[] { authorId }, this);

		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	public List<Author> readAll(int PageNo, int PageSize) throws SQLException
	{

		if (PageNo == 0)
			PageNo++;
		Integer PageOffset = (PageNo - 1) * PageSize;
		return (List<Author>) template.query("select * from tbl_author LIMIT ? OFFSET ?", new Object[] { PageSize, PageOffset }, this);
	}

	public List<Author> readByName(String searchString, int PageNo, int PageSize) throws SQLException
	{
		String qString = "%" + searchString + "%";

		Integer PageOffset = (PageNo - 1) * PageSize;
		return (List<Author>) template.query("select * from tbl_author where authorName like ? LIMIT ? OFFSET ?", new Object[] { qString, PageSize, PageOffset }, this);
	}
	
	public List<Author> readByBook(Book book) throws SQLException
	{
		return (List<Author>) template.query("select * from tbl_author Where authorId in (select authorId from tbl_book_authors where bookId = ?)", new Object[] {book.getBookId()}, this);
	}

	public int readByNameCount(String searchString)
	{
		searchString = "%" + searchString + "%";
		return template.queryForObject("SELECT count(*) from tbl_author where authorName Like ?", new Object[] { searchString }, Integer.class);

	}

	public Integer readAllCount()
	{
		return template.queryForObject("SELECT count(*) from tbl_author", new Object[] {}, Integer.class);
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException
	{
		List<Author> aList = new ArrayList<Author>();
		while (rs.next())
		{
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			aList.add(a);
		}

		return aList;
	}
}
