
<%@ page import="com.gcit.training.lms.entity.Book"%>
<%@ page import="java.util.List"%>
<%
	List<Book> books = (List<Book>)request.getAttribute("books");
%>

<div class="page-header">
	<h1>Add book to branch "${branchName}"</h1>

</div>
<div class="modal-content">
		<form action='addNoOfCopies'  method='POST' id="editCopies">
		<input type='hidden' name="branchId" id='branchId' value='${branchId}' />
		&nbsp;&nbsp;&nbsp;<input type=text' id="filter" placeholder="filter"/>
		<select name="bookId" id="bookId" required>
			<%
				for(Book book: books)
				{
				%>		
					<option value='<%=book.getBookId() %>' ><%=book.getTitle()%></option>
				<%
				}
			%>
		</select>

			Copies: <input  class="Text" type="number" name="copies" placeholder="Number Of Copies" id='change' min='1' value='1' step=1  required />
			<input  type="submit" class ="btn btn-mid primary" value="add Copies."/>
		</form>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#editCopies').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addNoOfCopies", //this is the submit URL
					type : 'POST', //or POST
					data : $('#editCopies').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							$("#SuccessEdit").show();
							alert("Book copies Successfully added.");
							$('#SuccessEdit').delay(5000).fadeOut('slow');
						} else {
							$("#ErrorEdit").show();
							alert("There was a problem adding book copies.");
							$('#ErrorEdit').delay(5000).fadeOut('slow');
						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

		$('#filter').keyup(function()
				{
					var filter = $(this).val().toLowerCase();
					$('option').each(function()
					{
						if ($(this).html().toLowerCase().indexOf(filter) >= 0)
					 	{
					 		//$(this).show();
					 		$(this).appendTo("#bookId");
					  	} 
				 		else
				 		{
							$(this).appendTo("#hiddenSelect");
				 			//$(this).hide();
						}
					
					})
					
					
				 });
		
		
	});
</script>
		<select id="hiddenSelect" style = "display:none;">
		</select>
