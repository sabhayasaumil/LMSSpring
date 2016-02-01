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
		<h2>Add Genre details below:</h2>
		Genre Name: <input type="text" name="genreName"
			value='${genreName}'>
		<button type="submit" class="btn btn-sm btn-primary">Add
			Genre</button>
	</form>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addGenre", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Genre Successfully added.");
						} else {
							alert("There was a problem adding Genre.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>