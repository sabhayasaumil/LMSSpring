package com.gcit.training;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.training.lms.dao.AuthorDAO;
import com.gcit.training.lms.dao.BookDAO;
import com.gcit.training.lms.dao.BookLoansDAO;
import com.gcit.training.lms.dao.BorrowerDAO;
import com.gcit.training.lms.dao.BranchDAO;
import com.gcit.training.lms.dao.CopiesDAO;
import com.gcit.training.lms.dao.GenreDAO;
import com.gcit.training.lms.dao.PublisherDAO;
import com.gcit.training.lms.service.AdministrativeService;

@EnableTransactionManagement
@Configuration	
public class LMSConfig
{	
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/library";
	private static String username = "root";
	private static String password = "";
	
	@Bean
	public BasicDataSource dataSource()
	{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	public PlatformTransactionManager txManager()
	{
		DataSourceTransactionManager tx = new DataSourceTransactionManager();
		tx.setDataSource(dataSource());
		return tx;
	}
	
	@Bean
	public JdbcTemplate template()
	{
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource());
		return template;
	}
	
	@Bean
	public AdministrativeService adminService()
	{
		return new AdministrativeService();
	}
	
	
	@Bean
	public AuthorDAO AuthorDao()
	{
		return new AuthorDAO();
	}
	
	@Bean
	public BookDAO BookDao()
	{
		return new BookDAO();
	}
	
	@Bean
	public BookLoansDAO BookLoansDao()
	{
		return new BookLoansDAO();
	}
	
	@Bean
	public BorrowerDAO BorrowerDao()
	{
		return new BorrowerDAO();
	}
	
	@Bean
	public BranchDAO BranchDao()
	{
		return new BranchDAO();
	}
	
	@Bean
	public CopiesDAO CopiesDao()
	{
		return new CopiesDAO();
	}
	
	@Bean
	public GenreDAO GenreDao()
	{
		return new GenreDAO();
	}
	
	@Bean
	public PublisherDAO PublisherDao()
	{
		return new PublisherDAO();
	}
	
	
}
