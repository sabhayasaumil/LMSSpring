package com.gcit.training.lms.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.gcit.training.lms.dao.AuthorDAO;
import com.gcit.training.lms.dao.BookDAO;
import com.gcit.training.lms.dao.BookLoansDAO;
import com.gcit.training.lms.dao.BorrowerDAO;
import com.gcit.training.lms.dao.BranchDAO;
import com.gcit.training.lms.dao.CopiesDAO;
import com.gcit.training.lms.dao.GenreDAO;
import com.gcit.training.lms.dao.PublisherDAO;
import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.BookLoans;
import com.gcit.training.lms.entity.Borrower;
import com.gcit.training.lms.entity.Branch;
import com.gcit.training.lms.entity.Copies;
import com.gcit.training.lms.entity.Genre;
import com.gcit.training.lms.entity.Publisher;

public class AdministrativeService
{

	@Autowired
	AuthorDAO adao;

	@Autowired
	BookDAO bookDao;

	@Autowired
	BookLoansDAO bookLoansDao;

	@Autowired
	BorrowerDAO borrowerDao;

	@Autowired
	GenreDAO genreDao;

	@Autowired
	PublisherDAO publisherDao;
	
	@Autowired
	BranchDAO branchDao;
	
	@Autowired
	CopiesDAO copiesDao;
	


	@Transactional
	public void addAuthor(String authorName) throws SQLException
	{
		Author author = new Author();
		author.setAuthorName(authorName);
		adao.create(author);
	}

	public List<Author> getAllAuthors(int pageNo, int pageSize, String searchString) throws SQLException
	{
		if (StringUtils.hasLength(searchString))
			return adao.readByName(searchString, pageNo, pageSize);
		else
			return adao.readAll(pageNo, pageSize);
	}

	public int getAllAuthorsCount() throws SQLException
	{

		return adao.readAllCount();

	}

	@Transactional
	public String deleteAuthor(Integer authorId) throws SQLException
	{
		Author author = new Author();
		author.setAuthorId(authorId);

		if (bookDao.getCountByAuthor(authorId,new String()) > 0)
			return "Unable to delete author, Author is a writer of some of the books owned by library";

		adao.delete(author);

		return "success";
	}

	@Transactional
	public void updateAuthor(Integer authorId, String authorName) throws SQLException
	{
		Author author = new Author();
		author.setAuthorId(authorId);
		author.setAuthorName(authorName);
		adao.update(author);
	}

	public List<Author> searchAuthors(String searchString, Integer pageNo, Integer pageSize) throws SQLException
	{
		return adao.readByName(searchString, pageNo, pageSize);

	}

	public int searchAuthorsCount(String searchString) throws SQLException
	{
		return adao.readByNameCount(searchString);
	}

	public String pagination(String url, String searchString, int count, int pageSize)
	{
		StringBuffer sb = new StringBuffer("<script src=\"./resources/template/pagination.js\"></script>");

		int totalPage = ((count) / pageSize);

		if (count % pageSize != 0)
			totalPage++;
		sb.append("<nav><ul class='pagination'>");

		if(!url.contains("?"))
			url = url + "?";

		if (StringUtils.hasLength(searchString))
			url = url + "&searchString=" + searchString;

		for (int i = 1; i <= totalPage; i++)
			// sb.append("<li><a  href = 'javascript:populate(" + url +
			// "pageNo=" + i + "&pageSize=" + pageSize + ")'>" + i +
			// "</a></li>");
			sb.append("<li><a   class='paginationClass' data-href='" + url + "&pageNo=" + i + "&pageSize=" + pageSize + "' >" + i + "</a></li>");

		sb.append("</ul></nav>");

		return sb.toString();

	}

	public Author getAuthorById(int authorId) throws SQLException
	{
		// TODO Auto-generated method stub
		return adao.readOne(authorId);
	}

	public int getAllBooksCount()
	{

		return bookDao.getCountAll();
	}

	public List<Book> getAllBooks(int pageNo, int pageSize, String searchString) throws SQLException
	{
		if (StringUtils.hasLength(searchString))
			return bookDao.readByTitle(pageNo, pageSize, searchString);
		else
			return bookDao.readAll(pageNo, pageSize);
	}

	public int getBooksCountByTitle(String searchString)
	{
		return bookDao.getCountByName(searchString);
	}

	public List<Genre> getAllGenre() throws SQLException
	{
		return genreDao.readAll();
	}

	public List<Publisher> getAllPublisher() throws SQLException
	{
		return publisherDao.readAll();
	}

	@Transactional
	public String addBook(String title, String authors, String genres, String publisher) throws SQLException
	{
		Book book = new Book();
		book.setTitle(title);
		book.setPublisher(publisherDao.readOne(Integer.parseInt(publisher)));
		int bookId = bookDao.create(book);
		book.setBookId(bookId);
		for (String author : authors.split(","))
			bookDao.insertAuthor(book, Integer.parseInt(author));
		for (String genre : genres.split(","))
			bookDao.insertGenre(book, Integer.parseInt(genre));

		return "success";
	}

	public int getAllGenreCount()
	{
		return genreDao.readAllCount();
	}

	public List<Genre> getAllGenre(int pageNo, int pageSize, String searchString) throws SQLException
	{
		if (StringUtils.hasLength(searchString))
			return genreDao.readByName(searchString, pageNo, pageSize);
		else
			return genreDao.readAll(pageNo, pageSize);
	}

	public int searchGenreCount(String searchString)
	{
		return genreDao.readByNameCount(searchString);
	}

	public Genre getGenreById(int genreId) throws SQLException
	{
		return genreDao.readOne(genreId);
	}

	public String addgenre(String genreName) throws SQLException
	{
		List<Genre> genres = genreDao.readAll();
		for (Genre genre : genres)
		{
			if (genre.getGenreName().toLowerCase().equals(genreName))
				return "Genre Already in library";
		}
		Genre a = new Genre();
		a.setGenreName(genreName);

		genreDao.create(a);
		return "success";
	}

	public void updateGenre(Integer genreId, String genreName) throws SQLException
	{

		Genre a = new Genre();
		a.setGenreId(genreId);
		a.setGenreName(genreName);
		genreDao.update(a);

	}

	@Transactional
	public String deleteGenre(int genreId) throws SQLException
	{
		Genre a = new Genre();
		a.setGenreId(genreId);

		if (bookDao.readByGenre(a,new String(), 1, 50).size() != 0)
			return "Unable to delete Genre, Genre is used for some of the books in library";

		genreDao.delete(a);
		return "success";
	}
	
	@Transactional
	public void addBorrower(String borName, String borAddress, String borPhone) throws SQLException {
		Borrower bor = new Borrower();
		bor.setName(borName);
		bor.setAddress(borAddress);
		bor.setPhoneNo(borPhone);
		borrowerDao.create(bor);
	}

	public List<Borrower> getBorrowers(int pageNo, int pageSize, String searchString) throws SQLException {
		if (StringUtils.hasLength(searchString))
			return borrowerDao.readByName(searchString, pageNo, pageSize);
		else
			return borrowerDao.readAll(pageNo, pageSize);
	}

	public int getAllBorrowersCount() throws SQLException {

		return borrowerDao.readAllCount();

	}

	@Transactional
	public String deleteBorrower(Integer borId) throws SQLException {
		Borrower bor = new Borrower();
		bor.setCardNo(borId);

		borrowerDao.delete(bor);

		return "success";
	}

	@Transactional
	public void updateBorrower(Integer borId, String borName, String borAddress, String borPhone)
			throws SQLException {
		Borrower bor = new Borrower();
		bor.setName(borName);
		bor.setAddress(borAddress);
		bor.setPhoneNo(borPhone);
		bor.setCardNo(borId);
		borrowerDao.update(bor);
	}

	public List<Borrower> searchBorowers(String searchString, Integer pageNo,
			Integer pageSize) throws SQLException {
		return borrowerDao.readByName(searchString, pageNo, pageSize);

	}

	public int searchBorrowersCount(String searchString) throws SQLException {
		return borrowerDao.readByNameCount(searchString);
	}


	public Borrower getBorrowerById(int borId) throws SQLException {
		// TODO Auto-generated method stub
		return borrowerDao.readOne(borId);
	}
	
	@Transactional
	public void addBranch(String BranchName, String BranchAddress)
			throws SQLException {
		Branch lb = new Branch();
		lb.setBranchName(BranchName);
		lb.setBranchAddress(BranchAddress);
		branchDao.create(lb);
	}

	public List<Branch> getAllBranches(int pageNo, int pageSize,
			String searchString) throws SQLException {
		if (StringUtils.hasLength(searchString))
			return branchDao.readByName(searchString, pageNo, pageSize);
		else
			return branchDao.readAll(pageNo, pageSize);
	}

	public int getBranchesCountByTitle(String searchString) throws SQLException {
		return branchDao.readByNameCount(searchString);
	}

	@Transactional
	public String deleteBranch(Integer branchId) throws SQLException {
		Branch lb = new Branch();
		lb.setBranchId(branchId);
		
		copiesDao.deleteByBranchId(lb);
		bookLoansDao.deleteByBranchId(branchId);
		branchDao.delete(lb);

		return "success";
	}

	@Transactional
	public void updateBranch(Integer branchId, String branchName,
			String branchAddress) throws SQLException {
		Branch lb = new Branch();
		lb.setBranchId(branchId);
		lb.setBranchName(branchName);
		lb.setBranchAddress(branchAddress);
		branchDao.update(lb);
	}

	public List<Branch> searchBranches(String searchString,
			Integer pageNo, Integer pageSize) throws SQLException {
		return branchDao.readByName(searchString, pageNo, pageSize);

	}

	public int searchBranchesCount(String searchString) throws SQLException {
		return branchDao.readByNameCount(searchString);
	}

	public Branch getBranchById(int branchId) throws SQLException {
		// TODO Auto-generated method stub
		return branchDao.readOne(branchId);
	}

	public int getBranchesCount(String searchString) throws SQLException {

		if(StringUtils.hasLength(searchString))
			return branchDao.readByNameCount(searchString);
		return branchDao.readAllCount();
	}

	public List<Branch> getLibrariesByBook(int bookId, int pageNo, int pageSize, String searchString)
	{
		// TODO Auto-generated method stub
		if(searchString.trim() == null)
			return branchDao.readByBook(bookId,pageNo, pageSize);
		else
			return branchDao.readByBook(bookId,pageNo,pageSize, searchString);
	}
	
	public int getLibrariesByBookCount(int bookId, String searchString)
	{
		if(searchString.trim() == null)
		return branchDao.readByBookCount(bookId);
		
		return branchDao.readByBookCount(bookId, searchString);

	}


	@Transactional
	public void borrowBook(int bookId, int branchId, int cardNo) throws SQLException
	{
			copiesDao.updateCopiesInc(bookId, branchId, -1);
			BookLoans loan = new BookLoans();
			loan.setBook(bookDao.readOne(bookId));
			loan.setBranch(branchDao.readOne(branchId));
			loan.setCard(borrowerDao.readOne(cardNo));
			bookLoansDao.create(loan);
		
		
	}
	
	public Book getBookById(int bookId) throws SQLException
	{
		return bookDao.readOne(bookId);
	}

	public List<Author> getAuthorsByBookId(Book book) throws SQLException
	{
		// TODO Auto-generated method stub
		return adao.readByBook(book);
	}
	
	public List<Genre> getGenresByBookId(Book book) throws SQLException
	{
		// TODO Auto-generated method stub
		return genreDao.readByBook(book);
	}

	@Transactional
	public String updateBook(int bookId, String title, String authors, String genres, String publisher) throws NumberFormatException, SQLException
	{
		Book book = new Book();
		book.setTitle(title);
		book.setPublisher(publisherDao.readOne(Integer.parseInt(publisher)));
		book.setBookId(bookId);
		
		bookDao.update(book);
		bookDao.removeAuthor(book);
		bookDao.removeGenre(book);
		
		for (String author : authors.split(","))
			bookDao.insertAuthor(book, Integer.parseInt(author));
		for (String genre : genres.split(","))
			bookDao.insertGenre(book, Integer.parseInt(genre));

		return "success";
	}

	public int getBooksCountByAuthor(int authorId, String searchString)
	{
		return bookDao.getCountByAuthor(authorId,searchString);
	}

	public List<Book> getBooksByAuthor(int authorId, int pageNo, int pageSize, String searchString) throws SQLException
	{
		return bookDao.readByAuthor(adao.readOne(authorId), searchString, pageNo, pageSize);
	}
	
	public List<Book> getBooksByGenre(int genreId, int pageNo, int pageSize, String searchString) throws SQLException
	{
		return bookDao.readByGenre(genreDao.readOne(genreId), searchString, pageNo, pageSize);
	}
	
	@Transactional
	public String deleteBook(int bookId, String Title) throws SQLException
	{
		// TODO Auto-generated method stub
		Book book = new Book();
		book.setBookId(bookId);
		if(copiesDao.readTotalCopiesByBookId(bookId) > 0)
			return "Cant delete Book " + Title + " Its owned by Libraries and they still have copies";
		
		copiesDao.deleteBooks(book);
		bookDao.removeAuthor(book);
		bookDao.removeGenre(book);
		
		bookDao.delete(book);
		
		return "success";
	}

	public int getBooksCountByGenre(int genreId, String searchString)
	{
		return bookDao.getCountByGenre(genreId,searchString);
	}

	public int getBookByBranchIdCount(int branchId, String searchString)
	{
		return copiesDao.getBookByBranchIdCount(branchId, searchString);
	}

	public List<Copies> getCopiesByBranchId(int branchId, int pageNo, int pageSize, String searchString) throws SQLException
	{
		return copiesDao.readByBranch(branchId, searchString, pageNo, pageSize);
	}

	public void addNoOfCopies(int bookId, int branchId, int change)
	{
		
		copiesDao.updateCopiesInc(bookId, branchId, change);
	}

	public List<Book> getBooksNotInBranch(int branchId)
	{
		// TODO Auto-generated method stub
		return bookDao.readNotInBranch(branchId);
	}

	@Transactional
	public void addCopiesOfBook(int bookId, int branchId, int copies)
	{
		// TODO Auto-generated method stub
		Copies copy =new Copies();
		copiesDao.create(bookId, branchId, copies);
		
	}

	public int getDueTodayCount()
	{
		return bookLoansDao.getDueTodayCount();
	}

	public List<BookLoans> getDueToday(int pageNo, int pageSize)
	{
		// TODO Auto-generated method stub
		return bookLoansDao.getDueToday(pageNo, pageSize);
	}
	
	public int getDueAllCount()
	{
		return bookLoansDao.getDueAllCount();
	}

	public List<BookLoans> getDueAll(int pageNo, int pageSize)
	{
		// TODO Auto-generated method stub
		return bookLoansDao.getDueAll(pageNo, pageSize);
	}

	@Transactional
	public String returnBook(Book book, Branch branch, Borrower bor) throws SQLException
	{
		// TODO Auto-generated method stub
		
		BookLoans loan = new BookLoans();
		loan.setBook(book);
		loan.setBranch(branch);
		loan.setCard(bor);
		
		bookLoansDao.updateDateIn(loan);
		copiesDao.updateCopiesInc(book.getBookId(), branch.getBranchId(), 1);
		return "success";
	}

	public int getAllCountForBorrower(int borId)
	{
		return bookLoansDao.getAllCountByBorId(borId);
	}
	
	public int getDueCountForBorrower(int borId)
	{
		return bookLoansDao.getDueCountByBorId(borId);
	}
	
	public List<BookLoans> getAllForBorrower(int borId, int pageNo, int pageSize)
	{
		return bookLoansDao.getAllByBorId(borId, pageNo, pageSize);
	}
	
	public List<BookLoans> getDueForBorrower(int borId, int pageNo, int pageSize)
	{
		return bookLoansDao.getDueByBorId(borId, pageNo, pageSize);
	}

	public int getHistoryCount(String searchString)
	{
		return bookLoansDao.getHistoryCount(searchString);
	}
	
	public List<BookLoans> getHistory(String searchString, int pageNo, int pageSize)
	{
		return bookLoansDao.getHistory(searchString, pageNo, pageSize);
	}

	public int getBookByBranchCount(String searchString)
	{
		return copiesDao.getCount(searchString);
	}

	public List<Copies> getCopies(int pageNo, int pageSize, String searchString)
	{
		// TODO Auto-generated method stub
		return copiesDao.getCopies(searchString, pageNo, pageSize);
	}

	
}
