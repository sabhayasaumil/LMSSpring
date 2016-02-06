package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.StringUtils;

import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.Branch;
import com.gcit.training.lms.entity.Copies;

public class CopiesDAO extends AbstractDAO implements ResultSetExtractor<List<Copies>>
{
	@Autowired
	JdbcTemplate template;
	
	@Autowired
	BookDAO bookDao;
	
	@Autowired
	BranchDAO branchDao;

	public void create(Copies a) throws SQLException
	{
		template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ? , ?)", new Object[] { a.getBook().getBookId(), a.getBranch().getBranchId(), a.getNoOfCopies() });
	}

	public void create(int bookId, int branchId, int copies)
	{
		template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ? , ?)", new Object[] { bookId,branchId, copies});// TODO Auto-generated method stub
		
	}
	
	public void update(Copies a) throws SQLException
	{
		template.update("update tbl_book_copies set noOfCopies = ? where bookId = ? AND branchId = ?", new Object[] { a.getNoOfCopies(), a.getBook().getBookId(), a.getBranch().getBranchAddress() });
	}

	public void updateCopiesInc(int bookId, int branchId, int change)
	{
		template.update("update tbl_book_copies set noOfCopies = noOfCopies + ? where bookId = ? AND branchId = ?", new Object[]{change, bookId, branchId});
	}
	
	
	public void delete(Copies a) throws SQLException
	{

		template.update("delete from tbl_book_copies where bookId = ? AND branchId = ?", new Object[] { a.getBook().getBookId(), a.getBranch().getBranchAddress() });

	}

	public Copies readOne(int bookId, int branchId) throws SQLException
	{
		List<Copies> list = (List<Copies>) template.query("select * from tbl_book_copies bookId = ? AND branchId = ?", new Object[] { bookId, branchId },this);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}

	}

	public List<Copies> readAll(int pageNo, int pageSize) throws SQLException
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		return (List<Copies>) template.query("select * from tbl_book_copies LIMIT ? OFFSET ?", new Object[] {pageSize, PageOffset }, this);
	}

	public List<Copies> readByBook(int bookId, int pageNo, int pageSize) throws SQLException
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		return (List<Copies>) template.query("select * from tbl_book_copies where bookId = ?  LIMIT ? OFFSET ?", new Object[] { bookId, pageSize, PageOffset }, this);
	}

	public List<Copies> readByBranch(int branchId, String searchString, int pageNo, int pageSize) throws SQLException
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		
		if(!StringUtils.hasLength(searchString))
			return (List<Copies>) template.query("select * from tbl_book_copies where branchId = ?  LIMIT ? OFFSET ?", new Object[] { branchId, pageSize, PageOffset }, this);
	
		searchString = "%" + searchString + "%";
		return (List<Copies>) template.query("select * from tbl_book_copies where bookId in (select bookId from tbl_book where title like ?) AND branchId = ?  LIMIT ? OFFSET ?", new Object[] { searchString, branchId, pageSize, PageOffset }, this);
	}

	@Override
	public List<Copies> extractData(ResultSet rs) throws SQLException
	{
		List<Copies> aList = new ArrayList<Copies>();
		while (rs.next())
		{
			Copies a = new Copies();
			a.setBook(bookDao.readOne(rs.getInt("bookId")));
			a.setBranch(branchDao.readOne(rs.getInt("branchId")));
			a.setNoOfCopies(rs.getInt("noOfCopies"));
			aList.add(a);
		}

		return aList;
	}

	public int readTotalCopiesByBookId(int bookId)
	{
		
		Integer count = template.queryForObject("select SUM(noOfCopies) from tbl_book_copies where bookId = ?", new Object[] { bookId }, Integer.class);
		if(count==null)
			return 0;
		else 
			return count;
	}

	public void deleteBooks(Book book)
	{
		template.update("delete from tbl_book_copies where bookId = ?", new Object[] {book.getBookId()});
	}

	public void deleteByBranchId(Branch branch)
	{
		template.update("delete from tbl_book_copies where branchId = ?", new Object[] {branch.getBranchId()});
		
	}

	public int getBookByBranchIdCount(int branchId, String searchString)
	{
		if(!StringUtils.hasLength(searchString))
			return template.queryForObject("select count(*) from tbl_book_copies where branchId = ?",new Object[]{branchId} ,Integer.class); 
		
		searchString = "%" + searchString + "%";
		return template.queryForObject("select count(*) from tbl_book_copies where bookId in (select bookId from tbl_book where title like ?) AND branchId = ?",new Object[]{searchString, branchId} ,Integer.class);
	}

	
}
