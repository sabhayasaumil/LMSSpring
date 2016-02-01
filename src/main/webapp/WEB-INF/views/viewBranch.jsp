<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include.html"%>
<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.service.ConnectionUtil"%>
<%@ page import="com.gcit.training.lms.entity.Author"%>
<%@ page import="com.gcit.training.lms.dao.AuthorDAO"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<div class="alert alert-danger" role="alert" id="#ErrorEdit" style="display:none;">
  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
  <span class="sr-only">Error:</span>
  Updating Failed
</div>
<div class="alert alert-success" role="alert" id="#SuccessEdit" style="display:none;">
  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
  <span class="sr-only">Error:</span>
  Branch Successfully Updated.
</div>


<div class="page-header">
	<h1>List of Branches in LMS Application</h1>
	
</div>
<form action="searchBranch" method="get">
<div class="input-group">
  <span class="input-group-addon" id="basic-addon1">Search</span>
  <input type="text" class="form-control" value='${searchResult}' placeholder="Branche Name" aria-describedby="basic-addon1" name="searchString" >
</div>
<button type="submit" class="btn btn-sm btn-primary">Search!</button>
</form>
<%
	AdministrativeService adminService = new AdministrativeService();

	if(request.getAttribute("pagination")==null)
	{
		out.write(adminService.pagination("", null, adminService.getAllBooksCount(), 10));
	
	}
	else
	{
		%>
			${pagination}
		<%		
	}
%>
<div class="row">
	<div class="col-md-6" id = "pageData">
		
	</div>
</div>

<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg">
		<div class="modal-content"></div>
	</div>
</div>