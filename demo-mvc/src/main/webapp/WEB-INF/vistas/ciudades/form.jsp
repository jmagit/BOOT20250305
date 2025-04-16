<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ include file="../parts/header.jsp" %>
<sf:form modelAttribute="elemento" action="${pageContext.request.contextPath}/${action}">
	<div class="mb-3">
		<sf:label path="cityId" cssClass="form-label"><s:message code="entidad.form.id" /></sf:label>
		<c:if test = '${modo == "add"}'>
			<sf:input path="cityId" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
			<sf:errors path="cityId" cssClass="invalid-feedback" />
		</c:if>
		<c:if test = '${modo != "add"}'>
			<sf:input path="cityId" cssClass="form-control-plaintext" readonly="readonly" />
		</c:if>		
	</div>
	<div class="mb-3">
		<sf:label path="city" cssClass="form-label"><s:message code="entidad.form.nombre" /></sf:label>
		<sf:input path="city" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
		<sf:errors path="city" cssClass="invalid-feedback" />
	</div>
	<div class="mb-3">
		<sf:label path="countryId" cssClass="form-label">País</sf:label>
		<sf:select path="countryId" cssClass="form-control" cssErrorClass="is-invalid form-control">
			<sf:options items="${paises}" itemValue="countryId" itemLabel="country"/>
		</sf:select>
		<sf:errors path="countryId" cssClass="invalid-feedback" />
	</div>
	<div class="mb-3">
		<input type="submit" value="<s:message code="form.enviar" />" class="btn btn-primary">
		<a href="/ciudades" class="btn btn-primary" ><s:message code="form.volver" /></a>
	</div>
</sf:form>
<%@ include file="../parts/footer.jsp" %>
