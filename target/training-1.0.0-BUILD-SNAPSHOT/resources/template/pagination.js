$(".paginationClass").click(function() {
	$.ajax({
		url : this.attr("href"),
		success : function(result) {
			$("#pageData").html(result);
		}
	});
});

$(".paginationClass").first().trigger("click");