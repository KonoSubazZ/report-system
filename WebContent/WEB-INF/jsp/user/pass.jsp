<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn" style="min-width: 1720px;">
<head>
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<title></title>
<link rel="stylesheet" href="css/pintuer.css">
<link rel="stylesheet" href="css/admin.css">
<script type="text/javascript" src="jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jquery/jquery.form.js"></script>
<script src="js/pintuer.js"></script>
<script type="text/javascript">
	function update(){
		if($("#renewpass").val() != "" && $("#new_password").val() != ""){
			$("#message").text("");
			if($("#renewpass").val() == $("#new_password").val()){
				$.ajax({
					url:"${pageContext.request.contextPath}/user/updatepwd.do",
					type:"post",
					data:{
						"user_id":"${sessionScope.user.user_id}",
						"new_password":$("#new_password").val()
					},
					beforeSend:function(){
						$("#message").text("正在处理请稍等...");
						return true;
					},
					success:function(jsonObject){
			
						if(jsonObject.success){
							$("#message").text("密码修改成功");
						}else{
							$("#message").text("密码错误，请重新填写");
						}
					}
					
					
				});
			}
		}else{
			$("#message").text("请填写新密码或者确认密码");
		}
		
	}

</script>
</head>
<body>

<div class="panel admin-panel">
  <div class="panel-head"><strong><span class="icon-key"></span> 修改密码</strong></div>
  <div class="body-content">
    <form method="post" class="form-x" action="" id="passForm">
    <input type="hidden" name="user_id" value="${sessionScope.user.user_id}"/>
      <div class="form-group">
        <div class="label">
          <label for="sitename">管理员帐号：</label>
        </div>
        <div class="field">
          <label style="line-height:33px;">
           ${user.full_name}
          </label>
        </div>
      </div>
      <input type="password" style="display: none;">      
      <div class="form-group">
        <div class="label">
          <label for="sitename">新密码：</label>
        </div>
        <div class="field">
          <input type="password" class="input w50" name="newpass" size="50" id="new_password" placeholder="请输入新密码" data-validate="required:请输入新密码,length#>=3:新密码不能小于3位" />         
       	  <div class="tips"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label for="sitename">确认新密码：</label>
        </div>
        <div class="field">
          <input type="password" class="input w50" name="renewpass" id="renewpass" size="50" placeholder="请再次输入新密码" data-validate="required:请再次输入新密码,repeat#newpass:两次输入的密码不一致" />          
          <div class="tips"></div>
        </div>
      </div>
      
      <div class="form-group">
        <div class="label">
          <label></label>
        </div>
        <div class="field">
          <button class="button bg-main icon-check-square-o" type="button" onclick="update();"> 提交</button>   
          <span id="message" style="color:red;font-size:12px"></span>
        </div>
      </div>      
    </form>
  </div>
</div>
</body></html>