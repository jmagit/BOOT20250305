<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ include file="../parts/header.jsp" %>

<dl>
	<dt><s:message code="entidad.form.id" /></dt>
	<dd>${elemento.actorId}</dd>
	<dt><s:message code="entidad.form.nombre" /></dt>
	<dd>${elemento.firstName}</dd>
	<dt><s:message code="entidad.form.apellidos" /></dt>
	<dd>${elemento.lastName}</dd>
</dl>
<p>
	<a href="/actores" class="btn btn-primary">Volver</a>
</p>
<%@ include file="../parts/footer.jsp" %>