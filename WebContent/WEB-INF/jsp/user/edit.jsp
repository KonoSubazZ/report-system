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
<link rel="stylesheet" href="css/pintuer.css">
<link rel="stylesheet" href="css/admin.css">
<script type="text/javascript" src="jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jquery/jquery.form.js"></script>
<script src="js/pintuer.js"></script>
<script type="text/javascript">
$(function(){
	$.ajax({
		url:"${pageContext.request.contextPath}/userRole/getAllUserRole.do",
		type:"post",
		dataType:"json",
		success:function(result){
			console.log(result.dataList);
			$.each(result.dataList,function(i,n){
				if(n.role_id == '${user.role_id}'){
					$("#select_role").append("<option id='select_option' selected value='"+n.role_id+"'>"+n.user_role_chinese+"</option>");
				}else{
					$("#select_role").append("<option id='select_option' value='"+n.role_id+"'>"+n.user_role_chinese+"</option>");	
				}
			});
		}
	}); 
	
	//密码确认
 	function pwdYes(){
 	   var pw1 = document.getElementById("encoded_password").value;
       var pw2 = document.getElementById("confirm_password").value;
       if(pw1==pw2){
    	   document .getElementById ("tips").innerHTML="";
    	   return true;
       }else{
    	   document .getElementById ("tips").innerHTML="确认密码与登陆密码不同，请重新输入";
    	   return false;
       }
 	}
	
 	$("#updateBtn").click(function(){
		$("#userForm").submit();
	}); 
	
    $("#userForm").ajaxForm({
 		
		beforeSubmit:function(){
			$("#message").text("数据正在保存，请稍后");
			return true;
		},
		success:function(jsonObject){
			if(jsonObject.success){
				//$("#message").text("数据保存成功");
				alert("数据保存成功");
				window.location.href="${pageContext.request.contextPath}/user/list";
			}else{
				$("#message").text("数据保存失败，请重新保存");
			}
		}

	}); 
 	
 	
});
</script>
</head>
<body>
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>增加内容</strong></div>
  <div class="body-content">
    <form action="user/update.do" id="userForm" method="post"  class="form-x">  
    	<input type="hidden" name="user_id" value="${user.user_id}"/>
		<input type="hidden" name="encoded_password" value="${user.encoded_password }" />
      <div class="form-group">
        <div class="label">
          <label>登录帐号：</label>
        </div>
        <div class="field">
          <input type="text" class="input w50" name="user_account" value="${user.user_account}" data-validate="required:请输入登录账号" />
          <div class="tips"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>用户姓名：</label>
        </div>
        <div class="field">
          <input type="text" class="input w50" name="full_name" value="${user.full_name}" data-validate="required:请输入用户姓名" />
          <div class="tips"></div>
        </div>
      </div>  
      
      <!--  <div class="form-group">
        <div class="label">
          <label>登录密码：</label>
        </div>
        <div class="field">
          <input type="password" class="input w50" value=""  id="encoded_password" name="encoded_password" data-validate="required:请输入登录密码" />
          <div class="tips"></div>
        </div>
      </div>  
      
       <div class="form-group">
        <div class="label">
          <label>确认密码：</label>
        </div>
        <div class="field">
          <input type="password" class="input w50" value="" name="title"  id="confirm_password" onkeyup="pwdYes()" data-validate="required:请输入确认密码" />
          <div class="tips" id="tips"></div>
        </div>
      </div>    -->
      
       <div class="form-group">
        <div class="label">
          <label>状态：</label>
        </div>
        <div class="field">
           <c:if test="${user.checking_status_flag==1}">
			<input type="radio" checked name="checking_status" value="A" />可用
			<input type="radio" name="checking_status" value="F" />禁用
			</c:if>
			<c:if test="${user.checking_status_flag==0}">
			<input type="radio" name="checking_status" value="A" />可用
			<input type="radio" checked name="checking_status" value="F" />禁用
			</c:if>
        </div>
      </div>  
      
        <div class="form-group">
          <div class="label">
            <label>角色选择：</label>
          </div>
          <div class="field">
            <select name="role_id" id="select_role" class="input w50">
              
            </select>
            <div class="tips"></div>
          </div>
        </div>
        
      <div class="form-group">
        <div class="label">
          <label></label>
        </div>
        <div class="field">
          <button class="button bg-main icon-check-square-o" id="updateBtn" type="button"> 提交</button>
          <span id="message"></span>
        </div>
      </div>
    </form>
  </div>
</div>

</body></html>