<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn" style="min-width: 1720px;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<title></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayData(this.value-1);
			}
		}); 
	});
	
	function displayData(pageNo){
		var pageSize=10;
		if(($("#platform").val() == null || $("#platform").val() == "") && ($("#specimen_type").val() == null || $("#specimen_type").val() == "") 
			&& ($("#subbarcode").val() == null || $("#subbarcode").val() == "") && ($("#product_name").val() == null || $("#product_name").val() == "")
			&& ($("#clinicalremark").val() == null || $("#clinicalremark").val() == "") && ($("#gene").val() == null || $("#gene").val() == "") 
			&& ($("#mutation").val() == null || $("#mutation").val() == "")	&& ($("#hospital").val() == null || $("#hospital").val() == "")
			&& ($("#fastcode").val() == null || $("#fastcode").val() == "") && ($("#tested_date_B").val() == null || $("#tested_date_B").val() == "") 
			&& ($("#tested_date_N").val() == null || $("#tested_date_N").val() == "")){
			$("#message").text("请选择筛选条件");
		}else{
			$("#message").text("");
			$.ajax({
				url:"${pageContext.request.contextPath}/ngsIntegratedMutationFileController/getNgsIntegratedMutationFileListByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"platform":$("#platform").val(),
					"specimen_type":$("#specimen_type").val(),
					"subbarcode":$("#subbarcode").val(),
					"product_name":$("#product_name").val(),
					"clinicalremark":$("#clinicalremark").val(),
					"gene":$("#gene").val(),
					"mutation":$("#mutation").val(),
					"hospital":$("#hospital").val(),
					"tested_date_B":$("#tested_date_B").val(),
					"tested_date_N":$("#tested_date_N").val(),
					"fastcode":$("#fastcode").val(),
				},
				beforeSend:function(){
					$("#message").text("正在查询，请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					$("#tInfo").empty();
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
						$("#tInfo").append(htmlString);
					} 
				  
				  //集成jquery的翻页插件
					$("#pagination").pagination(jsonObject.total, {//总记录条数
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
					$("#total").text(jsonObject.total); 
					//显示总页数
					var pageCount = jsonObject.total%pageSize==0?jsonObject.total/pageSize:parseInt(jsonObject.total/pageSize)+1;
					$("#pageCount").text(pageCount);
				}
			});
		}
	}
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> NGS查询</strong> </div>
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
        <li>平&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;台:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="platform" name="platform" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 样本类型:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="specimen_type" name="specimen_type" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 样本编号:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="subbarcode" name="subbarcode" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="product_name" name="product_name" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      </ul>
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
        <li>临床备注:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="clinicalremark" name="clinicalremark" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 基&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;因:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="gene" name="gene" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 变异位点:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="mutation" name="mutation" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>医&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;院:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="hospital" name="hospital" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        
      </ul>
      <script type="text/javascript">
      	$(function(){
      		$.ajax({url:"${pageContext.request.contextPath}/autoComplete/getIntegratedMutationFileList",
      				type:"post",
      				dataType:"json",
      				beforeSend:function(){
    					$("#message").text("正在加载列表数据,请稍后...");
    					return true;
    				},
      				success:function(data){
    					$("#message").text("");
		      			autoinput4("platform", data.platformList);
		      			autoinput4("specimen_type", data.specimenTypeList); 
		      			autoinput4("subbarcode", data.subbarcodeList);
		      			autoinput4("product_name", data.productNameList);
		      			autoinput4("clinicalremark", data.clinicalremarkList);
		      			autoinput4("hospital", data.hospitalList);
		      			autoinput4("fastcode", data.fastcodeList);
		      			$('#gene').autocomplete(data.geneList, {
	        				max : 12, //列表里的条目数
	        				minChars : 0, //自动完成激活之前填入的最小字符
	        				width : 250, //提示的宽度，溢出隐藏
	        				scrollHeight : 300, //提示的高度，溢出显示滚动条
	        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	        				autoFill : false, //自动填充
	        				formatItem : function(row, i, max) {
	        					return ""+row;
	        				},
		        		}).result(function(event, row, formatted) {
	        				$.post("${pageContext.request.contextPath}/autoComplete/getMutationListByGene",{"gene":row+""}, function(data){
	                			$('#mutation').autocomplete(data, {
	                				max : 12, //列表里的条目数
	                				minChars : 0, //自动完成激活之前填入的最小字符
	                				width : 250, //提示的宽度，溢出隐藏
	                				scrollHeight : 300, //提示的高度，溢出显示滚动条
	                				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	                				autoFill : false, //自动填充
	                				formatItem : function(row, i, max) {
	                					return ""+row;
	                				}
	                			});
	                		},"json");
		        		});
	      			}
      		});
      	})
      </script>
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
      	<li>销售区域:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="fastcode" name="fastcode" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      	<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 分析时间:</li>
        <li>从</li>
        <li>
          <input type="text" placeholder="选择日期" id="tested_date_B" name="tested_date_B" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px; cursor: pointer; line-height:17px;display:inline-block" />
        </li>
        <li>到</li>
        <li>
          <input type="text" placeholder="选择日期" id="tested_date_N" name="tested_date_N" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px; cursor: pointer; line-height:17px;display:inline-block" />
        </li>
        <li style="padding-left: 220px;">
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData(0);" > 搜索</a>
       	  <span id="message" style="color: red;font-size: 14px; padding-left: 20px;width: 30%;" ></span>
        </li>
        <li style="padding-right:478px;float: right;padding-top: 3px;">
          <button type="button"  class="button border-green" id="exportFile" disabled="disabled" onclick="exportNgsFile()"><span class="icon-download"></span> 导出</button>
       	 <!--  <span id="exportMessage" style="color: red;font-size: 14px; padding-left: 20px;"></span> -->
       	 <script type="text/javascript">
       	 	function exportNgsFile(){
       	 	$.ajax({url:"${pageContext.request.contextPath}/ngsIntegratedMutationFileController/exportNgsFile",
  				type:"post",
  				dataType:"json",
  				data:$('#listform').serialize(),
  				beforeSend:function(){
					$("#message").text("正在导出数据...");
					return true;
				},
  				success:function(data){
  					if(data){
       	 				 window.location.href="${pageContext.request.contextPath}/ngsIntegratedMutationFileController/downloadNgsFile";
						 $("#message").text("导出成功！");
  					}else{
						 $("#message").text("导出失败！");
  					}
  				}
       	 		});
       	 	}
       	 </script>
        </li>
      </ul>
      <ul class="search" style="padding-left:900px;margin-top: 3px;">
      </ul>
    </div>
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
      <tbody id="tInfo"></tbody>
      <tr>
     	<td colspan="10">
     		<table width="100%" height="30" border="0" cellpadding="0" cellspacing="0" class="page_table">
				<tr>
					<td width="8%" class="font_left">数据:<span id="total"></span>条</td>
					<td width="478" class="font_right"><div id="pagination"></div></td>
				</tr>
			</table>
     	</td>
      </tr>
    </table>
</form>
</body>
</html>