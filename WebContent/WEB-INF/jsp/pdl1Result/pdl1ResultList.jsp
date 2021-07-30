<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
		//$("#message").text("");
		$.ajax({
			url:"${pageContext.request.contextPath}/pdl1Result/getPdl1ResultByPage",
			type:"post",
			cache:false, //设置浏览器不缓存页面  
			data:{
				"pageNo":pageNo+1,
				"pageSize":pageSize,
				"report_date_B":$("#report_date_B").val(),
				"report_date_N":$("#report_date_N").val(),
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
						htmlString += '<td>'+n.result_id+'</td>';
						htmlString += '<td>'+n.report_id+'</td>';
						htmlString += '<td>'+n.barcode+'</td>';
						htmlString += '<td>'+n.subbarcode+'</td>';
						htmlString += '<td>'+n.disease_type+'</td>';
						htmlString += '<td>'+n.tumor_expression_result+'</td>';
						htmlString += '<td>'+n.tumor_expression_pct+'</td>';
						htmlString += '<td>'+n.tumor_cell_dying+'</td>';
						htmlString += '<td>'+n.immuno_expression_result+'</td>';
						htmlString += '<td>'+n.immuno_expression_pct+'</td>';
						htmlString += '<td>'+n.immuno_cell_dying+'</td>';
						htmlString += '<td>'+n.tested_date+'</td>';
						htmlString += '<td>'+n.tested_by+'</td>';
						htmlString += '<td>'+n.checked_date+'</td>';
						htmlString += '<td>'+n.checked_by+'</td>';
						htmlString += '<td>'+n.report_date+'</td>';
						/* htmlString += '<td><div class="button-group"><a href="${pageContext.request.contextPath}/PCR/download?report_id='+n.report_id+'">'+n.report_filename+'</a></div></td>'; */
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
		            num_display_entries:5,//默认显示页码入口的个数
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
    <div class="panel-head"><strong class="icon-reorder"> PDL1结果</strong> </div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <li>&nbsp;&nbsp;&nbsp;&nbsp; 报告时间:&nbsp;&nbsp; 从</li>
        <li>
          <input type="text" placeholder="选择日期" id="report_date_B" name="report_date_B" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px;cursor:pointer; line-height:17px;display:inline-block" />
        </li>
        <li>至</li>
        <li>
          <input type="text" placeholder="选择日期" id="report_date_N" name="report_date_N" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px; cursor:pointer; line-height:17px;display:inline-block" />
        </li>
        <li>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData(0);" > 搜索</a>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
       		<span id="message" style="color: red;font-size: 14px"></span>
        </li>
      </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <th>结果编号</th>
        <th>报告编号</th>
        <th>患者筛选号</th>
        <th>样本编号</th>
        <th>疾病种类</th>
        <th>肿瘤细胞表达</th>
        <th>表达百分比（%）</th>
        <th>染色强度</th>
        <th>浸润免疫细胞表达</th>
        <th>表达百分比（%）</th>
        <th>染色强度</th>
        <th>检测时间</th>
        <th>检测人</th>
        <th>复核时间</th>
        <th>复核人</th>
        <th>报告时间</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
															
	  </tbody>
      <tr>
     	<td colspan="16">
     		<table style="width: 100%;height: 30px;border: 0;" class="page_table">
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