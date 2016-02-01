<div class="modal-content">
	<form id="modal_form">
		<input type="hidden" name="branchId" value='${BranchId}'>
		<h2>Edit Branch details below:</h2>
		Branch Name: <input type="text" name="BranchName" value='${BranchName}'>
		Branch Address: <input type="text" name="BranchAddress" value='${BranchAddress}'>
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
					url : "editBranch", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							$("#SuccessEdit").show();
							alert("Branch Successfully edited.");
							$('#SuccessEdit').delay(5000).fadeOut('slow');
						} else {
							$("#ErrorEdit").show();
							alert("There was a problem updating branch.");
							$('#ErrorEdit').delay(5000).fadeOut('slow');
						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>