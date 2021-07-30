<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<title></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/alertBox.css">
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
		var pageSize=10;
		
			$.ajax({
				url:"${pageContext.request.contextPath}/falsePositive/getfalsePositiveByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"gene":$("#gene").val()
				},
				beforeSend:function(){
					$("#msg").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					$("#tInfo2").empty();
				    if(jsonObject.total==0){
						$("#msg").text("没数据");
					}else{
						$("#msg").text("");
						var htmlString="";
						$.each(jsonObject.dataList,function(i,n){
							htmlString += '<tr class="odd">';
							htmlString += '<td>'+n.id+'</td>';
							htmlString += '<td>'+n.platform+'</td>';
						    htmlString += '<td>'+n.gene+'</td>'; 
							htmlString += '<td>'+n.chrom+'</td>';
							htmlString += '<td>'+n.start+'</td>';
							htmlString += '<td>'+n.end+'</td>';
							//htmlString += '<td>'+n.aachange+'</td>';
							htmlString += '<td><span style="cursor:pointer" onclick="openTextRead('+"'"+"aachange"+i+"'"+');">';
							if(n.aachange != null){
								 if((n.aachange).length>24){
									var str = (n.aachange).substring(0,24)+"....";
									htmlString +=str;
								}else{
									htmlString +=n.aachange;
								} 
							}
							htmlString +='</span> <div id="aachange'+i+'" style="display:none;">'+n.aachange+'</div></td>';
							htmlString += '<td>'+n.matching_table+'</td>';
							htmlString += '<td>'+n.created_by+'</td>';
							htmlString += '<td>'+n.created_date+'</td>';
							htmlString += '<td><div class="button-group"><a class="button border-main" onclick="editOne('+n.id+','+pageNo+');">修改   </a>&nbsp;&nbsp;&nbsp;<a class="button border-main" onclick="deleteOne('+n.id+','+pageNo+');">删除   </a></div></td>';
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
	
	function editOne(id,pageNo) {
		location.href="${pageContext.request.contextPath}/falsePositive/editFalsePositive?id="+id;
	}
	
	function deleteOne(id,pageNo){
		if(confirm("是否删除")){
			$.ajax({
				url:"${pageContext.request.contextPath}/falsePositive/deletefalsePositive",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"id":id,
				},
				beforeSend:function(){
					$("#message").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					if(jsonObject){
						alert("删除成功");
						displayData(pageNo);
					}else{
						alert("删除失败");
					}
				}
			});
		}
	}
	
	function openTextRead(id){
		//alert($("#"+id).text());
		var iMask = $("<div></div>").addClass("iMask");
		iMask.css("width",$(window).width() );
		iMask.css("height",$(window).height() );
		iMask.appendTo("body");
		var iBox = $("<div></div>").addClass("iBox1");
		iBox.appendTo("body");
		var iTextarea = $('<textarea rows="3" cols="20" style="background-color: #fff;" readonly="readonly"></textarea>').addClass("iText1");
		iTextarea.appendTo(iBox);
		var iBtn = $("<button type='button'>关闭</button>").addClass("iBtn");
		iBtn.appendTo(iBox);
		iTextarea.val($("#"+id).text());
		iBtn.click(function(){
			iBox.remove();
			iMask.remove();
		});		
	}
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel" style="overflow-x: auto; overflow-y: auto; height: 700px; width:1730px;">
    <div class="panel-head"><strong class="icon-reorder"> 假阳性</strong> </div>
    <div class="padding border-bottom">
    	<ul class="search" style="padding-left:10px;">
    		<li> <a class="button border-blue icon-plus-square-o" href="${pageContext.request.contextPath}/falsePositive/addFalsePositive"> 添加假阳</a></li>
	    	<li>基因:</li>
	        <li>
	          <input type="text" placeholder="请输入搜索关键字" id="gene" name="gene" class="input" style="width:200px; line-height:17px;display:inline-block" />
	          <script type="text/javascript">
	          	$(function(){
	          	  $.post("${pageContext.request.contextPath}/autoComplete/getFalsePositiveIsGeneList", function(data){
	          			$('#gene').autocomplete(data, {
	          				max : 12, //列表里的条目数
	          				minChars : 0, //自动完成激活之前填入的最小字符
	          				width : 200, //提示的宽度，溢出隐藏
	          				scrollHeight : 300, //提示的高度，溢出显示滚动条
	          				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	          				autoFill : false, //自动填充
	          				formatItem : function(row, i, max) {
	          					if(row!=null){
		          					return ""+row.name;
	          					}else{
		          					return "";
	          					}
	          				}
	          			});
	          		},"json");
	            })
	          </script>
	        </li>
	        <li>
        	<a href="javascript:;" class="button border-main icon-search" onclick="displayData(0);" > 搜索</a>
       		<span id="msg" style="color: red;font-size: 14px"></span>
        </li>
        </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <tr>
        <th>id</th>
        <th>platform</th>
        <th>gene</th>
        <th>chrom</th>
        <th>start</th>
        <th>end</th>
        <th>aachange</th>
        <th>matching_table</th>
        <th>created_by</th>
        <th>created_date</th>
        <th>操作</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
															
	  </tbody>
      <tr>
     	<td colspan="25">
     		<table width="100%" height="30" border="0" cellpadding="0" cellspacing="0" class="page_table">
				<tr>
					<td width="8%" class="font_left">数据:<span id="total"></span>条</td>
					<td width="478" class="font_right"><div id="pagination"></div></td>
				</tr>
			</table>
     	</td>
      </tr>
    </table>
  </div>
</form>
</body>
</html>