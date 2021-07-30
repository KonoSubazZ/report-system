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
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>添加报告</strong></div>
  <div class="body-content" style="margin-left:100px">
  	
  
  
    <form class="form-x">  
    <table style="width:100%">
    <tr>
    	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label >样本编号：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="" name="subbarcode" id="subbarcode" data-validate="required:请正确填写样本 ID" />
	          <div class="tips"></div>
	        </div>
	      </div>
	     </td>
	     <td>
	      <div class="form-group">
	      	<div class="label" style="width:75px">
	          <label></label>
	        </div>
	        <div class="field" style="margin-left:370px;">
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
     <input type="hidden" name="subbarcode" id="subbarcode_hidden_id">
     <table style="width:100%">
     <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测类型：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value="" id="category" name="category"  placeholder="请选择检测类型" data-validate="required:请选择检测类型" />
			  <script type="text/javascript">
				  $(function(){
					  $.post("${pageContext.request.contextPath}/autoComplete/getCategory", function(data){
							$('#category').autocomplete(data, {
								max : 12, //列表里的条目数
								minChars : 0, //自动完成激活之前填入的最小字符
								width : 287, //提示的宽度，溢出隐藏
								scrollHeight : 300, //提示的高度，溢出显示滚动条
								matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
								autoFill : false, //自动填充
								formatItem : function(row, i, max) {
									return row.name;
								},
							}).result(function(event, row, formatted) {
								$(".ddd").css("display","none");
								$("#test_name").flushCache(); 
								$("#template_name").flushCache(); 
								$(".bbb").empty();
								$("#test_name").val("");
								$("#template_name").val("");
								$.post("${pageContext.request.contextPath}/autoComplete/getTestIdAndNameByCategory", { category:row.name},function(data){
									if(data.length==1){
										$(".form-group").css("display","block");
										$("#test_name").val(data[0].name);
										$("#test_id").val(data[0].id);
										$.post(
												"${pageContext.request.contextPath}/pcrVariant/getVariantByTestId",
												{test_id:data[0].id},
												function(data){
													$(".bbb").remove();
													var htmlString="";
													$.each(data,function(i,n){
														htmlString += '<tr class="bbb">';
														htmlString += '<td>';
														htmlString += '<div class="form-group " style="margin-left: 105px;margin-bottom: 0px;float:left;">';
														htmlString += '<input type="hidden" value='+n.pcr_variant_id+' name="pcrVariantIdAndFrequency'+i+'"  id="pcr_variant_id'+i+'"/>';
														htmlString += '<input type="text" class="input" style="width: 90px;text-align: center;float:left;" value='+n.gene_symbol+' name="gene_symbol'+i+'" readonly="readonly"/>';
														htmlString += '<input type="text" class="input" style="width: 90px;text-align: center;float:left;" id="variant'+i+'" name="variant'+i+'" value='+n.variant+' readonly="readonly"/>';	
														htmlString += '<input type="text" class="input w50" style="width: 85px;text-align: center;float:left;"onfocus="rmtn('+i+')" onblur="vald('+i+')" id="frequency'+i+'" name="'+(n.variant.toLowerCase()=="19del"?"egfr19del":n.variant.toLowerCase())+'frequency"/>';
														htmlString += '<span style="float: left;color:black;margin-top: 10px;margin-left: 10px">%</span>';
														htmlString += '<div class="tips"></div>';
														htmlString += '</div>';
														htmlString += '<div class="input-help" >';
														htmlString += '<ul>';
														htmlString += '<li>';
														htmlString += '<label class="error" for="frequency'+i+'" generated="true" style="color: red;"> </label>';
														htmlString += '</li>';
														htmlString += '</ul>';
														htmlString += '</div>';
														htmlString += '<td >';
														htmlString += '<div class="form-group" style="margin-left: 105px;">';
														htmlString += '<input type="text" class="input w50" style="width: 288px;text-align: center;float:left;"  id="frequencyresult'+i+'" name="frequencyresult'+i+'" readonly="readonly"/>';
														htmlString += '<div class="tips"></div>';
														htmlString += '</div>';
														htmlString += '</td>';
														htmlString += '</tr>';
													});
													//将上面拼接好的json字符串追加到tbody中
													$("#aaa").after(htmlString);
												},
												"json"
											);
									}else{
										$("#test_name").autocomplete(data, {
											max : 12, //列表里的条目数
											minChars : 0, //自动完成激活之前填入的最小字符
											width : 287, //提示的宽度，溢出隐藏
											scrollHeight : 300, //提示的高度，溢出显示滚动条
											matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
											autoFill : false, //自动填充
											formatItem : function(row, i, max) {
												return row.name;
											}
										}).result(function(event, row, formatted) {
											$(".form-group").css("display","block");
											$("#test_id").val(row.id);
											$.post(
												"${pageContext.request.contextPath}/pcrVariant/getVariantByTestId",
												{test_id:row.id},
												function(data){
													$(".bbb").remove();
													var htmlString="";
													$.each(data,function(i,n){
														htmlString += '<tr class="bbb">';
														htmlString += '<td>';
														htmlString += '<div class="form-group " style="margin-left: 105px;margin-bottom: 0px;float:left;">';
														htmlString += '<input type="hidden" value='+n.pcr_variant_id+' name="pcrVariantIdAndFrequency'+i+'"  id="pcr_variant_id'+i+'"/>';
														htmlString += '<input type="text" class="input" style="width: 90px;text-align: center;float:left;" value='+n.gene_symbol+' name="gene_symbol'+i+'" readonly="readonly"/>';
														htmlString += '<input type="text" class="input" style="width: 90px;text-align: center;float:left;" id="variant'+i+'" name="variant'+i+'" value='+n.variant+' readonly="readonly"/>';	
														htmlString += '<input type="text" class="input w50" style="width: 85px;text-align: center;float:left;"onfocus="rmtn('+i+')" onblur="vald('+i+')" id="frequency'+i+'" name="'+(n.variant.toLowerCase()=="19del"?"egfr19del":n.variant.toLowerCase())+'frequency"/>';
														htmlString += '<span style="float: left;color:black;margin-top: 10px;margin-left: 10px">%</span>';
														htmlString += '<div class="tips"></div>';
														htmlString += '</div>';
														htmlString += '<div class="input-help" >';
														htmlString += '<ul>';
														htmlString += '<li>';
														htmlString += '<label class="error" for="frequency'+i+'" generated="true" style="color: red;"> </label>';
														htmlString += '</li>';
														htmlString += '</ul>';
														htmlString += '</div>';
														htmlString += '<td >';
														htmlString += '<div class="form-group" style="margin-left: 105px;">';
														htmlString += '<input type="text" class="input w50" style="width: 288px;text-align: center;float:left;"  id="frequencyresult'+i+'" name="frequencyresult'+i+'" readonly="readonly"/>';
														htmlString += '<div class="tips"></div>';
														htmlString += '</div>';
														htmlString += '</td>';
														htmlString += '</tr>';
													});
													//将上面拼接好的json字符串追加到tbody中
													$("#aaa").after(htmlString);
												},
												"json"
											);
										});
									}
									
								});
							});
						},"json"); 
				  });
				//聚焦事件
				function rmtn(i){
					$("#template_name").flushCache(); 
					$("#template_name").val("");
					  $("#pcr_variant_id"+i+"").val( $("#pcr_variant_id"+i+"").val().toString().substring(0,4));
				}
				//离焦事件
				function vald(i){
					  $("#pcr_variant_id"+i+"").val( $("#pcr_variant_id"+i+"").val()+','+ $("#frequency"+i+"").val());
					  $("#frequencyresult"+i+"").val($("#frequencyresult"+i+"").val().toString().replace(/\+|\-/g,""));
					  
					  switch(i) {
						  case 0:
							  if($("#frequency0").val()>=0.1){
								  $("#frequencyresult0").val($("#variant0").val()+'+')
							  }else{
								  $("#frequencyresult0").val( $("#variant0").val()+'-')
							  }
						    break;
						  case 1:
							  if($("#frequency1").val()>=0.1){
								  $("#frequencyresult1").val( $("#variant1").val()+'+')
							  }else{
								  $("#frequencyresult1").val( $("#variant1").val()+'-')
							  }
							
						    break;
						  default:
							  if($("#frequency2").val()>=0.1){
								  $("#frequencyresult2").val( $("#variant2").val()+'+')
							  }else{
								  $("#frequencyresult2").val( $("#variant2").val()+'-')
							  }
					  }
					  $.post("${pageContext.request.contextPath}/autoComplete/getTemplateIdAndNameByFS", 
		      			{ frequencyresult0:$("#frequencyresult0").val(),frequencyresult1:$("#frequencyresult1").val(),frequencyresult2:$("#frequencyresult2").val(),test_id:$("#test_id").val()},
		      			function(data){
		      				if(data.length==1){
		      					$("#template_id").val(data[0].id);
		      					$("#template_name").val(data[0].name);
		      				}else{
		      					$("#template_name").autocomplete(data, {
									max : 12,minChars : 0,
									width : 287,
									scrollHeight : 300,
									matchContains : true,
									autoFill : false,
									formatItem : function(row, i, max) {
										return row.name;}
								}).result(function(event, row, formatted) {
									$("#template_id").val(row.id)});
		      				}
					   },"json");
				}
			  </script>
	          <div class="tips"></div>
	        </div>
	      </div>  
	     </td>
	     
	     <td>
	       <div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测名称：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" value=""  placeholder="请选择检测名称" id="test_name" name="test_name"  data-validate="required:请选择检测名称" />
	          <!-- 隐藏域 存放id -->	
			  <input id="test_id" name="test_id" value=""  type="hidden">
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
      </tr>
      <tr id="aaa" >
     	 <td >
	      	 <div class="form-group ddd" style="height:10px;display: none;">
		        <div class="label" style="width:153px;float:left">
		          <label >检测基因</label>
		        </div>
		        <div class="label" style="width:92px;float:left;">
		          <label >检测位点</label>
		        </div>
		        <div class="label" style="width:87px;float:left;">
		          <label >突变丰度</label>
		        </div>
	        </div>
	      	</td>
	      	<td style="height:10px">
	      	 <div class="form-group ddd" style="height:10px;display: none;">
		        <div class="label" style="width:260px">
		          <label >检查结果</label>
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
	          <input type="text" class="input w50" value="" name="tested_by" data-validate="required:请输入检测人" />
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	     	<div class="form-group">
	        <div class="label" style="width:75px">
	          <label>检测时间：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,autoPickDate:true,skin:'Stwoer2'})" value="" name="tested_date" data-validate="required:请选择日期" />
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
     <tr>
      <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label>报告模板：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50"  value="" id="template_name" placeholder="请选择报告模板"  data-validate="required:请选择报告模板" />
	          <input type="hidden" id="template_id" name="template_id">
	          <div class="tips"></div>
	        </div>
	      </div>  
	      </td> 
	     <td>
	       <div class="form-group">
	        <div class="label" style="width:75px">
	          <label>报告时间：</label>
	        </div>
	        <div class="field">
	          <input type="text" id="report_date" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,autoPickDate:true,skin:'Stwoer2'})"  name="report_date" data-validate="required:请选择日期" />
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
	       	  <button  class="button bg-main icon-file-o" onclick="javascript: window.history.back();" style="width: 135px" type="button"> 返回列表</button>
	        </div>
	        <script type="text/javascript">
	        	$(function(){
	        		$("#reportBtn").click(function(){
	        			$("#subbarcode_hidden_id").val($("#subbarcode").val());
         				$("#pcrForm").validate({
   	    					rules:{
   	    						"l858rfrequency":{"required":true,"number":true},"v600efrequency":{"required":true,"number":true},"t790mfrequency":{"required":true,"number":true},
   	    						"egfr19delfrequency":{"required":true,"number":true},"c797sfrequency":{"required":true,"number":true},"subbarcode":{"required":true},
   	    						"category":{"required":true},"test_name":{"required":true},"tested_by":{"required":true},"tested_date":{"required":true},
   	    						"checked_by":{"required":true},"checked_date":{"required":true},"template_name2":{"required":true},"report_date":{"required":true},
   	    					},
   	    					messages:{
   	    						"l858rfrequency":{"required":"✖","number":"✖"},"v600efrequency":{"required":"✖","number":"✖"},"t790mfrequency":{"required":"✖","number":"✖"},
   	    						"egfr19delfrequency":{"required":"✖ ","number":"✖"},"c797sfrequency":{"required":"✖","number":"✖"},"subbarcode":{"required":""},
   	    						"category":{"required":""},"test_name":{"required":""},"tested_by":{"required":""},
   	    						"tested_date":{"required":""},"checked_by":{"required":""},"checked_date":{"required":""},
   	    						"template_name2":{"required":""},"report_date":{"required":""}
   	    					},
   	    					submitHandler:function(){
   	    						$.ajax({
	   	 		          			url:"${pageContext.request.contextPath}/sampleFile/getSampleIdBySubbarcode",
	   	 		          			type:"post",
	   	 		          			data:{"subbarcode":$("#subbarcode_hidden_id").val()},
	   	 		          			dataType:"json",
	   	 		          			success:function(result){
	   	 		          				if(!result){
		   	 		          				$.ajax({   
		   	   	    							cache: false,
		   	   	    							type: "POST",   
		   	   	    							url:"${pageContext.request.contextPath}/PCR/createReport", //把表单数据发送到ajax.jsp  
		   	   	    							data:$('#pcrForm').serialize(), //要发送的是ajaxFrm表单中的数据   
		   	   	    							success:function(data){
	   	   	    									$("#report_date").val("");
		   	   	    								if(data>0){
		   	   	    									if(confirm("报告生成成功！是否立即下载文件？")){
		   	   	    									window.location.href="${pageContext.request.contextPath}/PCR/download?report_id="+data;
		   	   	    									}
		   	   	    								}else{
		   	   	    									alert("报告生成失败!");
		   	   	    								}
		   	   	    							} 
		   	   	    						}); 
   	 		          					}else{
   	 		          						alert("请从LIMS抓取数据!");
   	 		          						return;
   	 		          					}
   	 		          				}
   	 		          			});
   	    			        }  
   	    				}); 
		          			
	        		});
	        	});
	        </script>
	      </div> 
      	</td>
      </tr>
      </table>
      	<input type="hidden" name="created_by" value="${user.user_account}">
		<input type="hidden" name="update_by" value="${user.user_account}">
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