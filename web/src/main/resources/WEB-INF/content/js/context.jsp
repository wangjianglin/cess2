<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

StringBuffer buffer = new StringBuffer();

buffer.append("{");
buffer.append("ContextPath:\"");
buffer.append(request.getContextPath());
buffer.append("\",ServerName:\"");
buffer.append(request.getServerName());
buffer.append("\",ServerPort:\"");
buffer.append(request.getServerPort());
buffer.append("\",LocalAddr:\"");
buffer.append(request.getLocalAddr());
buffer.append("\",LocalName:\"");
buffer.append("\",LocalPort:\"");
buffer.append(request.getLocalPort());
buffer.append("\",Protocol:\"");
buffer.append(request.getProtocol());
buffer.append("\",PathInfo:\"");
buffer.append(request.getPathInfo());
buffer.append("\",RemoteAddr:\"");
buffer.append(request.getRemoteAddr());
buffer.append("\",RemoteHost:\"");
buffer.append(request.getRemoteHost());
buffer.append("\",RemotePort:\"");
buffer.append(request.getRemotePort());
buffer.append("\",RemoteUser:\"");
buffer.append(request.getRemoteUser());
buffer.append("\"");

buffer.append("}");


%>
<%=buffer.toString()%>