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
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		/*if("${flag}" == 1){
			getTimeYMD("analysis_date");
		}*/
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
		/* if($("#subbarcode").val()!="" || $("#analysis_date").val()!=""){
			pageNo=0;
		} */
		var pageSize=10;
		var life="";
		var illumina="";
        var analysis_date = "";
		if($("#Life").attr("checked")=="checked"){
			life="Life";
		}
		if($("#Illumina").attr("checked")=="checked"){
			illumina="Illumina";
		}
        if($("#analysis_date").val() == "" && $("#product_name").val() == "" && $("#subbarcode").val() == "" && $("#status").val()=="") {
            analysis_date = new Date().getFullYear()+((new Date().getMonth()+1)<10?'0':'')+(new Date().getMonth()+1)+(new Date().getDate()<10?'0':'')+new Date().getDate();
        } else {
            analysis_date = $("#analysis_date").val()
        }
        // $("#message").text("请选择筛选条件");
        $("#message").text("");
        $.ajax({
            url:"${pageContext.request.contextPath}/NgsAvailableDataVw/getNgsAvailableDataVwByPage",
            type:"post",
            cache:false, //设置浏览器不缓存页面
            data:{
                "pageNo":pageNo+1,
                "pageSize":pageSize,
                "life":life,
                "illumina":illumina,
                "analysis_date":analysis_date,
                "subbarcode":$("#subbarcode").val(),
                "product_name":$("#product_name").val(),
                "status":$("#status").val()
            },
            beforeSend:function(){
                $("#search-list").attr("onclick","");
                $("#message").text("正在处理请稍等...");
                return true;
            },
            success:function(jsonObject){
                $("#search-list").attr("onclick","displayData(0);");
                //清空内容
                $("#tInfo2").empty();
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
                        if(n.checked_date!=null && n.checked_date!=""){
                            htmlString += '<td>'+n.checked_date.substring(0,10)+'</td>';
                        }else{
                            htmlString += '<td>'+n.checked_date+'</td>';
                        }
                        htmlString += '<td>'+n.checked_by+'</td>';
                        htmlString += '<td>'+n.primary_cancer+'</td>';
                        htmlString += '<td>'+n.chem_cancer+'</td>';
                        if(n.report_date!=null && n.report_date!=""){
                            htmlString += '<td>'+n.report_date.substring(0,19)+'</td>';
                        }else{
                            htmlString += '<td>'+n.report_date+'</td>';
                        }
                        if(n.report_filename!=null){
                            htmlString += '<td><a style="cursor: pointer;" href="${pageContext.request.contextPath}/ngs/download?report_id='+n.report_id+'">'+n.report_filename+'</a></td>';
                        }else{
                            htmlString += '<td>'+n.report_filename+'</td>';
                        }
                        htmlString += '<td>'+n.status+'</td>';
                        if(n.report_id == null){
                            report_id_N=0;
                        }else{
                            report_id_N=n.report_id;
                        }
                        htmlString += '<td><div class="button-group"><a href="${pageContext.request.contextPath}/life/lifeMain1?report_id='+report_id_N+'&platform='+n.platform+'&status='+n.status+'&subbarcode='+n.subbarcode+'&analysis_date='+n.analysis_date+'&product_name='+n.product_name+'&product_name_show='+$("#product_name").val()+'&subbarcode_show='+$("#subbarcode").val()+'&analysis_date_show='+$("#analysis_date").val()+'&life='+life+'&illumina='+illumina+'&pageNo='+(pageNo+1)+'#tabs-4">检查</a></div></td>';
                        if(n.report_filename!=null){
                            htmlString += '<td><a><span style="cursor: pointer;" onclick="deleteReport('+n.report_id+','+pageNo+');">删除报告</span></a></td>';
                        }else{
                            htmlString += '<td></td>';
                        }
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
	function deleteReport(report_id,pageNo){
		if(confirm("确定删除？")){
			$.ajax({
				url:"${pageContext.request.contextPath}/ngs/deleteNgsReportByReportId",
				type:"post",
				data:{"report_id":report_id},
				dataType:"json",
				success:function(result){
					if(result){
						alert("删除成功！");
						displayData(pageNo);
					}else{
						$("#message").text("删除失败！");
					}
				}
			});
		}
	}
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 报告管理</strong> </div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <%-- <li> <a class="button border-blue icon-plus-square-o" href="${pageContext.request.contextPath}/PCR/addPcrReport"> 添加报告</a>&nbsp;&nbsp;&nbsp;&nbsp;</li> --%>
        <li hidden="hidden">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平台:</li>
        <li hidden="hidden">
		  Life<input type="checkbox" id="Life" class="Life_Illumina" <c:if test="${ currentNgsAvailable.life == 'Life' }">checked="checked"</c:if> />
		  Illumina<input type="checkbox" id="Illumina" class="Life_Illumina" <c:if test="${ currentNgsAvailable.illumina == 'Illumina' }">checked="checked"</c:if> />
		  
		  <script type="text/javascript">
          	$(function(){
          		getSubbarcodeListByPlatform();
        		$(".Life_Illumina").change(function(){
        			getSubbarcodeListByPlatform();
        		});
          	})
          	
			function getSubbarcodeListByPlatform(){
				if($("#Life").attr("checked")=="checked" || $("#Illumina").attr("checked")=="checked"){
				var life="";
				var illumina="";
				if($("#Life").attr("checked")=="checked"){
					life="Life";
				}
				if($("#Illumina").attr("checked")=="checked"){
					illumina="Illumina";
				}
				$("#subbarcode").flushCache();
				$("#product_name").flushCache();
				$.post("${pageContext.request.contextPath}/NgsAvailableDataVw/getSubbarcodeAndProductNameListByPlatform",
					{"life":life,"illumina":illumina}, 
					function(data){
					$('#subbarcode').autocomplete(data.subbarcodeList, {
	    				max : 12, //列表里的条目数
	    				minChars : 0, //自动完成激活之前填入的最小字符
	    				width : 200, //提示的宽度，溢出隐藏
	    				scrollHeight : 300, //提示的高度，溢出显示滚动条
	    				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	    				autoFill : false, //自动填充
	    				formatItem : function(row, i, max) {
	    				return ""+row;
    					}
    				});
					$('#product_name').autocomplete(data.productNameList, {
	    				max : 20, //列表里的条目数
	    				minChars : 0, //自动完成激活之前填入的最小字符
	    				width : 200, //提示的宽度，溢出隐藏
	    				scrollHeight : 300, //提示的高度，溢出显示滚动条
	    				matchContains : true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	    				autoFill : false, //自动填充
	    				formatItem : function(row, i, max) {
	    				return ""+row;
    					}
    				});
    		  	},"json");
     	  		}
          	}
          </script>
        </li>
        <li>分析时间:</li>
        <li>
          <input type="text" placeholder="选择日期" id="analysis_date" name="analysis_date" value="${currentNgsAvailable.analysis_date_show}" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})"  style="width:203px; line-height:17px;display:inline-block;cursor: pointer;" />
        </li>
        <li>检测产品:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="product_name" name="product_name" value="${currentNgsAvailable.product_name_show}" class="input" style="width:200px; line-height:17px;display:inline-block" />
        </li>
        <li>样本 ID:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="subbarcode" name="subbarcode" value="${currentNgsAvailable.subbarcode_show}" class="input" style="width:200px; line-height:17px;display:inline-block" />
        </li>
        <li>报告状态:</li>
        <li>
         <select id="status" name="status" class="input w50" style="width: 200px">
        	<option value="" selected="selected"></option>
            <%--<option value="初次看点" <c:if test="${currentNgsAvailable.status=='初次看点'}"> selected="selected" </c:if>>初次看点</option>--%>
            <option value="生信审核" <c:if test="${currentNgsAvailable.status=='生信审核'}"> selected="selected" </c:if>>生信审核</option>
            <option value="报告生成成功" <c:if test="${currentNgsAvailable.status=='报告生成成功'}"> selected="selected" </c:if>>报告生成成功</option>
            <%--<option value="报告生成失败" <c:if test="${currentNgsAvailable.status=='报告生成失败'}"> selected="selected" </c:if>>报告生成失败</option>
            <option value="报告审核通过" <c:if test="${currentNgsAvailable.status=='报告审核通过'}"> selected="selected" </c:if>>报告审核通过</option>
            <option value="报告审核未通过" <c:if test="${currentNgsAvailable.status=='报告审核未通过'}"> selected="selected" </c:if>>报告审核未通过</option>
            <option value="报告发送成功" <c:if test="${currentNgsAvailable.status=='报告发送成功'}"> selected="selected" </c:if>>报告发送成功</option>--%>
          </select>
        </li>
        <li>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <%--<a href="javascript:void(0)"  class="button border-main icon-search" id="search-list" onclick="displayData(0);"> 搜索</a>--%>
          <a class="button border-main icon-search" id="search-list" onclick="displayData(0);"> 搜索</a>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       		<span id="message" style="color: red;font-size: 14px"></span>
        </li>
      </ul>
    </div>
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
        <th>化疗癌种</th>
        <th>报告时间</th>
        <th>报告文件名(点击即可下载)</th>
        <th>报告状态</th>
        <th>任务</th>
        <th>删除报告</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
		<tr hidden="true">
			<td>
				<input type="text"  id="no" value="${currentNgsAvailable.pageNo}">
			</td>
		</tr>										
	  </tbody>
      <tr>
     	<td colspan="14">
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