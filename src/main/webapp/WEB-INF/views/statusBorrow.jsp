<%@include file="include.html"%>
<div class="container theme-showcase" role="main"
	style="left-margin: 100px">
	<%
		if (request.getAttribute("status").equals("success"))
		{
	%>

	<div class="alert alert-success" role="alert" id="#ErrorEdit">
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		<span class="sr-only">Success: </span> ${borrowerName} successfuly
		borrowed book ${bookName} from branch ${branchName}.
	</div>

	<%
		}
		else
		{
	%>
	<div class="alert alert-danger" role="alert" id="#ErrorEdit">
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		<span class="sr-only">Error:</span> ${status}
	</div>

	<%
		}
	%>
</div>
<!-- /container -->