<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include.html"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="page-header">
	<h1>History</h1>
	
</div>
<form action="entireHistory" method="get">
<div class="input-group">
  <span class="input-group-addon" id="basic-addon1">Search</span>
  <input type="text" class="form-control" value='${searchResult}' placeholder="Username" aria-describedby="basic-addon1" name="searchString" >
</div>
<button type="submit" class="btn btn-sm btn-primary">Search!</button>
</form>

	${pagination}


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