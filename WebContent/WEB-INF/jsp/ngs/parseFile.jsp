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
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/textContext.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/alertBox.css">

<script type="text/javascript">

	$(function(){
		getTimeYMD("analysis_date");
		displayData(0);
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayData(this.value-1);
			}
		});
	});
	function openTextRead(id){
		//alert($("#"+id).text());
		var iMask = $("<div></div>").addClass("iMask");
		iMask.css("width",$(window).width() );
		iMask.css("height",$(window).height() );
		iMask.appendTo("body");
		var iBox = $("<div></div>").addClass("iBox1");
		iBox.appendTo("body");
		var iTextarea = $('<textarea rows="3" cols="20" style="background-color: #fff;" readonly="readonly"></textarea>').addClass("iText1");
		iTextarea.appendTo(iBox);
		var iBtn = $("<button type='button'>关闭</button>").addClass("iBtn");
		iBtn.appendTo(iBox);
		iTextarea.val($("#"+id).text());
		iBtn.click(function(){
			iBox.remove();
			iMask.remove();
		});		
	}
	function displayData(pageNo){
		var pageSize=10;
		var flagStatus = false;
			$.ajax({
				url:"${pageContext.request.contextPath}/life/getDataFileStatusByPage",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"pageNo":pageNo+1,
					"pageSize":pageSize,
					"platform":"Life",
					"platform":'${platform}',
					"subbarcode":'${subbarcode}',
					"analysis_date":'${analysis_date}',
					"product_name":'${product_name}',
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
						$.each(jsonObject.dataList,function(i,n){
							htmlString += '<tr class="odd">';
							htmlString += '<td>'+n.file_id+'</td>';
							/* htmlString += '<td>'+n.file_path+'</td>'; */
							htmlString += '<td><span style="cursor:pointer" onclick="openTextRead('+"'"+"file_path"+i+"'"+');">';
							if(n.file_path != null){
								 if((n.file_path).length>24){
									var str = (n.file_path).substring(0,24)+"....";
									htmlString += str;
								}else{
									htmlString += n.file_path;
								} 
							}
							htmlString += '</span> <div id="file_path'+i+'" style="display:none;">'+n.file_path+'</div></td>';
							htmlString += '<td>'+n.file_name+'</td>';
							htmlString += '<td>'+n.file_type+'</td>';
							/* htmlString += '<td>'+n.file_text+'</td>'; */
							htmlString +='<td><span style="cursor:pointer" onclick="openTextRead('+"'"+"file_text"+i+"'"+');">';
							if(n.file_text != null){
								 if((n.file_text).length>24){
									var str = (n.file_text).substring(0,24)+"....";
									htmlString +=str;
								}else{
									htmlString +=n.file_text;
								} 
							}
							htmlString +='</span> <div id="file_text'+i+'" style="display:none;">'+n.file_text+'</div></td>';
							htmlString += '<td>'+n.analysis_date+'</td>';
							htmlString += '<td>'+n.subbarcode+'</td>';
							htmlString += '<td>'+n.product_name+'</td>';
							htmlString += '<td>'+n.status+'</td>';
							//alert(flagStatus);
							if(n.status == "Error" && !flagStatus){
								flagStatus = true;
								$.post("${pageContext.request.contextPath}/life/updateStatus",
										{ report_id:'${report_id }',flagStatus:'parseFile'},
										function(){
											
										});
							}
							htmlString += '<td>'+n.message+'</td>';
							htmlString += '<td>'+n.created_date+'</td>';
							// htmlString += '<td><div class="button-group"><a style="cursor:pointer" onclick="deleteOne('+n.file_id+','+pageNo+');">删除   </a></div></td>';

                            // htmlString += '<td><div class="button-group"><a style="cursor:pointer" onclick="deleteOne('+n.file_path+','+pageNo+');">重新驱动 </a></div></td>';
                            htmlString += '<td><div class="button-group"><a style="cursor:pointer" onclick="driveOne(\''+n.file_path+'\','+pageNo+');">重新驱动 </a></div></td>';

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
		/* } */
	}
	
	function deleteOne(file_id,pageNo){
		if(confirm("确定删除？")){
			//window.location.href="${pageContext.request.contextPath}/life/deleteParseFile?file_id="+file_id;
			$.ajax({
				url:"${pageContext.request.contextPath}/life/deleteParseFile",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"file_id":file_id,
				},
				beforeSend:function(){
					$("#message").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					if(jsonObject){
						alert("删除成功");
						displayData(pageNo);
					}else{
						alert("删除失败");
					}
				}
			});
		}
	}
    function driveOne(file_path,pageNo){
        if(confirm("确定重新驱动？")){

            $.ajax({
                url:"${pageContext.request.contextPath}/life/driveOneFile",
                type:"post",
                cache:false, //设置浏览器不缓存页面
                data:{
                    "file_path":file_path,
                },
                beforeSend:function(){
                    $("#message").text("正在处理请稍等...");
                    return true;
                },
                success:function(jsonObject){
                    if(jsonObject){
                        alert("驱动成功");
                        displayData(pageNo);
                    }else{
                        alert("驱动失败");
                    }
                }
            });
        }
    }

	
</script>

</head>
<body>
<form method="post" action="" id="listform">
  <div class="panel admin-panel" style="width:1680px">
    <div class="panel-head"><strong class="icon-reorder"> NGS报告管理>集群对接:${report_id }:${platform }>${analysis_date }>${subbarcode }>${product_name }</strong> </div>
    <table class="table table-hover text-center">
      <tr>
        <th width="6%">文件号码</th>
        <th width="10%">文件路径</th>
        <th>文件名</th>
        <th>文件类型</th>
        <th>文件内容</th>
        <th>分析时间</th>
        <th>样本编号</th>
        <th>检测产品</th>
        <th>状态</th>
        <th>信息</th>
        <th>上传时间</th>
        <th width="5%">任务</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
															
	  </tbody>
      <tr>
     	<td colspan="12">
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