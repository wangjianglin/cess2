<%@page import="java.io.PrintStream"%>
<%@page import="lin.CessException" %>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@ page language="java" 
    pageEncoding="UTF-8" isErrorPage="true"%>
    
    
   <%

// contentType="application/json; charset=UTF-8"
	//exception.printStackTrace(System.out);
	//response.setContentType("application/json;charset=UTF-8");
	
	//out.println(exception.getMessage());
	response.setStatus(200);
	
	//String cause = exception.getCause() == null?"":exception.getCause().toString();
		
 	StringBuffer buffer = new StringBuffer();
 	buffer.append("{\"code\":");
 	buffer.append(0x20404);
 	buffer.append(",\"message\":");
 	buffer.append("\"页面不存在！\"");
 	buffer.append(",\"cause\":");
 	buffer.append("\"页面不存在！\"");
 	buffer.append(",\"stackTrace\":null");
 	//buffer.append(null);
 	buffer.append("}");
 	String json= buffer.toString();
 	json = new sun.misc.BASE64Encoder().encodeBuffer(json.getBytes());
    json =  java.net.URLEncoder.encode(json, "ascii");
    //System.out.println("json:"+json);
%>
<%=json %>
