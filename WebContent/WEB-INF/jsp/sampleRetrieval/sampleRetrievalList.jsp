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
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	
	function displayData(){
		if($("#gene").val() == "" || $("#variant").val() == ""){
			$("#message").text("基因和突变不能为空");
		}else{
			$("#message").text("");
			$.ajax({
				url:"${pageContext.request.contextPath}/sampleRetrieval/getsampleRetrievalList",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"gene":$("#gene").val(),
					"variant":$("#variant").val(),
					"primary_cancer_id":$("#primary_cancer_id").val(),
					"before_date":$("#before_date").val(),
					"after_date":$("#after_date").val()
				},
				beforeSend:function(){
					$("#message").text("正在处理请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					console.log(jsonObject);
					$("#tInfo2").empty();
				    if(jsonObject.length==0){
						$("#message").text("没数据");
					}else{
						$("#message").text("");
						var htmlString="";
						$.each(jsonObject,function(i,n){
							htmlString += '<tr class="odd">';
							htmlString += '<td>'+n.report_id+'</td>';
							htmlString += '<td>'+n.subbarcode+'</td>';
							htmlString += '<td>'+n.ori_variant+'</td>';
							htmlString += '<td>'+n.person_name+'</td>';
							htmlString += '<td>'+n.gender+'</td>';
							htmlString += '<td>'+n.age+'</td>';
							htmlString += '<td>'+n.cancertype+'</td>';
							htmlString += '<td>'+n.specimen_type+'</td>';
							htmlString += '<td>'+n.customer+'</td>';
							htmlString += '<td>'+n.loaded_date+'</td>';
							htmlString += '</tr>';
						});
						//将上面拼接好的json字符串追加到tbody中
						$("#tInfo2").append(htmlString);
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
    <div class="panel-head"><strong class="icon-reorder"> 样本检索</strong> </div>
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <li>疾病种类:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="diseaseTypes" name="diseaseTypes"  class="input" style="width:200px; line-height:17px;display:inline-block" />
          <input type="hidden" id="primary_cancer_id"  name="primary_cancer_id" />
          <script type="text/javascript">
          $(function(){
        	  $.post("${pageContext.request.contextPath}/autoComplete/getDiseaseClassChineseAndId", function(data){
      			$('#diseaseTypes').autocomplete(data, {
      				max : data.length, //列表里的条目数
      				minChars : 0, //自动完成激活之前填入的最小字符
      				width : 288, //提示的宽度，溢出隐藏
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
      				$("#primary_cancer_id").val(row.id);
      			});
      		},"json");
          })
          
          </script>
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 基因:</li>
        <li>
          <input type="text" id="gene" name="gene" class="input" style="width:202px;line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 突变:</li>
        <li>
          <input type="text" id="variant" name="variant" class="input" style="width:202px;line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 分析时间:</li>
        <li>从</li>
        <li>
          <input type="text" placeholder="选择日期" id="before_date" name="before_date" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px; cursor: pointer; line-height:17px;display:inline-block" />
        </li>
        <li>到</li>
        <li>
          <input type="text" placeholder="选择日期" id="after_date" name="after_date" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px; cursor: pointer; line-height:17px;display:inline-block" />
        </li>
        <li>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData();" > 搜索</a>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
       		<span id="message" style="color: red;font-size: 14px"></span>
        </li>
      </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <th>report_id</th>
        <th>样本编号</th>
        <th>突变位点</th>
        <th>姓名</th>
        <th>性别</th>
        <th>年龄</th>
        <th>疾病种类</th>
        <th>样本类型</th>
        <th>送检单位</th>
        <th>送检时间</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
															
	  </tbody>
    </table>
  </div>
</form>
</body>
</html>