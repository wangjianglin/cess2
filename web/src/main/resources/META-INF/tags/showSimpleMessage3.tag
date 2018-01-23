<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- 在标签文件中定义属性title，必须属性 -->
<%@ attribute name="title" required="true"%>

<!-- 在标签文件夹中定义属性moreLinkTarget，必须属性 -->
<%@ attribute name="moreLinkTarget" required="true"%> 

<!--获取标签体内容 存于listMessage变量中 -->
<jsp:doBody var="listMessage" scope="page"/>

<style>
<!--
.style1{font-size: 12px}
-->
</style>
<table width="300" border="1" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC">
  <tr>
    <th>Column 1 Heading</th>
    <th>Column 2 Heading</th>
  </tr>
  <tr>
     <td width="296" bgcolor="#CCCCCC" class="style1" colspan="2">${title}<a href="${moreLinkTarget}">更多&gt;&gt;</a></td>
  </tr>
  <tr>
  	<td bgcolor="#FFFFCC" colspan="2">
  		<c:forEach var="item" items="${listMessage}">
  			<li><span class="style1">${item }</span></li>
  		</c:forEach>
  	</td>
  </tr>
</table>


