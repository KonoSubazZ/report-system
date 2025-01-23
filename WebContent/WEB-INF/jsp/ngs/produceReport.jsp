<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
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
<script src="${pageContext.request.contextPath}/js/previewImage.js"></script>
<script src="${pageContext.request.contextPath}/js/tools.js"></script>
<style type="text/css">
	td{vertical-align: middle;}
	input::-webkit-input-placeholder{
		 color:red;
	}
	input::-moz-placeholder{   /* Mozilla Firefox 19+ */
		color:red;
	}
	input:-moz-placeholder{    /* Mozilla Firefox 4 to 18 */
		color:red;
	}
	input:-ms-input-placeholder{  /* Internet Explorer 10-11 */
		color:red;
	}
</style>
</head>
<body>
	<div class="panel admin-panel">
		<div class="panel-head">
			<strong class="icon-reorder">
				NGS报告管理>产生报告:${currentNgsAvailableData.report_id}:${currentNgsAvailableData.platform }>${currentNgsAvailableData.analysis_date }>${currentNgsAvailableData.subbarcode }>${currentNgsAvailableData.product_name }
			</strong>
		</div>
	  <div class="body-content" style="margin-left:100px">
	    <form id="ngsForm"  class="form-x">  
	    <input type="hidden" name="report_id" id="report_id" value="${currentNgsAvailableData.report_id}">
	    <input type="hidden" name="platform" value="${currentNgsAvailableData.platform}">
		<input type="hidden" name="analysis_date" value="${currentNgsAvailableData.analysis_date}">
		<input type="hidden" name="subbarcode" value="${currentNgsAvailableData.subbarcode}">
		<input type="hidden" name="product_name" value="${currentNgsAvailableData.product_name}">
		<input type="hidden" name="product_name_chinese" value="${product.product_name_chinese}">
		<input type="hidden" name="created_by" value="${user.user_account}">
		<input type="hidden" name="update_by" value="${user.user_account}">
		<input type="hidden" name="analyzer" value="${user.user_account}">
		<input type="hidden" id="created_date" name="created_date" />
		<input type="hidden" id="update_date" name="update_date">
		<input type="hidden" name="test_id" value="9">
		<input type="hidden" name="moduleFlag" value="${moduleFlag}">
		<script type="text/javascript">
			$(function(){
				getTime("update_date");
				getTime("created_date");
				//获取系统时间。  
				var d=new Date(); 
				var YY=d.getFullYear();
				var MM=d.getMonth()+1;
				var DD=d.getDate();
				var hh=d.getHours();  
				var mm=d.getMinutes();  
				var ss=d.getSeconds();  
				//将时间显示，时间格式形如：2017-03-15 15:16:10  
				$("#report_date").val(YY+'-'+(MM<10?'0':'')+MM+'-'+(DD<10?'0':'')+DD);
				$("#tested_date").val(YY+'-'+(MM<10?'0':'')+MM+'-'+(DD<10?'0':'')+DD);
				$("#checked_date").val(YY+'-'+(MM<10?'0':'')+MM+'-'+(DD<10?'0':'')+DD);
			});
	   </script>
	    <table style="width:100%">
	     <tr>
	       <td>
	       <div class="form-group">
		     <div class="label" style="width:75px">
	          <label>本癌种：</label>
		     </div>
		        <div class="field">
		          <input type="text" class="input w50" id="primary_cancer" name="primary_cancer" value="${diseaseClass.disease_class_chinese}" placeholder="请选择原发癌种"  data-validate="required:请选择原发癌种"/>
		          <input type="hidden" id="primary_cancer_id"  name="primary_cancer_id" value="${diseaseClass.class_id}"/>
		          <script type="text/javascript">
			          $(function(){
			        	  $.post("${pageContext.request.contextPath}/autoComplete/getDiseaseClassChineseAndId", function(data){
			        			$('#primary_cancer').autocomplete(data, {
			        				max : data.length, //列表里的条目数
			        				minChars : 0, //自动完成激活之前填入的最小字符
			        				width : 288, //提示的宽度，溢出隐藏
			        				scrollHeight : 300, //提示的高度，溢出显示滚动条
			        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			        				autoFill : false, //自动填充
			        				formatItem : function(row, i, max) {
			        					return row.name;
			        				},
			        				formatResult : function(row) {
			        					return row.name;
			        				}
			        			}).result(function(event, row, formatted) {
			        				$("#primary_cancer_id").val(row.id);
			        			});
			        		},"json");
			          })
		          </script>
		          <div class="tips"></div>
		        </div>
		       </div>
	       </td>
	       <td>
		       <div class="form-group">
		        <div class="label" style="width:75px">
				 <label>化疗癌种：</label>
				</div>
				<div class="field">
				  <input type="text" id="chem_cancer" name="chem_cancer" class="input w50" value="${chem_cancer}" style="cursor: pointer;" placeholder="请选择化疗癌种" data-validate="required:请选择化疗癌种" />
					<script type="text/javascript">
						$(function(){
							var data = [{name: "非小细胞肺癌"}, {name: "结直肠癌"}, {name: "乳腺癌"}, {name: "胃癌"}, {name: "卵巢癌"}, {name: "睾丸癌"}, {name: "骨肉瘤"}, {name: "前列腺癌"}, {name: "胰腺癌"}, {name: "食管癌"}, {name: "间皮瘤"}, {name: "小细胞肺癌"}, {name: "其他癌种"}];
							$('#chem_cancer').autocomplete(data, {
								max : data.length, //列表里的条目数
								minChars : 0, //自动完成激活之前填入的最小字符
								width : 288, //提示的宽度，溢出隐藏
								scrollHeight : 300, //提示的高度，溢出显示滚动条
								matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
								autoFill : false, //自动填充
								formatItem : function(row, i, max) {
									return row.name;
								},
								formatResult : function(row) {
									return row.name;
								}
							});
						})
					</script>
				<div class="tips"></div>
		        </div>
		       </div>
	       </td>
	       <%-- <c:if test="${currentNgsAvailableData.product_name=='lung'}">
		       <td>
					<div class="form-group" style="margin-right: 50px">
						<div class="label" style="width:75px">
							<label></label>
						</div>
						<div class="field">
							肺癌非鳞癌<input type="radio" name="clinicalremark" checked="checked" value="s1"/>
							肺鳞癌<input type="radio" name="clinicalremark" value="s2"/>
							<div class="tips"></div>
						</div>
					</div>  
				</td>
			</c:if> --%>
	     </tr>
	     <tr>
	     	<td>
	     		<div class="form-group">
		     		<div class="label" style="width:75px">
			          <label>检测产品：</label>
			        </div>
			        <div class="field">
			          <input type="text" class="input w50" id="product_name_chinese"  value="${product.product_name_chinese}" placeholder="必选项！请选择检测产品" data-validate="required:请选择检测产品"/>
			          <input type="hidden" id="product_id" name="product_id" value="${product.product_id}"   />
			          <script type="text/javascript">
			          $(function(){
			        	  $.post("${pageContext.request.contextPath}/autoComplete/getProductNameChineseAndId", 
			        			{product_name:"${currentNgsAvailableData.product_name}"},  
			        			function(data){
			        			$('#product_name_chinese').autocomplete(data, {
			        				max : data.length, //列表里的条目数
			        				minChars : 0, //自动完成激活之前填入的最小字符
			        				width : 288, //提示的宽度，溢出隐藏
			        				scrollHeight : 300, //提示的高度，溢出显示滚动条
			        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			        				autoFill : false, //自动填充
			        				formatItem : function(row, i, max) {
			        					return row.name;
			        				},
			        				formatResult : function(row) {
			        					return row.name;
			        				}
			        			}).result(function(event, row, formatted) {
			        				$("#product_id").val(row.id);
			        			});
			        		},"json");
			          })
			          </script>
			          <div class="tips"></div>
			        </div>
			       </div>
		      </td>
			 <td>
				 <div class="form-group">
					 <div class="label" style="width:75px">
						 <label>靶向癌种：</label>
					 </div>
					 <div class="field">
						 <input type="text" id="target_cancer" name="target_cancer" class="input w50" value="${target_cancer}" style="cursor: pointer;" placeholder="请选择靶向癌种" data-validate="required:请选择靶向癌种" />
						 <script type="text/javascript">
							 $(function(){
								 var data = [{name: "泛癌种"}, {name: "肺癌"}, {name: "结直肠癌"}, {name: "乳腺癌"}];
								 $('#target_cancer').autocomplete(data, {
									 max : data.length, //列表里的条目数
									 minChars : 0, //自动完成激活之前填入的最小字符
									 width : 288, //提示的宽度，溢出隐藏
									 scrollHeight : 300, //提示的高度，溢出显示滚动条
									 matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
									 autoFill : false, //自动填充
									 formatItem : function(row, i, max) {
										 return row.name;
									 },
									 formatResult : function(row) {
										 return row.name;
									 }
								 });
							 })
						 </script>
						 <div class="tips"></div>
					 </div>
				 </div>
			 </td>
	     </tr>
	     <tr>
	     	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>报告模板：</label>
		        </div>
		        <div class="field">
		          <%--<input type="text" class="input w50"  value="" id="template_name" name="template_name" placeholder="请选择报告模板"  data-validate="required:请选择报告模板" />--%>
		          <input type="text" class="input w50"  value="" id="template_name" name="template_name" placeholder="请选择报告模板" />
		          <input type="hidden" id="template_id" name="template_id">
		          <div class="tips"></div>
                    <input id="mybtn" type="button" onclick="moreTemplate();" value="更多" />
		          <script type="text/javascript">
		          	$(function(){
		          		$.post("${pageContext.request.contextPath}/autoComplete/getReportTemplateIdAndName", 
		          			{"product_id":"${product.product_id}","subbarcode":"${currentNgsAvailableData.subbarcode}","flag":"1"},
		          			function(data){
		          			$('#template_name').autocomplete(data, {
		          				max : false, //列表里的条目数
		          				minChars : 0, //自动完成激活之前填入的最小字符
		          				width : 288, //提示的宽度，溢出隐藏
		          				scrollHeight : 300, //提示的高度，溢出显示滚动条
		          				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		          				autoFill : false, //自动填充
		          				formatItem : function(row, i, max) {
		          					return row.name;
		          				},
		          				formatResult : function(row) {
		          					return row.name;
		          				}
		          			}).result(function(event, row, formatted) {
		          				$("#template_id").val(row.id);
		          				if(row.name=='肺癌10基因报告模板-医生版' || row.name=='肠癌10基因模板-组织版'){
		          					$(".pic").prop("hidden",false);
		          				}else{
		          					$(".pic").prop("hidden",true);
		          				}
		          			});
		          		},"json");
		          	});
					function moreTemplate(){
						$.post("${pageContext.request.contextPath}/autoComplete/getReportTemplateIdAndName",
								{"product_id":"${product.product_id}","subbarcode":"${currentNgsAvailableData.subbarcode}","flag":"2"},
								function(data){
									$('#template_name').autocomplete(data, {
										max : false, //列表里的条目数
										minChars : 0, //自动完成激活之前填入的最小字符
										width : 288, //提示的宽度，溢出隐藏
										scrollHeight : 300, //提示的高度，溢出显示滚动条
										matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
										autoFill : false, //自动填充
										formatItem : function(row, i, max) {
											return row.name;
										},
										formatResult : function(row) {
											return row.name;
										}
									}).result(function(event, row, formatted) {
										$("#template_id").val(row.id);
										if(row.name=='肺癌10基因报告模板-医生版' || row.name=='肠癌10基因模板-组织版'){
											$(".pic").prop("hidden",false);
										}else{
											$(".pic").prop("hidden",true);
										}
									});
								},"json");
						document.getElementById('mybtn').style.backgroundColor='red';
					}
		          </script>
		        </div>
		      </div>
		    </td>
		    <td>
		       <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>报告时间：</label>
		        </div>
		        <div class="field">
		          <input type="text" id="report_date" name="report_date" class="input w50" value="" style="cursor: pointer;" placeholder="请选择日期" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" data-validate="required:请选择日期" />
		          <div class="tips"></div>
		        </div>
		      </div>  
	      	</td>
	     </tr>
	     <tr>
	    	<td>
				<div class="form-group" style="margin-right: 50px">
					<div class="label" style="width:75px">
						<label>检测人：</label>
					</div>
					<div class="field">
						<c:choose>
                            <c:when test="${user.user_account=='huangmanqing'}"><input type="text" class="input w50"
                                                                                       value="1" name="tested_by"
                                                                                       placeholder="盖章版请输入检测人"/></c:when>
                            <c:when test="${user.user_account=='wangxingsui'}"><input type="text"
                                                                                      class="input w50" value="2"
                                                                                      name="tested_by"
                                                                                      placeholder="盖章版请输入检测人"/></c:when>

							<c:when test="${user.user_account=='sunpeiya'}"><input type="text" class="input w50" value="3" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
							<c:when test="${user.user_account=='gaoyuan'}"><input type="text" class="input w50" value="4" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
							<c:when test="${user.user_account=='liulijie'}"><input type="text" class="input w50" value="5" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
							<c:when test="${user.user_account=='wangyiping'}"><input type="text" class="input w50" value="6" name="tested_by" placeholder="盖章版请输入检测人" /></c:when>
							<c:otherwise><input type="text" class="input w50" value="" name="tested_by" placeholder="盖章版请输入检测人" /></c:otherwise>
						</c:choose>
						<div class="tips"></div>
					</div>
				</div>  
			</td>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label >检测时间：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" id="tested_date"  name="tested_date" data-validate="required:请选择日期" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	     <tr>
			<td>
				<div class="form-group">
					<div class="label" style="width:75px">
						<label>复核人：</label>
					</div>
					<div class="field">
						<input type="text" class="input w50" value=""  id="checked_by" name="checked_by" placeholder="盖章版请输入复核人" />
						<div class="tips"></div>
					</div>
				</div> 
			</td> 
			<td>
		       <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>复核时间：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" id="checked_date"  name="checked_date" data-validate="required:请选择日期" />
		          <div class="tips"></div>
		        </div>
		      </div>  
	      	</td>
	     </tr>
		<tr class="pic" hidden="true">
	     	<td colspan="2">
	     	  <div class="label" style="float: left;margin-left: 25px;margin-top: 10px;">
				<label>镜下图像1：</label>
		      </div>
		      <div class="field" style="float:left;margin-left: 7px;width: 109px" id="preview3">
		         <button type="button" class="button bg-main icon-image" onclick="$('#previewImg3').click();"> 插入图片</button>
	             <img id="imghead3" onclick="$('#previewImg3').click();">
		      </div>
		      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview3','previewImg3','imghead3','tumorpuritypictureone')" style="display: none;" id="previewImg3">
		      <input type="hidden" id="tumorpuritypictureone" name="tumorpuritypictureone">
		      <div class="label" style="float: left;margin-left: 285px;margin-top: 10px;">
				<label>镜下图像2：</label>
		      </div>
		      <div class="field" style="float:left;margin-left: 15px;width: 109px" id="preview4">
		        <button type="button" class="button bg-main icon-image" onclick="$('#previewImg4').click();"> 插入图片</button>
	            <img id="imghead4" onclick="$('#previewImg4').click();">
		      </div>
		      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview4','previewImg4','imghead4','tumorpuritypicturetwo')" style="display: none;" id="previewImg4">
		      <input type="hidden" id="tumorpuritypicturetwo" name="tumorpuritypicturetwo">
	     	  <div class="label" style="float: left;margin-left: 195px;margin-top: 10px;">
				<label>镜下图像3：</label>
		      </div>
		      <div class="field" style="float:left;margin-left: 8px;width: 109px" id="preview5">
		         <button type="button" class="button bg-main icon-image" onclick="$('#previewImg5').click();"> 插入图片</button>
	             <img id="imghead5" onclick="$('#previewImg5').click();">
		      </div>
		      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview5','previewImg5','imghead5','tumorpuritypicturethree')" style="display: none;" id="previewImg5">
		      <input type="hidden" id="tumorpuritypicturethree" name="tumorpuritypicturethree">
		    </td> 
	      </tr>
		<tr class="pic" hidden="true">
			<td colspan="2">
				<div class="form-group" style="margin-right: 50px; margin-top: 12px;">
					<div class="label" style="width:75px;">
						<label>镜下描述：</label>
					</div>
					<div class="field">
						<textarea  class="input" rows="3" cols="75" style="width: 1145px;" name="tumorpuritydescription"></textarea>
						<div class="tips"></div>
					</div>
				</div>
	    </td> 
      </tr>
	     <tr>
	     	<td>
		       <div class="form-group">
		        <div class="label" style="width:75px">
		          <label></label>
		        </div>
		        <div class="field">
		        </div>
		      </div> 
	      	</td>
	     	<td>
		       <div class="form-group">
		        <div class="label" style="width:75px">
		          <label></label>
		        </div>
		        <div class="field">
		       	  <button id="reportBtn" class="button bg-main icon-check-square-o" type="submit"> 产生报告</button>
		       	  <span id="message" style="color: red;font-size: 14px"></span>
		        </div>
		        <script type="text/javascript">
		        	$(function(){
		        		$.post("${pageContext.request.contextPath}/life/getStatus",{"report_id":"${currentNgsAvailableData.report_id}"},
		    					function(data){
					        		if(data=="报告审核通过"){
					        			$("#reportBtn").prop("disabled","disabled");
						        	}
		    		    		},"text");
		        		
		        		$("#reportBtn").click(function(){
		        			$("#ngsForm").validate({
	   	    					rules:{
	   	    						// "tested_by":{"required":true},"tested_date":{"required":true},
	   	    						// "checked_by":{"required":true},"checked_date":{"required":true},
	   	    						"template_name":{"required":true},"report_date":{"required":true},
	   	    						"disease_class_chinese":{"disease_class_chinese":true},
	   	    						"product_name_chinese":{"product_name_chinese":true},
	   	    						"chem_cancer":{"required":true},
	   	    						"target_cancer":{"required":true}
	   	    					},
	   	    					messages:{
	   	    						// "tested_by":{"required":""},"tested_date":{"required":""},
	   	    						// "checked_by":{"required":""},"checked_date":{"required":""},
	   	    						"template_name":{"required":""},"report_date":{"required":""},
	   	    						"disease_class_chinese":{"required":""},
	   	    						"product_name_chinese":{"required":""},
	   	    						"chem_cancer":{"required":""},
	   	    						"target_cancer":{"required":""}
	   	    					},
	   	    					submitHandler:function(){
	   	    						var folder="";
	   	    						if($("#template_id").val()==1){
	   	    							folder="NGS/PATIENT";
	   	    						}else{
	   	    							folder="NGS/DOCTOR";
	   	    						}
	   	    						$.ajax({url:"${pageContext.request.contextPath}/life/updateProductByProductId",
	   			          				data:{"primary_cancer_id":$("#primary_cancer_id").val(),
	   			          					"product_id":$("#product_id").val(),
	   			          					"report_id":"${currentNgsAvailableData.report_id }",
	   	        							"platform":"${currentNgsAvailableData.platform}",
	   	        							"analysis_date":"${currentNgsAvailableData.analysis_date}",
	   	        							"subbarcode":"${currentNgsAvailableData.subbarcode}",
	   	        							"product_name":"${currentNgsAvailableData.product_name}",
	   	        							"analyzer":"${user.user_account}"},
	   			          				success:function(data){
	   			          					if($("#report_id").val() == data){
		   			          					$('#report_id').val(data);
			   			          				$.ajax({
			   	   	    							cache: false,
			   	   	    							type: "POST",   
			   	   	    							url:"${pageContext.request.contextPath}/ngs/createReport", //把表单数据发送到ajax.jsp  
			   	   	    							data:$('#ngsForm').serialize(), //要发送的是ajaxFrm表单中的数据   
			   	   	    							dataType:"json",
						   	    					beforeSend:function(){
						   	    						$("#message").text("正在产生报告...");
						   	    						$("#reportBtn").attr("disabled","disabled");
						   	    						return true;
						   	    					},
			   	   	    							success:function(data){
			   	   	    								$("#message").text("");
			  	   	    								$("#report_date").val("");
			  	   	    								$.post("${pageContext.request.contextPath}/geneMarkerVw/deleteRpVariantOrder",{"analysis_report_id":"${currentNgsAvailableData.report_id}"},
			   	   	  			    						function(data){
			   	   	  			    		    		});
			   	   	    								if(data>0){
			   	   	    									if(confirm("报告生成成功！是否立即下载文件？")){
			   	   	    										window.location.href="${pageContext.request.contextPath}/ngs/download?report_id="+data;
			   	   	    									}
			   	   	    								}else{
			   	   	    									alert("报告生成失败!");
				   	   	    								$("#message").text("报告生成失败!");
			   	   	    								}
						   	    						$("#reportBtn").removeAttr("disabled");
			   	   	    							} 
			   	   	    						});
	   			          					}else{
	   			          						alert("该样本已有报告，请重新点击生成报告按钮！");
	   			          						parent.urlRun_5(data);
	   			          					}
	   				          			},
	   			          			dataType:"json"});
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

</body>
</html>
