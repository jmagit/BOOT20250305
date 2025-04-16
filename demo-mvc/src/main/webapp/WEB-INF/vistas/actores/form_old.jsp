<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ include file="../parts/header.jsp" %>
<sf:form modelAttribute="elemento" action="${pageContext.request.contextPath}/${action}">
	<div class="mb-3">
		<sf:label path="actorId" cssClass="form-label">C�digo:</sf:label>
		<c:if test = '${modo == "add"}'>
			<sf:input path="actorId" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
			<sf:errors path="actorId" cssClass="invalid-feedback" />
		</c:if>
		<c:if test = '${modo != "add"}'>
			${elemento.actorId}
		</c:if>		
	</div>
	<div class="mb-3">
		<sf:label path="firstName" cssClass="form-label">Nombre:</sf:label>
		<sf:input required="required" path="firstName" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
		<sf:errors path="firstName" cssClass="invalid-feedback" />
	</div>
	<div class="mb-3">
		<sf:label path="lastName" cssClass="form-label">Apellidos:</sf:label>
		<sf:input path="lastName" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
		<sf:errors path="lastName" cssClass="invalid-feedback" />
	</div>
	<div class="mb-3">
		<sf:label path="lastUpdate" cssClass="form-label">Fecha:</sf:label>
		<sf:input type="date" path="lastUpdate" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
		<sf:errors path="lastUpdate" cssClass="invalid-feedback" />
	</div>
	<div class="mb-3">
		<input type="submit" value="Enviar" class="btn btn-primary">
		<a href="/actores" class="btn btn-primary" >Volver</a>
	</div>
</sf:form>
<%@ include file="../parts/footer.jsp" %>
