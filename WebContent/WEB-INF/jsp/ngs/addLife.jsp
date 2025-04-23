<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<script type="text/javascript" src="jquery/zeroModal.min.js"></script>
<link href="css/zeroModal.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div class="panel admin-panel" style="width:1800px">
  
	<div class="panel-head"><strong class="icon-reorder"> NGS报告管理>LIMS数据:${currentNgsAvailable.report_id }:${currentNgsAvailable.platform }>${currentNgsAvailable.analysis_date }>${currentNgsAvailable.subbarcode }>${currentNgsAvailable.product_name }</strong> </div>
  <div class="body-content" style="margin-left:100px">
  	
    <form id="SBCForm"  class="form-x">  
    <table style="width:100%">
    <tr>
	     <td width="800px">
	      <div class="form-group">
	      	
	        <div class="field">
	          <button id="sample_lims" style="width: 130px;margin-left: 85px;" class="button bg-main icon-search-plus"  type="submit"> 从LIMS抓取</button>
	          <script type="text/javascript">
	          	$(function(){
	          		$("#sample_lims").click(function(){
	          			$("#SBCForm").validate({
   	    					rules:{
   	    						"subbarcode":{"required":true},
   	    					},
   	    					messages:{
   	    						"subbarcode":{"required":""},
   	    					},
   	    					submitHandler:function(){
   	    						$.ajax({
   				          			url:"${pageContext.request.contextPath}/sampleFile/getSpecimenHeadBySubbarcode",
   				          			type:"post",
   				          			data:{"subbarcode":"${currentNgsAvailable.subbarcode }"},
   				          			dataType:"json",
   				          			success:function(data){
   				          				if(data){
   				          					alert("抓取成功");
   				          					$("#subbarcode").val(data.subbarcode);
   				          					$("#emailaddress").val(data.emailaddress);
   				          					$("#client").val(data.client);
   				          					$("#sales_contact").val(data.sales_contact);
	   				          				$("#commission_date").val(data.commission_date);
		  				          			$("#received_date").val(data.received_date);
		  				          			$("#person_name").val(data.person_name);
		  				          			$("#gender").val(data.gender);
			  				          		$("#age").val(data.age);
			  				         	 	$("#cancertype").val(data.cancertype);
			  				     			$("#pathologicaltype").val(data.pathologicaltype);
			  				       			$("#clinicalremark").val(data.clinicalremark);
			  				       			$("#remark").val(data.remark);
			  				     			$("#specimen_type").val(data.specimen_type);
			  				     			$("#specimen_quantity").val(data.specimen_quantity);
			  				     			$("#hospital").val(data.hospital);
			  				     			$("#collect_date").val(data.collect_date);
			  				     			$("#sample_type").val(data.sample_type);
			  				     			$("#recordercode").val(data.recordercode);
			  				     			$("#customer").val(data.customer);
											$("#disease_type").val(data.disease_type);
											$("#specimenno").val(data.specimenno);
											var label = document.getElementById('specimenno_label');
											if (data.customer.includes("阿克曼") || data.customer.includes("银丰") || data.customer.includes("迪安") || data.hospital.includes("常德") || data.customer.includes("赛福")) {
												$('#specimenno_div').show(); // 显示div
												if (data.customer.includes("阿克曼")) {
													label.innerText = "阿克曼编号：";
												} else if (data.customer.includes("银丰")) {
													label.innerText = "银丰编号：";
												} else if (data.customer.includes("迪安")) {
													label.innerText = "迪安编号：";
												} else if (data.hospital.includes("常德")) {
													label.innerText = "常德病理号：";
												}else if (data.hospital.includes("赛福")) {
													label.innerText = "赛福病理号：";
												}
											} else {
												$('#specimenno_div').hide(); // 隐藏div
											}
   				          				}else{
   				          					alert("抓取失败");
   				          				}
   				          			}
   				          		});
   	    			        }  
   	    				}); 
	          		});
	          	});
	          </script>
	       </div>
	      </div>
	     </td>
	      <td>
	     	<div class="form-group">
	        <div class="field">
	         <button id="sample_lims" <c:if test="${Person_count < 2}">disabled="disabled"</c:if> <c:if test="${Person_count >= 2}">style="background:red;border:0px solid #eee;width: 150px;margin-left: 135px;"</c:if> style="width: 150px;margin-left: 135px;" class="button bg-main icon-search-plus" onclick="open_new()"  type="button"> 搜索同样病人</button>
	          <div class="tips"></div>
	        	<script type="text/javascript">
					//跳到列表
					function open_new(){
							zeroModal.show({
								 url:"${pageContext.request.contextPath}/person/personList?person_name=${sampleFile.person_name}&gender=${sampleFile.gender }&birthday=${sampleFile.birthday}&remark=${sampleFile.remark}",
								 width:"70%",
								 height:"60%",
								 resize:false,
								 opacity:5,
							}); 
					}
				</script>
	        </div>
	      </div>  
      	</td>
     </tr>
     </table>
     </form>
     <form  id="pdl1Form" method="post"  class="form-x">
	 <script type="text/javascript">
	 $(function(){
		getTime("update_date");
		getTime("created_date");
	 });
	</script>
     <table style="width:100%">
     <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>样本编号：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="subbarcode" value="${sampleFile.subbarcode }" name="tested_by" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	     	<div class="form-group">
	        <div class="label" style="width:85px">
	          <label>客户邮箱：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="emailaddress" value="${sampleFile.emailaddress }" name="emailaddress" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>委托人：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="client" value="${sampleFile.client }" name="tested_by" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	     	<div class="form-group">
	        <div class="label" style="width:85px">
	          <label>联系人：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="sales_contact" value="${sampleFile.sales_contact }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>委托日期：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="commission_date" value="${sampleFile.commission_date }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	     	<div class="form-group">
	        <div class="label" style="width:85px">
	          <label>接收日期：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="received_date" value="${sampleFile.received_date }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>姓名：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="person_name" onchange="updatePersonName()" value="${sampleFile.person_name }" />
				<div class="tips"></div>
				<span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
				<script type="text/javascript">
					function updatePersonName(){
						var sub_val = $("#subbarcode").val();
						if(sub_val != ""){
							$.ajax({
								cache: false,
								type: "POST",
								url:"${pageContext.request.contextPath}/sampleFile/updatePersonName", //把表单数据发送到ajax.jsp
								data:{"subbarcode":sub_val,"person_name":$("#person_name").val()}, //要发送的是ajaxFrm表单中的数据
								success:function(data){
									if(data){
										$("#client").val($("#person_name").val());
										alert("修改成功！");
									}else{
										alert("修改失败！");
									}
								}
							});
						}else{
							alert("请先选择样本编号！");
						}
					}
				</script>
	        </div>
	      </div>  
	      </td> 
	     <td>
		      <div class="form-group">
		        <div class="label" style="width:85px">
		          <label>样品类型：</label>
		        </div>
		        <div class="field">
		          	<select id="sample_type" name="sample_type" onchange="updateSmapleType()" class="input w50">
		          	  <option value="" >无</option>
		              <option value="blood" <c:if test="${sampleFile.sample_type=='blood'}">selected="selected"</c:if>>血</option>
		              <option value="tissue" <c:if test="${sampleFile.sample_type=='tissue'}">selected="selected"</c:if>>组织</option>
		            </select>
		          <div class="tips"></div>
		          <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
		          <script type="text/javascript">
		          		function updateSmapleType(){
		          			var sub_val = $("#subbarcode").val();
		          			if(sub_val != ""){
		          				$.ajax({   
  	    							cache: false,
  	    							type: "POST",   
  	    							url:"${pageContext.request.contextPath}/sampleFile/updateSmapleType", //把表单数据发送到ajax.jsp  
  	    							data:{"subbarcode":sub_val,"sample_type":$("#sample_type").val()}, //要发送的是ajaxFrm表单中的数据   
  	    							success:function(data){
  	    								if(data){
	  	    								alert("修改成功！");
  	    								}else{
  	    									alert("修改失败！");
  	    								}
  	    							} 
  	    						}); 
		          			}else{
		          				alert("请先选择样本编号！");
		          			}
		          		}
		          </script>
		        </div>
		      </div>
		     </td>
      </tr>
      <tr>
      	<td>
	     	<div class="form-group">
	        <div class="label" style="width:85px">
	          <label>性别：</label>
	        </div>
	        <div class="field">
	          <%--<input type="text" class="input w50" id="gender" value="${sampleFile.gender }" readonly="readonly" />--%>
				<select id="gender" name="gender" onchange="updateGender()" class="input w50">
					<option value="" >-</option>
					<option value="男" <c:if test="${sampleFile.gender=='男'}">selected="selected"</c:if>>男</option>
					<option value="女" <c:if test="${sampleFile.gender=='女'}">selected="selected"</c:if>>女</option>
				</select>
				<div class="tips"></div>
				<span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
				<script type="text/javascript">
					function updateGender(){
						var sub_val = $("#subbarcode").val();
						if(sub_val != ""){
							$.ajax({
								cache: false,
								type: "POST",
								url:"${pageContext.request.contextPath}/sampleFile/updateGender", //把表单数据发送到ajax.jsp
								data:{"subbarcode":sub_val,"gender":$("#gender").val()}, //要发送的是ajaxFrm表单中的数据
								success:function(data){
									if(data){
										alert("修改成功！");
									}else{
										alert("修改失败！");
									}
								}
							});
						}else{
							alert("请先选择样本编号！");
						}
					}
				</script>
	        </div>
	      </div>  
      	</td>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>年龄：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="age" onchange="updateAge()" value="${sampleFile.age }" />
				<div class="tips"></div>
				<span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
				<script type="text/javascript">
					function updateAge(){
						var sub_val = $("#subbarcode").val();
						if(sub_val != ""){
							$.ajax({
								cache: false,
								type: "POST",
								url:"${pageContext.request.contextPath}/sampleFile/updateAge", //把表单数据发送到ajax.jsp
								data:{"subbarcode":sub_val,"age":$("#age").val()}, //要发送的是ajaxFrm表单中的数据
								success:function(data){
									if(data){
										alert("修改成功！");
									}else{
										alert("修改失败！");
									}
								}
							});
						}else{
							alert("请先选择样本编号！");
						}
					}
				</script>
	        </div>
	      </div>  
	      </td> 
      </tr>
      <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>检测癌种：</label>
	        </div>
	        <div class="field">
				  <input type="text" class="input w50" id="cancertype" value="${sampleFile.cancertype }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div> 
	      </td> 
	      <td>
	      <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>临床备注：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="clinicalremark" value="${sampleFile.clinicalremark }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div> 
	      </td> 
      </tr>
      <tr>
     	<td>
	       <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>病理分型：</label>
	        </div>
	        <div class="field">
				  <input type="text" class="input w50" id="pathologicaltype" value="${sampleFile.pathologicaltype }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
	     <td>
	       <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>备		注：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="remark" value="${sampleFile.remark }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>样品种类：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="specimen_type" value="${sampleFile.specimen_type }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div> 
	      </td> 
	     <td>
	       <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>样本量：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="specimen_quantity" value="${sampleFile.specimen_quantity }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>送检医院：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="hospital" value="${sampleFile.hospital }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div> 
	      </td> 
	     <td>
	       <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>寄样日期：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="collect_date" value="${sampleFile.collect_date }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>创建人编码：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="recordercode" value="${sampleFile.recordercode }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div> 
		</td>
		<td>
	       <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>送检单位：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="customer" value="${sampleFile.customer }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
		 <tr>
			 <td>
				 <div class="form-group" style="margin-right: 50px">
					 <div class="label" style="width:85px">
						 <label>临床诊断：</label>
					 </div>
					 <div class="field">
						 <input type="text" class="input w50" id="disease_type" onchange="updateDiseaseType()" value="${sampleFile.disease_type }" />
						 <div class="tips"></div>
						 <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
						 <script type="text/javascript">
							 function updateDiseaseType(){
								 var sub_val = $("#subbarcode").val();
								 if(sub_val != ""){
									 $.ajax({
										 cache: false,
										 type: "POST",
										 url:"${pageContext.request.contextPath}/sampleFile/updateDiseaseType", //把表单数据发送到ajax.jsp
										 data:{"subbarcode":sub_val,"disease_type":$("#disease_type").val()}, //要发送的是ajaxFrm表单中的数据
										 success:function(data){
											 if(data){
												 alert("修改成功！");
											 }else{
												 alert("修改失败！");
											 }
										 }
									 });
								 }else{
									 alert("请先选择样本编号！");
								 }
							 }
						 </script>
					 </div>
				 </div>
			 </td>
			 <td>
				 <div id="specimenno_div" class="form-group" style="margin-right: 50px">
					 <div class="label" style="width:85px">
						 <label id="specimenno_label" />
					 </div>
					 <div class="field">
						 <input type="text" class="input w50" id="specimenno" onchange="updateSpecimenno()" value="${sampleFile.specimenno }" />
						 <div class="tips"></div>
						 <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
						 <script type="text/javascript">
							 var customer = $("#customer").val();
							 var hospital = $("#hospital").val();
							 var label = document.getElementById('specimenno_label');
							 if (customer.includes("阿克曼") || customer.includes("银丰") || customer.includes("迪安") || hospital.includes("常德")|| customer.includes("赛福")) {
								 $('#specimenno_div').show(); // 显示div
								 if (customer.includes("阿克曼")) {
									 label.innerText = "阿克曼编号：";
								 } else if (customer.includes("银丰")) {
									 label.innerText = "银丰编号：";
								 } else if (customer.includes("迪安")) {
									 label.innerText = "迪安编号：";
								 } else if (hospital.includes("常德")) {
									 label.innerText = "常德病理号：";
								 } else if (customer.includes("赛福")) {
									 label.innerText = "赛福病理号：";
								 }
							 } else {
								 $('#specimenno_div').hide(); // 隐藏div
							 }
							 function updateSpecimenno(){
								 var sub_val = $("#subbarcode").val();
								 if(sub_val != ""){
									 $.ajax({
										 cache: false,
										 type: "POST",
										 url:"${pageContext.request.contextPath}/sampleFile/updateSpecimenno", //把表单数据发送到ajax.jsp
										 data:{"subbarcode":sub_val,"specimenno":$("#specimenno").val()}, //要发送的是ajaxFrm表单中的数据
										 success:function(data){
											 if(data){
												 alert("修改成功！");
											 }else{
												 alert("修改失败！");
											 }
										 }
									 });
								 }else{
									 alert("请先选择样本编号！");
								 }
							 }
						 </script>
					 </div>
				 </div>
			 </td>
		 </tr>
      <%-- <tr>
	      <td>
	       <div class="form-group">
	        <div class="label" style="width:85px">
	          <label>检测产品：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="product_name" value="${sampleFile.product_name }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr> --%>
      </table>
    </form>
  </div>
</div>

</body></html>