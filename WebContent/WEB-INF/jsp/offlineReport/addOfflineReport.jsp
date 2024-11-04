<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" ></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/myAlert.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js" role='reload'></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/myAlert.js"></script>
<script type="text/javascript">

</script>
</head>
<style>
	input[type="file"] {
		border: 2px solid #0056b3;
		border-radius: 5px;
		padding: 5px;
		width: 600px;
	}
</style>
<body>
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>上传报告</strong></div>
  <div class="body-content" style="margin-left:100px">

    <form  id="offlineForm" method="post" class="form-x"  enctype="multipart/form-data">  
    <table style="width:100%">
    <tr>
    	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label >样本编号：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="" name="subbarcode" id="subbarcode" onchange="getSampleFileBySubbarcode()" data-validate="required:请正确填写样本 ID" />
	          <div class="tips"></div>
				<script type="text/javascript">
					function getSampleFileBySubbarcode(){
						var sub_val = $("#subbarcode").val();
						if(sub_val != ""){
							$.ajax({
								cache: false,
								type: "POST",
								url:"${pageContext.request.contextPath}/sampleFile/getSampleFileBySubbarcode", //把表单数据发送到ajax.jsp
								data:{"subbarcode":sub_val}, //要发送的是ajaxFrm表单中的数据
								success:function(result){
									$("#person_name").val(result.person_name);
									$("#specimen_type").val(result.specimen_type);
									$("#disease_type").val(result.disease_type);
									$("#emailaddress").val(result.emailaddress);
								}
							});
						}
					}
				</script>
	        </div>
	      </div>
	     </td>
	     <td>
	      <div class="form-group">
	      	<div class="label" style="width:75px">
	          <label></label>
	        </div>
	        <div class="field" >
	          <button id="sample_lims" style="width: 130px;" class="button bg-main icon-search-plus"  type="button"> 从LIMS抓取</button>
	          <script type="text/javascript">
	          	$(function(){
	          		$("#sample_lims").click(function(){
	          			var s = $('#subbarcode').val();
	          			if(s==null || s==""){
	          				alert("样本编号为空！")
	          			}else{
			          		$.ajax({
			          			url:"${pageContext.request.contextPath}/sampleFile/getSpecimenHeadBySubbarcode",
			          			type:"post",
			          			data:{"subbarcode":$("#subbarcode").val()},
			          			dataType:"json",
			          			success:function(result){
			          				if(result){
			          					alert("抓取成功");
			          					$("#person_name").val(result.person_name);
			          					$("#specimen_type").val(result.specimen_type);
			          					$("#disease_type").val(result.disease_type);
			          					$("#emailaddress").val(result.emailaddress);
			          				}else{
			          					alert("抓取失败");
			          				}
			          			}
			          		});
	          			}
	          		});
	          	});
	          </script>
	       </div>
	      </div>
	     </td>
     </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>姓		名：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50"  id="person_name" name="person_name" value="" disabled="disabled"  />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
     		<div class="form-group" style="margin-right: 50px">
		        <div class="label" style="width:80px">
		          <label>选择文件1：</label>
		        </div>
		        <div class="field">
				  <input type="file" id="filenameone" name="filenameone">
		          <div class="tips"></div>
		        </div>
	        </div>  
		</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>样本类型：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="specimen_type" disabled="disabled"  />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
     		<div class="form-group" style="margin-right: 50px">
		        <div class="label" style="width:80px">
		          <label>选择文件2：</label>
		        </div>
		        <div class="field">
				  <input type="file" id="filenametwo" name="filenametwo">
		          <div class="tips"></div>
		        </div>
	        </div>  
		</td> 
      </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>疾病种类：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="disease_type" disabled="disabled" id="disease_type" name="disease_type"/>
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td>
		  <td>
			  <div class="form-group" style="margin-right: 50px">
				  <div class="label" style="width:80px">
					  <label>选择文件3：</label>
				  </div>
				  <div class="field">
					  <input type="file" id="filenamethree" name="filenamethree">
					  <div class="tips"></div>
				  </div>
			  </div>
		  </td>
	  </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>客户邮箱：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50"  disabled="disabled"  id="emailaddress" name="emailaddress"  />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	     	<div class="form-group">
	        <div class="label" style="width:75px">
	          <label>上传时间：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50"  value="" disabled="disabled"  id="tested_date" name="tested_date"  />
	          <script type="text/javascript">
					$(function(){
						getTime("tested_date");
					});
			  </script>
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group" >
	        <div class="label" style="width:75px">
	          <label>上传人：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="${user.user_account}" disabled="disabled"  id="tested_by" name="tested_by"  />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	       <div class="form-group">
	        <div class="label" style="width:75px">
	          <label></label>
	        </div>
	        <div class="field">
	       	  <button id="reportBtn" class="button bg-main icon-check-square-o" type="submit"> 上传报告</button>
	       	  <button id="sendEmail" class="button bg-main icon-send" > 发送邮件</button>
	       	  <button  class="button bg-main icon-file-o" onclick="javascript: window.history.back();" style="width: 135px" type="button"> 返回列表</button>
	        </div>
	        <script type="text/javascript">
	        	$(function(){
	        		$("#reportBtn").click(function(){
						$("#sendEmail").prop("disabled","disabled");
         				$("#offlineForm").validate({
   	    					rules:{
   	    						"subbarcode":{"required":true}
   	    					},
   	    					messages:{
   	    						"subbarcode":{"required":""}
   	    					},
   	    					submitHandler:function(){
   	    						var from =document.getElementById("offlineForm");
		        				var formData = new FormData(from);
   	    						$.ajax({
	   	 		          			url:"${pageContext.request.contextPath}/sampleFile/getSampleIdBySubbarcode",
	   	 		          			type:"post",
	   	 		          			data:{"subbarcode":$("#subbarcode").val()},
	   	 		          			dataType:"json",
	   	 		          			success:function(result){
	   	 		          				if(!result){
		   	 		          				$.ajax({
		   	   	    							cache: false,
		   	   	    							type: "POST",
					   	 		          		contentType: false,
										        processData: false,
				   	 		          			dataType:"json",
		   	   	    							url:"${pageContext.request.contextPath}/offlineReport/addOfflineReport", //把表单数据发送到ajax.jsp
		   	   	    							data:formData,
		   	   	    							success:function(data){
		   	   	    								if(data){
		   	   	    									alert("文件上传成功！");
		   	   	    								}else{
		   	   	    									alert("文件上传失败！"+data.errorMessage);
		   	   	    								}
		   	   	    							}
		   	   	    						});
   	 		          					}else{
   	 		          						alert("请从LIMS抓取数据!");
   	 		          						return;
   	 		          					}
   	 		          				}
   	 		          			});
   	    			        }
   	    				});
	        		});

					$("#sendEmail").click(function () {
						$("#reportBtn").prop("disabled","disabled");
						$("#offlineForm").validate({
							rules:{
								"subbarcode":{"required":true}
							},
							messages:{
								"subbarcode":{"required":""}
							},
							submitHandler:function(){
								$.myConfirm({
									title: '邮件发送确认', message: '确认发送邮件？', callback: function () {
										var from = document.getElementById("offlineForm");
										var formData = new FormData(from);
										$.ajax({
											url:"${pageContext.request.contextPath}/sampleFile/getSampleIdBySubbarcode",
											type:"post",
											data:{"subbarcode":$("#subbarcode").val()},
											dataType:"json",
											success:function(result){
												if(!result){
													$.ajax({
														cache: false,
														type: "POST",
														contentType: false,
														processData: false,
														url: "${pageContext.request.contextPath}/offlineReport/addOfflineSendEmail", //把表单数据发送到ajax.jsp
														data: formData,
														dataType:"json",
														success: function (data) {
															if(data){
																$.myAlert(data.errorMessage);
															}
														}
													});
												}else{
													alert("请从LIMS抓取数据!");
													return;
												}

											}
										});
									}
								});
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
  </div>

</body></html>