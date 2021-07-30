<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title>登录</title>  
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <script type="text/javascript" src="jquery/jquery-1.7.2.min.js"></script> 
    <script src="js/pintuer.js"></script>

	<script type="text/javascript">
		//页面加载完成之后
		$(function() {
			//给整个窗口加载注册keydown事件
			$(window).keydown(function(even) {
				if (even.keyCode == 13) {
					login();
				}
			});
			//让账号文本获取焦点
			$("#username").focus();
		});
		function login() {
			//发送ajax请求完成登陆认证
			$.ajax({
					url : "${pageContext.request.contextPath}/login",
					type : "post",
					data : {
						"user_account" : $("#user_account").val(),
						"encoded_password" : $("#encoded_password").val()
					},
					beforeSend : function() {
						$("#message").text("正在处理请稍等...");
						return true;
					},
					success : function(jsonObject) {
						if (jsonObject.success) {
							window.location.href = "${pageContext.request.contextPath}/index";
						} else {
							$("#message").text(jsonObject.errMsg);
						}
					}
				});
		}
	</script>
</head>
<body>
<div class="bg"></div>
<div class="container">
    <div class="line bouncein">
        <div class="xs6 xm4 xs3-move xm4-move">
            <div style="height:150px;"></div>
            <div class="media media-y margin-big-bottom">           
            </div>         
            <form  method="post">
            <div class="panel loginbox" style="margin-top:100px">
                <div class="text-center margin-big padding-big-top"><h1>诺禾致源基因检测报告系统</h1></div>
                <div class="panel-body" style="padding:30px; padding-bottom:10px; padding-top:10px;">
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input type="text" class="input input-big" id="user_account"  placeholder="登录账号" data-validate="required:请填写账号" />
                            <span class="icon icon-user margin-small"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input type="password" class="input input-big" id="encoded_password" placeholder="登录密码" data-validate="required:请填写密码" />
                            <span class="icon icon-key margin-small"></span>
                        </div>
                    </div>
                    <div >
                   	 <span id="message" style="color: red;margin: auto;font-size: 20px"></span>	
                    </div>
                    <%-- <div class="form-group">
                         <div class="field">
                            <input type="text" class="input input-big" name="code" placeholder="填写右侧的验证码" data-validate="required:请填写右侧的验证码" />
                           <img src="images/passcode.jpg" alt="" width="100" height="32" class="passcode" style="height:43px;cursor:pointer;" onclick="this.src=this.src+'?'">  
                                                   
                        </div>
                    </div> --%> 
                </div>
                <div style="padding:30px;"><input type="button" onclick="login();" class="button button-block bg-main text-big input-big" style="border-color:#063966; background-color:#063966" value="登录"></div>
            </div>
            </form>          
        </div>
    </div>
</div>
</body>
</html>