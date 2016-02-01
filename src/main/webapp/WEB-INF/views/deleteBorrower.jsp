<%@include file="include.html"%>
<div class="container theme-showcase" role="main"
	style="left-margin: 100px">
	<%
		if (request.getAttribute("status").equals("success"))
		{
	%>

	<div class="alert alert-success" role="alert" id="#ErrorEdit">
		<span class="sr-only">Success: </span> ${borName} is successfully
		deleted.
	</div>

	<%
		}
		else
		{
	%>
	<div class="alert alert-danger" role="alert" id="#ErrorEdit">
		<span class="sr-only">Error:</span> ${status}
	</div>

	<%
		}
	%>
</div>
<!-- /container -->