<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
   <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn" style="min-width: 1000px;">
<head>
	<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title>基因检测报告系统</title>  
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link href="css/1.0.8/iconfont.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
</head>
<body style="background-color:#f2f9fd;" onload="systemTime()">
<div class="header bg-main">
  <div class="logo margin-big-left fadein-top">
    <h1><a href="${pageContext.request.contextPath}/index"><img src="images/y.png" class="radius-big" height="50" alt=""/></a>基因检测报告系统</h1>
  </div>
  <div class="head-l">
		<span class="icon-user button button-little bg-green">&nbsp;当前用户&nbsp;:&nbsp;<span style="color: black">${user.user_account}</span></span>
	&nbsp;&nbsp;
		<span class="button button-little bg-blue icon-clock-o">&nbsp;系统时间&nbsp;:&nbsp;<span id="time" style="color: black"></span></span>
		<script type="text/javascript">
		$(function(){
			var flag = "${user.user_role}";
		});
		//获取系统时间，将时间以指定格式显示到页面。  
	    function systemTime() {  
	    	//获取系统时间。  
	    	var d=new Date(); 
	    	var YY=d.getFullYear();
	    	var MM=d.getMonth()+1;
	    	var DD=d.getDate();
	    	var hh=d.getHours();  
	    	var mm=d.getMinutes();  
	    	var ss=d.getSeconds();  
	    	//将时间显示，时间格式形如：2017-03-15 15:16:10  
	        document.getElementById("time").innerHTML=YY+"年"+(MM<10?'0':'')+MM+"月"+(DD<10?'0':'')+DD+"日   "+(hh<10?'0':'')+hh+"时"+(mm<10?'0':'')+mm+"分"+ (ss<10?'0':'')+ss+"秒";
	        //每隔1000ms执行方法systemTime()。  
	        setTimeout("systemTime()",1000);
	    } 
		</script>
	&nbsp;&nbsp;
  </div>
  <div style="float:right;margin-right: 15px" class="head-l">
	<a class="button button-little bg-red" href="${pageContext.request.contextPath}/user/logout" >
		<span class="icon-power-off"></span>&nbsp;退出登录
	</a>
  </div>
  
</div>
<div class="leftnav">
  <div class="leftnav-title"><strong><span class="icon-list"></span>菜单列表</strong></div>
  <c:if test="${ user.role_id == 3 || user.role_id == 6  || user.role_id == 10 || user.role_id == 12}">
  <h2><span class="icon-pencil-square-o"></span>PCR平台</h2>
  <ul style="display:none">
  	<c:if test="${ user.role_id != 10 }">
	    <li><a href="${pageContext.request.contextPath}/PCR/pcrReportList" target="right"><span class="icon-caret-right"></span>报告管理</a></li>
		<li><a href="${pageContext.request.contextPath}/PCR/addValidateResult" target="right"><span class="icon-caret-right"></span>验证结果</a></li>
	</c:if>
	<li><a href="${pageContext.request.contextPath}/pcrResult/pcrResultList" target="right"><span class="icon-caret-right"></span>PCR查询</a></li>
    <li><a href="${pageContext.request.contextPath}/pcrResult/pcrResultVw" target="right"><span class="icon-caret-right"></span>PCR统计</a></li>
  </ul> 
  </c:if> 
  <c:if test="${ user.role_id == 7 || user.role_id == 6 || user.role_id == 12}">
  <h2><span class="icon-pencil-square-o"></span>病理平台</h2>
  <ul style="display:none">
    <li><a href="${pageContext.request.contextPath}/pdl1/pdl1ReportList" target="right"><span class="icon-caret-right"></span>报告管理</a></li>
    <li><a href="${pageContext.request.contextPath}/pdl1Result/pdl1ResultList" target="right"><span class="icon-caret-right"></span>PDL1结果</a></li>
  </ul>  
  </c:if>
  
  <c:if test="${ user.role_id == 1 || user.role_id == 8 || user.role_id == 6 || user.role_id == 10 || user.role_id == 11 || user.role_id == 12 || user.role_id == 13 || user.role_id == 14 || user.role_id == 15}">
  <h2><span class="icon-pencil-square-o"></span>NGS平台</h2>
  <ul style="display:block">
  	<c:if test="${ user.role_id != 10 && user.role_id != 13 && user.role_id != 15}">
    	<li><a href="${pageContext.request.contextPath}/NgsAvailableDataVw/ngsList" target="right" ><span class="icon-caret-right"></span>报告管理</a></li>
	  	<c:if test="${ user.role_id != 12}">
	 		<li><a href="${pageContext.request.contextPath}/driver/driverUploadData" target="right" ><span class="icon-caret-right"></span>驱动数据上传</a></li>
	 		<li><a href="${pageContext.request.contextPath}/falsePositive/falsePositiveList" target="right" ><span class="icon-caret-right"></span>假阳性</a></li>
	    </c:if>
    </c:if>
    <c:if test="${ user.role_id != 11 && user.role_id != 13 && user.role_id != 15}">
    	<li><a href="${pageContext.request.contextPath}/ngs/queryTool" target="right" ><span class="icon-caret-right"></span>NGS查询</a></li>
    	<li><a href="${pageContext.request.contextPath}/integratedMutationFile/integratedMutationFileList" target="right" ><span class="icon-caret-right"></span>NGS统计</a></li>
 	</c:if>
  	<c:if test="${ user.role_id != 10 && user.role_id != 13 && user.role_id != 15}">
    	<li><a href="${pageContext.request.contextPath}/offlineReport/offlineReportList" target="right" ><span class="icon-caret-right"></span>线下报告管理</a></li>
    </c:if>
    <c:if test="${ user.role_id != 10 && user.role_id != 13}">
    	<li><a href="${pageContext.request.contextPath}/sampleRetrieval/sampleRetrievalList" target="right" ><span class="icon-caret-right"></span>样本检索</a></li>
    </c:if>
    <c:if test="${ user.role_id == 14}">
    		<li><a href="${pageContext.request.contextPath}/NgsAvailableDataVw/comparaResolveData" target="right" ><span class="icon-caret-right"></span>解读数据管理</a></li>
    </c:if>
    <c:if test="${ user.role_id == 13}">
    		<li><a href="${pageContext.request.contextPath}/ngs/testResultExport" target="right" ><span class="icon-caret-right"></span>检测结果导出</a></li>
    </c:if>
  </ul>   
  </c:if>
  <c:if test="${ user.role_id == 9 || user.role_id == 6 || user.role_id == 12 }">
  <h2><span class="icon-pencil-square-o"></span>MSI平台</h2>
  <ul style="display:none">
    <li><a href="${pageContext.request.contextPath}/MsiReportVw/msiList" target="right" ><span class="icon-caret-right"></span>报告管理</a></li>
  </ul>
  </c:if>
  <c:if test="${ user.role_id != 10 && user.role_id != 13 && user.role_id != 15}">
   <h2><span class="icon-pencil-square-o"></span>样本临床信息</h2>
  <ul style="display:block">
   	<li><a href="${pageContext.request.contextPath}/sampleFile/sampleFileList" target="right"><span class="icon-caret-right"></span>样本临床信息</a></li>
  </ul>  
  </c:if>
  <h2><span class="icon-user"></span>用户设置</h2>
  <ul style="display:block">
  	<c:if test="${user.role_id == 6 }">
	    <li><a  href="${pageContext.request.contextPath}/user/list" target="right"><span class="icon-caret-right"></span>用户管理</a></li>
	    <li><a href="${pageContext.request.contextPath}/userRole/userRoleList" target="right"><span class="icon-caret-right"></span>角色管理</a></li>
    </c:if>
    <li><a href="${pageContext.request.contextPath}/user/pass" target="right"><span class="icon-caret-right"></span>密码修改</a></li>
  </ul>   
</div>
<script type="text/javascript">
$(function(){
  $(".leftnav h2").click(function(){
	  $(this).next().slideToggle(200);	
	  $(this).toggleClass("on"); 
  })
  $(".leftnav ul li a").click(function(){
	    $("#a_leader_txt").text($(this).text());
  		$(".leftnav ul li a").removeClass("on");
		$(this).addClass("on");
  })
});
</script>
<ul class="bread">
  <li><a href="images/index.jpg" target="right" class="icon-home"> 首页</a></li>
</ul>
<div class="admin">
	<iframe scrolling="auto" frameborder="0" src="images/index.jpg" name="right" width="100%" height="100%"></iframe>
</div>
</body>
</html>