<%@include file="include.html"%>

<div class="page-header">
        <h1>Welcome Admin</h1>
        <h2>Pick an option</h2>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a data-toggle='modal' data-target='#myModal1' href='addAuthor'> Add Author</a></li>
            <li class="list-group-item"><a href="viewAuthor"> View Authors</a></li>
            <li class="list-group-item"><a href="addBook"> Add Book</a></li>
            <li class="list-group-item"><a href="viewBook"> View Books</a></li>
            <li class="list-group-item"><a data-toggle='modal' data-target='#myModal1' href="addGenre"> Add Genre</a></li>
            <li class="list-group-item"><a href="viewGenre"> View Genre</a></li>
          </ul>
        </div><!-- /.col-sm-4 -->
</div>

<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg">
		<div class="modal-content"></div>
	</div>
</div>