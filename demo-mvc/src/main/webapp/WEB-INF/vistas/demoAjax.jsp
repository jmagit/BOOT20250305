<%@ include file="parts/header.jsp" %>
<h1>Actores</h1>
<nav aria-label="Page navigation">
  <ul id="paginador" class="pagination"></ul>
</nav>

<ul id="rslt"></ul>

<%@ include file="parts/footerScripts.jsp" %>
<script type="text/javascript">
function pide(pag) {
	$('#rslt').empty();
	$('#paginador').empty();
	$.get('/api/actores?page='+pag).then(
			function(data) {
				if(data.totalPages === 0) return
				var lst = data.content;
				lst.forEach(function(item) {
					$('#rslt').append('<li>' + item.firstName + ' ' + item.lastName + '</li>');
				});
				if(data.totalPages === 1) return
				for(let i = 1; i <= data.totalPages; i++) {
					var btn = $('<button>').addClass('page-link').text(i).on('click', function(ev) { pide(+ev.target.textContent-1) })
					var li = $('<li>').addClass('page-item').append(btn)
					if(i === data.number + 1) li.addClass('active')
					$('#paginador').append(li)
				}
			}
		);
}

pide(0);
</script>

<%@ include file="parts/footerEnd.jsp" %>
