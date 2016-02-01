<%@include file="include.html"%>
	<form action="addBorrower" method="post">
	${result}
		<h2>Enter Borrower details below:</h2>

		Borrower Name: <input type="text" name="borName"> 
		Borrower Address: <input type="text" name="borAddress"> 
		Borrower Phone: <input type="text" name="borPhone"> 
		<button type="submit" class="btn btn-sm btn-primary">Add Borrower</button>
	</form>
