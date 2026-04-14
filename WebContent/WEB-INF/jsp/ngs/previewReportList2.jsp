<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="renderer" content="webkit">
    <base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/textContext.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/alertBox.css">
    <style>
        /* 数据库弹窗样式 */
        .db-modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.6);
            z-index: 9999;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .db-box {
            background: #fff;
            width: 700px;
            max-height: 80vh;
            overflow-y: auto;
            padding: 25px;
            border-radius: 10px;
            position: relative;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
        }
        .db-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 20px;
            color: #333;
            text-align: center;
        }
        .db-item {
            display: flex;
            border-bottom: 1px solid #eee;
            padding: 10px 0;
        }
        .db-key {
            width: 180px;
            font-weight: bold;
            color: #222;
            padding-right: 10px;
        }
        .db-val {
            flex: 1;
            color: #333;
            word-break: break-all;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            displayData(0);
            $("#pageNo").keydown(function (event) {
                if (event.keyCode == 13) {
                    displayData(this.value - 1);
                }
            });

            // 数据库标签点击弹窗事件
            $(document).on('click', '.db-tag', function () {
                var info = $(this).find("span:hidden").first().text().trim();
                if(!info){
                    alert("暂无数据库信息");
                    return;
                }
                var rawFinalCheck = $(this).attr("data-final");
                var html = '<div class="db-item"><div class="db-key">生信结果</div><div class="db-val">' + rawFinalCheck + '</div></div>';
                info.split(";").forEach(function(item){
                    var kv = item.split(":",2);
                    var key = kv[0] || "";
                    var val = kv[1] || "";
                    html += '<div class="db-item"><div class="db-key">'+ key +'</div><div class="db-val">'+ val +'</div></div>';
                });
                $("body").append(
                    '<div class="db-modal">' +
                    '    <div class="db-box">' +
                    '        <div style="position:absolute;right:20px;top:15px;font-size:24px;cursor:pointer" onclick="$(this).closest(\'.db-modal\').remove()">×</div>' +
                    '        <div class="db-title">基因数据库详情</div>' +
                    html +
                    '    </div>' +
                    '</div>'
                );
            });
            $(document).on('click','.db-modal',function(e){
                if($(e.target).hasClass('db-modal')) $(this).remove();
            });
        });

        function displayData(pageNo) {
            var pageSize = 10;
            var checkedElts = $(":checkbox[name='isPass']:checked");
            var isPass = "";
            if (checkedElts.length > 0) {
                for (var i = 0; i < checkedElts.length; i++) {
                    if (i == checkedElts.length - 1) {
                        isPass += checkedElts[i].value;
                    } else {
                        isPass += checkedElts[i].value + ",";
                    }
                }

            }

            //alert(isPass);
            $.ajax({
                url: "${pageContext.request.contextPath}/filterSnpIndel/getIlluminaSnpIndelByPage",
                type: "post",
                cache: false, //设置浏览器不缓存页面
                data: {
                    "pageNo": pageNo + 1,
                    "pageSize": pageSize,
                    "platform": $("#platform").val(),
                    "analysis_date": $("#analysis_date").val(),
                    "subbarcode": $("#subbarcode").val(),
                    "product_name": $("#product_name").val(),
                    "report_id": "${currentNgsAvailable.report_id}",
                    "Gene_knownGene": $("#Gene_knownGene").val(),
                    "isPass": isPass,
                    /* "platform":"Illumina",
                    "analysis_date":"20170213",
                    "subbarcode":"201602388500",
                    "product_name":"pms", */

                },
                beforeSend: function () {
                    $("#message").text("正在处理请稍等...");
                    return true;
                },
                success: function (jsonObject) {

                    //清空内容
                    $("#tInfo2").empty();
                    if (jsonObject.total == 0) {
                        $("#message").text("没数据");
                    } else {
                        $("#message").text("");
                        var htmlString = "";
                        var report_id_N = 0;
                        $.each(jsonObject.dataList, function (i, n) {
                            htmlString += '<tr class="odd">';
                            // htmlString += '<td><a name="mya" href="http://localhost:60151/load?file=http://172.30.0.5:3070/data/bam_link/1699897100_TKHS230055788-1AT.final.bam.xml&amp;locus=11:63672368-63672368&amp;merge=false">11:63672368</a></td>';
                            htmlString += '<td>' + n.file_id + '</td>';
                            htmlString += '<td>' + n.chr + '</td>';
                            htmlString += '<td>' + n.start + '</td>';
                            htmlString += '<td>' + n.end + '</td>';
                            htmlString += '<td>' + n.ref + '</td>';
                            htmlString += '<td>' + n.alt + '</td>';
                            htmlString += '<td>' + n.gene_knownGene + '</td>';
                            if (n.ori_variant != null && n.ori_variant != "") {
                                htmlString += '<td>' + n.ori_variant + '</td>';
                            } else {
                                htmlString += '<td>' + "" + '</td>';
                            }
                            htmlString += '<td>' + n.mutDepth + '</td>';
                            htmlString += '<td>' + n.totalDepth + '</td>';
                            htmlString += '<td>' + n.mutFreq + '</td>';
                            htmlString += '<td>' + n.e1000g2012apr_all + '</td>';
                            htmlString += '<td>' + (n.variant == null ? "" : n.gene_knownGene + " " + n.variant) + '</td>';

                            if (n.report == 0) {
                                htmlString += '<td  id="report' + i + '"><select id="sel' + i + '" name="check" onchange="updateReport(' + n.record_id + ',' + "'" + i + "'" + ')" ><option selected="selected" value="0">不出</option><option value="1">出</option><option value="2">default</option></select></td>';
                            }
                            if (n.report == 1) {
                                htmlString += '<td  id="report' + i + '"><select id="sel' + i + '" name="check" onchange="updateReport(' + n.record_id + ',' + "'" + i + "'" + ')" ><option value="0">不出</option><option selected="selected" value="1">出</option><option value="2">default</option></select></td>';
                            }
                            if (n.report == null || n.report == ".") {
                                htmlString += '<td  id="report' + i + '"><select id="sel' + i + '" name="check" onchange="updateReport(' + n.record_id + ',' + "'" + i + "'" + ')" ><option value="0">不出</option><option value="1">出</option><option selected="selected" value="2">default</option></select></td>';
                            }
                            if (n.filtered_rationale == null || n.filtered_rationale == "null") {
                                htmlString += '<td><input id="inp' + i + '" type="text" onblur="updateFiltered(' + n.record_id + ',' + i + ')" onfocus="cleanMessage2()" value=""/></td>';
                            } else {
                                htmlString += '<td><input id="inp' + i + '" type="text" onblur="updateFiltered(' + n.record_id + ',' + i + ')" onfocus="cleanMessage2()" value="' + n.filtered_rationale + '"/></td>';
                            }
                            htmlString += '<td><span style="cursor:pointer" onclick="openTextRead(' + "'" + "cosmic65" + i + "'" + ');">';
                            if (n.cosmic65 != null) {
                                if ((n.cosmic65).length > 24) {
                                    var str = (n.cosmic65).substring(0, 24) + "....";
                                    htmlString += str;
                                } else {
                                    htmlString += n.cosmic65;
                                }
                            }
                            htmlString += '</span> <div id="cosmic65' + i + '" style="display:none;">' + n.cosmic65 + '</div></td>';
                            htmlString += '<td>' + (n.mapped_variant_id == null ? "" : n.mapped_variant_id) + '</td>';
                            htmlString += '<td>' + (n.mapped_variant == null ? "" : n.mapped_variant) + '</td>';
                            htmlString += '<td>' + n.hom_het + '</td>';
                            htmlString += '<td>' + n.func_knownGene + '</td>';
                            htmlString += '<td>' + n.exonicFunc_knownGene + '</td>';
                            htmlString += '<td><span style="cursor:pointer" onclick="openTextRead(' + "'" + "aachange_knownGene" + i + "'" + ');">';
                            if (n.aachange_knownGene != null) {
                                if ((n.aachange_knownGene).length > 24) {
                                    var str = (n.aachange_knownGene).substring(0, 24) + "....";
                                    htmlString += str;
                                } else {
                                    htmlString += n.aachange_knownGene;
                                }
                            }
                            htmlString += '</span> <div id="aachange_knownGene' + i + '" style="display:none;">' + n.aachange_knownGene + '</div></td>';
                            htmlString += '<td><span style="cursor:pointer" onclick="openTextRead(' + "'" + "esp6500si_all" + i + "'" + ');">';
                            if (n.esp6500si_all != null) {
                                if ((n.esp6500si_all).length > 24) {
                                    var str = (n.esp6500si_all).substring(0, 24) + "....";
                                    htmlString += str;
                                } else {
                                    htmlString += n.esp6500si_all;
                                }
                            }
                            htmlString += '</span> <div id="esp6500si_all' + i + '" style="display:none;">' + n.esp6500si_all + '</div></td>';
                            htmlString += '<td>' + n.dbSNP_rs + '</td>';
                            htmlString += '<td>' + n.interpro_domain + '</td>';

                            // ========== 新增：Final_Check 标签列 ==========
                            var fc = n.Final_Check;
                            var text = fc;
                            var bg = "transparent";
                            var color = "#333";
                            if(fc === "P" || fc === "Conflict" || fc === "Conflict-C" || fc === "Conflict-P" || fc === "Conflict-HP"){
                                text = "解读";
                                bg = "#dc3545";
                                color = "white";
                            }else if(fc === "B" || fc === "Conflict-B"){
                                text = "良性";
                                bg = "#28a745";
                                color = "white";
                            }else if(fc === "VUS" || fc === "Conflict-VUS"){
                                text = "VUS";
                                bg = "#ffc107";
                                color = "#222";
                            }
                            htmlString += '<td>';
                            htmlString += '<span class="db-tag" style="background:'+bg+';color:'+color+';padding:4px 10px;border-radius:6px;font-weight:bold;cursor:pointer;display:inline-block;min-width:56px;" data-info="'+ (n.Database_Info||'') +'" data-final="'+ (fc||'') +'">';
                            htmlString += text;
                            htmlString += '<span style="display:none;">'+ (n.Database_Info||'') +'</span>';
                            htmlString += '</span>';
                            htmlString += '</td>';
                            // ========== 新增结束 ==========

                            htmlString += '</tr>';
                        });
                        //将上面拼接好的json字符串追加到tbody中
                        $("#tInfo2").append(htmlString);
                    }

                    //集成jquery的翻页插件
                    $("#pagination").pagination(jsonObject.total, {//总记录条数
                        callback: displayData,//每次翻页的时候执行的回调函数  会自动传递当前页码索引   比正常页码小1
                        items_per_page: pageSize, // 每页显示多少条数据
                        current_page: pageNo,//当前页码索引
                        link_to: "javascript:void(0)",//保留超链接的样式，执行js代码   不跳转到任何资源
                        num_display_entries: 5,//默认显示页码入口的个数
                        next_text: "下一页",
                        prev_text: "上一页",
                        next_show_always: true,//如果没有下一页是否显示连接
                        prev_show_always: true,//如果没有上一页是否显示连接
                        num_edge_entries: 2,//页码较多的时候 可以用...省略
                        ellipse_text: "..."
                    });
                    //获取总记录条数
                    $("#total").text(jsonObject.total);
                    //显示总页数
                    var pageCount = jsonObject.total % pageSize == 0 ? jsonObject.total / pageSize : parseInt(jsonObject.total / pageSize) + 1;
                    $("#pageCount").text(pageCount);
                }
            });
        }

        function updateReport(record_id, id) {
            var report = "report" + id;
            var sel = "sel" + id;
            var slectReprot = document.getElementById(sel).value;
            $.ajax({
                url: "${pageContext.request.contextPath}/filterSnpIndel/updateReport",
                type: "POST",
                data: {"report": slectReprot, "record_id": record_id, "platform": $("#platform").val()},
                dataType: "json",
                success: function (result) {
                    if (result) {
                        $("#message2").text("report修改成功！");
                    } else {
                        $("#message2").text("report修改失败！");
                    }
                }
            });
        }

        function cleanMessage2() {
            $("#message2").text("");
        }

        function updateFiltered(record_id, id) {
            var inp = "inp" + id;
            var inpContent = $("#" + inp).val();
            var inpVal = "";
            if (inpContent != "null") {
                inpVal = inpContent;
            }
            $.ajax({
                url: "${pageContext.request.contextPath}/filterSnpIndel/updateFiltered",
                type: "POST",
                data: {"record_id": record_id, "filtered_rationale": inpVal, "platform": $("#platform").val()},
                dataType: "json",
                success: function (result) {
                    if (result) {
                        $("#message2").text("filtered_rationale修改成功！");
                    } else {
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
<body style="width:4000px;">
<input type="hidden" id="platform" value="${currentNgsAvailable.platform }">
<input type="hidden" id="analysis_date" value="${currentNgsAvailable.analysis_date }">
<input type="hidden" id="subbarcode" value="${currentNgsAvailable.subbarcode }">
<input type="hidden" id="product_name" value="${currentNgsAvailable.product_name }">
<form method="post" action="" id="listform">
    <div class="panel admin-panel" id="alertBox">
        <div class="padding border-bottom">
            <ul class="search" style="padding-left:10px;">
                <%-- <li> <a class="button border-blue icon-plus-square-o" href="${pageContext.request.contextPath}/PCR/addPcrReport"> 添加报告</a>&nbsp;&nbsp;&nbsp;&nbsp;</li> --%>
                <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;筛选条件:</li>
                <li>
                    <input onchange="displayData(0);" name="isPass" type="checkbox" value="pass"/>通过&nbsp;
                    <input onchange="displayData(0);" name="isPass" type="checkbox" value="passNo"/>不通过&nbsp;
                    <input onchange="displayData(0);" name="isPass" type="checkbox" value="mateNo"/>无匹配&nbsp;
                    <input onchange="displayData(0);" name="isPass" type="checkbox" value="mate"/>匹配
                </li>
                <li>Gene_knownGene</li>
                <li>
                    <input type="text" placeholder="请输入搜索关键字" id="Gene_knownGene" name="Gene_knownGene" value=""
                           class="input" style="width:250px; line-height:17px;display:inline-block"/>
                </li>
                <li>report</li>
                <li>
                    <select id="report" onchange="selects();">
                        <option value="0">不出</option>
                        <option value="1">出</option>
                        <option selected="selected" value="2">default</option>
                    </select>
                </li>
                <li style="padding-left:2830px;float: left;color: red;font-size: 14px"><span id="message2"></span></li>
            </ul>

        </div>
        <script type="text/javascript">
            $(function () {
                $.post("${pageContext.request.contextPath}/filterSnpIndel/getIlluminaGene_knownGene",
                    function (data) {
                        $('#Gene_knownGene').autocomplete(data, {
                            max: 12, //列表里的条目数
                            minChars: 0, //自动完成激活之前填入的最小字符
                            width: 250, //提示的宽度，溢出隐藏
                            scrollHeight: 300, //提示的高度，溢出显示滚动条
                            matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                            autoFill: false, //自动填充
                            formatItem: function (row, i, max) {
                                return "" + row;
                            }
                        }).result(function (event, row, formatted) {
                            displayData(0);
                        });

                    }, "json");
            });
        </script>
        <table class="table table-hover text-center">
            <tr>
                <%--<th>IGV</th>--%>
                <th>file_id</th>
                <th>chr</th>
                <th>start</th>
                <th>end</th>
                <th>ref</th>
                <th>alt</th>
                <th>Gene_knownGene</th>
                <th>ori_variant</th>
                <th>预测结果</th>
                <th>mutDepth</th>
                <th>totalDepth</th>
                <th>mutFreq</th>
                <th>1000g2012apr_all</th>
                <th>variant</th>
                <th>report</th>
                <th>filtered_rationale</th>
                <th>cosmic65</th>
                <th>mapped_variant_id</th>
                <th>mapped_variant</th>
                <th>hom_het</th>
                <th>Func_knownGene</th>
                <th>ExonicFunc_knownGene</th>
                <th>AAChange_knownGene</th>
                <th>esp6500si_all</th>
                <th>dbSNP_rs</th>
                <th>Interpro_domain</th>
                <th>Final_Check</th> <!-- 新增表头 -->
            </tr>
            <tr>
                <tbody id="tInfo2">
                </tbody>
            <tr>
                <td colspan="24"> <!-- 修改为24列 -->
                    <table width="100%" height="30" border="0" cellpadding="0" cellspacing="0" class="page_table">
                        <tr>
                            <td width="8%" class="font_left">数据:<span id="total"></span>条</td>
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