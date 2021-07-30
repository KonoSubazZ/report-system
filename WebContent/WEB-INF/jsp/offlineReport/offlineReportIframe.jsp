<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html lang="en">
	<head>
	  <meta charset="utf-8">
	  <title>基因检测报告系统</title>
	  <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/ui/css/ui-lightness/jquery-ui-1.8.18.custom.css">
	  <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script> 
	  <script src="${pageContext.request.contextPath}/jquery/ui/js/jquery-ui-1.8.18.custom.min.js"></script>
	  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	  <script type="text/javascript">
	  $(function() {
		 $( "#tabs" ).tabs();
	  });
       function getPage(tab) {
       	$( "#tabs" ).tabs({selected:tab});
       }
     </script>
	  <style type="text/css">
        iframe {min-width: 1720px;}
	  </style>
	</head>
	<body style="margin: auto;"> 
		<div id="tabs">
		  <ul style="min-width: 1720px;">
		    <li><a href="#tabs-1" onclick="urlRun();" style="cursor: pointer;">样本信息</a></li>
		    <li><a href="#tabs-2" onclick="urlRun1();" style="cursor: pointer;">审核及发送报告</a></li>
		    <li style="margin-left: 300px"><a href="#tabs-3" onclick="fun_back();" id="button_back" >返回报告管理</a></li>
		  </ul>
		  <form id="form_back" method="post" action="${pageContext.request.contextPath}/offlineReport/offlineReportList" >
			    <input type="hidden" name="subbarcode" value="${offlineReportIframeBean.subbarcode}" >
			    <input type="hidden" name="tested_date" value="${offlineReportIframeBean.tested_date}">
			    <input type="hidden" name="status" value="${offlineReportIframeBean.status}" >
			    <input type="hidden" name="pageNo" value="${offlineReportIframeBean.pageNo}" >
		    </form>
	        <script type="text/javascript">
	        	function fun_back(){
	       			$("#form_back").submit();
	        	};
	        	function urlRun(){
	   			  var objFrm = document.getElementById('life');
	   		      objFrm.src = "${pageContext.request.contextPath}/offlineReport/SampleFile?report_id=${offlineReportIframeBean.report_id}&subbarcode=${offlineReportIframeBean.subbarcode}&tested_date=${offlineReportIframeBean.tested_date}&status=${offlineReportIframeBean.status}&pageNo=${offlineReportIframeBean.pageNo}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun1(){
	   			  var objFrm = document.getElementById('reviewAndSendReport');
	   		      objFrm.src = "${pageContext.request.contextPath}/offlineReport/reviewAndSendReport?report_id=${offlineReportIframeBean.report_id}&subbarcode=${offlineReportIframeBean.subbarcode}&tested_date=${offlineReportIframeBean.tested_date}&status=${offlineReportIframeBean.status}&pageNo=${offlineReportIframeBean.pageNo}";
	   		      objFrm.style.display = "block";
	    		}
	        </script>
		  <div id="tabs-1" style="height:782px;">
		    <iframe id="life" scrolling="no" src="${pageContext.request.contextPath}/offlineReport/SampleFile?report_id=${offlineReportIframeBean.report_id}&subbarcode=${offlineReportIframeBean.subbarcode}&tested_date=${offlineReportIframeBean.tested_date}&status=${offlineReportIframeBean.status}&pageNo=${offlineReportIframeBean.pageNo}" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-2" style="height:782px;">
		  	<iframe id="reviewAndSendReport" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		</div>
	</body>
</html>