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
<script src="${pageContext.request.contextPath}/js/previewImage.js"></script>
<script src="${pageContext.request.contextPath}/js/tools.js"></script>

</head>
<body>
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>添加报告</strong></div>
  <div class="body-content" style="margin-left:100px">
  	
  
  
    <form id="SBCForm"  class="form-x">  
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
	        <div class="field" style="margin-right: 52px;">
	          <button id="sample_lims" style="width: 130px;" class="button bg-main icon-search-plus"  type="submit"> 从LIMS抓取</button>
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
   	    			        }  
   	    				}); 
	          		});
	          	});
	          </script>
	       </div>
	      </div>
	     </td>
     </tr>
     </table>
     </form>
     <form  id="pdl1Form" method="post"  class="form-x">
     <input type="hidden" name="subbarcode" id="subbarcode_hidden_id">
     <input type="hidden" name="test_id" value="8">
     <input type="hidden" name="created_by" value="${user.user_account}">
	 <input type="hidden" name="update_by" value="${user.user_account}">
	 <input type="hidden" id="created_date" name="created_date" />
	 <input type="hidden" id="update_date" name="update_date">
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
		$("#report_date").val(YY+"-"+(MM<10?'0':'')+MM+"-"+(DD<10?'0':'')+DD+" "+(hh<10?'0':'')+hh+":"+(mm<10?'0':'')+mm+":"+ (ss<10?'0':'')+ss);
		$("#tested_date").val(YY+"-"+(MM<10?'0':'')+MM+"-"+(DD<10?'0':'')+DD+" "+(hh<10?'0':'')+hh+":"+(mm<10?'0':'')+mm+":"+ (ss<10?'0':'')+ss);
		$("#checked_date").val(YY+"-"+(MM<10?'0':'')+MM+"-"+(DD<10?'0':'')+DD+" "+(hh<10?'0':'')+hh+":"+(mm<10?'0':'')+mm+":"+ (ss<10?'0':'')+ss);
	 });
	</script>
     <table style="width:100%">
     <tr>
     	<td>
	      <div class="form-group">
	        <div class="label" style="width:75px">
	          <label>报告模板：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input w50"  value="" id="template_name" name="template_name" placeholder="请选择报告模板"  data-validate="required:请选择报告模板" />
	          <input type="hidden" id="template_id" name="template_id">
	          <div class="tips"></div>
	          <script type="text/javascript">
	          	$(function(){
	          		$("#template_name").change(function(){
	          			$(".hidden_show").prop("hidden",true);
	          		});
	          		$.post("${pageContext.request.contextPath}/autoComplete/getPdl1TemplateIdAndName", function(data){
	          			$('#template_name').autocomplete(data, {
	          				max : 12, //列表里的条目数
	          				minChars : 0, //自动完成激活之前填入的最小字符
	          				width : 310, //提示的宽度，溢出隐藏
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
	          				$(".clearval").val("");
	          				if(row.id==30){
	          					$(".hidden_show").prop("hidden",false);
	          				}else if(row.id==31 || row.id==32 || row.id==33){
	          					$(".hidden_show").prop("hidden",false);
	          				}else if(row.id==34){
	          					$(".hidden_show").prop("hidden",true);
	          				}
	          			});
	          		},"json");
	          	});
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
	          <input type="text" id="report_date" class="input w50" value="" placeholder="请选择日期" style="cursor: pointer;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,autoPickDate:true,skin:'Stwoer2'})"  name="report_date" data-validate="required:请选择日期" />
	          <div class="tips"></div>
	        </div>
	      </div>  
      	</td>
     </tr>
      <tr class="hidden_show" hidden="true">
     	 <td colspan="2">
	      	 <div class="form-group " style="margin-left: 105px;margin-bottom: 0px;float:left;">
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="检测项目"  readonly="readonly" />
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="检测方法"  readonly="readonly" />
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="表达结果"  readonly="readonly" />
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="表达百分比（%）"  readonly="readonly" />
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="染色强度"  readonly="readonly" />
				<div class="tips"></div>
			</div>
		</td>
      </tr>
      <tr class="hidden_show" hidden="true">
     	 <td colspan="2">
	      	 <div class="field" style="margin-left: 105px;float:left;">
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="肿瘤细胞"  readonly="readonly" />
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="免疫组化（IHC）"  readonly="readonly" />
				<select class="input w50" style="width: 227px;float: left;text-align: center;" name="tumor_expression_result">  
					<option value="有表达">有表达</option>  
					<option value="无表达">无表达</option>  
				</select>
			</div>
			<div class="form-group" style="float: left;margin-left: 0px;padding-bottom: 0px;margin-bottom: 0px;">
				<input type="text" class="input w50 clearval" style="width: 227px;text-align: center;float:left;" id="tumor_expression_pct" name="tumor_expression_pct"/>
			</div>
			<div class="form-group" style="float: left;margin-left: 0px;padding-bottom: 0px;margin-bottom: 0px;">
				<input type="text" class="input w50 clearval" style="width: 227px;text-align: center;float:left;" id="tumor_cell_dying" name="tumor_cell_dying"/>
			</div>
		</td>
      </tr>
      <tr class="hidden_show" hidden="true">
     	 <td colspan="2">
	      	 <div class="form-group " style="margin-left: 105px;float:left;">
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="浸润免疫细胞"  readonly="readonly" />
				<input type="text" class="input" style="width: 227px;text-align: center;float:left;background: white" value="免疫组化（IHC）"  readonly="readonly" />
				<select class="input w50" style="width: 227px;float: left;text-align: center;" name="immuno_expression_result">  
					<option value="有表达">有表达</option>  
					<option value="无表达">无表达</option>  
				</select>
			</div>
			<div class="form-group" style="float: left;margin-left: 0px;">
				<input type="text" class="input w50 clearval" style="width: 227px;text-align: center;float:left;" id="immuno_expression_pct" name="immuno_expression_pct"/>
			</div>
			<div class="form-group" style="float: left;margin-left: 0px;">
				<input type="text" class="input w50 clearval" style="width: 227px;text-align: center;float:left;" id="immuno_cell_dying" name="immuno_cell_dying"/>
			</div>
		</td>
      </tr>
      <!-- <tr>
     	<td colspan="2">
	      <div class="label" style="float: left;margin-left: 20px;margin-top: 10px;">
			<label>样本检测图：</label>
	      </div>
	      <div class="form-group" style="float:left;margin-left: 6px;width: 109px" id="preview">
	           <button type="button" class="button bg-main icon-image" onclick="$('#previewImg').click();"> 插入图片</button>
	           <img id="imghead" onclick="$('#previewImg').click();">
	      </div> 
	      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview','previewImg','imghead','specimentestingpicture')" style="display: none;" id="previewImg">
	      <input type="hidden" id="specimentestingpicture" name="specimentestingpicture">
	      <div class="label" style="float: left;margin-left: 660px;margin-top: 10px;">
			<label>阳性对照图：</label>
	      </div>
	      <div class="form-group" style="float:left;margin-left: 10px;width: 109px" id="preview2">
	         <button type="button" class="button bg-main icon-image" onclick="$('#previewImg2').click();" > 插入图片</button>
	         <img id="imghead2" onclick="$('#previewImg2').click();">
	      </div>
	      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview2','previewImg2','imghead2','controltestingpicture')" style="display: none;" id="previewImg2"> 
	      <input type="hidden" id="controltestingpicture" name="controltestingpicture">
	    </td>
      </tr> -->
      <tr>
     	<td colspan="2">
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>IHC描述：</label>
	        </div>
	        <div class="field">
	          <textarea id="qwer" class="input" rows="3" cols="75" style="width: 1150px;" name="pdl1_scope_description"></textarea>
	          <div class="tips"></div>
	        </div>
	      </div>
	    </td> 
      </tr>
      <tr>
     	<td >
	      <div class="form-group" style="float:left;" >
	      <b>肿瘤纯度评估</b>
	      </div>  
	    </td> 
      </tr>
      <!-- <tr>
     	<td colspan="2">
     	  <div class="label" style="float: left;margin-left: 20px;margin-top: 10px;">
			<label>镜下图像1：</label>
	      </div>
	      <div class="form-group" style="float:left;margin-left: 10px;width: 109px" id="preview3">
	         <button type="button" class="button bg-main icon-image" onclick="$('#previewImg3').click();"> 插入图片</button>
             <img id="imghead3" onclick="$('#previewImg3').click();">
	      </div>
	      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview3','previewImg3','imghead3','tumorpuritypictureone')" style="display: none;" id="previewImg3">
	      <input type="hidden" id="tumorpuritypictureone" name="tumorpuritypictureone">
	      <div class="label" style="float: left;margin-left: 663px;margin-top: 10px;">
			<label>镜下图像2：</label>
	      </div>
	      <div class="form-group" style="float:left;margin-left: 15px;width: 109px" id="preview4">
	        <button type="button" class="button bg-main icon-image" onclick="$('#previewImg4').click();"> 插入图片</button>
            <img id="imghead4" onclick="$('#previewImg4').click();">
	      </div>
	      <input type="file" accept="image/jpeg,image/png" onchange="previewImage(this,'preview4','previewImg4','imghead4','tumorpuritypicturetwo')" style="display: none;" id="previewImg4">
	      <input type="hidden" id="tumorpuritypicturetwo" name="tumorpuritypicturetwo">
	    </td> 
      </tr> -->
      <tr>
     	<td colspan="2">
	      <div class="form-group" style="margin-right: 50px">
	        <div class="label" style="width:75px">
	          <label>镜下描述：</label>
	        </div>
	        <div class="field">
	          <textarea  class="input" rows="3" cols="75" style="width: 1150px;" name="purity_scope_description">肿瘤细胞纯度</textarea>
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
	       	  <span id="message" style="color: red;font-size: 14px"></span>
	        </div>
	        <script type="text/javascript">
        	$(function(){
        		$("#reportBtn").click(function(){
        			$("#subbarcode_hidden_id").val($("#subbarcode").val());
        			var rightExt = ["png","jpeg","jpg","eps"];
         				$("#pdl1Form").validate({
   	    					rules:{
   	    						"tested_by":{"required":true},"tested_date":{"required":true},
   	    						"checked_by":{"required":true},"checked_date":{"required":true},
   	    						"template_name":{"required":true},"report_date":{"required":true}
   	    					},
   	    					messages:{
   	    						"tested_by":{"required":""},"tested_date":{"required":""},
   	    						"checked_by":{"required":""},"checked_date":{"required":""},
   	    						"template_name":{"required":""},"report_date":{"required":""}
   	    					},
   	    					submitHandler:function(){
   	    						$.ajax({
	   	 		          			url:"${pageContext.request.contextPath}/sampleFile/getSampleIdBySubbarcode",
	   	 		          			type:"post",
	   	 		          			data:{"subbarcode":$("#subbarcode_hidden_id").val()},
	   	 		          			dataType:"json",
		   	 		          		beforeSend:function(){
			   	 						$("#message").text("生成中...");
			   	 						return true;
		   	 						},
	   	 		          			success:function(result){
	   	 		          				if(!result){
		   	 		          				$.ajax({   
		   	   	    							cache: false,
		   	   	    							type: "POST",   
		   	   	    							url:"${pageContext.request.contextPath}/pdl1/createReport", //把表单数据发送到ajax.jsp  
		   	   	    							data:$('#pdl1Form').serialize(), //要发送的是ajaxFrm表单中的数据   
		   	   	    							dataType:"json",
		   	   	    							success:function(data){
		   	   	    								$("#message").text("");
	   	   	    									$("#report_date").val("");
	   	   	    									$("#tumor_expression_pct").val("");
	   	   	    									$("#tumor_cell_dying").val("");
	   	   	    									$("#immuno_expression_pct").val("");
	   	   	    									$("#immuno_cell_dying").val("");
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
    </form>
  </div>
</div>

</body></html>