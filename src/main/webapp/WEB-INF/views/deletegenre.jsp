<%@include file="include.html"%>
<div class="container theme-showcase" role="main"
	style="left-margin: 100px">
	<%
		if (request.getAttribute("status").equals("success"))
		{
	%>

	<div class="alert alert-success" role="alert" id="#ErrorEdit">
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
		<span class="sr-only">Success: </span> ${genreName} is successfully
		deleted.
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