<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<title></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/myAlert.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" ></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/lib/layui/css/layui.css" />
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/myAlert.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/lib/layui/layui.js"></script>
<style type="text/css">
	td{vertical-align: middle;}
	#dele:hover{background:red;}
</style>
</head>
<body>
	<div class="panel admin-panel">
		<div class="panel-head">
			<strong class="icon-reorder">
				NGS报告管理>审核报告:${currentNgsAvailableData.report_id}:${currentNgsAvailableData.platform }>${currentNgsAvailableData.analysis_date }>${currentNgsAvailableData.subbarcode }>${currentNgsAvailableData.product_name }
			</strong>
		</div>
	  	<div class="body-content" style="margin-left:100px;font-size:16px;">
		  <%--表单展示详情--%>
			  <div class="layui-row" style="padding:20px 0;">
				  <div class="layui-col-xs5" style="display: flex;">
					  <div style="width:100px">姓名</div>
					  <div>${sampleFile.client}</div>
				  </div>
				  <div class="layui-col-xs5" style="display: flex;">
					  <div style="width:100px">样本编号</div>
					  <div>${sampleFile.subbarcode}</div>
				  </div>
			  </div>
			  <div class="layui-row" style="padding:20px 0;">
				  <div class="layui-col-xs5" style="display: flex;">
					  <div style="width:100px">产品</div>
					  <div>${currentNgsAvailableData.product_name}</div>
				  </div>
				  <div class="layui-col-xs5" style="display: flex;">
					  <div style="width:100px">原发癌种</div>
					  <div>${sampleFile.disease_type}</div>
				  </div>
			  </div>
			  <div class="layui-row" style="padding:20px 0;">
				  <div class="layui-col-xs8" style="display: flex;">
					  <div style="width:100px">报告文件</div>
					  <div>${analysisFile.report_filename}</div>
				  </div>
				  <div class="layui-col-3" style="display: flex;">
					  <div class="layui-upload-drag" style="display: block;" id="ID-upload-demo-drag">
						  <i class="layui-icon layui-icon-upload"></i>
						  <i class="layui-icon layui-icon-face-smile" style="font-size: 30px; color: #1E9FFF;"></i>

						  <div>点击上传，或将文件拖拽到此处</div>
						  <div class="layui-hide" id="ID-upload-demo-preview">
							  <i class="layui-icon layui-icon-face-smile" style="font-size: 30px; color: #1E9FFF;"></i>
							  <div style="margin-top: 5px; color: #666">或将文件拖拽到此处</div>
						  </div>
					  </div>
				  </div>
			  </div>
		</div>
	</div>
</body>
<script>
	layui.use(function(){
		var upload = layui.upload;
		var $ = layui.$;
		// 渲染
		upload.render({
			elem: '#ID-upload-demo-drag',
			url: '', // 实际使用时改成您自己的上传接口即可。
			done: function(res){
				layer.msg('上传成功');
				$('#ID-upload-demo-preview').removeClass('layui-hide')
						.find('img').attr('src', res.files.file);
				console.log(res)
			}
		});
	});
</script>
</html>
