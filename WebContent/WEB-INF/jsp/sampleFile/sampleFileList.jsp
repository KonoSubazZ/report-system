<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn" style="min-width: 1720px;">
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
		if($("#subbarcode").val() == "" && $("#disease_type").val() == ""){
			$("#msg").text("请选择筛选条件");
		}else{
			$("#msg").text("");
			$.ajax({
				url:"${pageContext.request.contextPath}/sampleFile/getsampleFileByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"subbarcode":$("#subbarcode").val(),
					"disease_type":$("#disease_type").val(),
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
							htmlString += '<td>'+n.sample_id  +'</td>';
							htmlString += '<td>'+n.subbarcode+'</td>';
							htmlString += '<td>'+n.client  +'</td>';
						    htmlString += '<td>'+n.sales_contact+'</td>'; 
							/* htmlString += '<td>'+n.hospital+'</td>'; */
							htmlString += '<td>'+n.commission_date.toString().substring(0,10) +'</td>';
							htmlString += '<td>'+n.received_date.toString().substring(0,10)+'</td>';
							htmlString += '<td>'+n.report_receiver+'</td>';
							htmlString += '<td>'+n.person_name+'</td>';
							htmlString += '<td>'+n.gender+'</td>';
							htmlString += '<td>'+n.birthday.toString().substring(0,10)+'</td>';
							htmlString += '<td>'+n.disease_type+'</td>';
							htmlString += '<td>'+n.specimen_type+'</td>';
							htmlString += '<td>'+n.specimen_quantity+'</td>';
							htmlString += '<td>'+n.hospital+'</td>';
							htmlString += '<td>'+n.collect_date+'</td>';
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
	}
	
	function refulshLims(){
		$.ajax({
			url:"${pageContext.request.contextPath}/sampleFile/RefulshLims",
			type:"post",
			cache:false, //设置浏览器不缓存页面  
			beforeSend:function(){
				$("#msg2").text("正在处理请稍等...");
				return true;
			},
			success:function(data){
				if (data) {
					displayData(0);
					$("#msg2").text("数据更新成功！");
				}else{
					$("#msg2").text("数据更新失败！");
				}
			}
		},"json");
	}
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 样本临床信息</strong> </div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <c:if test="${ user.role_id != 10 }">
        	<li> <a class="button border-blue icon-plus-square-o" href="${pageContext.request.contextPath}/sampleFile/addSampleFile"> 添加样本</a></li>
        </c:if>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;样本 ID:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="subbarcode" name="subbarcode"  class="input" style="width:250px; line-height:17px;display:inline-block" />
          <script type="text/javascript">
          $(function(){
        	  $.post("${pageContext.request.contextPath}/autoComplete/getSubBarcodeList", function(data){
        			$('#subbarcode').autocomplete(data, {
        				max : 12, //列表里的条目数
        				minChars : 0, //自动完成激活之前填入的最小字符
        				width : 250, //提示的宽度，溢出隐藏
        				scrollHeight : 300, //提示的高度，溢出显示滚动条
        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
        				autoFill : false, //自动填充
        				formatItem : function(row, i, max) {
        					return ""+row.subbarcode;
        				}
        			});
        		},"json");
          })
          
          </script>
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 疾病种类:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="disease_type" name="disease_type" class="input" style="width:250px; line-height:17px;display:inline-block" />
          <script type="text/javascript">
          $(function(){
        	  $.post("${pageContext.request.contextPath}/autoComplete/getDiseaseTypeList", function(data){
        			$('#disease_type').autocomplete(data, {
        				max : 12, //列表里的条目数
        				minChars : 0, //自动完成激活之前填入的最小字符
        				width : 250, //提示的宽度，溢出隐藏
        				scrollHeight : 300, //提示的高度，溢出显示滚动条
        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
        				autoFill : false, //自动填充
        				formatItem : function(row, i, max) {
        					return row.disease_type;
        				}
        			});
        		},"json");
          })
          
          </script>
        </li>
        <li>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData(0);" > 搜索</a>
          	&nbsp;&nbsp;&nbsp;&nbsp;
       		<span id="msg" style="color: red;font-size: 14px"></span>
        </li>
        <c:if test="${ user.role_id != 10 }">
	        <li style="margin-left: 100px;">
	           <button id="rflims" class="button border-main icon-search-plus" onclick="refulshLims();" type="button"  style=<c:if test="${user.role_id != 6 }">"display: none;"</c:if>> 更新LIMS</button>
	        </li>
        </c:if>
        <li style="float: right;margin-right: 200px">
       		<span id="msg2" style="color: red;font-size: 14px"></span>
        </li>
      </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <th>序号</th>
        <th>样本 ID</th>
        <th>委托人</th>
        <th>联系人</th>
      <!--   <th>送检单位</th> -->
        <th>委托日期</th>
        <th>接收日期</th>
        <th>接收人</th>
        <th>姓名</th>
        <th>性别</th>
        <th>出生日期</th>
        <th>疾病种类</th>
        <th>样品种类</th>
        <th>样本量</th>
        <th>送检医院</th>
        <th>送检日期</th>
        
      </tr>
      <tr>
      <tbody id="tInfo2">
															
	  </tbody>
      <tr>
     	<td colspan="15">
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