<%@ include file="parts/header.jsp" %>
<h1>Actores</h1>
<p>
	<button type="button" onClick="pide(0)">1</button>
	<button type="button" onClick="pide(1)">2</button>
	<button type="button" onClick="pide(2)">3</button>
	<button type="button" onClick="pide(3)">4</button>
	<button type="button" onClick="pide(4)">5</button>
	<button type="button" onClick="pide(5)">6</button>
	<button type="button" onClick="pide(6)">7</button>
	<button type="button" onClick="pide(7)">8</button>
</p>

<ul id="rslt"></ul>

<%@ include file="parts/footerScripts.jsp" %>
<script type="text/javascript">
function pide(pag) {
	$('#rslt').empty();
	$.get('/api/actores?page='+pag).then(
			function(data) {
				var lst = eval(data);
				lst.forEach(function(item) {
					$('#rslt').append('<li>' + item.firstName + ' ' + item.lastName + '</li>');
				});
				
			}
			);
}

pide(0);
</script>

<%@ include file="parts/footerEnd.jsp" %>
