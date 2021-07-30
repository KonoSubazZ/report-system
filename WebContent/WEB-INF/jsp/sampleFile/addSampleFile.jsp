<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-cn" style="min-width: 1720px;">
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
<script type="text/javascript">

</script>
</head>
<body>
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>添加样本</strong></div>
  <div class="body-content" style="margin-left:100px">
    <form class="form-x" id="sampleFileForm">  
    <table style="width:100%">
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>委托人：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="client" id="client" data-validate="required:请输入委托人" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>联系人：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="sales_contact" id="sales_contact" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>委托日期：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" placeholder="请选择日期" name="commission_date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" style="cursor: pointer;" data-validate="required:请选择日期" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>接收日期：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" placeholder="请选择日期" name="received_date" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>样本编号：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="subbarcode" id="subbarcode" data-validate="required:请输入样本编号" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
		      <div class="form-group" style="margin-left: 20px">
		        <div class="label" style="width:85px">
		          <label>患者筛选号：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="barcode" id="barcode" data-validate="required:请输入患者筛选号" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>姓名：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="person_name" id="person_name" data-validate="required:请输入姓名" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>性别：</label>
		        </div>
		        <div class="field">
		          	<select name="gender" class="input w50">
		              <option value="男" selected="selected">男</option>
		              <option value="女">女</option>
		            </select>
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>出生日期：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" placeholder="请选择日期" name="birthday" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" style="cursor: pointer;" data-validate="required:请选择日期" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
		     <td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>疾病种类：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="disease_type" id="disease_type" />
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	    <tr >
	    	<td>
		      <div class="form-group" style="margin-left: 26.5px;">
		        <div class="label">
		          <label>样品种类：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="specimen_type" id="specimen_type"/>
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
	    	<div class="form-group" style="margin-left: 0px;">
		        <div class="label" style="width:106.8px">
		          <label>样本量(含单位)：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="specimen_quantity" id="specimen_quantity"/>
		          <div class="tips"></div>
		        </div>
		      </div>
		      
		     </td>
	     </tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>送检医院：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" value="" name="hospital" id="hospital"/>
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		          <label>送检日期：</label>
		        </div>
		        <div class="field">
		          <input type="text" class="input w50" style="cursor: pointer;" value="" placeholder="请选择日期" name="collect_date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,autoPickDate:true,skin:'Stwoer2'})"/>
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	     </tr>
	    <tr>
	    	<td>
		      <div class="form-group">
		        <div class="label" style="width:75px">
		        </div>
		        <div class="field">
		          <div class="tips"></div>
		        </div>
		      </div>
		     </td>
	    	<td>
		        <div class="form-group">
			        <div class="label" style="width:75px">
			          <label></label>
			        </div>
			        <div class="field">
			       	  <button id="sampleFileBtn" class="button bg-main icon-check-square-o" type="submit" style="width: 143px"> 保存</button>
			       	  <button  class="button bg-main icon-file-o" onclick="javascript: window.history.back();" style="width: 143px" type="button"> 返回</button>
			        </div>
		        </div>
		     <script type="text/javascript">
	        	$(function(){
	        		$("#sampleFileBtn").click(function(){
         				$("#sampleFileForm").validate({
   	    					rules:{
   	    						"client":{"required":true},"commission_date":{"required":true},"subbarcode":{"required":true},"barcode":{"required":true},"person_name":{"required":true},"birthday":{"required":true}
   	    					},
   	    					messages:{
   	    						"client":{"required":""},"commission_date":{"required":""},"subbarcode":{"required":""},"barcode":{"required":""},"person_name":{"required":""},"birthday":{"required":""}
   	    					},
   	    					submitHandler:function(){
   	    						$.ajax({   
  	    							cache: false,
  	    							type: "POST",   
  	    							url:"${pageContext.request.contextPath}/sampleFile/createSampleFile", //把表单数据发送到ajax.jsp  
  	    							data:$('#sampleFileForm').serialize(), //要发送的是ajaxFrm表单中的数据   
  	    							success:function(data){
  	    								if(data){
	  	    								alert("添加成功！");
  	    								}else{
  	    									alert("添加失败！");
  	    								}
  	    							} 
  	    						}); 
   	    			        }  
   	    				}); 
	        		});
	        	});
	        </script>
		     </td>
	     </tr>
     </table>
     </form>
  </div>
</div>

</body>
</html>