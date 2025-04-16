<%@ include file="parts/header.jsp" %>
<c:url value="/mylogin" var="loginUrl" />
<form action="${loginUrl}" method="post">
	<c:if test="${param.error != null}">
		<div class="alert alert-danger">Invalid username and password.</div>
	</c:if>
	<c:if test="${param.logout != null}">
		<div class="alert alert-info">You have been logged out.</div>
	</c:if>
	<div class="mb-3">
		<label for="username" class="form-label">Username</label>
		<input type="text" id="username" name="username" class="form-control"/>
	</div>
	<div class="mb-3">
		<label for="password" class="form-label">Password</label>
		<input type="password" id="password" name="password" class="form-control"/>
	</div>
	<div class="mb-3">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<input type="submit" class="btn btn-primary">
	</div>
</form>

<%@ include file="parts/footerEnd.jsp" %>
