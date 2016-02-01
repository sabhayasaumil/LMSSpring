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
import com.gcit.training.lms.dao.GenreDAO;
import com.gcit.training.lms.dao.PublisherDAO;
import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.Borrower;
import com.gcit.training.lms.entity.Branch;
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

		if (bookDao.readByAuthor(author).size() != 0)
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

		url = url + "?";

		if (StringUtils.hasLength(searchString))
			url = url + "searchString=" + searchString + "&";

		for (int i = 1; i <= totalPage; i++)
			// sb.append("<li><a  href = 'javascript:populate(" + url +
			// "pageNo=" + i + "&pageSize=" + pageSize + ")'>" + i +
			// "</a></li>");
			sb.append("<li><a   class='paginationClass' data-href='" + url + "pageNo=" + i + "&pageSize=" + pageSize + "' >" + i + "</a></li>");

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

	public String deleteGenre(int genreId) throws SQLException
	{
		Genre a = new Genre();
		a.setGenreId(genreId);

		if (bookDao.readByGenre(a).size() != 0)
			return "Unable to delete Genre, Genre is used for some of the books in library";

		genreDao.delete(a);
		return "success";
	}
	
	public void addBorrower(String borName, String borAddress, String borPhone) throws SQLException {
		Borrower bor = new Borrower();
		bor.setName(borName);
		bor.setAddress(borAddress);
		bor.setPhoneNo(borPhone);
		borrowerDao.create(bor);
	}

	public List<Borrower> getAllBorrowers(int pageNo, int pageSize,
			String searchString) throws SQLException {
		if (StringUtils.hasLength(searchString))
			return borrowerDao.readByName(searchString, pageNo, pageSize);
		else
			return borrowerDao.readAll(pageNo, pageSize);
	}

	public int getAllBorrowersCount() throws SQLException {

		return borrowerDao.readAllCount();

	}

	@Transactional
	public String deleteborrower(Integer borId) throws SQLException {
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
		
		branchDao.delete(lb);

		return "success";
	}

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

	public int getAllBranchesCount() throws SQLException {

		return branchDao.readAllCount();
	}

}
