<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js" role='reload'></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

</script>
</head>
<body>
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>修改假阳</strong></div>
  <div class="body-content" style="margin-left:90px">
    <form class="form-x" id="sendEmailForm">
     <input type="hidden" name="email_id" id="email_id" value="${sendEmail.email_id }" />
    <table style="width:100%">
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:120px">
		          <label>送检机构：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="${sendEmail.customer }" name="customer" id="customer"  data-validate="required:请输入customer"/>
		          <div class="tips"></div>
		          <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
		        </div>
		      </div>
		     </td>
	     </tr>
	     <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:120px">
		          <label>收件人邮箱：</label>
		        </div>
		        <div class="field">
					<textarea cols="10" rows="10" type="text" class="input w50" name="emailaddress" id="emailaddress" data-validate="required:请输入emailaddress">${sendEmail.emailaddress }</textarea>
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
	    	  <div class="form-group" style="margin-left: 0px;">
		        <div class="label" style="width:106.8px">
		          <label>抄送人邮箱：</label>
		        </div>
		        <div class="field">
					<textarea cols="10" rows="10" type="text" class="input w50" name="CCemail" id="CCemail">${sendEmail.CCemail }</textarea>
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
		<tr>
			<td>
				<div class="form-group">
					<div class="label" style="width:120px">
						<label>内容：</label>
					</div>
					<div class="field">
						<textarea cols="10" rows="10" type="text" value="" style="width:288px;height:222px;" name="content" id="content">${sendEmail.content }</textarea>
						<div class="tips"></div>
					</div>
				</div>
			</td>
			<td>
				<div class="form-group">
					<div class="label" style="width:75px">
						<label>主题：</label>
					</div>
					<div class="field">
						<textarea cols="10" rows="10" type="text" value="" style="width:288px;height:222px;" name="subject" id="subject">${sendEmail.subject }</textarea>
						<div class="tips"></div>
					</div>
				</div>
			</td>
		</tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">

		        </div>
		        <div class="field">
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
			       	  <button id="sendEmailBtn" class="button bg-main icon-check-square-o" type="submit" style="width: 143px"> 保存</button>
			       	  <button  class="button bg-main icon-file-o" onclick="javascript: window.history.back();" style="width: 143px" type="button"> 返回</button>
			        </div>
		        </div>
		     <script type="text/javascript">
		     $(function(){
	        		$("#sendEmailBtn").click(function(){
      				$("#sendEmailForm").validate({
	    					rules:{
	    						"customer":{"required":true},"emailaddress":{"required":true}
	    					},
	    					messages:{
	    						"customer":{"required":""},"emailaddress":{"required":""}
	    					},
	    					submitHandler:function(){
	    						$.ajax({
	    							cache: false,
	    							type: "POST",   
	    							url:"${pageContext.request.contextPath}/sendEmail/updateSendEmail", //把表单数据发送到ajax.jsp
	    							data:$('#sendEmailForm').serialize(), //要发送的是ajaxFrm表单中的数据
	    							success:function(data){
	    								if(data.success){
	  	    								alert("修改成功！");
	    								}else{
	    									alert(data.mgs);
	    								}
	    							} 
	    						});
	    			        }  
	    				}); 
	        		});
	        	});
	        </script>
		     </td>
	     </tr>
     </table>
     </form>
  </div>
</div>

</body>
</html>