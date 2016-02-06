package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.BookLoans;

public class BookLoansDAO extends AbstractDAO implements ResultSetExtractor<List<BookLoans>>
{

	@Autowired
	JdbcTemplate template;

	@Autowired
	BookDAO bookDao;

	@Autowired
	BranchDAO branchDao;

	@Autowired
	BorrowerDAO borrowerDao;

	public void create(BookLoans a) throws SQLException
	{
		template.update("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values (?,?,?,CURDATE(),DATE_ADD(CURDATE(), INTERVAL 7 DAY))", new Object[] { a.getBook().getBookId(), a.getBranch().getBranchId(),
				a.getCard().getCardNo()});
	}

	public void update(BookLoans a) throws SQLException
	{
		template.update("update tbl_book_loans set dateIn = ? where bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?", new Object[] { a.getDateIn(), a.getBook().getBookId(),
				a.getBranch().getBranchId(), a.getCard().getCardNo(), a.getDateOut() });
	}

	public void updateDueDate(BookLoans a) throws SQLException
	{
		template.update("update tbl_book_loans set dueDate = ? where bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?", new Object[] { a.getDueDate(), a.getBook().getBookId(),
				a.getBranch().getBranchId(), a.getCard().getCardNo(), a.getDateOut() });
	}
	
	public void updateDateIn(BookLoans a) throws SQLException
	{
		template.update("update tbl_book_loans set dateIn = CURDATE() where bookId = ? AND branchId = ? AND cardNo = ?", new Object[] { a.getBook().getBookId(),a.getBranch().getBranchId(), a.getCard().getCardNo() });
	}

	public void delete(BookLoans a) throws SQLException
	{
		template.update("delete from tbl_book_loans  where bookId = ? AND branchId = ? AND cardNo = ? AND dateOut = ?", new Object[] { a.getBook().getBookId(), a.getBranch().getBranchId(),
				a.getCard().getCardNo(), a.getDateOut() });

	}

	public void deleteByBookId(int bookId) throws SQLException
	{
		template.update("delete from tbl_book_loans where bookId = ?", new Object[] { bookId });
	}

	public void deleteByBranchId(int branchId) throws SQLException
	{
		template.update("delete from tbl_book_loans where branchId = ?", new Object[] { branchId });
	}

	public void deleteByCardNo(int cardNo) throws SQLException
	{
		template.update("delete from tbl_book_loans where cardNo = ?", new Object[] { cardNo });
	}

	public BookLoans readOne(int bookId, int branchId, int cardNo) throws SQLException
	{
		List<BookLoans> list = (List<BookLoans>) template.query("select * from tbl_book_loans bookId = ? AND branchId = ? AND cardNo = ? SORT BY dateOut DESC",
				new Object[] { bookId, branchId, cardNo }, this);

		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	public List<BookLoans> readDue() throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where dueDate = cast(now() as date)", new Object[] {}, this);
	}

	public List<BookLoans> readAll() throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans", new Object[] {}, this);
	}

	public List<BookLoans> readByCardNo(int CardNo) throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where cardNo = ?", new Object[] { CardNo }, this);
	}

	public List<BookLoans> readByBranchId(int branchId) throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where branchId = ?", new Object[] { branchId }, this);
	}

	public List<BookLoans> readByBookId(int BookId) throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where bookId = ?", new Object[] { BookId }, this);
	}

	public List<BookLoans> readByDateCut(Timestamp DateOut) throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where cast(dateOut as Date) = cast(? as Date)", new Object[] { DateOut }, this);
	}

	public List<BookLoans> readByDueDate(Timestamp DueDate) throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where cast(DueDate as Date) = cast(? as Date)", new Object[] { DueDate }, this);
	}

	public List<BookLoans> readByDateIn(Timestamp DateIn) throws SQLException
	{
		return (List<BookLoans>) template.query("select * from tbl_book_loans where cast(dateIn as Date) = cast(? as Date)", new Object[] { DateIn }, this);
	}

	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException
	{
		List<BookLoans> aList = new ArrayList<BookLoans>();
		while (rs.next())
		{
			BookLoans a = new BookLoans();
			a.setBook(bookDao.readOne(rs.getInt("bookId")));
			a.setCard(borrowerDao.readOne(rs.getInt("cardNo")));
			a.setBranch(branchDao.readOne(rs.getInt("branchId")));
			a.setDateOut(rs.getTimestamp("dateOut"));
			a.setDueDate(rs.getTimestamp("dueDate"));
			a.setDateIn(rs.getTimestamp("dateIn"));
			aList.add(a);
		}

		return aList;

	}

	public int getDueTodayCount()
	{
		return template.queryForObject("select count(*) from tbl_book_loans where dueDate = cast(now() as date) AND dateIn IS NULL", new Object[] {}, Integer.class);
	}
	
	public List<BookLoans> getDueToday(int pageNo, int pageSize)
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		
		return template.query("select * from tbl_book_loans where dueDate = cast(now() as date) AND dateIn IS NULL LIMIT ? OFFSET ?", new Object[] { pageSize, PageOffset }, this);
	}
	
	public int getDueAllCount()
	{
		return template.queryForObject("select count(*) from tbl_book_loans where dateIn IS NULL", new Object[] {}, Integer.class);
	}

	public List<BookLoans> getDueAll(int pageNo, int pageSize)
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		
		return template.query("select * from tbl_book_loans where dateIn IS NULL LIMIT ? OFFSET ?", new Object[] { pageSize, PageOffset }, this);
	}

	public int getAllCountByBorId(int borId)
	{
		return template.queryForObject("select count(*) from tbl_book_loans where cardNo = ?", new Object[] {borId}, Integer.class);
	}
	
	public List<BookLoans> getAllByBorId(int borId, int pageNo, int pageSize)
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		return template.query("select * from tbl_book_loans where cardNo = ? LIMIT ? OFFSET ?", new Object[] {borId , pageSize, PageOffset}, this);
	}
	
	public int getDueCountByBorId(int borId)
	{
		return template.queryForObject("select count(*) from tbl_book_loans where dateIn IS NULL AND cardNo = ?", new Object[] {borId}, Integer.class);
	}
	
	public List<BookLoans> getDueByBorId(int borId, int pageNo, int pageSize)
	{
		if (pageNo == 0)
			pageNo++;
		Integer PageOffset = (pageNo - 1) * pageSize;
		return template.query("select * from tbl_book_loans where dateIn IS NULL AND cardNo = ? LIMIT ? OFFSET ?", new Object[] {borId, pageSize, PageOffset}, this);
	}
	
}
