<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<title></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" ></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js" role='reload'></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${pageContext.request.contextPath}/js/previewImage.js"></script>
<script src="${pageContext.request.contextPath}/js/tools.js"></script>
<style type="text/css">
	td{vertical-align: middle;}
</style>
</head>
<body>
	<div class="panel admin-panel">
		<div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>导入样本信息</strong></div>
	  <div class="body-content" style="margin-left:100px">
	  <form id="reportFileForm" method="post" enctype="multipart/form-data">
	    <table style="width:100%">
	     <tr>
		     <td>
				<div class="form-group">
					<input type="file" id="filename" name="filename">
					<button id="updateReportFile"  style="width: 150px;" class="button bg-main icon-file-o" > 导入数据</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="button border-green" href="${pageContext.request.contextPath}/sampleFile/exportPcrFile"><span class="icon-download"></span> 导出样本模板</a>
				</div> 
			</td> 
	     </tr>
	     </table>
	  </form>
	  <script type="text/javascript">
        	$(function(){
        		$("#updateReportFile").click(function(){
        			if(confirm("确定导入数据吗？")){
	        			var filename = $("#filename").val();
	        			//alert(filename)
        				var from =document.getElementById("reportFileForm");
        				var formData = new FormData(from);  
        				$.ajax({
        					async: false, 
							cache: false,
							type: "POST",   
							url:"${pageContext.request.contextPath}/sampleFile/execute_inputSampleFile", 
							data:formData, 
							contentType: false,  
					        processData: false,
							dataType:"json",
							success:function(data){
								if(data){
									alert("导入数据成功！");
								}else{
									alert("导入数据失败！");
								}
							} 
						});
        			}
        		});
        	});
        </script>
	  </div>
	</div>
</body>
</html>
