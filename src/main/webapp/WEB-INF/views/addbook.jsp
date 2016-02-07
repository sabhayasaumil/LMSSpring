<%@page import="com.gcit.training.lms.entity.Publisher"%>
<%@include file="include.html"%>
<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.service.ConnectionUtil"%>
<%@ page import="com.gcit.training.lms.entity.Author"%>
<%@ page import="com.gcit.training.lms.entity.Publisher"%>
<%@ page import="com.gcit.training.lms.entity.Genre"%>
<%@ page import="java.util.List"%>
<%
	List<Author> authors = (List<Author>) request.getAttribute("authors");
	List<Genre> genres = (List<Genre>) request.getAttribute("genres");
	List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
%>



<div class="page-header">
	<h1>Add Book</h1>

</div>
<div class="row">
	<div class="col-sm-4">
		<form action='addBook' method='POST' id="addBook">
			Title: <input class="Text form-control" type='title' name="bookName" placeholder="title" id='title' required />
			</br> 
			Author: <select class="form-control" multiple name = "authors" id="authors" required >
				<%
					for(Author author:authors)
					{
				%>
				<option class="multiple" value="<%=author.getAuthorId()%>"><%=author.getAuthorName() %></option>
				<%
					}
				%>
			</select>
			</br>
			Genre: <select class="form-control" multiple name = 'genres' id='genres' required >
				<%
					for(Genre genre:genres)
					{
				%>
				<option class="multiple" value="<%=genre.getGenreId()%>"><%=genre.getGenreName()%></option>
				<%
					}
				%>
			</select>
			</br>
			Publisher: <select class="form-control" name='publisher' id="publisher" required>
				<%
					for(Publisher publisher:publishers)
					{
				%>
				<option value="<%=publisher.getPublisherId()%>"><%=publisher.getPublisherName() %></option>
				<%
					}
				%>
			</select></br>
			<input type="submit" class ="btn btn-lg" value="add Book"/>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#addBook').on('submit', function(e) {
				e.preventDefault();
				var param = "authors="+$("#authors").val()+"&genres="+$("#genres").val()+"&publisher="+$("#publisher").val()+"&title="+$("#title").val();
			    $.post("addBook", param).done(function( data ) {
			    	$('#addBook').remove();
			        $(".modal-content").html(data);
			        $("#myModal1").modal("toggle");
			    });
				/*
				e.preventDefault();
				request.setAttribute("authors",$("#authors").val());
				request.setAttribute("genres",$("#genres").val());
				request.setAttribute("publisher",$("#publisher").val());
				request.setAttribute("title",$("#title").val());
			    RequestDispatcher requestDispatcher =
			    request.getRequestDispatcher("addBook");
			    requestDispatcher.forward(request,response);
				
				
				
				/*
				var Authors =""; 
				$.ajax({
					url : "addBook", //this is the submit URL
					type : 'POST', //or POST
					data : "authors="+$("#authors").val()+"&genres="+$("#genres").val()+"&publisher="+$("#publisher").val()+"&title="+$("#title").val(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Book Successfully added.");
							
						} else {
							alert("There was a problem adding Book.");

						}
					}
				});*/
			});
		});

	});
</script>


<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg">
		<div class="modal-content"></div>
	</div>
</div>