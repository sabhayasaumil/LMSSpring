<%@include file="include.html"%>
	<form action="addBranch" method="post">
	${result}
		<h2>Enter Branch details below:</h2>

		Branch Name: <input type="text" name="BranchName"> 
		Branch Address: <input type="text" name="BranchAddress"> 
		<button type="submit" class="btn btn-sm btn-primary">Add Branch</button>
	</form>
