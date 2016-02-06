package com.gcit.training;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.gcit.training.lms.dao.BranchDAO;
import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.BookLoans;
import com.gcit.training.lms.entity.Borrower;
import com.gcit.training.lms.entity.Branch;
import com.gcit.training.lms.entity.Copies;
import com.gcit.training.lms.entity.Genre;
import com.gcit.training.lms.entity.Publisher;
import com.gcit.training.lms.service.AdministrativeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController
{

	@Autowired
	AdministrativeService adminService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model)
	{
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "index";
	}

	@RequestMapping(value = "/librarian", method = RequestMethod.GET)
	public String librarian(Locale locale, Model model) throws SQLException
	{
		return "librarian";
	}

	@RequestMapping(value = "/borrower", method = RequestMethod.GET)
	public String borrower(Locale locale, Model model) throws SQLException
	{
		return "borrower";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		return "admin";

	}

	@RequestMapping(value = "/viewAuthor", method = RequestMethod.GET)
	public String viewAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int count = adminService.getAllAuthorsCount();
		String s = adminService.pagination("/training/getAuthor", new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewauthor";

	}

	@RequestMapping(value = "/getAuthor", method = RequestMethod.GET)
	public String getAuthorData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Author> authors;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		authors = (List<Author>) adminService.getAllAuthors(pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer("<table class='table' id='authorsTable'><thead><tr><th>#</th><th>Author Name</th><th>Books By Author</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Author author : authors)
			sb.append("<tr><td>"
					+ author.getAuthorId()
					+ "</td><td>"
					+ author.getAuthorName()
					+ "</td><td align='center'><a href='viewBooksByAuthor?authorId="
					+ author.getAuthorId()
					+ "'><button type='button' class='btn btn btn-primary' >Books</button></a></td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editAuthor?authorId="
					+ author.getAuthorId() + "'>Edit Author</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteAuthor?authorId="
					+ author.getAuthorId() + "';\">Delete Author</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchAuthor", method = RequestMethod.GET)
	public String searchAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchAuthorsCount(searchString);
		String s = adminService.pagination("/training/getAuthor", searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewauthor";

	}

	@RequestMapping(value = "/editAuthor", method = RequestMethod.GET)
	public String editAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String authorId = webRequest.getParameter("authorId");
		if (authorId == null)
		{
			String error = "Please Provide Author Id";
			model.addAttribute("error", error);
			return error;
		}

		try
		{
			Author author = adminService.getAuthorById(Integer.parseInt(authorId));
			model.addAttribute("authorName", author.getAuthorName());
			model.addAttribute("authorId", authorId);
		}
		catch (Exception E)
		{
			String error = "Please Provide Author Id";
			model.addAttribute("error", error);
			return error;

		}
		return "editauthor";

	}

	@RequestMapping(value = "/editAuthor", method = RequestMethod.POST)
	public String editAuthorPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		Integer authorId = Integer.parseInt(webRequest.getParameter("authorId"));
		String authorName = webRequest.getParameter("authorName");

		try
		{
			adminService.updateAuthor(authorId, authorName);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addAuthor", method = RequestMethod.GET)
	public String addAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		return "addauthor";
	}

	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
	public String addAuthorPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		String authorName = webRequest.getParameter("authorName");

		try
		{
			adminService.addAuthor(authorName);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";
	}

	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.GET)
	public String DeleteAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int authorId = Integer.parseInt(webRequest.getParameter("authorId"));

		try
		{
			model.addAttribute("authorName", adminService.getAuthorById(authorId).getAuthorName());
			String status = adminService.deleteAuthor(authorId);
			model.addAttribute("status", status);

		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error Delete Author");
		}
		return "deleteauthor";
	}

	@RequestMapping(value = "/deleteBook", method = RequestMethod.GET)
	public String DeleteBook(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		String Title = adminService.getBookById(bookId).getTitle();
		model.addAttribute("Title", Title);
		String status = adminService.deleteBook(bookId, Title);
		model.addAttribute("status", status);

		return "deleteauthor";
	}

	@RequestMapping(value = "/viewBook", method = RequestMethod.GET)
	public String viewBook(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int count = adminService.getAllBooksCount();
		String s = adminService.pagination("/training/getBook", new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewbook";
	}

	@RequestMapping(value = "/viewBooksByAuthor", method = RequestMethod.GET)
	public String viewBookByAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int authorId = Integer.parseInt(webRequest.getParameter("authorId"));
		String searchString = webRequest.getParameter("searchString");
		if (searchString == null)
			searchString = new String();
		int count = adminService.getBooksCountByAuthor(authorId, searchString);
		String s = adminService.pagination("/training/getBookByAuthor?authorId=" + authorId, searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		model.addAttribute("authorId", authorId);

		return "viewbookbyauthor";
	}

	@RequestMapping(value = "/viewBooksByGenre", method = RequestMethod.GET)
	public String viewBooksByGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int genreId = Integer.parseInt(webRequest.getParameter("genreId"));
		String searchString = webRequest.getParameter("searchString");
		if (searchString == null)
			searchString = new String();
		int count = adminService.getBooksCountByGenre(genreId, searchString);
		String s = adminService.pagination("/training/getBookByGenre?genreId=" + genreId, searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		model.addAttribute("genreId", genreId);
		return "viewbookbygenre";
	}

	@RequestMapping(value = "/searchBook", method = RequestMethod.GET)
	public String searchBook(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		String searchString = webRequest.getParameter("searchString");

		int count = adminService.getBooksCountByTitle(searchString);
		String s = adminService.pagination("/training/getBook", searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewbook";
	}

	@RequestMapping(value = "/getBook", method = RequestMethod.GET)
	public String getBookData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Book> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		books = (List<Book>) adminService.getAllBooks(pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer("<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Library</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Book book : books)
			sb.append("<tr><td>"
					+ book.getBookId()
					+ "</td><td>"
					+ book.getTitle()
					+ "</td><td align='center'><a href='viewBookLibrary?bookId="
					+ book.getBookId()
					+ "'><button type='button' class='btn btn btn-primary' >See in Library</button></a></td><td align='center'><a href='editBook?bookId="
					+ book.getBookId()
					+ "'><button type='button' class='btn btn btn-primary'>Edit Book</button></a></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBook?bookId="
					+ book.getBookId() + "';\">Delete Book</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/getBookByAuthor", method = RequestMethod.GET)
	public String getBookByAuthor(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Book> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");
		int authorId = Integer.parseInt(webRequest.getParameter("authorId"));

		if (searchString == null)
			searchString = new String();
		books = (List<Book>) adminService.getBooksByAuthor(authorId, pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer("<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Library</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Book book : books)
			sb.append("<tr><td>"
					+ book.getBookId()
					+ "</td><td>"
					+ book.getTitle()
					+ "</td><td align='center'><a href='viewBookLibrary?bookId="
					+ book.getBookId()
					+ "'><button type='button' class='btn btn btn-primary' >See in Library</button></a></td><td align='center'><a href='editBook?bookId="
					+ book.getBookId()
					+ "'><button type='button' class='btn btn btn-primary'>Edit Book</button></a></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBook?bookId="
					+ book.getBookId() + "';\">Delete Book</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/getBookByGenre", method = RequestMethod.GET)
	public String getBookByGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Book> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");
		int genreId = Integer.parseInt(webRequest.getParameter("genreId"));

		if (searchString == null)
			searchString = new String();
		books = (List<Book>) adminService.getBooksByGenre(genreId, pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer("<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Library</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Book book : books)
			sb.append("<tr><td>"
					+ book.getBookId()
					+ "</td><td>"
					+ book.getTitle()
					+ "</td><td align='center'><a href='viewBookLibrary?bookId="
					+ book.getBookId()
					+ "'><button type='button' class='btn btn btn-primary' >See in Library</button></a></td><td align='center'><a href='editBook?bookId="
					+ book.getBookId()
					+ "'><button type='button' class='btn btn btn-primary'>Edit Book</button></a></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBook?bookId="
					+ book.getBookId() + "';\">Delete Book</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/addBook", method = RequestMethod.GET)
	public String addBook(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int Count = adminService.getAllAuthorsCount();
		List<Author> authors = adminService.getAllAuthors(1, Count, new String());
		List<Genre> genres = adminService.getAllGenre();
		List<Publisher> publishers = adminService.getAllPublisher();

		model.addAttribute("authors", authors);
		model.addAttribute("genres", genres);
		model.addAttribute("publishers", publishers);

		return "addbook";
	}

	@RequestMapping(value = "/addBook", method = RequestMethod.POST)
	public String addBookPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		String authors = webRequest.getParameter("authors");
		String genres = webRequest.getParameter("genres");
		String title = webRequest.getParameter("title");
		String publisher = webRequest.getParameter("publisher");

		String status = adminService.addBook(title, authors, genres, publisher);

		return "status";
	}

	@RequestMapping(value = "/editBook", method = RequestMethod.GET)
	public String editBook(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));

		int Count = adminService.getAllAuthorsCount();
		List<Author> authors = adminService.getAllAuthors(1, Count, new String());
		List<Genre> genres = adminService.getAllGenre();
		List<Publisher> publishers = adminService.getAllPublisher();

		model.addAttribute("authors", authors);
		model.addAttribute("genres", genres);
		model.addAttribute("publishers", publishers);

		Book book = adminService.getBookById(bookId);
		book.setAuthors(adminService.getAuthorsByBookId(book));
		book.setGenres(adminService.getGenresByBookId(book));

		model.addAttribute("book", book);

		return "editbook";

	}

	@RequestMapping(value = "/editBook", method = RequestMethod.POST)
	public String editBookPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		String authors = webRequest.getParameter("authors");
		String genres = webRequest.getParameter("genres");
		String title = webRequest.getParameter("title");
		String publisher = webRequest.getParameter("publisher");

		String status = adminService.updateBook(bookId, title, authors, genres, publisher);
		model.addAttribute("status", status);
		model.addAttribute("title", title);
		return "statusbookedit";
	}

	@RequestMapping(value = "/viewGenre", method = RequestMethod.GET)
	public String viewGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int count = adminService.getAllGenreCount();
		String s = adminService.pagination("/training/getGenre", new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewgenre";

	}

	@RequestMapping(value = "/getGenre", method = RequestMethod.GET)
	public String getGenreData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Genre> genres;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		genres = (List<Genre>) adminService.getAllGenre(pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer("<table class='table' id='authorsTable'><thead><tr><th>#</th><th>Genre</th><th>Books By Genre</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Genre genre : genres)
			sb.append("<tr><td>"
					+ genre.getGenreId()
					+ "</td><td>"
					+ genre.getGenreName()
					+ "</td><td align='center'><a href='viewBooksByGenre?genreId="
					+ genre.getGenreId()
					+ "'><button type='button' class='btn btn btn-primary'>Books</button></a></td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editGenre?genreId="
					+ genre.getGenreId() + "'>Edit Genre</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteGenre?genreId="
					+ genre.getGenreId() + "';\">Delete Genre</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchGenre", method = RequestMethod.GET)
	public String searchGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchGenreCount(searchString);
		String s = adminService.pagination("/training/getGenre", searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewgenre";

	}

	@RequestMapping(value = "/editGenre", method = RequestMethod.GET)
	public String editGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String genreId = webRequest.getParameter("genreId");
		if (genreId == null)
		{
			String error = "Please Provide Genre Id";
			model.addAttribute("error", error);
			return error;
		}

		try
		{
			Genre genre = adminService.getGenreById(Integer.parseInt(genreId));
			model.addAttribute("genreName", genre.getGenreName());
			model.addAttribute("genreId", genreId);
		}
		catch (Exception E)
		{
			String error = "Please Provide Genre Id";
			model.addAttribute("error", error);
			return error;

		}
		return "editgenre";

	}

	@RequestMapping(value = "/editGenre", method = RequestMethod.POST)
	public String editGenrePost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		Integer genreId = Integer.parseInt(webRequest.getParameter("genreId"));
		String genreName = webRequest.getParameter("genreName");

		try
		{
			adminService.updateGenre(genreId, genreName);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addGenre", method = RequestMethod.GET)
	public String addGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		return "addgenre";
	}

	@RequestMapping(value = "/addGenre", method = RequestMethod.POST)
	public String addGenrePost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		String genreName = webRequest.getParameter("genreName");

		try
		{
			adminService.addgenre(genreName);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";
	}

	@RequestMapping(value = "/deleteGenre", method = RequestMethod.GET)
	public String DeleteGenre(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int genreId = Integer.parseInt(webRequest.getParameter("genreId"));

		try
		{
			model.addAttribute("genreName", adminService.getGenreById(genreId).getGenreName());
			String status = adminService.deleteGenre(genreId);
			model.addAttribute("status", status);

		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error Delete Genre");
		}
		return "deletegenre";
	}

	@RequestMapping(value = "/viewBranch", method = RequestMethod.GET)
	public String viewBranch(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String searchString = webRequest.getParameter("searchString");

		if (searchString == null)
			searchString = new String();

		int count = adminService.getBranchesCount(searchString);
		String s = adminService.pagination("/training/getBranch", searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);

		return "viewbranch";

	}

	@RequestMapping(value = "/getBranch", method = RequestMethod.GET)
	public String getBranchData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Branch> lb;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		lb = (List<Branch>) adminService.getAllBranches(pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Branch Name</th><th>Branch Address</th><th>Book List</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Branch lbs : lb)
			sb.append("<tr><td>"
					+ lbs.getBranchId()
					+ "</td><td>"
					+ lbs.getBranchName()
					+ "</td><td>"
					+ lbs.getBranchAddress()
					+ "</td><td align='center'><a href='getBranchCopies?branchId="
					+ lbs.getBranchId()
					+ "'><button type='button' class='btn btn btn-primary' >See Books</button></td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editBranch?branchId="
					+ lbs.getBranchId() + "'>Edit Branch</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBranch?branchId="
					+ lbs.getBranchId() + "';\">Delete Branch</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/getBranchCopies", method = RequestMethod.GET)
	public String getBranchCopies(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		String searchString = webRequest.getParameter("searchString");
		if (!StringUtils.hasLength(searchString))
			searchString = new String();

		int count = adminService.getBookByBranchIdCount(branchId, searchString);

		String s = adminService.pagination("/training/getBranchCopiesData?branchId=" + branchId, searchString, count, 10);

		model.addAttribute("branchName", adminService.getBranchById(branchId).getBranchName());
		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		model.addAttribute("branchId", branchId);
		return "viewbookbranch";
	}

	@RequestMapping(value = "/getBranchCopiesData", method = RequestMethod.GET)
	public String getBranchCopiesData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");
		if (!StringUtils.hasLength(searchString))
			searchString = new String();

		List<Copies> copies = adminService.getCopiesByBranchId(branchId, pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='bookBranchTable'><thead><tr><th>Book Title</th><th>no Of Copies.</th><th>Borrow Book</th><th>Edit No of Copies</th></tr></thead><tbody>");
		for (Copies copy : copies)
			sb.append("<tr><td>"
					+ copy.getBook().getTitle()
					+ "</td><td>"
					+ copy.getNoOfCopies()
					+ "</td><td align='center'><a href='selectBorrower?branchId="
					+ branchId
					+ "&bookId="
					+ copy.getBook().getBookId()
					+ "'><button type='button' class='btn btn btn-primary'>Borrow Book</button></a></td><td><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editNoOfCopies?branchId="
					+ branchId + "&bookId=" + copy.getBook().getBookId() + "'>Edit Copies</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());

		model.addAttribute("searchResult", searchString);

		return "result";
	}

	@RequestMapping(value = "/searchBranch", method = RequestMethod.GET)
	public String searchBranch(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.getBooksCountByTitle(searchString);
		String s = adminService.pagination("/training/getBranch", searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewbranch";

	}

	@RequestMapping(value = "/editBranch", method = RequestMethod.GET)
	public String editBranch(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String branchId = webRequest.getParameter("branchId");
		if (branchId == null)
		{
			String error = "Please Provide Branch Id1";
			model.addAttribute("error", error);
			return error;
		}

		try
		{
			Branch lb = adminService.getBranchById(Integer.parseInt(branchId));
			model.addAttribute("BranchName", lb.getBranchName());
			model.addAttribute("BranchAddress", lb.getBranchAddress());
			model.addAttribute("BranchId", branchId);
		}
		catch (Exception E)
		{
			String error = "Please Provide Branch Id2";
			model.addAttribute("error", error);
			return error;

		}
		return "editbranch";

	}

	@RequestMapping(value = "/editBranch", method = RequestMethod.POST)
	public String editBranchPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		Integer branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		String BranchName = webRequest.getParameter("BranchName");
		String BranchAddress = webRequest.getParameter("BranchAddress");

		try
		{
			adminService.updateBranch(branchId, BranchName, BranchAddress);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addBranch", method = RequestMethod.GET)
	public String addBranch(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		return "addbranch";
	}

	@RequestMapping(value = "/addBranch", method = RequestMethod.POST)
	public String addBranchPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		String BranchName = webRequest.getParameter("BranchName");
		String BranchAddress = webRequest.getParameter("BranchAddress");

		try
		{
			adminService.addBranch(BranchName, BranchAddress);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";
	}

	@RequestMapping(value = "/deleteBranch", method = RequestMethod.GET)
	public String deleteBranch(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));

		try
		{
			model.addAttribute("brancName", adminService.getBranchById(branchId).getBranchName());
			String status = adminService.deleteBranch(branchId);
			model.addAttribute("status", status);

		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error Deleting Branch");
		}
		return "deletebranch";
	}

	@RequestMapping(value = "/viewBorrower", method = RequestMethod.GET)
	public String viewBorrower(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int count = adminService.getAllBorrowersCount();
		String s = adminService.pagination("/training/getBorrower", new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewborrower";

	}

	@RequestMapping(value = "/getBorrower", method = RequestMethod.GET)
	public String getBorrowerData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Borrower> bor;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		bor = (List<Borrower>) adminService.getBorrowers(pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='borrowersTable'><thead><tr><th>#</th><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>History</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Borrower bors : bor)
			sb.append("<tr><td>"
					+ bors.getCardNo()
					+ "</td><td>"
					+ bors.getName()
					+ "</td><td>"
					+ bors.getAddress()
					+ "</td><td>"
					+ bors.getPhoneNo()
					+ "</td><td align='center'><a href='history?borId="
					+ bors.getCardNo()
					+ "'><button type='button' class='btn btn btn-primary' >Select</button></td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editBorrower?borId="
					+ bors.getCardNo() + "'>Edit Borrower</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBorrower?borId="
					+ bors.getCardNo() + "';\">Delete Borrower</button></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBorrower", method = RequestMethod.GET)
	public String searchBorrower(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.getBooksCountByTitle(searchString);
		String s = adminService.pagination("/training/getBorrower", searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewborrower";

	}

	@RequestMapping(value = "/editBorrower", method = RequestMethod.GET)
	public String editBorrower(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		String borId = webRequest.getParameter("borId");
		if (borId == null)
		{
			String error = "Please Provide Borrower ID";
			model.addAttribute("error", error);
			return error;
		}

		try
		{
			Borrower bor = adminService.getBorrowerById(Integer.parseInt(borId));
			model.addAttribute("borName", bor.getName());
			model.addAttribute("borAddress", bor.getAddress());
			model.addAttribute("borPhone", bor.getPhoneNo());
			model.addAttribute("borId", borId);
		}
		catch (Exception E)
		{
			String error = "Please Provide Borrower Id2";
			model.addAttribute("error", error);
			return error;

		}
		return "editborrower";

	}

	@RequestMapping(value = "/editBorrower", method = RequestMethod.POST)
	public String editBorrowerPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		Integer borId = Integer.parseInt(webRequest.getParameter("borId"));
		String borName = webRequest.getParameter("borName");
		String borAddress = webRequest.getParameter("borAddress");
		String borPhone = webRequest.getParameter("borPhone");

		try
		{
			adminService.updateBorrower(borId, borName, borAddress, borPhone);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addBorrower", method = RequestMethod.GET)
	public String addBorrower(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		return "addborrower";
	}

	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST)
	public String addBorrowerPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		String borName = webRequest.getParameter("borName");
		String borAddress = webRequest.getParameter("borAddress");
		String borPhone = webRequest.getParameter("borPhone");

		try
		{
			adminService.addBorrower(borName, borAddress, borPhone);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";
	}

	@RequestMapping(value = "/deleteBorrower", method = RequestMethod.GET)
	public String DeleteBorrower(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int borId = Integer.parseInt(webRequest.getParameter("borId"));

		try
		{
			model.addAttribute("borName", adminService.getBorrowerById(borId).getName());
			String status = adminService.deleteBorrower(borId);
			model.addAttribute("status", status);

		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error Deleting Book");
		}
		return "deleteborrower";
	}

	@RequestMapping(value = "/viewBookLibrary", method = RequestMethod.GET)
	public String viewbookLibrary(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));

		String searchString = webRequest.getParameter("searchString");
		if (searchString == null)
			searchString = new String();

		int count = adminService.getLibrariesByBookCount(bookId, searchString);

		String s = adminService.pagination("/training/getBookLibrary?bookId=" + bookId, searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("bookId", bookId);
		model.addAttribute("searchResult", searchString);
		return "viewbooklibrary";

	}

	@RequestMapping(value = "/getBookLibrary", method = RequestMethod.GET)
	public String getbookLibrary(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		if (searchString == null)
			searchString = new String();

		List<Branch> branches = adminService.getLibrariesByBook(bookId, pageNo, pageSize, searchString);

		StringBuffer sb = new StringBuffer("<table class='table' id='branchTable'><thead><tr><th>#</th><th>Branch Name</th><th>Address</th><th>Borrow Book</th></tr></thead><tbody>");
		for (Branch branch : branches)
			sb.append("<tr><td>" + branch.getBranchId() + "</td><td>" + branch.getBranchName() + "</td><td>" + branch.getBranchAddress() + "</td><td align='center'><a href='selectBorrower?branchId="
					+ branch.getBranchId() + "&bookId=" + bookId + "'><button type='button' class='btn btn btn-primary' data-toggle='modal'>Borrow Book</button></a></td></tr>");

		sb.append("</table>");
		model.addAttribute("result", sb.toString());
		return "result";

	}

	@RequestMapping(value = "/selectBorrower", method = RequestMethod.GET)
	public String selectBorrower(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));

		int count = adminService.getAllBorrowersCount();
		String searchString = webRequest.getParameter("searchString");

		if (searchString == null)
			searchString = new String();

		String s = adminService.pagination("/training/getSelectBorrower?bookId=" + bookId + "&branchId=" + branchId, searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);

		model.addAttribute("bookId", bookId);
		model.addAttribute("title", adminService.getBookById(bookId).getTitle());
		model.addAttribute("branchName", adminService.getBranchById(branchId).getBranchName());
		model.addAttribute("branchId", branchId);

		return "viewborrowerselect";
	}

	@RequestMapping(value = "/getSelectBorrower", method = RequestMethod.GET)
	public String getSelectBorrowerData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		List<Borrower> bor;

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));

		String searchString = webRequest.getParameter("searchString");

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));

		bor = (List<Borrower>) adminService.getBorrowers(pageNo, pageSize, searchString);
		StringBuffer sb = new StringBuffer(
				"<table class='table' id='borrowersTable'><thead><tr><th>#</th><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>Borrow</th></tr></thead><tbody>");
		for (Borrower bors : bor)
			sb.append("<tr><td>" + bors.getCardNo() + "</td><td>" + bors.getName() + "</td><td>" + bors.getAddress() + "</td><td>" + bors.getPhoneNo()
					+ "</td><td align='center'><a href='Borrow?borId=" + bors.getCardNo() + "&bookId=" + bookId + "&branchId=" + branchId
					+ "'><button type='button' class='btn btn btn-primary'>Borrow Book</button></a></td></tr>");

		sb.append("</table>");

		model.addAttribute("result", sb.toString());

		return "result";
	}

	@RequestMapping(value = "/Borrow", method = RequestMethod.GET)
	public String borrow(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		int cardNo = Integer.parseInt(webRequest.getParameter("borId"));

		String bookName = adminService.getBookById(bookId).getTitle();
		String branchName = adminService.getBranchById(branchId).getBranchName();
		String borrowerName = adminService.getBorrowerById(cardNo).getName();

		try
		{
			adminService.borrowBook(bookId, branchId, cardNo);
			model.addAttribute("status", "success");
			model.addAttribute("bookName", bookName);
			model.addAttribute("branchName", branchName);
			model.addAttribute("borrowerName", borrowerName);

		}
		catch (Exception E)
		{
			model.addAttribute("status", "Unable to borrow Book " + bookName + " from branch " + branchName);
		}
		return "statusborrow";
	}

	@RequestMapping(value = "/editNoOfCopies", method = RequestMethod.GET)
	public String editNoOfCopies(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));

		model.addAttribute("branchId", branchId);
		model.addAttribute("bookId", bookId);
		model.addAttribute("title", adminService.getBookById(bookId).getTitle());
		model.addAttribute("branchName", adminService.getBranchById(branchId).getBranchName());

		return "editcopies";
	}

	@RequestMapping(value = "/editNoOfCopies", method = RequestMethod.POST)
	public String editNoOfCopiesPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		int multiplier = Integer.parseInt(webRequest.getParameter("mult"));
		int change = Integer.parseInt(webRequest.getParameter("change"));

		try
		{
			change = change * multiplier;
			adminService.addNoOfCopies(bookId, branchId, change);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addNoOfCopies", method = RequestMethod.GET)
	public String addBooksToBranch(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		List<Book> books = adminService.getBooksNotInBranch(branchId);
		model.addAttribute("branchId", branchId);
		model.addAttribute("books", books);
		model.addAttribute("branchName", adminService.getBranchById(branchId).getBranchName());

		return "addbooktobranch";
	}

	@RequestMapping(value = "/addNoOfCopies", method = RequestMethod.POST)
	public String addBooksToBranchPost(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int copies = Integer.parseInt(webRequest.getParameter("copies"));

		try
		{
			adminService.addCopiesOfBook(bookId, branchId, copies);
			model.addAttribute("status", "success");
		}
		catch (Exception E)
		{
			model.addAttribute("status", "Error");
		}

		return "status";

	}

	@RequestMapping(value = "/viewBooksDue", method = RequestMethod.GET)
	public String viewBooksDue(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int count = adminService.getDueTodayCount();
		String s = adminService.pagination("/training/getDueBooks", new String(), count, 10);
		if (s.trim() == null)
			s = "No Books Due Today.";

		model.addAttribute("pagination", s);
		model.addAttribute("title", "Books Due Today");
		return "viewbookdue";
	}

	@RequestMapping(value = "/getDueBooks", method = RequestMethod.GET)
	public String getBooksDueData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));

		List<BookLoans> loans = adminService.getDueToday(pageNo, pageSize);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='loanTable'><thead><tr><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>Book Title</th><th>Branch</th><th>Date Borrowed</th><th>Due Date</th><th>Due In</th><th>Return Book</th></tr></thead><tbody>");
		for (BookLoans loan : loans)
			sb.append("<tr><td>" + loan.getCard().getName() + "</td><td>" + loan.getCard().getAddress() + "</td><td>" + loan.getCard().getPhoneNo() + "</td><td>" + loan.getBook().getTitle()
					+ "</td><td>" + loan.getBranch().getBranchName() + "</td><td>" + loan.getDateOut() + "</td><td>" + loan.getDueDate() + "</td><td>" + loan.getDateIn()
					+ "</td><td align='center'><a href='return?borId=" + loan.getCard().getCardNo() + "&bookId=" + loan.getBook().getBookId() + "&branchId=" + loan.getBranch().getBranchId()
					+ "'><button type='button' class='btn btn btn-primary'>Return Book</button></a></td></tr>");

		sb.append("</table>");

		model.addAttribute("result", sb.toString());

		return "result";
	}

	@RequestMapping(value = "/viewAllBooksDue", method = RequestMethod.GET)
	public String viewAllBooksDue(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int count = adminService.getDueAllCount();
		String s = adminService.pagination("/training/getAllDueBooks", new String(), count, 10);

		if (s.trim() == null)
			s = "No Books Due.";
		model.addAttribute("pagination", s);
		model.addAttribute("title", "All Due books");
		return "viewbookdue";
	}

	@RequestMapping(value = "/getAllDueBooks", method = RequestMethod.GET)
	public String getAllBooksDueData(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));

		List<BookLoans> loans = adminService.getDueAll(pageNo, pageSize);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='loanTable'><thead><tr><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>Book Title</th><th>Branch</th><th>Date Borrowed</th><th>Due Date</th><th>Due In</th><th>Return Book</th></tr></thead><tbody>");
		for (BookLoans loan : loans)
		{
			sb.append("<tr><td>" + loan.getCard().getName() + "</td><td>" + loan.getCard().getAddress() + "</td><td>" + loan.getCard().getPhoneNo() + "</td><td>" + loan.getBook().getTitle()

			+ "</td><td>" + loan.getBranch().getBranchName() + "</td><td>" + loan.getDateOut() + "</td><td>" + loan.getDueDate() + "</td><td>");

			if (loan.getDateIn() != null)
				sb.append(loan.getDateIn());
			else
				sb.append("-");
			sb.append("</td><td align='center'><a href='return?borId=" + loan.getCard().getCardNo() + "&bookId=" + loan.getBook().getBookId() + "&branchId=" + loan.getBranch().getBranchId()
					+ "'><button type='button' class='btn btn btn-primary'>Return Book</button></a></td></tr>");
		}
		sb.append("</table>");

		model.addAttribute("result", sb.toString());

		return "result";
	}

	@RequestMapping(value = "/return", method = RequestMethod.GET)
	public String returnBook(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));
		int borId = Integer.parseInt(webRequest.getParameter("borId"));

		try
		{
			Book book = adminService.getBookById(bookId);
			Branch branch = adminService.getBranchById(branchId);
			Borrower bor = adminService.getBorrowerById(borId);

			String status = adminService.returnBook(book, branch, bor);
			model.addAttribute("borrowerName", bor.getName());
			model.addAttribute("bookName", book.getTitle());
			model.addAttribute("branchName", branch.getBranchName());

			model.addAttribute("status", status);

		}
		catch (Exception e)
		{
			model.addAttribute("status", e.getMessage());
		}

		return "statusreturn";
	}
	
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String history(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
		int borId = Integer.parseInt(webRequest.getParameter("borId"));

		model.addAttribute("borId", borId);
		return "history";
	}

@RequestMapping(value = "/viewHistory", method = RequestMethod.GET)
	public String viewHistory(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
	
		int borId = Integer.parseInt(webRequest.getParameter("borId"));
		int count = adminService.getAllCountForBorrower(borId);
		String s = adminService.pagination("/training/getHistory?borId="+borId, new String(), count, 10);

		if (s.trim() == null)
			s = "No Hitory Found.";
		model.addAttribute("pagination", s);
		model.addAttribute("title", "All Due books");
		return "viewbookdue";
	}

	@RequestMapping(value = "/getHistory", method = RequestMethod.GET)
	public String getHistory(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		int borId = Integer.parseInt(webRequest.getParameter("borId"));
	
		List<BookLoans> loans = adminService.getAllForBorrower(borId, pageNo, pageSize);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='loanTable'><thead><tr><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>Book Title</th><th>Branch</th><th>Date Borrowed</th><th>Due Date</th><th>Due In</th></tr></thead><tbody>");
		for (BookLoans loan : loans)
		{
			sb.append("<tr><td>" + loan.getCard().getName() + "</td><td>" + loan.getCard().getAddress() + "</td><td>" + loan.getCard().getPhoneNo() + "</td><td>" + loan.getBook().getTitle()

			+ "</td><td>" + loan.getBranch().getBranchName() + "</td><td>" + loan.getDateOut() + "</td><td>" + loan.getDueDate() + "</td><td align='center'>");

			if (loan.getDateIn() != null)
				sb.append(loan.getDateIn());
			else
				sb.append("<a href='return?borId=" + loan.getCard().getCardNo() + "&bookId=" + loan.getBook().getBookId() + "&branchId=" + loan.getBranch().getBranchId()
					+ "'><button type='button' class='btn btn btn-primary'>Return Book</button></a>");
			sb.append("</td></tr>");
		}
		sb.append("</table>");

		model.addAttribute("result", sb.toString());

		return "result";
	}

@RequestMapping(value = "/viewDue", method = RequestMethod.GET)
	public String viewDue(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{
	
		int borId = Integer.parseInt(webRequest.getParameter("borId"));
		int count = adminService.getDueCountForBorrower(borId);
		String s = adminService.pagination("/training/getDue?borId="+borId, new String(), count, 10);

		if (s.trim() == null)
			s = "No Hitory Found.";
		model.addAttribute("pagination", s);
		model.addAttribute("title", "All Due books");
		return "viewbookdue";
	}

	@RequestMapping(value = "/getDue", method = RequestMethod.GET)
	public String getDue(Locale locale, Model model, WebRequest webRequest) throws SQLException
	{

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		int borId = Integer.parseInt(webRequest.getParameter("borId"));
	
		List<BookLoans> loans = adminService.getDueForBorrower(borId, pageNo, pageSize);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='loanTable'><thead><tr><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>Book Title</th><th>Branch</th><th>Date Borrowed</th><th>Due Date</th><th>Due In</th></tr></thead><tbody>");
		for (BookLoans loan : loans)
		{
			sb.append("<tr><td>" + loan.getCard().getName() + "</td><td>" + loan.getCard().getAddress() + "</td><td>" + loan.getCard().getPhoneNo() + "</td><td>" + loan.getBook().getTitle()

			+ "</td><td>" + loan.getBranch().getBranchName() + "</td><td>" + loan.getDateOut() + "</td><td>" + loan.getDueDate() + "</td><td align='center'>");

			if (loan.getDateIn() != null)
				sb.append(loan.getDateIn());
			else
				sb.append("<a href='return?borId=" + loan.getCard().getCardNo() + "&bookId=" + loan.getBook().getBookId() + "&branchId=" + loan.getBranch().getBranchId()
					+ "'><button type='button' class='btn btn btn-primary'>Return Book</button></a>");
			sb.append("</td></tr>");
		}
		sb.append("</table>");

		model.addAttribute("result", sb.toString());

		return "result";
	}

}
