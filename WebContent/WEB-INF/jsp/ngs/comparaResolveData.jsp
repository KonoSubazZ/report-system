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
<script src="${pageContext.request.contextPath}/js/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sweet-alert.css">
<script type="text/javascript">
	$(function(){
		$("#pageNo").keydown(function(event){
			if(event.keyCode==13){
				displayData(this.value-1);
			}
		}); 
	});
	
	function displayData(pageNo){
		currentPageNo = pageNo;
		var pageSize=10;
		if((!$("#dataGrid").val())){
			$("#message").text("请选择筛选条件");
		}else{
			$("#message").text("");
			$.ajax({
				url:"${pageContext.request.contextPath}/resolveData/getData",
				type:"post",
				cache:false, //设置浏览器不缓存页面  
				data:{
					"dataGrid":$("#dataGrid").val(),
					"condition":"1",
					"before_date":$("#before_date").val(),
					"after_date":$("#after_date").val(),
					"pageNo":pageNo+1,
					"pageSize":pageSize
				},
				beforeSend:function(){
					$("#message").text("正在查询，请稍等...");
					return true;
				},
				success:function(jsonObject){
					//清空内容
					$("#varDrugTable").empty();
				    if(jsonObject.total==0){
						$("#message").text("没有符合条件的数据");
						$("#exportFile").prop("disabled",true);
						$("#deleteBtn").attr("disabled",true);
						$("#deleteAllBtn").attr("disabled",true);
					}else{
						//显示导出按钮
						$("#exportFile").prop("disabled",false);
						$("#deleteBtn").attr("disabled",false)
						$("#deleteAllBtn").attr("disabled",false)
						$("#message").text("");
						var htmlString="";
						dataList = jsonObject.dataList;
						$.each(jsonObject.dataList,function(i,n){
							htmlString += '<tr class="odd">';
							if($("#dataGrid").val()=='rp_var_drug_en7') {
								htmlString += '<td><input type="checkbox" name="cb"></td>';
								htmlString += '<td>'+(n.lang||'')+'</td>';
								htmlString += '<td>'+(n.gene||'')+'</td>';
								htmlString += '<td>'+(n.variant||'')+'</td>';
								htmlString += '<td>'+(n.ori_variant||'')+'</td>';
								htmlString += '<td>'+(n.disease_id||'')+'</td>';
								htmlString += '<td>'+(n.gender||'')+'</td>';
								htmlString += '<td>'+(n.disease_name_chinese||'')+'</td>';
								htmlString += '<td>'+(n.var_drug_desc1||'')+'</td>';
								htmlString += '<td>'+(n.var_drug_desc||'')+'</td>';
								htmlString += '<td>'+(n.drugsA||'')+'</td>';
								htmlString += '<td>'+(n.drugsB||'')+'</td>';
								htmlString += '<td>'+(n.drugsC||'')+'</td>';
								htmlString += '<td>'+(n.drugsD||'')+'</td>';
								htmlString += '<td>'+(n.resistant_drugsA||'')+'</td>';
								htmlString += '<td>'+(n.resistant_drugsB||'')+'</td>';
								htmlString += '<td>'+(n.resistant_drugsC||'')+'</td>';
								htmlString += '<td>'+(n.resistant_drugsD||'')+'</td>';
								htmlString += '<td>'+(n.clinical_trial||'')+'</td>';
								htmlString += '<td>'+(n.modified)+'</td>';
								htmlString += '<td>'+(n.update_by||'')+'</td>';
								htmlString += '<td>'+(n.update_date||'')+'</td>';
								htmlString += '<td>'+(n.check_date||'')+'</td>';
							} else if($("#dataGrid").val()=='rp_unknown_var') {
								htmlString += '<td><input type="checkbox" name="cb"></td>';
								htmlString += '<td>'+(n.lang||'')+'</td>';
								htmlString += '<td>'+(n.gene||'')+'</td>';
								htmlString += '<td>'+(n.variant||'')+'</td>';
								htmlString += '<td>'+(n.ori_variant||'')+'</td>';
								htmlString += '<td>'+(n.disease_id||'')+'</td>';
								htmlString += '<td>'+(n.disease_name_chinese||'')+'</td>';
								htmlString += '<td>'+(n.result_type||'')+'</td>';
								htmlString += '<td>'+(n.gene_description1||'')+'</td>';
								htmlString += '<td>'+(n.gene_description||'')+'</td>';
								htmlString += '<td>'+(n.var_drug_desc1||'')+'</td>';
								htmlString += '<td>'+(n.var_drug_desc||'')+'</td>';
								htmlString += '<td>'+(n.modified)+'</td>';
								htmlString += '<td>'+(n.update_by||'')+'</td>';
								htmlString += '<td>'+(n.update_date||'')+'</td>';
								htmlString += '<td>'+(n.check_date||'')+'</td>';
							} else if($("#dataGrid").val()=='rp_drug_info') {//drug_id,drug_name_chinese,cfda,approval_desc_chinese
								htmlString += '<td><input type="checkbox" name="cb"></td>';
								htmlString += '<td>'+(n.lang||'')+'</td>';
								htmlString += '<td>'+(n.drug_id||'')+'</td>';
								htmlString += '<td>'+(n.disease_id||'')+'</td>';
								htmlString += '<td>'+(n.drug_name||'')+'</td>';
								htmlString += '<td>'+(n.cfda)+'</td>';
								htmlString += '<td>'+(n.approval_desc1||'')+'</td>';
								htmlString += '<td>'+(n.approval_desc||'')+'</td>';
								htmlString += '<td>'+(n.modified)+'</td>';
								htmlString += '<td>'+(n.update_by||'')+'</td>';
								htmlString += '<td>'+(n.update_date||'')+'</td>';
							} else if($("#dataGrid").val()=='rp_clinical_trial') {//clinical_trial_id,title_chinese,condition_chinese,phase,location_chinese
								htmlString += '<td><input type="checkbox" name="cb"></td>';
								htmlString += '<td>'+(n.lang||'')+'</td>';
								htmlString += '<td>'+(n.clinical_trial_id||'')+'</td>';
								htmlString += '<td>'+(n.title1||'')+'</td>';
								htmlString += '<td>'+(n.title||'')+'</td>';
								htmlString += '<td>'+(n.recruiting_condition||'')+'</td>';
								htmlString += '<td>'+(n.phase||'')+'</td>';
								htmlString += '<td>'+(n.location||'')+'</td>';
								htmlString += '<td>'+(n.modified)+'</td>';
								htmlString += '<td>'+(n.update_by||'')+'</td>';
								htmlString += '<td>'+(n.update_date||'')+'</td>';
							} else if($("#dataGrid").val()=='rp_cr') {//Gene,Mutation,ori_mutation,Zygosity,DiseaseID,disease_name_chinese,Clinical_significance,GeneDesc,VarClianno,has_drug
								htmlString += '<td><input type="checkbox" name="cb"></td>';
								htmlString += '<td>'+(n.lang||'')+'</td>';
								htmlString += '<td>'+(n.Gene||'')+'</td>';
								htmlString += '<td>'+(n.Mutation||'')+'</td>';
								htmlString += '<td>'+(n.ori_mutation||'')+'</td>';
								htmlString += '<td>'+(translateClinicalSignificance(n.Clinical_significance))+'</td>';
								htmlString += '<td>'+(n.GeneDesc||'')+'</td>';
								htmlString += '<td>'+(n.VarClianno||'')+'</td>';
								htmlString += '<td>'+(n.has_drug)+'</td>';
								htmlString += '<td>'+(n.updated_by||'')+'</td>';
								htmlString += '<td>'+(n.update_time||'')+'</td>';
								htmlString += '<td>'+(n.check_date||'')+'</td>';
							}
							htmlString += '</tr>';
							
						});
						//将上面拼接好的json字符串追加到tbody中
						$("#varDrugTable").append(htmlString);
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
    <div class="panel-head"><strong class="icon-reorder"> 解读数据管理</strong> </div>
      <ul class="search" style="padding-left:10px;margin-top: 3px;">
        <li>数据表格:</li>
        <li>
	        	<select id="dataGrid" name="dataGrid" class="input w50">
	        		<option value="">请选择数据库表</option>
	        		<option value="rp_var_drug_en7">rp_var_drug_en7</option>
	        		<option value="rp_unknown_var">rp_unknown_var</option>
	        		<option value="rp_drug_info">rp_drug_info</option>
	        		<option value="rp_clinical_trial">rp_clinical_trial</option>
	        		<option value="rp_cr">rp_cr</option>
	        	</select>
	        	<script>
	        		$("#dataGrid").change(function(){
	        			if($(this).val()){
	        				$.each($("tr[name=rp_tr]"),function(k,v){
		        				$(v).hide();
		        			})
		        			$("#"+$(this).val()+"_tr").show();
	        			}
	        			$("#varDrugTable").empty();
	        			$("#exportFile").prop("disabled",true);
					$("#deleteBtn").attr("disabled",true);
					$("#deleteAllBtn").attr("disabled",true);
	        		});
	        	</script>
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 更改日期:</li>
        <li>从</li>
        <li>
          <input type="text" placeholder="选择日期" id="before_date" name="before_date" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px; cursor: pointer; line-height:17px;display:inline-block" />
        </li>
        <li>到</li>
        <li>
          <input type="text" placeholder="选择日期" id="after_date" name="after_date" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,skin:'twoer'})" style="width:203px; cursor: pointer; line-height:17px;display:inline-block" />
        </li>
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 筛选条件:</li>
        
        <li style="padding-left: 0px; text-align: center;">
          <a href="javascript:void(0)" class="button border-main icon-search" onclick="displayData(0);" > 查看</a>
        </li>
        <li style="padding-left: 0px; text-align: center;">
          <a id="deleteBtn" href="javascript:void(0)" class="button border-red icon-remove" onclick="deleteData();" disabled="disabled"> 删除</a>
        </li>
        <li style="padding-left: 0px; text-align: center;">
          <button type="button"  class="button border-green" id="exportFile" disabled="disabled" onclick="exportExcel()"><span class="icon-download"></span> 导出</button>
       	 <!--  <span id="exportMessage" style="color: red;font-size: 14px; padding-left: 20px;"></span> -->
       	 <script type="text/javascript">
      		// 输出base64编码
		      const base64 = s => window.btoa(unescape(encodeURIComponent(s)));
       	 	function exportExcel(){
       	 	$.ajax({url:"${pageContext.request.contextPath}/resolveData/exportFile",
  				type:"post",
  				dataType:"json",
  				data:{
  					"dataGrid":$("#dataGrid").val(),
					"condition":"1",
					"before_date":$("#before_date").val(),
					"after_date":$("#after_date").val(),
  				},
  				beforeSend:function(){
					$("#message").text("正在导出数据...");
					return true;
				},
  				success:function(data){
  					if(data){
  						var str = ""
  						var keyStr = data.keyStr;
  						var exportFile = data.exportFile;
  						str+='<tr>';
  						for(let i = 0 ; i < keyStr.length ; i++ ){
  							str+="<td>"+ keyStr[i] +"\t</td>";
  				        }
  						str+='</tr>';
  						for(let i = 0 ; i < exportFile.length ; i++ ){
  				            str+='<tr>';
  				          	for(let j = 0 ; j < keyStr.length ; j++ ){
  				          		str+="<td>"+  exportFile[i][keyStr[j]] +"\t</td>"; 
    				        }
  				            str+='</tr>';
  				        }
  					// Worksheet名
  				        const worksheet = 'Sheet1'
  				        const uri = 'data:application/vnd.ms-excel;base64,';
  				 
  				        // 下载的表格模板数据
  				        const template = `<html xmlns:o="urn:schemas-microsoft-com:office:office" 
  				        xmlns:x="urn:schemas-microsoft-com:office:excel" 
  				        xmlns="http://www.w3.org/TR/REC-html40">
  				        <head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
  				        <x:Name>${worksheet}</x:Name>
  				        <x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
  				        </x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
  				        </head><body><table>`+str+`</table></body></html>`;
  				        // 下载模板
  				        window.location.href = uri + base64(template);
						 $("#message").text("导出成功！");
  					}else{
						 $("#message").text("导出失败！");
  					}
  				}
       	 		});
       	 	}
       	 </script>
        </li>
        <li style="padding-left: 0px; text-align: center;">
          <a id="deleteAllBtn" href="javascript:void(0)" class="button border-yellow icon-trash" onclick="deleteAllData();" disabled="disabled"> 清空</a>
        </li>
      </ul>
      <ul class="search" style="padding-left:900px;margin-top: 3px;">
      </ul>
          <span id="message" style="color: red;font-size: 14px; padding-left: 20px;width: 30%;" ></span>
    </div>
    <table id="dataable" class="table table-hover text-center" border=1 style=" border-width: 0px;">
      <tr id="rp_var_drug_en7_tr" style="display:none" name="rp_tr">
      	<th><div style="width:50px"><input type="checkbox" name="cb_selectAll">全选</div></th>
      	<th>lang</th>
        <th>gene</th>
        <th>variant</th>
        <th>ori_variant</th>
        <th>disease_id</th>
        <th>gender</th>
        <th>disease_name_chinese</th>
        <th>var_drug_desc标记</th>
        <th>var_drug_desc</th>
        <th>drugsA</th>
        <th>drugsB</th>
        <th>drugsC</th>
        <th>drugsD</th>
        <th>resistant_drugsA</th>
        <th>resistant_drugsB</th>
        <th>resistant_drugsC</th>
        <th>resistant_drugsD</th>
        <th>clinical_trial</th>
        <th>modified</th>
        <th>update_by</th>
        <th>update_date</th>
        <th>check_date</th>
      </tr>
      <tr id="rp_unknown_var_tr" style="display:none" name="rp_tr">
      	<th><div style="width:50px"><input type="checkbox" name="cb_selectAll">全选</div></th>
      	<th>lang</th>
        <th>gene</th>
        <th>variant</th>
        <th>ori_variant</th>
        <th>disease_id</th>
        <th>disease_name_chinese</th>
        <th>result_type</th>
        <th>gene_description标记</th>
        <th>gene_description</th>
        <th>var_drug_desc标记</th>
        <th>var_drug_desc</th>
        <th>modified</th>
        <th>update_by</th>
        <th>update_date</th>
        <th>check_date</th>
      </tr>
      <tr id="rp_drug_info_tr" style="display:none" name="rp_tr">
      	<th><div style="width:50px"><input type="checkbox" name="cb_selectAll">全选</div></th>
        <th>lang</th>
        <th>drug_id</th>
        <th>disease_id</th>
        <th>drug_name</th>
        <th>cfda</th>
        <th>approval_desc标记</th>
        <th>approval_desc</th>
        <th>modified</th>
        <th>update_by</th>
        <th>update_date</th>
      </tr>
      <tr id="rp_clinical_trial_tr" style="display:none" name="rp_tr">
      	<th><div style="width:50px"><input type="checkbox" name="cb_selectAll">全选</div></th>
      	<th>lang</th>
        <th>clinical_trial_id</th>
        <th>title标记</th>
        <th>title</th>
        <th>recruiting_condition</th>
        <th>phase</th>
        <th>location</th>
        <th>modified</th>
        <th>update_by</th>
        <th>update_date</th>
      </tr>
      <tr id="rp_cr_tr" style="display:none" name="rp_tr">
      	<th><div style="width:50px"><input type="checkbox" name="cb_selectAll">全选</div></th>
      	<th>lang</th>
        <th>Gene</th>
        <th>Mutation</th>
        <th>ori_mutation</th>
        <th>Clinical_significance</th>
        <th>GeneDesc</th>
        <th>VarClianno</th>
        <th>has_drug</th>
        <th>update_by</th>
        <th>update_time</th>
        <th>check_date</th>
      </tr>
      <tbody id="varDrugTable"></tbody>
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
    <script>
    		$("input[name=cb_selectAll]").click(function(){
			if($("input[name=cb_selectAll]").is(':checked')) {
				$.each($("input[name='cb']"), function(k,v){
					$(v).prop("checked",true)
				})
			} else {
				$.each($("input[name='cb']"), function(k,v){
					$(v).prop("checked",false)
				})
			}
    		})
    </script>
</form>
<script>
	var dataList = [];
	var currentPageNo = 0;
	//翻译临床意义
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
	function deleteData(){
		var deleteList = [];
		$.each($("input[name=cb]"),function(k,v){
			if($(v).is(':checked')) {
				deleteList.push(dataList[k]);
			}
		});
		$.ajax({
			type: "post",
			url: "${pageContext.request.contextPath}/resolveData/deleteRecord?dataGrid="+$("#dataGrid").val(),
			data: JSON.stringify(deleteList),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				displayData(currentPageNo);
			},
			error: function(data){
				
			}
		});
	}
	function deleteAllData(){
		swal({ 
			  title: "确定清空？", 
			  text: "该操作将删除"+$("#dataGrid").val()+"表的所有数据", 
			  type: "warning",
			  showCancelButton: true, 
			  confirmButtonColor: "#DD6B55",
			  confirmButtonText: "确定清空", 
			  cancelButtonText: "取消", 
			  closeOnConfirm: true
			},
			function(yes){
				if(yes){
					$.ajax({
						type: "post",
						url: "${pageContext.request.contextPath}/resolveData/deleteAllRecord?dataGrid="+$("#dataGrid").val(),
						contentType: "application/json; charset=utf-8",
						dataType: "json",
						success: function (data) {
							displayData(0);
						},
						error: function(data){
							
						}
					});
				}
			});
	}
</script>
</body>
</html>