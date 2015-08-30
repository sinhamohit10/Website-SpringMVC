<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:choose>
	<c:when test="${custAlt }">
		<c:choose>
			<c:when test="${not empty order.qcInstructions && order.qcInstructions != 'null'}">
				Customer Alteration - System Alteration
			</c:when>
			<c:otherwise>
				Customer Alteration			
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${not empty order.qcInstructions && order.qcInstructions != 'null'}">
		System Alteration
	</c:when>
	<c:otherwise>
		New
	</c:otherwise>
</c:choose>