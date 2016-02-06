<div class="page-header">
	<h1>Edit number of copies for book "${title}" from branch "${branchName}"</h1>

</div>
<div class="modal-content">
		<form action='editCopies' method='POST' id="editCopies">
		<input type='hidden' name="bookId" id='bookId' value='${bookId}' />
		<input type='hidden' name="branchId" id='branchId' value='${branchId}' />
		&nbsp;&nbsp;&nbsp;<select name="mult" id="mult">
			<option value ="1">Add</option>
			<option value ="-1">Remove</option>
		</select>
			Copies: <input class="Text" type="number" name="change" placeholder="Number Of Copies" id='change' min=0 step=1 required />
			<input type="submit" class ="btn primary" value="Change Copies."/>
		</form>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#editCopies').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "editNoOfCopies", //this is the submit URL
					type : 'POST', //or POST
					data : $('#editCopies').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							$("#SuccessEdit").show();
							alert("copies Successfully changed.");
							$('#SuccessEdit').delay(5000).fadeOut('slow');
						} else {
							$("#ErrorEdit").show();
							alert("There was a problem updating number of copies.");
							$('#ErrorEdit').delay(5000).fadeOut('slow');
						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});	
</script>
