<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-cn"  style="min-width: 1720px;">
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
	function fun_sub(){
		$("#role_form").submit();
		
		/* $.ajax({
			url:"${pageContext.request.contextPath}/userRole/saveUserRole",
			type:"post",
			dataType:"json",
			success:function(result){
			}
		}); */
	}
	$(function(){
		$("#role_form").ajaxForm({
			success:function(jsonObject){
				if(jsonObject.success){
					alert("角色添加成功!");
					window.location.href="${pageContext.request.contextPath}/userRole/userRoleList";
				}else{
					alert("角色添加失败!");
				}
			}
	
		});
	});
</script>
</head>
<body>
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>添加角色</strong></div>
  <div class="body-content">
    <form method="post" class="form-x" id="role_form" action="${pageContext.request.contextPath}/userRole/saveUserRole">  
      <div class="form-group">
        <div class="label">
          <label>角色名称：</label>
        </div>
        <div class="field">
          <input type="text" class="input w50" value="" name="user_role" data-validate="required:请输入角色名称" />
          <div class="tips"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>中文名称：</label>
        </div>
        <div class="field">
          <input type="text" class="input w50" value="" name="user_role_chinese" />
          <div class="tips"></div>
        </div>
      </div>  
      <div class="form-group">
        <div class="label">
          <label></label>
        </div>
        <div class="field">
          <button class="button bg-main icon-check-square-o"  onclick="fun_sub();" id="sub_btn" type="button"> 提交</button>
        </div>
      </div>
    </form>
  </div>
</div>

</body></html>