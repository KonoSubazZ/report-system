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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/js/highcharts/exporting.js"></script>
<script src="https://img.hcharts.cn/highcharts-plugins/highcharts-zh_CN.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js" role='reload'></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${pageContext.request.contextPath}/js/previewImage.js"></script>
<script src="${pageContext.request.contextPath}/js/tools.js"></script>
</head>
<body>
	<style>
		.label1{
			color:#000;float:left;padding: 7px;line-height: 20px;
		}
	</style>
	<form id="chartForm" class="form-x">
		<div class="panel admin-panel">
			<div class="panel-head">
				<strong class="icon-reorder"> 检测结果导出</strong>
			</div>
			<div class="padding border-bottom" style="margin-top: 0px; margin-bottom: 30px">
				<ul class="form-group" style="margin-left:-30px;margin-top: 3px;">
					<li class="label">产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品:</li>
					<li class="field"><input type="text" placeholder="请选择检测产品" data-validate="required:请选择检测产品" id="product_name" name="product_name" class="input w50" 
						style="width: 250px; line-height: 17px; display: inline-block" />
						<input type="hidden" id="product_id" name="product_id"/>
					</li>
					<li class="tips"></li>
					<script type="text/javascript">
				          $(function(){
				        	  $.post("${pageContext.request.contextPath}/autoComplete/getProductNameByUserId",  
				        			function(data){
				        			$('#product_name').autocomplete(data, {
				        				max : 20, //列表里的条目数
				        				minChars : 0, //自动完成激活之前填入的最小字符
				        				width : 288, //提示的宽度，溢出隐藏
				        				scrollHeight : 300, //提示的高度，溢出显示滚动条
				        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
				        				autoFill : false, //自动填充
				        				formatItem : function(row, i, max) {
				        					return row.name;
				        				},
				        				formatResult : function(row) {
				        					return row.name;
				        				}
				        			}).result(function(event, row, formatted) {
				        				$("#product_id").val(row.id);
				        			});
				        		},"json");
				          })
			          </script>
				</ul>
				<ul class="form-group" style="margin-left:-30px;margin-top: 3px;">
					<li class="label">送检机构:</li>
					<li class="field">
						<input type="text" placeholder="请输入搜索关键字" data-validate="required:请输入搜索关键字" id="customer" name="customer" class="input w50" style="width: 250px; line-height: 17px; display: inline-block" />
					</li>
					<script type="text/javascript">
				          $(function(){
				        	   $.post("${pageContext.request.contextPath}/autoComplete/getCustomer",  
				        			function(data){
				        			$('#customer').autocomplete(data, {
				        				max : 10, //列表里的条目数
				        				minChars : 0, //自动完成激活之前填入的最小字符
				        				width : 288, //提示的宽度，溢出隐藏
				        				scrollHeight : 300, //提示的高度，溢出显示滚动条
				        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
				        				autoFill : false, //自动填充
				        				formatItem : function(row, i, max) {
				        					return row.name;
				        				},
				        				formatResult : function(row) {
				        					return row.name;
				        				}
				        			}).result(function(event, row, formatted) {
										
				        			});
				        		},"json");
				          })
			          </script>
					<li class="tips"></li>
				</ul>
				<ul class="form-group" style="margin-left:-30px;margin-top: 3px;">
			        <li class="label">出报告起止时间: </li>
			        <li>
			          <input type="text" placeholder="起始日期" id="analysis_date_B" name="analysis_date_B" class="input w50" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px;cursor:pointer; line-height:17px;display:inline-block" />
			        	<span class="label1">至</span>
			        	<input type="text" placeholder="终止日期" id="analysis_date_N" name="analysis_date_N" class="input w50" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px; cursor:pointer; line-height:17px;display:inline-block" />
			        </li>
			     </ul>
			     <ul class="search" style="padding-left:10px;margin-top: 3px;">
			        <li style="margin-left: 300px;">
			          <a href="javascript:void(0)" class="button border-main icon-search" onclick="reportStat()" > 导出结果</a>
			       		<span id="message" style="color: red;font-size: 14px;margin-left:50px;"></span>
			        </li>
			        <!-- <li style="">
			          <a href="javascript:void(0)" class="button border-main icon-search" onclick="exportData()" >json数据补全</a>
			        </li> -->
			     </ul>
			</div>
		</div>
	</form>
	<script type="text/javascript">
		function reportStat(){
			var formData = $("#chartForm").serialize();
			$.post("${pageContext.request.contextPath}/sampleFile/ReportStatIsNull",formData,function(data){
				if(data){
					alert("数据为空");
				}else{
					window.open("${pageContext.request.contextPath}/sampleFile/ReportStat?"+formData)
				}
			});
		}
		function exportData(){
			var formData = $("#chartForm").serialize();
			$.post("${pageContext.request.contextPath}/sampleFile/ReportStatIsNull",formData,function(data){
				if(data){
					alert("数据为空");
				}else{
					$.post("${pageContext.request.contextPath}/resultExport/getResultExportData",formData,function(data){
						if(data){
							alert("数据补全成功！");
						}else{
							alert("数据补全失败！");
						}
					});
				}
			});
		}
	</script>
</body>
</html>