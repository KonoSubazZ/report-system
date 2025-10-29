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
	});
	
	function displayData(pageNo){
		var pageSize=10;
		var checkedElts = $(":checkbox[name='isPass']:checked");
		var isPass="";
		if(checkedElts.length>0){
			for(var i=0; i<checkedElts.length; i++){
				if(i == checkedElts.length-1){
					isPass += checkedElts[i].value;
				}else{
					isPass += checkedElts[i].value + ",";
				}
			}
			
		}
		//alert(isPass);
		$.ajax({
			url:"${pageContext.request.contextPath}/filterFusion/getIlluminaFusionByPage",
			type:"post",
			cache:false, //设置浏览器不缓存页面  
			data:{
				"pageNo":pageNo+1,
				"pageSize":pageSize,
				"platform":$("#platform").val(),
				"analysis_date":$("#analysis_date").val(),
				"subbarcode":$("#subbarcode").val(),
				"product_name":$("#product_name").val(), 
				"isPass":isPass,
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
					$.each(jsonObject.dataList,function(i,n){
					    htmlString += '<tr class="odd">';
						htmlString += '<td>'+n.file_id+'</td>';
						htmlString += '<td>'+n.chromosome1+'</td>';
						// htmlString += '<td>'+n.softclip1+'</td>';
                        if (n.fusion_quality.indexOf("RNA") != -1) {
                            htmlString += '<td><a name="mya" href="http://127.0.0.1:8086/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'R.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip1+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://10.168.4.236/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'R.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip1+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://192.168.200.82/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'R.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip1+'</a></td>';
                        } else {
                            htmlString += '<td><a name="mya" href="http://127.0.0.1:8086/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'T.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip1+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://10.168.4.236/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'T.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip1+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://192.168.200.82/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'T.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip1+'</a></td>';
                        }
                        htmlString += '<td>'+n.sclip1_info+'</td>';
						htmlString += '<td>'+n.chromosome2+'</td>';
						// htmlString += '<td>'+n.softclip2+'</td>';
                        if (n.fusion_quality.indexOf("RNA") != -1) {
                            htmlString += '<td><a name="mya" href="http://127.0.0.1:8086/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'R.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip2+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://10.168.4.236/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'R.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip2+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://192.168.200.82/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'R.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip2+'</a></td>';
                        } else {
                            htmlString += '<td><a name="mya" href="http://127.0.0.1:8086/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'T.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip2+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://10.168.4.236/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'T.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip2+'</a></td>';
                            // htmlString += '<td><a name="mya" href="http://192.168.200.82/index.php/Home/Index/ldtigvs1/bam/'+$("#subbarcode").val()+'T.bam/chr/'+n.chromosome1.replace("chr","")+'/start/'+n.softclip1+'/chr1/'+n.chromosome2.replace("chr","")+'/start1/'+n.softclip2+'" target="_blank">'+n.softclip2+'</a></td>';
                        }
                        htmlString += '<td>'+n.sclip2_info+'</td>';
						htmlString += '<td>'+n.cosmic_info+'</td>';
						htmlString += '<td>'+n.db_info+'</td>';
						htmlString += '<td>'+n.fusion_quality+'</td>';
						htmlString += '<td>'+n.sup_reads_hq+'</td>';
						htmlString += '<td>'+n.sup_reads_uniq+'</td>';
						htmlString += '<td>'+n.depth+'</td>';
						htmlString += '<td>'+n.freq+'</td>';
						htmlString += '<td>'+n.gene1+'</td>';
						htmlString += '<td>'+n.bp1+'</td>';
						htmlString += '<td>'+n.gene2+'</td>';
						htmlString += '<td>'+n.bp2+'</td>';
						htmlString += '<td>'+(n.variant == null? "" : n.variant) +'</td>';

                        if(n.report == 0){
                            htmlString += '<td  id="report'+i+'"><select id="sel'+i+'" name="check" onchange="updateReport('+n.record_id+','+"'"+i+"'"+')" ><option selected="selected" value="0">不报出</option><option value="1">报出</option></select></td>';
                        } else {
                            htmlString += '<td  id="report'+i+'"><select id="sel'+i+'" name="check" onchange="updateReport('+n.record_id+','+"'"+i+"'"+')" ><option value="0">不报出</option><option selected="selected" value="1">报出</option></select></td>';
                        }
						if(n.filtered_rationale==null){
							htmlString += '<td><input id="inp'+i+'" type="text" onblur="updateFiltered('+n.record_id+','+i+')" onfocus="cleanMessage2()" value=""/></td>';
						}else{
							htmlString += '<td><input id="inp'+i+'" type="text" onblur="updateFiltered('+n.record_id+','+i+')" onfocus="cleanMessage2()" value="'+n.filtered_rationale+'"/></td>';
						}
						htmlString += '<td>'+(n.mapped_variant_id == null? "" : n.mapped_variant_id)+'</td>';
						htmlString += '<td>'+(n.mapped_variant == null? "" : n.mapped_variant)+'</td>';
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
	
	function updateReport(record_id,id){
		
		var report = "report"+id;
		var sel = "sel"+id;
	    var slectReprot = document.getElementById(sel).value;
		$.ajax({
			url:"${pageContext.request.contextPath}/filterFusion/updateReport",
			type:"POST",
			data:{"report":slectReprot,"record_id":record_id,"platform":$("#platform").val()},
			dataType:"json",
			success:function(result){
				if(result){
					$("#message2").text("report修改成功！");
				}else{
					$("#message2").text("report修改失败！");
				}
			}
		}); 
	}
	
	function cleanMessage2(){
		$("#message2").text("");
	}
	
	function updateFiltered(record_id,id){
		var inp = "inp"+id;
		var inpContent=$("#"+inp).val();
		var inpVal="";
		if(inpContent != "null"){
			inpVal=inpContent;
		}
		$.ajax({
			url:"${pageContext.request.contextPath}/filterFusion/updateFiltered",
			type:"POST",
			data:{"record_id":record_id,"filtered_rationale":inpVal,"platform":$("#platform").val()},
			dataType:"json",
			success:function(result){
				if(result){
					$("#message2").text("filtered_rationale修改成功！");
				}else{
					$("#message2").text("filtered_rationale修改失败！");
				}
			}
		}); 
		
	}

    //report批量选择
    function selects() {
        var report = $("#report").val();
        $("[name='check']").val(report).change();
    }
</script>

</head>
<body>
<input type="hidden" id="platform" value="${currentNgsAvailable.platform }">
<input type="hidden" id="analysis_date" value="${currentNgsAvailable.analysis_date }">
<input type="hidden" id="subbarcode" value="${currentNgsAvailable.subbarcode }">
<input type="hidden" id="product_name" value="${currentNgsAvailable.product_name }">
<form method="post" action="" id="listform">
  <div class="panel admin-panel" style="width:2343px">
    <div class="padding border-bottom">
      <ul class="search" style="padding-left:10px;">
        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;筛选条件:</li>
        <li>
		  <input onchange="displayData(0);" name="isPass" type="checkbox" value="pass" />通过&nbsp;
	      <input onchange="displayData(0);" name="isPass" type="checkbox" value="passNo" />不通过&nbsp;
	      <input onchange="displayData(0);" name="isPass" type="checkbox" value="mateNo" />无匹配&nbsp;
	      <input onchange="displayData(0);" name="isPass" type="checkbox" value="mate" />匹配
        </li>
        <li>report</li>
        <li>
            <select id="report" onchange="selects();"><option value="0">不报出</option><option selected="selected" value="1">报出</option></select>
        </li>
        <li style="padding-left:800px;float: left;color: red;font-size: 14px"><span id="message2"></span></li>      
      </ul>
    </div>
    <table class="table table-hover text-center">
      <tr>
        <th>file_id</th>
        <th>chromosome1</th>
        <th>softclip1</th>
        <th>sclip1_info</th>
        <th>chromosome2</th>
        <th>softclip2</th>
        <th>sclip2_info</th>
        <th>cosmic_info</th>
        <th>db_info</th>
        <th>fusion_quality</th>
        <th>sup_reads_hq</th>
        <th>sup_reads_uniq</th>
        <th>depth</th>
        <th>freq</th>
        <th>gene1</th>
        <th>bp1</th>
        <th>gene2</th>
        <th>bp2</th>
        <th>variant</th>
        <th>report</th>
        <th>filtered_rationale</th>
        <th>mapped_variant_id</th>
        <th>mapped_varaint</th>
      </tr>
      <tr>
      <tbody id="tInfo2">
	  </tbody>
      <tr>
     	<td colspan="22">
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