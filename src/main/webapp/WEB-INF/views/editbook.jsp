<%@page import="com.gcit.training.lms.entity.Publisher"%>
<%@include file="include.html"%>
<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.service.ConnectionUtil"%>
<%@ page import="com.gcit.training.lms.entity.Author"%>
<%@ page import="com.gcit.training.lms.entity.Publisher"%>
<%@ page import="com.gcit.training.lms.entity.Book"%>
<%@ page import="com.gcit.training.lms.entity.Genre"%>
<%@ page import="java.util.List"%>
<%
	List<Author> authors = (List<Author>) request.getAttribute("authors");
	List<Genre> genres = (List<Genre>) request.getAttribute("genres");
	List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
	Book book = (Book)request.getAttribute("book");
%>



<div class="page-header">
	<h1>Edit Book</h1>

</div>
<div class="row">
	<div class="col-sm-4">
		<form action='editBook' method='POST' id="editBook">
		<input type='hidden' name="bookId" id='bookId' value='<%=book.getBookId() %>' />
			Title: <input class="Text form-control" type='title' name="bookName" placeholder="title" id='title' value='<%=book.getTitle() %>' required />
			</br> 
			Author: <select class="form-control" multiple name = "authors" id="authors" required >
				<%
					for(Author author:authors)
					{
				%>
				<option class="multiple" id = 'author<%=author.getAuthorId()%>' value="<%=author.getAuthorId()%>"><%=author.getAuthorName() %></option>
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
				<option class="multiple" id = 'genre<%=genre.getGenreId()%>' value="<%=genre.getGenreId()%>"><%=genre.getGenreName()%></option>
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
			<input type="submit" class ="btn btn-lg" value="Edit Book"/>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		
		<%
			for(Author author:book.getAuthors())
			{
				%>
				$("#author"+<%=author.getAuthorId()%>).prop("selected", true);
				<%	
			}
		
			for(Genre genre:book.getGenres())
			{
				%>
				$("#genre"+<%=genre.getGenreId()%>).prop("selected", true);
				<%
				
			}
		%>
		
		
		
		
		
		$(function() {
			$('#editBook').on('submit', function(e) {
				e.preventDefault();
				var param = "bookId="+$("#bookId").val()+"&authors="+$("#authors").val()+"&genres="+$("#genres").val()+"&publisher="+$("#publisher").val()+"&title="+$("#title").val();
			    $.post("editBook", param).done(function( data ) {
			        $(".modal-content").html(data);
			        $("#myModal1").modal("toggle");
			    });
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