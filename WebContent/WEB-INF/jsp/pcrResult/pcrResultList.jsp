<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		displayData(0);
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayData(this.value-1);
			}
		}); 
	});
	
	function displayData(pageNo){
		var pageSize=10;
		if($("#gene_symbol").val() == "" && $("#variant").val() == "" && $("#category").val() == "" && $("#variant_frequency_sta").val() == "" && $("#variant_frequency_end").val() == "" && $("#tested_date_B").val() == "" && $("#tested_date_N").val() == ""){
			$("#message").text("请选择筛选条件");
		}else{
			$("#message").text("");
			$.ajax({
				url:"${pageContext.request.contextPath}/pcrResult/getpcrResultByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"gene_symbol":$("#gene_symbol").val(),
					"variant":$("#variant").val(),
					"category":$("#category").val(),
					"tested_date_B":$("#tested_date_B").val(),
					"tested_date_N":$("#tested_date_N").val(),
					"variant_frequency_sta":$("#variant_frequency_sta").val(),
					"variant_frequency_end":$("#variant_frequency_end").val(),
				},
				beforeSend:function(){
					$("#message").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					$("#tInfo2").empty();
				    if(jsonObject.total==0){
						$("#message").text("没数据");
						$("#exportFile").prop("disabled",true);
					}else{
						//显示导出按钮
						$("#exportFile").prop("disabled",false);
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
						$("#tInfo2").append(htmlString);
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
    <div class="panel-head"><strong class="icon-reorder"> PCR查询</strong> </div>
    <div class="padding border-bottom" style="margin-top: 0px; margin-bottom: 30px">
      <ul class="search" style="padding-left:50px;">
         <li>基&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;因:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="gene_symbol" name="gene_symbol" class="input" style="width:250px; line-height:17px;display:inline-block" />
           <script type="text/javascript">
	          $(function(){
	        	  $.post("${pageContext.request.contextPath}/autoComplete/getGeneSymbolList", function(data){
	        			$('#gene_symbol').autocomplete(data, {
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
		        				$.post("${pageContext.request.contextPath}/autoComplete/getVariantListByGene",{"gene_symbol":row+""}, function(data){
		                			$('#variant').autocomplete(data, {
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
	        			
	        		},"json");
	          })
          
          </script>
        </li>
        <li>变异位点:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="variant" name="variant" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp; 检测时间:&nbsp;&nbsp; 从</li>
        <li>
          <input type="text" placeholder="选择日期" id="tested_date_B" name="tested_date_B" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px;cursor:pointer; line-height:17px;display:inline-block" />
        </li>
        <li>至</li>
        <li>
          <input type="text" placeholder="选择日期" id="tested_date_N" name="tested_date_N" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px;cursor:pointer; line-height:17px;display:inline-block" />
        </li>
       <li style="margin-left:55px;">突变丰度:&nbsp;&nbsp; 从</li>
        <li>
          <input type="text" id="variant_frequency_sta" name="variant_frequency_sta" class="input" style="width:50px; line-height:17px;display:inline-block" />
        </li>
        <li>至</li>
        <li>
          <input type="text" id="variant_frequency_end" name="variant_frequency_end" class="input" style="width:50px; line-height:17px;display:inline-block" />
        </li>
       </ul>
       <ul class="search" style="padding-left:50px; margin-top: 20px">
        <li>检测产品:</li>
        <li>
        	<input type="text" placeholder="请输入搜索关键字" id="category" name="category" class="input" style="width:250px; line-height:17px;display:inline-block" />
        	<script type="text/javascript">
	          $(function(){
	        	  $.post("${pageContext.request.contextPath}/autoComplete/getCategoryList", function(data){
	        			$('#category').autocomplete(data, {
	        				max : 12, //列表里的条目数
	        				minChars : 0, //自动完成激活之前填入的最小字符
	        				width : 250, //提示的宽度，溢出隐藏
	        				scrollHeight : 300, //提示的高度，溢出显示滚动条
	        				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	        				autoFill : false, //自动填充
	        				formatItem : function(row, i, max) {
	        					return ""+row;
	        				},
		        		})
	        		},"json");
	          })
          </script>
        </li>
        <li>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData(0);" > 搜索</a>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
       		<span id="message" style="color: red;font-size: 14px"></span>
        </li>
        <li style="padding-right:555px;float: right;">
          <button type="button"  class="button border-green" id="exportFile" disabled="disabled" onclick="exportPcrFile()"><span class="icon-download"></span> 导出</button>
       	 <!--  <span id="exportMessage" style="color: red;font-size: 14px; padding-left: 20px;"></span> -->
       	 <script type="text/javascript">
       	 	function exportPcrFile(){
       	 	$.ajax({url:"${pageContext.request.contextPath}/pcrResult/exportPcrFile",
  				type:"post",
  				dataType:"json",
  				data:$('#listform').serialize(),
  				beforeSend:function(){
					$("#message").text("正在导出数据...");
					return true;
				},
  				success:function(data){
  					if(data){
       	 				 window.location.href="${pageContext.request.contextPath}/pcrResult/downloadPcrFile";
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
    </div>
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
      <tbody id="tInfo2">
															
	  </tbody>
      <tr>
     	<td colspan="14">
     		<table style="width: 100%;height: 30px;border: 0;" class="page_table">
				<tr>
					<td width="8%" class="font_left">数据:<span id="total"></span>条</td>
					<td width="478" class="font_right"><div id="pagination"></div></td>
				</tr>
			</table>
     	</td>
      </tr>
    </table>
  </div>
</form>
</body>
</html>