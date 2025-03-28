<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ include file="../parts/header.jsp" %>
<sf:form modelAttribute="elemento" action="${pageContext.request.contextPath}/${action}">
	<div class="form-group">
		<sf:label path="cityId"><s:message code="actores.form.id" /></sf:label>
		<c:if test = '${modo == "add"}'>
			<sf:input path="cityId" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
			<sf:errors path="cityId" cssClass="invalid-feedback" />
		</c:if>
		<c:if test = '${modo != "add"}'>
			<sf:input path="cityId" cssClass="form-control-plaintext" readonly="readonly" />
		</c:if>		
	</div>
	<div class="form-group">
		<sf:label path="city"><s:message code="actores.form.nombre" /></sf:label>
		<sf:input path="city" cssClass="form-control" cssErrorClass="is-invalid form-control"/>
		<sf:errors path="city" cssClass="invalid-feedback" />
	</div>
	<div class="form-group">
		<sf:label path="countryId">Pais:</sf:label>
		<sf:select path="countryId" cssClass="form-control" cssErrorClass="is-invalid form-control">
			<sf:options items="${paises}" itemValue="countryId" itemLabel="country"/>
		</sf:select>
		<sf:errors path="countryId" cssClass="invalid-feedback" />
	</div>
	<div class="form-group">
		<input type="submit" value="<s:message code="form.enviar" />" class="btn btn-primary">
		<a href="/ciudades" class="btn btn-primary" ><s:message code="form.volver" /></a>
	</div>
</sf:form>
<%@ include file="../parts/footer.jsp" %>
