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
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>验证结果</strong></div>
  <div class="body-content" style="margin-left:100px">
  	
  
  
    <form class="form-x">  
    <table style="width:100%;">
    <tr>
    	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label >样本编号：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="" name="subbarcode" id="subbarcode" data-validate="required:请正确填写样本 ID" />
	          <div class="tips"></div>
	          <span id="message_primary" style="color:#FF0000; font-size:25px;  margin-left:15px;">*</span>
	        </div>
	      </div>
	     </td>
	     <td>
	      <div class="form-group">
	      	<div class="label" style="width:75px">
	          <label></label>
	        </div>
	        <div class="field" style="margin-left:382px;">
	          <button id="sample_lims" style="width: 130px;" class="button bg-main icon-search-plus"  type="button"> 从LIMS抓取</button>
	          <script type="text/javascript">
	          	$(function(){
	          		$("#sample_lims").click(function(){
		          		$.ajax({
		          			url:"${pageContext.request.contextPath}/sampleFile/getSpecimenHeadBySubbarcode",
		          			type:"post",
		          			data:{"subbarcode":$("#subbarcode").val()},
		          			dataType:"json",
		          			success:function(result){
		          				if(result){
		          					alert("抓取成功");
		          				}else{
		          					alert("抓取失败");
		          				}
		          			}
		          		});
	          		});
	          	});
	          </script>
	          <button id="add_sample_file" style="width: 135px;" class="button bg-main icon-plus-square-o"  type="button"> 添加样本</button>
	          <script type="text/javascript">
	          	$(function(){
	          		$("#add_sample_file").click(function(){
		          		window.location.href="${pageContext.request.contextPath}/sampleFile/addSampleFile";
	          		});
	          	});
	          </script>
	       </div>
	      </div>
	     </td>
     </tr>
     </table>
     </form>
     <form  id="pcrForm" method="post"  class="form-x">
     <table style="width:50%;  float: left;">
     <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测名称：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="3D PCR 验证" id="test_name" name="test_name"  />
	          <!-- 隐藏域 存放id -->	
			  <input id="test_id" name="test_id" value="10"  type="hidden">
	        </div>
	      </div>  
	     </td>
	     
	    
      </tr>
      <tr>
     	<td>
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>检测基因：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" placeholder="请输入搜索关键字" id="gene_symbol" name="gene_symbol" />
	          <div class="tips"></div>
	          <script type="text/javascript">
	          $(function(){
	        	  $.post("${pageContext.request.contextPath}/PCR/getGeneSymbolList",{"test_id":10}, function(data){
	        			$('#gene_symbol').autocomplete(data, {
	        				max : 12, //列表里的条目数
	        				minChars : 0, //自动完成激活之前填入的最小字符
	        				width : 288, //提示的宽度，溢出隐藏
	        				scrollHeight : 300, //提示的高度，溢出显示滚动条
	        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	        				autoFill : false, //自动填充
	        				formatItem : function(row, i, max) {
	        					return ""+row;
	        				},
		        			}).result(function(event, row, formatted) {
		        				$.post("${pageContext.request.contextPath}/PCR/getVariantListByGene",{"gene_symbol":row+""}, function(data){
		                			$('#variant').autocomplete(data, {
		                				max : 12, //列表里的条目数
		                				minChars : 0, //自动完成激活之前填入的最小字符
		                				width : 288, //提示的宽度，溢出隐藏
		                				scrollHeight : 300, //提示的高度，溢出显示滚动条
		                				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
		                				autoFill : false, //自动填充
		                				formatItem : function(row, i, max) {
		                					return ""+row;
		                				}
		                			});
		                		},"json");
		        			});
	        			
	        		},"json");
	          })
          
          </script>
	        </div>
	      </div>  
	      </td> 
	     
      </tr>
      <tr>
      	<td>
	     	<div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测位点：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" placeholder="请输入搜索关键字" id="variant" name="variant" />
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
	        <div class="field"  style="">
	         <button id="add_validate" style="width: 135px;" class="button bg-main icon-plus-square-o"  type="button">加入验证</button>
	         <script type="text/javascript">
	          	$(function(){
	          		var flag = 1;
	          		var gene_symbol_list = [];
	          		var variant_list = [];
	          		$("#add_validate").click(function(){
	          			var gene_symbol="";
	          			var variant="";
	          			gene_symbol = $("#gene_symbol").val();
	          			variant = $("#variant").val();
	          			if($.inArray(gene_symbol, gene_symbol_list)!=-1 && $.inArray(variant, variant_list)!=-1){
	          				alert("请勿添加相同结果！");
	          			}else{
		          			gene_symbol_list.push(gene_symbol);
		          			variant_list.push(variant);
		          			if(gene_symbol == "" || variant == ""){
		          				alert("检测基因 或 检测位点 为空，请重新填写");
		          			}else{
		          			//根据gene_symbol和variant 给 pcr_variant_id赋值
								$.post("${pageContext.request.contextPath}/pcrVariant/getPcrVariantId",
										{"gene_symbol":gene_symbol,"variant":variant},
										function(result){
											var htmlString="";
											htmlString += '<tr class="bbb">';
											htmlString += '<td>';
											htmlString += '<div class="form-group" style="margin-left: 140px;margin-bottom: 0px;float:left;">';
											htmlString += '<input type="text" style="width: 80px;text-align: center;float:left;" id="gene_symbol'+flag+'" value="'+gene_symbol+'" readonly="readonly">';
											htmlString += '<input type="text" style="width: 290px;text-align: center;float:left;" id="variant'+flag+'" name="variant'+flag+'" value="'+variant+'" readonly="readonly">';
											htmlString += '<input type="text" style="width: 80px;text-align: center;float:left;" id="variant_frequency'+flag+'" value="" >&nbsp;&nbsp;%';
											htmlString += '<input type="hidden" style="width: 150px;text-align: center;float:left;" id="pcr_variant_id'+flag+'" name="pcr_variant_id'+flag+'" value="'+result+'" >';
											htmlString += '</div>';
											htmlString += '<td>';
											htmlString += '</tr>';
											$(".table_result").css("display","block");
											$("#flag").val(flag);
											$("#aaa").after(htmlString); 
											++flag;
										},"json");
		          			}
	          			}
	          		});
	          	});
	          </script>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
      	<td>
	     	<div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测人：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="" id="tested_by" name="tested_by" data-validate="required:请输入检测人" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
      	<td>
	     	<div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测时间：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" value="" id="tested_date" name="tested_date" data-validate="required:请选择日期" />
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
	          <input type="text" class="input w50" value=""  id="checked_by" name="checked_by" data-validate="required:请输入复核人" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr>
      	<td>
	     	<div class="form-group">
	        <div class="label" style="width:75px">
	          <label>复核时间：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" id="checked_date"  name="checked_date" data-validate="required:请选择日期" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      </table>
      <table style="width:50%;display: none;float: left;" class="table_result">
	      <tr id="aaa">
	     	 <td >
	     	 	<input type="hidden" id="flag" value="" />
		      	 <div class="form-group ddd" style="height:10px; margin-left: 70px;">
			        <div class="label" style="width:145px;float:left">
			          <label >检测基因</label>
			        </div>
			        <div class="label" style="width:175px;float:left;">
			          <label >检测位点</label>
			        </div>
			        <div class="label" style="width:190px;float:left;">
			          <label >突变丰度</label>
			        </div>
		        </div>
		      </td>
	      </tr>
      	  <tr>
      	  	<td>
      	  		<div style="margin-left: 150px; margin-top: 50px;">
      	  			<button id="saveBtn" class="button bg-main icon-check-square-o" type="button"> 保存结果</button>
      	  			<script type="text/javascript">
			        	$(function(){
			        		$("#saveBtn").click(function(){
			        			var flag = $("#flag").val();
			        			var pcr_variant_id = "";
			        			var variant_frequency = "";
			        			var sig = true;
			        			for(var i=1; i<=flag; i++){
			        				if($("#variant_frequency"+i).val()!=null && $("#variant_frequency"+i).val()!='' && $("#variant_frequency"+i).val().length!=0){
				        				if(i!=flag){
					        				pcr_variant_id += $("#pcr_variant_id"+i).val()+",";
					        				variant_frequency += $("#variant_frequency"+i).val()+",";
				        				}else{
					        				pcr_variant_id += $("#pcr_variant_id"+i).val();
					        				variant_frequency += $("#variant_frequency"+i).val();
				        				}
			        				}else{
			        					var sig = false;
			        					break;
			        				}
			        			}
			        			if(sig){
			        				$.ajax({
		   	 		          			url:"${pageContext.request.contextPath}/PCR/addResult",
		   	 		          			type:"post",
		   	 		          			data:{
		   	 		          				  "subbarcode":$("#subbarcode").val(),
		   	 		          				  "pcr_variant_id_str":pcr_variant_id,
		   	 		          				  "variant_frequency_str":variant_frequency,
		   	 		          				  "created_by":$("#created_by").val(),
		   	 		          				  "update_by":$("#update_by").val(),
		   	 		          				  "created_date":$("#created_dat").val(),
		   	 		          				  "update_date":$("#update_date").val(),
		   	 		          				  "tested_by":$("#tested_by").val(),
		   	 		          			      "tested_date":$("#tested_date").val(),
		   	 		          				  "checked_by":$("#checked_by").val(),
		   	 		          				  "checked_date":$("#checked_date").val()
		   	 		          				  },
		   	 		          			success:function(result){
		   	 		          				if(result){
			 		          					alert("数据保存成功！");
			 		          				}else{
		   	 		          					alert("数据保存失败！");
			 		          				}
			 		          			}
			 		          		});
			        			}else{
			        				alert("请正确填写突变丰度！");
			        			}
			        		})
			        	})
		        	</script>
      	  		</div>
      	  		
      	  	</td>
      	  </tr>
      </table>
      	<input type="hidden" id="created_by" name="created_by" value="${user.user_account}">
		<input type="hidden" id="update_by" name="update_by" value="${user.user_account}">
		<input type="hidden" id="created_dat" name="created_date" />
		<input type="hidden" id="update_date" name="update_date">
		<script type="text/javascript">
		$(function(){
			getTime("update_date");
			getTime("created_dat");
		});
		
		</script>
		
    </form>
  </div>
</div>

</body></html>