<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <%@ page import="java.util.*"%>
   <%@ page import="java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head><meta charset="UTF-8" />
	<title>QUnit Test Suite</title>
	<link rel="stylesheet" href="qunit/qunit.css">
	<script src="qunit/qunit.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="../extjs-4.1.1/ext-all-dev.js"></script>
			
			<script src="../js/http.js"></script>
<!--script src="../js/http.js"></script-->


    <%!
    List<File> getTestFiles(File file,String name){
    	List<File> files =  null;
    	if(file.isDirectory()){
    		files = new ArrayList<File>();
    		List<File> tmpList = null;
    		for(File tmp:file.listFiles()){
    			tmpList = getTestFiles(tmp,name);
    			if(tmpList != null && tmpList.size() > 0){
    				files.addAll(tmpList);
    			}
    		}
    	}
    	
    	if(file.getName().endsWith(name)){
    		files = new ArrayList<File>();
    		files.add(file);
    	}
    	return files;
    }
    %>
<%

File file = new File(session.getServletContext().getRealPath("/WEB-INF/content/"));
String name = getInitParameter("js-test");
if(name == null || "".equals(name)){
	name = "-test.js";
}

String incName = getInitParameter("js-include-name");
if(incName == null || "".equals(incName)){
	incName = "-test.inc";
}
int start = file.getAbsolutePath().length() + 1;

List<File> incs = getTestFiles(file,incName);
String jsFileName = null;
for(File tmp : incs){
	
	//if(tmp.getName().equals(incName)){
		
		BufferedReader reader = new BufferedReader(new FileReader(tmp));
		String jsIncFileName = null;
		jsFileName = tmp.getParent().substring(start);
		jsFileName = jsFileName.replace('\\', '/');
		//jsIncFileName = reader.readLine();
		while((jsIncFileName = reader.readLine()) != null){
			//while(jsIncFileName != null){
			
			%>
			<script src="../<%=jsFileName%>/<%=jsIncFileName%>"></script>
			<%
			//jsIncFileName = reader.readLine();
			//System.out.println("inc filename:" + jsIncFileName);
		}
	}

List<File> list = getTestFiles(file, name);
for(File tmp : list){
	
	
	jsFileName = tmp.getAbsolutePath().substring(start);
	if(!jsFileName.startsWith("qunit" + System.getProperty("file.separator"))){
		jsFileName = jsFileName.replace('\\', '/');
	%>
	
	<script src="../<%=jsFileName%>"></script>
	<%
	}
}

%>
</head>
<body>
<div id="qunit"></div>
	<div id="qunit-fixture">test markup</div>
</body>
</html>