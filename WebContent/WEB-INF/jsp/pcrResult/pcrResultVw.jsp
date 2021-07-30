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
    <div class="panel-head"><strong class="icon-reorder"> PCR统计</strong> </div>
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
          <input type="text" placeholder="选择日期" id="tested_date_E" name="tested_date_E" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px; cursor:pointer; line-height:17px;display:inline-block" />
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
        <li>统计分类:</li>
        <li>
        	<select id="categoryType" name="categoryType" class="input w50" style="width: 250px;float: left;text-align: center;">
        		<option value="specimen_type">样本类型</option>
        		<option value="category">检测产品</option>
        		<option value="cancertype">肿瘤分类</option>
        		<option value="clinicalremark">临床分型</option>
        		<option value="pathologicaltype">病理分期</option>
        		<option value="mutation_frequency">突变频率</option>
        		<option value="gene">变异基因</option>
        		<option value="variant">变异位点</option>
        	</select>
        </li>
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
        <li style="margin-left: 115px;">
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData();" > 搜索</a>
       		<span id="message" style="color: red;font-size: 14px"></span>
        </li>
        
        <li style="margin-left:55px;">图形显示:</li>
         <li data-ui="tab-nav" class="fore1 abtest_huafei current" >
             <input type="radio" name="Bar_Chart" id="Bar_Chart" onchange="barOrPie_Chart();" checked="checked" /> 饼状图 &nbsp;&nbsp;&nbsp;&nbsp;
	         <input type="radio" name="Bar_Chart" id="Pie_Chart" onchange="barOrPie_Chart();" /> 柱状图 &nbsp;&nbsp;&nbsp;&nbsp;
          </li>
      </ul>
    </div>
		<div style="width:400px; height: 400px; margin: 0 auto;float: left;margin-top: 1px;overflow-y:auto;" id="show_content_div">
			<span style="color:red;text-align: center;font-size: 20px;" id="show_content_span"></span>
			<table style="width: 100%;height: 19px;" id="show_content_table">
				<tbody id="show_content"></tbody>
			</table>
		</div>
		<div style="float: left; width:1200px; height: 100%; margin-top: 1px;">
			<div id="container" style="width:100%; height: 100%; margin: 0 auto"></div>
			<div id="container1" style="min-width:400px;height:100%"></div>
		</div>		
		<script type="text/javascript">
		function displayData() {
			barOrPie_Chart();
			var title_Text;
			if($("#categoryType").val() == "specimen_type"){
				title_Text = "样本类型";
			}
			if($("#categoryType").val() == "category"){
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
			if($("#categoryType").val() == "variant"){
				title_Text = "变异位点";
			}
			
				$.post("${pageContext.request.contextPath}/pcrResult/getNameAndData",
						$('#chartForm').serialize(),
						function (data) {
				var data2 =data.replace(/"bt/g, "").replace(/et"/g, "").replace(/"name"/g, "name").replace(/"data"/g, "y").replace(/\r/g, "");
				var json = eval('(' + data2 + ')'); 
				$("#show_content").empty();
				if(json!=null && json!=""){
					$("#show_content_span").text("");
					var showContentHtml="<tr><th>"+title_Text+"</th><th style='width:40px'>数量</th></tr>";
					$.each(json,function(i,m){
						showContentHtml += "<tr><td>"+m.name+"</td><td>"+m.y+"</td></tr>";
					});
					$("#show_content").append(showContentHtml);
				}else{
					$("#show_content_span").text("暂无数据！");
				}
				var data3 =data.replace(/et"}/g, "]").replace(/{"name":/g, "[").replace(/"data":"bt/g, "").replace(/\r/g, "");
				var json1 = eval('(' + data3 + ')');
					$('#container1').highcharts({
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
				                        if (this.percentage > 0) return this.point.name +'		'+this.y+"个";
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
				 Highcharts.chart('container', {
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
				            /* tickPositions: [0, 20, 40, 60, 80, 100, 120, 140] */
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
				            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b><br/>'
				        },
				        series: [{
				            name: title_Text,
				            colorByPoint: true,
				            data: json
				        }],
				       
				    });
		 },"text");
		}
		
		function barOrPie_Chart(){
			if($("#Bar_Chart").attr("checked")=="checked"){
				$("#container").css('display','none');
				$("#container1").css('display','block');
			}
			if($("#Pie_Chart").attr("checked")=="checked"){
				$("#container1").css('display','none');
				$("#container").css('display','block');
			}
		}
		
		</script>
  </div>
 </form> 
</body>
</html>