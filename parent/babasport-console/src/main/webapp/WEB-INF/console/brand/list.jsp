<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>babasport-list</title>
<script>
//全选全不选
function checkBox(name,checked)
{
	$("input[name='"+name+"']").attr("checked",checked);
}

//批量删除品牌
function optDelete()
{
	//删除用户勾选的品牌
	
	//获得用户选择的数量
	var size = $("input[name='ids']:checked").size();
	
	//alert(size);
	
	if(size==0)
	{
		alert("请选择：");
		return;
	}
	
	if(!confirm("确定删除吗？"))
	{
		return;
	}
	
	//真的要删除了
	
	//提交表单
	$("#formList")[0].action = "doDelete.do";
	$("#formList").submit();
	
	
}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 品牌管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="button" value="添加" onclick="javascript:window.location.href='add.jsp'"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
<form action="/console/brand/list.do" method="get" style="padding-top:5px;">
品牌名称: <input type="text" name="name" value="${name}"/>
	<select name="isDisplay">
		<option value="1">是</option>
		<option value="0">否</option>
	</select>
	
	<script>
		$("select[name='isDisplay']").val('${isDisplay}');
	</script>
	
	<input type="submit" class="query" value="查询"/>
</form>

<form id="formList" method="post">

<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="checkBox('ids',this.checked)"/></th>
			<th>品牌ID</th>
			<th>品牌名称</th>
			<th>品牌图片</th>
			<th>品牌描述</th>
			<th>排序</th>
			<th>是否可用</th>
			<th>操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
	
	<c:forEach items="${pageBrands.result}" var="brand">
	
		<tr bgcolor="#ffffff" onmouseout="this.bgColor='#ffffff'" onmouseover="this.bgColor='#eeeeee'">
			<td><input type="checkbox" value="${brand.id}" name="ids"/></td>
			<td align="center">${brand.id}</td>
			<td align="center">${brand.name}</td>
			<td align="center"><img width="40" height="40" src="${brand.imgUrl}"/></td>
			<td align="center">
			
			<c:out value="${brand.description}"></c:out>
			
			
			</td>
			<td align="center">${brand.sort}</td>
			<td align="center">
			
			<c:if test="${brand.isDisplay==1}">是</c:if>
			<c:if test="${brand.isDisplay==0}">否</c:if>
		
			</td>
			<td align="center">
			<a class="pn-opt" href="showEdit.do?brandId=${brand.id}">修改</a> 
			| 
			<a class="pn-opt" onclick="if(!confirm('您确定删除吗？')) {return false;}" href="#">删除</a>
			</td>
		</tr>
	
	</c:forEach>
	
	</tbody>
</table>

</form>


<div class="page pb15">
	<span class="r inb_a page_b">
	
		<font size="2"><a href="list.do?pageNum=1">首页</a></font>
	
		<font size="2"><a href="list.do?pageNum=${pageBrands.pageNum-1}">上一页</a></font>
	
		
		<c:forEach begin="1" end="${pageBrands.pages}" var="pn">
			
		<c:if test="${pn==pageBrands.pageNum}"><strong>${pn}</strong></c:if>
		
		<c:if test="${pn!=pageBrands.pageNum}"><strong><a href="list.do?pageNum=${pn}">
		${pn}</a></strong></c:if>
			
		</c:forEach>
		
	
		
		<font size="2"><a href="list.do?pageNum=${pageBrands.pageNum+1}&name=${name}&isDisplay=${isDisplay}">下一页</a></font>
	
		<a href="list.do?pageNum=${pageBrands.pages}"><font size="2">尾页</font></a>
	
		共<var>${pageBrands.pages}</var>页 到第<input type="text" size="3" id="PAGENO"/>页 <input type="button" onclick="javascript:window.location.href = '/product/list.do?&amp;isShow=0&amp;pageNo=' + $('#PAGENO').val() " value="确定" class="hand btn60x20" id="skip"/>
	
	</span>
</div>
<div style="margin-top:15px;">
<input class="del-button" type="button" value="删除" onclick="optDelete();"/>
</div>
</div>
</body>
</html>