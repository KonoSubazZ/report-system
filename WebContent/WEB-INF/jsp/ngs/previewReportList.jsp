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
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
<script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
<script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css" />
<script src="${pageContext.request.contextPath}/js/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sweet-alert.css">
<style type="text/css">
	.table th{white-space: nowrap;}
</style>
<script type="text/javascript">
	$(function(){
		context_show();
		if($("#primary_cancer").val()!="" && $("#primary_cancer").val()!=null && $("#product_name_chinese").val()!="" && $("#product_name_chinese").val()!=null){
			$("#chemical").css("display","block");
			$("#geneAndResult-show").css("display","block");
		}
	});
	
</script>
<style type="text/css">
tr {
	border-bottom: 1px solid #ddd;
}

.my-tbody th {
	background-color: #0ae;
}

.my-tbody th, td {
	border-right: 1px solid #ddd;
}

.table-hover>tbody>tr:hover>th {
	background-color: #0ae;
} /*不支持IE6*/
.textarea {
	padding-left: 10px;
	border: solid 1px #ddd;
	width: 100%;
	line-height: 20px;
	border-radius: 3px;
	-webkit-appearance: none;
}

.drug-ul {
	background-color: white;
	padding-left: 0px;
	min-height: 200px;
	width: 350px;
}

.drug-li {
	padding-left: 10px;
	padding-top: 5px;
	font-size: 14px;
}

.drug-li:hover {
	cursor: pointer;
	background-color: #00b8ff
}

.drug-li:active {
	background-color: #00b8ff
}

.preview-content-table {
	width: 100%;
	border-width: 1px 0px 0px 1px;
	border-style: solid;
	border-color: #4BAD5B;
	margin-top: 10px;
}

.preview-content-table tr {
	height: 30px;
	vertical-align: middle;
}

.preview-content-table th {
	border: solid #4BAD5B;
	border-width: 0px 1px 1px 0px;
	color: #4BAD5B;
	border-top-width: 5px;
}

.preview-content-table td {
	text-align: center;
	border: solid #4BAD5B;
	border-width: 0px 1px 1px 0px;
}

#sample-info-table td {
	text-align: left;
}

.preview-content-table2 {
	width: 100%;
	border-width: 1px 0px 0px 1px;
	border-style: solid;
	border-color: #5DA1A6;
	margin-top: 10px;
}

.preview-content-table2 tr {
	height: 30px;
	vertical-align: middle;
}

.preview-content-table2 th {
	border: solid #5DA1A6;
	border-width: 0px 1px 1px 0px;
	color: #5DA1A6;
	border-top-width: 5px;
}

.preview-content-table2 td {
	text-align: center;
	border: solid #5DA1A6;
	border-width: 0px 1px 1px 0px;
}
.btn{
	border-radius: 2px;
	cursor: pointer;
	background: #0ae;
	border: 0 none;
}
#disease_risk_dialog_btn table tr,#risk_intervention_dialog_btn table tr{
	line-height:30px;
	height:30px;
}
#disease_risk_dialog_btn table th,#risk_intervention_dialog_btn table th{
	padding:0 30px;
}
#disease_risk_dialog_btn table td,#risk_intervention_dialog_btn table td{
	text-align:center;
}
#disease_risk_dialog_btn table td:last-child,#risk_intervention_dialog_btn table td:last-child{
	border-right:0px solid #000;
}
#disease_risk_dialog_add input,#disease_risk_dialog_add select,#risk_intervention_dialog_add input,#risk_intervention_dialog_add select{
	width: 190px;
    height: 30px;
    border-radius: 4px;
    font-size: 16px;
    border: 0;
    padding-left: 10px;;
}
#disease_risk_dialog_add table tr,#risk_intervention_dialog_add table tr{
    height: 38px;
    line-height: 38px;
    border:0;
}
#disease_risk_dialog_add table td,#risk_intervention_dialog_add table td{
   text-align: right;
   padding-right:10px;
   width: 280px;
   font-size: 16px;
   border:0;
}
</style>

</head>
<body>
	<input type="hidden" id="platform" value="${geneticMarkerVwPageBean.platform}">
	<input type="hidden" id="analysis_date" value="${geneticMarkerVwPageBean.analysis_date}">
	<input type="hidden" id="subbarcode" value="${geneticMarkerVwPageBean.subbarcode}">
	<input type="hidden" id="product_name" value="${geneticMarkerVwPageBean.product_name}">
	<div class="panel admin-panel" style="width:99%;margin-left: 5px;">
		<div class="panel-head">
			<strong class="icon-reorder">
				NGS报告管理>报告预览:${geneticMarkerVwPageBean.report_id}:${geneticMarkerVwPageBean.platform }>${geneticMarkerVwPageBean.analysis_date }>${geneticMarkerVwPageBean.subbarcode }>${geneticMarkerVwPageBean.product_name }
			</strong>
		</div>
		<div class="form-group" style="height: 50px;">
	        <div class="label" style="width:70px;float: left;padding-top: 15px;">
	          <label style="font-size:15px;">本癌种：</label>
	        </div>
	        <div class="field" style="padding-top: 5px;float: left;">
	          <input type="text" class="input w50" id="primary_cancer" name="primary_cancer" value="${diseaseClass.disease_class_chinese}" placeholder="必选项！请选择本癌种"  />
	          <span id="message_primary" style="color:#FF0000; font-size:25px;  margin-left:15px;">*</span>
	          <input type="hidden" id="primary_cancer_id"  name="primary_cancer_id" value="${diseaseClass.class_id}"   />
	          <script type="text/javascript">
	          $(function(){
	        	  $.post("${pageContext.request.contextPath}/autoComplete/getDiseaseClassChineseAndId", function(data){
	        			$('#primary_cancer').autocomplete(data, {
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
	        </div>
	        <div class="label" style="width:85px;margin-left:20px;padding-top: 15px;float: left;">
	          <label style="font-size:15px;">检测产品：</label>
	        </div>
	        <div class="field" style="padding-top: 5px;float: left;">
	          <input type="text" class="input w50" id="product_name_chinese" value="${product.product_name_chinese}" placeholder="必选项！请选择检测产品"  />
	          <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
	          <input type="hidden" id="product_id" name="product_id" value="${product.product_id}"   />
	          <script type="text/javascript">
	          $(function(){
	        	  $.post("${pageContext.request.contextPath}/autoComplete/getProductNameChineseAndId", 
	        			{product_name:"${geneticMarkerVwPageBean.product_name}"},  
	        			function(data){
	        			$('#product_name_chinese').autocomplete(data, {
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
	        				$("#product_id").val(row.id);
	        			});
	        		},"json");
	          })
	          </script>
	          <div class="tips"></div>
	        </div>
	        <div style="float:left;margin:8px 0 0 10px;">
		        <button style="width: 150px;padding:10px;border-radius: 3px;border:none;background:#0ae;border:1px solid #ccc" onclick="knowledgeBaseMatching()">匹配最新库</button>
		      </div>
		      <script type="text/javascript">
		      	function knowledgeBaseMatching(){
		      		$.ajax({url:"${pageContext.request.contextPath}/life/updateProductByProductId",
          				data:{"primary_cancer_id":$("#primary_cancer_id").val(),
          					"product_id":$("#product_id").val(),
          					"report_id":"${geneticMarkerVwPageBean.report_id }",
    							"platform":"${geneticMarkerVwPageBean.platform}",
    							"analysis_date":"${geneticMarkerVwPageBean.analysis_date}",
    							"subbarcode":"${geneticMarkerVwPageBean.subbarcode}",
    							"product_name":"${geneticMarkerVwPageBean.product_name}"},
          				success:function(data){
          					parent.urlRunp4(data);
	          			},
          			dataType:"json"});		      		
		      	}
		      </script>
	      </div>
	     
	      <div class="form-group">
	      	<div id="chemical" style="float: right; padding:13px 600px 20px 50px;float: left;">
	          	<input type="radio" onclick="context_show();" checked name="show" value="1"/> 人工查看
		        <input type="radio" onclick="context_show();" name="show" value="2"/> 报告预览
	          	<script type="text/javascript">
	          		function context_show(){
	          			$("#message_primary").html("*");
	          			var obj = document.getElementsByName("show");
	          			 for(var i=0; i<obj.length; i ++){
	          		        if(obj[i].checked){
	          		            //alert(obj[i].value == 1);
	          		            if(obj[i].value == 1){
		          		            	$("#geneAndResult-show").show();
		          		            	$("#meditationPoints-show").hide();
		          		            	$("#chemical-show").hide();
	          		            }else if(obj[i].value == 2){
	          		            		clearPreviewContent();
	          		            		initTargetDrugTable();
	          		            		initGeneticCancerRisk();
	          		            		initTargetDrugAnalysisTable();
	          		            		initEffectivenessAndSideEffects();
		          		            	$("#geneAndResult-show").hide();
		          		            	$("#meditationPoints-show").show();
		          		            	$("#chemical-show").hide();
	          		            }
	          		        }
	          		    }
	          		}
	          	</script>
	          </div>
	      </div>
	      <div id="geneAndResult-show" style="margin-top: 60px;width: 90%;margin-left: 2%;">
			<div>
				<h2 style="color: blue;font-size: 20px;font-weight: bold;">肿瘤遗传风险</h2>
				CR_ALL:${CR_ALL == null ? 0 : CR_ALL}
			</div>
			<br>
		    <table class="table table-hover text-center" style="width: 80%;">
				<tbody id="tInfo1" class="my-tbody">
					<tr>
						<th>序号</th>
						<th>基因</th>
						<th>染色体</th>
						<th>外显子</th>
						<th>核苷酸</th>
						<th>氨基酸</th>
						<th>杂合/纯合</th>
						<th>突变类型</th>
						<th>ClinVar号</th>
						<th>千人频率</th>
						<th>临床意义</th>
						<th>变异解析</th>
						<th>上次审核</th>
						<th>操作</th>
					</tr>
				</tbody>
				<c:forEach items="${crAllList}" var="item" varStatus="vs">
					<tr>
						<td>${vs.index+1}</td>
						<td>${item.Gene}</td>
						<td>${item.Chr}</td>
						<td>${item.Exon}</td>
						<td style="cursor:pointer">
							<p id="pos_transcript_${vs.count}" onmouseover='showTitle1(${vs.count})' title=''>${item.cHGVS}</p>
							<script type="text/javascript">
								function showTitle1(index){
									var data = crAllList[parseInt(index)-1];
									$("#pos_transcript_"+index).attr("title","位置："+(data.Pos||"")+"\r\n转录本号："+(data.Transcript||""));
								}
							</script>
						<%-- ${item.cHGVS}
							<script>
								$("#pos_transcript_${vs.count}").click(function(e){
									showPosTranscriptDialog("${vs.count}");
								});
							</script> --%>
						</td>
						<td>${item.pHGVS}</td>
						<td>${item.Zygosity}</td>
						<td>${item.ExonicFunc}</td>
						<td>
						<c:choose>
							<c:when test="${item.avsnp150 == '.'}">
								<label>.</label>
							</c:when>
							<c:otherwise>
								<a style="text-decoration: underline; color: blue;" target="_blank" href="https://www.ncbi.nlm.nih.gov/clinvar/variation/${item.avsnp150}/">${item.avsnp150}</a>
							</c:otherwise>
						</c:choose>
						</td>
						<td>${item.c1000g2015aug_all}</td>
						<td>
							<select id="myselect${vs.count}" name="a${vs.count}" style="width:90%">
							  <option value="0">请选择</option>
							  <option value="1">致病</option>
							  <option value="2">可能致病</option>
							  <option value="3">临床意义未明</option>
							  <option value="4">可能良性</option>
							  <option value="5">良性</option>
							</select>
							<script>
							    if("${item.rpCr.Clinical_significance}"){
									$("#myselect${vs.count}").val("${item.rpCr.Clinical_significance}")
								}
								$("#myselect${vs.count}").off("change");
								$("#myselect${vs.count}").change(function(e){
									if($("#myselect${vs.count}").val() == '0' || $("#myselect${vs.count}").val() == '3' || $("#myselect${vs.count}").val() == '4' || $("#myselect${vs.count}").val() == '5'){
										$("#edit_a${vs.count}").attr("disabled",true);
										$("#use_drug_select${vs.count}").attr("disabled",true);
										var data = crAllList[parseInt(${vs.count})-1];
										data['Clinical_significance'] = $("#myselect${vs.count}").val();
										$("#use_drug_select${vs.count}").val("0");
										deleteRecord("${vs.count}");
									}else{
										var data = crAllList[parseInt(${vs.count})-1];
										$("#edit_a${vs.count}").attr("disabled",false);
										$("#use_drug_select${vs.count}").attr("disabled",false);
										data['Clinical_significance'] = $("#myselect${vs.count}").val();
										$.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=2", data, function(returnData){
											data['rpCr'] = returnData;
											crAllList[parseInt(${vs.count})-1] = data;
											crAllList[parseInt(${vs.count})-1].check_date = returnData.check_date;
										});
									}
									$("#cr_check_date_${vs.count}").html("未审核");
								});
							</script>
						</td>
						<td>
							<c:choose>
								<c:when test="${item.rpCr.Clinical_significance == null or item.rpCr.Clinical_significance == 0 or item.rpCr.Clinical_significance == 3 or item.rpCr.Clinical_significance == 4 or item.rpCr.Clinical_significance == 5}">
									<select id="edit_a${vs.count}" style="cursor: pointer;" name="edit_a${vs.count}" disabled>
										<option value="0">请选择</option>
										<option value="1">变异解析</option>
										<option value="2">疾病风险</option>
										<option value="3">风险干预措施</option>
									</select>
								</c:when>
								<c:otherwise>
									<select id="edit_a${vs.count}" style="cursor: pointer;" name="edit_a${vs.count}">
										<option value="0">请选择</option>
										<option value="1">变异解析</option>
										<option value="2">疾病风险</option>
										<option value="3">风险干预措施</option>
									</select>
								</c:otherwise>
							</c:choose>
							<script>
								//$("#myselect${vs.count}").off("click");
								$("#edit_a${vs.count}").change(function(e){
									if($("#edit_a${vs.count}").val() == '1'){
										showMutationAnalysis("${vs.count}")
									}else if($("#edit_a${vs.count}").val() == '2'){
										showdiseaseRisk("${vs.count}")
									}else if($("#edit_a${vs.count}").val() == '3'){
										showRiskInterventionDialog("${vs.count}")
									}
								});
							</script>
						</td>
						<td>
							<label id="cr_check_date_${vs.count}">${item.check_date}</label>
						</td>
						<td>
							<select id="use_drug_select${vs.count}" style="width:90%" <c:if test="${item.rpCr.Clinical_significance == null or item.rpCr.Clinical_significance == 3 or item.rpCr.Clinical_significance == 4 or item.rpCr.Clinical_significance == 5}">disabled</c:if>>
							  <option value="0">不用药</option>
							  <option value="1">用药</option>
							</select>
							<script>
								if("${item.rpCr.has_drug}" == "true"){
									$("#use_drug_select${vs.count}").val("1")
								}
								$("#use_drug_select${vs.count}").change(function(e){
									var use_drug = $("#use_drug_select${vs.count}").val();
									if(use_drug == '1') {
										addRecord("${vs.count}");
									} else {
										deleteRecord("${vs.count}");
									}
									$("#cr_check_date_${vs.count}").html("未审核");
								});
							</script>
						</td>
					</tr>
				</c:forEach>
			</table>
			<br>
		
			<div>
				<h2 style="color: blue;font-size: 20px;font-weight: bold;">用药突变</h2>
				位点总数: ${mutNum == null ? 0 : mutNum} (SNP:${SNP == null ? 0 : SNP} Indel:${Indel == null ? 0 : Indel} Fusion:${Fusion == null ? 0 : Fusion} CNV:${CNV == null ? 0 : CNV})
			</div>
			<br>
			<div>
				<select id="select2">
					  <option value="0">修改基因检测结果类别</option>
					  <option value="1">靶向药物</option>
					  <option value="2">未知临床意义</option>
					  <option value="3">不报告</option>
				</select>
				<script>
				$("#select2").off("change");
				$("#select2").change(function(e){
					var arr = 0;
					$("input[name='table_cb']:checked").each(function(k,v){
						var id = v.id.slice(3);
						if($("#result_type_"+id).html() == "未知临床意义"){
							arr++;
						}
					});
					if(arr>1){
						alert("只能选择一个未知临床意义改为靶向药物！");
					}else{
						$.each($("input[name='table_cb']"), function(k,v){
							if($(v).is(':checked')) {
								//去掉前面三个字符'cb_'
								var id = v.id.slice(3);
								if($("#result_type_"+id).html() != $("#select2").find("option:selected").text()){
									
									if($("#select2").val() == 3){
										$("#modify_a_"+id).css("text-decoration","none");
										$("#modify_a_"+id).css("color","#000")
									} else {
										$("#modify_a_"+id).css("text-decoration","underline");
										$("#modify_a_"+id).css("color","blue")
									}
									if($("#select2").val() == 2 || $("#select2").val() == 3){
										deleteDrugAndAddUnknownVar(id-1, $("#select2").val());
										$("#result_type_"+id).html($("#select2").find("option:selected").text());
										$("#result_type_val_"+id).html($("#select2").find("option:selected").val());
									} else if($("#select2").val() == 1) {
										var medicine = medicineList[id-1];
										getOriVariant(medicine.gene);
										showGimDialog(medicine.gene,id-1);
									}
								}
								$("#result_check_date_"+id).html("未审核");
								//可能要有隐藏列存value
							}
						})
					}						
				});
				</script>
			</div>
		    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;" id="table_wz">
				<tbody id="tInfo2"  class="my-tbody">
					<tr>
						<th><input id="allCb" type="checkbox">选择</th>
						<script>
							$("#allCb").off('change')
							$("#allCb").change(function(v) {
								if($("#allCb").is(':checked')) {
									$.each($("input[name='table_cb']"), function(k,v){
										$(v).prop("checked",true)
									})
								} else {
									$.each($("input[name='table_cb']"), function(k,v){
										$(v).prop("checked",false)
									})
								}
							});
						</script>
						<th>基因</th>
						<th>突变</th>
						<th>突变频率</th>
						<th>COSMIC</th>
						<th>基因检测结果类别</th>
						<th>解读结果</th>
						<th>上次审核</th>
						<th>操作</th>
					</tr>
				<c:forEach items="${medicineList}" var="item" varStatus="vs">
					<tr name="medicine_tr">
						<td>
							<c:choose>
								<c:when test="${!item.has_drug}">
									<input id="cb_${vs.count}" type="checkbox" name="table_cb" onclick="initSelect2()">${vs.count}
								</c:when>
								<c:otherwise>
									${vs.count}
								</c:otherwise>
							</c:choose>
						</td>
						<td class="gene">${item.gene}</td>
						<td class="ori_variant" style="cursor:pointer">
							<p id="titleText_${vs.count}" onmouseover='showTitle(${vs.count})' title='' onclick='showWindow("${item.gene}","${item.variant}")'>${item.ori_variant}</p>
							<script type="text/javascript">
								function showTitle(index){
									var data = medicineList[parseInt(index)-1];
									$("#titleText_"+index).attr('title',"转录本号："+(data.transcript||''));
								}
								function showWindow(gene,variant){
									window.open("https://www.ncbi.nlm.nih.gov/search/all/?term="+gene+"%20%20AND%20%20"+variant);
								}
							</script>
						</td>
						<td >${item.mutFreq}
						</td>
						<td style="cursor:pointer"><p style="width:120px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" title="${item.cosmic}">${item.cosmic}</p>
						</td>
						<td>
							<label id="result_type_${vs.count}">${item.resultTypeDesc}</label>
							<script>
								if(typeof medicineList != "undefined"){
									$("#result_type_${vs.count}").html(medicineList[parseInt(${vs.count})-1].resultTypeDesc);
								}
							</script>
						</td>
						<td style="display:none">
							<label id="result_type_val_${vs.count}">${item.resultTypeVal}</label>
							<script>
							if(typeof medicineList != "undefined"){
								$("#result_type_val_${vs.count}").html(medicineList[parseInt(${vs.count})-1].resultTypeVal)
							}
							</script>
						</td>
						<td>
							<c:choose>
								<c:when test="${item.resultTypeVal == 3}">
									<label id="modify_a_${vs.count}">修改</label>
								</c:when>
								<c:otherwise>
									<a id="modify_a_${vs.count}" style="text-decoration: underline; color: blue; cursor: pointer;">修改</a>
								</c:otherwise>
							</c:choose>
							<script>	
								$(document).on('click',"#modify_a_${vs.count}",function(){
									if($("#result_type_val_${vs.count}").html() == '1'){
										showTddDialog("${vs.count}")
									} else if($("#result_type_val_${vs.count}").html() == '2'){
										showUcmdDialog("${vs.count}")
									} else {
										return;
									}
								})
							</script>
						</td>
						<td>
							<label id="result_check_date_${vs.count}">${item.check_date}</label>
						</td>
						<td><input type="button" value="置顶" class="btn"/> <input type="button" value="上移" class="btn"/> <input type="button" value="下移" class="btn"/> <c:choose>
								<c:when test="${!item.has_drug}">
									<input type="button" value="匹配" onclick="updateDrug(${vs.count})" class="btn"/>
								</c:when></c:choose></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<script>
				$(document).on('click',"input[value='置顶']",function(){
					if($(this).parent().parent().index() == 1) {
						alert("已经到顶啦!");
						return;
					}
					var aaa = $(this).parent().parent().index();
					//移动元素
					for(var i = 1; i<aaa;i++){
						$(this).parent().parent().prev().before($(this).parent().parent());
					}
					  $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder", 
							{
								"analysis_report_id":${ geneticMarkerVwPageBean.report_id },
								"variant":$(this).parent().parent().find(".gene").html(),
								"ori_variant":$(this).parent().parent().find(".ori_variant p").html(),
								"type":3
							}
					  );   
				})
				$(document).on('click',"input[value='上移']",function(){
					//判断是否到顶
					if($(this).parent().parent().index() == 1) {
						alert("已经到顶啦!");
						return;
					}
					//移动元素
					$(this).parent().parent().prev().before($(this).parent().parent());
					 $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder", 
							{
								"analysis_report_id":${ geneticMarkerVwPageBean.report_id },
								"variant":$(this).parent().parent().find(".gene").html(),
								"ori_variant":$(this).parent().parent().find(".ori_variant p").html(),
								"type":1
							}
					 );  
				})
				$(document).on('click',"input[value='下移']",function(){
					//判断是否到底
					if($(this).parent().parent().index() == $("#table_wz").find("tr").length-1) {
						alert("已经到底啦!");
						return;
					}
					//移动元素
					$(this).parent().parent().next().after($(this).parent().parent())
					 $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder", 
							{
								"analysis_report_id":${geneticMarkerVwPageBean.report_id },
								"variant":$(this).parent().parent().find(".gene").html(),
								"ori_variant":$(this).parent().parent().find(".ori_variant p").html(),
								'type':2
							}
					 );  
				})
				function updateDrug(index){
					var medicine = medicineList[index-1];
					var variant = encodeURIComponent(medicine.variant)
					var ori_variant = encodeURIComponent(medicine.ori_variant);
					var cosmic = encodeURIComponent(medicine.cosmic);
					var mutFreq = encodeURIComponent(medicine.mutFreq);
					$.post("${pageContext.request.contextPath}/geneMarkerVw/updateFromNkb?userAccount=${user.user_account}&gene="+medicine.gene+"&variant="+variant+"&ori_variant="+ori_variant+"&cosmic="+cosmic+"&mutFreq="+mutFreq+"&disease_id=${diseaseId}&lang=2", null, function(returnData){
						if(!returnData || !returnData.isError) {
							medicineList[index-1].drugList = returnData.drugList||[];
							medicineList[index-1].varDrugNote = returnData.varDrugNote||[];
							medicineList[index-1].clinicalList = returnData.clinicalList||[];
							medicineList[index-1].rpUnknownVar = returnData.rpUnknownVar||{};
							medicineList[index-1].resultTypeDesc = returnData.resultTypeDesc||'';
							medicineList[index-1].resultTypeVal = returnData.resultTypeVal||'';
							$("#result_type_val_"+index).html(medicineList[index-1].resultTypeVal);
							$("#result_type_"+index).html(medicineList[index-1].resultTypeDesc);
							alert("匹配成功");
							$("#result_check_date_"+index).html("未审核");
						}
					}); 
				}
				</script>
			<br>
			<br>
		</div>
		<div id="meditationPoints-show" style="margin-top:30px; margin-left:5%; width:60%; overflow-y: scroll; height: 600px; padding: 10px;">
			<div id="sample-info" style="clear:both">
				<div style="text-align:center">
					<lable style="color:#4BAD5B; font-weight: bold; font-size: 20px;">Patient and Specimen Information</lable>
				</div>
				<table id="sample-info-table" class="preview-content-table">
					<tr>
						<td style="border-top-width: 5px;height:40px;border-bottom-width: 2px;text-align:center;">PATIENT</td>
						<td style="border-top-width: 5px;border-bottom-width: 2px;text-align:center;">SPECIMEN</td>
						<td style="border-top-width: 5px;border-bottom-width: 2px;text-align:center;">PHYSICAN</td>
					</tr>
					<tr>
						<td style="padding-left:10px">Name：<c:out value="${sampleFile.person_name}" default="-"></c:out></td>
						<td style="padding-left:10px">Specimen I.D.：<c:out value="${sampleFile.barcode}" default="-"></c:out></td>
						<td style="padding-left:10px">Ordering Physician：</td>
					</tr>
					<tr>
						<td style="padding-left:10px">Patient NRIC/FIN/ID：</td>
						<td style="padding-left:10px">Specimen Type/Size：<c:out value="${sampleFile.sample_type}" default="-"></c:out></td>
						<td style="padding-left:10px">Institution：</td>
					</tr>
					<tr>
						<td style="padding-left:10px">Gender：
							<c:choose>
								<c:when test="${sampleFile.gender == '男'}"><!-- 如果 -->
									 <c:out value="Mate"></c:out>  
								</c:when>
								<c:when test="${sampleFile.gender == '女'}"><!-- 如果 -->
									 <c:out value="Femate"></c:out>  
								</c:when>
								<c:otherwise> <!-- 否则 -->
								     <c:out value="${sampleFile.gender}"></c:out>                      
								</c:otherwise>
							</c:choose>
						</td>
						<td style="padding-left:10px">Specimen Collection Date：<c:out value="${sampleFile.collect_date}" default="-"></c:out></td>
						<td style="padding-left:10px"></td>
					</tr>
					<tr>
						<td style="padding-left:10px">Data of Birth：<c:out value="${sampleFile.birthday}" default="-"></c:out></td>
						<td style="padding-left:10px">Specimen Received Date：<c:out value="${sampleFile.received_date}" default="-"></c:out></td>
						<td style="padding-left:10px"></td>
					</tr>
					<tr>
						<td style="padding-left:10px">Nationality：</td>
						<td style="padding-left:10px"></td>
						<td style="padding-left:10px"></td>
					</tr>
					<tr>
						<td style="padding-left:10px">Diagnosis：<c:out value="${diseaseName}" default="-"></c:out></td>
						<td style="padding-left:10px"></td>
						<td style="padding-left:10px"></td>
					</tr>
				</table>
			
			</div>
			<div id="detectResult-drugTip-info" style="margin-top:20px">
				<div style="text-align:center">
					<lable style="color:	#4BAD5B; font-weight: bold; font-size: 20px;">Therapeutics Implications</lable>
				</div>
				<div id="target-drug-info" style="margin-top:10px">
					<div style="text-align:center">
						<lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">Targeted Therapy</lable>
					</div>
					<table id="target-drug-table" class="preview-content-table">
						<tr>
							<th rowspan="2" style="width:10%">
								Gene
							</th>
							<th rowspan="2" style="width:30%">
								Variant
							</th>
							<th rowspan="2">
								VAF
							</th>
							<th colspan="3">
								Targeted therapies with potential benefit
							</th>
							<th rowspan="2" style="width: 14%;">
								Information on potential drug resistance
							</th>
						</tr>
						<tr>   
							<th style="border-top-width: 1px;    width: 10%;">Level A</th> 
							<th style="border-top-width: 1px;    width: 10%;">Level B</th>
							<th style="border-top-width: 1px;    width: 10%;">Level C</th>
						</tr>
					</table>
				</div>
				
				<div id="immunity-drug-info" style="margin-top:10px">
					<div style="text-align:center">
						<lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">Immunotherapy </lable>
					</div>
					<table id="bTMB-table" class="preview-content-table">
						<tr>
							<th>
								Type of genomic alterations
							</th>
							<th>
								Test result
							</th>
						</tr>
						<tr>
							<td>Microsatellite status</td>
							<td>${MSI_STATUS}</td>
						</tr>
						<c:choose>
							<c:when test="${sampleFile.sample_type == 'tissue'}"><!-- 如果 -->
								<tr>
									<td>Tumor Mutation Burden (TMB)</td>
									<td>${TMB}Mutations /Megabase</td>
								</tr>
							</c:when>
							<c:when test="${sampleFile.sample_type == 'blood'}"><!-- 如果 -->
								<tr>
									<td>blood Tumor Mutation Burden (bTMB)</td>
									<td>${TMB}Mutations /Megabase</td>
								</tr>
							</c:when>
						</c:choose>
					</table>
				</div>
			<div id="detect-result-analysis-info"  style="margin-top:10px">
				<div style="text-align:center">
					<lable style="color:	#5DA1A6; font-weight: bold; font-size: 20px;">Detailed Test Results about Targeted Therapy</lable>
				</div>
				<div style="text-align:center; margin-top: 10px;">
					<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">Detected Mutations and Related Targeted Therapy</lable>
				</div>
				<div id="target-drug-analysis-tables">
				</div>
				<div style="text-align:center; margin-top: 10px;">
					<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">Detailed Test Results about Chemotherapy</lable>
				</div>
				<div style="margin-top:20px">
					<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">Detected Mutations and the Relevance to Chemotherapy Toxicities</lable>
				</div>
				<table id="chemo-sideeffects-analysis-tables" class="preview-content-table2">
				</table>
				<div style="margin-top:20px">
					<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">Detected Mutations and the Relevance to Chemotherapy Efficacies</lable>
				</div>
				<table id="chemo-effectiveness-analysis-tables" class="preview-content-table2">
				</table>
			</div>
			<br>
		</div>
	</div>
	<!-- 以下是各个浮层弹框的内容 -->
	<div id="disease_risk_dialog_btn" name="disease_risk_dialog_btn" style="display: none;  background-color:darkgrey">
		<table class="tab_col">
			<tr>
				<th>序号</th>
				<th>Gene</th>
				<th>Cancer</th>
				<th>Age</th>
				<th>Risk of Developing Cancer</th>
				<th>Risk of The General Population</th>
				<th>Het/Hom</th>
				<th>操作</th>
			</tr>
			<tbody id="disease_risk_table">
			</tbody>
		</table>
	</div>
	<div id="disease_risk_dialog_add" name="disease_risk_dialog_add" style="display: none;  background-color:darkgrey">
		<input type="hidden" id="disease_risk_gene" name="disease_risk_gene">
		<table>
			<tr>
				<td>Cancer：</td>
				<td><input type="text" id="disease_risk_cancer" name="disease_risk_cancer" value="" placeholder="必填"></td>
			</tr>
			<tr>
				<td>Age：</td>
				<td><input type="text" id="disease_risk_age" name="disease_risk_age" value=""></td>
			</tr>
			<tr>
				<td>Risk of Developing Cancer：</td>
				<td><input type="text" id="risk_of_developing_cancer" name="risk_of_developing_cancer" value=""></td>
			</tr>
			<tr>
				<td>Risk of The General Population：</td>
				<td><input type="text" id="risk_of_the_general_population" name="risk_of_the_general_population" value=""></td>
			</tr>
			<tr>
				<td>Het/Hom：</td>
				<td>
					<select id="het_hom" name="het_hom">
						<option value="1">杂合</option>
						<option value="2">纯合</option>
						<option value="3">纯合/杂合</option>
					</select>
				</td>
			</tr>
		</table>
	</div>
	<div id="risk_intervention_dialog_btn" name="risk_intervention_dialog_btn" style="display: none;  background-color:darkgrey">
		<table class="tab_col">
			<tr>
				<th>序号</th>
				<th>Gene</th>
				<th>Cancer</th>
				<th>measure</th>
				<th>Age</th>
				<th>Frequency</th>
				<th>Het/Hom</th>
				<th>操作</th>
			</tr>
			<tbody id="risk_intervention_table">
			</tbody>
		</table>
	</div>
	<div id="risk_intervention_dialog_add" name="risk_intervention_dialog_add" style="display: none;  background-color:darkgrey">
		<input type="hidden" id="risk_intervention_gene" name="risk_intervention_gene">
		<table>
			<tr>
				<td>Cancer：</td>
				<td><input type="text" id="risk_intervention_cancer" name="risk_intervention_cancer" value="" placeholder="必填"></td>
			</tr>
			<tr>
				<td>Age：</td>
				<td><input type="text" id="risk_intervention_age" name="risk_intervention_age" value=""></td>
			</tr>
			<tr>
				<td>Measure：</td>
				<td><input type="text" id="measure" name="measure" value=""></td>
			</tr>
			<tr>
				<td>Frequency：</td>
				<td><input type="text" id="frequency" name="frequency" value=""></td>
			</tr>
			<tr>
				<td>Het/Hom：</td>
				<td>
					<select id="risk_intervention_het_hom" name="risk_intervention_het_hom">
						<option value="1">杂合</option>
						<option value="2">纯合</option>
						<option value="3">纯合/杂合</option>
					</select>
				</td>
			</tr>
		</table>
	</div>
	<div title="变异解析" id="analytical_variation_dialog" name="analytical_variation_dialog" style="display: none;  background-color:darkgrey">
		<div style="margin-top:0px;">
			<label style="float:left;margin-top: 5px;">基因名称</label>
			<input class="input" style="width: 87%; margin-left: 5%; height: 25px; float: left;" type="text" id="avd_gene_name" value="" readonly/>
		</div>
		<div style="clear:both"></div>
		<div style="margin-top:20px;">
			<label style="float:left;margin-top: 5px;">突变形式</label>
			<input class="input" style="width: 87%; margin-left: 5%; height: 25px; float: left;" type="text" id="avd_mut_style" value="" readonly/>
		</div>
		<div style="clear:both"></div>
		<div style="margin-top:20px;">
			<label style="float:left">突变说明</label>
			<textarea class="textarea" style="width: 87%; margin-left: 5%; height: 60px;" id="avd_mut_desc" readonly></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">基因说明</label>
			<textarea style="width: 87%; margin-left: 5%; height: 60px; resize:vertical;" id="avd_gene_desc" ></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">变异解析</label>
			<textarea style="width: 87%; margin-left: 5%; height: 100px; resize:vertical;" id="avd_mut_analysis" ></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">位点建议</label>
			<textarea style="width: 87%; margin-left: 5%; height: 60px; resize:vertical;" id="suggestion" ></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">位点总结</label>
			<textarea style="width: 87%; margin-left: 5%; height: 60px; resize:vertical;" id="conclusion" ></textarea>
		</div>
	</div>
	<div id="unkonw_clinical_dialog" name="unkonw_clinical_dialog" style="display: none;  background-color:darkgrey">
		<div style="float:left;line-height:50px;">请选择要关联的父级突变(可不选)：</div>
		 	<input type="text" class="input w50" id="ori_variant" />
	          <input type="hidden" id="gene_variant_id" name="gene_variant_id" />
	          <script type="text/javascript">
	          function getOriVariant(gene){
	        	  $.post("${pageContext.request.contextPath}/autoComplete/getGeneVariant", 
	        			{"gene":gene},  
	        			function(data){
	        			$("#ori_variant").flushCache(); 
	        			$('#ori_variant').autocomplete(data, {
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
	        				$("#gene_variant_id").val(row.id);
	        			});
	        		},"json");
	          }
	          </script>
	</div>
	<div title="靶向药物" id="target_drug_dialog" name="target_drug_dialog" style="display: none;  background-color:darkgrey">
		<div >
		    <button class="button" onclick="switchContent(1)">突变信息</button>
		    <button class="button" onclick="switchContent(2)">靶向药物</button>
		    <button class="button" onclick="switchContent(3)">临床试验药物</button>
		</div>
		<div id="content1" name="content" style="display:block; margin-top:20px">
			<div style="margin-top:0px;">
				<label style="float:left;margin-top: 5px;">基因名称</label>
				<input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text" id="tdd_gene_name_1" value="" readonly/>
			</div>
			<div style="clear:both"></div>
			<div style="margin-top:20px;">
				<label style="float:left;margin-top: 5px;">突变形式</label>
				<input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text" id="tdd_mut_style" value="" readonly/>
			</div>
			<div style="clear:both"></div>
			<div style="margin-top:20px;">
				<label style="float:left">突变丰度</label>
				<input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text" id="tdd_mut_freq" value="" readonly/>
			</div>
			<div style="clear:both"></div>
			<div style="margin-top:20px;">
				<label style="float:left">循证医学证据</label>
			</div>
			<div style="clear:both"></div>
			<div style="margin-top:20px;margin-left:30px">
				<form id="tdd_medicine_desc">
					<input type="hidden" id="drug1" name="基因说明:" value="">
					<input type="hidden" id="drug2" name="信号通路说明:" value="">
					<!-- <input type="hidden" id="drug3" name="位点说明:" value="">
					<input type="hidden" id="drug4" name="NCCN指南:" value=""> -->
					<div style="line-height:25px;">位点说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:60px;overflow: auto;word-break: break-all;" id="drug3" name="位点说明:"></textarea>
					<div style="line-height:25px;">NCCN指南:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;" id="drug4" name="NCCN指南:"></textarea>
					<div style="line-height:25px;">预后和诊断说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;" id="drug5" name="预后和诊断说明:" ></textarea>
					<div style="line-height:25px;">用药说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:90px;overflow: auto;word-break: break-all;" id="drug6" name="用药说明:"></textarea>
					<div style="line-height:25px;">耐药说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;" id="drug7" name="耐药说明:"></textarea>
					<div id="recommend1"></div><input type="hidden" id="recommend" name="recommend:" value="">
				</form>
					
				<script>
					$("#tdd_medicine_desc").keyup('textarea',function(e){
						varDrugNote_modified = true;
					})
					
				</script>
			</div>
			<div>
				<button style="width: 80px; float: right; margin-right: 40px;" onclick="saveVarDrugNote()">保存</button>
			</div>
		</div>
		<div id="content2" name="content" style="display:none; margin-top:20px;width:100%">
			<div style="float:left">
				<div style="height: 200px; width: 180px; overflow: auto;">
					<ul id="c2_ul" class="drug-ul" style="background-color:white">
				    </ul>
				</div>
				<div style="margin-top: 20px;">
					<button style="width:100px" onclick="addDrug1()">增加</button><br>
					<button style="width:100px; margin-top:5px" onclick="deleteDrug1()">删除</button><br>
					<button style="width:100px; margin-top:5px" onclick="getDrug1()">抓取药物信息</button><br>
					<button style="width:100px; margin-top:5px" onclick="saveDrug1()">保存</button>
				</div>
			</div>
			<div style="float:left;width: 70%;margin-left: 20px;">
				<div style="margin-top:0px;">
					<label style="float:left;margin-top: 5px; width: 15%;">药物名称</label>
					<input class="input" style="width: 85%; margin-left: 20px; height: 25px; margin-right:0;" type="text" name="c2_tdd_drug_name" id="c2_tdd_drug_name" value=""/>
					<script>
						$.post("${pageContext.request.contextPath}/autoComplete/getDrugNameAndDrugId",{lang:2},function(data){
		        			$('#c2_tdd_drug_name').autocomplete(data, {
		        				max : 30, //列表里的条目数
		        				minChars : 0, //自动完成激活之前填入的最小字符
		        				width : 350, //提示的宽度，溢出隐藏
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
		        				if(listIndex > 0) {
									$("#c2_li_"+listIndex).html($(this).val());
									drugList[listIndex-1].old_drug_name = drugList[listIndex-1].drug_name;
									drugList[listIndex-1].drug_name = row.name;
									drugList[listIndex-1].drug_id = row.id;
									if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
										drugList[listIndex-1].status = "update";
									}
									getDrug1();
								}
		        			});
		        		},"json");
						/* $("#c2_tdd_drug_name").off("change");
						$("#c2_tdd_drug_name").blur(function(e){
							if(listIndex > 0) {
								$("#c2_li_"+listIndex).html($(this).val());
								drugList[listIndex-1].old_drug_name = drugList[listIndex-1].drug_name;
								drugList[listIndex-1].drug_name = $(this).val();
								if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
									drugList[listIndex-1].status = "update";
								}
								getDrug1();
							}
						}) */
					</script>
				</div>
				<div style="clear:both"></div>
				<div style="margin-top:20px;">
					<label style="float:left;margin-top: 5px; width: 15%;">证据级别</label>
					<select id="approve_range_select" style="width: 85%;height: 30px;">
						  <option value="1">A级</option>
						  <option value="2">B级</option>
						  <option value="3">C级</option>
						  <option value="4">D级</option>
						  <option value="5">耐药</option>
					</select>
					<script>
						$("#approve_range_select").off("change");
						$("#approve_range_select").change(function(e){
							if(listIndex > 0) {
								if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
									drugList[listIndex-1].status = "update";
								}
								drugList[listIndex-1].approve_range = $(this).val();
								if($(this).val() == 1) {
									$("#c2_li_"+listIndex).css("color","#000000");
								}
								if($(this).val() == 2) {
									$("#c2_li_"+listIndex).css("color","#0000CD");
								}
								if($(this).val() == 3) {
									$("#c2_li_"+listIndex).css("color","#008B00");
								}
								if($(this).val() == 4) {
									$("#c2_li_"+listIndex).css("color","#CD8500");
								}
								if($(this).val() == 5) {
									$("#c2_li_"+listIndex).css("color","#FF0000");
								}
							}
						})
					</script>
				</div>
				<div style="clear:both"></div>
				<div style="margin-top:20px;">
					<label style="float:left;margin-top: 5px; width: 15%;">是否CFDA批准</label>
					<input id="isCFDA" type="checkbox" style="margin-top: 5px;">
					<script>
						$("#isCFDA").off("change");
						$("#isCFDA").change(function(e){
							if(listIndex > 0) {
								if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
									drugList[listIndex-1].status = "update";
								}
								var drug_name = $("#c2_tdd_drug_name").val();
								if($(this).is(':checked')){
									if ($("#recruit_select").val() == '1') {
										$("#c2_li_"+listIndex).html(drug_name + "*#");
									} else {
										$("#c2_li_"+listIndex).html(drug_name + "*");
									}
									drugList[listIndex-1].cfda = "1";
								} else {
									if ($("#recruit_select").val() == '1') {
										$("#c2_li_"+listIndex).html(drug_name + "#");
									} else {
										$("#c2_li_"+listIndex).html(drug_name);
									}
									drugList[listIndex-1].cfda = "0";
								}
							}
						})
					</script>
				</div>
				<div style="clear:both"></div>
				<div style="margin-top:20px;">
					<label style="float:left;margin-top: 5px; width: 15%;">有没有正在招募的临床试验</label>
					<select id="recruit_select" style="width: 85%;height: 30px;">
						  <option value="1">有</option>
						  <option value="0" selected>没有</option>
					</select>
					<script>
						$("#recruit_select").off("change");
						$("#recruit_select").change(function(e){
							if(listIndex > 0) {
								if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
									drugList[listIndex-1].status = "update";
								}
								var drug_name = $("#c2_tdd_drug_name").val();
								drugList[listIndex-1].recruiting = $(this).val();
								if($(this).val() == 1) {
									if ($("#isCFDA").is(':checked')) {
										$("#c2_li_"+listIndex).html(drug_name + "*#");
									} else {
										$("#c2_li_"+listIndex).html(drug_name+"#");
									}
								}
								if($(this).val() == 0) {
									if ($("#isCFDA").is(':checked')) {
										$("#c2_li_"+listIndex).html(drug_name + "*");
									} else {
										$("#c2_li_"+listIndex).html(drug_name);
									}
								}
							}
						})
					</script>
				</div>
				<div style="clear:both"></div>
				<div style="margin-top:20px;">
					<label style="float:left; width: 15%;">适应症</label>
					<textarea class="textarea" style="width: 85%;  height: 200px;resize:vertical;" id="tdd_indication"></textarea>
					<script>
						$("#tdd_indication").off("change");
						$("#tdd_indication").change(function(e){
							if(listIndex > 0) {
								if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
									drugList[listIndex-1].status = "update";
								}
								drugList[listIndex-1].approval_desc = $(this).val();
							}
						})
					</script>
				</div>
			</div>
		</div>
		<div id="content3" name="content" style="display:none; margin-top:20px">
			<div style="float:left">
				<div style="height: 200px; width: 180px; overflow: auto;">
					<ul id="c3_ul" class="drug-ul" style="background-color:white">
				    </ul>
				</div>
				<div style="margin-top: 20px;">
					<button style="width:120px" onclick="addDrug2()">增加</button><br>
					<button style="width:120px; margin-top:5px" onclick="deleteDrug2()">删除</button><br>
					<button style="width:120px; margin-top:5px" onclick="getDrug2()">抓取临床试验信息</button><br>
					<button style="width:120px; margin-top:5px" onclick="saveDrug2()">保存</button>
				</div>
			</div>
			<div style="float:left;width: 80%;margin-left: 20px;">
				<div style="margin-top:0px;">
					<label style="float:left;margin-top: 5px; width: 10%;">临床试验ID</label>
					<input class="input" style="width: 75%; margin-left: 0%; height: 25px;float:left;" type="text" id="clinical_trial_id" value=""/>
					<button id="clinical_tria_btn" style="width:10%;margin-left:5%;height:25px;">查看</button>
					<script>
						$('#clinical_tria_btn').unbind('click').click(function(){
							if($("#clinical_trial_id").val()){
								window.open("https://clinicaltrials.gov/ct2/show/"+$("#clinical_trial_id").val(),"_blank");
							}else{
								alert("请选择药物！");
							}
						});
					</script>
					<script>
						$("#clinical_trial_id").off("change");
						$("#clinical_trial_id").change(function(e){
							if(clinicalIndex > 0) {
								$("#c3_li_"+clinicalIndex).html($("#c3_tdd_drug_name").val()+"-"+$(this).val());
								clinicalList[clinicalIndex-1].old_clinical_trial_id = clinicalList[clinicalIndex-1].clinical_trial_id;
								clinicalList[clinicalIndex-1].clinical_trial_id = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
								getDrug2();
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%;">药物名称</label>
					<input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="c3_tdd_drug_name" value=""/>
					<script>
						$("#c3_tdd_drug_name").off("change");
						$("#c3_tdd_drug_name").change(function(e){
							if(clinicalIndex > 0) {
								$("#c3_li_"+clinicalIndex).html($(this).val()+"-"+$("#clinical_trial_id").val());
								clinicalList[clinicalIndex-1].drug_name = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%;">临床试验名称</label>
					<input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="title_chinese" value=""/>
					<script>
						$("#title_chinese").off("change");
						$("#title_chinese").change(function(e){
							if(clinicalIndex > 0) {
								clinicalList[clinicalIndex-1].title = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%;">肿瘤类型</label>
					<input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="condition_chinese" value=""/>
					<script>
						$("#condition_chinese").off("change");
						$("#condition_chinese").change(function(e){
							if(clinicalIndex > 0) {
								clinicalList[clinicalIndex-1].recruiting_condition = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%;">临床试验阶段</label>
					<input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="phase" value=""/>
					<script>
						$("#phase").off("change");
						$("#phase").change(function(e){
							if(clinicalIndex > 0) {
								clinicalList[clinicalIndex-1].phase = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%;">临床试验地点</label>
					<input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="location_chinese" value=""/>
					<script>
						$("#location_chinese").off("change");
						$("#location_chinese").change(function(e){
							if(clinicalIndex > 0) {
								clinicalList[clinicalIndex-1].location = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%; ">入组标准</label>
					<textarea style="width: 90%; margin-left: 0%; height: 75px; resize:vertical;" id="inclusion_criteria" ></textarea>
					<script>
						$("#inclusion_criteria").off("change");
						$("#inclusion_criteria").change(function(e){
							if(clinicalIndex > 0) {
								clinicalList[clinicalIndex-1].inclusion_criteria = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
				<div style="margin-top:10px;">
					<label style="float:left;margin-top: 5px; width: 10%; ">排除标准</label>
					<textarea style="width: 90%; margin-left: 0%; height: 75px; resize:vertical;" id="exclusion_criteria" ></textarea>
					<script>
						$("#exclusion_criteria").off("change");
						$("#exclusion_criteria").change(function(e){
							if(clinicalIndex > 0) {
								clinicalList[clinicalIndex-1].exclusion_criteria = $(this).val();
								if(!clinicalList[clinicalIndex-1].status || clinicalList[clinicalIndex-1].status != 'add'){
									clinicalList[clinicalIndex-1].status = "update";
								}
							}
						})
					</script>
				</div>
			</div>
		</div>
	</div>
	<div title="未知临床意义" id="unknow_clinical_meaning_dialog" name="unknow_clinical_meaning_dialog" style="display: none; background-color:darkgrey" float: "left">
		<div style="margin-top:0px;">
			<label style="float:left;margin-top: 5px;">基因名称</label>
			<input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text" id="ucmd_gene_name" value="" readonly/>
		</div>
		<div style="clear:both"></div>
		<div style="margin-top:20px;">
			<label style="float:left;margin-top: 5px;">突变形式</label>
			<input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text" id="ucmd_mut_style" value="" readonly/>
		</div>
		<div style="clear:both"></div>
		<div style="margin-top:20px;">
			<label style="float:left">突变说明</label>
			<textarea class="textarea" style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;" id="ucmd_mut_desc" readonly></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">基因说明</label>
			<textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;" id="ucmd_gene_desc" ></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">用药说明</label>
		</div>
		<div style="clear:both"></div>
		<div style="margin-top:20px;margin-left:30px">	
			<form id="ucmd_medicine_desc">
				<div style="line-height:25px;">预后和诊断说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:70px;overflow: auto;word-break: break-all;" id="ucmd_medicine1" name="预后和诊断说明:" ></textarea>
				<div style="line-height:25px;">用药说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:70px;overflow: auto;word-break: break-all;" id="ucmd_medicine2" name="用药说明:"></textarea>
			</form>
		</div>
		<script>
				$("#ucmd_medicine_desc,#ucmd_gene_desc").off("change");
				$("#ucmd_medicine_desc,#ucmd_gene_desc").change(function(e){
					rpUnknownVar_modified = true;
				});
		</script>
	</div>
	<div title="位置及转录本号" id="pos_transcript_dialog" style="display:none">
		<div>
			<lable>位置：</lable>
			<lable id="pos"></lable>
		</div>
		<div>
			<lable>转录本号：</lable>
			<lable id="transcript2"></lable>
		</div>
	</div>
	<div title="转录本号" id="transcript_dialog" style="display:none">
		<lable>转录本号：</lable>
		<lable id="transcript"></lable>
	</div>
	
	<!-- 以下是预览报告时的一些通用表格 -->
	<div id="target-drug-analysis-gene-template" style="display:none">
		<div style="margin-top: 20px;">
			<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">_gene</lable>
		</div>
		<table class="preview-content-table2">
			<tr>
				<td style="border-top-width: 5px; font-size: 14px; font-weight: bold; color: #5DA1A6; width: 20%;">Variant</td>
				<td style="border-top-width: 5px;" colspan="4">_mutation</td>
			</tr>
			<tr>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">VAF</td>
				<td colspan="4">_mutFreq</td>
			</tr>
			<tr>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6; width: 1%; padding: 10px;" rowspan="3">Targeted therapy</td>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6; padding: 10px;" colspan="3">Potential benefit</td>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6; padding: 10px;" rowspan="2">Potential resistance</td>
			</tr>
			<tr>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">Level A</td>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">Level B</td>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">Level C</td>
			</tr>
			<tr>
				<td style="width:25%;">_drugsA</td>
				<td style="width:25%;">_drugsB</td>
				<td style="width:25%;">_drugsC</td>
				<td style="width:25%;">_resistant</td>
			</tr>
			<tr>
				<td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">Evidence-based Medicine</td>
				<td style="text-align:left;" colspan="4">_drugNote</td>
			</tr>
		</table>
		<div name="target-drug-analysis-druginfo-table-div">
			<div>
				<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">Details of drug information</lable>
			</div>
			<table id="target-drug-analysis-druginfo-table" class="preview-content-table2" name="target-drug-analysis-druginfo-table">
				<tr>
					<th style="width: 20%;">Drugs</th>
					<th>Indications</th>
				</tr>
			</table>
		</div>
		<div name="target-drug-analysis-clinicalinfo-table-div">
			<div>
				<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">Information on related clinical trials</lable>
			</div>
			<table class="preview-content-table2" name="target-drug-analysis-clinicalinfo-table">
				<tr>
					<th style="width: 20%;">Trial ID on ClinicalTrials.gov</th>
					<th>Clinical trial name</th>
					<th style="width: 10%">Tumor type</th>
					<th style="width: 5%">Phase</th>
					<th style="width: 10%">Drug candidate(s)</th>
					<th style="width: 5%">Locations of recruiting sites</th>
					<!-- <th style="">入组标准</th>
					<th style="">排除标准</th> -->
				</tr>
			</table>
		</div>
	</div>
	<div id="unknown-gene-analysis-template" style="display:none">
		<div style="margin-top: 20px;">
			<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">_gene</lable>
		</div>
		<table class="preview-content-table2">
			<tr>
				<td style="width: 20%; border-top-width: 5px;">突变形式</td>
				<td style="border-top-width: 5px;">_mutation</td>
			</tr>
			<tr>
				<td>突变说明</td>
				<td>_mutDesc</td>
			</tr>
			<tr>
				<td>基因说明</td>
				<td>_geneDesc</td>
			</tr>
			<tr>
				<td>用药说明</td>
				<td style="text-align:left;">_drugNote</td>
			</tr>
		</table>
	</div>
	<div id="cancer-risk-analysis-template" style="display:none">
		<div style="margin-top: 20px;">
			<lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">_gene</lable>
		</div>
		<table class="preview-content-table2">
			<tr>
				<td style="width: 20%; border-top-width: 5px;">突变形式</td>
				<td style="border-top-width: 5px;">_mutation</td>
			</tr>
			<tr>
				<td>突变说明</td>
				<td>_mutDesc</td>
			</tr>
			<tr>
				<td>基因说明</td>
				<td>_geneDesc</td>
			</tr>
			<tr>
				<td>变异解析</td>
				<td>_clianno</td>
			</tr>
		</table>
	</div>
</body>
<script>
	var crAllList = ${crAllListJson == null ? "[]" : crAllListJson};
	var medicineList = ${medicineListJson == null ? "[]" : medicineListJson};
	var chemo = ${chemoJson == null ? "[]" : chemoJson};
	
	var chemo_effectiveness = [];
	var chemo_sideeffects = [];
	if(chemo) {
		if(chemo['化疗药物检测解析']){
			chemo_effectiveness = chemo['化疗药物检测解析']['Effectiveness']||[];
			chemo_sideeffects = chemo['化疗药物检测解析']['SideEffects']||[];
		}
	}
	
	// 总的药物列表(本癌种+其它癌种+临床+耐药)
	var drugList = [];
	// 总的临床试验药物列表
	var clinicalList = [];
	
	// 靶向药物数标记
	var drugCount = 0;
	// 临床药物数标记
	var clinicalCount = 0;
	
	// 本癌种
	var thisTargetDrugs = [];
	// 其它癌种
	var thatTargetDrugs = [];
	// 耐药
	var resistantDrugs = [];
	// 临床(下拉列表)
	var clinicalDrugs = [];
	
	// 靶向药物列表下标
	var listIndex = 0;
	// 临床试验药物列表下标
	var clinicalIndex = 0;
	
	// 用药突变表格下标
	var varDrugIndex = -1;
	
	// 靶向药物浮层-用药说明修改标记
	var varDrugNote_modified = false;
	
	//未知临床意义-用药说明修改标记
	var rpUnknownVar_modified = false;
	
	$(document).ajaxSuccess(
         function(event, xhr, options, json){
             if(json.isError) {
            	 	sweetAlert("错误", json.errorMsg, "error");
             }
         }
    );
	
	function initSelect2() {
		$("#select2").val(0);
	}
	
	//获取转义后的ori_variant
	function get_ori_variant_str(data) {
		return (/^[0-9]+$/.test(data.Exon)) ? data.Transcript+" "+"exon"+data.Exon+" "+data.cHGVS.replace(">","&gt;")+" "+data.pHGVS : data.Transcript+" "+data.Exon+" "+data.cHGVS.replace(">","&gt;")+" "+data.pHGVS;
	}
	
	//获取ori_variant
	function get_ori_variant(data) {
		return (/^[0-9]+$/.test(data.Exon)) ? data.Transcript+" "+"exon"+data.Exon+" "+data.cHGVS+" "+data.pHGVS : data.Transcript+" "+data.Exon+" "+data.cHGVS+" "+data.pHGVS;
	}
	
	// 添加用药
	function addRecord(index) {
		var data = crAllList[parseInt(index)-1];
		$.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&hasDrug=1&lang=2", data, function(returnData){
			if(returnData && !returnData.isError) {
				data['rpCr'] = returnData;
				crAllList[parseInt(index)-1] = data;
				crAllList[parseInt(index)-1].check_date = returnData.check_date;
				var mutFreq = ".";
				$.post("${pageContext.request.contextPath}/geneMarkerVw/addDrugRecord?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=2", data, function(returnData){
					if(returnData && !returnData.isError) {
						medicineList.push(returnData);
						mutFreq = returnData.mutFreq;
					}
				});
				 $.post("${pageContext.request.contextPath}/geneMarkerVw/addRPVariantOrder",
						{
							"analysis_report_id":${ geneticMarkerVwPageBean.report_id },
							"index_id":"",
							"variant":data.Gene,
							"ori_variant":get_ori_variant_str(data)
						}
					
				);
				var variant = data.pHGVS.substr(data.pHGVS.indexOf(".")+1,data.pHGVS.length);
				var Gene = data.Gene;
				var new_record_num = $("tr[name='medicine_tr']").length+1;
				var trString = "<tr name='medicine_tr'>";
				trString += '<td>'+new_record_num+'</td>';
				trString += '<td class="gene">'+data.Gene+'</td>';
				trString += '<td class="ori_variant" style="cursor:pointer"><p id="titleText_'+new_record_num+'" onmouseover="showTitle('+new_record_num+')" title="" onclick="showWindow(\''+Gene+'\',\''+variant+'\')">'+get_ori_variant(data)+'</p></td>';
				trString += "<td>"+ data.Zygosity + "</td>";
				trString += '<td style="cursor:pointer"><p style="width:120px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" title=".">.</p></td>';
				trString += "<td><label id='result_type_"+new_record_num+"'>靶向药物</label></td>";
				trString += "<td style='display:none'><label id='result_type_val_"+new_record_num+"'>1</label></td>";
				trString += "<td><a id='modify_a_"+new_record_num+"' style='text-decoration: underline; color: blue; cursor: pointer;'>修改</a>";
				trString += "<script> $(\"#modify_a_"+new_record_num+"\").click(function(e){ if($(\"#result_type_val_"+new_record_num+"\").html() == '1'){showTddDialog(\""+new_record_num+"\")} else if($(\"#result_type_val_"+new_record_num+"\").html() == '2'){showUcmdDialog(\""+new_record_num+"\")} else {return;}});<\/script>";
				trString += "</td>";
				trString += "<td>未审核</td>";
				trString +='<td><input type="button" value="置顶" class="btn"/> <input type="button" value="上移" class="btn"/> <input type="button" value="下移" class="btn"/></td>';
				trString += "</tr>";
				
				$("#tInfo2").append(trString);
				
				/* $.post("${pageContext.request.contextPath}/geneMarkerVw/addDrugRecord?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}", 
						data); */
			}
		});
		
	}
	
	// 删除用药
	function deleteRecord(index) {
		var data = crAllList[parseInt(index)-1];
		var update_flag = false;
		$("tr[name=medicine_tr]").each(function(k,v){
			var td = $(v).find("td");
			if(td && td[1].innerText == data.Gene && td[2].innerText == (get_ori_variant(data))) {
				$(v).remove();
				$.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&hasDrug=0&lang=2", data, function(returnData){
					if(returnData && !returnData.isError) {
						data['rpCr'] = returnData;
						crAllList[parseInt(index)-1] = data;
						crAllList[parseInt(index)-1].check_date = returnData.check_date;
						update_flag = true;
						$.post("${pageContext.request.contextPath}/geneMarkerVw/deleteDrugRecord?diseaseId=${diseaseId}&lang=2", data, function(returnData){
							
						});
						$.post("${pageContext.request.contextPath}/geneMarkerVw/deleteRpVariantOrder", 
						{
							"analysis_report_id":${ geneticMarkerVwPageBean.report_id },
							"variant":data.Gene,
							"ori_variant":get_ori_variant_str(data)
						}
						); 
						$.each(medicineList, function(kk,vv){
							if(vv.gene == data.Gene && vv.ori_variant == (get_ori_variant(data))) {
								medicineList.splice(kk,1);
								return false;
							}
						});
					}
				});
				return false;
			}
		})
		if(!update_flag) {
			$.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=2", data, function(returnData){
				data['rpCr'] = returnData;
				crAllList[parseInt(index)-1] = data;
				crAllList[parseInt(index)-1].check_date = returnData.check_date;
			});
		}
	}
	
	function showMutationAnalysis(index){
		showAvdDialog(index);
	}
	function showdiseaseRisk(index){
		showDiseaseRiskDialog(index);
	}
	// 展示基因的疾病风险表
	function showDiseaseRiskDialog(index){
	    $("#disease_risk_dialog_btn").dialog({
	        width:'70%',
	        height:'auto',
	        position: {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initDiseaseRiskBtnDialog(index);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "添加":function(){
	            	showDiseaseRiskAddDialog(index,"-1");
	            }         
	        }
	    });
	}
	//初始化基因的疾病风险表
	function initDiseaseRiskBtnDialog(index){
		$("#disease_risk_table").html("");
		var data = crAllList[parseInt(index)-1];
		var gene = data.Gene;
		$.post("${pageContext.request.contextPath}/rpCrGeneRisk/getRpCrGeneRiskList?lang=2&gene="+gene, function(returnData){
			if(returnData && !returnData.isError) {
				var DiseaseRisk = "";
				$.each(returnData, function(k,v){
					DiseaseRisk += "<tr>";
					DiseaseRisk += "<td>"+(k+1)+"</td>";
					DiseaseRisk += "<td>"+v.gene+"</td>";
					DiseaseRisk += "<td>"+v.cancer+"</td>";
					DiseaseRisk += "<td>"+v.age+"</td>";
					DiseaseRisk += "<td>"+v.risk_of_developing_cancer+"</td>";
					DiseaseRisk += "<td>"+v.risk_of_general_population+"</td>";
					DiseaseRisk += "<td>"+translateNum(v.hom_flag)+"</td>";
					DiseaseRisk += '<td><a style="padding:0 5px;text-decoration: underline; color: blue; cursor: pointer;" onclick="fixDiseaseRisk('+v.record_id+','+index+')">修改</a><a style="padding:0 5px;text-decoration: underline; color: blue; cursor: pointer;" onclick="deleteDiseaseRisk('+v.record_id+','+index+')">删除</a></td>';
					DiseaseRisk += "</tr>";
				});
				$("#disease_risk_table").html(DiseaseRisk);
			}
		});
	}
	function fixDiseaseRisk(record_id,index){
		showDiseaseRiskAddDialog(index,record_id);
	}
	function deleteDiseaseRisk(record_id,index){
		$.post("${pageContext.request.contextPath}/rpCrGeneRisk/deleteDiseaseRiskByRecordId?record_id="+record_id, function(returnData){
			initDiseaseRiskBtnDialog(index);
		}); 
		
	}
	// 展示基因的疾病风险添加页面
	function showDiseaseRiskAddDialog(index,record_id){
	    $("#disease_risk_dialog_add").dialog({
	        width:'50%',
	        height:'auto',
	        position: {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initDiseaseRiskAddDialog(index,record_id);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "保存":function(){
	            	saveDiseaseRiskAddDialog(index,record_id);
	            }         
	        }
	    });
	}
	function saveDiseaseRiskAddDialog(index,record_id){
		var gene = $("#disease_risk_gene").val();
		var disease_risk_cancer = $("#disease_risk_cancer").val();
		var disease_risk_age = $("#disease_risk_age").val();
		var risk_of_developing_cancer = $("#risk_of_developing_cancer").val();
		var risk_of_the_general_population = $("#risk_of_the_general_population").val();
		var het_hom = $("#het_hom").val();
		if(disease_risk_cancer != null && disease_risk_cancer != ""){
			$.post("${pageContext.request.contextPath}/rpCrGeneRisk/saveDiseaseRisk",{"record_id":record_id,"gene":gene,"lang":2,"hom_flag":het_hom,"cancer":disease_risk_cancer,"age":disease_risk_age,"risk_of_developing_cancer":risk_of_developing_cancer,"risk_of_general_population":risk_of_the_general_population}, function(returnData){
				$("#disease_risk_dialog_add").dialog("destroy");//关闭当前弹窗
				initDiseaseRiskBtnDialog(index);
			});
		}else{
			alert("Cancer不能为空");
		}
		
	}
	function initDiseaseRiskAddDialog(index,record_id){
		var data = crAllList[parseInt(index)-1];
		var gene = data.Gene;
		$("#disease_risk_gene").val(gene);
		if(record_id == "-1"){
			$("#disease_risk_cancer").val("");
			$("#disease_risk_age").val("");
			$("#risk_of_developing_cancer").val("");
			$("#risk_of_the_general_population").val("");
			$("#het_hom").val("1");
		}else{
			$.post("${pageContext.request.contextPath}/rpCrGeneRisk/getRpCrGeneRiskByRecordId",{"record_id":record_id}, function(returnData){
				$("#disease_risk_cancer").val(returnData.cancer);
				$("#disease_risk_age").val(returnData.age);
				$("#risk_of_developing_cancer").val(returnData.risk_of_developing_cancer);
				$("#risk_of_the_general_population").val(returnData.risk_of_general_population);
				$("#het_hom").val(returnData.hom_flag);
			});
		}
	}
	// 展示风险干预措施表
	function showRiskInterventionDialog(index){
	    $("#risk_intervention_dialog_btn").dialog({
	        width:'70%',
	        height:'auto',
	        position: {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initRiskInterventionDialog(index);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "添加":function(){
	            	showRiskInterventionAddDialog(index,"-1");
	            }         
	        }
	    });
	}
	// 初始化风险干预措施
	function initRiskInterventionDialog(index){
		$("#risk_intervention_table").html("");
		var data = crAllList[parseInt(index)-1];
		var gene = data.Gene;
		$.post("${pageContext.request.contextPath}/rpCrGeneRiskReduction/getRpCrGeneRiskReductionList?lang=2&gene="+gene, function(returnData){
			if(returnData && !returnData.isError) {
				var DiseaseRisk = "";
				$.each(returnData, function(k,v){
					DiseaseRisk += "<tr>";
					DiseaseRisk += "<td>"+(k+1)+"</td>";
					DiseaseRisk += "<td>"+v.gene+"</td>";
					DiseaseRisk += "<td>"+v.cancer+"</td>";
					DiseaseRisk += "<td>"+v.measure+"</td>";
					DiseaseRisk += "<td>"+v.age+"</td>";
					DiseaseRisk += "<td>"+v.frequency+"</td>";
					DiseaseRisk += "<td>"+translateNum(v.hom_flag)+"</td>";
					DiseaseRisk += '<td><a style="padding:0 5px;text-decoration: underline; color: blue; cursor: pointer;" onclick="fixRiskIntervention('+v.record_id+','+index+')">修改</a><a style="padding:0 5px;text-decoration: underline; color: blue; cursor: pointer;" onclick="deleteRiskIntervention('+v.record_id+','+index+')">删除</a></td>';
					DiseaseRisk += "</tr>";
				});
				$("#risk_intervention_table").html(DiseaseRisk);
			}
		});
	}
	function fixRiskIntervention(record_id,index){
		showRiskInterventionAddDialog(index,record_id);
	}
	function deleteRiskIntervention(record_id,index){
		$.post("${pageContext.request.contextPath}/rpCrGeneRiskReduction/deleteDiseaseRiskReductionByRecordId?record_id="+record_id, function(returnData){
			initRiskInterventionDialog(index);
		});
	}
	// 展示基因的风险干预措施添加页面
	function showRiskInterventionAddDialog(index,record_id){
	    $("#risk_intervention_dialog_add").dialog({
	        width:'50%',
	        height:'auto',
	        position: {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initRiskInterventionAddDialog(index,record_id);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "保存":function(){
	            	saveRiskInterventionAddDialog(index,record_id);
	            }         
	        }
	    });
	}
	function initRiskInterventionAddDialog(index,record_id){
		var data = crAllList[parseInt(index)-1];
		var gene = data.Gene;
		$("#risk_intervention_gene").val(gene);
		if(record_id == "-1"){
			$("#risk_intervention_cancer").val("");
			$("#risk_intervention_age").val("");
			$("#measure").val("");
			$("#frequency").val("");
			$("#risk_intervention_het_hom").val("1");
		}else{
			$.post("${pageContext.request.contextPath}/rpCrGeneRiskReduction/getRpCrGeneRiskReductionByRecordId",{"record_id":record_id}, function(returnData){
				$("#risk_intervention_cancer").val(returnData.cancer);
				$("#risk_intervention_age").val(returnData.age);
				$("#measure").val(returnData.measure);
				$("#frequency").val(returnData.measure);
				$("#risk_intervention_het_hom").val(returnData.hom_flag);
			});
		}
	}
	function saveRiskInterventionAddDialog(index,record_id){
		var gene = $("#risk_intervention_gene").val();
		var risk_intervention_cancer = $("#risk_intervention_cancer").val();
		var risk_intervention_age = $("#risk_intervention_age").val();
		var measure = $("#measure").val();
		var frequency = $("#frequency").val();
		var risk_intervention_het_hom = $("#risk_intervention_het_hom").val();
		if(disease_risk_cancer != null && disease_risk_cancer != ""){
			$.post("${pageContext.request.contextPath}/rpCrGeneRiskReduction/saveDiseaseRiskReduction",{"record_id":record_id,"gene":gene,"lang":2,"hom_flag":risk_intervention_het_hom,"cancer":risk_intervention_cancer,"age":risk_intervention_age,"measure":measure,"frequency":frequency}, function(returnData){
				$("#risk_intervention_dialog_add").dialog("destroy");//关闭当前弹窗
				initRiskInterventionDialog(index);
			});
		}else{
			alert("Cancer不能为空");
		}
	}
	// 展示变异解析对话框
	function showAvdDialog(index){
	    $("#analytical_variation_dialog").dialog({
	        width:'75%',
	        height:'auto',
	        position: {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initAvdDialog(index);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "保存":function(){
	            	saveAvdDialog(index);
	            	$(this).dialog("destroy");//关闭当前弹窗
	            },
	            "关闭":function(){ 
	                $(this).dialog("destroy");//关闭当前弹窗
	            }           
	        }
	    });
	}
	// 展示靶向药物对话框
	function showTddDialog(index){
		varDrugIndex = index;
	    $("#target_drug_dialog").dialog({
	        width:'75%',
	        height:'auto',
	        position :  {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initTddDialog(index);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        	closeTddDialog();
	        },
	        buttons:{//设置页面的按钮
	            "关闭":function(){ 
	                $(this).dialog("destroy");//关闭当前弹窗
	                closeTddDialog();
	            }           
	        }
	    });
	}
	// 选择要关联的父级突变对话框
	function showGimDialog(gene,index){
	    $("#unkonw_clinical_dialog").dialog({
	        width:'40%',
	        height:'auto',
	        position :  {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initGimDialog();
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        	closeGimDialog();
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "保存":function(){
	            	saveGimDialog(index,$("#gene_variant_id").val());
	            	$(this).dialog("destroy");//关闭当前弹窗
	            }          
	        }
	    });
	}
	function initGimDialog() {
		$("#ori_variant").val("");
		$("#gene_variant_id").val("");
	}
	function closeGimDialog(){
		$("#ori_variant").val("");
		$("#gene_variant_id").val("");
	}
	function saveGimDialog(id,gene_variant_id){
		deleteUnknownVar(id,gene_variant_id);
	}
	// 展示未知临床意义对话框
	function showUcmdDialog(index){
	    $("#unknow_clinical_meaning_dialog").dialog({
	        width:'75%',
	        height:'auto',
	        position : {
	      　　　　　　my: "center",
	      　　　　　　at: "left",
	      　　　　　　of: window,
	      　　　　　　collision: "fit",
	      　　　　　　// Ensure the titlebar is always visible
	      　　　　　　using: function( pos ) {
	      　　　　　　　　var topOffset = $( this ).css( pos ).offset().top;
	      　　　　　　　　if ( topOffset < 0 ) {
	      　　　　　　　　　　$( this ).css( "top", pos.top - topOffset );
	      　　　　　　　　}
	      　　　　　　}
	      　　},
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        	initUcmdDialog(index);
	        },
	        close:function(e, ui) {
	        	$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            //按钮的名字:对应的方法
	            "保存":function(){
	            	saveUcmdDialog(index);
	            	$(this).dialog("destroy");//关闭当前弹窗
	            },
	            "关闭":function(){ 
	                $(this).dialog("destroy");//关闭当前弹窗
	            }           
	        }
	    });
	}
	function showPosTranscriptDialog(index) {
		$("#pos_transcript_dialog").dialog({
	        width:300,
	        height:'auto',
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        		initPosTranscriptDialog(index);
	        },
	        close:function(e, ui) {
	        		$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            "关闭":function(){ 
	                $(this).dialog("destroy");//关闭当前弹窗
	            }           
	        }
	    });
	}
	
	// 展示转录本号对话框
	function showTranscriptDialog(index) {
		$("#transcript_dialog").dialog({
	        width:300,
	        height:'auto',
	        closeOnEscape:true,//右上角没有叉号
	        //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
	        modal:true,//弹窗的时候不能动页面的其他地方（模态模式）
	        create:function(e, ui){//开始执行的方法，用来处理弹出时事件
	        		initTranscriptDialog(index);
	        },
	        close:function(e, ui) {
	        		$(this).dialog("destroy");//关闭当前弹窗
	        },
	        buttons:{//设置页面的按钮
	            "关闭":function(){ 
	                $(this).dialog("destroy");//关闭当前弹窗
	            }           
	        }
	    });
	}
	// 初始化变异解析对话框
	function initAvdDialog(index) {
		varDrugIndex = index;
		var data = crAllList[parseInt(index)-1];
		$("#avd_gene_name").val(data.Gene);
		$("#avd_mut_style").val(get_ori_variant(data));
		$("#avd_mut_desc").val(data.mutDesc);
		if(data && data.rpCr) {
			$("#avd_gene_desc").val(data.rpCr.GeneDesc||'');
			$("#avd_mut_analysis").val(data.rpCr.VarClianno||'');
			$("#suggestion").val(data.suggestion||'');
			$("#conclusion").val(data.conclusion||'');
		} else {
			$("#avd_gene_desc").val('');
			$("#avd_mut_analysis").val('');
			$("#suggestion").val('');
			$("#conclusion").val('');
		}
	}
	function isJSON(str) {
	    if (typeof str == 'string') {
	        try {
	            JSON.parse(str);
	            return true;
	        } catch(e) {
	            return false;
	        }
	    } 
	}
	function conversionStr2(str1){
		$.each($.parseJSON(str1),function(i,val){ 
			if(val.key == "基因说明:"){
				$('#drug1').val(val.value);
			}
			if(val.key == "信号通路说明:"){
				$('#drug2').val(val.value);
			}
			if(val.key == "位点说明:"){
				$('#drug3').val(val.value);
			}
			if(val.key == "NCCN指南:"){
				$('#drug4').val(val.value);
			}
			if(val.key == "预后和诊断说明:"){
				$('#drug5').val(val.value);
			}
			if(val.key == "用药说明:"){
				$('#drug6').val(val.value);
			}
			if(val.key == "耐药说明:"){
				$('#drug7').val(val.value);
			}
			if(val.key == "recommend:"){
				$('#recommend').val(val.value);
				$('#recommend1').html(val.value);
			}
		}); 
	}
	function initconversionStr2(){
		$('#drug1').val('');
		$('#drug2').val('');
		$('#drug3').val('');
		$('#drug4').val('');
		$('#drug5').val('');
		$('#drug6').val('');
		$('#drug7').val('');
		$('#recommend').val('');
		$('#recommend1').html('');
	}
	function initconversionStr(){
		$('#ucmd_medicine1').val('');
		$('#ucmd_medicine2').val('');
	}
	//将用药说明转换为文本
	function conversionStr(str1){
		$.each($.parseJSON(str1),function(i,val){ 	
			if(val.key == "预后和诊断说明:"){
				$('#ucmd_medicine1').val(val.value);
			}
			if(val.key == "用药说明:"){
				$('#ucmd_medicine2').val(val.value);
			}
		}); 
	}
	// 初始化靶向药物对话框
	function initTddDialog(index) {
		varDrugNote_modified = false;
		varDrugIndex = index;
		var data = medicineList[parseInt(index)-1];
		$("#tdd_gene_name_1").val(data.gene);
		$("#tdd_mut_style").val(data.ori_variant);
		$("#tdd_mut_desc").val(data.mutDesc);
		$("#tdd_mut_freq").val(data.mutFreq);
		$('#recommend').val('');
		$('#recommend1').html('');
		if(data.varDrugNote){
			conversionStr2(data.varDrugNote);
		}else{
			initconversionStr2();
		}
		/* if(data.varDrugNote){
			$("#tdd_medicine_desc").html(conversionStr2(data.varDrugNote));
		}else{
			$("#tdd_medicine_desc").html("");
		} */
		
		drugList = data.drugList||[];
		clinicalList = data.clinicalList||[];
		
		if(drugList){
			/*
			thisTargetDrugs = drugList.filter(getThisTargetDrugs);
			thatTargetDrugs = drugList.filter(getThatTargetDrugs);
			resistantDrugs = drugList.filter(getResistantDrugs);
			clinicalDrugs = drugList.filter(getClinicalDrugs);
			*/
		}
		// 初始化靶向药物内容
		drugCount = 0;
		clinicalCount = 0;
		if(drugList){
			var drugSet = {};
			for (let i = 0; i < drugList.length; i++) {
			//$.each(drugList,function(k,v){
				var v = drugList[i];
				var drugName = v.drug_name;
				if (drugSet[drugName]) { continue; }
				drugSet[drugName] = true;
				drugCount++;
				if(v.status == 'delete'){
					return true;
				}
				var color = 'black'
				if(v.approve_range ==  '1') {
					color = '#000000';
				} else if(v.approve_range ==  '2'){
					color = '#0000CD';
				} else if(v.approve_range ==  '3'){
					color = '#008B00';
				} else if(v.approve_range ==  '4'){
					color = '#CD8500';
				} else if(v.approve_range ==  '5'){
					color = '#FF0000';
				}
				
				if(v.cfda == '1') {
					drugName += "*";
				}
				if(v.recruiting == '1') {
					drugName += "#";
				}
				var newLiStr = "<li id=\"c2_li_"+drugCount+"\" class=\"drug-li\" style=\"color:"+color+"\" onclick=\"handleTargetInfo('"+drugCount+"')\">"+drugName+"</li>";
				$("#c2_ul").append(newLiStr);
			}
		}
		
		if(clinicalList){
			$.each(clinicalList,function(k,v){
				clinicalCount++;
				if(v.status == 'delete'){
					return true;
				}
				var clinical_trial_id = v.clinical_trial_id;
				var drugName = v.drug_name;
				var newLiStr = "<li id=\"c3_li_"+clinicalCount+"\" class=\"drug-li\" onclick=\"handleTargetInfo2('"+clinicalCount+"')\">"+drugName+"-"+clinical_trial_id+"</li>";
				$("#c3_ul").append(newLiStr);
			})
		}
	}
	// 初始化未知临床意义对话框
	function initUcmdDialog(index) {
		rpUnknownVar_modified = false;
		var data = medicineList[parseInt(index)-1];
		$("#ucmd_gene_name").val(data.gene);
		$("#ucmd_mut_style").val(data.ori_variant);
		$("#ucmd_mut_desc").val(data.mutDesc);
		$("#ucmd_gene_desc").val(data.rpUnknownVar.gene_description);
		if(data.rpUnknownVar.var_drug_desc){
			conversionStr(data.rpUnknownVar.var_drug_desc);
		}else{
			initconversionStr();
		}
	}
	function initPosTranscriptDialog(index) {
		var data = crAllList[parseInt(index)-1];
		$("#pos").html(data.Pos||'');
		$("#transcript2").html(data.Transcript||'');
	}
	// 初始化转录本号对话框
	function initTranscriptDialog(index) {
		var data = medicineList[parseInt(index)-1];
		$("#transcript").html(data.transcript||'');
	}
	// 保存变异解析对话框
	function saveAvdDialog(index) {
		var data = crAllList[parseInt(index)-1];
		data.geneDesc = $("#avd_gene_desc").val();
		data.varClianno = $("#avd_mut_analysis").val();
		data.vardesc = $("#avd_mut_desc").val();
		data.suggestion = $("#suggestion").val();
		data.conclusion = $("#conclusion").val();
		$.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=2", data, function(returnData){
			if(returnData && !returnData.isError) {
				swal("成功！", "保存成功", "success");
				data['rpCr'] = returnData;
				crAllList[parseInt(index)-1] = data;
				crAllList[parseInt(index)-1].check_date = returnData.check_date;
				$("#cr_check_date_"+index).html("未审核");
			}
		});
	}
	
	// 保存未知临床意义对话框
	function saveUcmdDialog(index) {
		var data = medicineList[parseInt(index)-1].rpUnknownVar || {};
		var ori_variant = encodeURIComponent(data.ori_variant);
		data.gene_description = $("#ucmd_gene_desc").val();
		var aaa= $('#ucmd_medicine_desc').serializeArray();
		aaa = JSON.stringify(aaa);
		aaa = aaa.replace(/"name"/g, '"key"');
		data.var_drug_desc = aaa;
		if(rpUnknownVar_modified){
			data.modified = rpUnknownVar_modified;
			$.post("${pageContext.request.contextPath}/geneMarkerVw/saveUnknownVar?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&gene="+data.gene+"&ori_variant="+ori_variant+"&disease_id=${diseaseId}", data, function(returnData){
				if(!returnData || !returnData.isError){
					swal("成功！", "保存成功", "success");
					medicineList[parseInt(index)-1].check_date = null;
					$("#result_check_date_"+index).html("未审核");
				}
			});
		}
	}
	function switchContent(index) {
		$("div[name=content]").each(function(k,v){
			if(v.id != 'content'+index) {
				$(v).hide();
			} else {
				$(v).show();
			}
		})
	}
	function closeTddDialog(){
		switchContent(1);
		$("#c2_ul").html("");
		$("#c2_tdd_drug_name").val("");
		$("#approve_range_select").val(1);
		$("#recruit_select").val(0);
		$("#isCFDA").prop("checked",false);
		$("#tdd_indication").val("");
		$("#c3_ul").html("");
		$("#c3_tdd_drug_name").val("");
		$("#clinical_trial_id").val("");
		$("#title_chinese").val("");
		$("#condition_chinese").val("");
		$("#phase").val("");
		$("#location_chinese").val("");
		$("#inclusion_criteria").val("");
		$("#exclusion_criteria").val("");
		listIndex = 0;
		clinicalIndex = 0;
	}
	function getThisTargetDrugs(drug){
		return drug.relationship != 'Resistant' && drug.evidence_phase == 'Approved';
	}
	function getThatTargetDrugs(drug){
		return drug.relationship != 'Resistant' && drug.evidence_phase == 'Guildline recommended';
	}
	function getResistantDrugs(drug){
		return drug.relationship == 'Resistant';
	}
	function getClinicalDrugs(drug){
		return drug.relationship != 'Resistant' && drug.evidence_phase != 'Approved' && drug.evidence_phase != 'Guildline recommended';
	}
	// 处理靶向药物列表的点击事件
	function handleTargetInfo(index){
		listIndex = index;
		var targetDrug = drugList[index-1];
		$("#c2_tdd_drug_name").val(targetDrug.drug_name);
		if(targetDrug.cfda == 1){
			$("#isCFDA").prop("checked",true);
		} else {
			$("#isCFDA").prop("checked",false);
		}
		$("#approve_range_select").val(targetDrug.approve_range);
		$("#recruit_select").val(targetDrug.recruiting);
		// 待定
		$("#tdd_indication").val(targetDrug.approval_desc);
		
		/* if(targetDrug.relationship != 'Resistant' && targetDrug.evidence_phase == 'Approved') {
			$("#approve_range_select").val(1);
		} else if(targetDrug.relationship != 'Resistant' && targetDrug.evidence_phase == 'Guildline recommended'){
			$("#approve_range_select").val(2);
		} else if(targetDrug.relationship == 'Resistant'){
			$("#approve_range_select").val(4);
		} else if(targetDrug.relationship != 'Resistant' && targetDrug.evidence_phase != 'Approved' && targetDrug.evidence_phase != 'Guildline recommended'){
			$("#approve_range_select").val(3);
		} */
	}
	
	function addDrug1() {
		drugCount++;
		listIndex = drugCount;
		var newLiStr = "<li id=\"c2_li_"+drugCount+"\" style=\"color: #551A8B\" class=\"drug-li\" onclick=\"handleTargetInfo('"+drugCount+"')\">新建药物</li>";
		$("#c2_tdd_drug_name").val("新建药物");
		drugList.push({"drug_name":"新建药物","approve_range":"1","recruiting":"0","cfda":"0","status":"add"});
		$("#c2_ul").append(newLiStr);
		$("#isCFDA").prop("checked",false);
		$("#approve_range_select").val("1");
		$("#recruit_select").val("0");
		$("#tdd_indication").val("");
		
	}
	function deleteDrug1() {
		if(listIndex > 0) {
			drugList[listIndex-1].status = 'delete';
			$("#c2_tdd_drug_name").val("");
			$("#isCFDA").prop("checked",false);
			$("#approve_range_select").val("1");
			$("#recruit_select").val("0");
			$("#tdd_indication").val("");
			$("#c2_li_"+listIndex).remove();
			listIndex = 0;
		}
	}
	// 抓取药物信息
	function getDrug1(){
		if(listIndex > 0) {
			var drug_name = encodeURIComponent(drugList[listIndex-1].drug_name);
			$.post("${pageContext.request.contextPath}/geneMarkerVw/getDrugInfo?lang=2&drug_name="+drug_name+"&report_id=${geneticMarkerVwPageBean.report_id}", function(returnData){
				if(returnData && !returnData.isError){
					swal("成功！", "获取数据成功", "success");
					drugList[listIndex-1].cfda = returnData.cfda;
					if(returnData.cfda == '1') {
						$("#c2_li_"+listIndex).html(returnData.drug_name+"*");
					} else {
						$("#c2_li_"+listIndex).html(returnData.drug_name);
					}
					if(drugList[listIndex-1].recruiting == '1') {
						$("#c2_li_"+listIndex).html(returnData.drug_name+"#");
					}
					drugList[listIndex-1].old_drug_name = returnData.drug_name;
					drugList[listIndex-1].approval_desc = returnData.approval_desc;
					drugList[listIndex-1].disease_id = returnData.disease_id;
					drugList[listIndex-1].status = returnData.status;
					handleTargetInfo(listIndex);
				}
			});
		}
	}
	// 保存药物信息
	function saveDrug1() {
		var medicine = medicineList[varDrugIndex-1];
		var ori_variant = encodeURIComponent(medicine.ori_variant)
		$.ajax({
			type: "post",
			url: "${pageContext.request.contextPath}/geneMarkerVw/saveDrugRecord?userAccount=${user.user_account}&gene="+medicine.gene+"&ori_variant="+ori_variant+"&disease_id=${diseaseId}&record_id="+medicine.record_id+"&lang=2",
			data: JSON.stringify(drugList),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				if(data && !data.isError) {
					swal("成功！", "保存成功", "success");
					drugList = data;
					medicineList[varDrugIndex-1].drugList = drugList;
					medicineList[varDrugIndex-1].check_date = null;
					$("#result_check_date_"+varDrugIndex).html("未审核");
				}
			},
			error: function(data){
				
			}
		})
	}
	
	// 保存靶向药物用药说明
	function saveVarDrugNote() {
		var medicine = medicineList[varDrugIndex-1];
		var ori_variant = encodeURIComponent(medicine.ori_variant);
		var aaa= $('#tdd_medicine_desc').serializeArray();
		aaa = JSON.stringify(aaa);
		aaa = aaa.replace(/"name"/g, '"key"');
		if(varDrugNote_modified) {
			$.post("${pageContext.request.contextPath}/geneMarkerVw/updateVarDrugNote?userAccount=${user.user_account}&gene="+medicine.gene+"&ori_variant="+ori_variant+"&disease_id=${diseaseId}&record_id="+medicine.record_id+"&lang=2", 
					{"var_drug_desc":aaa}, 
			function(returnData){
				if(!returnData || !returnData.isError) {
					swal("成功！", "保存成功", "success");
					medicineList[varDrugIndex-1].varDrugNote = aaa;
					medicineList[varDrugIndex-1].check_date = null;
					$("#result_check_date_"+varDrugIndex).html("未审核");
				}
			});
		 }
	}
	$.fn.serializeObject = function()
	{
	    var o = '[';
	    var a = this.serializeArray();
	    $.each(a, function() {
	    	o += '{"key":"'+this.name+'","value":"'+this.value+'"},'
	    });
	    o = o.substring(0,o.length-1);
	    o+=']';
	    return o;
	};
	
	// 删除用药并添加未知临床意义(修改'基因检测结果类别'时触发)
	function deleteDrugAndAddUnknownVar(index, resultType) {
		var medicine = medicineList[index];
		var variant = encodeURIComponent(medicine.variant);
		var ori_variant = encodeURIComponent(medicine.ori_variant);
		$.post("${pageContext.request.contextPath}/geneMarkerVw/deleteDrugAndAddUnknownVar?userAccount=${user.user_account}&gene="+medicine.gene+"&variant="+variant+"&ori_variant="+ori_variant+"&disease_id=${diseaseId}&lang=2&resultType="+resultType, null, function(returnData){
			if(!returnData || !returnData.isError) {
				medicineList[index].resultTypeDesc = returnData.resultTypeDesc;
				medicineList[index].resultTypeVal = returnData.resultTypeVal;
				medicineList[index].drugList = [];
				medicineList[index].clinicalList = [];
				medicineList[index].rpUnknownVar = returnData.rpUnknownVar||{};
			}
		});
	}
	
	// 删除未知临床意义(修改'基因检测结果类别'时触发)
	function deleteUnknownVar(index,gene_variant_id) {
		var medicine = medicineList[index];
		var variant = encodeURIComponent(medicine.variant)
		var ori_variant = encodeURIComponent(medicine.ori_variant);
		var cosmic = encodeURIComponent(medicine.cosmic);
		var mutFreq = encodeURIComponent(medicine.mutFreq);
		$.post("${pageContext.request.contextPath}/geneMarkerVw/deleteUnknownVar?userAccount=${user.user_account}&gene="+medicine.gene+"&variant="+variant+"&ori_variant="+ori_variant+"&cosmic="+cosmic+"&mutFreq="+mutFreq+"&disease_id=${diseaseId}&lang=2&parent_mutID="+gene_variant_id, null, function(returnData){
			if(!returnData || !returnData.isError) {
				medicineList[index].resultTypeDesc = returnData.resultTypeDesc;
				medicineList[index].resultTypeVal = returnData.resultTypeVal;
				medicineList[index].drugList = returnData.drugList||[];
				medicineList[index].varDrugNote = returnData.varDrugNote||[];
				medicineList[index].clinicalList = returnData.clinicalList||[];
				medicineList[index].rpUnknownVar = null;
				$("#result_type_"+(index+1)).html($("#select2").find("option:selected").text());
				$("#result_type_val_"+(index+1)).html($("#select2").find("option:selected").val());
			}
		});
	}
	
	// 新增临床试验药物
	function addDrug2() {
		clinicalCount++;
		clinicalIndex = clinicalCount;
		var newLiStr = "<li id=\"c3_li_"+clinicalCount+"\" class=\"drug-li\" onclick=\"handleTargetInfo2('"+clinicalCount+"')\">新建药物-新建临床试验ID</li>";
		$("#c3_tdd_drug_name").val("新建药物");
		clinicalList.push({"clinical_trial_id":"新建临床试验ID","drug_name":"新建药物","status":"add"});
		$("#c3_ul").append(newLiStr);
		$("#clinical_trial_id").val("新建临床试验ID");
		$("#title_chinese").val("");
		$("#condition_chinese").val("");
		$("#phase").val("");
		$("#location_chinese").val("");
		$("#inclusion_criteria").val("");
		$("#exclusion_criteria").val("");
	}
	// 删除临床试验药物
	function deleteDrug2() {
		if(clinicalIndex > 0) {
			clinicalList[clinicalIndex-1].status = 'delete';
			$("#c3_tdd_drug_name").val("");
			$("#clinical_trial_id").val("");
			$("#title_chinese").val("");
			$("#condition_chinese").val("");
			$("#phase").val("");
			$("#location_chinese").val("");
			$("#inclusion_criteria").val("");
			$("#exclusion_criteria").val("");
			$("#c3_li_"+clinicalIndex).remove();
			clinicalIndex = 0;
		}
	}
	// 抓取临床试验信息
	function getDrug2(){
		if(clinicalIndex > 0) {
			var clinical_trial_id = encodeURIComponent(clinicalList[clinicalIndex-1].clinical_trial_id);
			var drug_name = encodeURIComponent(clinicalList[clinicalIndex-1].drug_name);
			$.post("${pageContext.request.contextPath}/geneMarkerVw/getClinicalInfo?clinical_trial_id="+clinical_trial_id + "&lang=2&drug_name="+drug_name, function(returnData){
				if(returnData && !returnData.isError){
					swal("成功！", "获取数据成功", "success");
					clinicalList[clinicalIndex-1].title = returnData.title;
					clinicalList[clinicalIndex-1].recruiting_condition = returnData.recruiting_condition;
					clinicalList[clinicalIndex-1].phase = returnData.phase;
					clinicalList[clinicalIndex-1].location = returnData.location;
					clinicalList[clinicalIndex-1].inclusion_criteria = returnData.inclusion_criteria;
					clinicalList[clinicalIndex-1].exclusion_criteria = returnData.exclusion_criteria;
					clinicalList[clinicalIndex-1].status = returnData.status;
					clinicalList[clinicalIndex-1].cfda = returnData.cfda
					handleTargetInfo2(clinicalIndex);
				}
			});
		}
	}
	// 保存临床试验信息
	function saveDrug2() {
		var medicine = medicineList[varDrugIndex-1];
		var ori_variant = encodeURIComponent(medicine.ori_variant);
		$.ajax({
			type: "post",
			url: "${pageContext.request.contextPath}/geneMarkerVw/saveClinicalRecord?userAccount=${user.user_account}&gene="+medicine.gene+"&ori_variant="+ori_variant+"&disease_id=${diseaseId}&record_id="+medicine.record_id+"&lang=2",
			data: JSON.stringify(clinicalList),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				if(data && !data.isError) {
					swal("成功！", "保存成功", "success");
					clinicalList = data;
					medicineList[varDrugIndex-1].clinicalList = clinicalList;
					medicineList[varDrugIndex-1].check_date = null;
					$("#result_check_date_"+varDrugIndex).html("未审核");
					/* var dl = medicineList[varDrugIndex-1].drugList;
					$.each(clinicalList,function(k,v){
						var flag = false;
						$.each(dl,function(kk,vv){
							if(vv.drug_name_chinese == v.drug_name_chinese){
								flag = true;
								return false;
							}
						});
						if(!flag) {
							drugCount++;
							var newLiStr = "<li id=\"c2_li_"+drugCount+"\" style=\"color: #00688B\" class=\"drug-li\" onclick=\"handleTargetInfo('"+drugCount+"')\">"+v.drug_name_chinese+"</li>";
							$("#c2_tdd_drug_name").val(v.drug_name_chinese);
							drugList.push({"drug_name_chinese":v.drug_name_chinese,"approve_range":"4","cfda":"0","status":"add"});
							$("#c2_ul").append(newLiStr);
							$("#isCFDA").prop("checked",false);
							$("#approve_range_select").val("4");
							$("#tdd_indication").val("");
						}
					}) */
				}
			},
			error: function(data){
				
			}
		})
	}
	// 处理临床试验药物列表的点击事件
	function handleTargetInfo2(index){
		clinicalIndex = index;
		var clinicalDrug = clinicalList[index-1];
		$("#clinical_trial_id").val(clinicalDrug.clinical_trial_id);
		$("#c3_tdd_drug_name").val(clinicalDrug.drug_name);
		$("#title_chinese").val(clinicalDrug.title);
		$("#condition_chinese").val(clinicalDrug.recruiting_condition);
		$("#phase").val(clinicalDrug.phase);
		$("#location_chinese").val(clinicalDrug.location);
		$("#inclusion_criteria").val(clinicalDrug.inclusion_criteria);
		$("#exclusion_criteria").val(clinicalDrug.exclusion_criteria);
	}
	
	// 清除报告预览页内容
	function clearPreviewContent() {
		$("tr[name=target-drug-tr]").remove();
		$("tr[name=unknown-mut-tr]").remove();
		$("#target-drug-analysis-tables").html("");
		$("#chemo-sideeffects-analysis-tables").html("");
		$("#chemo--effectiveness-analysis-tables").html("");
	}
	// 初始化靶向药物信息列表
	function initTargetDrugTable() {
		var table = $("#target-drug-table");
		var drugCount = 0;
		$.each(medicineList, function(k,v){
			if(v.drugList && v.drugList.length > 0) {
				var drugList = v.drugList;
				var gene_str = "<td>"+v.gene+"</td>";
				var mutation_str = "<td>"+removeMutations(v.ori_variant)+"</td>";
				var freq_str = "";
				if(mutation_str.indexOf("Amplification") < 0 && v.mutFreq != '.' && v.mutFreq.indexOf("H") < 0){
					freq_str = "<td>"+v.mutFreq+"%</td>";
				} else {
					freq_str = "<td>"+v.mutFreq+"</td>";
				}
				var druga_str = "";
				var drugb_str = "";
				var drugc_str = "";
				var drugd_str = "";
				var resistant_str = "";
				$.each(drugList,function(kk,vv){
					if(!vv.status || vv.status != 'delete'){
						var drug_name = vv.drug_name;
						if(vv.cfda == '1'){
							drug_name += "*";
						}
						if(vv.recruiting == '1'){
							drug_name += "#";
						}
						if(vv.approval_desc){
							drug_name = "<b>"+drug_name+"</b>";
						}
						if(vv.approve_range == '1'){
							druga_str += drug_name + '，';
						}
						if(vv.approve_range == '2'){
							drugb_str += drug_name + '，';
						}
						if(vv.approve_range == '3'){
							drugc_str += drug_name + '，';
						}
						if(vv.approve_range == '4'){
							drugd_str += drug_name + '，';
						}
						if(vv.approve_range == '5'){
							resistant_str += drug_name + '，';
						}
					}
				});
				druga_str = "<td>"+(druga_str.substr(0,druga_str.length-1))+"</td>";
				drugb_str = "<td>"+(drugb_str.substr(0,drugb_str.length-1))+"</td>";
				drugc_str = "<td>"+(drugc_str.substr(0,drugc_str.length-1))+"</td>";
				resistant_str = "<td>"+(resistant_str.substr(0,resistant_str.length-1))+"</td>";
				table.append("<tr name='target-drug-tr'>"+gene_str+mutation_str+freq_str+druga_str+drugb_str+drugc_str+resistant_str+"</tr>")
				drugCount++;
			}
		});
		if(drugCount==0){
			table.append("<tr name='target-drug-tr'><td>/</td><td>/</td><td>/</td><td>/</td><td>/</td><td>/</td><td>/</td></tr>")
		}
		
	}
	
	// 初始化肿瘤遗传风险检测表格
	function initGeneticCancerRisk() {
		$.each(crAllList, function(k,v){
			if(v.rpCr) {
				$("#genetic-cancer-risk-table tr").eq(k+1).find('td').eq(8).html(translateClinicalSignificance(v.rpCr.Clinical_significance));
			} else {
				$("#genetic-cancer-risk-table tr").eq(k+1).find('td').eq(8).html("-");
			}
		})
		
	}
	// 初始化靶向药物检测解析表格
	function initTargetDrugAnalysisTable() {
		var count = 0;
		$.each(medicineList, function(k,v){
			if((v.drugList && v.drugList.length > 0) || (v.clinicalList && v.clinicalList.length > 0)) {
				var drugList = v.drugList;
				var druga_str = "";
				var drugb_str = "";
				var drugc_str = "";
				var drugd_str = "";
				var resistant_str = "";
				
				var drugInfo_str = "";
				var clinicalInfo_str = "";
				var drug_Blod = [];
				$.each(drugList,function(kk,vv){
					if(!vv.status || vv.status != 'delete'){
						var drug_name = vv.drug_name;
						if(vv.cfda == '1'){
							drug_name += "*";
						}
						if(vv.approval_desc){
							drug_Blod.push(drug_name);
						}
						if(vv.recruiting == '1'){
							drug_name += "#";
						}
						if(vv.approve_range != '5' && vv.approval_desc){
							drugInfo_str += "<tr><td>" + drug_name + "</td>";
							drugInfo_str += "<td>" + vv.approval_desc + "</td></tr>";
						}
						if(vv.approval_desc){
							drug_name = "<b>"+drug_name+"</b>"; 
						}
						if(vv.approve_range == '1'){
							druga_str += drug_name + '，';
						}
						if(vv.approve_range == '2'){
							drugb_str += drug_name + '，';
						}
						if(vv.approve_range == '3'){
							drugc_str += drug_name + '，';
						}
						if(vv.approve_range == '4'){
							drugd_str += drug_name + '，';
						}
						if(vv.approve_range == '5'){
							resistant_str += drug_name + '，';
						}
					}
				});
				var template_str = $("#target-drug-analysis-gene-template").html();
				var mutation_str = v.ori_variant;
				var freq_str = '';
				if(mutation_str.indexOf("Amplification") < 0 && v.mutFreq != '.' && v.mutFreq.indexOf("H") < 0){
					freq_str = v.mutFreq+"%";
				} else {
					freq_str = v.mutFreq;
				}
				template_str = template_str.replace(">_gene<",">"+v.gene+"<");
				template_str = template_str.replace(">_mutation<",">"+removeMutations(v.ori_variant)+"<");
				template_str = template_str.replace(">_mutFreq<",">"+freq_str+"<");
				template_str = template_str.replace(">_drugsA<",">"+druga_str.substr(0,druga_str.length-1)+"<");
				template_str = template_str.replace(">_drugsB<",">"+drugb_str.substr(0,drugb_str.length-1)+"<");
				template_str = template_str.replace(">_drugsC<",">"+drugc_str.substr(0,drugc_str.length-1)+"<");
				template_str = template_str.replace(">_resistant<",">"+resistant_str.substr(0,resistant_str.length-1)+"<");
				var str = "";
				$.each($.parseJSON(v.varDrugNote),function(i,val){ 
					 var txt = val.value.trim();    
					 txt = txt.replace(/(^\s*)|(\s*$)/g, "");
					if(txt != null && txt != ""){
						if(val.key == "recommend:"){
							//str+="<b>"+txt+"</b>";
						}else{
							str+="<b>"+translateMedicationDescTitle(val.key)+"</b>"+txt+"<br/>"; 
						}
					}
				}); 
				if(v.clinicalList && v.clinicalList.length > 0){
					str+="<b>The clinical trials shown in the table beow are recommended.</b>";
				}
				template_str = template_str.replace(">_drugNote<",">"+(str||'')+"<");
				//template_str = template_str.replace("_druginfo",drugInfo_str);
				$("#target-drug-analysis-tables").append(template_str);
				if(drugInfo_str){
					$("table[name=target-drug-analysis-druginfo-table]").eq(count).append(drugInfo_str);
				} else {
					$("div[name=target-drug-analysis-druginfo-table-div]").eq(count).hide();
				}
				
				if(v.clinicalList && v.clinicalList.length > 0) {
					var clinicalList = v.clinicalList;
					$.each(clinicalList,function(kk,vv){
						if(!vv.status || vv.status != 'delete'){
							var drug_name = vv.drug_name;
							if(vv.cfda == '1'){
								drug_name += "*";
							}
							if(drug_Blod.indexOf(drug_name) != -1){
								drug_name = "<b>"+drug_name+"</b>";
							}
							clinicalInfo_str += "<tr><td>" + vv.clinical_trial_id + "</td>";
							clinicalInfo_str += "<td>" + vv.title + "</td>";
							clinicalInfo_str += "<td>" + vv.recruiting_condition + "</td>";
							clinicalInfo_str += "<td>" + vv.phase + "</td>";
							clinicalInfo_str += "<td>" + drug_name + "</td>";
							clinicalInfo_str += "<td>" + vv.location + "</td></tr>";
							/* clinicalInfo_str += "<td>" + vv.inclusion_criteria + "</td>";
							clinicalInfo_str += "<td>" + vv.exclusion_criteria + "</td>"; */
						}
					});
					$("table[name=target-drug-analysis-clinicalinfo-table]").eq(count).append(clinicalInfo_str);
				} else {
					$("div[name=target-drug-analysis-clinicalinfo-table-div]").eq(count).hide();
				}
				count++;
			}
		})
	}
	
	// 初始化化疗药物毒副作用解析和化疗药物有效性解析表格
	function initEffectivenessAndSideEffects() {
		var chemo_sideeffects_str = "";
		var chemo_effectiveness_str = "";
		$.each(chemo_sideeffects,function(k,v){
			chemo_sideeffects_str += "<tr>";
			var i = 0;
			var flag = false;
			$.each(v,function(kk,vv){
				i += 1;
				if(k == 0) {
					if(vv=='Evidence level'){
						chemo_sideeffects_str += "<th style='width: 5%;'>"+vv+"</th>";
					}  else if(vv=='Genotype'){
						chemo_sideeffects_str += "<th style='width: 8%;'>"+vv+"</th>";
					}else if(vv=='Category'){
						chemo_sideeffects_str += "<th colspan='2' style='width: 5%;'>Chemotherapy</th>";
					}else if(vv=='Chemotherapy'){
						
					} else {
						chemo_sideeffects_str += "<th>"+vv+"</th>";
					}
				} else {
					if(i==1 && vv =='-'){
						flag = true;
					}else if(i==2 && flag){
						chemo_sideeffects_str += '<td colspan="2">'+vv+'</td>';
					}else{
						chemo_sideeffects_str += "<td>"+vv+"</td>";
					}
				}
			});
			chemo_sideeffects_str += "</tr>";
		});
		$.each(chemo_effectiveness,function(k,v){
			chemo_effectiveness_str += "<tr>";
			var i = 0;
			var flag = false;
			$.each(v,function(kk,vv){
				i += 1;
				if(k == 0) {
					if(vv=='Evidence level'){
						chemo_effectiveness_str += "<th style='width: 5%;'>"+vv+"</th>";
					}  else if(vv=='Genotype'){
						chemo_effectiveness_str += "<th style='width: 8%;'>"+vv+"</th>";
					}else if(vv=='Category'){
						chemo_effectiveness_str += "<th colspan='2' style='width: 5%;'>Chemotherapy</th>";
					}else if(vv=='Chemotherapy'){
						
					} else {
						chemo_effectiveness_str += "<th>"+vv+"</th>";
					}
				} else {
					if(i==1 && vv =='-'){
						flag = true;
					}else if(i==2 && flag){
						chemo_effectiveness_str += '<td colspan="2">'+vv+'</td>';
					}else{
						chemo_effectiveness_str += "<td>"+vv+"</td>";
					}
				}
			});
			chemo_effectiveness_str += "</tr>";
		})
		$("#chemo-sideeffects-analysis-tables").append(chemo_sideeffects_str);
		$("#chemo-effectiveness-analysis-tables").append(chemo_effectiveness_str);
	}
	
	// 翻译临床意义
	function translateClinicalSignificance(Clinical_significance) {
		switch(Clinical_significance) {
			case 1: return '致病';
			case 2: return '可能致病';
			case 3: return '临床意义未明';
			case 4: return '可能良性';
			case 5: return '良性';
			default: return '-';
		}
	}
	// 翻译突变类型
	function translateMutType(ExonicFunc) {
		switch(ExonicFunc) {
			case "nonsynonymous SNV": return "错义突变";
			case "synonymous SNV": return "同义突变";
			case "nonframeshift insertion": return "非移码突变";
			case "nonframeshift deletion": return "非移码突变";
			case "frameshift deletion": return "移码突变";
			case "frameshift insertion": return "移码突变";
			case "frameshift indel": return "移码突变";
			case "nonframeshift indel": return "非移码突变";
			case "stopgain": return "无义突变";
			case "stoploss": return "stoploss";
			case "splicing": return "剪接突变";
			case "promoter": return "启动子区变异";
			case "unknown": return "未知";
			default: return ExonicFunc;
		}
	}
	function removeMutations(variant){
		if(variant.indexOf(" (") != -1){
			variant = variant.substr(0,variant.indexOf(" ("));
		}
		return variant;
	}
	function translateSampleType(sample_type) {
		switch(sample_type) {
			case "tissue": return "组织";
			case "blood": return "血液";
			default: return sample_type;
		}
	}
	// 翻译阶段
	function translatePhase(phase){
		switch(phase) {
			case "Phase IV": return "IV期";
			case "Phase III": return "III期";
			case "Phase II/III": return "II/III期";
			case "Phase II": return "II期";
			case "Phase I/II": return "I/II期";
			case "Phase I": return "I期";
			default: return "未知";
		}
	}
	// 纯合杂合显示
	function translateNum(num){
		switch(num) {
			case 1: return "杂合";
			case 2: return "纯合";
			case 3: return "纯合/杂合";
			default: return "未知";
		}
	}
	// 翻译用药说明标题
	function translateMedicationDescTitle(Str){
		switch(Str) {
			case "基因说明:": return "Gene description:";
			case "信号通路说明:": return "Description of signaling pathway:";
			case "位点说明:": return "Variant description:";
			case "NCCN指南:": return "Description of NCCN Guidelines";
			case "预后和诊断说明:": return "Description of prognostic diagnosis:";
			case "耐药说明:": return "Description of drug resistance:";
			case "用药说明:": return "Related biological and medical information:";
			default: return Str;
		}
	}
	//监听滚动条滚动
	window.onscroll=function(){
		var scrollTop=document.documentElement.scrollTop||document.body.scrollTop;//滚动条距离顶部的距离
		var select2 = document.getElementById('select2')//下拉框
		if(scrollTop>500){//如果滚动条距离顶部大于100
			select2.style.position='fixed';
			select2.style.top='500px';
		}else{
			select2.style.position='static';
		}
	}
	
</script>
</html>