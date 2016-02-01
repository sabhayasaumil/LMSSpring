<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include.html"%>
<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.service.ConnectionUtil"%>
<%@ page import="com.gcit.training.lms.entity.Author"%>
<%@ page import="com.gcit.training.lms.dao.AuthorDAO"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	AdministrativeService adminService = new AdministrativeService();

	if(request.getAttribute("pagination")!=null)
	{
		%><%=adminService.pagination("", null, adminService.getAllAuthorsCount(), 10)%>
	<% 
	}
	else
	{
		%>
			${pagination}
		<%		
	}
%>

<div class="page-header">
	<h1>List of Authors in LMS Application</h1>
	
</div>
<form action="searchAuthor" method="get">
<div class="input-group">
  <span class="input-group-addon" id="basic-addon1">Search</span>
  <input type="text" class="form-control" value=$(searchResult) placeholder="Username" aria-describedby="basic-addon1" name="searchString" >
</div>
<button type="submit" class="btn btn-sm btn-primary">Search!</button>
</form>

<div class="row">
	<div class="col-md-6" id = "pageData">
		
	</div>
</div>