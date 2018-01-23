<%@page import="lin.CessException"%>
<%@page import="java.io.PrintStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@ page language="java"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%
// contentType="application/json; charset=UTF-8"
	exception.printStackTrace(System.out);
	//response.setContentType("application/json;charset=UTF-8");
	
	//out.println(exception.getMessage());
	response.setStatus(200);
	String cause = exception.getCause() == null?"":exception.getCause().toString();
	ByteArrayOutputStream sOut = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(sOut);
	exception.printStackTrace(ps);
	String stacktrace = sOut.toString();
	StackTraceElement[] stack = exception.getStackTrace();
	
	if(cause == null){
		cause = "";
	}
	cause = cause.replace("'","\\'").replace("\"","\\\"").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r");
	
	stacktrace = stacktrace.replace("'","\\'").replace("\"","\\\"").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r");
	long code = -1l;
	System.out.println("code:"+code);
 	if(exception instanceof CessException){
 		CessException le = (CessException)exception;
		code = le.getCode();
		if(code == 0){
			code = -1l;
		}
	}

	String message = null;
	message = CessException.getMessage(code);
	if(message == null){
		message = exception.getMessage();
	}
	if(message == null){
		message = "";
	}
	message = message.replace("'","\\'").replace("\"","\\\"").replace("\\","\\\\").replace("\n","\\n").replace("\r","\\r").replace("\r","\\r");
	
 	StringBuffer buffer = new StringBuffer();
 	buffer.append("{\"code\":");
 	buffer.append(code);
 	buffer.append(",\"message\":\"");
 	buffer.append(message);
 	buffer.append("\",\"cause\":\"");
 	buffer.append(cause);
 	buffer.append("\",\"stackTrace\":\"");
 	buffer.append(stacktrace);
 	buffer.append("\",\"result\":null}");
 	String json= buffer.toString();
 	System.out.println("json:"+json);
 	json = new sun.misc.BASE64Encoder().encodeBuffer(json.getBytes());
    json =  java.net.URLEncoder.encode(json, "ascii");
%>
<%=json %>
