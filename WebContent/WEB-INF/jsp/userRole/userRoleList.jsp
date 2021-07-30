<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-cn"  style="min-width: 1720px;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<title></title>
<link rel="stylesheet" href="css/pintuer.css">
<link rel="stylesheet" href="css/admin.css">

<link rel="stylesheet" href="jquery/pagination/pagination.css" />
<script type="text/javascript" src="jquery/jquery-1.7.2.min.js"></script> 
<script type="text/javascript" src="jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="jquery/jquery.form.js"></script>
<script src="js/pintuer.js"></script>
<script type="text/javascript">
	$(function(){
		displayData(0);
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayData(this.value-1);
			}
		}); 
	});
	
	function displayData(pageNo){
		var pageSize=5;
		$.ajax({
			url:"${pageContext.request.contextPath}/userRole/getUserRoleByPage",
			type:"post",
			cache:false, //设置浏览器不缓存页面  
			data:{
				"pageNo":pageNo+1,
				"pageSize":pageSize
			},
			beforeSend:function(){
				$("#message").text("正在处理请稍等...");
				return true;
			},
			success:function(jsonObject){
				//清空内容
				$("#tInfo2").empty();
			    if(jsonObject.total==0){
					$("#message").text("没数据");
				}else{
					$("#message").text("");
					var htmlString="";
					$.each(jsonObject.dataList,function(i,n){
						htmlString += '<tr class="odd">';
						htmlString += '<td>'+(i+1)+'</td>';
						htmlString += '<td>'+n.user_role+'</td>';
						htmlString += '<td>'+n.user_role_chinese+'</td>';
						htmlString += '<td>'+n.created_by+'</td>';
						htmlString += '<td>'+n.created_date+'</td>';
						htmlString += '<td>'+n.update_date+'</td>';
						htmlString += '<td><div class="button-group"><a class="button border-main" href="${pageContext.request.contextPath}/userRole/editUserRole?id='+n.role_id+'"><span class="icon-edit"></span> 修改 </a></div></td>';
						htmlString += '</tr>';
						
					});
					//将上面拼接好的json字符串追加到tbody中
					$("#tInfo2").append(htmlString);
				} 
			  
			  //集成jquery的翻页插件
				$("#pagination").pagination(jsonObject.total, {//总记录条数
		            callback: displayData,//每次翻页的时候执行的回调函数  会自动传递当前页码索引   比正常页码小1
		            items_per_page:pageSize, // 每页显示多少条数据
		            current_page:pageNo,//当前页码索引
		            link_to:"javascript:void(0)",//保留超链接的样式，执行js代码   不跳转到任何资源
		            num_display_entries:3,//默认显示页码入口的个数
		            next_text:"下一页",
		            prev_text:"上一页",
		            next_show_always:true,//如果没有下一页是否显示连接
		            prev_show_always:true,//如果没有上一页是否显示连接
		            num_edge_entries:2,//页码较多的时候 可以用...省略
		            ellipse_text:"..."
		        });
				//获取总记录条数
				$("#total").text(jsonObject.total); 
				//显示总页数
				var pageCount = jsonObject.total%pageSize==0?jsonObject.total/pageSize:parseInt(jsonObject.total/pageSize)+1;
				$("#pageCount").text(pageCount);
			}
		});
	}
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 角色管理</strong> <a href="" style="float:right; display:none;">添加字段</a></div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <li> <a class="button border-main" href="${pageContext.request.contextPath}/userRole/addUserRole"><span class="icon-plus-square-o"></span> 添加角色 </a> </li>
      </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <th width="100" style="text-align:center; padding-left:8px;">序号</th>
        <th>角色名称</th>
        <th>角色中文名称</th>
        <th>创建人</th>
        <th>创建时间</th>
        <th>修改时间</th>
        <th>操作</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
															
	  </tbody>
        <tr>
          
      <tr>
     	<td colspan="8">
     		<table width="100%" height="30" border="0" cellpadding="0"
															cellspacing="0" class="page_table">
															<tr>
																<td width="8%" class="font_left">
																	数据:<span id="total"></span>条
																</td>
																<td width="478" class="font_right">
																	<div id="pagination"></div>
																</td>
															</tr>
														</table>
     	</td>
      </tr>
    </table>
  </div>
</form>
</body>
</html>