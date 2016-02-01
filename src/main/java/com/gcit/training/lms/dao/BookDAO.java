package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.Genre;
import com.gcit.training.lms.entity.Publisher;

public class BookDAO extends AbstractDAO implements ResultSetExtractor<List<Book>>
{

	@Autowired
	JdbcTemplate template;

	@Autowired
	AuthorDAO authorDao;

	@Autowired
	PublisherDAO publisherDao;

	public int create(Book a) throws SQLException
	{

		KeyHolder keyHolder = new GeneratedKeyHolder();
		Integer pubId = a.getPublisher().getPublisherId();

		
		
		template.update("insert into tbl_book (title, pubId) values (?,?) ", new Object[] { a.getTitle(), pubId });
		
		return template.queryForObject("select LAST_INSERT_ID();", new Object[]{}, Integer.class);

	}

	public void update(Book a) throws SQLException
	{

		template.update("update tbl_book set title = ? where bookId = ?", new Object[] { a.getTitle(), a.getBookId() });
	}

	public void insertAuthor(Book book, int authorId)
	{
		template.update("insert into tbl_book_authors (authorId,bookId) values ((select authorId from tbl_author where authorId = ?),(select bookId from tbl_book where bookId = ?))", new Object[] { authorId, book.getBookId() });
	}

	public void insertGenre(Book book, int genre_id)
	{
		template.update("insert into tbl_book_genres (genre_id,bookId) values (?,?)", new Object[] { genre_id, book.getBookId() });
	}

	public void delete(Book a) throws SQLException
	{

		template.update("delete from tbl_book where bookId = ?", new Object[] { a.getBookId() });

	}

	public Book readOne(int bookId) throws SQLException
	{

		List<Book> list = (List<Book>) template.query("select * from tbl_book where bookId = ?", new Object[] { bookId }, this);

		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}

	}

	public List<Book> readAll(int pageNo, int pageSize) throws SQLException
	{

		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;

		return (List<Book>) template.query("select * from tbl_book  LIMIT ? OFFSET ?", new Object[] { pageSize, PageOffset }, this);
	}

	public List<Book> readByTitle(int pageNo, int pageSize, String searchString) throws SQLException
	{

		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		String qString = "%" + searchString + "%";
		return (List<Book>) template.query("select * from tbl_book where title like ?  LIMIT ? OFFSET ?", new Object[] { qString, pageSize, PageOffset }, this);

	}

	public List<Book> readByAuthor(Author author) throws SQLException
	{
		return (List<Book>) template.query("select * from tbl_book Where bookId in (select bookId from tbl_book_authors where authorId = ?)", new Object[] { author.getAuthorId() }, this);
	}

	public List<Book> readByGenre(Genre genre) throws SQLException
	{
		return (List<Book>) template.query("select * from tbl_book Where bookId in (select bookId from tbl_book_genres where genre_id = ?)", new Object[] { genre.getGenreId() }, this);
	}

	public List<Book> readByPublisher(Publisher publisher) throws SQLException
	{
		return (List<Book>) template.query("select * from tbl_book where pubId = ?", new Object[] { publisher.getPublisherId() }, this);
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException
	{
		List<Book> aList = new ArrayList<Book>();
		while (rs.next())
		{
			Book a = new Book();
			a.setBookId(rs.getInt("bookId"));
			a.setTitle(rs.getString("title"));
			a.setPublisher(publisherDao.readOne(rs.getInt("pubId")));
			aList.add(a);
		}

		return aList;
	}

	public int getCountAll()
	{
		return template.queryForObject("select count(*) from tbl_book", new Object[] {}, Integer.class);
	}

	public int getCountByName(String searchString)
	{
		searchString = "%" + searchString + "%";
		return template.queryForObject("select count(*) from tbl_book where title like ?", new Object[] { searchString }, Integer.class);
	}
}
