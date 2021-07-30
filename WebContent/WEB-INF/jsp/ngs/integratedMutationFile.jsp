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
<script src="${pageContext.request.contextPath}/js/highcharts/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/js/highcharts/exporting.js"></script>
<script src="https://img.hcharts.cn/highcharts-plugins/highcharts-zh_CN.js"></script>
<script type="text/javascript">
	
</script>
<style type="text/css">
	tr{text-align: center;border: solid 1px;}
</style>
</head>
<body>
 <form id="chartForm"  class="form-x"> 
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> NGS统计</strong> </div>
    <div class="padding border-bottom" style="margin-top: 0px; margin-bottom: 30px">
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
      	<li>平&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;台:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="platform" name="platform" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      	<li>医&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;院:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="hospital" name="hospital" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      	<li>样本类型:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="specimen_type" name="specimen_type" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      	<li>产&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;品:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="product_name" name="product_name" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      </ul>
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
      	<li>检测癌种:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="cancertype" name="cancertype" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      	<li>临床备注:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="clinicalremark" name="clinicalremark" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>基&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;因:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="gene" name="gene" class="input" style="width:250px; line-height:17px;display:inline-block" />
           <script type="text/javascript">
	          $(function(){
	        	  $.post("${pageContext.request.contextPath}/integratedMutationFile/getIntegratedMutationList", function(data){
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
	        			autoinput4("platform", data.platformList)
	        			autoinput4("hospital", data.hospitalList)
	        			autoinput4("specimen_type", data.specimenTypeList)
	        			autoinput4("product_name", data.productNameList)
	        			autoinput4("cancertype", data.cancertypeList)
	        			autoinput4("clinicalremark", data.clinicalremarkList)
	        			autoinput4("pathologicaltype", data.pathologicaltypeList)
	        			autoinput4("fastcode", data.fastcodeList)
	        		},"json");
	          });
          
          </script>
        </li>
        <li>变异位点:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="mutation" name="mutation" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
      </ul>
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
        <li>病理分型:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="pathologicaltype" name="pathologicaltype" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
        <li>统计分类:</li>
        <li>
        	<select id="categoryType" name="categoryType" class="input w50" style="width: 250px;float: left;text-align: center;">
        		<option value="platform">检测平台</option>
        		<option value="hospital">送样医院</option>
        		<option value="specimen_type">样本类型</option>
        		<option value="product_name">检测产品</option>
        		<option value="cancertype">检测癌种</option>
        		<option value="clinicalremark">临床备注</option>
        		<option value="pathologicaltype">病理分型</option>
        		<option value="mutation_frequency">突变频率</option>
        		<option value="gene">变异基因</option>
        		<option value="gene_mutation">变异位点</option>
        		<option value="month">统计月份</option>
        		<option value="fastcode">销售区域</option>
        	</select>
        </li>
        <li>销售区域:</li>
        <li>
          <input type="text" placeholder="请输入搜索关键字" id="fastcode" name="fastcode" class="input" style="width:250px; line-height:17px;display:inline-block" />
        </li>
	  </ul>
	  <ul class="search" style="padding-left:10px;margin-top: 3px;">
        <li>分析时间:&nbsp;&nbsp; 从</li>
        <li>
          <input type="text" placeholder="选择日期" id="analysis_date_B" name="analysis_date_B" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px;cursor:pointer; line-height:17px;display:inline-block" />
        </li>
        <li>至</li>
        <li>
          <input type="text" placeholder="选择日期" id="analysis_date_N" name="analysis_date_N" class="input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true,skin:'twoer'})" style="width:203px; cursor:pointer; line-height:17px;display:inline-block" />
        </li>
     </ul>
     <ul class="search" style="padding-left:10px;margin-top: 3px;">
        <li >图形显示:</li>
         <li data-ui="tab-nav" class="fore1 abtest_huafei current" >
             <input type="radio" name="Bar_Chart" id="Pie_Chart" onchange="barOrPie_Chart();" checked="checked" /> 饼状图 &nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="Bar_Chart" id="Bar_Chart" onchange="barOrPie_Chart();" /> 柱状图 &nbsp;&nbsp;&nbsp;&nbsp;
        </li>
        <li style="margin-left:81px;">数字显示:</li>
         <li data-ui="tab-nav" class="fore1 abtest_huafei current" >
             <input type="radio" name="numberShow" id="ybs" onchange="barOrPie_Chart();" value="ybs" checked="checked"/> 变异样本数 &nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="numberShow" id="jcl" onchange="barOrPie_Chart();" value="jcl" /> 变异检出率&nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="numberShow" id="yyybs" onchange="barOrPie_Chart();" value="yyybs" /> 用药样本数&nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="numberShow" id="yyjcl" onchange="barOrPie_Chart();" value="yyjcl" /> 用药检出率&nbsp;&nbsp;&nbsp;&nbsp;
        </li>
        <li data-ui="tab-nav" class="fore1 abtest_huafei current" style="display: none;" id="mySelect">
         	<select id="mySelectOption" onchange="barOrPie_Chart();">
         		<option value="本癌种" selected="selected" id="drugs_for_indication">本癌种</option>
         		<option value="临床试验" id="drugs_for_other_indications">临床试验</option>
         		<option value="其他癌种" id="drugs_in_clinical_trials">其他癌种</option>
         	</select>
        </li>
        <li style="margin-left: 30px;">
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData();" > 搜索</a>
       		<span id="message" style="color: red;font-size: 14px;margin-left:50px;"></span>
        </li>
      </ul>
    </div>
		<div style="width:600px; height: 400px; margin: 0 auto;float: left;margin-top: 1px;overflow-y:auto;" id="show_content_div">
			<span style="color:red;text-align: center;font-size: 20px;" id="show_content_span"></span>
			<table style="width: 100%;height: 19px;" id="show_content_table">
				<tbody id="show_content1"></tbody>
				<tbody id="show_content2"></tbody>
				<tbody id="show_content3"></tbody>
				<tbody id="show_content4"></tbody>
			</table>
		</div>
		<div id="allContainer" style="float: left; width:1100px; height: 100%; margin-top: 1px;">
			<div id="container1" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container2" style="min-width:400px;height:100%"></div>
			<div id="container3" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container4" style="min-width:400px;height:100%"></div>
			<div id="container5" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container6" style="min-width:400px;height:100%"></div>
			<div id="container7" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container8" style="min-width:400px;height:100%"></div>
			<div id="container9" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container10" style="min-width:400px;height:100%"></div>
			<div id="container11" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container12" style="min-width:400px;height:100%"></div>
			<div id="container13" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container14" style="min-width:400px;height:100%"></div>
			<div id="container15" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container16" style="min-width:400px;height:100%"></div>
		</div>		
		<script type="text/javascript">
		function displayData() {
			barOrPie_Chart();
			var title_Text;
			if($("#categoryType").val() == "platform"){
				title_Text = "检测平台";
			}
			if($("#categoryType").val() == "hospital"){
				title_Text = "送样医院";
			}
			if($("#categoryType").val() == "specimen_type"){
				title_Text = "样本类型";
			}
			if($("#categoryType").val() == "product_name"){
				title_Text = "检测产品";
			}
			if($("#categoryType").val() == "cancertype"){
				title_Text = "肿瘤分类";
			}
			if($("#categoryType").val() == "clinicalremark"){
				title_Text = "临床分型";
			}
			if($("#categoryType").val() == "pathologicaltype"){
				title_Text = "病理分期";
			}
			if($("#categoryType").val() == "mutation_frequency"){
				title_Text = "突变频率";
			}
			if($("#categoryType").val() == "gene"){
				title_Text = "变异基因";
			}
			if($("#categoryType").val() == "gene_mutation"){
				title_Text = "变异位点";
			}
			if($("#categoryType").val() == "month"){
				title_Text = "统计月份";
			}
			if($("#categoryType").val() == "fastcode"){
				title_Text = "销售区域";
			}
			$.ajax({
				url:"${pageContext.request.contextPath}/integratedMutationFile/getNgsCountList",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:$('#chartForm').serialize(),
				dataType:"text",
				beforeSend:function(){
					$("#message").text("正在搜索统计...");
					return true;
				},
				success:function(result){
					$("#show_content1").empty();
					$("#show_content2").empty();
					$("#show_content3").empty();
					$("#show_content4").empty();
					if(result.length>13){
						var resultJson = JSON.parse(result);
						console.log(resultJson);
						console.log(resultJson.length);
						var showContentHtml1="<tr><th width='312px'>"+title_Text+"</th><th width='90px'>变异样本数</th><th width='90px'>全部样本数</th><th width='90px'>变异检出率</th></tr>";
						var showContentHtml2="<tr><th width='312px'>"+title_Text+"</th><th width='90px'>用药样本数-<br>本癌种</th><th width='90px'>全部样本数</th><th width='90px'>用药检出率-<br>本癌种</th></tr>";
						var showContentHtml3="<tr><th width='312px'>"+title_Text+"</th><th width='90px'>用药样本数-<br>临床试验</th><th width='90px'>全部样本数</th><th width='90px'>用药检出率-<br>临床试验</th></tr>";
						var showContentHtml4="<tr><th width='312px'>"+title_Text+"</th><th width='90px'>用药样本数-<br>其他癌种</th><th width='90px'>全部样本数</th><th width='90px'>用药检出率-<br>其他癌种</th></tr>";
						$.each(resultJson,function(i,dataList){
							if(dataList.length>0){
								$("#message").text("");
								var data = JSON.stringify(dataList);
								var data2 =data.replace(/"bt/g, "").replace(/et"/g, "").replace(/"name"/g, "name").replace(/"data"/g, "y").replace(/\r/g, "");
								var json = eval('(' + data2 + ')'); 
								var data3="[";
								$.each(json,function(i,m){
									data3 += "['"+m.name+"',"+m.y+"],";
								});
								data3+="]";
								var json1 = eval('(' + data3 + ')'); 
								var data4 =data.replace(/"bt/g, "").replace(/et"/g, "").replace(/"name"/g, "name").replace(/"rate"/g, "y").replace(/\r/g, "");
								var json2 = eval('(' + data4 + ')'); 
								var data5="[";
								$.each(json2,function(i,m){
									data5 += "['"+m.name+"',"+m.y+"],";
								});
								data5+="]"; 
								var json3 = eval('(' + data5 + ')');
								$("#show_content1").empty();
								$("#show_content2").empty();
								$("#show_content3").empty();
								$("#show_content4").empty();
								$("#allContainer").css("display","block");
								$("#show_content_span").text("");
								$.each(json,function(i,m){
									if(m.drugs==null){
										showContentHtml1 += "<tr><td>"+m.name+"</td><td>"+m.y+"</td><td>"+m.datatwo+"</td><td>"+m.rate+"%</td></tr>";
									}else if(m.drugs=="drugs_for_indication"){
										showContentHtml2 += "<tr><td>"+m.name+"</td><td>"+m.y+"</td><td>"+m.datatwo+"</td><td>"+m.rate+"%</td></tr>";
									}else if(m.drugs=="drugs_for_other_indications"){
										showContentHtml3 += "<tr><td>"+m.name+"</td><td>"+m.y+"</td><td>"+m.datatwo+"</td><td>"+m.rate+"%</td></tr>";
									}else if(m.drugs=="drugs_in_clinical_trials"){
										showContentHtml4 += "<tr><td>"+m.name+"</td><td>"+m.y+"</td><td>"+m.datatwo+"</td><td>"+m.rate+"%</td></tr>";
									}
								});
								$("#show_content1").append(showContentHtml1);
								$("#show_content2").append(showContentHtml2);
								$("#show_content3").append(showContentHtml3);
								$("#show_content4").append(showContentHtml4);
								barOrPieAndChart(title_Text, json, json1, "container"+(i*4+1), "container"+(i*4+2));
								barOrPieAndChart(title_Text, json2, json3, "container"+(i*4+3), "container"+(i*4+4)); 
							}
						});
					}else{
						$("#message").text("没数据");
						$("#allContainer").css("display","none");
						$("#show_content_span").text("暂无数据！");
					}
				}
			});
		}
		
		//饼图和柱图
		function barOrPieAndChart(title_Text,json,json1,id,id1){
			$('#'+id).highcharts({
		        chart: {
		            renderTo: 'chart'
		        },
		        title: {
		            text: title_Text
		        },
		        plotArea: {
		            shadow: null,
		            borderWidth: null,
		            backgroundColor: null
		        },
		        tooltip: {
		            formatter: function() {
		                return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.percentage, 1) +'% ('+
		                    Highcharts.numberFormat(this.y, 0, ',') +')';
		            }
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    formatter: function() {
		                        if (this.percentage > 0) return this.point.name +'		'+this.y+"";
		                    },
		                    color: 'black',
		                    style: {
		                        font: '13px Trebuchet MS, Verdana, sans-serif'
		                    }
		                }
		            }
		        },
		        legend: {
		            backgroundColor: '#FFFFFF',
		            x: 0,
		            y: -30
		        },
		        credits: {
		            enabled: false
		        },
		        series: [{
		            type: 'pie',
		            name: title_Text,
		            data: json1
		        }]
		    });
			Highcharts.chart(id1, {
		    	credits:{
		    	     enabled:false // 禁用版权信息
		    	},
		    	exporting: {
		            enabled:false
				},
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: title_Text
		        },
		        subtitle: {
		            text: ''
		        },
		        xAxis: {
		            type: 'category'
		        },
		        yAxis: {
		            title: {
		                text: '数量统计'
		            },
		        },
		        legend: {
		            enabled: false
		        },
		        plotOptions: {
		            series: {
		                borderWidth: 0,
		                dataLabels: {
		                    enabled: true,
		                    format: '{point.y}'
		                },
		                maxPointWidth:70
		            }
		        },
		        tooltip: {
		            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> of total<br/>'
		        },
		        series: [{
		            name: title_Text,
		            colorByPoint: true,
		            data: json
		        }],
		       
		    });
		}
		
		function barOrPie_Chart(){
			if($("input[name=numberShow]:checked").val()=="yyybs"){
      			$("#mySelect").css("display","block");
      			$("#show_content1").css("display","none");
      			$("#container1").css('display','none');
      			$("#container2").css('display','none');
      			$("#container3").css('display','none');
      			$("#container4").css('display','none');
      			$("#show_content2").css("display","block");
      			$("#show_content3").css("display","none");
      			$("#show_content4").css("display","none");
      			if($("#mySelectOption").val()=="本癌种"){
	      			$("#container7").css('display','none');
	      			$("#container8").css('display','none');
	      			$("#container9").css('display','none');
	      			$("#container10").css('display','none');
	      			$("#container11").css('display','none');
	      			$("#container12").css('display','none');
	      			$("#container13").css('display','none');
	      			$("#container14").css('display','none');
	      			$("#container15").css('display','none');
	      			$("#container16").css('display','none');
	      			if($("#Pie_Chart").attr("checked")=="checked"){
		      			$("#container5").css('display','block');
		      			$("#container6").css('display','none');
	      			}else{
	      				$("#container5").css('display','none');
		      			$("#container6").css('display','block');
	      			}
      			}else if($("#mySelectOption").val()=="临床试验"){
	      			$("#show_content2").css("display","none");
	      			$("#show_content3").css("display","block");
	      			$("#show_content4").css("display","none");
	      			$("#container5").css('display','none');
	      			$("#container6").css('display','none');
	      			$("#container7").css('display','none');
	      			$("#container8").css('display','none');
	      			$("#container11").css('display','none');
	      			$("#container12").css('display','none');
	      			$("#container13").css('display','none');
	      			$("#container14").css('display','none');
	      			$("#container15").css('display','none');
	      			$("#container16").css('display','none');
	      			if($("#Pie_Chart").attr("checked")=="checked"){
		      			$("#container9").css('display','block');
		      			$("#container10").css('display','none');
	      			}else{
		      			$("#container9").css('display','none');
		      			$("#container10").css('display','block');
	      			}
      			}else if($("#mySelectOption").val()=="其他癌种"){
	      			$("#show_content2").css("display","none");
	      			$("#show_content3").css("display","none");
	      			$("#show_content4").css("display","block");
	      			$("#container5").css('display','none');
	      			$("#container6").css('display','none');
	      			$("#container7").css('display','none');
	      			$("#container8").css('display','none');
	      			$("#container9").css('display','none');
	      			$("#container10").css('display','none');
	      			$("#container11").css('display','none');
	      			$("#container12").css('display','none');
	      			$("#container15").css('display','none');
	      			$("#container16").css('display','none');
	      			if($("#Pie_Chart").attr("checked")=="checked"){
		      			$("#container13").css('display','block');
		      			$("#container14").css('display','none');
	      			}else{
		      			$("#container13").css('display','none');
		      			$("#container14").css('display','block');
	      			}
      			}
      		}else if($("input[name=numberShow]:checked").val()=="yyjcl"){
      			$("#mySelect").css("display","block");
      			$("#show_content1").css("display","none");
      			$("#container1").css('display','none');
      			$("#container2").css('display','none');
      			$("#container3").css('display','none');
      			$("#container4").css('display','none');
      			$("#show_content2").css("display","block");
      			$("#show_content3").css("display","none");
      			$("#show_content4").css("display","none");
      			if($("#mySelectOption").val()=="本癌种"){
	      			$("#container5").css('display','none');
	      			$("#container6").css('display','none');
	      			$("#container9").css('display','none');
	      			$("#container10").css('display','none');
	      			$("#container11").css('display','none');
	      			$("#container12").css('display','none');
	      			$("#container13").css('display','none');
	      			$("#container14").css('display','none');
	      			$("#container15").css('display','none');
	      			$("#container16").css('display','none');
	      			if($("#Pie_Chart").attr("checked")=="checked"){
		      			$("#container7").css('display','block');
		      			$("#container8").css('display','none');
	      			}else{
		      			$("#container7").css('display','none');
		      			$("#container8").css('display','block');
	      			}
      			}else if($("#mySelectOption").val()=="临床试验"){
	      			$("#show_content2").css("display","none");
	      			$("#show_content3").css("display","block");
	      			$("#show_content4").css("display","none");
	      			$("#container5").css('display','none');
	      			$("#container6").css('display','none');
	      			$("#container7").css('display','none');
	      			$("#container8").css('display','none');
	      			$("#container9").css('display','none');
	      			$("#container10").css('display','none');
	      			$("#container13").css('display','none');
	      			$("#container14").css('display','none');
	      			$("#container15").css('display','none');
	      			$("#container16").css('display','none');
	      			if($("#Pie_Chart").attr("checked")=="checked"){
		      			$("#container11").css('display','block');
		      			$("#container12").css('display','none');
	      			}else{
		      			$("#container11").css('display','none');
		      			$("#container12").css('display','block');
	      			}
      			}else if($("#mySelectOption").val()=="其他癌种"){
	      			$("#show_content2").css("display","none");
	      			$("#show_content3").css("display","none");
	      			$("#show_content4").css("display","block");
	      			$("#container5").css('display','none');
	      			$("#container6").css('display','none');
	      			$("#container7").css('display','none');
	      			$("#container8").css('display','none');
	      			$("#container9").css('display','none');
	      			$("#container10").css('display','none');
	      			$("#container11").css('display','none');
	      			$("#container12").css('display','none');
	      			$("#container13").css('display','none');
	      			$("#container14").css('display','none');
	      			if($("#Pie_Chart").attr("checked")=="checked"){
		      			$("#container15").css('display','block');
		      			$("#container16").css('display','none');
	      			}else{
		      			$("#container15").css('display','none');
		      			$("#container16").css('display','block');
	      			}
      			}
      		}else if($("#ybs").attr("checked")=="checked"){
      			$("#mySelect").css("display","none");
      			$("#drugs_for_indication").attr("selected","selected");
      			$("#show_content1").css("display","block");
      			$("#show_content2").css("display","none");
      			$("#show_content3").css("display","none");
      			$("#show_content4").css("display","none");
      			$("#container3").css('display','none');
      			$("#container4").css('display','none');
      			$("#container5").css('display','none');
      			$("#container6").css('display','none');
      			$("#container7").css('display','none');
      			$("#container8").css('display','none');
      			$("#container9").css('display','none');
      			$("#container10").css('display','none');
      			$("#container11").css('display','none');
      			$("#container12").css('display','none');
      			$("#container13").css('display','none');
      			$("#container14").css('display','none');
      			$("#container15").css('display','none');
      			$("#container16").css('display','none');
      			if($("#Pie_Chart").attr("checked")=="checked"){
	      			$("#container1").css('display','block');
	      			$("#container2").css('display','none');
      			}else{
	      			$("#container1").css('display','none');
	      			$("#container2").css('display','block');
      			}
      		}else if($("#jcl").attr("checked")=="checked"){
      			$("#mySelect").css("display","none");
      			$("#drugs_for_indication").attr("selected","selected");
      			$("#show_content1").css("display","block");
      			$("#show_content2").css("display","none");
      			$("#show_content3").css("display","none");
      			$("#show_content4").css("display","none");
      			$("#container1").css('display','none');
      			$("#container2").css('display','none');
      			$("#container5").css('display','none');
      			$("#container6").css('display','none');
      			$("#container7").css('display','none');
      			$("#container8").css('display','none');
      			$("#container9").css('display','none');
      			$("#container10").css('display','none');
      			$("#container11").css('display','none');
      			$("#container12").css('display','none');
      			$("#container13").css('display','none');
      			$("#container14").css('display','none');
      			$("#container15").css('display','none');
      			$("#container16").css('display','none');
      			if($("#Pie_Chart").attr("checked")=="checked"){
	      			$("#container3").css('display','block');
	      			$("#container4").css('display','none');
      			}else{
	      			$("#container3").css('display','none');
	      			$("#container4").css('display','block');
      			} 
      			
      		}
		}
		
		</script>
  </div>
 </form> 
</body>
</html>