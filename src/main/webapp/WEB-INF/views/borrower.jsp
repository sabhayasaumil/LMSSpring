<%@include file="include.html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<div class="page-header">
        <h1>Welcome Borrower</h1>
        <h2>Pick an option</h2>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a href="viewBook"> View Book</a></li>
            <li class="list-group-item"><a href="viewAllBooksDue"> Return a Book</a></li>
            <li class="list-group-item"><a href="viewBorrower"> Select Borrower</a></li>
          </ul>
        </div><!-- /.col-sm-4 -->
</div>