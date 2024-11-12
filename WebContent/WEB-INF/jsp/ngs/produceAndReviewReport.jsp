<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/myAlert.css">
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/myAlert.js"></script>
<style type="text/css">
	td{vertical-align: middle;}
	input::-webkit-input-placeholder{
		 color:red;
	}
	input::-moz-placeholder{   /* Mozilla Firefox 19+ */
		color:red;
	}
	input:-moz-placeholder{    /* Mozilla Firefox 4 to 18 */
		color:red;
	}
	input:-ms-input-placeholder{  /* Internet Explorer 10-11 */
		color:red;
	}
</style>
</head>
<body>
	<div class="panel admin-panel">
		<div class="panel-head">
			<strong class="icon-reorder">
				NGS报告管理>报告审核及下载:${currentNgsAvailableData.report_id}:${currentNgsAvailableData.platform }>${currentNgsAvailableData.analysis_date }>${currentNgsAvailableData.subbarcode }>${currentNgsAvailableData.product_name }
			</strong>
		</div>
		<div class="body-content" style="border:1px solid #DCDCDC">
			<form id="ngsForm" class="form-x">
				<input type="hidden" name="report_id" id="report_id" value="${currentNgsAvailableData.report_id}">
				<input type="hidden" name="platform" value="${currentNgsAvailableData.platform}">
				<input type="hidden" name="analysis_date" value="${currentNgsAvailableData.analysis_date}">
				<input type="hidden" name="subbarcode" value="${currentNgsAvailableData.subbarcode}">
				<input type="hidden" name="product_name" value="${currentNgsAvailableData.product_name}">
				<input type="hidden" name="product_name_chinese" value="${product.product_name_chinese}">
				<input type="hidden" name="created_by" value="${user.user_account}">
				<input type="hidden" name="update_by" value="${user.user_account}">
				<input type="hidden" name="analyzer" value="${user.user_account}">
				<input type="hidden" id="created_date" name="created_date" />
				<input type="hidden" id="update_date" name="update_date">
				<input type="hidden" name="test_id" value="9">
				<input type="hidden" id="flag" name="flag" value=1>
				<script type="text/javascript">
					$(function(){
						getTime("update_date");
						getTime("created_date");
						//获取系统时间。
						var d=new Date();
						var YY=d.getFullYear();
						var MM=d.getMonth()+1;
						var DD=d.getDate();
						var hh=d.getHours();
						var mm=d.getMinutes();
						var ss=d.getSeconds();
						//将时间显示，时间格式形如：2017-03-15 15:16:10
						$("#report_date").val(YY+'-'+(MM<10?'0':'')+MM+'-'+(DD<10?'0':'')+DD);
						$("#tested_date").val(YY+'-'+(MM<10?'0':'')+MM+'-'+(DD<10?'0':'')+DD);
						$("#checked_date").val(YY+'-'+(MM<10?'0':'')+MM+'-'+(DD<10?'0':'')+DD);
					});
				</script>
				<table style="width:100%">
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>本癌种：</label>
								</div>
								<div class="field">
									<input type="text" class="input w50" id="primary_cancer" name="primary_cancer" value="${diseaseClass.disease_class_chinese}" placeholder="请选择原发癌种"  data-validate="required:请选择原发癌种"/>
									<input type="hidden" id="primary_cancer_id"  name="primary_cancer_id" value="${diseaseClass.class_id}"/>
									<script type="text/javascript">
										$(function(){
											$.post("${pageContext.request.contextPath}/autoComplete/getDiseaseClassChineseAndId", function(data){
												$('#primary_cancer').autocomplete(data, {
													max : data.length, //列表里的条目数
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
													$("#primary_cancer_id").val(row.id);
												});
											},"json");
										})
									</script>
									<div class="tips"></div>
								</div>
							</div>
						</td>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>化疗癌种：</label>
								</div>
								<div class="field">
									<input type="text" id="chem_cancer" name="chem_cancer" class="input w50" value="${chem_cancer}" style="cursor: pointer;" placeholder="请选择化疗癌种" data-validate="required:请选择化疗癌种" />
									<script type="text/javascript">
										$(function(){
											var data = [{name: "非小细胞肺癌"}, {name: "结直肠癌"}, {name: "乳腺癌"}, {name: "胃癌"}, {name: "卵巢癌"}, {name: "睾丸癌"}, {name: "骨肉瘤"}, {name: "前列腺癌"}, {name: "胰腺癌"}, {name: "食管癌"}, {name: "间皮瘤"}, {name: "小细胞肺癌"}, {name: "其他癌种"}];
											$('#chem_cancer').autocomplete(data, {
												max : data.length, //列表里的条目数
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
											});
										})
									</script>
									<div class="tips"></div>
								</div>
							</div>
						</td>
						<%-- <c:if test="${currentNgsAvailableData.product_name=='lung'}">
                            <td>
                                 <div class="form-group" style="margin-right: 50px">
                                     <div class="label" style="width:75px">
                                         <label></label>
                                     </div>
                                     <div class="field">
                                         肺癌非鳞癌<input type="radio" name="clinicalremark" checked="checked" value="s1"/>
                                         肺鳞癌<input type="radio" name="clinicalremark" value="s2"/>
                                         <div class="tips"></div>
                                     </div>
                                 </div>
                             </td>
                         </c:if> --%>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>检测产品：</label>
								</div>
								<div class="field">
									<input type="text" class="input w50" id="product_name_chinese"  value="${product.product_name_chinese}" placeholder="必选项！请选择检测产品" data-validate="required:请选择检测产品"/>
									<input type="hidden" id="product_id" name="product_id" value="${product.product_id}"   />
									<script type="text/javascript">
										$(function(){
											$.post("${pageContext.request.contextPath}/autoComplete/getProductNameChineseAndId",
													{product_name:"${currentNgsAvailableData.product_name}"},
													function(data){
														$('#product_name_chinese').autocomplete(data, {
															max : data.length, //列表里的条目数
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
									<div class="tips"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>报告模板：</label>
								</div>
								<div class="field">
									<%--<input type="text" class="input w50"  value="" id="template_name" name="template_name" placeholder="请选择报告模板"  data-validate="required:请选择报告模板" />--%>
									<input type="text" class="input w50"  value="" id="template_name" name="template_name" placeholder="请选择报告模板" />
									<input type="hidden" id="template_id" name="template_id">
									<div class="tips"></div>
									<input id="mybtn" type="button" onclick="moreTemplate();" value="更多" />
									<script type="text/javascript">
										$(function(){
											$.post("${pageContext.request.contextPath}/autoComplete/getReportTemplateIdAndName",
													{"product_id":"${product.product_id}","subbarcode":"${currentNgsAvailableData.subbarcode}","flag":"1"},
													function(data){
														$('#template_name').autocomplete(data, {
															max : false, //列表里的条目数
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
															$("#template_id").val(row.id);
															if(row.name=='肺癌10基因报告模板-医生版' || row.name=='肠癌10基因模板-组织版'){
																$(".pic").prop("hidden",false);
															}else{
																$(".pic").prop("hidden",true);
															}
														});
													},"json");
										});
										function moreTemplate(){
											$.post("${pageContext.request.contextPath}/autoComplete/getReportTemplateIdAndName",
													{"product_id":"${product.product_id}","subbarcode":"${currentNgsAvailableData.subbarcode}","flag":"2"},
													function(data){
														$('#template_name').autocomplete(data, {
															max : false, //列表里的条目数
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
															$("#template_id").val(row.id);
															if(row.name=='肺癌10基因报告模板-医生版' || row.name=='肠癌10基因模板-组织版'){
																$(".pic").prop("hidden",false);
															}else{
																$(".pic").prop("hidden",true);
															}
														});
													},"json");
											document.getElementById('mybtn').style.backgroundColor='red';
										}
									</script>
								</div>
							</div>
						</td>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>报告时间：</label>
								</div>
								<div class="field">
									<input type="text" id="report_date" name="report_date" class="input w50" value="" style="cursor: pointer;" placeholder="请选择日期" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" data-validate="required:请选择日期" />
									<div class="tips"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>检测人：</label>
								</div>
								<div class="field">
									<c:choose>
										<c:when test="${user.user_account=='houlitao'}"><input type="text" class="input w50" value="1" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
										<c:when test="${user.user_account=='liuxiaomin'}"><input type="text" class="input w50" value="2" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
										<c:when test="${user.user_account=='sunpeiya'}"><input type="text" class="input w50" value="3" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
										<c:when test="${user.user_account=='gaoyuan'}"><input type="text" class="input w50" value="4" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
										<c:when test="${user.user_account=='liulijie'}"><input type="text" class="input w50" value="5" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
										<c:when test="${user.user_account=='wangyiping'}"><input type="text" class="input w50" value="6" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
										<c:otherwise><input type="text" class="input w50" value="" name="tested_by" placeholder="盖章版请输入检测人" /></c:otherwise>
									</c:choose>
									<div class="tips"></div>
								</div>
							</div>
						</td>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label >检测时间：</label>
								</div>
								<div class="field">
									<input type="text" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" id="tested_date"  name="tested_date" data-validate="required:请选择日期" />
									<div class="tips"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>复核人：</label>
								</div>
								<div class="field">
									<input type="text" class="input w50" value=""  id="checked_by" name="checked_by" placeholder="盖章版请输入复核人" />
									<div class="tips"></div>
								</div>
							</div>
						</td>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label>复核时间：</label>
								</div>
								<div class="field">
									<input type="text" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" id="checked_date"  name="checked_date" data-validate="required:请选择日期" />
									<div class="tips"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr class="pic" hidden="true">
						<td colspan="2">
							<div class="label" style="float: left;margin-left: 25px;margin-top: 10px;">
								<label>镜下图像1：</label>
							</div>
							<div class="field" style="float:left;margin-left: 7px;width: 109px" id="preview3">
								<button type="button" class="button bg-main icon-image" onclick="$('#previewImg3').click();"> 插入图片</button>
								<img id="imghead3" onclick="$('#previewImg3').click();">
							</div>
							<input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview3','previewImg3','imghead3','tumorpuritypictureone')" style="display: none;" id="previewImg3">
							<input type="hidden" id="tumorpuritypictureone" name="tumorpuritypictureone">
							<div class="label" style="float: left;margin-left: 285px;margin-top: 10px;">
								<label>镜下图像2：</label>
							</div>
							<div class="field" style="float:left;margin-left: 15px;width: 109px" id="preview4">
								<button type="button" class="button bg-main icon-image" onclick="$('#previewImg4').click();"> 插入图片</button>
								<img id="imghead4" onclick="$('#previewImg4').click();">
							</div>
							<input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview4','previewImg4','imghead4','tumorpuritypicturetwo')" style="display: none;" id="previewImg4">
							<input type="hidden" id="tumorpuritypicturetwo" name="tumorpuritypicturetwo">
							<div class="label" style="float: left;margin-left: 195px;margin-top: 10px;">
								<label>镜下图像3：</label>
							</div>
							<div class="field" style="float:left;margin-left: 8px;width: 109px" id="preview5">
								<button type="button" class="button bg-main icon-image" onclick="$('#previewImg5').click();"> 插入图片</button>
								<img id="imghead5" onclick="$('#previewImg5').click();">
							</div>
							<input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview5','previewImg5','imghead5','tumorpuritypicturethree')" style="display: none;" id="previewImg5">
							<input type="hidden" id="tumorpuritypicturethree" name="tumorpuritypicturethree">
						</td>
					</tr>
					<tr class="pic" hidden="true">
						<td colspan="2">
							<div class="form-group" style="margin-right: 50px; margin-top: 12px;">
								<div class="label" style="width:75px;">
									<label>镜下描述：</label>
								</div>
								<div class="field">
									<textarea  class="input" rows="3" cols="75" style="width: 1145px;" name="tumorpuritydescription"></textarea>
									<div class="tips"></div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label></label>
								</div>
								<div class="field">
								</div>
							</div>
						</td>
						<td>
							<div class="form-group">
								<div class="label" style="width:75px">
									<label></label>
								</div>
								<div class="field" style="margin-left:230px">
									<button id="reportBtn" class="button bg-main icon-check-square-o" type="submit"> 产生报告</button>
									<span id="message" style="color: red;font-size: 14px"></span>
								</div>
								<script type="text/javascript">
									$(function(){
										$.post("${pageContext.request.contextPath}/life/getStatus",{"report_id":"${currentNgsAvailableData.report_id}"},
												function(data){
													if(data=="报告审核通过"){
														$("#reportBtn").prop("disabled","disabled");
													}
												},"text");

										$("#reportBtn").click(function(){
											$("#ngsForm").validate({
												rules:{
													// "tested_by":{"required":true},"tested_date":{"required":true},
													// "checked_by":{"required":true},"checked_date":{"required":true},
													"template_name":{"required":true},"report_date":{"required":true},
													"disease_class_chinese":{"disease_class_chinese":true},
													"product_name_chinese":{"product_name_chinese":true},
													"chem_cancer":{"required":true}
												},
												messages:{
													// "tested_by":{"required":""},"tested_date":{"required":""},
													// "checked_by":{"required":""},"checked_date":{"required":""},
													"template_name":{"required":""},"report_date":{"required":""},
													"disease_class_chinese":{"required":""},
													"product_name_chinese":{"required":""},
													"chem_cancer":{"required":""}
												},
												submitHandler:function(){
													var folder="";
													if($("#template_id").val()==1){
														folder="NGS/PATIENT";
													}else{
														folder="NGS/DOCTOR";
													}
													$.ajax({url:"${pageContext.request.contextPath}/life/updateProductByProductId",
														data:{"primary_cancer_id":$("#primary_cancer_id").val(),
															"product_id":$("#product_id").val(),
															"report_id":"${currentNgsAvailableData.report_id }",
															"platform":"${currentNgsAvailableData.platform}",
															"analysis_date":"${currentNgsAvailableData.analysis_date}",
															"subbarcode":"${currentNgsAvailableData.subbarcode}",
															"product_name":"${currentNgsAvailableData.product_name}",
															"analyzer":"${user.user_account}"},
														success:function(data){
															if($("#report_id").val() == data){
																$('#report_id').val(data);
																$.ajax({
																	cache: false,
																	type: "POST",
																	url:"${pageContext.request.contextPath}/ngs/createReport", //把表单数据发送到ajax.jsp
																	data:$('#ngsForm').serialize(), //要发送的是ajaxFrm表单中的数据
																	dataType:"json",
																	beforeSend:function(){
																		$("#message").text("正在产生报告...");
																		$("#reportBtn").attr("disabled","disabled");
																		return true;
																	},
																	success:function(data){
																		$("#message").text("");
																		$("#report_date").val("");
																		$.post("${pageContext.request.contextPath}/geneMarkerVw/deleteRpVariantOrder",{"analysis_report_id":"${currentNgsAvailableData.report_id}"},
																				function(data){
																				});
																		if(data>0){
																			if(confirm("报告生成成功！是否立即下载文件？")){
																				window.location.href="${pageContext.request.contextPath}/ngs/download?report_id="+data;
																			}
																		}else{
																			alert("报告生成失败!");
																			$("#message").text("报告生成失败!");
																		}
																		$("#reportBtn").removeAttr("disabled");
																	}
																});
															}else{
																alert("该样本已有报告，请重新点击生成报告按钮！");
																parent.urlRun_5(data);
															}
														},
														dataType:"json"});
												}
											});
										});
									});
								</script>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="body-content" style="border:1px solid #DCDCDC">
			<form id="reportFileForm" class="form-x" method="post" enctype="multipart/form-data">
				<table style="width:75%">
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:85px;float: left;margin-top: 10px">
									<label >报告名称：</label>
								</div>
								<div class="field">
									<input type="text" id="report_filename" name="report_filename" readonly="readonly" class="input w50" style="width:600px;float: left;" value="${analysis_report.report_filename }"  />
									<div class="tips"></div>
									<input type="hidden" id="report_file_path" name="report_file_path" value="${analysis_report.report_file_path }" />
								</div>
							</div>
						</td>
						<td>
							<div class="form-group" style="margin-left:30px">
								<input type="file" id="reportFile" name="reportFile">
								<button id="updateReportFile"  style="width: 150px;margin-right:60px" class="button bg-main icon-file-o" > 更换报告文件</button>
							</div>
							<input type="hidden" name="report_id"  value="${currentNgsAvailableData.report_id}" >
						</td>
					</tr>
				</table>
			</form>
			<script type="text/javascript">
				$(function(){
					$("#updateReportFile").click(function(){
						if(confirm("确定更换报告文件？")){
							// var filename = $("#reportFile").val().split(".")[0];
							// var file = $("#reportFile").val();
							// var filename = file.substring(file.lastIndexOf("\\") + 1).substring(0,file.substring(file.lastIndexOf("\\") + 1).lastIndexOf("."));
							// var report_filename = $("#report_filename").val().substring(0,$("#report_filename").val().lastIndexOf("."));
							var from =document.getElementById("reportFileForm");
							var formData = new FormData(from);
							$.ajax({
								async: false,
								cache: false,
								type: "POST",
								url:"${pageContext.request.contextPath}/ngs/updateReportFileByReportId",
								data:formData,
								contentType: false,
								processData: false,
								dataType:"json",
								success:function(data){
									if(data){
										alert("文件更换成功！");
									}else{
										alert("文件更换失败！");
									}
								}
							});
						}
					});
				});
			</script>
			<div id="zhe" style="display:none"></div>
			<div id="biao" style="width:600px;height:500px;background:#fff;padding:0 20px 20px;display:none;position:fixed;top:20%;left:30%;z-index:201;overflow-y:scroll;overflow-x:hidden;">
				<div id="dele" style="margin:5px 0 5px 510px;padding:5px 10px;width:30px;cursor: pointer;border:1px solid #aaa;">X</div>
				<table style="width:100%;" border="1">
					<tr height="30px" style="line-height:30px;">
						<th style="width:10%;">序号</th>
						<th style="width:10%;">record_id</th>
						<th style="width:20%;">基因</th>
						<th style="width:20%;">突变</th>
						<th style="width:20%;">类型</th>
						<th style="width:20%;">审核时间</th>
					</tr>
					<tbody id="siteInfo">
					</tbody>
				</table>
			</div>
			<table style="width:100%">
				<tr>
					<td style="width:708px">
						<div class="form-group" style="margin-right: 50px;">
							<div class="label" style="width:85px;float: left;margin-top: 10px;">
								<label>审核结果：</label>
							</div>
							<div class="field">
								<button id="review" style="width: 170px;float: left;" class="button border-main icon-check" > 审核通过</button>
								<button id="reviewfalse" style="width: 170px;float: left;" class="button border-red icon-times" > 审核不通过</button>
								<div id="message1" class="tips" style="color: red;font-size: 14px"></div>
							</div>
						</div>
					</td>
					<td hidden="hidden">
						<button id="viewSite" style="float: left;" class="button border-main" >查看位点</button>
					</td>
					<td hidden="hidden">
						<select style="width: 120px;float: left;height:40px;border:0px solid #ccc;border-radius:5px;" name="send_way" id="send_way" value="${analysis_report.send_way}">
							<option value="1">线上发送</option>
							<option value="0">线下发送</option>
						</select>
					</td>
					<td style="width:560px">
						<div class="form-group">
							<div class="label" style="width:85px">
								<label></label>
							</div>
							<div class="field" hidden="hidden">
								<button id="sendEmail" style="width: 150px;float:left;" disabled="disabled" class="button bg-main icon-send" > 发送邮件</button>
								<p id="sendTip" style="font-size:14px;color:red;float:left;line-height:42px;"></p>
								<script type="text/javascript">
									$("#viewSite").click(function(){
										$.post("${pageContext.request.contextPath}/life/getSiteInfo",{"report_id":"${currentNgsAvailableData.report_id}"},
												function(data){
													if(data.flag){
														$("#biao").show();
														$("#zhe").show();
														var siteInfo = "";
														$.each(data.siteData,function(item,index,arr){
															siteInfo += '<tr height="50px" align="center"><td>'+item+'</td><td>'+index.record_id+'</td><td>'+index.gene+'</td><td>'+index.variant+'</td><td>'+index.type+'</td><td>'+index.check_date+'</td></tr>';
														})
														$("#siteInfo").html(siteInfo);

													}else{
														alert("请先生产报告");
													}
												});
									})
									$("#dele").click(function(){
										$("#biao").hide();
										$("#zhe").hide();
									})
									$("#zhe").click(function(){
										$("#biao").hide();
										$("#zhe").hide();
									})
									$("#zhe").css({"height":$(window).width(),"width":$(window).width(),"background":"#000","z-index":"200","position":"fixed","top":"0","left":"0","opacity":".7"})
									$("#send_way").change(function(){
										var flag = $("#send_way").val();
										if(flag == 1){
											var changeFlg=confirm("是否确认线上发送?");
											if(changeFlg){
												$("#send_way").val("1");
												$("#send_way").css({"background":"#0ae","color":"#fff"});
												$("#sendEmail").prop("disabled",false);
											}else{
												$("#send_way").val("0");
												$("#send_way").css({"background":"red","color":"#fff"});
												$("#sendEmail").prop("disabled","disabled");
											}
										}else{
											var changeFlg=confirm("是否确认线下发送?");
											if(changeFlg){
												$("#send_way").val("0");
												$("#send_way").css({"background":"red","color":"#fff"});
												$("#sendEmail").prop("disabled","disabled");
											}else{
												$("#send_way").val("1");
												$("#send_way").css({"background":"#0ae","color":"#fff"});
												$("#sendEmail").prop("disabled",false);
											}
										}
										$.post("${pageContext.request.contextPath}/life/updateSendWay",{"report_id":"${currentNgsAvailableData.report_id}","send_way":$("#send_way").val()},
												function(data){
													if(data){
														alert("报告发送状态更改成功！");
													}else{
														alert("报告发送状态更改失败！");
													}
												});

									});
									$(function(){
										$("#send_way").val(${analysis_report.send_way});
										isDisabled();
										$.post("${pageContext.request.contextPath}/life/getStatus",{"report_id":"${currentNgsAvailableData.report_id}"},
												function(data){
													if(data=="报告审核通过" || data=="报告发送成功"){
														$("#updateReportFile").prop("disabled","disabled");
														$("#review").prop("disabled","disabled");
														$("#reviewfalse").prop("disabled","disabled");
														isDisabled();
													}
												},"text");
										$("#review").click(function(){
											if($("#report_filename").val()){
												$.myConfirm({title:'报告审核确认',message:'确认审核通过后将无法更换报告文件！',callback:function(){
														$.post("${pageContext.request.contextPath}/life/editStatus",{"report_id":"${currentNgsAvailableData.report_id}","subbarcode":"${currentNgsAvailableData.subbarcode}","status":"报告审核通过","flag":1},
																function(data){
																	if(data.flag){
																		$("#updateReportFile").prop("disabled","disabled");
																		$("#review").prop("disabled","disabled");
																		$("#reviewfalse").prop("disabled","disabled");
																		isDisabled();
																		$.myAlert('报告状态已修改为：报告审核通过！');
																	}else{
																		$.myAlert('报告状态已修改为：报告审核未通过！');
																	}
																},"json");
													}
												})
											}else{
												alert("文件不能为空！");
											}

										});
										$("#reviewfalse").click(function(){
											if($("#report_filename").val()){
												$.myConfirm({title:'报告审核确认',message:'确认审核未通过后将更改报告状态！',callback:function(){
														$.post("${pageContext.request.contextPath}/life/editStatus",{"report_id":"${currentNgsAvailableData.report_id}","subbarcode":"${currentNgsAvailableData.subbarcode}","status":"报告审核未通过"},
																function(data){
																	$.myAlert('报告状态已修改为：报告审核未通过！');
																},"json"
														);
													}
												})
											}else{
												alert("文件不能为空！");
											}
										});
										$("#sendEmail").click(function(){
											fn(function () {
												$.myConfirm({title:'邮件发送确认',message:'确认发送邮件？',callback:function(){
														$("#sendEmail").prop("disabled","disabled");
														$("#sendTip").text("正在发送邮件，请稍后...");
														$.post("${pageContext.request.contextPath}/ngs/sendEmail",
																{"report_id":"${currentNgsAvailableData.report_id}","subbarcode":"${currentNgsAvailableData.subbarcode}",
																	"report_filename":"${analysis_report.report_filename }","report_file_path":"${analysis_report.report_file_path }"},
																function(data){
																	$.myAlert(data.errorMessage);
																	$("#sendEmail").prop("disabled",false);
																	$("#sendTip").text("");
																},"json"
														);
													}
												})
											})
										});
									});
									function isDisabled(){
										if($("#send_way").val() == 1){
											$("#sendEmail").prop("disabled",false);
											$("#send_way").css({"background":"#0ae","color":"#fff"});
										}else{
											$("#sendEmail").prop("disabled","disabled");
											$("#send_way").css({"background":"red","color":"#fff"});
										}
									}

									function fn(callback){
										fn.prototype.init(callback);
									}
									fn.prototype = {
										canclick: true,
										init: function(callback){
											if(this.canclick){
												this.canclick = false
												callback();
												setTimeout(function(){
													this.canclick = true
												}.bind(this),1000)
											}else{
												console.log('1s中之内不允许重复点击')
											}
										}
									}
								</script>
								<div class="tips"></div>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="body-content" style="border:1px solid #DCDCDC">
			<form id="FileForm91360" class="form-x" method="post" enctype="multipart/form-data">
				<table style="width:75%">
					<tr>
						<td>
							<div class="form-group">
								<div class="label" style="width:85px;float: left;margin-top: 10px">
									<label >91360名称：</label>
								</div>
								<div class="field">
									<input type="text" id="filename91360" name="filename91360" readonly="readonly" class="input w50" style="width:600px;float: left;" value="${analysis_report.filename91360 }"  />
									<div class="tips"></div>
									<input type="hidden" id="file_path91360" name="file_path91360" value="${analysis_report.file_path91360 }" />
								</div>
							</div>
						</td>
						<td>
							<div class="form-group" style="margin-left:30px">
								<input type="file" id="File91360" name="File91360">
								<button id="updateFile91360"  style="width: 150px;margin-right:60px" class="button bg-main icon-file-o" > 更换91360文件</button>
							</div>
							<input type="hidden" name="report_id"  value="${currentNgsAvailableData.report_id}" >
						</td>
					</tr>
				</table>
			</form>
			<script type="text/javascript">
				$(function(){
					$("#updateFile91360").click(function(){
						if(confirm("确定更换91360文件？")){
							var from =document.getElementById("FileForm91360");
							var formData = new FormData(from);
							$.ajax({
								async: false,
								cache: false,
								type: "POST",
								url:"${pageContext.request.contextPath}/ngs/updateFile91360ByReportId",
								data:formData,
								contentType: false,
								processData: false,
								dataType:"json",
								success:function(data){
									if(data){
										alert("文件更换成功！");
									}else{
										alert("文件更换失败！");
									}
								}
							});
						}
					});
				});
			</script>
			<table style="width:100%">
				<tr>
					<td style="width:708px">
						<div class="form-group" style="margin-right: 50px;">
							<div class="label" style="width:85px;float: left;margin-top: 10px;">
								<label>审核结果：</label>
							</div>
							<div class="field">
								<button id="review1" style="width: 170px;float: left;" class="button border-main icon-check" > 审核通过</button>
								<button id="reviewfalse1" style="width: 170px;float: left;" class="button border-red icon-times" > 审核不通过</button>
								<div id="message2" class="tips" style="color: red;font-size: 14px"></div>
								<script type="text/javascript">
									$(function(){
										$("#review1").click(function(){
											if($("#filename91360").val()){
												$.myConfirm({title:'91360审核确认',message:'确认审核通过后将无法更换91360文件！',callback:function(){
														$("#updateFile91360").prop("disabled","disabled");
														$("#review1").prop("disabled","disabled");
														$("#reviewfalse1").prop("disabled","disabled");
														$.myAlert('状态已修改为：91360审核通过！');
													}
												})
											}else{
												alert("文件不能为空！");
											}

										});
										$("#reviewfalse1").click(function(){
											if($("#filename91360").val()){
												$.myConfirm({title:'91360审核确认',message:'确认审核未通过后将更改91360状态！',callback:function(){
														$.myAlert('状态已修改为：91360审核未通过！');
													}
												})
											}else{
												alert("文件不能为空！");
											}
										});
									});
								</script>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
