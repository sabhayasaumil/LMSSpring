$(document).ready(function() {
	$(".paginationClass").click(function() {
		$.ajax({
			url : $(this).data("href"),
		}).done(function(data) {
			$("#pageData").html(data);
		});
	});

$(".paginationClass").first().trigger('click');
});
