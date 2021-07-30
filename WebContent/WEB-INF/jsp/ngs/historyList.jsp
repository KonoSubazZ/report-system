<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<title></title>
<link rel="stylesheet" href="css/pintuer.css">
<link rel="stylesheet" href="css/admin.css">

<link rel="stylesheet" href="jquery/pagination/pagination.css" />
<script type="text/javascript" src="jquery/jquery-1.7.2.min.js"></script> 
<script type="text/javascript" src="jquery/pagination/jquery.pagination.js"></script>
<script src="js/pintuer.js"></script>
<script type="text/javascript">
	$(function(){
		if("${flag}" == 1){
			getTimeYMD("analysis_date");
		}
		displayData(0);
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayData(this.value-1);
			}
		}); 
	});
	
	function displayData(pageNo){
		if($("#no").val()!="" && $("#no").val()!=null){
			pageNo=$("#no").val()-1;
		}
		var pageSize=10;
		if('${subbarcodes}' != ''){
			$.ajax({
				url:"${pageContext.request.contextPath}/NgsAvailableDataVw/getNgsAvailableDataVwByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"subbarcodes_Str":'${subbarcodes}',
				},
				beforeSend:function(){
					$("#message").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					//alert(jsonObject.total);
					//清空内容
					$("#tInfo2-NgsReport").empty();
				    if(jsonObject.total==0){
						$("#message").text("没数据");
					}else{
						$("#message").text("");
						var htmlString="";
						var report_id_N=0;
						$.each(jsonObject.dataList,function(i,n){
							htmlString += '<tr class="odd">';
							htmlString += '<td>'+n.report_id+'</td>';
							htmlString += '<td>'+n.analysis_date+'</td>';
							htmlString += '<td>'+n.barcode+'</td>';
							htmlString += '<td>'+n.subbarcode+'</td>';
							htmlString += '<td>'+n.product_name+'</td>';
							htmlString += '<td>'+n.analyzer+'</td>';
							htmlString += '<td>'+n.checked_date+'</td>';
							htmlString += '<td>'+n.checked_by+'</td>';
							htmlString += '<td>'+n.primary_cancer+'</td>';
							htmlString += '<td>'+n.report_date+'</td>';
							htmlString += '<td>'+n.report_filename+'</td>';
							htmlString += '<td>'+n.status+'</td>';
							htmlString += '</tr>';
						});
						//将上面拼接好的json字符串追加到tbody中
						$("#tInfo2-NgsReport").append(htmlString);
						//获取总记录条数
						$("#total_ngs").text(jsonObject.total); 
					} 
				  
				  //集成jquery的翻页插件
					$("#pagination1").pagination(jsonObject.total, {//总记录条数
			            callback: displayData,//每次翻页的时候执行的回调函数  会自动传递当前页码索引   比正常页码小1
			            items_per_page:pageSize, // 每页显示多少条数据
			            current_page:pageNo,//当前页码索引
			            link_to:"javascript:void(0)",//保留超链接的样式，执行js代码   不跳转到任何资源
			            num_display_entries:5,//默认显示页码入口的个数
			            next_text:"下一页",
			            prev_text:"上一页",
			            next_show_always:true,//如果没有下一页是否显示连接
			            prev_show_always:true,//如果没有上一页是否显示连接
			            num_edge_entries:2,//页码较多的时候 可以用...省略
			            ellipse_text:"..."
			        });
					
					//显示总页数
					var pageCount = jsonObject.total%pageSize==0?jsonObject.total/pageSize:parseInt(jsonObject.total/pageSize)+1;
					//$("#pageCount1").text(pageCount);
				}
			});
		}
	}
	
	function resultClick(){
		var radios = document.getElementsByName("result");  
	    //根据 name集合长度 遍历name集合  
	    for(var i=0;i<radios.length;i++)  
	    {   
	        //判断那个单选按钮为选中状态  
	        if(radios[i].checked)  
	        {  
	        	if(radios[i].value == "ngsReport"){
	        		$("#NgsReport").css("display","block");
	      			$("#PcrResult").css("display","none");
	      			$("#NgsResult").css("display","none");
	        	}
	        	if(radios[i].value == "pcrRsult"){
	        		$("#NgsReport").css("display","none");
	      			$("#PcrResult").css("display","block");
	      			$("#NgsResult").css("display","none");
	        		
	        	}
	        	if(radios[i].value == "ngsRsult"){
	        		$("#NgsReport").css("display","none");
	      			$("#PcrResult").css("display","none");
	      			$("#NgsResult").css("display","block");
	        		
	        	}
	        }   
	    }   
	}
</script>

</head>
<body>
<form name="form1" method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 历史信息</strong></div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        
        <li>
        	历史报告<input type="radio" name="result" onchange="resultClick();" checked="checked" value="ngsReport" />  &nbsp;&nbsp;&nbsp;&nbsp;
	        PCR检测结果<input type="radio" name="result" onchange="resultClick();" value="pcrRsult" /> &nbsp;&nbsp;&nbsp;&nbsp;
	        NGS检测结果<input type="radio" name="result" onchange="resultClick();" value="ngsRsult" /> &nbsp;&nbsp;&nbsp;&nbsp;
        </li>
      </ul>
    </div>
    <div id="NgsReport">
	    <table class="table table-hover text-center">
	      <tr>
	        <th>报告编号</th>
	        <th>分析时间</th>
	        <th>患者编号</th>
	        <th>样本编号</th>
	        <th>检测产品</th>
	        <th>信息分析师</th>
	        <th>审核时间</th>
	        <th>基因解读师</th>
	        <th>原发癌种</th>
	        <th>报告时间</th>
	        <th>报告文件名</th>
	        <th>状况</th>
	      </tr>
	      <tr>
	      <tbody id="tInfo2-NgsReport">
				<tr hidden="true">
					<td>
						<input type="text"  id="no" value="${currentNgsAvailable.pageNo}">
					</td>
				</tr>												
		  </tbody>
	        <tr>
	          
	      <tr>
	     	<td colspan="12">
	     		<table width="100%" height="30" border="0" cellpadding="0"
					cellspacing="0" class="page_table">
					<tr>
						<td width="8%" class="font_left">数据:<span id="total_ngs"></span>条</td>
						<td width="478" class="font_right"><div id="pagination1"></div></td>
					</tr>
				</table>
	     	</td>
	      </tr>
	    </table>
    </div>
  
  <script type="text/javascript">
	$(function(){
		displayDataResult(0);
		$("#pageNo1").keydown(function(event){
			if(event.keyCode==13){
				displayDataResult(this.value-1);
			}
		}); 
	});
	
	function displayDataResult(pageNo){
		var pageSize=10;
		if('${subbarcodes}' != ''){
			$.ajax({
				url:"${pageContext.request.contextPath}/ngsIntegratedMutationFileController/getNgsIntegratedMutationFileListByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"subbarcodes_Str":'${subbarcodes}',
				},
				beforeSend:function(){
					$("#message").text("正在查询，请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					$("#tInfo2-NgsResult").empty();
				    if(jsonObject.total==0){
						$("#message").text("没有符合条件的数据");
						$("#exportFile").prop("disabled",true);
					}else{
						//显示导出按钮
						$("#exportFile").prop("disabled",false);
						$("#message").text("");
						var htmlString="";
						$.each(jsonObject.dataList,function(i,n){
							htmlString += '<tr class="odd">';
							htmlString += '<td>'+n.platform+'</td>';
							htmlString += '<td>'+n.specimen_type+'</td>';
							htmlString += '<td>'+n.product_name+'</td>';
							htmlString += '<td>'+n.clinicalremark+'</td>';
							htmlString += '<td>'+n.analysis_date+'</td>';
							htmlString += '<td>'+n.subbarcode+'</td>';
							htmlString += '<td>'+n.gene+'</td>';
							htmlString += '<td>'+n.mutation+'</td>';
							htmlString += '<td>'+n.mutation_frequency+'</td>';
							htmlString += '<td>'+n.hospital+'</td>';
							htmlString += '</tr>';
							
						});
						//将上面拼接好的json字符串追加到tbody中
						$("#tInfo2-NgsResult").append(htmlString);
					} 
				  
				  //集成jquery的翻页插件
					$("#pagination2").pagination(jsonObject.total, {//总记录条数
			            callback: displayDataResult,//每次翻页的时候执行的回调函数  会自动传递当前页码索引   比正常页码小1
			            items_per_page:pageSize, // 每页显示多少条数据
			            current_page:pageNo,//当前页码索引
			            link_to:"javascript:void(0)",//保留超链接的样式，执行js代码   不跳转到任何资源
			            num_display_entries:5,//默认显示页码入口的个数
			            next_text:"下一页",
			            prev_text:"上一页",
			            next_show_always:true,//如果没有下一页是否显示连接
			            prev_show_always:true,//如果没有上一页是否显示连接
			            num_edge_entries:2,//页码较多的时候 可以用...省略
			            ellipse_text:"..."
			        });
					//获取总记录条数
					$("#total2").text(jsonObject.total); 
					//显示总页数
					var pageCount = jsonObject.total%pageSize==0?jsonObject.total/pageSize:parseInt(jsonObject.total/pageSize)+1;
					$("#pageCount2").text(pageCount);
					}
				});
			}
		}
	</script>
	<div id="NgsResult" style="display:none">
	    <table class="table table-hover text-center">
	      <tr>
	        <th>平台</th>
	        <th>样本类型</th>
	        <th>产品</th>
	        <th>癌种</th>
	        <th>分析时间</th>
	        <th>样本编号</th>
	        <th>基因</th>
	        <th>变异位点</th>
	        <th>突变频率</th>
	        <th>送检医院</th>
      	</tr>
	      <tr>
	      <tbody id="tInfo2-NgsResult">
		  </tbody>
	        <tr>
	          
	      <tr>
	     	<td colspan="10">
	     		<table width="100%" height="30" border="0" cellpadding="0" cellspacing="0" class="page_table">
				<tr>
					<td width="8%" class="font_left">数据:<span id="total2"></span>条</td>
					<td width="478" class="font_right"><div id="pagination2"></div></td>
				</tr>
			</table>
	     	</td>
	      </tr>
	    </table>
    </div>
    <script type="text/javascript">
	$(function(){
		displayDataPcrResult(0);
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayDataPcrResult(this.value-1);
			}
		}); 
	});
	
	function displayDataPcrResult(pageNo){
		var pageSize=10;
			$("#message").text("");
		if('${sample_ids}' != ''){
			$.ajax({
				url:"${pageContext.request.contextPath}/pcrResult/getpcrResultByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"sample_ids_str":'${sample_ids}',
				},
				beforeSend:function(){
					$("#message").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					$("#tInfo2-PcrResult").empty();
				    if(jsonObject.total==0){
						$("#message").text("没数据");
					}else{
						$("#message").text("");
						var htmlString="";
						$.each(jsonObject.dataList,function(i,n){
							htmlString += '<tr class="odd">';
							htmlString += '<td>'+n.result_id+'</td>';
							htmlString += '<td>'+n.report_id+'</td>';
							htmlString += '<td>'+n.barcode+'</td>';
							htmlString += '<td>'+n.subbarcode+'</td>';
							htmlString += '<td>'+n.disease_type+'</td>';
							htmlString += '<td>'+n.category+'</td>';
							htmlString += '<td>'+n.gene_symbol+'</td>';
							htmlString += '<td>'+n.variant+'</td>';
							htmlString += '<td>'+n.variant_frequency+'</td>';
							htmlString += '<td>'+n.tested_date+'</td>';
							htmlString += '<td>'+n.tested_by+'</td>';
							htmlString += '<td>'+n.checked_date+'</td>';
							htmlString += '<td>'+n.checked_by+'</td>';
							htmlString += '<td>'+n.report_date+'</td>';
							/* htmlString += '<td><div class="button-group"><a href="${pageContext.request.contextPath}/PCR/download?report_id='+n.report_id+'">'+n.report_filename+'</a></div></td>'; */
							htmlString += '</tr>';
							
						});
						//将上面拼接好的json字符串追加到tbody中
						$("#tInfo2-PcrResult").append(htmlString);
					} 
				  
				  //集成jquery的翻页插件
					$("#pagination3").pagination(jsonObject.total, {//总记录条数
			            callback: displayData,//每次翻页的时候执行的回调函数  会自动传递当前页码索引   比正常页码小1
			            items_per_page:pageSize, // 每页显示多少条数据
			            current_page:pageNo,//当前页码索引
			            link_to:"javascript:void(0)",//保留超链接的样式，执行js代码   不跳转到任何资源
			            num_display_entries:5,//默认显示页码入口的个数
			            next_text:"下一页",
			            prev_text:"上一页",
			            next_show_always:true,//如果没有下一页是否显示连接
			            prev_show_always:true,//如果没有上一页是否显示连接
			            num_edge_entries:2,//页码较多的时候 可以用...省略
			            ellipse_text:"..."
			        });
					//获取总记录条数
					$("#total3").text(jsonObject.total); 
					//显示总页数
					var pageCount = jsonObject.total%pageSize==0?jsonObject.total/pageSize:parseInt(jsonObject.total/pageSize)+1;
					$("#pageCount3").text(pageCount);
				}
			});
		}
	}
	</script>
    <div id="PcrResult" style="display:none">
	    <table class="table table-hover text-center">
	      <tr>
	        <th>结果编号</th>
	        <th>报告编号</th>
	        <th>患者筛选号</th>
	        <th>样本编号</th>
	        <th>疾病种类</th>
	        <th>检测类别</th>
	        <th>基因</th>
	        <th>突变位点</th>
	        <th>突变丰度</th>
	        <th>检测时间</th>
	        <th>检测人</th>
	        <th>复核时间</th>
	        <th>复核人</th>
	        <th>报告时间</th>
	      </tr>
	      <tr>
	      <tbody id="tInfo2-PcrResult">
		  </tbody>
	      <tr>
	     	<td colspan="14">
	     		<table width="100%" height="30" border="0" cellpadding="0" cellspacing="0" class="page_table">
				<tr>
					<td width="8%" class="font_left">数据:<span id="total3"></span>条</td>
					<td width="478" class="font_right"><div id="pagination3"></div></td>
				</tr>
			</table>
	     	</td>
	      </tr>
	    </table>
    </div>
</div>
</form>
</body>
</html>