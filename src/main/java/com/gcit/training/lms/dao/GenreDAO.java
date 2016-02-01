package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.Genre;

public class GenreDAO extends AbstractDAO implements ResultSetExtractor<List<Genre>>
{

	@Autowired
	JdbcTemplate template;

	public void create(Genre a) throws SQLException
	{
		template.update("insert into tbl_genre (genre_name) values (?)", new Object[] { a.getGenreName() });
	}

	public void update(Genre a) throws SQLException
	{
		template.update("update tbl_genre set genre_name = ? where genre_id = ?", new Object[] { a.getGenreName(), a.getGenreId() });
	}

	public void delete(Genre a) throws SQLException
	{
		template.update("delete from tbl_genre where genre_id = ?", new Object[] { a.getGenreId() });
	}

	public Genre readOne(int genreId) throws SQLException
	{

		List<Genre> list = (List<Genre>) template.query("select * from tbl_genre where genre_id = ?", new Object[] { genreId }, this);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	public List<Genre> readAll() throws SQLException
	{
		return (List<Genre>) template.query("select * from tbl_genre", new Object[] {}, this);
	}

	public List<Genre> readAll(int PageNo, int PageSize) throws SQLException
	{

		Integer PageOffset = (PageNo - 1) * PageSize;
		return (List<Genre>) template.query("select * from tbl_genre LIMIT ? OFFSET ?", new Object[] { PageSize, PageOffset }, this);
	}

	public List<Genre> readByName(String searchString, int PageNo, int PageSize) throws SQLException
	{
		Integer PageOffset = (PageNo - 1) * PageSize;
		String qString = "%" + searchString + "%";
		return (List<Genre>) template.query("select * from tbl_genre where genre_name like ? LIMIT ? OFFSET ?", new Object[] { qString, PageSize, PageOffset }, this);

	}

	public List<Genre> readByBook(Book book) throws SQLException
	{
		return (List<Genre>) template.query("select * from tbl_genre Where genre_id in (select genre_id from tbl_book_genres where bookId = ?)", new Object[] { book.getBookId() }, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException
	{
		List<Genre> aList = new ArrayList<Genre>();
		while (rs.next())
		{
			Genre a = new Genre();
			a.setGenreId(rs.getInt("genre_id"));
			a.setGenreName(rs.getString("genre_name"));

			aList.add(a);
		}

		return aList;
	}

	public int readAllCount()
	{
		return template.queryForObject("Select count(*) from tbl_genre", new Object[] {}, Integer.class);
	}

	public int readByNameCount(String searchString)
	{
		searchString = "%" + searchString + "%";
		return template.queryForObject("SELECT count(*) from tbl_genre where genre_name Like ?", new Object[] { searchString }, Integer.class);

	}

}
