<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@taglib uri="http://java.sun.com/jsp/jstl/fmt"
                                          prefix="fmt" %> <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"
    />
    <meta name="renderer" content="webkit" />
    <base
            href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"
    />
    <title></title>
    <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/jquery.autocomplete.css"
    />
    <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/pintuer.css"
    />
    <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/admin.css"
    />
    <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/jquery/pagination/pagination.css"
    />
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"
    ></script>
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/textContext.js"
    ></script>
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"
    ></script>
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/jquery.form.js"
    ></script>
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"
    ></script>
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"
    ></script>
    <script
            type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"
    ></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/css/alertBox.css"
    />
    <script type="text/javascript">
        $(function () {
            displayData(0);
            $("#pageNo").keydown(function (event) {
                if (event.keyCode == 13) {
                    displayData(this.value - 1);
                }
            });
        });

        var crClinicalData = [];

        function loadCRClinicalData(reportId) {
            $.ajax({
                url: "${pageContext.request.contextPath}/filterCr/get_CR_clinical_significance_info",
                type: "get",
                data: { reportId: reportId },
                async: false,
                success: function (res) {
                    crClinicalData = res;
                },
            });
        }

        // 获取临床意义 + 颜色样式
        function getClinicalSignificanceHtml(n) {
            let clinical = "";
            if (crClinicalData ===[]){
                return;
            }
            for (let i = 0; i < crClinicalData.length; i++) {
                var item = crClinicalData[i];
                if (
                    n.gene == item.gene &&
                    n.transcript == item.Transcript &&
                    n.cHGVS == item.cHGVS &&
                    n.pHGVS == item.pHGVS &&
                    n.zygosity == item.Zygosity
                ) {
                    clinical = item.Clinical_significance || "";
                    break;
                }
            }

            // 增加B/LB标签
            if (!clinical) {
                clinical = "可能良性/良性变异";
            }

            // 颜色逻辑
            var bgColor = "";
            if (
                clinical === "致病性变异" ||
                clinical === "可能致病性变异" ||
                clinical === "不确定性变异"
            ) {
                bgColor = "#dc3545"; // 红
            } else if (clinical === "可能良性/良性变异") {
                bgColor = "#28a745"; // 绿
            }

            if (bgColor) {
                return (
                    '<span style="background-color:' +
                    bgColor +
                    ';color:#fff;padding:3px 8px;border-radius:4px;font-size:12px;">' +
                    clinical +
                    "</span>"
                );
            }

            return clinical;
        }

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

            loadCRClinicalData("${currentNgsAvailable.report_id}");

            $.ajax({
                url: "${pageContext.request.contextPath}/filterCr/getIlluminaCrByPage",
                type: "post",
                cache: false,
                data: {
                    pageNo: pageNo + 1,
                    pageSize: pageSize,
                    platform: $("#platform").val(),
                    analysis_date: $("#analysis_date").val(),
                    subbarcode: $("#subbarcode").val(),
                    product_name: $("#product_name").val(),
                    report_id: "${currentNgsAvailable.report_id}",
                    isPass: isPass,
                },
                beforeSend: function () {
                    $("#message").text("正在处理请稍等...");
                    return true;
                },
                success: function (jsonObject) {
                    $("#tInfo2").empty();
                    if (jsonObject.total == 0) {
                        $("#message").text("没数据");
                    } else {
                        $("#message").text("");
                        var htmlString = "";
                        $.each(jsonObject.dataList, function (i, n) {
                            // console.log(n)
                            // 带颜色的临床意义
                            var clinicalHtml = getClinicalSignificanceHtml(n);

                            htmlString += '<tr class="odd">';
                            htmlString += "<td>" + n.file_id + "</td>";
                            htmlString += "<td>" + n.chr + "</td>";
                            htmlString += "<td>" + n.pos + "</td>";
                            htmlString += "<td>" + n.gene + "</td>";
                            htmlString += "<td>" + n.transcript + "</td>";
                            htmlString += "<td>" + n.exon + "</td>";
                            htmlString += "<td>" + n.cHGVS + "</td>";
                            htmlString += "<td>" + n.pHGVS + "</td>";
                            htmlString += "<td>" + n.zygosity + "</td>";
                            // 临床意义（带颜色标签）
                            htmlString += "<td>" + clinicalHtml + "</td>";
                            htmlString += "<td>" + n.exonicFunc + "</td>";
                            htmlString += "<td>" + n.c1000g2015aug_all + "</td>";
                            htmlString += "<td>" + n.exAC_EAS + "</td>";
                            htmlString += "<td>" + n.avsnp150 + "</td>";
                            htmlString += "<td>" + n.sift_pred + "</td>";
                            htmlString += "<td>" + n.polyphen2_HDIV_pred + "</td>";
                            htmlString += "<td>" + n.mutationTaster_pred + "</td>";
                            htmlString += "<td>" + n.revel + "</td>";
                            htmlString += "<td>" + n.gnomAD_genome_ALL + "</td>";
                            htmlString += "<td>" + n.interpro_domain + "</td>";
                            htmlString += "<td>" + n.clnsig + "</td>";
                            htmlString += "<td>" + n.omim_Phenotypes + "</td>";
                            htmlString += "<td>" + n.hgmd_tag + "</td>";
                            htmlString += "<td>" + n.hgmd_disease + "</td>";
                            htmlString += "<td>" + n.hgmd_pmid + "</td>";
                            if (n.report == 0) {
                                htmlString +=
                                    '<td id="report' +
                                    i +
                                    '"><select id="sel' +
                                    i +
                                    '" name="check" onchange="updateReport(' +
                                    n.record_id +
                                    ",'" +
                                    i +
                                    '\')"><option selected="selected" value="0">不出</option><option value="1">出</option><option value="2">default</option></select></td>';
                            }
                            if (n.report == 1) {
                                htmlString +=
                                    '<td id="report' +
                                    i +
                                    '"><select id="sel' +
                                    i +
                                    '" name="check" onchange="updateReport(' +
                                    n.record_id +
                                    ",'" +
                                    i +
                                    '\')"><option value="0">不出</option><option selected="selected" value="1">出</option><option value="2">default</option></select></td>';
                            }
                            if (n.report == null || n.report == 2) {
                                htmlString +=
                                    '<td id="report' +
                                    i +
                                    '"><select id="sel' +
                                    i +
                                    '" name="check" onchange="updateReport(' +
                                    n.record_id +
                                    ",'" +
                                    i +
                                    '\')"><option value="0">不出</option><option value="1">出</option><option selected="selected" value="2">default</option></select></td>';
                            }
                            if (
                                n.filtered_rationale == null ||
                                n.filtered_rationale == "null"
                            ) {
                                htmlString +=
                                    '<td><input id="inp' +
                                    i +
                                    '" type="text" onblur="updateFiltered(' +
                                    n.record_id +
                                    "," +
                                    i +
                                    ')" onfocus="cleanMessage2()" value=""/></td>';
                            } else {
                                htmlString +=
                                    '<td><input id="inp' +
                                    i +
                                    '" type="text" onblur="updateFiltered(' +
                                    n.record_id +
                                    "," +
                                    i +
                                    ')" onfocus="cleanMessage2()" value="' +
                                    n.filtered_rationale +
                                    '"/></td>';
                            }
                            htmlString +=
                                "<td>" +
                                (n.loaded_date == null ? "" : n.loaded_date) +
                                "</td>";
                            htmlString +=
                                "<td>" + (n.record_id == null ? "" : n.record_id) + "</td>";
                            htmlString += "</tr>";
                        });
                        $("#tInfo2").append(htmlString);
                    }

                    $("#pagination").pagination(jsonObject.total, {
                        callback: displayData,
                        items_per_page: pageSize,
                        current_page: pageNo,
                        link_to: "javascript:void(0)",
                        num_display_entries: 5,
                        next_text: "下一页",
                        prev_text: "上一页",
                        next_show_always: true,
                        prev_show_always: true,
                        num_edge_entries: 2,
                        ellipse_text: "...",
                    });
                    $("#total").text(jsonObject.total);
                    var pageCount =
                        jsonObject.total % pageSize == 0
                            ? jsonObject.total / pageSize
                            : parseInt(jsonObject.total / pageSize) + 1;
                    $("#pageCount").text(pageCount);
                },
            });
        }

        function updateReport(record_id, id) {
            var sel = "sel" + id;
            var slectReprot = document.getElementById(sel).value;
            $.ajax({
                url: "${pageContext.request.contextPath}/filterCr/updateReport",
                type: "POST",
                data: {
                    report: slectReprot,
                    record_id: record_id,
                    platform: $("#platform").val(),
                },
                dataType: "json",
                success: function (result) {
                    if (result) {
                        $("#message2").text("report修改成功！");
                    } else {
                        $("#message2").text("report修改失败！");
                    }
                },
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
                url: "${pageContext.request.contextPath}/filterCr/updateFiltered",
                type: "POST",
                data: {
                    record_id: record_id,
                    filtered_rationale: inpVal,
                    platform: $("#platform").val(),
                },
                dataType: "json",
                success: function (result) {
                    if (result) {
                        $("#message2").text("filtered_rationale修改成功！");
                    } else {
                        $("#message2").text("filtered_rationale修改失败！");
                    }
                },
            });
        }

        function selects() {
            var report = $("#report").val();
            $("[name='check']").val(report).change();
        }
    </script>
</head>
<body style="width: 4305px">
<input
        type="hidden"
        id="platform"
        value="${currentNgsAvailable.platform }"
/>
<input
        type="hidden"
        id="analysis_date"
        value="${currentNgsAvailable.analysis_date }"
/>
<input
        type="hidden"
        id="subbarcode"
        value="${currentNgsAvailable.subbarcode }"
/>
<input
        type="hidden"
        id="product_name"
        value="${currentNgsAvailable.product_name }"
/>
<form method="post" action="" id="listform">
    <div class="panel admin-panel" id="alertBox">
        <div class="padding border-bottom">
            <ul class="search" style="padding-left: 10px">
                <li>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;筛选条件:
                </li>
                <li>
                    <input
                            onchange="displayData(0)"
                            name="isPass"
                            type="checkbox"
                            value="pass"
                    />通过&nbsp;
                    <input
                            onchange="displayData(0)"
                            name="isPass"
                            type="checkbox"
                            value="passNo"
                    />不通过&nbsp;
                    <input
                            onchange="displayData(0)"
                            name="isPass"
                            type="checkbox"
                            value="mateNo"
                    />无匹配&nbsp;
                    <input
                            onchange="displayData(0)"
                            name="isPass"
                            type="checkbox"
                            value="mate"
                    />匹配
                </li>
                <li>report</li>
                <li>
                    <select id="report" onchange="selects()">
                        <option value="0">不出</option>
                        <option value="1">出</option>
                        <option selected="selected" value="2">default</option>
                    </select>
                </li>
                <li
                        style="
                padding-left: 3130px;
                float: left;
                color: red;
                font-size: 14px;
              "
                >
                    <span id="message2"></span>
                </li>
            </ul>
        </div>
        <table class="table table-hover text-center">
            <tr>
                <th>file_id</th>
                <th>chr</th>
                <th>pos</th>
                <th>gene</th>
                <th>Transcript</th>
                <th>Exon</th>
                <th>cHGVS</th>
                <th>pHGVS</th>
                <th>Zygosity</th>
                <th>Clinical_significance</th>
                <!-- 带颜色 -->
                <th>ExonicFunc</th>
                <th>c1000g2015aug_all</th>
                <th>ExAC_EAS</th>
                <th>avsnp150</th>
                <th>SIFT_pred</th>
                <th>Polyphen2_HDIV_pred</th>
                <th>MutationTaster_pred</th>
                <th>revel</th>
                <th>gnomADALL</th>
                <th>Interpro_domain</th>
                <th>CLNSIG</th>
                <th>OMIM_Phenotypes</th>
                <th>HGMD_tag</th>
                <th>HGMD_disease</th>
                <th>HGMD_pmid</th>
                <th>report</th>
                <th>filtered_rationale</th>
                <th>loaded_date</th>
                <th>record_id</th>
            </tr>
            <tbody id="tInfo2"></tbody>
            <tr>
                <td colspan="29">
                    <table
                            width="100%"
                            height="30"
                            border="0"
                            cellpadding="0"
                            cellspacing="0"
                            class="page_table"
                    >
                        <tr>
                            <td width="8%" class="font_left">
                                数据:<span id="total"></span>条
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
