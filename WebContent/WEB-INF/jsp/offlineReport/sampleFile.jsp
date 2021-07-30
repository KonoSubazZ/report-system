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
<script type="text/javascript" src="jquery/zeroModal.min.js"></script>
<link href="css/zeroModal.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div class="panel admin-panel" style="width:1800px">
  
	<div class="panel-head"><strong class="icon-reorder"> 线下报告管理>样本信息:${offlineReportIframeBean.report_id }>${offlineReportIframeBean.tested_date }>${offlineReportIframeBean.subbarcode }>${offlineReportIframeBean.status }</strong> </div>
  <div class="body-content" style="margin-left:100px">
     <form  id="pdl1Form" method="post"  class="form-x">
     <table style="width:100%">
     <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>样本编号：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="subbarcode" value="${sampleFile.subbarcode }" name="subbarcode" readonly="readonly" />
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
	          <input type="text" class="input w50" id="client" value="${sampleFile.client }" name="client" readonly="readonly" />
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
	          <input type="text" class="input w50" id="person_name" value="${sampleFile.person_name }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
		      <div class="form-group">
		        <div class="label" style="width:85px">
		          <label>样品类型：</label>
		        </div>
		        <div class="field">
		        	<c:if test="${sampleFile.sample_type=='blood'}">
				        <input type="text" class="input w50" value="血" readonly="readonly" />
		        	</c:if>
		        	<c:if test="${sampleFile.sample_type=='tissue'}">
				        <input type="text" class="input w50" value="组织" readonly="readonly" />
		        	</c:if>
		          <div class="tips"></div>
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
	          <input type="text" class="input w50" id="gender" value="${sampleFile.gender }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:85px">
	          <label>出生日期：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="birthday" value="${sampleFile.birthday }" readonly="readonly" />
	          <div class="tips"></div>
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
	          <label>病理分型：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" id="pathologicaltype" value="${sampleFile.pathologicaltype }" readonly="readonly" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
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