<div class="modal-content">
	<form id="modal_form">
		<input type="hidden" name="borId" value='${borId }' />
		<h2>Edit Branch details below:</h2>
		Borrower Name: <input type="text" name="borName" value='${borName}'>
		Borrower Address: <input type="text" name="borAddress" value='${borAddress}'>
		Borrower Phone: <input type="text" name="borPhone" value='${borPhone}'> 
		<button type="submit" class="btn btn-sm btn-primary">Apply
			Changes</button>
	</form>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "editBorrower", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							$("#SuccessEdit").show();
							alert("Borrower Successfully edited.");
							$('#SuccessEdit').delay(5000).fadeOut('slow');
						} else {
							$("#ErrorEdit").show();
							alert("There was a problem updating Borrower.");
							$('#ErrorEdit').delay(5000).fadeOut('slow');
						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>