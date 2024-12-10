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
		  var falg = "${currentNgsAvailable.falg}";
		  if(falg==4){
			 var previewReport = document.getElementById("previewReport");
			 previewReport.src="${pageContext.request.contextPath}/geneMarkerVw/getGeneMarker?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}";
		     getPage(4);
		  }else if(falg==5){
			 var test = document.getElementById("test");
			 test.src="${pageContext.request.contextPath}/geneMarkerVw/produceReport?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}";
		     getPage(5);
		  }else{
		     $( "#tabs" ).tabs();
		  }
	  });
       function getPage(tab) {
       	$( "#tabs" ).tabs({selected:tab});
       	if(tab==4){
       		$("#wait").show();
				$(".overlay").show();
   			$("#previewReport").load(function(){  
                $("#wait").hide();
                $(".overlay").hide();
            }); 
       	}
       }
     </script>
	  <style type="text/css">
        iframe {min-width: 1720px;}
        .overlay {
		  position: fixed;
		  top: 0;
		  right: 0;
		  bottom: 0;
		  left: 0;
		  background-color: rgba(0,0,0,.8);
		}
	  </style>
	</head>
	<body style="margin: auto;"> 
		<div id="tabs">
		  <ul style="min-width: 1720px;">
		    <li><a href="#tabs-1" onclick="urlRun();" style="cursor: pointer;">LIMS</a></li>
		    <li><a href="#tabs-2" onclick="urlRun1();" style="cursor: pointer;">集群对接</a></li>
		    <li><a href="#tabs-3" onclick="urlRun2();" style="cursor: pointer;">质检文件</a></li>
		    <li><a href="#tabs-4" onclick="urlRun3();" style="cursor: pointer;">筛选位点</a></li>
		    <li><a href="#tabs-5" onclick="urlRun4();" style="cursor: pointer;">报告预览</a></li>
		    <c:if test="${ user.role_id != 8 }">
		    	<li><a href="#tabs-6" onclick="urlRun5();" style="cursor: pointer;">产生报告</a></li>
				<li><a href="#tabs-7" onclick="urlRun6();" style="cursor: pointer;">审核及发送报告</a></li>
		    </c:if>
		    <li style="margin-left: 300px"><a href="#tabs-8" onclick="fun_back();" id="button_back" >返回报告管理</a></li>
		  </ul>
		  <form id="form_back" method="post" action="${pageContext.request.contextPath}/NgsAvailableDataVw/lifeList1" >
			    <input type="hidden" name="life" value="${currentNgsAvailable.life}" id="life">
			    <input type="hidden" name="illumina" value="${currentNgsAvailable.illumina}" id="illumina">
			    <input type="hidden" name="subbarcode_show" value="${currentNgsAvailable.subbarcode_show}" id="subbarcode_hidden">
			    <input type="hidden" name="product_name_show" value="${currentNgsAvailable.product_name_show}" id="product_name_show">
			    <input type="hidden" name="analysis_date_show" value="${currentNgsAvailable.analysis_date_show}" id="analysis_date_hidden">
			    <input type="hidden" name="product_name" value="${currentNgsAvailable.product_name}" id="analysis_date_hidden">
			    <input type="hidden" name="pageNo" value="${currentNgsAvailable.pageNo}" id="pageNo_hidden">
			    <input type="hidden" name="status_show" value="${currentNgsAvailable.status_show}" id="status_show">
		    </form>
	        <script type="text/javascript">
	        	function fun_back(){
	       			$("#form_back").submit();
	        	};
	        	function urlRun(){
	   			  var objFrm = document.getElementById('life');
	   		      objFrm.src = "${pageContext.request.contextPath}/life/addLife?report_id=${currentNgsAvailable.report_id}&subbarcode=${currentNgsAvailable.subbarcode}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&pageNo=${currentNgsAvailable.pageNo}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun1(){
	   			  var objFrm = document.getElementById('ParseFile');
	   		      objFrm.src = "${pageContext.request.contextPath}/life/ParseFile?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun2(){
	   			  var objFrm = document.getElementById('qualityStatFile');
	   		      objFrm.src = "${pageContext.request.contextPath}/qualityStatFileVw/qualityStatFileVwList?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun3(){
	   			  var objFrm = document.getElementById('filter');
	   		      objFrm.src = "${pageContext.request.contextPath}/filter/filterIndex?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun4(){
	   			  var objFrm = document.getElementById('previewReport');
	   		      objFrm.src = "${pageContext.request.contextPath}/geneMarkerVw/getGeneMarker?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRunp4(data, moduleFlag, i){
		   			  var objFrm = document.getElementById('previewReport');
		   				$("#wait").show();
		   				$(".overlay").show();
			   			$("#previewReport").load(function(){  
			                $("#wait").hide();
			                $(".overlay").hide();
			            }); 
			   			if(${currentNgsAvailable.report_id} != data){
			   				urlRun_4(data);
			   				alert("该样本已有报告，请重新点击知识库匹配按钮！");
			   			}else{
			   				objFrm.src = "${pageContext.request.contextPath}/geneMarkerVw/getGeneMarkerData?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}&moduleFlag="+moduleFlag+"&module="+i;
				   		    objFrm.style.display = "block";
			   			}
		    		}
	        	function urlRun5(){
	   			  var objFrm = document.getElementById('test');
	   		      objFrm.src = "${pageContext.request.contextPath}/geneMarkerVw/produceReport?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun6(){
	   			  var objFrm = document.getElementById('reviewAndSendReport');
	   		      objFrm.src = "${pageContext.request.contextPath}/geneMarkerVw/reviewAndSendReport?report_id=${currentNgsAvailable.report_id}&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&status=${currentNgsAvailable.status}&pageNo=${currentNgsAvailable.pageNo}";
	   		      objFrm.style.display = "block";
	    		}
	        	function urlRun_4(data){
	        		window.location.href="${pageContext.request.contextPath}/life/lifeMain?report_id="+data+"&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&product_id=${product.product_id}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}&falg=4";
	    		}
	        	function urlRun_5(data){
          		  window.location.href="${pageContext.request.contextPath}/life/lifeMain?report_id="+data+"&platform=${currentNgsAvailable.platform}&subbarcode=${currentNgsAvailable.subbarcode}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&product_id=${product.product_id}&life=${currentNgsAvailable.life}&illumina=${currentNgsAvailable.illumina}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&product_name_show=${currentNgsAvailable.product_name_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&pageNo=${currentNgsAvailable.pageNo}&falg=5";
	    		}
	        	
	        </script>
		  <div id="tabs-1" style="height:782px;">
		    <iframe id="life" scrolling="no" src="${pageContext.request.contextPath}/life/addLife?report_id=${currentNgsAvailable.report_id}&subbarcode=${currentNgsAvailable.subbarcode}&subbarcode_show=${currentNgsAvailable.subbarcode_show}&analysis_date_show=${currentNgsAvailable.analysis_date_show}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&pageNo=${currentNgsAvailable.pageNo}" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-2" style="height:782px;">
		  	<iframe id="ParseFile" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-3" style="height:782px;">
		  	<iframe id="qualityStatFile" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-4" style="height:782px;">
		  	<iframe id="filter" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-5" style="height:782px;">
		  	<iframe id="previewReport" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-6" style="height:782px;">
		  	<iframe id="test" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		  <div id="tabs-7" style="height:782px;">
		  	<iframe id="reviewAndSendReport" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
		  </div>
		</div>
		<div class="overlay" style="display:none;"></div>
		<div id="wait" style="display:none; z-index:1;position: absolute; margin: 0px; left: 50%; top: 50%; font-weight: bold; font-size: 20px; color: white;">正在加载数据...</div>
	</body>
</html>