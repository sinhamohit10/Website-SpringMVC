<%@ include file="/WEB-INF/jsp/include.jsp" %>

<%
    			java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
    			java.util.Date current = new java.util.Date();
    			
    			String dateString = (String)request.getAttribute("date");
    			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MM-yyyy");
    			
    			String currentDateString = formatter.format(current);
    			java.util.Date currentDate = formatter.parse(currentDateString);
    			
    			request.getAttribute("date");
    			java.util.Date date = formatter.parse(dateString);
    			
    			java.util.Calendar calendar = java.util.Calendar.getInstance();
  		  		calendar.setTime(date);
  		  		calendar.add(java.util.Calendar.DAY_OF_YEAR, Integer.parseInt((String)request.getAttribute("limit")));
  		  		
  		  		java.util.Date limitDate = formatter.parse(formatter.format(calendar.getTime()));
  		  		
  		  		boolean isLimitExceded = false;
  		  		boolean isOnLimit = false;
  		  		
  		  		
  		  		
  		  		if(currentDate.after(limitDate)){
  		  			isLimitExceded = true;
  		  		} else if(limitDate.equals(currentDate)) {
  		  			isOnLimit = true;
  		  		}
  		  		
  		  		
  		  		request.setAttribute("isLimitExceded", isLimitExceded);
  		  		request.setAttribute("isOnLimit", isOnLimit);
  		  		
  		  		calendar.setTime(date);
  		  		calendar.add(java.util.Calendar.DAY_OF_YEAR, Integer.parseInt((String)request.getAttribute("deliverylimit")));
  		  		  		
  		  		java.util.Date deliveryDate = formatter.parse(formatter.format(calendar.getTime()));
  				request.setAttribute("deliveryDate", deliveryDate);
    		%>