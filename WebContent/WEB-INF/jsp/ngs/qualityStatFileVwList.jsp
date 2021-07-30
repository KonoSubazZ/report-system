<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
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
		$("#clickText").change(function(){
		    var clickTextStatus = $("input[id='clickText']").attr("checked");
		    displayData(0);
	    });
	});
	function displayData(pageNo){
		var pageSize=10;
		var clickTextStatus = $("input[id='clickText']").attr("checked");
		if(true){
			$.ajax({
				url:"${pageContext.request.contextPath}/qualityStatFileVw/getQualityStatFileVwByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"data_type":$("#data_type").val(),
					"platform":"${currentNgsAvailableDate.platform}",
					"analysis_date":"${currentNgsAvailableDate.analysis_date}",
					"subbarcode":"${currentNgsAvailableDate.subbarcode}",
					"product_name":"${currentNgsAvailableDate.product_name}",
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
					}else{
						$("#message").text("");
						var htmlString="";
						var report_id_N=0;
						var record_idList=new Array();
						$.each(jsonObject.dataList,function(i,n){
							if(clickTextStatus=="checked"){
								htmlString += '<tr class="odd" id="'+n.record_id+'" style="display: none;">';
							}else{
								htmlString += '<tr class="odd" id="'+n.record_id+'">';
							}
							htmlString += '<td>'+n.platform+'</td>';
							htmlString += '<td>'+n.product_name+'</td>';
							htmlString += '<td>'+n.data_type+'</td>';
							htmlString += '<td>'+n.file_type+'</td>';
							htmlString += '<td>'+n.param_name+'</td>';
							htmlString += '<td>'+n.value+'</td>';
							htmlString += '<td>'+n.operator1+'</td>';
							htmlString += '<td>'+n.standard_value1+'</td>';
							htmlString += '<td>'+n.operator2+'</td>';
							htmlString += '<td>'+n.standard_value2+'</td>';
							htmlString += '</tr>';
							var standard_value1 = n.standard_value1;
							var standard_value2 = n.standard_value2;
							var operator1 = n.operator1;
							var operator2 = n.operator2;
							if(standard_value1!=null && (standard_value1.indexOf("%")!=-1 || standard_value1.indexOf("M")!=-1)){standard_value1=standard_value1.replace("%","").replace("M","");}
							if(standard_value2!=null && (standard_value2.indexOf("%")!=-1 || standard_value2.indexOf("M")!=-1)){standard_value2=standard_value2.replace("%","").replace("M","");}
							if(standard_value1!=null && standard_value2!=null){
								console.log(n.value+" "+ n.operator1+" "+standard_value1);
								console.log(n.value+" "+ n.operator2+" "+standard_value2);
								if(!(eval(n.value+n.operator1+standard_value1) && eval(n.value+n.operator2+standard_value2))){
									record_idList.push(n.record_id);
								}
							}else if(standard_value1!=null && standard_value2==null){
								if(!eval(n.value+n.operator1+standard_value1)){
									record_idList.push(n.record_id);
								}
							}else if(standard_value1==null && standard_value2!=null){
								if(!eval(n.value+n.operator2+standard_value2)){
									record_idList.push(n.record_id);
								}
							}
						});
						//将上面拼接好的json字符串追加到tbody中
						$("#tInfo2").append(htmlString);
						for (i=0;i<record_idList.length;i++){
							if(clickTextStatus=="checked"){
								$("#"+record_idList[i]).removeAttr("style");
							}
							$("#"+record_idList[i]).css('color','red');
						}
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
		}else{
			$("#message").text("请选择平台");
		}
	}
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel" style="width:1680px">
    <div class="panel-head"><strong class="icon-reorder"> NGS报告管理>质检文件:${currentNgsAvailableDate.report_id }:${currentNgsAvailableDate.platform }>${currentNgsAvailableDate.analysis_date }>${currentNgsAvailableDate.subbarcode }>${currentNgsAvailableDate.product_name }</strong> </div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <%-- <li> <a class="button border-blue icon-plus-square-o" href="${pageContext.request.contextPath}/PCR/addPcrReport"> 添加报告</a>&nbsp;&nbsp;&nbsp;&nbsp;</li> --%>
        <li>Data Type:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="data_type" name="data_type" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData(0);" > 搜索</a>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
       		<span id="message" style="color: red;font-size: 14px"></span>
        </li>
        <li>
        	<input type="checkbox" id="clickText" value="111"><label for="clickText">只显示不合格质检</label>
        </li>
      </ul>
    </div>
    <script type="text/javascript">
	    $(function(){
		    $.post("${pageContext.request.contextPath}/qualityStatFileVw/getDataType",
					function(data){
			   			$('#data_type').autocomplete(data,{
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
    </script>
    <table class="table table-hover text-center">
      <tr>
        <th>平台</th>
        <th>产品</th>
        <th>数据类型</th>
        <th>文件类型</th>
        <th>参数名</th>
        <th>值</th>
        <th>算符1</th>
        <th>标准值1</th>
        <th>算符2</th>
        <th>标准值2</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
	  </tbody>
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
  </div>
</form>
</body>
</html>