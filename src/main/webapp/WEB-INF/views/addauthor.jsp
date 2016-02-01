<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.service.ConnectionUtil"%>
<%@ page import="com.gcit.training.lms.entity.Author"%>
<%@ page import="com.gcit.training.lms.dao.AuthorDAO"%>
<%@ page import="java.util.List"%>

<%
	AdministrativeService adminService = new AdministrativeService();
%>
<div class="modal-content">
	<form id="modal_form">
		<h2>Add Author details below:</h2>
		Author Name: <input type="text" name="authorName"
			value='${authorName}'>
		<button type="submit" class="btn btn-sm btn-primary">Add
			Author</button>
	</form>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addAuthor", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Author Successfully added.");
						} else {
							alert("There was a problem adding author.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>