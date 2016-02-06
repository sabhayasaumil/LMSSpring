<%@include file="include.html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<div class="page-header">
        <h1>Welcome Librarian</h1>
        <h2>Pick an option</h2>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a href="addGenre"> Add Genre</a></li>
            <li class="list-group-item"><a href="viewGenre"> View Genres</a></li>
            <li class="list-group-item"><a href="addBorrower"> Add Borrower</a></li>
            <li class="list-group-item"><a href="viewBorrower"> View Borrower</a></li>
            <li class="list-group-item"><a href="viewCopies"> View Copies</a></li>
            <li class="list-group-item"><a data-toggle='modal' data-target='#myModal1' href="addBranch"> Add Branch</a></li>
            <li class="list-group-item"><a href="viewBranch"> View Branch</a></li>
            <li class="list-group-item"><a href="viewBooksDue"> Books Due Today</a></li>
            <li class="list-group-item"><a href="viewAllBooksDue"> All Due Books</a></li>

          </ul>
        </div><!-- /.col-sm-4 -->
</div>


<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg">
		<div class="modal-content"></div>
	</div>
