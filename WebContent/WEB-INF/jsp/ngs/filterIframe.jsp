<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html><head>
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基因检测报告系统</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/jquery/life/popui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<link rel="stylesheet" href="css/life/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css" />
<script src="${pageContext.request.contextPath}/js/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sweet-alert.css">
<style>
#virtuals-2014 {
	position: relative;
	margin: 10px auto;
	width: 100%;
	height: 100%;
	overflow: hidden; 
	/*border: 1px solid #eee*/
}

#virtuals-2014 .close:hover {
	color: #aaa;
	text-decoration: none
}

#virtuals-2014 .close {
	position: absolute;
	right: 5px;
	top: 5px;
	z-index: 1;
	font: 20px simsun;
	color: #aaa
}

.root61 #virtuals-2014 .close {
	right: 10px
}

#virtuals-2014 .mt ul {
	width: 218px
}

#virtuals-2014 .mt ul li.current a {
	border-bottom: 0;
	background-color:#0ae;
}

#virtuals-2014 .mt li {
	position: relative;
	border-right: 1px solid #eee;
	border-bottom: 1px solid #eee;
	height: 84px;
	overflow: hidden;
	-webkit-transition: height .2s ease-in-out;
	-moz-transition: height .2s ease-in-out;
	-ms-transition: height .2s ease-in-out;
	-o-transition: height .2s ease-in-out;
	transition: height .2s ease-in-out
}

#virtuals-2014 .mt li.current {
	border-bottom: 0
}

#virtuals-2014.hover .mt li a {
	top: -42px
}

#virtuals-2014.hover .mt li {
	height: 29px
}

#virtuals-2014 .mt li a {
	position: relative;
	top: 0;
	float: left;
	display: inline;
	width: 62px;
	height: 83px;
	padding: 0 5px;
	text-align: center;
	-webkit-transition: top .2s ease-in-out;
	-moz-transition: top .2s ease-in-out;
	-ms-transition: top .2s ease-in-out;
	-o-transition: top .2s ease-in-out;
	transition: top .2s ease-in-out
}

#virtuals-2014 .mt ul.fore2 {
}

.root61 #virtuals-2014 .mt li a {
	padding: 0 17px
}

#virtuals-2014 .mt .fore4,#virtuals-2014 .mt .fore8 {
	border-right: 0
}

#virtuals-2014 .mt li a s {
	display: block;
	width: 42px;
	height: 34px;
	margin-top: 10px;
	margin-bottom: 5px;
	background-image: url(http://misc.360buyimg.com/product/skin/2013/i/virtuals-20140606.png);
	background-repeat: no-repeat
}

#virtuals-2014 .mt li.fore2 a s {
	background-position: -64px 0
}

#virtuals-2014 .mt li.fore3 a s {
	background-position: -126px 0
}

#virtuals-2014 .mt li.fore4 a s {
	background-position: -189px 0
}

#virtuals-2014 .mt li.fore5 a s {
	background-position: 0 -50px
}

#virtuals-2014 .mt li.fore6 a s {
	background-position: -64px -50px
}

#virtuals-2014 .mt li.fore7 a s {
	background-position: -126px -50px
}

#virtuals-2014 .mt li.fore8 a s {
	background-position: -189px -50px
}

#virtuals-2014 .mt li a i {
	display: block;
	width: 0;
	height: 0;
	overflow: hidden;
	margin-left: 16px;
	margin-top: 6px;
	border-style: solid;
	border-width: 3px 3px 0;
	border-color: #aaa #fff #fff;
	*margin-left: 0
}

#virtuals-2014.hover ul.fore1 a i {
	display: none
}

#virtuals-2014.hover ul.fore2 {
	display: none
}

#virtuals-2014 .mc {
	position: relative;
	height: 580px
}

#virtuals-2014 .mc .virtuals-iframes {
	position: absolute;
	top: 0;
	left: 0
}

.root61 #virtuals-2014 {
	width: 100%
}

.root61 #virtuals-2014 .mt ul {
	width: 518px
}

#virtuals-2014 .mt li .hot {
	background: url(http://img12.360buyimg.com/da/jfs/t412/295/467335180/598/af76a604/541f893aN662da9bc.png) no-repeat;
	width: 24px;
	height: 14px;
	display: inline-block;
	*display: inline;
	*zoom: 1;
	position: absolute;
	right: 6px;
	bottom: 34px
}

#virtuals-2014.hover .mt li .hot {
	display: none
}
</style>
<script type="text/javascript">
	window.pageConfig={
	compatible:true,
	navId:"home",
	enableArea: true
	};
</script>
</head>
<body class="root61">
<div class="panel-head"><strong class="icon-reorder"> NGS报告管理>筛选位点:${currentNgsAvailable.report_id }:${currentNgsAvailable.platform }>${currentNgsAvailable.analysis_date }>${currentNgsAvailable.subbarcode }>${currentNgsAvailable.product_name }</strong> </div>
<div id="virtuals-2014" class="m _520_a_lifeandjourney_1 hover" data-ui="u-tab|&amp;|current">
    <div class="mt" >
    	<ul class="fore1 lh" style="float: left; display:none">
            <li data-ui="tab-nav" class="fore1 abtest_huafei current">
            	<a id="clickOne" href="javascript:void(0);" onclick="urlRun();"><s></s>筛选-SnpIndel<i></i></a>
            </li>
            <li data-ui="tab-nav" class="fore5 abtest_jipiao">
            	<a id="clickTwe" href="javascript:void(0);" onclick="urlRun1();"><s></s>筛选-CNV<i></i></a>
            </li>
            <li data-ui="tab-nav" class="fore3 abtest_caipiao">
            	<a id="clickThree" href="javascript:void(0);" onclick="urlRun2();"><s></s>筛选-Fusion<i></i></a>
            </li>
            <li data-ui="tab-nav" class="fore3 abtest_caipiao">
            	<a id="clickFour" href="javascript:void(0);" onclick="urlRun3();"><s></s>筛选-Chemical<i></i></a>
            </li>
             <li data-ui="tab-nav" class="fore3 abtest_caipiao">
            	<a id="clickFive" href="javascript:void(0);" onclick="urlRun4();"><s></s>筛选-CR<i></i></a>
            </li>
            <li data-ui="tab-nav" class="fore3 abtest_caipiao">
                <a id="clickSix" href="javascript:void(0);" onclick="urlRun5();"><s></s>筛选-PD<i></i></a>
            </li>
            <li data-ui="tab-nav" class="fore3 abtest_caipiao">
                <a id="clickSeven" href="javascript:void(0);" onclick="urlRun6();"><s></s>筛选-QC<i></i></a>
            </li>
       </ul>
		<div style="float: left; padding:13px 0px 20px 50px;">
	         <input type="radio" name="tableName" id="subBut_SnpIndel" onchange="subBut1();" checked="checked" value="SnpIndel"/> SnpIndel &nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="tableName" id="subBut_CNV" onchange="subBut2();" value="CNV"/> CNV	  &nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="tableName" id="subBut_Fusion" onchange="subBut3();" value="Fusion"/> Fusion   &nbsp;&nbsp;&nbsp;&nbsp;
        	 <input type="radio" name="tableName" id="subBut_Chemical" onchange="subBut4();" value="Chemical_all"/> Chemical   &nbsp;&nbsp;&nbsp;&nbsp;
         	 <c:if test="${currentNgsAvailable.platform == 'Illumina'}">
         	 	<input type="radio" name="tableName" id="subBut_CR" onchange="subBut5();" value="CR_ALL"/> CR   &nbsp;&nbsp;&nbsp;&nbsp;
         	 	<input type="radio" name="tableName" id="subBut_PD" onchange="subBut6();" value="PD"/> PD   &nbsp;&nbsp;&nbsp;&nbsp;
         	 	<%--<input type="radio" name="tableName" id="subBut_QC" onchange="subBut7();" value="QC"/> QC   &nbsp;&nbsp;&nbsp;&nbsp;--%>
       		 </c:if>
         </div>
        <c:if test="${ user.role_id == 11 || user.role_id == 8 || user.role_id == 6 || user.role_id == 1 }">
	        <div style="float: left; padding-left: 50px; padding-top: 0px;">
	        	 <button class="button border-main icon-search" onclick="matchingSite();" id="machingSite" > 匹配位点</button>&nbsp;&nbsp;&nbsp;&nbsp;
	            <span id="message" style="color:red;"></span>
	        </div>
	        <div style="float: left; padding-left: 50px; padding-top: 0px;">
	        	<button class="button border-main" onclick="auditing();" id="auditing">生信审核</button>&nbsp;&nbsp;&nbsp;&nbsp;
	            <span id="message_auditing" style="color:red;"></span>
	        </div>
	        <div style="float: left; padding-left: 50px; padding-top: 0px;">
	        	<button class="button border-main" onclick="showMutationsDialog()" id="auditing">输入突变数目</button>
	        </div>
	    </c:if>
        <script type="text/javascript">
        	//alert('${currentNgsAvailable.platform}');
        	$(function(){
  				$("#message").text("");
  				$.post("${pageContext.request.contextPath}/life/getStatus",{"report_id":"${currentNgsAvailableData.report_id}"},
    					function(data){
			        		if(data=="报告审核通过" || data=="报告发送成功"){
			        			$("#machingSite").prop("disabled","disabled");
			        			$("#auditing").prop("disabled","disabled");
				        	}
    		    		},"text");
        	});
        	//生信审核标识
        	function auditing(){
        		$("#auditing").attr("disabled","true");
        		$.ajax({
        			url:"${pageContext.request.contextPath}/matchingSite/auditing",
        			type:"post",
        			data:{
        				  "report_id":"${currentNgsAvailable.report_id}"
        				 },
        			dataType:"text",
        			success:function(result){
        				if(result){
	        				$("#message_auditing").text("审核成功！");
        				}else{
	        				$("#message_auditing").text("审核失败！");
        				}
        				$("#auditing").removeAttr("disabled");
        			}
        		});
        	}
        	// 展示突变数目对话框
        	function showMutationsDialog(index){
        		varDrugIndex = index;
        	    $("#Loci_dialog").dialog({
        	        width:'30%',
        	        height:'auto',
        	        position : {
        	      　　　　　　my: "center",
        	      　　　　　　at: "center",
        	      　　　　　　of: window,
        	      　　　　　　collision: "fit",
        	      　　　　　　using: function( pos ) {
        	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
        	      　　　　　　　　if ( topOffset < 0 ) {
        	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
        	      　　　　　　　　}
        	      　　　　　　}
        	      　　},
        	        closeOnEscape:true,//右上角没有叉号
        	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
        	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
        	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
        	        	initTddDialog();
        	        },
        	        close:function(e, ui) {
        	        	$(this).dialog("destroy");//关闭当前弹窗
        	        	closeTddDialog();
        	        },
        	        buttons:{//设置页面的按钮
        	            "保存":function(){ 
        	            	saveMutationsNum();
        	            }           
        	        }
        	    });
        	}
        	function saveMutationsNum(){
        		var data = $("#mut_form").serialize();
        		$.ajax({
        			url:"${pageContext.request.contextPath}/matchingSite/saveMutationsNum",
        			type:"post",
        			data:data,
        			dataType:"text",
        			success:function(result){
        				if(result =='true'){
        					alert("保存成功！");
        					$("#Loci_dialog").dialog("destroy");
        					closeTddDialog();
        				}else{
        					alert("请把数据填写完整！");
        				}
        			}
        		});
        	}
        	function initTddDialog(){
        		var date = $("input[name='tableName']:checked").val();
        		var str = '<input type="hidden" name="subbarcode" value="${currentNgsAvailable.subbarcode}"/>';
        		str += '<input type="hidden" name="analysis_date" value="${currentNgsAvailable.analysis_date}"/>';
        		if(date == "SnpIndel"){
        			str +='<input type="number" name="SNP" class="input w50" value="" style="margin-bottom:5px;" placeholder="请输入SNP突变数目"  />';
        			str +='<input type="number" name="Indel" class="input w50" value="" placeholder="请输入Indel突变数目" />';
        		}else{
        			str +='<input type="number" name="'+date+'" class="input w50" value="" placeholder="请输入'+date+'突变数目" />';
        		}
        		$('#mut_form').html(str);
        	}
        	function closeTddDialog(){
        		$("#mut_form").html("");
        	}
        	function matchingSite(){
        		$("#machingSite").attr("disabled","true");
        		$.ajax({
        			url:"${pageContext.request.contextPath}/matchingSite/matchingSite",
        			type:"post",
        			data:{"report_id":"${currentNgsAvailable.report_id}",
        					  "subbarcode":"${currentNgsAvailable.subbarcode}",
        					  "platform":"${currentNgsAvailable.platform}",
        					  "analysis_date":"${currentNgsAvailable.analysis_date}",
        					  "product_name":"${currentNgsAvailable.product_name}",
        					  "whatTable":$("#whatTable").val(),
        					  "user":"${user.user_account}"},
        			dataType:"text",
        			success:function(result){
        				if(result=='true'){
        					var whatTable = $("#whatTable").val();
        					if(whatTable=="SnpIndel"){
        						urlRun();
        					}else if(whatTable=="CNV"){
        						urlRun1();
        					}else if(whatTable=="Fusion"){
        						urlRun2();
        					}else if(whatTable=="Chemical"){
        						urlRun3();
        					}else if(whatTable=="CR"){
        						urlRun4();
        					}else if(whatTable=="PD"){
                                urlRun5();
                            }
	        				$("#message").text("匹配位点成功！");
        				}else{
	        				$("#message").text("匹配位点失败！");
        				}
        				$("#machingSite").removeAttr("disabled");
        			}
        		});
        	}
       		function subBut1(){
       			$("#message").text("");
       			$("#clickOne").trigger("click");
       			$("#whatTable").val("SnpIndel");
       		}
       		function subBut2(){
       			$("#message").text("");
       			$("#clickTwe").trigger("click"); 
       			$("#whatTable").val("CNV");
       		}
       		function subBut3(){
       			$("#message").text("");
       			$("#clickThree").trigger("click");
       			$("#whatTable").val("Fusion");
       		}
       		function subBut4(){
       			$("#message").text("");
       			$("#clickFour").trigger("click");
       			$("#whatTable").val("Chemical");
       		}
       		function subBut5(){
       			$("#message").text("");
       			$("#clickFive").trigger("click");
       			$("#whatTable").val("CR");
       		}
            function subBut6(){
                $("#message").text("");
                $("#clickSix").trigger("click");
                $("#whatTable").val("PD");
            }
            function subBut7(){
                $("#message").text("");
                $("#clickSeven").trigger("click");
                $("#whatTable").val("PD");
            }
       		function urlRun(){
       			 //alert('${currentNgsAvailable.platform}');
   			  var objFrm = document.getElementById('filterSnpIndel');
   			  if('${currentNgsAvailable.platform}' == "Life"){
   				 objFrm.src = "${pageContext.request.contextPath}/filterSnpIndel/lifeSnpIndelList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
   			  }else{
   		     	 objFrm.src = "${pageContext.request.contextPath}/filterSnpIndel/illuminaSnpIndelList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
   			  }
   		      objFrm.style.display = "block";
    		}
        	function urlRun1(){
   			  var objFrm = document.getElementById('cnv');
   				if('${currentNgsAvailable.platform}' == "Life"){
   		     		 objFrm.src = "${pageContext.request.contextPath}/filterCnv/lifeCnvlList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
   				}else{
   					objFrm.src = "${pageContext.request.contextPath}/filterCnv/illuminaCnvlList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
   				}
   		     	 objFrm.style.display = "block";
    		}
        	function urlRun2(){
   			  var objFrm = document.getElementById('fusion');
   			  if('${currentNgsAvailable.platform}' == "Life"){
   		      	objFrm.src = "${pageContext.request.contextPath}/filterFusion/lifeFusionList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
   			  }else{
   				objFrm.src = "${pageContext.request.contextPath}/filterFusion/illuminaFusionList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";  
   			  }
   		      	objFrm.style.display = "block";
    		}
        	function urlRun3(){
     			  var objFrm = document.getElementById('chemical');
     			  if('${currentNgsAvailable.platform}' == "Life"){
     		      	objFrm.src = "${pageContext.request.contextPath}/filteChemical/lifeChemicalList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
     			  }else{
     				objFrm.src = "${pageContext.request.contextPath}/filteChemical/illuminaChemicalList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";  
     			  }
     		      	objFrm.style.display = "block";
      		}
        	function urlRun4(){
	   			  var objFrm = document.getElementById('cr');
	   			  if('${currentNgsAvailable.platform}' == "Illumina"){
	   				objFrm.src = "${pageContext.request.contextPath}/filterCr/illuminaCrList?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}&report_id=${currentNgsAvailable.report_id}";
	   			  }
	   		      	objFrm.style.display = "block";
    		}
            function urlRun5(){
                var objFrm = document.getElementById('pd');
                if('${currentNgsAvailable.platform}' == "Illumina"){
                    objFrm.src = "${pageContext.request.contextPath}/filterPd/illuminaPd?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
                }
                objFrm.style.display = "block";
            }
            function urlRun6(){
                var objFrm = document.getElementById('qc');
                if('${currentNgsAvailable.platform}' == "Illumina"){
                    objFrm.src = "${pageContext.request.contextPath}/filterQc/illuminaQc?subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}";
                }
                objFrm.style.display = "block";
            }
        </script>
    </div>
    <input type="hidden" id="whatTable" value="SnpIndel">
    <div class="mc">
        <div data-ui="tab-content" class="virtuals-iframes" style="display: block; width:100%; height:100%;" data-loaded="true">
            <iframe id="filterSnpIndel" scrolling="auto" <c:if test="${currentNgsAvailable.platform == 'Life'}"> src="${pageContext.request.contextPath}/filterSnpIndel/lifeSnpIndelList?report_id=${currentNgsAvailable.report_id}&subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}"  </c:if>
              									<c:if test="${currentNgsAvailable.platform == 'Illumina'}"> src = "${pageContext.request.contextPath}/filterSnpIndel/illuminaSnpIndelList?report_id=${currentNgsAvailable.report_id}&subbarcode=${currentNgsAvailable.subbarcode}&platform=${currentNgsAvailable.platform}&analysis_date=${currentNgsAvailable.analysis_date}&product_name=${currentNgsAvailable.product_name}"</c:if> width="100%" height="100%" frameborder="0"></iframe>
        </div>
        <div data-ui="tab-content" class="virtuals-iframes hide" data-loaded="true" style="display: none; width:100%; height:100%;">
            <iframe id="cnv" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
        </div>
        <div data-ui="tab-content" class="virtuals-iframes hide" data-loaded="true" style="display: none;  width:100%; height:100%;">
            <iframe id="fusion" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
        </div>
         <div data-ui="tab-content" class="virtuals-iframes hide" data-loaded="true" style="display: none;  width:100%; height:100%;">
            <iframe id="chemical" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
        </div>
         <div data-ui="tab-content" class="virtuals-iframes hide" data-loaded="true" style="display: none;  width:100%; height:100%;">
            <iframe id="cr" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
        </div>
        <div data-ui="tab-content" class="virtuals-iframes hide" data-loaded="true" style="display: none;  width:100%; height:100%;">
            <iframe id="pd" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
        </div>
        <div data-ui="tab-content" class="virtuals-iframes hide" data-loaded="true" style="display: none;  width:100%; height:100%;">
            <iframe id="qc" scrolling="auto" width="100%" height="100%" frameborder="0"></iframe>
        </div>
    </div>
</div>
<div id="Loci_dialog"  style="display: none;  background-color:darkgrey">
	<form style="margin-top:20px;margin-left:30px;" id="mut_form"></form>
</div>


</body></html>