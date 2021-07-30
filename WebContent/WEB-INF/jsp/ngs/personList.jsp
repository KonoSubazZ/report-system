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
		displayData();
	});
	
	function displayData(){
		$.ajax({
			url:"${pageContext.request.contextPath}/person/getAllPersonByPage",
			type:"post",
			cache:false, //设置浏览器不缓存页面  
			data:{
				"person_name":'${person_name}',
				"gender":'${gender}',
				"birthday":'${birthday}'
			},
			beforeSend:function(){
				$("#message").text("正在处理请稍等...");
				return true;
			},
			success:function(jsonObject){
				//清空内容
				$("#tInfo3").empty();
			   
					$("#message").text("");
					var htmlString="";
					$.each(jsonObject.dataList,function(i,n){
						htmlString += '<tr class="odd">';
						htmlString += '<td>'+n.person_id+'</td>';
						htmlString += '<td>'+n.subbarcode+'</td>';
						htmlString += '<td>'+n.received_date+'</td>';
						htmlString += '<td>'+n.person_name+'</td>';
						htmlString += '<td>'+n.gender+'</td>';
						htmlString += '<td>'+n.birthday+'</td>';
						htmlString += '<td>'+n.clinicalremark+'</td>';	
						htmlString += '<td>'+n.remark+'</td>';
						htmlString += '<td>'+n.specimen_type+'</td>';
						htmlString += '<td>'+n.specimen_quantity+'</td>';	
						htmlString += '<td>'+n.hospital+'</td>';
						htmlString += '<td>'+n.collect_date+'</td>';
						if(n.person_id == null || n.person_id == ''){
							htmlString += '<td><input id="sample_id" name="sample_id" onclick="updatePersonId(this,'+n.sample_id+')" type="checkbox" value="" /></td>';
						}else{
							htmlString += '<td><input id="sample_id" name="sample_id" checked onclick="updatePersonId(this,'+n.sample_id+')" type="checkbox" value="" /></td>';
						}
						htmlString += '</tr>';
						
					});
					//将上面拼接好的json字符串追加到tbody中
					$("#tInfo3").append(htmlString);
				
			}
		}); 	
	}
	
	function updatePersonId(checkbox,sample_id){
		var person_id = "";
		var flag_checked = false;
		if ( checkbox.checked == true){
			person_id = '${person_id}';
			flag_checked = true;
		}
		$.ajax({   
			cache: false,   
			type: "POST",   
			url:"${pageContext.request.contextPath}/person/updatePersonId", //把表单数据发送到ajax.jsp  
			data:{"sample_id":sample_id,'person_id':person_id},
			success:function(data){
				if(data.success){
					alert("数据修改成功!");
					//设置选中   
					$('#sample_id').attr('checked',flag_checked);
					displayData();
				}else{
					$("#message").text("数据修改失败，请重新修改");
				}
			} 
		}); 
		
	}
	
</script>

</head>
<body>
<form name="form1" method="post" action="" id="listform">
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 病人信息</strong></div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <li>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</li>
        <li>${person_name}</li>
        <li>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</li>
        <li>${gender}</li>
        <li>出生日期:</li>
        <li>${birthday}</li>
        <li>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</li>
        <li>${remark}</li>
        
        <li><button id="sample_lims" style="width: 150px;margin-left: 85px;" class="button bg-main icon-search-plus" onclick="open_PersonList()"  type="button"> 历史信息</button></li>
     	<script type="text/javascript">
					//跳到列表
					function open_PersonList(){
							zeroModal.show({
								 url:"${pageContext.request.contextPath}/person/historyList?person_id=${person_id}",
								 width:"90%",
								 height:"80%",
								 resize:false,
								 opacity:5,
							}); 
					}
				</script>
      </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <th width="100" style="text-align:center; padding-left:8px;">人员 ID</th>
        <th>样本 ID</th>
        <th>接收日期</th>
        <th>姓名</th>
        <th>性别</th>
        <th>出生日期</th>
        <th>临床备注</th>
        <th>备注</th>
        <th>样品种类</th>
        <th>样本量</th>
        <th>送检医院</th>
        <th>送检日期</th>
        <th>选择</th>
      </tr>
      <tr>
      <tbody id="tInfo3">
															
	  </tbody>
        <tr>
          
      <tr>
     	<td colspan="13">
     		<table width="100%" height="30" border="0" cellpadding="0"
				cellspacing="0" class="page_table">
				<tr>
					<td width="8%" class="font_left">
						
					</td>
					<td width="478" class="font_right">
						<div id="pagination"></div>
					</td>
				</tr>
			</table>
     	</td>
      </tr>
    </table>
  </div>
</form>
</body>
</html>