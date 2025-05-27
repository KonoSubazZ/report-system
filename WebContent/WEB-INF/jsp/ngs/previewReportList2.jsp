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
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-ui.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js"></script>
    <script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css"/>
    <script src="${pageContext.request.contextPath}/js/sweet-alert.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sweet-alert.css">
    <style type="text/css">
        .table th {
            white-space: nowrap;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            context_show();
            if ($("#primary_cancer").val() != "" && $("#primary_cancer").val() != null && $("#product_name_chinese").val() != "" && $("#product_name_chinese").val() != null) {
                $("#chemical").css("display", "block");
                $("#geneAndResult-show").css("display", "block");
            }
        });

        function deleteCancerTyping1166(id) {
            console.log("wozhixing", id);
            $.ajax({
                url: "${pageContext.request.contextPath}/geneMarkerVw/update-typing1166",
                type: "POST",
                data: {
                    "id": id
                },
                dataType: "json",
                success: function (result) {
                    let domId = "#evidence1166_" + id;
                    let domId1 = "#subtype1166_" + id;
                    $(domId).text("/");
                    $(domId1).text("/");
                    if (result) {
                        swal("成功！", "修改成功", "success");
                    } else {
                        swal("失败！", "修改失败", "error");
                    }
                }
            });
        }

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

        .table-hover > tbody > tr:hover > th {
            background-color: #0ae;
        }

        /*不支持IE6*/
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

        .btn {
            border-radius: 2px;
            cursor: pointer;
            background: #0ae;
            border: 0 none;
        }
    </style>

</head>
<body>
<input type="hidden" id="platform" value="${geneticMarkerVwPageBean.platform}">
<input type="hidden" id="analysis_date" value="${geneticMarkerVwPageBean.analysis_date}">
<input type="hidden" id="subbarcode" value="${geneticMarkerVwPageBean.subbarcode}">
<input type="hidden" id="product_name" value="${geneticMarkerVwPageBean.product_name}">
<input type="hidden" id="isblood" value="${isblood}">
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
            <input type="text" class="input w50" id="primary_cancer" name="primary_cancer"
                   value="${diseaseClass.disease_class_chinese}" placeholder="必选项！请选择本癌种"/>
            <span id="message_primary" style="color:#FF0000; font-size:25px;  margin-left:15px;">*</span>
            <input type="hidden" id="primary_cancer_id" name="primary_cancer_id" value="${diseaseClass.class_id}"/>
            <script type="text/javascript">
                $(function () {
                    $.post("${pageContext.request.contextPath}/autoComplete/getDiseaseClassChineseAndId", function (data) {
                        $('#primary_cancer').autocomplete(data, {
                            max: data.length, //列表里的条目数
                            minChars: 0, //自动完成激活之前填入的最小字符
                            width: 288, //提示的宽度，溢出隐藏
                            scrollHeight: 300, //提示的高度，溢出显示滚动条
                            matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                            autoFill: false, //自动填充
                            formatItem: function (row, i, max) {
                                return row.name;
                            },
                            formatResult: function (row) {
                                return row.name;
                            }
                        }).result(function (event, row, formatted) {
                            $("#primary_cancer_id").val(row.id);
                            //location.href="${pageContext.request.contextPath}/life/updatePrimaryCancerId?primary_cancer_id="+row.id+"&report_id=${geneticMarkerVwPageBean.report_id}&platform=${geneticMarkerVwPageBean.platform}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&analysis_date=${geneticMarkerVwPageBean.analysis_date}&product_name=${geneticMarkerVwPageBean.product_name}&analyzer=${user.user_account}&sig=tabs-5";
                        });
                    }, "json");
                })
            </script>
        </div>
        <div class="label" style="width:85px;margin-left:20px;padding-top: 15px;float: left;">
            <label style="font-size:15px;">检测产品：</label>
        </div>
        <div class="field" style="padding-top: 5px;float: left;">
            <input type="text" class="input w50" id="product_name_chinese" value="${product.product_name_chinese}"
                   placeholder="必选项！请选择检测产品"/>
            <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
            <input type="hidden" id="product_id" name="product_id" value="${product.product_id}"/>
            <script type="text/javascript">
                $(function () {
                    $.post("${pageContext.request.contextPath}/autoComplete/getProductNameChineseAndId",
                        <%--{product_name:"${geneticMarkerVwPageBean.product_name}"},--%>
                        function (data) {
                            $('#product_name_chinese').autocomplete(data, {
                                max: data.length, //列表里的条目数
                                minChars: 0, //自动完成激活之前填入的最小字符
                                width: 288, //提示的宽度，溢出隐藏
                                scrollHeight: 300, //提示的高度，溢出显示滚动条
                                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                                autoFill: false, //自动填充
                                formatItem: function (row, i, max) {
                                    return row.name;
                                },
                                formatResult: function (row) {
                                    return row.name;
                                }
                            }).result(function (event, row, formatted) {
                                $("#product_id").val(row.id);
                            });
                        }, "json");
                })
            </script>
            <div class="tips"></div>
        </div>
        <div class="label" style="width:85px;margin-left:20px;padding-top: 15px;float: left;">
            <label style="font-size:15px;">癌种模块：</label>
        </div>
        <div class="field" style="padding-top: 5px;float: left;">
            <select class="input w50" id="moduleFlag" name="moduleFlag" onchange="getAnalysisReportByReportId()">
                <option value=""></option>
                <option value="子宫内膜癌分子分型">子宫内膜癌分子分型</option>
                <option value="肉瘤分子分型">肉瘤分子分型</option>
                <option value="肾癌预后">肾癌预后</option>
                <option value="前列腺癌内分泌和预后">前列腺癌内分泌和预后</option>
                <option value="尿路上皮癌/膀胱癌预后">尿路上皮癌/膀胱癌预后</option>
                <option value="子宫内膜癌分子分型+肉瘤分子分型">子宫内膜癌分子分型+肉瘤分子分型</option>
                <option value="肾癌1166分子分型">肾癌1166分子分型</option>
                <option value="脑胶质瘤1166分子分型">脑胶质瘤1166分子分型</option>
            </select>
            <%--<input type="text" class="input w50" id="moduleFlag" onchange="getAnalysisReportByReportId()" value="${geneticMarkerVwPageBean.moduleFlag}" placeholder="非必选项！请选择癌种模块"  />--%>
            <script type="text/javascript">
                /*$(function(){
						var data = [{name: "子宫内膜癌分子分型"}, {name: "肉瘤分子分型"}, {name: "肾癌预后"}, {name: "前列腺癌内分泌和预后"}, {name: "尿路上皮癌/膀胱癌预后"}];
						$('#moduleFlag').autocomplete(data, {
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
						});
					})*/
                $("#moduleFlag").val("${moduleFlag}");

                function getAnalysisReportByReportId() {
                    $.ajax({
                        cache: false,
                        type: "POST",
                        url: "${pageContext.request.contextPath}/geneMarkerVw/updateModuleFlagByReportId", //把表单数据发送到ajax.jsp
                        data: {
                            "report_id": "${geneticMarkerVwPageBean.report_id }",
                            "module_flag": $("#moduleFlag").val()
                        }, //要发送的是ajaxFrm表单中的数据
                        success: function (result) {
                            if (result) {
                                alert("癌种模块修改成功！");
                            } else {
                                alert("癌种模块修改失败！");
                            }
                        }
                    });
                }
            </script>
            <span style="color:#FF0000; font-size:25px;  margin-left:15px">*</span>
            <div class="tips"></div>
        </div>
        <div style="float:left;margin:8px 0 0 10px;">
            <button style="width: 150px;padding:10px;border-radius: 3px;border:none;background:#0ae;border:1px solid #ccc"
                    onclick="knowledgeBaseMatching()"
                    <c:if test="${pendingAndErrorCount > 0}">disabled="disabled"</c:if>>匹配最新库
            </button>
        </div>
        <script type="text/javascript">
            function knowledgeBaseMatching() {
                $.ajax({
                    url: "${pageContext.request.contextPath}/life/updateProductByProductId",
                    data: {
                        "primary_cancer_id": $("#primary_cancer_id").val(),
                        "product_id": $("#product_id").val(),
                        "report_id": "${geneticMarkerVwPageBean.report_id }",
                        "platform": "${geneticMarkerVwPageBean.platform}",
                        "analysis_date": "${geneticMarkerVwPageBean.analysis_date}",
                        "subbarcode": "${geneticMarkerVwPageBean.subbarcode}",
                        "product_name": "${geneticMarkerVwPageBean.product_name}",
                        "product_name_show": "${geneticMarkerVwPageBean.product_name_show}"
                    },
                    success: function (data) {
                        parent.urlRunp4(data, $("#moduleFlag").val(), "0");
                    },
                    dataType: "json"
                });
            }
        </script>
        <div style="float:left;margin:8px 0 0 10px;">
            <button style="width: 150px;padding:10px;border-radius: 3px;border:none;background:#0ae;border:1px solid #ccc"
                    onclick="knowledgeBaseMatching2()"
                    <c:if test="${pendingAndErrorCount > 0}">disabled="disabled"</c:if>>匹配模块化
            </button>
        </div>
        <script type="text/javascript">
            function knowledgeBaseMatching2() {
                $.ajax({
                    url: "${pageContext.request.contextPath}/life/updateProductByProductId",
                    data: {
                        "primary_cancer_id": $("#primary_cancer_id").val(),
                        "product_id": $("#product_id").val(),
                        "report_id": "${geneticMarkerVwPageBean.report_id }",
                        "platform": "${geneticMarkerVwPageBean.platform}",
                        "analysis_date": "${geneticMarkerVwPageBean.analysis_date}",
                        "subbarcode": "${geneticMarkerVwPageBean.subbarcode}",
                        "product_name": "${geneticMarkerVwPageBean.product_name}",
                        "product_name_show": "${geneticMarkerVwPageBean.product_name_show}"
                    },
                    success: function (data) {
                        parent.urlRunp4(data, $("#moduleFlag").val(), "1");
                    },
                    dataType: "json"
                });
            }
        </script>
    </div>

    <div class="form-group">
        <div id="chemical" style="float: right; padding:13px 600px 20px 50px;float: left;">
            <input type="radio" onclick="context_show();" checked name="show" value="1"/> 人工查看
            <input type="radio" onclick="context_show();" name="show" value="2"/> 报告预览
            <script type="text/javascript">
                function context_show() {
                    $("#message_primary").html("*");
                    var obj = document.getElementsByName("show");
                    for (var i = 0; i < obj.length; i++) {
                        if (obj[i].checked) {
                            //alert(obj[i].value == 1);
                            if (obj[i].value == 1) {
                                $("#geneAndResult-show").show();
                                $("#meditationPoints-show").hide();
                                $("#chemical-show").hide();
                            } else if (obj[i].value == 2) {
                                clearPreviewContent();
                                initTargetDrugTable();
                                initUnknownMutTable();
                                initChemoSideEffectsAndEffectivenessTable();
                                initGeneticCancerRisk();
                                initTargetDrugAnalysisTable();
                                initUnknownGeneAnalysisTable();
                                initEffectivenessAndSideEffects();
                                initCancerRiskAnalysisTable();
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
        <c:if test="${geneticMarkerVwPageBean.module == '1'}">
            <c:if test="${endometrialCarcinoma == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">子宫内膜癌TCGA分子分型检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo3" class="my-tbody">
                        <tr id="tcga_tr">
                            <th>检测项目</th>
                            <th>检测结果</th>
                        </tr>
                        <tr>
                            <td><label>TCGA分子分型</label></td>
                            <td>
                                <select id="tcga" onChange="updateTcga()">
                                  <%--  <option value="POLE基因突变型（POLE）"
                                            <c:if test="${tcga == 'POLE基因突变型（POLE）'}">selected</c:if>>
                                        POLE基因突变型（POLE）
                                    </option>
                                    <option value="微卫星不稳定型（MSI-H）"
                                            <c:if test="${tcga == '微卫星不稳定型（MSI-H）'}">selected</c:if>>
                                        微卫星不稳定型（MSI-H）
                                    </option>
                                    <option value="高拷贝型（Copy-number High，CN-H）"
                                            <c:if test="${tcga == '高拷贝型（Copy-number High，CN-H）'}">selected</c:if>>
                                        高拷贝型（Copy-number High，CN-H）
                                    </option>
                                    <option value="低拷贝型（Copy-number Low，CN-L）"
                                            <c:if test="${tcga == '低拷贝型（Copy-number Low，CN-L）'}">selected</c:if>>
                                        低拷贝型（Copy-number Low，CN-L）
                                    </option>--%>
                                      <option value="POLE突变型"
                                              <c:if test="${tcga == 'POLE突变型'}">selected</c:if>>
                                          POLE突变型
                                      </option>
                                      <option value="微卫星不稳定型"
                                              <c:if test="${tcga == '微卫星不稳定型'}">selected</c:if>>
                                          微卫星不稳定型
                                      </option>
                                      <option value="p53突变型"
                                              <c:if test="${tcga == 'p53突变型'}">selected</c:if>>
                                          p53突变型
                                      </option>
                                      <option value="非特异性分子谱型"
                                              <c:if test="${tcga == '非特异性分子谱型'}">selected</c:if>>
                                          非特异性分子谱型
                                      </option>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <script>
                        function updateTcga() {
                            var tcga = $("#tcga").val();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateTcga",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "tcga": tcga,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${hrdFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">同源重组缺陷状态提示</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo14" class="my-tbody">
                        <tr id="hrd_tr">
                            <th>检测标志物</th>
                            <th>检测结果</th>
                        </tr>
                        <c:if test="${hrdBRCAState != ''}">
                            <tr>
                                <td><label>BRCA1/2基因状态</label></td>
                                <td>
                                    <select id="hrdBRCAState" onChange="updateHrd()">
                                        <option value="检测到该肿瘤患者存在BRCA基因致病或可能致病性变异"
                                                <c:if test="${hrdBRCAState == '检测到该肿瘤患者存在BRCA基因致病或可能致病性变异'}">selected</c:if>>
                                            检测到该肿瘤患者存在BRCA基因致病或可能致病性变异
                                        </option>
                                        <option value="未检测到该肿瘤患者存在BRCA基因致病或可能致病性变异"
                                                <c:if test="${hrdBRCAState == '未检测到该肿瘤患者存在BRCA基因致病或可能致病性变异'}">selected</c:if>>
                                            未检测到该肿瘤患者存在BRCA基因致病或可能致病性变异
                                        </option>
                                    </select>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td><label>基因组不稳定性评分</label></td>
                            <td><input id="hrdScore" style="text-align:center;" onChange="updateHrd()"
                                       value="${hrdScore}"/></td>
                        </tr>
                        <tr>
                            <td><label>同源重组缺陷状态</label></td>
                            <td>
                                <select id="hrdState" onChange="updateHrd()">
                                    <option value="阳性" <c:if test="${hrdState == '阳性'}">selected</c:if>>阳性
                                    </option>
                                    <option value="阴性" <c:if test="${hrdState == '阴性'}">selected</c:if>>阴性
                                    </option>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <script>
                        function updateHrd() {
                            var hrdBRCAState = $("#hrdBRCAState").val();
                            var hrdScore = $("#hrdScore").val();
                            var hrdState = $("#hrdState").val();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateHrd",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "hrdBRCAState": hrdBRCAState,
                                    "hrdScore": hrdScore,
                                    "hrdState": hrdState,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                            if (hrdState == "阳性") {
                                $("#hrd").show();
                            } else if (hrdState == "阴性") {
                                $("#hrd").hide();
                            }
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${msi_status_state != ''}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">微卫星不稳定（MSI）检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="msi" class="my-tbody">
                        <tr>
                            <th>检测项目</th>
                            <th>检测结果</th>
                        </tr>
                        <tr>
                            <td><label>微卫星不稳定（microsatellite instability，MSI）</label></td>
                            <td><label>${msi_status_state}</label></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <br>
            </c:if>
            <div>
                <h2 style="color: blue;font-size: 20px;font-weight: bold;">错配修复（MMR）相关基因检测结果</h2><c:if
                    test="${flag == true}"><input type="button" value="添加" onclick="addMmDmmr()"
                                                  class="button border-blue icon-plus-square-o"/></c:if>
                <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                    <tbody id="tInfo8" class="my-tbody">
                    <tr id="dmmr_tr">
                        <th>基因名称</th>
                        <th>检测结果</th>
                        <th>变异丰度</th>
                        <th>突变类型</th>
                        <th>操作</th>
                    </tr>
                    <c:forEach items="${mmDmmrs}" var="item" varStatus="vs">
                        <tr id="dmmr_tr_${vs.count}">
                            <td><label id="dmmr_gene_${vs.count}">${item.gene}</label></td>
                            <td><label id="dmmr_ori_variant_${vs.count}">${item.ori_variant}</label></td>
                            <td><label id="dmmr_mutFreq_${vs.count}">${item.mutFreq}</label></td>
                            <td><input id="dmmr_mut_type_${vs.count}" style="text-align:center; width: 250px;"
                                       value="${item.mut_type}"/></td>
                            <td><input type="button" value="保存" onclick="updateMmDmmr(${vs.count})" class="btn"/>
                                <input type="button" value="删除" onclick="deleteMmDmmr(${vs.count})" class="btn"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <script>
                    function addMmDmmr() {
                        showMddDialog();
                    }

                    function updateMmDmmr(i) {
                        var gene = $("#dmmr_gene_" + i).text();
                        var ori_variant = $("#dmmr_ori_variant_" + i).text();
                        var mutFreq = $("#dmmr_mutFreq_" + i).text();
                        var mut_type = $("#dmmr_mut_type_" + i).val();
                        $.ajax({
                            url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmDmmr",
                            type: "POST",
                            data: {
                                "report_id":${geneticMarkerVwPageBean.report_id},
                                "gene": gene,
                                "ori_variant": ori_variant,
                                "mutFreq": mutFreq,
                                "mut_type": mut_type,
                                "update_by": "${user.user_account}"
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result) {
                                    swal("成功！", "修改成功", "success");
                                } else {
                                    swal("失败！", "修改失败", "error");
                                }
                            }
                        });
                    }

                    function deleteMmDmmr(i) {
                        var gene = $("#dmmr_gene_" + i).text();
                        var ori_variant = $("#dmmr_ori_variant_" + i).text();
                        var mutFreq = $("#dmmr_mutFreq_" + i).text();
                        $.ajax({
                            url: "${pageContext.request.contextPath}/geneMarkerVw/deleteMmDmmr",
                            type: "POST",
                            data: {
                                "report_id":${geneticMarkerVwPageBean.report_id},
                                "gene": gene,
                                "ori_variant": ori_variant,
                                "mutFreq": mutFreq
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result) {
                                    $("#dmmr_tr_" + i).remove();
                                    swal("成功！", "删除成功", "success");
                                } else {
                                    swal("失败！", "删除失败", "error");
                                }
                            }
                        });
                    }
                </script>
            </div>
            <br>
            <c:if test="${sarcomaFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">肉瘤辅助诊断提示</h2><input type="button"
                                                                                                           value="添加"
                                                                                                           onclick="addMmSarcomaTyping()"
                                                                                                           class="button border-blue icon-plus-square-o"/>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo4" class="my-tbody">
                        <tr id="sarcomaTyping_tr">
                            <th>检测结果</th>
                            <th>变异丰度/融合reads</th>
                            <th>基因变异相关肉瘤亚型</th>
                            <th>证据等级</th>
                            <th>突变形式</th>
                            <th>突变说明</th>
                            <th>变异解析</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${mmSarcomaTypings}" var="item" varStatus="vs">
                            <tr id="sarcomaTyping_tr_${vs.count}">
                                <td><label id="st_mutation_${vs.count}">${item.mutation}</label><br><input
                                        id="st_transcript_${vs.count}" style="text-align:center; width: 250px;"
                                        value="${item.transcript}"/></td>
                                <td><label id="st_mutFreq_${vs.count}">${item.mutFreq}</label></td>
                                <td><textarea id="st_sarcoma_subtype_${vs.count}"
                                              style="width: 319px; height: 80px;">${item.sarcoma_subtype}</textarea>
                                </td>
                                <td>
                                    <select id="st_evidence_${vs.count}">
                                        <option value="WHO" <c:if test="${item.evidence == 'WHO'}">selected</c:if>>WHO
                                        </option>
                                        <option value="NCCN" <c:if test="${item.evidence == 'NCCN'}">selected</c:if>>
                                            NCCN
                                        </option>
                                        <option value="CSCO" <c:if test="${item.evidence == 'CSCO'}">selected</c:if>>
                                            CSCO
                                        </option>
                                        <option value="专家共识"
                                                <c:if test="${item.evidence == '专家共识'}">selected</c:if>>专家共识
                                        </option>
                                    </select>
                                </td>
                                <td><label id="st_ori_variant_${vs.count}">${item.ori_variant}</label></td>
                                <td><textarea id="st_mutDesc2_${vs.count}"
                                              style="width: 319px; height: 80px;">${item.mutDesc2}</textarea></td>
                                <td><textarea id="st_mutationAnalysis_${vs.count}"
                                              style="width: 319px; height: 80px;">${item.mutationAnalysis}</textarea>
                                </td>
                                <td><input type="button" value="保存" onclick="updateMmSarcomaTyping(${vs.count})"
                                           class="btn"/> <input type="button" value="删除"
                                                                onclick="deleteMmSarcomaTyping(${vs.count})"
                                                                class="btn"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function addMmSarcomaTyping() {
                            showMstdDialog();
                        }

                        function updateMmSarcomaTyping(i) {
                            var mutation = $("#st_mutation_" + i).text();
                            var transcript = $("#st_transcript_" + i).val();
                            var mutFreq = $("#st_mutFreq_" + i).text();
                            var sarcoma_subtype = $("#st_sarcoma_subtype_" + i).val();
                            var evidence = $("#st_evidence_" + i).val();
                            var ori_variant = $("#st_ori_variant_" + i).text();
                            var mutDesc2 = $("#st_mutDesc2_" + i).val();
                            var mutationAnalysis = $("#st_mutationAnalysis_" + i).val();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmSarcomaTyping",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "mutation": mutation,
                                    "transcript": transcript,
                                    "mutFreq": mutFreq,
                                    "sarcoma_subtype": sarcoma_subtype,
                                    "evidence": evidence,
                                    "ori_variant": ori_variant,
                                    "mutDesc2": mutDesc2,
                                    "mutationAnalysis": mutationAnalysis,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }

                        function deleteMmSarcomaTyping(i) {
                            var mutation = $("#st_mutation_" + i).text();
                            var mutFreq = $("#st_mutFreq_" + i).text();
                            var ori_variant = $("#st_ori_variant_" + i).text();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/deleteMmSarcomaTyping",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "mutation": mutation,
                                    "mutFreq": mutFreq,
                                    "ori_variant": ori_variant
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        $("#sarcomaTyping_tr_" + i).remove();
                                        swal("成功！", "删除成功", "success");
                                    } else {
                                        swal("失败！", "删除失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${lymphomaFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">淋巴瘤辅助分型及预后相关提示</h2><input
                        type="button" value="添加" onclick="addMmLymphomaTyping()"
                        class="button border-blue icon-plus-square-o"/>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo5" class="my-tbody">
                        <tr id="lymphomaTyping_tr">
                            <th>突变基因</th>
                            <th>检测结果</th>
                            <th>变异丰度</th>
                            <th>辅助分型相关的淋巴瘤亚型</th>
                            <th>预后相关的淋巴瘤亚型</th>
                            <th>证据等级</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${mmLymphomaTypings}" var="item" varStatus="vs">
                            <tr id="lymphomaTyping_tr_${vs.count}">
                                <td><label id="lt_gene_${vs.count}">${item.gene}</label></td>
                                <td><label id="lt_ori_variant_${vs.count}">${item.ori_variant}</label></td>
                                <td><label id="lt_mutFreq_${vs.count}">${item.mutFreq}</label></td>
                                <td><textarea id="lt_lymphoma_subtype_${vs.count}"
                                              style="width: 280px; height: 80px;">${item.lymphoma_subtype}</textarea>
                                </td>
                                <td><textarea id="lt_lymphoma_subtype2_${vs.count}"
                                              style="width: 280px; height: 80px;">${item.lymphoma_subtype2}</textarea>
                                </td>
                                <td>
                                    <select id="lt_evidence_${vs.count}">
                                        <option value="WHO" <c:if test="${item.evidence == 'WHO'}">selected</c:if>>WHO
                                        </option>
                                        <option value="NCCN" <c:if test="${item.evidence == 'NCCN'}">selected</c:if>>
                                            NCCN
                                        </option>
                                        <option value="CSCO" <c:if test="${item.evidence == 'CSCO'}">selected</c:if>>
                                            CSCO
                                        </option>
                                        <option value="专家共识"
                                                <c:if test="${item.evidence == '专家共识'}">selected</c:if>>专家共识
                                        </option>
                                    </select>
                                </td>
                                <td><input type="button" value="保存" onclick="updateMmLymphomaTyping(${vs.count})"
                                           class="btn"/> <input type="button" value="删除"
                                                                onclick="deleteMmLymphomaTyping(${vs.count})"
                                                                class="btn"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function addMmLymphomaTyping() {
                            showMltdDialog();
                        }

                        function updateMmLymphomaTyping(i) {
                            var gene = $("#lt_gene_" + i).text();
                            var ori_variant = $("#lt_ori_variant_" + i).text();
                            var mutFreq = $("#lt_mutFreq_" + i).text();
                            var lymphoma_subtype = $("#lt_lymphoma_subtype_" + i).val();
                            var lymphoma_subtype2 = $("#lt_lymphoma_subtype2_" + i).val();
                            var evidence = $("#lt_evidence_" + i).val();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmLymphomaTyping",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "gene": gene,
                                    "ori_variant": ori_variant,
                                    "mutFreq": mutFreq,
                                    "lymphoma_subtype": lymphoma_subtype,
                                    "lymphoma_subtype2": lymphoma_subtype2,
                                    "evidence": evidence,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }

                        function deleteMmLymphomaTyping(i) {
                            var gene = $("#lt_gene_" + i).text();
                            var ori_variant = $("#lt_ori_variant_" + i).text();
                            var mutFreq = $("#lt_mutFreq_" + i).text();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/deleteMmLymphomaTyping",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "gene": gene,
                                    "ori_variant": ori_variant,
                                    "mutFreq": mutFreq
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        $("#lymphomaTyping_tr_" + i).remove();
                                        swal("成功！", "删除成功", "success");
                                    } else {
                                        swal("失败！", "删除失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${thyroidHotspotFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">甲状腺癌热点基因检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo6" class="my-tbody">
                        <tr id="thyroidHotspot_tr">
                            <th>标志物</th>
                            <th>变异类型</th>
                            <th>临床意义</th>
                            <th>检出情况</th>
                        </tr>
                        <c:forEach items="${mmThyroidHotspots}" var="item" varStatus="vs">
                            <tr id="thyroidHotspot_tr_${vs.count}">
                                <td><label id="th_gene_${vs.count}">${item.gene}</label></td>
                                <td><label id="th_type_${vs.count}">${item.type}</label></td>
                                <td><label id="th_meaning_${vs.count}">${item.meaning}</label></td>
                                <td>
                                    <select id="th_situation_${vs.count}"
                                            <c:if test="${item.situation == '检出'}">style="background: red"</c:if>
                                            onChange="updateMmThyroidHotspot(${vs.count})">
                                        <option value="检出" <c:if test="${item.situation == '检出'}">selected</c:if>>
                                            检出
                                        </option>
                                        <option value="未检出"
                                                <c:if test="${item.situation == '未检出'}">selected</c:if>>未检出
                                        </option>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function updateMmThyroidHotspot(i) {
                            var gene = $("#th_gene_" + i).text();
                            var situation = $("#th_situation_" + i).val();
                            if (situation == "检出") {
                                document.getElementById("th_situation_" + i).style.backgroundColor = 'red'
                            } else {
                                document.getElementById("th_situation_" + i).style.backgroundColor = 'white'
                            }
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmThyroidHotspot",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "gene": gene,
                                    "situation": situation,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">甲状腺癌预后评估</h2><input type="button"
                                                                                                           value="添加"
                                                                                                           onclick="addMmThyroidPrognosis()"
                                                                                                           class="button border-blue icon-plus-square-o"/>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo7" class="my-tbody">
                        <tr id="thyroidPrognosis_tr">
                            <th>突变基因</th>
                            <th>检测结果</th>
                            <th>变异丰度</th>
                            <th>预后评估</th>
                            <th>预后评估说明</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${mmThyroidPrognoses}" var="item" varStatus="vs">
                            <tr id="thyroidPrognosis_tr_${vs.count}">
                                <td><label id="tp_gene_${vs.count}">${item.gene}</label></td>
                                <td><input id="tp_ori_variant_${vs.count}" style="text-align:center; width: 250px;"
                                           value="${item.ori_variant}"/></td>
                                <td><label id="tp_mutFreq_${vs.count}">${item.mutFreq}</label></td>
                                <td><textarea id="tp_prognosis_evaluation_${vs.count}"
                                              style="width: 280px; height: 80px;">${item.prognosis_evaluation}</textarea>
                                </td>
                                <td><textarea id="tp_prognosis_assessment_${vs.count}"
                                              style="width: 280px; height: 80px;">${item.prognosis_assessment}</textarea>
                                </td>
                                <td><input type="button" value="保存" onclick="updateMmThyroidPrognosis(${vs.count})"
                                           class="btn"/> <input type="button" value="删除"
                                                                onclick="deleteMmThyroidPrognosis(${vs.count})"
                                                                class="btn"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function addMmThyroidPrognosis() {
                            showMtpdDialog();
                        }

                        function updateMmThyroidPrognosis(i) {
                            var gene = $("#tp_gene_" + i).text();
                            var ori_variant = $("#tp_ori_variant_" + i).val();
                            var mutFreq = $("#tp_mutFreq_" + i).text();
                            var prognosis_evaluation = $("#tp_prognosis_evaluation_" + i).val();
                            var prognosis_assessment = $("#tp_prognosis_assessment_" + i).val();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmThyroidPrognosis",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "gene": gene,
                                    "ori_variant": ori_variant,
                                    "mutFreq": mutFreq,
                                    "prognosis_evaluation": prognosis_evaluation,
                                    "prognosis_assessment": prognosis_assessment,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }

                        function deleteMmThyroidPrognosis(i) {
                            var gene = $("#tp_gene_" + i).text();
                            var ori_variant = $("#tp_ori_variant_" + i).val();
                            var mutFreq = $("#tp_mutFreq_" + i).text();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/deleteMmThyroidPrognosis",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "gene": gene,
                                    "ori_variant": ori_variant,
                                    "mutFreq": mutFreq
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        $("#thyroidPrognosis_tr_" + i).remove();
                                        swal("成功！", "删除成功", "success");
                                    } else {
                                        swal("失败！", "删除失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${brainGliomaFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">脑胶质瘤相关分子标记物检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo10" class="my-tbody">
                        <tr id="brainGlioma_tr">
                            <th>分子标记物</th>
                            <th>检测结果</th>
                        </tr>
                        <c:forEach items="${mmBrainGliomas}" var="item" varStatus="vs">
                            <tr id="brainGlioma_tr_${vs.count}">
                                <td <c:if test="${vs.index < 3}">bgcolor="#fcd5b4"</c:if>><label
                                        id="bg_info_${vs.count}">${item.info}</label></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.output == '检出' or item.output == '未检出' }">
                                            <select id="bg_output_${vs.count}"
                                                    <c:if test="${item.output == '检出'}">style="background: red"</c:if>
                                                    onChange="updateMmBrainGlioma(${vs.count})">
                                                <option value="检出"
                                                        <c:if test="${item.output == '检出'}">selected</c:if>>检出
                                                </option>
                                                <option value="未检出"
                                                        <c:if test="${item.output == '未检出'}">selected</c:if>>未检出
                                                </option>
                                            </select>
                                        </c:when>
                                        <c:when test="${item.output == '阳性' or item.output == '阴性'  or item.output == '待验证' }">
                                            <select id="bg_output_${vs.count}"
                                                    <c:if test="${item.output == '阳性'}">style="background: red"
                                            </c:if>
                                                    <c:if test="${item.output == '待验证'}">style="background: yellow"</c:if>
                                                    onChange="updateMmBrainGlioma(${vs.count})">
                                                <option value="阳性"
                                                        <c:if test="${item.output == '阳性'}">selected</c:if>>阳性
                                                </option>
                                                <option value="阴性"
                                                        <c:if test="${item.output == '阴性'}">selected</c:if>>阴性
                                                </option>
                                                <option value="待验证"
                                                        <c:if test="${item.output == '待验证'}">selected</c:if>>待验证
                                                </option>
                                            </select>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function updateMmBrainGlioma(i) {
                            var info = $("#bg_info_" + i).text();
                            var output = $("#bg_output_" + i).val();
                            if (output == "检出" || output == "阳性") {
                                document.getElementById("bg_output_" + i).style.backgroundColor = 'red'
                            } else {
                                document.getElementById("bg_output_" + i).style.backgroundColor = 'white'
                            }
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmBrainGlioma",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "info": info,
                                    "output": output,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${cancerTyping1166Flag == true}">

                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">中线癌/肾癌辅助诊断提示</h2>
                    <table>
                        <tbody id="tInfo20" class="my-tbody">
                        <tr id="cancerTyping1166_tr">
                            <th style="border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;">
                                检测结果
                            </th>
                            <th style="border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;">
                                融合reads
                            </th>
                            <th style="border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;">
                                基因变异相关亚型
                            </th>
                            <th style="border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;">
                                证据等级
                            </th>
                            <th style="border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;">
                                操作
                            </th>
                        </tr>
                        <c:forEach items="${cancerTyping1166}" var="item" varStatus="vs">
                            <tr id="cancerTyping1166_${item.id}">
                                <td style="width:280px;border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;vertical-align: middle;">
                                    <div>${item.variant}<br>${item.transcript}</div>
                                </td>
                                <td style="border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;vertical-align: middle;">${item.mut_freq}</td>
                                <td id="evidence1166_${item.id}" style="width:200px;border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;vertical-align: middle;">${item.subtype}</td>
                                <td id="subtype1166_${item.id}" style="width:100px;border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;vertical-align: middle;">${item.evidence}</td>
                                <td style="width:100px;border: 1px solid #ccc; padding: 12px; text-align: center; font-size: 14px; color: #555;vertical-align: middle;">
                                    <input type="button" value="删除" onclick="deleteCancerTyping1166(${item.id})"
                                           class="btn"/>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

            </c:if>
            <c:if test="${prostateCancerFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">内分泌治疗相关基因检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo11" class="my-tbody">
                        <tr id="endocrineTherapy_tr">
                            <th>分子标记物</th>
                            <th>检测结果</th>
                        </tr>
                        <c:forEach items="${mmEndocrineTherapys}" var="item" varStatus="vs">
                            <tr id="endocrineTherapy_tr_${vs.count}">
                                <td><label id="et_info_${vs.count}">${item.info}</label></td>
                                <td>
                                    <select id="et_output_${vs.count}"
                                            <c:if test="${item.output == '检出'}">style="background: red"</c:if>
                                            onChange="updateMmEndocrineTherapy(${vs.count})">
                                        <option value="检出" <c:if test="${item.output == '检出'}">selected</c:if>>
                                            检出
                                        </option>
                                        <option value="未检出" <c:if test="${item.output == '未检出'}">selected</c:if>>
                                            未检出
                                        </option>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function updateMmEndocrineTherapy(i) {
                            var info = $("#et_info_" + i).text();
                            var output = $("#et_output_" + i).val();
                            if (output == "检出") {
                                document.getElementById("et_output_" + i).style.backgroundColor = 'red'
                            } else {
                                document.getElementById("et_output_" + i).style.backgroundColor = 'white'
                            }
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmEndocrineTherapy",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "info": info,
                                    "output": output,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">神经内分泌分化相关基因检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo12" class="my-tbody">
                        <tr id="endocrineDifferentiation_tr">
                            <th>分子标记物</th>
                            <th>检测结果</th>
                        </tr>
                        <c:forEach items="${mmEndocrineDifferentiations}" var="item" varStatus="vs">
                            <tr id="endocrineDifferentiation_tr_${vs.count}">
                                <td><label id="ed_info_${vs.count}">${item.info}</label></td>
                                <td>
                                    <select id="ed_output_${vs.count}"
                                            <c:if test="${item.output == '检出'}">style="background: red"</c:if>
                                            onChange="updateMmEndocrineDifferentiation(${vs.count})">
                                        <option value="检出" <c:if test="${item.output == '检出'}">selected</c:if>>
                                            检出
                                        </option>
                                        <option value="未检出" <c:if test="${item.output == '未检出'}">selected</c:if>>
                                            未检出
                                        </option>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function updateMmEndocrineDifferentiation(i) {
                            var info = $("#ed_info_" + i).text();
                            var output = $("#ed_output_" + i).val();
                            if (output == "检出") {
                                document.getElementById("ed_output_" + i).style.backgroundColor = 'red'
                            } else {
                                document.getElementById("ed_output_" + i).style.backgroundColor = 'white'
                            }
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmEndocrineDifferentiation",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "info": info,
                                    "output": output,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
            <c:if test="${urinaryProstateFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">泌尿预后相关基因检测结果</h2>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo13" class="my-tbody">
                        <tr id="urinaryProstate_tr">
                            <th>分子标记物</th>
                            <th>检测结果</th>
                        </tr>
                        <c:forEach items="${mmUrinaryProstates}" var="item" varStatus="vs">
                            <tr id="urinaryProstate_tr_${vs.count}">
                                <td><label id="up_info_${vs.count}">${item.info}</label></td>
                                <td>
                                    <select id="up_output_${vs.count}"
                                            <c:if test="${item.output == '检出'}">style="background: red"</c:if>
                                            onChange="updateMmUrinaryProstate(${vs.count})">
                                        <option value="检出" <c:if test="${item.output == '检出'}">selected</c:if>>
                                            检出
                                        </option>
                                        <option value="未检出" <c:if test="${item.output == '未检出'}">selected</c:if>>
                                            未检出
                                        </option>
                                    </select>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function updateMmUrinaryProstate(i) {
                            var info = $("#up_info_" + i).text();
                            var output = $("#up_output_" + i).val();
                            if (output == "检出") {
                                document.getElementById("up_output_" + i).style.backgroundColor = 'red'
                            } else {
                                document.getElementById("up_output_" + i).style.backgroundColor = 'white'
                            }
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmUrinaryProstate",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "info": info,
                                    "output": output,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
        </c:if>
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
                <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;临床意义&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
                <th>变异解析</th>
                <th>上次审核</th>
                <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
                <th>revel</th>
                <th>gnomADALL</th>
                <th>Interpro_domain</th>
                <th>CLNSIG</th>
                <th>HGMD_tag</th>
                <th>HGMD_disease</th>
                <th>HGMD_pmid</th>
            </tr>
            </tbody>
            <c:forEach items="${crAllList}" var="item" varStatus="vs">
                <tr>
                    <td>${vs.index+1}</td>
                    <td>${item.Gene}</td>
                    <td>${item.Chr}</td>
                    <td>${item.Exon}</td>
                    <td style="cursor:pointer">
                        <p id="pos_transcript_${vs.count}" onmouseover='showTitle1(${vs.count})'
                           title=''>${item.cHGVS}</p>
                        <script type="text/javascript">
                            function showTitle1(index) {
                                var data = crAllList[parseInt(index) - 1];
                                $("#pos_transcript_" + index).attr("title", "位置：" + (data.Pos || "") + "\r\n转录本号：" + (data.Transcript || ""));
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
                        <%--<td>${item.ExonicFunc}</td>--%>
                    <td>
                        <select id="myExonicFunc${vs.count}">
                            <option value="${item.ExonicFunc}">${item.ExonicFunc}</option>
                            <option value="错义突变">错义突变</option>
                            <option value="同义突变">同义突变</option>
                            <option value="非移码突变">非移码突变</option>
                            <option value="移码突变">移码突变</option>
                            <option value="无义突变">无义突变</option>
                            <option value="终止子缺失">终止子缺失</option>
                            <option value="剪接突变">剪接突变</option>
                            <option value="启动子区变异">启动子区变异</option>
                            <option value="未知">未知</option>
                        </select>
                        <script>
                            $("#myExonicFunc${vs.count}").val("${item.ExonicFunc}")
                            $("#myExonicFunc${vs.count}").off("change");
                            $("#myExonicFunc${vs.count}").change(function (e) {
                                var ExonicFunc = $("#myExonicFunc${vs.count}").val();
                                $.ajax({
                                    url: "${pageContext.request.contextPath}/geneMarkerVw/updateCrAll",
                                    type: "POST",
                                    data: {
                                        "subbarcode": "${geneticMarkerVwPageBean.subbarcode}",
                                        "analysis_date": "${geneticMarkerVwPageBean.analysis_date}",
                                        "Gene": "${item.Gene}",
                                        "Exon": "${item.Exon}",
                                        "cHGVS": "${item.cHGVS}",
                                        "pHGVS": "${item.pHGVS}",
                                        "ExonicFunc": ExonicFunc,
                                        "update_by": "${user.user_account}"
                                    },
                                    dataType: "json",
                                    success: function (result) {
                                        if (result) {
                                            swal("成功！", "修改成功", "success");
                                        } else {
                                            swal("失败！", "修改失败", "error");
                                        }
                                    }
                                });
                            });
                        </script>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.avsnp150 == '.'}">
                                <label>.</label>
                            </c:when>
                            <c:otherwise>
                                <a style="text-decoration: underline; color: blue;" target="_blank"
                                   href="https://www.ncbi.nlm.nih.gov/clinvar/variation/${item.avsnp150}/">${item.avsnp150}</a>
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
                            if ("${item.rpCr.Clinical_significance}") {
                                $("#myselect${vs.count}").val("${item.rpCr.Clinical_significance}")
                            }
                            $("#myselect${vs.count}").off("change");
                            $("#myselect${vs.count}").change(function (e) {
                                if ($("#myselect${vs.count}").val() == '0' || $("#myselect${vs.count}").val() == '3' || $("#myselect${vs.count}").val() == '4' || $("#myselect${vs.count}").val() == '5') {
                                    $("#edit_a${vs.count}").css("text-decoration", "none")
                                    $("#edit_a${vs.count}").css("color", "#000")
                                    $("#use_drug_select${vs.count}").attr("disabled", true);
                                    var data = crAllList[parseInt(${vs.count}) - 1];
                                    data['Clinical_significance'] = $("#myselect${vs.count}").val();
                                    $("#use_drug_select${vs.count}").val("0");
                                    deleteRecord("${vs.count}");
                                } else {
                                    var data = crAllList[parseInt(${vs.count}) - 1];
                                    $("#edit_a${vs.count}").css("text-decoration", "underline")
                                    $("#edit_a${vs.count}").css("color", "blue")
                                    $("#use_drug_select${vs.count}").attr("disabled", false);
                                    data['Clinical_significance'] = $("#myselect${vs.count}").val();
                                    $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=${lang}", data, function (returnData) {
                                        data['rpCr'] = returnData;
                                        crAllList[parseInt(${vs.count}) - 1] = data;
                                        crAllList[parseInt(${vs.count}) - 1].check_date = returnData.check_date;
                                    });
                                }
                                $("#cr_check_date_${vs.count}").html("未审核");
                            });
                        </script>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.rpCr.Clinical_significance == null or item.rpCr.Clinical_significance == 3 or item.rpCr.Clinical_significance == 4 or item.rpCr.Clinical_significance == 5}">
                                <label id="edit_a${vs.count}">修改</label>
                            </c:when>
                            <c:otherwise>
                                <a id="edit_a${vs.count}"
                                   style="text-decoration: underline; color: blue; cursor: pointer;">修改</a>
                            </c:otherwise>
                        </c:choose>
                        <script>
                            //$("#myselect${vs.count}").off("click");
                            $("#edit_a${vs.count}").click(function (e) {
                                if ($("#myselect${vs.count}").val() == '0' || $("#myselect${vs.count}").val() == '3' || $("#myselect${vs.count}").val() == '4' || $("#myselect${vs.count}").val() == '5') {
                                    return;
                                } else {
                                    showAvdDialog("${vs.count}")
                                }
                            });
                        </script>
                    </td>
                    <td>
                        <label id="cr_check_date_${vs.count}">${item.check_date}</label>
                    </td>
                    <td>
                        <select id="use_drug_select${vs.count}" style="width:90%"
                                <c:if test="${item.rpCr.Clinical_significance == null or item.rpCr.Clinical_significance == 3 or item.rpCr.Clinical_significance == 4 or item.rpCr.Clinical_significance == 5}">disabled</c:if>>
                            <option value="0">不用药</option>
                            <option value="1">用药</option>
                        </select>
                        <script>
                            if ("${item.rpCr.has_drug}" == "true") {
                                $("#use_drug_select${vs.count}").val("1")
                            }
                            $("#use_drug_select${vs.count}").change(function (e) {
                                var use_drug = $("#use_drug_select${vs.count}").val();
                                if (use_drug == '1') {
                                    getOriVariant("${item.Gene}");
                                    showGimDialog("${item.Gene}", "${vs.count}", 0);
                                    //addRecord("${vs.count}");
                                } else {
                                    deleteRecord("${vs.count}");
                                }
                                $("#cr_check_date_${vs.count}").html("未审核");
                            });
                        </script>
                    </td>
                    <td>${item.revel}</td>
                    <td>${item.gnomAD_genome_ALL}</td>
                    <td>${item.Interpro_domain}</td>
                    <td>${item.CLNSIG}</td>
                    <td>${item.HGMD_tag}</td>
                    <td>${item.HGMD_disease}</td>
                    <td>${item.HGMD_pmid}</td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <div>
            <h2 style="color: blue;font-size: 20px;font-weight: bold;">用药突变</h2>
            位点总数: ${mutNum == null ? 0 : mutNum} (SNP:${SNP == null ? 0 : SNP} Indel:${Indel == null ? 0 : Indel}
            Fusion:${Fusion == null ? 0 : Fusion} CNV:${CNV == null ? 0 : CNV})
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
                $("#select2").change(function (e) {
                    var arr = 0;
                    $("input[name='table_cb']:checked").each(function (k, v) {
                        var id = v.id.slice(3);
                        if ($("#result_type_" + id).html() == "未知临床意义") {
                            arr++;
                        }
                    });
                    if (arr > 1) {
                        alert("只能选择一个未知临床意义改为靶向药物！");
                    } else {
                        $.each($("input[name='table_cb']"), function (k, v) {
                            if ($(v).is(':checked')) {
                                //去掉前面三个字符'cb_'
                                var id = v.id.slice(3);
                                if ($("#result_type_" + id).html() != $("#select2").find("option:selected").text()) {

                                    if ($("#select2").val() == 3) {
                                        $("#modify_a_" + id).css("text-decoration", "none");
                                        $("#modify_a_" + id).css("color", "#000")
                                    } else {
                                        $("#modify_a_" + id).css("text-decoration", "underline");
                                        $("#modify_a_" + id).css("color", "blue")
                                    }
                                    if ($("#select2").val() == 2 || $("#select2").val() == 3) {
                                        deleteDrugAndAddUnknownVar(id - 1, $("#select2").val());
                                        $("#result_type_" + id).html($("#select2").find("option:selected").text());
                                        $("#result_type_val_" + id).html($("#select2").find("option:selected").val());
                                    } else if ($("#select2").val() == 1) {
                                        var medicine = medicineList[id - 1];
                                        getOriVariant(medicine.gene);
                                        showGimDialog(medicine.gene, id - 1, 1);
                                    }
                                }
                                $("#result_check_date_" + id).html("未审核");
                                //可能要有隐藏列存value
                            }
                        })
                    }
                });
            </script>
        </div>
        <table class="table table-hover text-center" style="margin-top:20px;width: 80%;" id="table_wz">
            <tbody id="tInfo2" class="my-tbody">
            <tr id="medicine_tr">
                <th><input id="allCb" type="checkbox">选择</th>
                <script>
                    $("#allCb").off('change')
                    $("#allCb").change(function (v) {
                        if ($("#allCb").is(':checked')) {
                            $.each($("input[name='table_cb']"), function (k, v) {
                                $(v).prop("checked", true)
                            })
                        } else {
                            $.each($("input[name='table_cb']"), function (k, v) {
                                $(v).prop("checked", false)
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
                <c:choose>
                    <c:when test="${item.color}">
                        <tr name="medicine_tr" bgcolor="#fcd5b4">
                    </c:when>
                    <c:when test="${item.gene == 'HRD' and hrdState == '阳性'}">
                        <tr id="hrd" name="medicine_tr">
                    </c:when>
                    <c:when test="${item.gene == 'HRD'}">
                        <tr id="hrd" style="display:none" name="medicine_tr">
                    </c:when>
                    <c:otherwise>
                        <tr name="medicine_tr">
                    </c:otherwise>
                </c:choose>
                <td>
                    <c:choose>
                        <c:when test="${!item.has_drug}">
                            <input id="cb_${vs.count}" type="checkbox" name="table_cb"
                                   onclick="initSelect2()">${vs.count}
                        </c:when>
                        <c:otherwise>
                            ${vs.count}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td style="cursor:pointer"><p id="geneText_${vs.count}" onmouseover='showGeneTitle(${vs.count})'
                                              class="gene"
                        <c:if test="${endometrialCarcinoma == true}">
                            <c:if test="${item.gene == 'POLE' or item.gene == 'TP53'}">style="color:red"</c:if>
                        </c:if>>${item.gene}</p>
                    <script type="text/javascript">
                        function showGeneTitle(index) {
                            var data = medicineList[parseInt(index) - 1];
                            if (data.gene == "Complex") {
                                if (data.simple_vars.length > 0) {
                                    var geneTitle = "";
                                    for (var i = 0; i < data.simple_vars.length; i++) {
                                        geneTitle += data.simple_vars[i] + "\n";
                                    }
                                    $("#geneText_" + index).attr('title', geneTitle);
                                }
                            }
                        }
                    </script>
                </td>
                <td class="ori_variant" style="cursor:pointer">
                    <p id="titleText_${vs.count}" onmouseover='showTitle(${vs.count})' title=''
                       onclick='showWindow("${item.gene}","${item.variant}")'>${item.ori_variant}</p>
                    <script type="text/javascript">
                        function showTitle(index) {
                            var data = medicineList[parseInt(index) - 1];
                            $("#titleText_" + index).attr('title', "转录本号：" + (data.transcript || ''));
                        }

                        function showWindow(gene, variant) {
                            window.open("https://www.ncbi.nlm.nih.gov/search/all/?term=" + gene + "%20%20AND%20%20" + variant);
                        }
                    </script>
                        <%-- <lable id="transcript_${vs.count}" style="cursor:pointer">${item.ori_variant}</lable>
							<script>
								$("#transcript_${vs.count}").click(function(e){
									showTranscriptDialog("${vs.count}");
								});
							</script> --%>
                </td>
                <td>
                    <label id="mutFreq_${vs.count}">${item.mutFreq}</label>
                    <script>
                        var mutFreq = $("#mutFreq_${vs.count}").text();
                        var isblood = $("#isblood").val();
                        if (/^\d+(\.\d+)?$/.test(mutFreq) && ((isblood == "true" && mutFreq < 0.5) || (isblood == "false" && mutFreq < 1.0))) {
                            document.getElementById('mutFreq_${vs.count}').style.color = 'red';
                        }
                    </script>
                </td>
                <td style="cursor:pointer"><p
                        style="width:120px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;"
                        title="${item.cosmic}">${item.cosmic}</p>
                </td>
                <td>
                    <label id="result_type_${vs.count}">${item.resultTypeDesc}</label>
                    <script>
                        if (typeof medicineList != "undefined") {
                            $("#result_type_${vs.count}").html(medicineList[parseInt(${vs.count}) - 1].resultTypeDesc);
                        }
                    </script>
                </td>
                <td style="display:none">
                    <label id="result_type_val_${vs.count}">${item.resultTypeVal}</label>
                    <script>
                        if (typeof medicineList != "undefined") {
                            $("#result_type_val_${vs.count}").html(medicineList[parseInt(${vs.count}) - 1].resultTypeVal)
                        }
                    </script>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${item.resultTypeVal == 3}">
                            <label id="modify_a_${vs.count}">修改</label>
                        </c:when>
                        <c:otherwise>
                            <a id="modify_a_${vs.count}"
                               style="text-decoration: underline; color: blue; cursor: pointer;">修改</a>
                        </c:otherwise>
                    </c:choose>
                    <script>
                        $(document).on('click', "#modify_a_${vs.count}", function () {
                            if ($("#result_type_val_${vs.count}").html() == '1') {
                                showTddDialog("${vs.count}")
                            } else if ($("#result_type_val_${vs.count}").html() == '2') {
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
                <td><input type="button" value="置顶" class="btn"/> <input type="button" value="上移" class="btn"/>
                    <input type="button" value="下移" class="btn"/> <c:choose>
                        <c:when test="${!item.has_drug}">
                            <input type="button" value="匹配" onclick="updateDrug(${vs.count})" class="btn"/>
                        </c:when></c:choose></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <script>
            $(document).on('click', "input[value='置顶']", function () {
                if ($(this).parent().parent().index() == 1) {
                    alert("已经到顶啦!");
                    return;
                }
                var aaa = $(this).parent().parent().index();
                //移动元素
                for (var i = 1; i < aaa; i++) {
                    $(this).parent().parent().prev().before($(this).parent().parent());
                }
                $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder",
                    {
                        "analysis_report_id":${ geneticMarkerVwPageBean.report_id },
                        "variant": $(this).parent().parent().find(".gene").text(),
                        "ori_variant": $(this).parent().parent().find(".ori_variant p").text(),
                        "type": 3
                    }
                );
            })
            $(document).on('click', "input[value='上移']", function () {
                //判断是否到顶
                if ($(this).parent().parent().index() == 1) {
                    alert("已经到顶啦!");
                    return;
                }
                //移动元素
                $(this).parent().parent().prev().before($(this).parent().parent());
                $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder",
                    {
                        "analysis_report_id":${ geneticMarkerVwPageBean.report_id },
                        "variant": $(this).parent().parent().find(".gene").text(),
                        "ori_variant": $(this).parent().parent().find(".ori_variant p").text(),
                        "type": 1
                    }
                );
            })
            $(document).on('click', "input[value='下移']", function () {
                //判断是否到底
                if ($(this).parent().parent().index() == $("#table_wz").find("tr").length - 1) {
                    alert("已经到底啦!");
                    return;
                }
                //移动元素
                $(this).parent().parent().next().after($(this).parent().parent())
                $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder",
                    {
                        "analysis_report_id":${geneticMarkerVwPageBean.report_id },
                        "variant": $(this).parent().parent().find(".gene").text(),
                        "ori_variant": $(this).parent().parent().find(".ori_variant p").text(),
                        'type': 2
                    }
                );
            })

            function updateDrug(index) {
                var medicine = medicineList[index - 1];
                var variant = encodeURIComponent(medicine.variant)
                var ori_variant = encodeURIComponent(medicine.ori_variant);
                var cosmic = encodeURIComponent(medicine.cosmic);
                if (cosmic == "undefined") {
                    cosmic = "";
                }
                var mutFreq = encodeURIComponent(medicine.mutFreq);
                if (mutFreq == "undefined") {
                    mutFreq = "0";
                }
                $.post("${pageContext.request.contextPath}/geneMarkerVw/updateFromNkb?userAccount=${user.user_account}&gene=" + medicine.gene + "&variant=" + variant + "&ori_variant=" + ori_variant + "&cosmic=" + cosmic + "&mutFreq=" + mutFreq + "&disease_id=${diseaseId}&lang=${lang}&reportId=${geneticMarkerVwPageBean.report_id}&gender=${sampleFile.gender}", null, function (returnData) {
                    if (!returnData || !returnData.isError) {
                        medicineList[index - 1].drugList = returnData.drugList || [];
                        medicineList[index - 1].varDrugNote = returnData.varDrugNote || [];
                        medicineList[index - 1].clinicalList = returnData.clinicalList || [];
                        medicineList[index - 1].rpUnknownVar = returnData.rpUnknownVar || {};
                        medicineList[index - 1].resultTypeDesc = returnData.resultTypeDesc || '';
                        medicineList[index - 1].resultTypeVal = returnData.resultTypeVal || '';
                        $("#result_type_val_" + index).html(medicineList[index - 1].resultTypeVal);
                        $("#result_type_" + index).html(medicineList[index - 1].resultTypeDesc);
                        alert("匹配成功");
                        $("#result_check_date_" + index).html("未审核");
                    }
                });
            }
        </script>
        <br>
        <c:if test="${geneticMarkerVwPageBean.module == '1'}">
            <div>
                <h2 style="color: blue;font-size: 20px;font-weight: bold;">免疫正负超进展相关基因检测</h2><c:if
                    test="${flag == true}"><input type="button" value="添加" onclick="addMmImmnueAll()"
                                                  class="button border-blue icon-plus-square-o"/></c:if>
                <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                    <tbody id="tInfo9" class="my-tbody">
                    <tr id="immnueAll_tr">
                        <th>免疫项目</th>
                        <th>基因名称</th>
                        <th>突变形式</th>
                        <th>变异丰度</th>
                        <th>循证医学证据</th>
                        <th>操作</th>
                    </tr>
                    <c:forEach items="${mmImmnueAlls}" var="item" varStatus="vs">
                        <tr id="immnueAll_tr_${vs.count}">
                            <td><label id="ia_flag_${vs.count}"><c:choose><c:when
                                    test="${item.flag == '1'}">正相关</c:when><c:when
                                    test="${item.flag == '2'}">负相关</c:when><c:when
                                    test="${item.flag == '3'}">"超进展"</c:when></c:choose></label></td>
                            <td><label id="ia_gene_${vs.count}">${item.gene}</label></td>
                            <td><label id="ia_variant_${vs.count}">${item.variant}</label></td>
                            <td><label id="ia_mutFreq_${vs.count}">${item.mutFreq}</label></td>
                            <td><textarea id="ia_varDesc_${vs.count}"
                                          style="width: 319px; height: 80px;">${item.varDesc}</textarea></td>
                            <td><input type="button" value="保存" onclick="updateMmImmnueAll(${vs.count})" class="btn"/>
                                <input type="button" value="删除" onclick="deleteMmImmnueAll(${vs.count})" class="btn"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <script>
                    function addMmImmnueAll() {
                        showMiadDialog();
                    }

                    function updateMmImmnueAll(i) {
                        var flag_desc = $("#ia_flag_" + i).text();
                        var flag = "";
                        if (flag_desc == "正相关") {
                            flag = 1;
                        } else if (flag_desc == "负相关") {
                            flag = 2;
                        } else if (flag_desc == "超进展") {
                            flag = 3;
                        }
                        var gene = $("#ia_gene_" + i).text();
                        var variant = $("#ia_variant_" + i).text();
                        var mutFreq = $("#ia_mutFreq_" + i).text();
                        var varDesc = $("#ia_varDesc_" + i).val();
                        $.ajax({
                            url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmImmnueAll",
                            type: "POST",
                            data: {
                                "report_id":${geneticMarkerVwPageBean.report_id},
                                "flag": flag,
                                "gene": gene,
                                "variant": variant,
                                "mutFreq": mutFreq,
                                "varDesc": varDesc,
                                "update_by": "${user.user_account}"
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result) {
                                    swal("成功！", "修改成功", "success");
                                } else {
                                    swal("失败！", "修改失败", "error");
                                }
                            }
                        });
                    }

                    function deleteMmImmnueAll(i) {
                        var flag_desc = $("#ia_flag_" + i).text();
                        var flag = "";
                        if (flag_desc == "正相关") {
                            flag = 1;
                        } else if (flag_desc == "负相关") {
                            flag = 2;
                        } else if (flag_desc == "超进展") {
                            flag = 3;
                        }
                        var gene = $("#ia_gene_" + i).text();
                        var variant = $("#ia_variant_" + i).text();
                        var mutFreq = $("#ia_mutFreq_" + i).text();
                        $.ajax({
                            url: "${pageContext.request.contextPath}/geneMarkerVw/deleteMmImmnueAll",
                            type: "POST",
                            data: {
                                "report_id":${geneticMarkerVwPageBean.report_id},
                                "flag": flag,
                                "gene": gene,
                                "variant": variant,
                                "mutFreq": mutFreq
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result) {
                                    $("#immnueAll_tr_" + i).remove();
                                    swal("成功！", "删除成功", "success");
                                } else {
                                    swal("失败！", "删除失败", "error");
                                }
                            }
                        });
                    }
                </script>
            </div>
            <br>
            <c:if test="${approvedDrugFlag == true}">
                <div>
                    <h2 style="color: blue;font-size: 20px;font-weight: bold;">本癌种FDA/NMPA获批的其他可选靶向药物</h2>
                    <input type="button" value="添加" onclick="addMmApprovedDrug()"
                           class="button border-blue icon-plus-square-o"/>
                    <table class="table table-hover text-center" style="margin-top:20px;width: 80%;">
                        <tbody id="tInfo15" class="my-tbody">
                        <tr id="approvedDrug_tr">
                                <%--<th><input id="allCb" type="checkbox">选择</th>--%>
                            <th>癌种</th>
                            <th>药物</th>
                            <th>获批适应症</th>
                            <th>获批机构</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${approvedDrugData}" var="item" varStatus="vs">
                            <tr id="approvedDrug_tr_${vs.count}">
                                    <%--<td><input id="cb_${vs.count}" type="checkbox" name="table_cb" onclick="initSelect2()">${vs.count}<td>--%>
                                <td><label id="add_disease_${vs.count}">${item.disease}</label></td>
                                <td><label id="add_drug_${vs.count}">${item.drug}</label></td>
                                <td><textarea id="add_indication_${vs.count}"
                                              style="width: 319px; height: 80px;">${item.indication}</textarea></td>
                                <td>
                                    <select id="add_institution_${vs.count}">
                                        <option value="FDA" <c:if test="${item.institution == 'FDA'}">selected</c:if>>
                                            FDA
                                        </option>
                                        <option value="NMPA" <c:if test="${item.institution == 'NMPA'}">selected</c:if>>
                                            NMPA
                                        </option>
                                        <option value="FDA/NMPA"
                                                <c:if test="${item.institution == 'FDA/NMPA'}">selected</c:if>>FDA/NMPA
                                        </option>
                                    </select>
                                </td>
                                <td><input type="button" value="保存" onclick="updateMmApprovedDrug(${vs.count})"
                                           class="btn"/> <input type="button" value="删除"
                                                                onclick="deleteMmApprovedDrug(${vs.count})"
                                                                class="btn"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <script>
                        function addMmApprovedDrug() {
                            showMaddDialog();
                        }

                        function updateMmApprovedDrug(i) {
                            var disease = $("#add_disease_" + i).text();
                            var drug = $("#add_drug_" + i).text();
                            var indication = $("#add_indication_" + i).val();
                            var institution = $("#add_institution_" + i).val();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/updateMmApprovedDrug",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "disease": disease,
                                    "drug": drug,
                                    "indication": indication,
                                    "institution": institution,
                                    "update_by": "${user.user_account}"
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        swal("成功！", "修改成功", "success");
                                    } else {
                                        swal("失败！", "修改失败", "error");
                                    }
                                }
                            });
                        }

                        function deleteMmApprovedDrug(i) {
                            var disease = $("#add_disease_" + i).text();
                            var drug = $("#add_drug_" + i).text();
                            $.ajax({
                                url: "${pageContext.request.contextPath}/geneMarkerVw/deleteMmApprovedDrug",
                                type: "POST",
                                data: {
                                    "report_id":${geneticMarkerVwPageBean.report_id},
                                    "disease": disease,
                                    "drug": drug
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result) {
                                        $("#approvedDrug_tr_" + i).remove();
                                        swal("成功！", "删除成功", "success");
                                    } else {
                                        swal("失败！", "删除失败", "error");
                                    }
                                }
                            });
                        }
                    </script>
                </div>
                <br>
            </c:if>
        </c:if>
        <br>
    </div>
    <div id="meditationPoints-show"
         style="margin-top:30px; margin-left:5%; width:60%; overflow-y: scroll; height: 600px; padding: 10px;">
        <div id="sample-info" style="clear:both">
            <div style="text-align:center">
                <lable style="color:#4BAD5B; font-weight: bold; font-size: 20px;">样本信息</lable>
            </div>
            <table id="sample-info-table" class="preview-content-table">
                <tr>
                    <td style="border-top-width: 5px;">姓名：<c:out value="${sampleFile.person_name}"
                                                                   default="-"></c:out></td>
                    <td style="border-top-width: 5px;">送检医院：<c:out value="${sampleFile.hospital}"
                                                                       default="-"></c:out></td>
                    <td style="border-top-width: 5px;">样本条码：<c:out value="${sampleFile.subbarcode}"
                                                                       default="-"></c:out></td>
                </tr>
                <tr>
                    <td>性别：<c:out value="${sampleFile.gender}" default="-"></c:out></td>
                    <td>送检科室：<c:out value="${sampleFile.locationname}" default="-"></c:out></td>
                    <td>样本编号：<c:out value="${sampleFile.barcode}" default="-"></c:out></td>
                </tr>
                <tr>
                    <td>年龄：<c:out value="${sampleFile.age}" default="-"></c:out></td>
                    <td>送检医生：<c:out value="${sampleFile.doctorname}" default="-"></c:out></td>
                    <td>门检/住院号：-</td>
                </tr>
                <tr>
                    <td>联系电话：<c:out value="${sampleFile.patient_phone}" default="-"></c:out></td>
                    <td>样本类型：
                        <c:choose>
                            <c:when test="${sampleFile.sample_type == 'tissue'}"><!-- 如果 -->
                                <c:out value="组织"></c:out>
                            </c:when>
                            <c:when test="${sampleFile.sample_type == 'blood'}"><!-- 如果 -->
                                <c:out value="血液"></c:out>
                            </c:when>
                            <c:otherwise> <!-- 否则 -->
                                <c:out value="${sampleFile.sample_type}"></c:out>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>送检时间：<c:out value="${sampleFile.commission_date}" default="-"></c:out></td>
                </tr>
                <tr>
                    <td>临床诊断：<c:out value="${diseaseName}" default="-"></c:out></td>
                    <td>订单编号：<c:out value="-" default="-"></c:out></td>
                    <td>接收日期：<c:out value="${sampleFile.received_date}" default="-"></c:out></td>
                </tr>
            </table>

        </div>
        <div id="detectResult-drugTip-info" style="margin-top:20px">
            <div style="text-align:center">
                <lable style="color:	#4BAD5B; font-weight: bold; font-size: 20px;">检测结果及用药提示</lable>
            </div>
            <div id="target-drug-info" style="margin-top:10px">
                <div style="text-align:center">
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">◆靶向药物用药提示</lable>
                </div>
                <table id="target-drug-table" class="preview-content-table">
                    <tr>
                        <th rowspan="2" style="width:10%">
                            突变基因
                        </th>
                        <th rowspan="2" style="width:30%">
                            检测结果
                        </th>
                        <th rowspan="2">
                            突变丰度
                        </th>
                        <th colspan="3">
                            可能获益药物
                        </th>
                        <th rowspan="2" style="width: 14%;">
                            可能耐药信息
                        </th>
                    </tr>
                    <tr>
                        <th style="border-top-width: 1px;    width: 10%;">A级</th>
                        <th style="border-top-width: 1px;    width: 10%;">B级</th>
                        <th style="border-top-width: 1px;    width: 10%;">C级</th>
                    </tr>
                </table>
                <div id="unknown-mut-info" style="margin-top: 10px;">
                    <div>
                        <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">未知临床意义的基因突变
                        </lable>
                    </div>
                    <table id="unknown-mut-table" class="preview-content-table">
                        <tr>
                            <th>
                                基因名称
                            </th>
                            <th>
                                检测结果
                            </th>
                            <th>
                                突变类型
                            </th>
                            <th>
                                突变丰度
                            </th>
                        </tr>
                    </table>
                </div>
            </div>

            <div id="immunity-drug-info" style="margin-top:10px">
                <div style="text-align:center">
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">◆免疫药物用药提示</lable>
                </div>
                <div style="margin-top:10px">
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">肿瘤突变负荷(bTMB)检测结果
                    </lable>
                </div>
                <table id="bTMB-table" class="preview-content-table">
                    <tr>
                        <th>
                            检测项目
                        </th>
                        <th>
                            检测结果
                        </th>
                    </tr>
                    <tr>
                        <td>肿瘤突变负荷（Tumor mutation burden, TMB）</td>
                        <td>${TMB}Muts/Mb</td>
                    </tr>
                </table>
                <div style="margin-top:10px">
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">微卫星不稳定 (MSI) 检测结果
                    </lable>
                </div>
                <table id="MSI-table" class="preview-content-table">
                    <tr>
                        <th>
                            检测项目
                        </th>
                        <th>
                            检测结果
                        </th>
                    </tr>
                    <tr>
                        <td>微卫星不稳定 (microsatellite instability,MSI）</td>
                        <td>${MSI}</td>
                    </tr>
                </table>
            </div>
            <div id="chemo-drug-info" style="margin-top:10px">
                <div style="text-align:center">
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">◆化疗药物用药提示</lable>
                </div>
                <div>
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">
                        化疗药物毒副作用风险及有效性预测
                    </lable>
                </div>
                <table id="chemo-sideeffects-effectiveness-table" class="preview-content-table">

                </table>
            </div>
            <div id="genetic-cancer-risk-info" style="margin-top:10px">
                <div style="text-align:center">
                    <lable style="color:	#4BAD5B; font-weight: bold; font-size: 16px;">◆肿瘤遗传风险检测</lable>
                </div>
                <table id="genetic-cancer-risk-table" class="preview-content-table">
                    <tr>
                        <th>基因</th>
                        <th>染色体</th>
                        <th>外显子</th>
                        <th>核苷酸</th>
                        <th>氨基酸</th>
                        <th>杂合/纯合</th>
                        <th>突变类型</th>
                        <th>千人频率</th>
                        <th>临床意义</th>
                    </tr>
                    <c:forEach items="${crAllList}" var="item" varStatus="vs">
                        <tr>
                            <td>${item.Gene}</td>
                            <td>${item.Chr}</td>
                            <td>${item.Exon}</td>
                            <td>${item.cHGVS}</td>
                            <td>${item.pHGVS}</td>
                            <td>${item.Zygosity}</td>
                            <td>${item.ExonicFunc}</td>
                            <td>${item.c1000g2015aug_all}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        <div id="detect-result-analysis-info" style="margin-top:10px">
            <div style="text-align:center">
                <lable style="color:	#5DA1A6; font-weight: bold; font-size: 20px;">基因检测结果解析</lable>
            </div>
            <div style="text-align:center; margin-top: 10px;">
                <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">◆靶向药物检测解析</lable>
            </div>
            <div id="target-drug-analysis-tables">
            </div>
            <div style="margin-top:20px">
                <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">未知临床意义基因突变解析</lable>
            </div>
            <div id="unknown-gene-analysis-tables">
            </div>
            <div style="text-align:center; margin-top: 10px;">
                <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">◆化疗药物检测解析</lable>
            </div>
            <div style="margin-top:20px">
                <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">化疗药物毒副作用解析</lable>
            </div>
            <table id="chemo-sideeffects-analysis-tables" class="preview-content-table2">
            </table>
            <div style="margin-top:20px">
                <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">化疗药物有效性解析</lable>
            </div>
            <table id="chemo-effectiveness-analysis-tables" class="preview-content-table2">
            </table>
            <div id="cancer-risk-analysis-div">
                <div style="text-align:center; margin-top: 20px;">
                    <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">◆遗传变异检测解析</lable>
                </div>
                <div>
                    <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">遗传风险相关基因检测结果解析
                    </lable>
                </div>
                <div id="cancer-risk-analysis-tables">
                </div>
            </div>
        </div>
        <br>
    </div>
</div>
<!-- 以下是各个浮层弹框的内容 -->
<div title="变异解析" id="analytical_variation_dialog" name="analytical_variation_dialog"
     style="display: none;  background-color:darkgrey">
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">基因名称</label>
        <input class="input" style="width: 87%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="avd_gene_name" value="" readonly/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">突变形式</label>
        <input class="input" style="width: 87%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="avd_mut_style" value="" readonly/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">突变说明</label>
        <textarea class="textarea" style="width: 87%; margin-left: 5%; height: 60px;" id="avd_mut_desc"
                  readonly></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">基因说明</label>
        <textarea style="width: 87%; margin-left: 5%; height: 60px; resize:vertical;" id="avd_gene_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">变异解析</label>
        <textarea style="width: 87%; margin-left: 5%; height: 100px; resize:vertical;" id="avd_mut_analysis"></textarea>
    </div>
</div>
<div id="unkonw_clinical_dialog" name="unkonw_clinical_dialog" style="display: none;  background-color:darkgrey">
    <div style="float:left;line-height:50px;">请选择要关联的父级突变(可不选)：</div>
    <input type="text" class="input w50" id="ori_variant"/>
    <input type="hidden" id="gene_variant_id" name="gene_variant_id"/>
    <script type="text/javascript">
        function getOriVariant(gene) {
            $.post("${pageContext.request.contextPath}/autoComplete/getGeneVariant",
                {"gene": gene},
                function (data) {
                    $("#ori_variant").flushCache();
                    /*let name = data.map(item => item.name);
	        			if (name.includes(gene + " Active Mutation")) {
							$('#ori_variant').val(gene + " Active Mutation,");
						}*/
                    $('#ori_variant').autocomplete(data, {
                        max: data.length, //列表里的条目数
                        minChars: 0, //自动完成激活之前填入的最小字符
                        width: 288, //提示的宽度，溢出隐藏
                        scrollHeight: 300, //提示的高度，溢出显示滚动条
                        matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                        autoFill: false, //自动填充
                        multiple: true,
                        multipleSeparator: ",",
                        formatItem: function (row, i, max) {
                            return row.name;
                        },
                        formatResult: function (row) {
                            return row.name;
                        }
                    }).result(function (event, row, formatted) {
                    });
                }, "json");
        }
    </script>
</div>
<div title="靶向药物" id="target_drug_dialog" name="target_drug_dialog"
     style="display: none;  background-color:darkgrey">
    <div>
        <button class="button" onclick="switchContent(1)">突变信息</button>
        <button class="button" onclick="switchContent(2)">靶向药物</button>
        <button class="button" onclick="switchContent(3)">临床试验药物</button>
    </div>
    <div id="content1" name="content" style="display:block; margin-top:20px">
        <div style="margin-top:0px;">
            <label style="float:left;margin-top: 5px;">基因名称</label>
            <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
                   id="tdd_gene_name_1" value="" readonly/>
        </div>
        <div style="clear:both"></div>
        <div style="margin-top:20px;">
            <label style="float:left;margin-top: 5px;">突变形式</label>
            <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
                   id="tdd_mut_style" value="" readonly/>
        </div>
        <div style="clear:both"></div>
        <div style="margin-top:20px;">
            <label style="float:left">突变丰度</label>
            <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
                   id="tdd_mut_freq" value="" readonly/>
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
                <div style="line-height:25px;">突变说明:</div>
                <textarea
                        style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;"
                        readonly id="drug8" name="突变说明:"></textarea>
                <div style="line-height:25px;">位点说明:</div>
                <textarea
                        style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:60px;overflow: auto;word-break: break-all;"
                        id="drug3" name="位点说明:"></textarea>
                <div style="line-height:25px;">NCCN指南:</div>
                <textarea
                        style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;"
                        id="drug4" name="NCCN指南:"></textarea>
                <!-- <div style="line-height:25px;">预后和诊断说明:</div><textarea style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;" id="drug5" name="预后和诊断说明:" ></textarea> -->
                <div style="line-height:25px;">用药说明:</div>
                <textarea
                        style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:90px;overflow: auto;word-break: break-all;"
                        id="drug6" name="用药说明:"></textarea>
                <div style="line-height:25px;">耐药说明:</div>
                <textarea
                        style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:40px;overflow: auto;word-break: break-all;"
                        id="drug7" name="耐药说明:"></textarea>
                <div id="recommend1"></div>
                <input type="hidden" id="recommend" name="recommend:" value="">
            </form>

            <script>
                $("#tdd_medicine_desc").keyup('textarea', function (e) {
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
                <button style="width:100px" onclick="addDrug1()">增加</button>
                <br>
                <button style="width:100px; margin-top:5px" onclick="deleteDrug1()">删除</button>
                <br>
                <button style="width:100px; margin-top:5px" onclick="getDrug1()">抓取药物信息</button>
                <br>
                <button style="width:100px; margin-top:5px" onclick="saveDrug1()">保存</button>
            </div>
        </div>
        <div style="float:left;width: 70%;margin-left: 20px;">
            <div style="margin-top:0px;">
                <label style="float:left;margin-top: 5px; width: 15%;">药物名称</label>
                <input class="input" style="width: 85%; margin-left: 20px; height: 25px; margin-right:0;" type="text"
                       id="c2_tdd_drug_name" value=""/>
                <script>
                    $.post("${pageContext.request.contextPath}/autoComplete/getDrugNameAndDrugId", {lang: "${lang}"}, function (data) {
                        $('#c2_tdd_drug_name').autocomplete(data, {
                            max: 30, //列表里的条目数
                            minChars: 0, //自动完成激活之前填入的最小字符
                            width: 350, //提示的宽度，溢出隐藏
                            scrollHeight: 300, //提示的高度，溢出显示滚动条
                            matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                            autoFill: false, //自动填充
                            formatItem: function (row, i, max) {
                                return row.name;
                            },
                            formatResult: function (row) {
                                return row.name;
                            }
                        }).result(function (event, row, formatted) {
                            /* if(listIndex > 0) {
									$("#c2_li_"+listIndex).html($(this).val());
									console.log($(this).val());
									console.log(drugList[listIndex-1].drug_name);
									drugList[listIndex-1].old_drug_name = drugList[listIndex-1].drug_name;
									drugList[listIndex-1].drug_name = row.name;
									drugList[listIndex-1].drug_id = row.id;
									if(!drugList[listIndex-1].status || drugList[listIndex-1].status != 'add'){
										drugList[listIndex-1].status = "update";
									}
									getDrug1();
								} */
                        });
                    }, "json");
                    $("#c2_tdd_drug_name").off("change");
                    $("#c2_tdd_drug_name").blur(function (e) {
                        if (listIndex > 0) {
                            $("#c2_li_" + listIndex).html($(this).val());
                            drugList[listIndex - 1].old_drug_name = $(this).val();
                            drugList[listIndex - 1].drug_name = $(this).val();
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            // getDrug1();
                        }
                    })
                </script>
            </div>
            <div style="clear:both"></div>
            <div style="margin-top:20px;">
                <label style="float:left;margin-top: 5px; width: 15%;">癌种名称</label>
                <input class="input" style="width: 85%; margin-left: 20px; height: 25px; margin-right:0;" type="text"
                       id="c2_tdd_anno_disease_name"/>
                <script>
                    $.post("${pageContext.request.contextPath}/autoComplete/getDiseaseNameAndDiseaseId", {"primary_cancer_id": $("#primary_cancer_id").val()}, function (data) {
                        $('#c2_tdd_anno_disease_name').autocomplete(data, {
                            max: 30, //列表里的条目数
                            minChars: 0, //自动完成激活之前填入的最小字符
                            width: 350, //提示的宽度，溢出隐藏
                            scrollHeight: 300, //提示的高度，溢出显示滚动条
                            matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                            autoFill: false, //自动填充
                            formatItem: function (row, i, max) {
                                return row.name;
                            },
                            formatResult: function (row) {
                                return row.name;
                            }
                        }).result(function (event, row, formatted) {
                        });
                    }, "json");
                    $("#c2_tdd_anno_disease_name").off("change");
                    $("#c2_tdd_anno_disease_name").blur(function (e) {
                        if (listIndex > 0) {
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            drugList[listIndex - 1].anno_disease_name = $(this).val();
                        }
                    })
                </script>
            </div>
            <div style="clear:both"></div>
            <div style="margin-top:20px;">
                <label style="float:left;margin-top: 5px; width: 15%;">证据阶段</label>
                <input class="input" style="width: 85%; margin-left: 20px; height: 25px; margin-right:0;" type="text"
                       id="c2_tdd_evidence_phase"/>
                <script>
                    $.post("${pageContext.request.contextPath}/autoComplete/getEvidencePhaseNameAndEvidencePhaseId", function (data) {
                        $('#c2_tdd_evidence_phase').autocomplete(data, {
                            max: 30, //列表里的条目数
                            minChars: 0, //自动完成激活之前填入的最小字符
                            width: 350, //提示的宽度，溢出隐藏
                            scrollHeight: 300, //提示的高度，溢出显示滚动条
                            matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                            autoFill: false, //自动填充
                            formatItem: function (row, i, max) {
                                return row.name;
                            },
                            formatResult: function (row) {
                                return row.name;
                            }
                        }).result(function (event, row, formatted) {
                        });
                    }, "json");
                    $("#c2_tdd_evidence_phase").off("change");
                    $("#c2_tdd_evidence_phase").blur(function (e) {
                        if (listIndex > 0) {
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            drugList[listIndex - 1].evidence_phase = $(this).val();
                        }
                    })
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
                    <option value="5">A级耐药</option>
                    <option value="6">B级耐药</option>
                    <option value="7">C级耐药</option>
                    <option value="8">D级耐药</option>
                </select>
                <script>
                    $("#approve_range_select").off("change");
                    $("#approve_range_select").change(function (e) {
                        if (listIndex > 0) {
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            drugList[listIndex - 1].approve_range = $(this).val();
                            if ($(this).val() == 1) {
                                $("#c2_li_" + listIndex).css("color", "#000000");
                            }
                            if ($(this).val() == 2) {
                                $("#c2_li_" + listIndex).css("color", "#0000CD");
                            }
                            if ($(this).val() == 3) {
                                $("#c2_li_" + listIndex).css("color", "#008B00");
                            }
                            if ($(this).val() == 4) {
                                $("#c2_li_" + listIndex).css("color", "#CD8500");
                            }
                            if ($(this).val() == 5) {
                                $("#c2_li_" + listIndex).css("color", "#FF0000");
                            }
                            if ($(this).val() == 6) {
                                $("#c2_li_" + listIndex).css("color", "#FFB6C1");
                            }
                            if ($(this).val() == 7) {
                                $("#c2_li_" + listIndex).css("color", "#800080");
                            }
                            if ($(this).val() == 8) {
                                $("#c2_li_" + listIndex).css("color", "#00FFFF");
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
                    $("#isCFDA").change(function (e) {
                        if (listIndex > 0) {
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            var drug_name = $("#c2_tdd_drug_name").val();
                            if ($(this).is(':checked')) {
                                if ($("#recruit_select").val() == '1') {
                                    $("#c2_li_" + listIndex).html(drug_name + "*#");
                                } else {
                                    $("#c2_li_" + listIndex).html(drug_name + "*");
                                }
                                drugList[listIndex - 1].cfda = "1";
                            } else {
                                if ($("#recruit_select").val() == '1') {
                                    $("#c2_li_" + listIndex).html(drug_name + "#");
                                } else {
                                    $("#c2_li_" + listIndex).html(drug_name);
                                }
                                drugList[listIndex - 1].cfda = "0";
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
                    $("#recruit_select").change(function (e) {
                        if (listIndex > 0) {
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            var drug_name = $("#c2_tdd_drug_name").val();
                            drugList[listIndex - 1].recruiting = $(this).val();
                            if ($(this).val() == 1) {
                                if ($("#isCFDA").is(':checked')) {
                                    $("#c2_li_" + listIndex).html(drug_name + "*#");
                                } else {
                                    $("#c2_li_" + listIndex).html(drug_name + "#");
                                }
                            }
                            if ($(this).val() == 0) {
                                if ($("#isCFDA").is(':checked')) {
                                    $("#c2_li_" + listIndex).html(drug_name + "*");
                                } else {
                                    $("#c2_li_" + listIndex).html(drug_name);
                                }
                            }
                        }
                    })
                </script>
            </div>
            <div style="clear:both"></div>
            <div style="margin-top:20px;">
                <label style="float:left; width: 15%;">适应症</label>
                <textarea class="textarea" style="width: 85%;  height: 200px;resize:vertical;"
                          id="tdd_indication"></textarea>
                <script>
                    $("#tdd_indication").off("change");
                    $("#tdd_indication").change(function (e) {
                        if (listIndex > 0) {
                            if (!drugList[listIndex - 1].status || drugList[listIndex - 1].status != 'add') {
                                drugList[listIndex - 1].status = "update";
                            }
                            drugList[listIndex - 1].approval_desc = $(this).val();
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
                <button style="width:120px" onclick="addDrug2()">增加</button>
                <br>
                <button style="width:120px; margin-top:5px" onclick="deleteDrug2()">删除</button>
                <br>
                <button style="width:120px; margin-top:5px" onclick="getDrug2()">抓取临床试验信息</button>
                <br>
                <button style="width:120px; margin-top:5px" onclick="saveDrug2()">保存</button>
            </div>
        </div>
        <div style="float:left;width: 80%;margin-left: 20px;">
            <div style="margin-top:0px;">
                <label style="float:left;margin-top: 5px; width: 10%;">临床试验ID</label>
                <input class="input" style="width: 75%; margin-left: 0%; height: 25px;float:left;" type="text"
                       id="clinical_trial_id" value=""/>
                <button id="clinical_tria_btn" style="width:10%;margin-left:5%;height:25px;">查看</button>
                <script>
                    $('#clinical_tria_btn').unbind('click').click(function () {
                        if ($("#clinical_trial_id").val()) {
                            window.open("https://clinicaltrials.gov/ct2/show/" + $("#clinical_trial_id").val(), "_blank");
                        } else {
                            alert("请选择药物！");
                        }
                    });
                </script>
                <script>
                    $("#clinical_trial_id").off("change");
                    $("#clinical_trial_id").change(function (e) {
                        if (clinicalIndex > 0) {
                            $("#c3_li_" + clinicalIndex).html($("#c3_tdd_drug_name").val() + "-" + $(this).val());
                            clinicalList[clinicalIndex - 1].old_clinical_trial_id = clinicalList[clinicalIndex - 1].clinical_trial_id;
                            clinicalList[clinicalIndex - 1].clinical_trial_id = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                            getDrug2();
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%;">药物名称</label>
                <input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text"
                       id="c3_tdd_drug_name" value=""/>
                <script>
                    $("#c3_tdd_drug_name").off("change");
                    $("#c3_tdd_drug_name").change(function (e) {
                        if (clinicalIndex > 0) {
                            $("#c3_li_" + clinicalIndex).html($(this).val() + "-" + $("#clinical_trial_id").val());
                            clinicalList[clinicalIndex - 1].drug_name = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%;">临床试验名称</label>
                <input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="title_chinese"
                       value=""/>
                <script>
                    $("#title_chinese").off("change");
                    $("#title_chinese").change(function (e) {
                        if (clinicalIndex > 0) {
                            clinicalList[clinicalIndex - 1].title = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%;">肿瘤类型</label>
                <input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text"
                       id="condition_chinese" value=""/>
                <script>
                    $("#condition_chinese").off("change");
                    $("#condition_chinese").change(function (e) {
                        if (clinicalIndex > 0) {
                            clinicalList[clinicalIndex - 1].recruiting_condition = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%;">临床试验阶段</label>
                <input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text" id="phase"
                       value=""/>
                <script>
                    $("#phase").off("change");
                    $("#phase").change(function (e) {
                        if (clinicalIndex > 0) {
                            clinicalList[clinicalIndex - 1].phase = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%;">临床试验地点</label>
                <input class="input" style="width: 90%; margin-left: 0%; height: 25px;" type="text"
                       id="location_chinese" value=""/>
                <script>
                    $("#location_chinese").off("change");
                    $("#location_chinese").change(function (e) {
                        if (clinicalIndex > 0) {
                            clinicalList[clinicalIndex - 1].location = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%; ">入组标准</label>
                <textarea style="width: 90%; margin-left: 0%; height: 75px; resize:vertical;"
                          id="inclusion_criteria"></textarea>
                <script>
                    $("#inclusion_criteria").off("change");
                    $("#inclusion_criteria").change(function (e) {
                        if (clinicalIndex > 0) {
                            clinicalList[clinicalIndex - 1].inclusion_criteria = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
            <div style="margin-top:10px;">
                <label style="float:left;margin-top: 5px; width: 10%; ">排除标准</label>
                <textarea style="width: 90%; margin-left: 0%; height: 75px; resize:vertical;"
                          id="exclusion_criteria"></textarea>
                <script>
                    $("#exclusion_criteria").off("change");
                    $("#exclusion_criteria").change(function (e) {
                        if (clinicalIndex > 0) {
                            clinicalList[clinicalIndex - 1].exclusion_criteria = $(this).val();
                            if (!clinicalList[clinicalIndex - 1].status || clinicalList[clinicalIndex - 1].status != 'add') {
                                clinicalList[clinicalIndex - 1].status = "update";
                            }
                        }
                    })
                </script>
            </div>
        </div>
    </div>
</div>
<div title="未知临床意义" id="unknow_clinical_meaning_dialog" name="unknow_clinical_meaning_dialog"
     style="display: none; background-color:darkgrey;">
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">基因名称</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="ucmd_gene_name" value="" readonly/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">突变形式</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="ucmd_mut_style" value="" readonly/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">突变说明</label>
        <textarea class="textarea" style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="ucmd_mut_desc" readonly></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">基因说明</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;" id="ucmd_gene_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">用药说明</label>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;margin-left:30px">
        <form id="ucmd_medicine_desc">
            <div style="line-height:25px;">预后和诊断说明:</div>
            <textarea
                    style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:70px;overflow: auto;word-break: break-all;"
                    id="ucmd_medicine1" name="预后和诊断说明:"></textarea>
            <div style="line-height:25px;">用药说明:</div>
            <textarea
                    style="width: 92%;outline: 0 none; margin-left: 75px;resize:vertical; height:70px;overflow: auto;word-break: break-all;"
                    id="ucmd_medicine2" name="用药说明:"></textarea>
        </form>
    </div>
    <script>
        $("#ucmd_medicine_desc,#ucmd_gene_desc").off("change");
        $("#ucmd_medicine_desc,#ucmd_gene_desc").change(function (e) {
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
<div title="肉瘤辅助诊断" id="mm_sarcoma_typings_dialog" name="mm_sarcoma_typings_dialog"
     style="display: none; background-color:darkgrey;">
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">突变形式</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mstd_mutation_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">转录本号</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mstd_transcript_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">变异丰度</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" id="mstd_mutFreq_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">肉瘤亚型</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mstd_sarcoma_subtype_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">证据等级</label>
        <select id="mstd_evidence_desc" style="margin-left: 5%;">
            <option value="WHO">WHO</option>
            <option value="NCCN">NCCN</option>
            <option value="CSCO">CSCO</option>
            <option value="专家共识">专家共识</option>
        </select>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">突变形式</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;"
               id="mstd_ori_variant_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">突变说明</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mstd_mutDesc2_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">变异解析</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mstd_mutationAnalysis_desc"></textarea>
    </div>
</div>
<div title="淋巴瘤辅助分型及预后相关提示" id="mm_lymphoma_typings_dialog" name="mm_lymphoma_typings_dialog"
     style="display: none; background-color:darkgrey;">
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">突变基因</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mltd_gene_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">检测结果</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mltd_ori_variant_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">变异丰度</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" id="mltd_mutFreq_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">辅助分型相关的淋巴瘤亚型：</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mltd_lymphoma_subtype_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">预后相关的淋巴瘤亚型：</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mltd_lymphoma_subtype2_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">证据等级</label>
        <select id="mltd_evidence_desc" style="margin-left: 5%;">
            <option value="WHO">WHO</option>
            <option value="NCCN">NCCN</option>
            <option value="CSCO">CSCO</option>
            <option value="专家共识">专家共识</option>
        </select>
    </div>
</div>
<div title="甲状腺癌预后评估" id="mm_thyroid_prognoses_dialog" name="mm_thyroid_prognoses_dialog"
     style="display: none; background-color:darkgrey;">
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">突变基因</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mtpd_gene_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">检测结果</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mtpd_ori_variant_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">变异丰度</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" id="mtpd_mutFreq_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">预后评估：</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mtpd_prognosis_evaluation_desc"></textarea>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left">预后说明：</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;"
                  id="mtpd_prognosis_assessment_desc"></textarea>
    </div>
</div>
<div title="错配修复（MMR）相关基因检测结果" id="mm_dmmrs_dialog" name="mm_dmmrs_dialog"
     style="display: none; background-color:darkgrey;">
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">突变基因</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mdd_gene_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">检测结果</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="mdd_ori_variant_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">变异丰度</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" id="mdd_mutFreq_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">突变类型</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" id="mdd_mut_type_desc"/>
    </div>
</div>
<div title="本癌种FDA/NMPA获批的其他可选靶向药物" id="mm_approved_drugs_dialog" name="mm_approved_drugs_dialog"
     style="display: none; background-color:darkgrey;">
    <%--<input type="hidden" id="madd_approved_id_desc" >
		<div style="margin-top:0px;">
			<label style="float:left;margin-top: 5px;">癌种-药物</label>
			<input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text" id="madd_diseaseAndDrug_desc"/>
		</div>
		<div style="clear:both"></div>
		<div style="margin-top:20px;">
			<label style="float:left">获批适应症：</label>
			<textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;" id="madd_indication_desc" readonly></textarea>
		</div>
		<div style="margin-top:20px;">
			<label style="float:left">获批机构</label>
			<select id="madd_institution_desc" style="margin-left: 5%;" readonly>
				<option value="FDA">FDA</option>
				<option value="NMPA">NMPA</option>
				<option value="FDA/NMPA">FDA/NMPA</option>
			</select>
		</div>--%>
    <div style="margin-top:0px;">
        <label style="float:left;margin-top: 5px;">癌种</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="madd_disease_desc"/>
    </div>
</div>
<div title="免疫正负超进展相关基因检测" id="mm_immnue_alls_dialog" name="mm_immnue_alls_dialog"
     style="display: none; background-color:darkgrey;">
    <div style="margin-top:0px;">
        <label style="float:left;">免疫项目</label>
        <select id="miad_flag_desc" style="margin-left: 5%;">
            <option value="正相关">正相关</option>
            <option value="负相关">负相关</option>
            <option value="超进展">超进展</option>
        </select>
    </div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">突变基因</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="miad_gene_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left;margin-top: 5px;">突变形式</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" type="text"
               id="miad_variant_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">变异丰度</label>
        <input class="input" style="width: 90%; margin-left: 5%; height: 25px; float: left;" id="miad_mutFreq_desc"/>
    </div>
    <div style="clear:both"></div>
    <div style="margin-top:20px;">
        <label style="float:left">循证医学证据：</label>
        <textarea style="width: 90%; margin-left: 5%; height: 60px; resize:vertical;" id="miad_varDesc_desc"></textarea>
    </div>
</div>
<c:if test="${geneticMarkerVwPageBean.module == '1'}">
    <script>
        $.post("${pageContext.request.contextPath}/geneMarkerVw/selectAllMutation", {report_id: "${geneticMarkerVwPageBean.report_id}"}, function (data) {
            $('#mstd_mutation_desc').autocomplete(data, {
                max: data.length, //列表里的条目数
                minChars: 0, //自动完成激活之前填入的最小字符
                width: 288, //提示的宽度，溢出隐藏
                scrollHeight: 300, //提示的高度，溢出显示滚动条
                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false, //自动填充
                formatItem: function (row, i, max) {
                    return row.mutation;
                },
                formatResult: function (row) {
                    return row.mutation;
                }
            }).result(function (event, row, formatted) {
                $('#mstd_transcript_desc').val(row.transcript);
                $('#mstd_mutFreq_desc').val(row.mutFreq);
                $('#mstd_ori_variant_desc').val(row.ori_variant);
                $('#mstd_mutDesc2_desc').val(row.mutDesc2);
            });
            $('#mltd_gene_desc').autocomplete(data, {
                max: data.length, //列表里的条目数
                minChars: 0, //自动完成激活之前填入的最小字符
                width: 288, //提示的宽度，溢出隐藏
                scrollHeight: 300, //提示的高度，溢出显示滚动条
                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false, //自动填充
                formatItem: function (row, i, max) {
                    return row.gene;
                },
                formatResult: function (row) {
                    return row.gene;
                }
            }).result(function (event, row, formatted) {
                $('#mltd_ori_variant_desc').val(row.ori_variant);
                $('#mltd_mutFreq_desc').val(row.mutFreq);
            });
            $('#mtpd_gene_desc').autocomplete(data, {
                max: data.length, //列表里的条目数
                minChars: 0, //自动完成激活之前填入的最小字符
                width: 288, //提示的宽度，溢出隐藏
                scrollHeight: 300, //提示的高度，溢出显示滚动条
                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false, //自动填充
                formatItem: function (row, i, max) {
                    return row.gene;
                },
                formatResult: function (row) {
                    return row.gene;
                }
            }).result(function (event, row, formatted) {
                $('#mtpd_ori_variant_desc').val(row.ori_variant);
                $('#mtpd_mutFreq_desc').val(row.mutFreq);
            });
            $('#mdd_gene_desc').autocomplete(data, {
                max: data.length, //列表里的条目数
                minChars: 0, //自动完成激活之前填入的最小字符
                width: 288, //提示的宽度，溢出隐藏
                scrollHeight: 300, //提示的高度，溢出显示滚动条
                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false, //自动填充
                formatItem: function (row, i, max) {
                    return row.gene;
                },
                formatResult: function (row) {
                    return row.gene;
                }
            }).result(function (event, row, formatted) {
                $('#mdd_ori_variant_desc').val(row.ori_variant);
                $('#mdd_mutFreq_desc').val(row.mutFreq);
                $('#mdd_mut_type_desc').val(row.ExonicFunc);
            });
            $('#miad_gene_desc').autocomplete(data, {
                max: data.length, //列表里的条目数
                minChars: 0, //自动完成激活之前填入的最小字符
                width: 288, //提示的宽度，溢出隐藏
                scrollHeight: 300, //提示的高度，溢出显示滚动条
                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false, //自动填充
                formatItem: function (row, i, max) {
                    return row.gene;
                },
                formatResult: function (row) {
                    return row.gene;
                }
            }).result(function (event, row, formatted) {
                $('#miad_variant_desc').val(row.ori_variant);
                $('#miad_mutFreq_desc').val(row.mutFreq);
            });
        })
        /*$.post("${pageContext.request.contextPath}/geneMarkerVw/getApprovedDrugData",function(data) {
				$("#madd_diseaseAndDrug_desc").flushCache();
				$('#madd_diseaseAndDrug_desc').autocomplete(data, {
					max: data.length, //列表里的条目数
					minChars: 0, //自动完成激活之前填入的最小字符
					width: 288, //提示的宽度，溢出隐藏
					scrollHeight: 300, //提示的高度，溢出显示滚动条
					matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false, //自动填充
					multiple:true,
					multipleSeparator:",",
					formatItem: function (row, i, max) {
						return row.disease + "-" + row.drug;
					},
					formatResult: function (row) {
						return row.disease + "-" + row.drug;
					}
				}).result(function (event, row, formatted) {
					$('#madd_approved_id_desc').val(row.approved_id);
					$('#madd_indication_desc').val(row.indication);
					$('#madd_institution_desc').val(row.institution);
				});
			})*/
        $.post("${pageContext.request.contextPath}/geneMarkerVw/getDiseases", function (data) {
            $("#madd_disease_desc").flushCache();
            $('#madd_disease_desc').autocomplete(data, {
                max: data.length, //列表里的条目数
                minChars: 0, //自动完成激活之前填入的最小字符
                width: 288, //提示的宽度，溢出隐藏
                scrollHeight: 300, //提示的高度，溢出显示滚动条
                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                autoFill: false, //自动填充
                multiple: true,
                multipleSeparator: ",",
                formatItem: function (row, i, max) {
                    return row.disease;
                },
                formatResult: function (row) {
                    return row.disease;
                }
            });
        })
    </script>
</c:if>

<!-- 以下是预览报告时的一些通用表格 -->
<div id="target-drug-analysis-gene-template" style="display:none">
    <div style="margin-top: 20px;">
        <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">_gene</lable>
    </div>
    <table class="preview-content-table2">
        <tr>
            <td style="border-top-width: 5px; font-size: 14px; font-weight: bold; color: #5DA1A6; width: 20%;"
                colspan="2">突变形式
            </td>
            <td style="border-top-width: 5px;">_mutation</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;" colspan="2">变异丰度</td>
            <td>_mutFreq</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6; width: 1%; padding: 10px;" rowspan="4">
                潜在获益药物
            </td>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">A级</td>
            <td>_drugsA</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">B级</td>
            <td>_drugsB</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">C级</td>
            <td>_drugsC</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">D级</td>
            <td>_drugsD</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;" colspan="4">潜在耐药信息</td>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">A级</td>
            <td>_resistantA</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">B级</td>
            <td>_resistantB</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">C级</td>
            <td>_resistantC</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;">D级</td>
            <td>_resistantD</td>
        </tr>
        <tr>
            <td style="font-size: 14px; font-weight: bold; color: #5DA1A6;" colspan="2">循证医学证据</td>
            <td style="text-align:left;">_drugNote</td>
        </tr>
    </table>
    <div name="target-drug-analysis-druginfo-table-div">
        <div>
            <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">药物信息</lable>
        </div>
        <table id="target-drug-analysis-druginfo-table" class="preview-content-table2"
               name="target-drug-analysis-druginfo-table">
            <tr>
                <th style="width: 20%;">药物名称</th>
                <th>适应症</th>
            </tr>
        </table>
    </div>
    <div name="target-drug-analysis-clinicalinfo-table-div">
        <div>
            <lable style="color:	#5DA1A6; font-weight: bold; font-size: 16px;">临床试验</lable>
        </div>
        <table class="preview-content-table2" name="target-drug-analysis-clinicalinfo-table">
            <tr>
                <th style="width: 20%;">ID</th>
                <th>临床试验名称</th>
                <th style="width: 10%">肿瘤类型</th>
                <th style="width: 5%">阶段</th>
                <th style="width: 10%">药物</th>
                <th style="width: 5%">地点</th>
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
        <%--<tr>
				<td>用药说明</td>
				<td style="text-align:left;">_drugNote</td>
			</tr>--%>
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
    var mmSarcomaTypings = ${mmSarcomaTypingsJson == null ? "[]" : mmSarcomaTypingsJson};
    var mmLymphomaTypings = ${mmLymphomaTypingsJson == null ? "[]" : mmLymphomaTypingsJson};
    var mmThyroidPrognoses = ${mmThyroidPrognosesJson == null ? "[]" : mmThyroidPrognosesJson};
    var mmDmmrs = ${mmDmmrsJson == null ? "[]" : mmDmmrsJson};
    var mmapprovedDrugsJson = ${mmapprovedDrugsJson == null ? "[]" : mmapprovedDrugsJson};
    var mmImmnueAlls = ${mmImmnueAllsJson == null ? "[]" : mmImmnueAllsJson};
    var chemo = ${chemoJson == null ? "[]" : chemoJson};
    let cancerTyping1166 = ${cancerTyping1166Json == null ? "[]" : cancerTyping1166Json};

    var chemo_this = [];
    var chemo_unknown = [];
    var chemo_effectiveness = [];
    var chemo_sideeffects = [];
    if (chemo) {
        if (chemo['化疗药物毒副作用风险及有效性预测']) {
            chemo_this = chemo['化疗药物毒副作用风险及有效性预测']['本癌种'] || [];
            chemo_unknown = chemo['化疗药物毒副作用风险及有效性预测']['未区分癌种'] || [];
        }
        if (chemo['化疗药物检测解析']) {
            chemo_effectiveness = chemo['化疗药物检测解析']['Effectiveness'] || [];
            chemo_sideeffects = chemo['化疗药物检测解析']['SideEffects'] || [];
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
        function (event, xhr, options, json) {
            if (json.isError) {
                sweetAlert("错误", json.errorMsg, "error");
            }
        }
    );

    function initSelect2() {
        $("#select2").val(0);
    }

    //获取转义后的ori_variant
    function get_ori_variant_str(data) {
        return (/^[0-9]+$/.test(data.Exon)) ? data.Transcript + " " + "exon" + data.Exon + " " + data.cHGVS.replace(">", "&gt;") + " " + data.pHGVS : data.Transcript + " " + data.Exon + " " + data.cHGVS.replace(">", "&gt;") + " " + data.pHGVS;
    }

    //获取ori_variant
    function get_ori_variant(data) {
        return (/^[0-9]+$/.test(data.Exon)) ? data.Transcript + " " + "exon" + data.Exon + " " + data.cHGVS + " " + data.pHGVS : data.Transcript + " " + data.Exon + " " + data.cHGVS + " " + data.pHGVS;
    }

    // 添加用药
    function addRecord(index, gene_variant_id) {
        var data = crAllList[parseInt(index) - 1];
        $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&hasDrug=1&lang=${lang}", data, function (returnData) {
            if (returnData && !returnData.isError) {
                data['rpCr'] = returnData;
                crAllList[parseInt(index) - 1] = data;
                crAllList[parseInt(index) - 1].check_date = returnData.check_date;
                var mutFreq = ".";
                $.post("${pageContext.request.contextPath}/geneMarkerVw/addDrugRecord?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=${lang}&parent_mutID=" + gene_variant_id, data, function (returnData) {
                    if (returnData && !returnData.isError) {
                        medicineList.push(returnData);
                        mutFreq = returnData.mutFreq;
                    }
                    $.post("${pageContext.request.contextPath}/geneMarkerVw/addRPVariantOrder",
                        {
                            "analysis_report_id":${ geneticMarkerVwPageBean.report_id },
                            "index_id": "",
                            "variant": data.Gene,
                            "ori_variant": get_ori_variant(data)
                        }
                    );
                    var variant = data.pHGVS.substr(data.pHGVS.indexOf(".") + 1, data.pHGVS.length);
                    var Gene = data.Gene;
                    // var new_record_num = $("tr[name='medicine_tr']").length+1;
                    var new_record_num = medicineList.length;
                    var trString = "<tr name='medicine_tr'>";
                    trString += '<td>' + new_record_num + '</td>';
                    trString += '<td class="gene">' + data.Gene + '</td>';
                    trString += '<td class="ori_variant" style="cursor:pointer"><p id="titleText_' + new_record_num + '" onmouseover="showTitle(' + new_record_num + ')" title="" onclick="showWindow(\'' + Gene + '\',\'' + variant + '\')">' + get_ori_variant(data) + '</p></td>';
                    trString += "<td>" + data.Zygosity + "</td>";
                    trString += '<td style="cursor:pointer"><p style="width:120px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" title=".">.</p></td>';
                    trString += "<td><label id='result_type_" + new_record_num + "'>靶向药物</label></td>";
                    trString += "<td style='display:none'><label id='result_type_val_" + new_record_num + "'>1</label></td>";
                    trString += "<td><a id='modify_a_" + new_record_num + "' style='text-decoration: underline; color: blue; cursor: pointer;'>修改</a>";
                    trString += "<script> $(\"#modify_a_" + new_record_num + "\").click(function(e){ if($(\"#result_type_val_" + new_record_num + "\").html() == '1'){showTddDialog(\"" + new_record_num + "\")} else if($(\"#result_type_val_" + new_record_num + "\").html() == '2'){showUcmdDialog(\"" + new_record_num + "\")} else {return;}});<\/script>";
                    trString += "</td>";
                    trString += "<td>未审核</td>";
                    trString += '<td><input type="button" value="置顶" class="btn"/> <input type="button" value="上移" class="btn"/> <input type="button" value="下移" class="btn"/></td>';
                    trString += "</tr>";

                    $("#medicine_tr").after(trString);
                    //$("#tInfo2").append(trString);

                    /* $.post("




                    ${pageContext.request.contextPath}/geneMarkerVw/addDrugRecord?userAccount=




                    ${user.user_account}&subbarcode=




                    ${geneticMarkerVwPageBean.subbarcode}&reportId=




                    ${geneticMarkerVwPageBean.report_id}",
						data); */

                    setTimeout(function () {
                        $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpVariantOrder",
                            {
                                "analysis_report_id":${ geneticMarkerVwPageBean.report_id },
                                "variant": data.Gene,
                                "ori_variant": get_ori_variant(data),
                                "type": 3
                            }
                        );
                    }, 1000);
                });
            }
        });
    }

    // 删除用药
    function deleteRecord(index) {
        var data = crAllList[parseInt(index) - 1];
        var update_flag = false;
        $("tr[name=medicine_tr]").each(function (k, v) {
            var td = $(v).find("td");
            if (td && td[1].innerText.trim() == data.Gene && td[2].innerText.trim() == (get_ori_variant(data))) {
                $(v).remove();
                $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&hasDrug=0&lang=${lang}", data, function (returnData) {
                    if (returnData && !returnData.isError) {
                        data['rpCr'] = returnData;
                        crAllList[parseInt(index) - 1] = data;
                        crAllList[parseInt(index) - 1].check_date = returnData.check_date;
                        update_flag = true;
                        $.post("${pageContext.request.contextPath}/geneMarkerVw/deleteDrugRecord?diseaseId=${diseaseId}&lang=${lang}&gender=${sampleFile.gender}", data, function (returnData) {

                        });
                        $.post("${pageContext.request.contextPath}/geneMarkerVw/deleteRpVariantOrder",
                            {
                                "analysis_report_id":${ geneticMarkerVwPageBean.report_id },
                                "variant": data.Gene,
                                "ori_variant": get_ori_variant(data)
                            }
                        );
                        $.each(medicineList, function (kk, vv) {
                            if (vv.gene == data.Gene && vv.ori_variant == (get_ori_variant(data))) {
                                // medicineList.splice(kk,1);
                                return false;
                            }
                        });
                    }
                });
                return false;
            }
        })
        if (!update_flag) {
            $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=${lang}", data, function (returnData) {
                data['rpCr'] = returnData;
                crAllList[parseInt(index) - 1] = data;
                crAllList[parseInt(index) - 1].check_date = returnData.check_date;
            });
        }
    }

    // 展示变异解析对话框
    function showAvdDialog(index) {
        $("#analytical_variation_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                initAvdDialog(index);
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveAvdDialog(index);
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示靶向药物对话框
    function showTddDialog(index) {
        varDrugIndex = index;
        $("#target_drug_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                initTddDialog(index);
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
                closeTddDialog();
            },
            buttons: {//设置页面的按钮
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                    closeTddDialog();
                }
            }
        });
    }

    // 选择要关联的父级突变对话框
    function showGimDialog(gene, index, flag) {
        $("#unkonw_clinical_dialog").dialog({
            width: '40%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                initGimDialog();
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
                closeGimDialog();
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    let that = $(this);
                    var oriVariantList = $("#ori_variant").val();
                    $.post("${pageContext.request.contextPath}/autoComplete/getGeneVariantId", {"oriVariantList": oriVariantList}, function (data) {
                        $("#gene_variant_id").val(data);
                        if (flag == 0) {
                            addRecord(index, data);
                        } else if (flag == 1) {
                            saveGimDialog(index, data);
                        }
                        that.dialog("destroy");//关闭当前弹窗
                    });
                }
            }
        });
    }

    function initGimDialog() {
        $("#ori_variant").val("");
        $("#gene_variant_id").val("");
    }

    function closeGimDialog() {
        $("#ori_variant").val("");
        $("#gene_variant_id").val("");
    }

    function saveGimDialog(id, gene_variant_id) {
        deleteUnknownVar(id, gene_variant_id);
    }

    // 展示未知临床意义对话框
    function showUcmdDialog(index) {
        $("#unknow_clinical_meaning_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                initUcmdDialog(index);
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveUcmdDialog(index);
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    function showPosTranscriptDialog(index) {
        $("#pos_transcript_dialog").dialog({
            width: 300,
            height: 'auto',
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                initPosTranscriptDialog(index);
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示肉瘤辅助诊断对话框
    function showMstdDialog() {
        $("#mm_sarcoma_typings_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                $('#mstd_mutation_desc').val('');
                $('#mstd_transcript_desc').val('');
                $('#mstd_mutFreq_desc').val('');
                $('#mstd_sarcoma_subtype_desc').val('');
                $('#mstd_evidence_desc').val('');
                $('#mstd_ori_variant_desc').val('');
                $('#mstd_mutDesc2_desc').val('');
                $('#mstd_mutationAnalysis_desc').val('');
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveMstdDialog();
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示淋巴瘤辅助分型提示对话框
    function showMltdDialog() {
        $("#mm_lymphoma_typings_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                $('#mltd_gene_desc').val('');
                $('#mltd_ori_variant_desc').val('');
                $('#mltd_mutFreq_desc').val('');
                $('#mltd_lymphoma_subtype_desc').val('');
                $('#mltd_lymphoma_subtype2_desc').val('');
                $('#mltd_evidence_desc').val('');
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveMltdDialog();
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示甲状腺癌预后评估对话框
    function showMtpdDialog() {
        $("#mm_thyroid_prognoses_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                $('#mtpd_gene_desc').val('');
                $('#mtpd_ori_variant_desc').val('');
                $('#mtpd_mutFreq_desc').val('');
                $('#mtpd_prognosis_evaluation_desc').val('');
                $('#mtpd_prognosis_assessment_desc').val('');
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveMtpdDialog();
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示错配修复（MMR）相关基因检测结果对话框
    function showMddDialog() {
        $("#mm_dmmrs_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                $('#mdd_gene_desc').val('');
                $('#mdd_ori_variant_desc').val('');
                $('#mdd_mutFreq_desc').val('');
                $('#mdd_mut_type_desc').val('');
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveMddDialog();
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示本癌种FDA/NMPA获批的其他可选靶向药物对话框
    function showMaddDialog() {
        $("#mm_approved_drugs_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                /*$('#madd_approved_id_desc').val('');
				$('#madd_diseaseAndDrug_desc').val('');
				$('#madd_indication_desc').val('');
				$('#madd_institution_desc').val('');*/
                $('#madd_disease_desc').val('');
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveMaddDialog();
                    // $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示免疫正负超进展相关基因检测对话框
    function showMiadDialog() {
        $("#mm_immnue_alls_dialog").dialog({
            width: '75%',
            height: 'auto',
            position: {
                my: "center",
                at: "left",
                of: window,
                collision: "fit",
                // Ensure the titlebar is always visible
                using: function (pos) {
                    var topOffset = $(this).css(pos).offset().top;
                    if (topOffset < 0) {
                        $(this).css("top", pos.top - topOffset);
                    }
                }
            },
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                $('#miad_flag_desc').val('');
                $('#miad_gene_desc').val('');
                $('#miad_variant_desc').val('');
                $('#miad_mutFreq_desc').val('');
                $('#miad_varDesc_desc').val('');
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                //按钮的名字:对应的方法
                "保存": function () {
                    saveMaidDialog();
                    $(this).dialog("destroy");//关闭当前弹窗
                },
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 展示转录本号对话框
    function showTranscriptDialog(index) {
        $("#transcript_dialog").dialog({
            width: 300,
            height: 'auto',
            closeOnEscape: true,//右上角没有叉号
            //↑↑↑在弹窗右上角不显示关闭按钮，如果要用右上角的关闭就是true或者不写这个属性
            modal: true,//弹窗的时候不能动页面的其他地方（模态模式）
            create: function (e, ui) {//开始执行的方法，用来处理弹出时事件
                initTranscriptDialog(index);
            },
            close: function (e, ui) {
                $(this).dialog("destroy");//关闭当前弹窗
            },
            buttons: {//设置页面的按钮
                "关闭": function () {
                    $(this).dialog("destroy");//关闭当前弹窗
                }
            }
        });
    }

    // 初始化变异解析对话框
    function initAvdDialog(index) {
        varDrugIndex = index;
        var data = crAllList[parseInt(index) - 1];
        $("#avd_gene_name").val(data.Gene);
        $("#avd_mut_style").val(get_ori_variant(data));
        $("#avd_mut_desc").val(data.mutDesc);
        if (data && data.rpCr) {
            $("#avd_gene_desc").val(data.rpCr.GeneDesc || '');
            $("#avd_mut_analysis").val(data.rpCr.VarClianno || '');
        } else {
            $("#avd_gene_desc").val('');
            $("#avd_mut_analysis").val('');
        }
    }

    function isJSON(str) {
        if (typeof str == 'string') {
            try {
                JSON.parse(str);
                return true;
            } catch (e) {
                return false;
            }
        }
    }

    function conversionStr2(str1, mutDesc2) {
        $.each($.parseJSON(str1), function (i, val) {
            if (val.key == "基因说明:") {
                $('#drug1').val(val.value);
            }
            if (val.key == "信号通路说明:") {
                $('#drug2').val(val.value);
            }
            $('#drug8').val(mutDesc2);
            if (val.key == "位点说明:") {
                $('#drug3').val(val.value);
            }
            if (val.key == "NCCN指南:") {
                $('#drug4').val(val.value);
            }
            /* if(val.key == "预后和诊断说明:"){
				$('#drug5').val(val.value);
			} */
            if (val.key == "用药说明:") {
                $('#drug6').val(val.value);
            }
            if (val.key == "耐药说明:") {
                $('#drug7').val(val.value);
            }
            if (val.key == "recommend:") {
                $('#recommend').val(val.value);
                $('#recommend1').html(val.value);
            }
        });
    }

    function initconversionStr2() {
        $('#drug1').val('');
        $('#drug2').val('');
        $('#drug8').val('');
        $('#drug3').val('');
        $('#drug4').val('');
        /* $('#drug5').val(''); */
        $('#drug6').val('');
        $('#drug7').val('');
        $('#recommend').val('');
        $('#recommend1').html('');
    }

    function initconversionStr() {
        $('#ucmd_medicine1').val('');
        $('#ucmd_medicine2').val('');
    }

    //将用药说明转换为文本
    function conversionStr(str1) {
        $.each($.parseJSON(str1), function (i, val) {
            if (val.key == "预后和诊断说明:") {
                $('#ucmd_medicine1').val(val.value);
            }
            if (val.key == "用药说明:") {
                $('#ucmd_medicine2').val(val.value);
            }
        });
    }

    // 初始化靶向药物对话框
    function initTddDialog(index) {
        varDrugNote_modified = false;
        varDrugIndex = index;
        var data = medicineList[parseInt(index) - 1];
        $("#tdd_gene_name_1").val(data.gene);
        $("#tdd_mut_style").val(data.ori_variant);
        $("#tdd_mut_desc").val(data.mutDesc2);
        $("#tdd_mut_freq").val(data.mutFreq);
        $('#recommend').val('');
        $('#recommend1').html('');
        initconversionStr2();
        if (data.varDrugNote) {
            conversionStr2(data.varDrugNote, data.mutDesc2);
        }
        /* if(data.varDrugNote){
			$("#tdd_medicine_desc").html(conversionStr2(data.varDrugNote));
		}else{
			$("#tdd_medicine_desc").html("");
		} */

        drugList = data.drugList || [];
        clinicalList = data.clinicalList || [];

        if (drugList) {
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
        $("#c2_ul").html("");
        if (drugList) {
            // var drugSet = {};
            for (let i = 0; i < drugList.length; i++) {
                //$.each(drugList,function(k,v){
                var v = drugList[i];
                var drugName = v.drug_name;
                // if (drugSet[drugName]) { continue; }
                // drugSet[drugName] = true;
                drugCount++;
                if (v.status == 'delete') {
                    return true;
                }
                var color = 'black'
                if (v.approve_range == '1') {
                    color = '#000000';
                } else if (v.approve_range == '2') {
                    color = '#0000CD';
                } else if (v.approve_range == '3') {
                    color = '#008B00';
                } else if (v.approve_range == '4') {
                    color = '#CD8500';
                } else if (v.approve_range == '5') {
                    color = '#FF0000';
                } else if (v.approve_range == '6') {
                    color = '#FFB6C1';
                } else if (v.approve_range == '7') {
                    color = '#800080';
                } else if (v.approve_range == '8') {
                    color = '#00FFFF';
                }

                if (v.cfda == '1') {
                    drugName += "*";
                }
                if (v.recruiting == '1') {
                    drugName += "#";
                }
                var newLiStr = "<li id=\"c2_li_" + drugCount + "\" class=\"drug-li\" style=\"color:" + color + "\" onclick=\"handleTargetInfo('" + drugCount + "')\">" + drugName + "</li>";
                $("#c2_ul").append(newLiStr);
            }
        }
        $("#c3_ul").html("");
        if (clinicalList) {
            $.each(clinicalList, function (k, v) {
                clinicalCount++;
                if (v.status == 'delete') {
                    return true;
                }
                var clinical_trial_id = v.clinical_trial_id;
                var drugName = v.drug_name;
                var newLiStr = "<li id=\"c3_li_" + clinicalCount + "\" class=\"drug-li\" onclick=\"handleTargetInfo2('" + clinicalCount + "')\">" + drugName + "-" + clinical_trial_id + "</li>";
                $("#c3_ul").append(newLiStr);
            })
        }
    }

    // 初始化未知临床意义对话框
    function initUcmdDialog(index) {
        rpUnknownVar_modified = false;
        var data = medicineList[parseInt(index) - 1];
        $("#ucmd_gene_name").val(data.gene);
        $("#ucmd_mut_style").val(data.ori_variant);
        $("#ucmd_mut_desc").val(data.mutDesc);
        $("#ucmd_gene_desc").val(data.rpUnknownVar.gene_description);
        if (data.rpUnknownVar.var_drug_desc) {
            conversionStr(data.rpUnknownVar.var_drug_desc);
        } else {
            initconversionStr();
        }
    }

    function initPosTranscriptDialog(index) {
        var data = crAllList[parseInt(index) - 1];
        $("#pos").html(data.Pos || '');
        $("#transcript2").html(data.Transcript || '');
    }

    // 初始化转录本号对话框
    function initTranscriptDialog(index) {
        var data = medicineList[parseInt(index) - 1];
        $("#transcript").html(data.transcript || '');
    }

    // 保存变异解析对话框
    function saveAvdDialog(index) {
        var data = crAllList[parseInt(index) - 1];
        data.geneDesc = $("#avd_gene_desc").val();
        data.varClianno = $("#avd_mut_analysis").val();
        $.post("${pageContext.request.contextPath}/geneMarkerVw/updateRpCr?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&lang=${lang}", data, function (returnData) {
            if (returnData && !returnData.isError) {
                swal("成功！", "保存成功", "success");
                data['rpCr'] = returnData;
                crAllList[parseInt(index) - 1] = data;
                crAllList[parseInt(index) - 1].check_date = returnData.check_date;
                $("#cr_check_date_" + index).html("未审核");
            }
        });
    }

    // 保存未知临床意义对话框
    function saveUcmdDialog(index) {
        var data = medicineList[parseInt(index) - 1].rpUnknownVar || {};
        var ori_variant = encodeURIComponent(data.ori_variant);
        data.gene_description = $("#ucmd_gene_desc").val();
        var aaa = $('#ucmd_medicine_desc').serializeArray();
        aaa = JSON.stringify(aaa);
        aaa = aaa.replace(/"name"/g, '"key"');
        data.var_drug_desc = aaa;
        if (rpUnknownVar_modified) {
            data.modified = rpUnknownVar_modified;
            $.post("${pageContext.request.contextPath}/geneMarkerVw/saveUnknownVar?userAccount=${user.user_account}&subbarcode=${geneticMarkerVwPageBean.subbarcode}&reportId=${geneticMarkerVwPageBean.report_id}&gene=" + data.gene + "&ori_variant=" + ori_variant + "&disease_id=${diseaseId}", data, function (returnData) {
                if (!returnData || !returnData.isError) {
                    swal("成功！", "保存成功", "success");
                    medicineList[parseInt(index) - 1].check_date = null;
                    $("#result_check_date_" + index).html("未审核");
                }
            });
        }
    }

    // 保存肉瘤辅助诊断对话框
    function saveMstdDialog() {
        var mutation = $("#mstd_mutation_desc").val();
        var transcript = $("#mstd_transcript_desc").val();
        var mutFreq = $("#mstd_mutFreq_desc").val();
        var sarcoma_subtype = $("#mstd_sarcoma_subtype_desc").val();
        var evidence = $("#mstd_evidence_desc").val();
        var ori_variant = $("#mstd_ori_variant_desc").val();
        var mutDesc2 = $("#mstd_mutDesc2_desc").val();
        var mutationAnalysis = $("#mstd_mutationAnalysis_desc").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveMmSarcomaTyping",
            type: "POST",
            data: {
                "report_id":${geneticMarkerVwPageBean.report_id},
                "mutation": mutation,
                "transcript": transcript,
                "mutFreq": mutFreq,
                "sarcoma_subtype": sarcoma_subtype,
                "evidence": evidence,
                "ori_variant": ori_variant,
                "mutDesc2": mutDesc2,
                "mutationAnalysis": mutationAnalysis,
                "update_by": "${user.user_account}"
            },
            dataType: "json",
            success: function (result) {
                if (result) {
                    swal("成功！", "保存成功", "success");
                    var i = ++mmSarcomaTypings.length;
                    var trString = "<tr id='sarcomaTyping_tr_" + i + "'>";
                    trString += "<td><label id='st_mutation_" + i + "'>" + mutation + "</label><br><input id='st_transcript_" + i + "' style='text-align:center; width: 250px;' value='" + transcript + "'/></td>";
                    trString += "<td><label id='st_mutFreq_" + i + "'>" + mutFreq + "</label></td>";
                    trString += "<td><textarea id='st_sarcoma_subtype_" + i + "' style='width: 319px; height: 80px;'>" + sarcoma_subtype + "</textarea></td>";
                    trString += "<td><select id='st_evidence_" + i + "'><option value='WHO'>WHO</option><option value='NCCN'>NCCN</option><option value='CSCO'>CSCO</option><option value='专家共识'>专家共识</option></select></td>";
                    trString += "<td><label id='st_ori_variant_" + i + "'>" + ori_variant + "</label></td>";
                    trString += "<td><textarea id='st_mutDesc2_" + i + "' style='width: 319px; height: 80px;'>" + mutDesc2 + "</textarea></td>";
                    trString += "<td><textarea id='st_mutationAnalysis_" + i + "' style='width: 319px; height: 80px;'>" + mutationAnalysis + "</textarea></td>";
                    trString += "<td><input type='button' value='修改' onclick='updateMmSarcomaTyping(" + i + ")' class='btn'/> <input type='button' value='删除' onclick='deleteMmSarcomaTyping(" + i + ")' class='btn'/></td>";
                    trString += "</tr>";
                    $("#tInfo4").append(trString);
                    $("#st_evidence_" + i).val(evidence);
                } else {
                    swal("失败！", "保存失败", "error");
                }
            }
        });
    }

    // 保存淋巴瘤辅助分型对话框
    function saveMltdDialog() {
        var gene = $("#mltd_gene_desc").val();
        var ori_variant = $("#mltd_ori_variant_desc").val();
        var mutFreq = $("#mltd_mutFreq_desc").val();
        var lymphoma_subtype = $("#mltd_lymphoma_subtype_desc").val();
        var lymphoma_subtype2 = $("#mltd_lymphoma_subtype2_desc").val();
        var evidence = $("#mltd_evidence_desc").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveMmLymphomaTyping",
            type: "POST",
            data: {
                "report_id":${geneticMarkerVwPageBean.report_id},
                "gene": gene,
                "ori_variant": ori_variant,
                "mutFreq": mutFreq,
                "lymphoma_subtype": lymphoma_subtype,
                "lymphoma_subtype2": lymphoma_subtype2,
                "evidence": evidence,
                "update_by": "${user.user_account}"
            },
            dataType: "json",
            success: function (result) {
                if (result) {
                    swal("成功！", "保存成功", "success");
                    var i = ++mmLymphomaTypings.length;
                    var trString = "<tr id='lymphomaTyping_tr_" + i + "'>";
                    trString += "<td><label id='lt_gene_" + i + "'>" + gene + "</label></td>";
                    trString += "<td><label id='lt_ori_variant_" + i + "'>" + ori_variant + "</label></td>";
                    trString += "<td><label id='lt_mutFreq_" + i + "'>" + mutFreq + "</label></td>";
                    trString += "<td><textarea id='lt_lymphoma_subtype_" + i + "' style='width: 280px; height: 80px;'>" + lymphoma_subtype + "</textarea></td>";
                    trString += "<td><textarea id='lt_lymphoma_subtype2_" + i + "' style='width: 280px; height: 80px;'>" + lymphoma_subtype2 + "</textarea></td>";
                    trString += "<td><select id='lt_evidence_" + i + "'><option value='WHO'>WHO</option><option value='NCCN'>NCCN</option><option value='CSCO'>CSCO</option><option value='专家共识'>专家共识</option></select></td>";
                    trString += "<td><input type='button' value='保存' onclick='updateMmLymphomaTyping(" + i + ")' class='btn'/> <input type='button' value='删除' onclick='deleteMmLymphomaTyping(" + i + ")' class='btn'/></td>";
                    trString += "</tr>";
                    $("#tInfo5").append(trString);
                    $("#lt_evidence_" + i).val(evidence);
                } else {
                    swal("失败！", "保存失败", "error");
                }
            }
        });
    }

    // 保存甲状腺癌预后评估对话框
    function saveMtpdDialog() {
        var gene = $("#mtpd_gene_desc").val();
        var ori_variant = $("#mtpd_ori_variant_desc").val();
        var mutFreq = $("#mtpd_mutFreq_desc").val();
        var prognosis_evaluation = $("#mtpd_prognosis_evaluation_desc").val();
        var prognosis_assessment = $("#mtpd_prognosis_assessment_desc").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveMmThyroidPrognosis",
            type: "POST",
            data: {
                "report_id":${geneticMarkerVwPageBean.report_id},
                "gene": gene,
                "ori_variant": ori_variant,
                "mutFreq": mutFreq,
                "prognosis_evaluation": prognosis_evaluation,
                "prognosis_assessment": prognosis_assessment,
                "update_by": "${user.user_account}"
            },
            dataType: "json",
            success: function (result) {
                if (result) {
                    swal("成功！", "保存成功", "success");
                    var i = ++mmThyroidPrognoses.length;
                    var trString = "<tr id='thyroidPrognosis_tr_" + i + "'>";
                    trString += "<td><label id='tp_gene_" + i + "'>" + gene + "</label></td>";
                    trString += "<td><input id='tp_ori_variant_" + i + "' style='text-align:center; width: 250px;' value='" + ori_variant + "'/></td>";
                    trString += "<td><label id='tp_mutFreq_" + i + "'>" + mutFreq + "</label></td>";
                    trString += "<td><textarea id='tp_prognosis_evaluation_" + i + "' style='width: 280px; height: 80px;'>" + prognosis_evaluation + "</textarea></td>";
                    trString += "<td><textarea id='tp_prognosis_assessment_" + i + "' style='width: 280px; height: 80px;'>" + prognosis_assessment + "</textarea></td>";
                    trString += "<td><input type='button' value='保存' onclick='updateMmThyroidPrognosis(" + i + ")' class='btn'/> <input type='button' value='删除' onclick='deleteMmThyroidPrognosis(" + i + ")' class='btn'/></td>";
                    trString += "</tr>";
                    $("#tInfo7").append(trString);
                } else {
                    swal("失败！", "保存失败", "error");
                }
            }
        });
    }

    // 保存错配修复（MMR）相关基因检测结果对话框
    function saveMddDialog() {
        var gene = $("#mdd_gene_desc").val();
        var ori_variant = $("#mdd_ori_variant_desc").val();
        var mutFreq = $("#mdd_mutFreq_desc").val();
        var mut_type = $("#mdd_mut_type_desc").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveMmDmmr",
            type: "POST",
            data: {
                "report_id":${geneticMarkerVwPageBean.report_id},
                "gene": gene,
                "ori_variant": ori_variant,
                "mutFreq": mutFreq,
                "mut_type": mut_type,
                "update_by": "${user.user_account}"
            },
            dataType: "json",
            success: function (result) {
                if (result) {
                    swal("成功！", "保存成功", "success");
                    var i = ++mmDmmrs.length;
                    var trString = "<tr id='dmmr_tr_" + i + "'>";
                    trString += "<td><label id='dmmr_gene_" + i + "'>" + gene + "</label></td>";
                    trString += "<td><label id='dmmr_ori_variant_" + i + "'>" + ori_variant + "</label></td>";
                    trString += "<td><label id='dmmr_mutFreq_" + i + "'>" + mutFreq + "</label></td>";
                    trString += "<td><input id='dmmr_mut_type_" + i + "' style='text-align:center; width: 250px;' value='" + mut_type + "'/></td>";
                    trString += "<td><input type='button' value='保存' onclick='updateMmDmmr(" + i + ")' class='btn'/> <input type='button' value='删除' onclick='deleteMmDmmr(" + i + ")' class='btn'/></td>";
                    trString += "</tr>";
                    $("#tInfo8").append(trString);
                } else {
                    swal("失败！", "保存失败", "error");
                }
            }
        });
    }

    // 保存本癌种FDA/NMPA获批的其他可选靶向药物对话框
    function saveMaddDialog() {
        // var diseaseAndDrug = $("#madd_diseaseAndDrug_desc").val();
        var diseases = $("#madd_disease_desc").val();
        $.ajax({
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveMmApprovedDrugs",
            type: "POST",
            data: {
                "report_id":${geneticMarkerVwPageBean.report_id},
                "diseases": diseases,
                "update_by": "${user.user_account}"
            },
            dataType: "json",
            success: function (result) {
                if (result.flag) {
                    swal("成功！", "保存成功", "success");
                    var data = result.data;
                    for (var j = 0; j < data.length; j++) {
                        var i = ++mmapprovedDrugsJson.length;
                        var trString = "<tr id='approvedDrug_tr_" + i + "'>";
                        trString += "<td><label id='add_disease_" + i + "'>" + data[j].disease + "</label></td>";
                        trString += "<td><label id='add_drug_" + i + "'>" + data[j].drug + "</label></td>";
                        trString += "<td><textarea id='add_indication_" + i + "' style='width: 319px; height: 80px;'>" + data[j].indication + "</textarea></td>";
                        trString += "<td><select id='add_institution_" + i + "'><option value='FDA'>FDA</option><option value='NMPA'>NMPA</option><option value='FDA/NMPA'>FDA/NMPA</option></select></td>";
                        trString += "<td><input type='button' value='修改' onclick='updateMmApprovedDrug(" + i + ")' class='btn'/> <input type='button' value='删除' onclick='deleteMmApprovedDrug(" + i + ")' class='btn'/></td>";
                        trString += "</tr>";
                        $("#tInfo15").append(trString);
                        $("#add_institution_" + i).val(data[j].institution);
                    }
                } else {
                    swal("失败！", "保存失败", "error");
                }
            }
        });
    }

    // 保存免疫正负超进展相关基因检测对话框
    function saveMaidDialog() {
        var flag_desc = $('#miad_flag_desc').val();
        var flag = "";
        if (flag_desc == "正相关") {
            flag = 1;
        } else if (flag_desc == "负相关") {
            flag = 2;
        } else if (flag_desc == "超进展") {
            flag = 3;
        }
        var gene = $('#miad_gene_desc').val();
        var variant = $('#miad_variant_desc').val();
        var mutFreq = $('#miad_mutFreq_desc').val();
        var varDesc = $('#miad_varDesc_desc').val();
        $.ajax({
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveMmImmnueAll",
            type: "POST",
            data: {
                "report_id":${geneticMarkerVwPageBean.report_id},
                "flag": flag,
                "gene": gene,
                "variant": variant,
                "mutFreq": mutFreq,
                "varDesc": varDesc,
                "update_by": "${user.user_account}"
            },
            dataType: "json",
            success: function (result) {
                if (result) {
                    swal("成功！", "保存成功", "success");
                    var i = ++mmImmnueAlls.length;
                    var trString = "<tr id='immnueAll_tr_" + i + "'>";
                    trString += "<td><label id='ia_flag_" + i + "'>" + flag_desc + "</label></td>";
                    trString += "<td><label id='ia_gene_" + i + "'>" + gene + "</label></td>";
                    trString += "<td><label id='ia_variant_" + i + "'>" + variant + "</label></td>";
                    trString += "<td><label id='ia_mutFreq_" + i + "'>" + mutFreq + "</label></td>";
                    trString += "<td><textarea id='ia_varDesc_" + i + "' style='width: 280px; height: 80px;'>" + varDesc + "</textarea></td>";
                    trString += "<td><input type='button' value='保存' onclick='updateMmImmnueAll(" + i + ")' class='btn'/> <input type='button' value='删除' onclick='deleteMmImmnueAll(" + i + ")' class='btn'/></td>";
                    trString += "</tr>";
                    $("#tInfo9").append(trString);
                } else {
                    swal("失败！", "保存失败", "error");
                }
            }
        });
    }

    function switchContent(index) {
        $("div[name=content]").each(function (k, v) {
            if (v.id != 'content' + index) {
                $(v).hide();
            } else {
                $(v).show();
            }
        })
    }

    function closeTddDialog() {
        switchContent(1);
        $("#c2_ul").html("");
        $("#c2_tdd_drug_name").val("");
        $("#c2_tdd_anno_disease_name").val("");
        $("#c2_tdd_evidence_phase").val("");
        $("#approve_range_select").val(1);
        $("#recruit_select").val(0);
        $("#isCFDA").prop("checked", false);
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

    function getThisTargetDrugs(drug) {
        return drug.relationship != 'Resistant' && drug.evidence_phase == 'Approved';
    }

    function getThatTargetDrugs(drug) {
        return drug.relationship != 'Resistant' && drug.evidence_phase == 'Guildline recommended';
    }

    function getResistantDrugs(drug) {
        return drug.relationship == 'Resistant';
    }

    function getClinicalDrugs(drug) {
        return drug.relationship != 'Resistant' && drug.evidence_phase != 'Approved' && drug.evidence_phase != 'Guildline recommended';
    }

    // 处理靶向药物列表的点击事件
    function handleTargetInfo(index) {
        listIndex = index;
        var targetDrug = drugList[index - 1];
        $("#c2_tdd_drug_name").val(targetDrug.drug_name);
        $("#c2_tdd_anno_disease_name").val(targetDrug.anno_disease_name);
        $("#c2_tdd_evidence_phase").val(targetDrug.evidence_phase);
        if (targetDrug.cfda == 1) {
            $("#isCFDA").prop("checked", true);
        } else {
            $("#isCFDA").prop("checked", false);
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
        var newLiStr = "<li id=\"c2_li_" + drugCount + "\" style=\"color: #551A8B\" class=\"drug-li\" onclick=\"handleTargetInfo('" + drugCount + "')\">新建药物</li>";
        $("#c2_tdd_drug_name").val("新建药物");
        drugList.push({"drug_name": "新建药物", "approve_range": "1", "recruiting": "0", "cfda": "0", "status": "add"});
        $("#c2_ul").append(newLiStr);
        $("#c2_tdd_anno_disease_name").val("");
        $("#c2_tdd_evidence_phase").val("");
        $("#isCFDA").prop("checked", false);
        $("#approve_range_select").val("1");
        $("#recruit_select").val("0");
        $("#tdd_indication").val("");

    }

    function deleteDrug1() {
        if (listIndex > 0) {
            drugList[listIndex - 1].status = 'delete';
            $("#c2_tdd_drug_name").val("");
            $("#c2_tdd_anno_disease_name").val("");
            $("#c2_tdd_evidence_phase").val("");
            $("#isCFDA").prop("checked", false);
            $("#approve_range_select").val("1");
            $("#recruit_select").val("0");
            $("#tdd_indication").val("");
            $("#c2_li_" + listIndex).remove();
            listIndex = 0;
        }
    }

    // 抓取药物信息
    function getDrug1() {
        if (listIndex > 0) {
            var drug_name = encodeURIComponent(drugList[listIndex - 1].drug_name);
            var anno_disease_name = encodeURIComponent(drugList[listIndex - 1].anno_disease_name);
            var evidence_phase = drugList[listIndex - 1].evidence_phase;
            $.post("${pageContext.request.contextPath}/geneMarkerVw/getDrugInfo?lang=${lang}&drug_name=" + drug_name + "&anno_disease_name=" + anno_disease_name, function (returnData) {
                if (returnData && !returnData.isError) {
                    swal("成功！", "获取数据成功", "success");
                    drugList[listIndex - 1].cfda = returnData.cfda;
                    if (returnData.cfda == '1') {
                        $("#c2_li_" + listIndex).html(returnData.drug_name + "*");
                    } else {
                        $("#c2_li_" + listIndex).html(returnData.drug_name);
                    }
                    if (drugList[listIndex - 1].recruiting == '1') {
                        $("#c2_li_" + listIndex).html(returnData.drug_name + "#");
                    }
                    drugList[listIndex - 1].old_drug_name = returnData.drug_name;
                    drugList[listIndex - 1].approval_desc = returnData.approval_desc;
                    drugList[listIndex - 1].disease_id = returnData.disease_id;
                    drugList[listIndex - 1].status = returnData.status;
                    handleTargetInfo(listIndex);
                } else {
                    swal("成功！", "无适应症数据", "success");
                }
            });
        }
    }

    // 保存药物信息
    function saveDrug1() {
        var medicine = medicineList[varDrugIndex - 1];
        var ori_variant = encodeURIComponent(medicine.ori_variant)
        $.ajax({
            type: "post",
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveDrugRecord?userAccount=${user.user_account}&gene=" + medicine.gene + "&ori_variant=" + ori_variant + "&disease_id=${diseaseId}&record_id=" + medicine.record_id + "&lang=${lang}&gender=${sampleFile.gender}",
            data: JSON.stringify(drugList),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data && !data.isError) {
                    swal("成功！", "保存成功", "success");
                    drugList = data;
                    medicineList[varDrugIndex - 1].drugList = drugList;
                    medicineList[varDrugIndex - 1].check_date = null;
                    initTddDialog(varDrugIndex);
                    $("#result_check_date_" + varDrugIndex).html("未审核");
                }
            },
            error: function (data) {

            }
        })
    }

    // 保存靶向药物用药说明
    function saveVarDrugNote() {
        var medicine = medicineList[varDrugIndex - 1];
        var ori_variant = encodeURIComponent(medicine.ori_variant);
        var aaa = $('#tdd_medicine_desc').serializeArray();
        aaa = JSON.stringify(aaa);
        aaa = aaa.replace(/"name"/g, '"key"');
        if (varDrugNote_modified) {
            $.post("${pageContext.request.contextPath}/geneMarkerVw/updateVarDrugNote?userAccount=${user.user_account}&gene=" + medicine.gene + "&ori_variant=" + ori_variant + "&disease_id=${diseaseId}&record_id=" + medicine.record_id + "&lang=${lang}&gender=${sampleFile.gender}",
                {"var_drug_desc": aaa},
                function (returnData) {
                    if (!returnData || !returnData.isError) {
                        swal("成功！", "保存成功", "success");
                        medicineList[varDrugIndex - 1].varDrugNote = aaa;
                        medicineList[varDrugIndex - 1].check_date = null;
                        $("#result_check_date_" + varDrugIndex).html("未审核");
                    }
                });
        }
    }

    $.fn.serializeObject = function () {
        var o = '[';
        var a = this.serializeArray();
        $.each(a, function () {
            o += '{"key":"' + this.name + '","value":"' + this.value + '"},'
        });
        o = o.substring(0, o.length - 1);
        o += ']';
        return o;
    };

    // 删除用药并添加未知临床意义(修改'基因检测结果类别'时触发)
    function deleteDrugAndAddUnknownVar(index, resultType) {
        var medicine = medicineList[index];
        var variant = encodeURIComponent(medicine.variant);
        var ori_variant = encodeURIComponent(medicine.ori_variant);
        $.post("${pageContext.request.contextPath}/geneMarkerVw/deleteDrugAndAddUnknownVar?userAccount=${user.user_account}&gene=" + medicine.gene + "&variant=" + variant + "&ori_variant=" + ori_variant + "&disease_id=${diseaseId}&lang=${lang}&gender=${sampleFile.gender}&resultType=" + resultType, null, function (returnData) {
            if (!returnData || !returnData.isError) {
                medicineList[index].resultTypeDesc = returnData.resultTypeDesc;
                medicineList[index].resultTypeVal = returnData.resultTypeVal;
                medicineList[index].drugList = [];
                medicineList[index].clinicalList = [];
                medicineList[index].rpUnknownVar = returnData.rpUnknownVar || {};
            }
        });
    }

    // 删除未知临床意义(修改'基因检测结果类别'时触发)
    function deleteUnknownVar(index, gene_variant_id) {
        var medicine = medicineList[index];
        var variant = encodeURIComponent(medicine.variant)
        var ori_variant = encodeURIComponent(medicine.ori_variant);
        var cosmic = encodeURIComponent(medicine.cosmic);
        var mutFreq = encodeURIComponent(medicine.mutFreq);
        $.post("${pageContext.request.contextPath}/geneMarkerVw/deleteUnknownVar?userAccount=${user.user_account}&gene=" + medicine.gene + "&variant=" + variant + "&ori_variant=" + ori_variant + "&cosmic=" + cosmic + "&mutFreq=" + mutFreq + "&disease_id=${diseaseId}&lang=${lang}&reportId=${geneticMarkerVwPageBean.report_id}&parent_mutID=" + gene_variant_id, null, function (returnData) {
            if (!returnData || !returnData.isError) {
                medicineList[index].resultTypeDesc = returnData.resultTypeDesc;
                medicineList[index].resultTypeVal = returnData.resultTypeVal;
                medicineList[index].drugList = returnData.drugList || [];
                medicineList[index].varDrugNote = returnData.varDrugNote || [];
                medicineList[index].clinicalList = returnData.clinicalList || [];
                medicineList[index].rpUnknownVar = null;
                $("#result_type_" + (index + 1)).html($("#select2").find("option:selected").text());
                $("#result_type_val_" + (index + 1)).html($("#select2").find("option:selected").val());
            }
        });
    }

    // 新增临床试验药物
    function addDrug2() {
        clinicalCount++;
        clinicalIndex = clinicalCount;
        var newLiStr = "<li id=\"c3_li_" + clinicalCount + "\" class=\"drug-li\" onclick=\"handleTargetInfo2('" + clinicalCount + "')\">新建药物-新建临床试验ID</li>";
        $("#c3_tdd_drug_name").val("新建药物");
        clinicalList.push({"clinical_trial_id": "新建临床试验ID", "drug_name": "新建药物", "status": "add"});
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
        if (clinicalIndex > 0) {
            clinicalList[clinicalIndex - 1].status = 'delete';
            $("#c3_tdd_drug_name").val("");
            $("#clinical_trial_id").val("");
            $("#title_chinese").val("");
            $("#condition_chinese").val("");
            $("#phase").val("");
            $("#location_chinese").val("");
            $("#inclusion_criteria").val("");
            $("#exclusion_criteria").val("");
            $("#c3_li_" + clinicalIndex).remove();
            clinicalIndex = 0;
        }
    }

    // 抓取临床试验信息
    function getDrug2() {
        if (clinicalIndex > 0) {
            var clinical_trial_id = encodeURIComponent(clinicalList[clinicalIndex - 1].clinical_trial_id);
            var drug_name = encodeURIComponent(clinicalList[clinicalIndex - 1].drug_name);
            $.post("${pageContext.request.contextPath}/geneMarkerVw/getClinicalInfo?clinical_trial_id=" + clinical_trial_id + "&lang=${lang}&drug_name=" + drug_name, function (returnData) {
                if (returnData && !returnData.isError) {
                    swal("成功！", "获取数据成功", "success");
                    clinicalList[clinicalIndex - 1].title = returnData.title;
                    clinicalList[clinicalIndex - 1].recruiting_condition = returnData.recruiting_condition;
                    clinicalList[clinicalIndex - 1].phase = returnData.phase;
                    clinicalList[clinicalIndex - 1].location = returnData.location;
                    clinicalList[clinicalIndex - 1].inclusion_criteria = returnData.inclusion_criteria;
                    clinicalList[clinicalIndex - 1].exclusion_criteria = returnData.exclusion_criteria;
                    clinicalList[clinicalIndex - 1].status = returnData.status;
                    clinicalList[clinicalIndex - 1].cfda = returnData.cfda
                    handleTargetInfo2(clinicalIndex);
                }
            });
        }
    }

    // 保存临床试验信息
    function saveDrug2() {
        var medicine = medicineList[varDrugIndex - 1];
        var ori_variant = encodeURIComponent(medicine.ori_variant);
        $.ajax({
            type: "post",
            url: "${pageContext.request.contextPath}/geneMarkerVw/saveClinicalRecord?userAccount=${user.user_account}&gene=" + medicine.gene + "&ori_variant=" + ori_variant + "&disease_id=${diseaseId}&record_id=" + medicine.record_id + "&lang=${lang}&gender=${sampleFile.gender}",
            data: JSON.stringify(clinicalList),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                if (data && !data.isError) {
                    swal("成功！", "保存成功", "success");
                    clinicalList = data;
                    medicineList[varDrugIndex - 1].clinicalList = clinicalList;
                    medicineList[varDrugIndex - 1].check_date = null;
                    initTddDialog(varDrugIndex);
                    $("#result_check_date_" + varDrugIndex).html("未审核");
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
            error: function (data) {

            }
        })
    }

    // 处理临床试验药物列表的点击事件
    function handleTargetInfo2(index) {
        clinicalIndex = index;
        var clinicalDrug = clinicalList[index - 1];
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
        $("#chemo-sideeffects-effectiveness-table").html("");
        $("#target-drug-analysis-tables").html("");
        $("#unknown-gene-analysis-tables").html("");
        $("#chemo-sideeffects-analysis-tables").html("");
        $("#chemo--effectiveness-analysis-tables").html("");
        $("#cancer-risk-analysis-tables").html("");
    }

    // 初始化靶向药物信息列表
    function initTargetDrugTable() {
        var table = $("#target-drug-table");
        var drugCount = 0;
        $.each(medicineList, function (k, v) {
            if (v.drugList && v.drugList.length > 0) {
                var drugList = v.drugList;
                var gene_str = "<td>" + v.gene + "</td>";
                var mutation_str = "<td>" + removeMutations(v.ori_variant) + "</td>";
                var freq_str = "";
                if (mutation_str.indexOf("Amplification") < 0 && v.mutFreq != '.' && v.mutFreq.indexOf("合") < 0 && v.mutFreq.indexOf("H") < 0) {
                    freq_str = "<td>" + v.mutFreq + "%</td>";
                } else {
                    freq_str = "<td>" + v.mutFreq + "</td>";
                }
                var druga_str = "";
                var drugb_str = "";
                var drugc_str = "";
                var drugd_str = "";
                var resistanta_str = "";
                var resistantb_str = "";
                var resistantc_str = "";
                var resistantd_str = "";
                $.each(drugList, function (kk, vv) {
                    if (!vv.status || vv.status != 'delete') {
                        var drug_name = vv.drug_name;
                        if (vv.cfda == '1') {
                            drug_name += "*";
                        }
                        if (vv.recruiting == '1') {
                            drug_name += "#";
                        }
                        if (vv.approval_desc) {
                            drug_name = "<b>" + drug_name + "</b>";
                        }
                        if (vv.approve_range == '1') {
                            druga_str += drug_name + '，';
                        }
                        if (vv.approve_range == '2') {
                            drugb_str += drug_name + '，';
                        }
                        if (vv.approve_range == '3') {
                            drugc_str += drug_name + '，';
                        }
                        if (vv.approve_range == '4') {
                            drugd_str += drug_name + '，';
                        }
                        if (vv.approve_range == '5') {
                            resistanta_str += drug_name + '，';
                        }
                        if (vv.approve_range == '6') {
                            resistantb_str += drug_name + '，';
                        }
                        if (vv.approve_range == '7') {
                            resistantc_str += drug_name + '，';
                        }
                        if (vv.approve_range == '8') {
                            resistantd_str += drug_name + '，';
                        }
                    }
                });
                druga_str = "<td>" + (druga_str.substr(0, druga_str.length - 1)) + "</td>";
                drugb_str = "<td>" + (drugb_str.substr(0, drugb_str.length - 1)) + "</td>";
                drugc_str = "<td>" + (drugc_str.substr(0, drugc_str.length - 1)) + "</td>";
                drugd_str = "<td>" + (drugd_str.substr(0, drugd_str.length - 1)) + "</td>";
                resistanta_str = "<td>" + (resistanta_str.substr(0, resistanta_str.length - 1)) + "</td>";
                resistantb_str = "<td>" + (resistantb_str.substr(0, resistantb_str.length - 1)) + "</td>";
                resistantc_str = "<td>" + (resistantc_str.substr(0, resistantc_str.length - 1)) + "</td>";
                resistantd_str = "<td>" + (resistantd_str.substr(0, resistantd_str.length - 1)) + "</td>";
                table.append("<tr name='target-drug-tr'>" + gene_str + mutation_str + freq_str + druga_str + drugb_str + drugc_str + drugd_str + resistanta_str + resistantb_str + resistantc_str + resistantd_str + "</tr>")
                drugCount++;
            }
        });
        if (drugCount == 0) {
            table.append("<tr name='target-drug-tr'><td>/</td><td>/</td><td>/</td><td>/</td><td>/</td><td>/</td><td>/</td></tr>")
        }

    }

    // 初始化未知临床意义的基因突变表格
    function initUnknownMutTable() {
        var table = $("#unknown-mut-table");
        var unknownCount = 0;
        $.each(medicineList, function (k, v) {
            if (v.rpUnknownVar && (!v.drugList || v.drugList.length <= 0) && v.rpUnknownVar.result_type == '未知临床意义') {
                if (v.gene == "Complex") return;
                var gene_str = "<td>" + v.gene + "</td>";
                var mutation_str = "<td>" + removeMutations(v.ori_variant) + "</td>";
                var mutation_type_str = "<td>" + translateMutType(v.ExonicFunc) + "</td>";
                var freq_str = "";
                if (mutation_str.indexOf("Amplification") < 0 && v.mutFreq != '.' && v.mutFreq.indexOf("合") < 0) {
                    freq_str = "<td>" + v.mutFreq + "%</td>";
                } else {
                    freq_str = "<td>" + v.mutFreq + "</td>";
                }

                table.append("<tr name='unknown-mut-tr'>" + gene_str + mutation_str + mutation_type_str + freq_str + "</tr>")
                unknownCount++;
            }
        });
        if (unknownCount == 0) {
            table.append("<tr name='unknown-mut-tr'><td>/</td><td>/</td><td>/</td><td>/</td></tr>")
        }
    }

    // 初始化化疗药物毒副作用风险及有效性预测表格
    function initChemoSideEffectsAndEffectivenessTable() {
        var chemo_this_str = "";
        var chemo_unknown_str = "";
        $.each(chemo_this, function (k, v) {
            chemo_this_str += "<tr>";
            $.each(v, function (kk, vv) {
                if (k == 0) {
                    chemo_this_str += "<th>" + vv + "</th>";
                } else {
                    chemo_this_str += "<td>" + vv + "</td>";
                }
            });
            chemo_this_str += "</tr>";
        });
        $.each(chemo_unknown, function (k, v) {
            chemo_unknown_str += "<tr>";
            $.each(v, function (kk, vv) {
                if (k == 0) {
                    chemo_unknown_str += "<th style='border-top-width:1px'>" + vv + "</th>";
                } else {
                    chemo_unknown_str += "<td>" + vv + "</td>";
                }
            });
            chemo_unknown_str += "</tr>";
        })
        $("#chemo-sideeffects-effectiveness-table").append(chemo_this_str);
        $("#chemo-sideeffects-effectiveness-table").append(chemo_unknown_str);
    }

    // 初始化肿瘤遗传风险检测表格
    function initGeneticCancerRisk() {
        $.each(crAllList, function (k, v) {
            if (v.rpCr) {
                $("#genetic-cancer-risk-table tr").eq(k + 1).find('td').eq(8).html(translateClinicalSignificance(v.rpCr.Clinical_significance));
            } else {
                $("#genetic-cancer-risk-table tr").eq(k + 1).find('td').eq(8).html("-");
            }
        })

    }

    // 初始化靶向药物检测解析表格
    function initTargetDrugAnalysisTable() {
        var count = 0;
        $.each(medicineList, function (k, v) {
            if ((v.drugList && v.drugList.length > 0) || (v.clinicalList && v.clinicalList.length > 0)) {
                var drugList = v.drugList;
                var druga_str = "";
                var drugb_str = "";
                var drugc_str = "";
                var drugd_str = "";
                var resistanta_str = "";
                var resistantb_str = "";
                var resistantc_str = "";
                var resistantd_str = "";

                var drugInfo_str = "";
                var clinicalInfo_str = "";
                var drug_Blod = [];
                $.each(drugList, function (kk, vv) {
                    if (!vv.status || vv.status != 'delete') {
                        var drug_name = vv.drug_name;
                        if (vv.cfda == '1') {
                            drug_name += "*";
                        }
                        if (vv.approval_desc) {
                            drug_Blod.push(drug_name);
                        }
                        if (vv.recruiting == '1') {
                            drug_name += "#";
                        }
                        if (vv.approve_range != '5' && vv.approve_range != '6' && vv.approve_range != '7' && vv.approve_range != '8' && vv.approval_desc) {
                            drugInfo_str += "<tr><td>" + drug_name + "</td>";
                            drugInfo_str += "<td>" + vv.approval_desc + "</td></tr>";
                        }
                        if (vv.approval_desc) {
                            drug_name = "<b>" + drug_name + "</b>";
                        }
                        if (vv.approve_range == '1') {
                            druga_str += drug_name + '，';
                        }
                        if (vv.approve_range == '2') {
                            drugb_str += drug_name + '，';
                        }
                        if (vv.approve_range == '3') {
                            drugc_str += drug_name + '，';
                        }
                        if (vv.approve_range == '4') {
                            drugd_str += drug_name + '，';
                        }
                        if (vv.approve_range == '5') {
                            resistanta_str += drug_name + '，';
                        }
                        if (vv.approve_range == '6') {
                            resistantb_str += drug_name + '，';
                        }
                        if (vv.approve_range == '7') {
                            resistantc_str += drug_name + '，';
                        }
                        if (vv.approve_range == '8') {
                            resistantd_str += drug_name + '，';
                        }
                    }
                });
                var template_str = $("#target-drug-analysis-gene-template").html();
                var mutation_str = v.ori_variant;
                var freq_str = '';
                if (mutation_str.indexOf("Amplification") < 0 && v.mutFreq != '.') {
                    freq_str = v.mutFreq + "%";
                } else {
                    freq_str = v.mutFreq;
                }
                template_str = template_str.replace(">_gene<", ">" + v.gene + "<");
                template_str = template_str.replace(">_mutation<", ">" + removeMutations(v.ori_variant) + "<");
                template_str = template_str.replace(">_mutFreq<", ">" + freq_str + "<");
                template_str = template_str.replace(">_drugsA<", ">" + druga_str.substr(0, druga_str.length - 1) + "<");
                template_str = template_str.replace(">_drugsB<", ">" + drugb_str.substr(0, drugb_str.length - 1) + "<");
                template_str = template_str.replace(">_drugsC<", ">" + drugc_str.substr(0, drugc_str.length - 1) + "<");
                template_str = template_str.replace(">_drugsD<", ">" + drugd_str.substr(0, drugd_str.length - 1) + "<");
                template_str = template_str.replace(">_resistantA<", ">" + resistanta_str.substr(0, resistanta_str.length - 1) + "<");
                template_str = template_str.replace(">_resistantB<", ">" + resistantb_str.substr(0, resistantb_str.length - 1) + "<");
                template_str = template_str.replace(">_resistantC<", ">" + resistantc_str.substr(0, resistantc_str.length - 1) + "<");
                template_str = template_str.replace(">_resistantD<", ">" + resistantd_str.substr(0, resistantd_str.length - 1) + "<");
                var str = "";
                $.each($.parseJSON(v.varDrugNote), function (i, val) {
                    var txt = val.value.trim();
                    txt = txt.replace(/(^\s*)|(\s*$)/g, "");
                    if (txt != null && txt != "") {
                        if (val.key == "recommend:") {
                            //str+="<b>"+txt+"</b>";
                        } else {
                            str += "<b>" + val.key + "</b>" + txt + "<br/>";
                        }
                    }
                });
                if (v.clinicalList && v.clinicalList.length > 0) {
                    str += "<b>推荐下表所示的临床试验。</b>";
                }
                template_str = template_str.replace(">_drugNote<", ">" + (str || '') + "<");
                //template_str = template_str.replace("_druginfo",drugInfo_str);
                $("#target-drug-analysis-tables").append(template_str);
                if (drugInfo_str) {
                    $("table[name=target-drug-analysis-druginfo-table]").eq(count).append(drugInfo_str);
                } else {
                    $("div[name=target-drug-analysis-druginfo-table-div]").eq(count).hide();
                }

                if (v.clinicalList && v.clinicalList.length > 0) {
                    var clinicalList = v.clinicalList;
                    $.each(clinicalList, function (kk, vv) {
                        if (!vv.status || vv.status != 'delete') {
                            var drug_name = vv.drug_name;
                            if (vv.cfda == '1') {
                                drug_name += "*";
                            }
                            if (drug_Blod.indexOf(drug_name) != -1) {
                                drug_name = "<b>" + drug_name + "</b>";
                            }
                            clinicalInfo_str += "<tr><td>" + vv.clinical_trial_id + "</td>";
                            clinicalInfo_str += "<td>" + vv.title + "</td>";
                            clinicalInfo_str += "<td>" + vv.recruiting_condition + "</td>";
                            clinicalInfo_str += "<td>" + translatePhase(vv.phase) + "</td>";
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

    // 初始化未知临床意义检测解析表格
    function initUnknownGeneAnalysisTable() {
        $.each(medicineList, function (k, v) {
            if (v.rpUnknownVar && v.rpUnknownVar.result_type == '未知临床意义') {
                var template_str = $("#unknown-gene-analysis-template").html();
                template_str = template_str.replace(">_gene<", ">" + v.gene + "<");
                template_str = template_str.replace(">_mutation<", ">" + removeMutations(v.ori_variant) + "<");
                template_str = template_str.replace(">_mutDesc<", ">" + v.mutDesc + "<");
                template_str = template_str.replace(">_geneDesc<", ">" + v.rpUnknownVar.gene_description + "<");
                /*var str = "";
				if ($.parseJSON(v.rpUnknownVar.var_drug_desc) != null) {
					$.each($.parseJSON(v.rpUnknownVar.var_drug_desc),function(i,val){
						var txt = val.value.trim();
						txt = txt.replace(/(^\s*)|(\s*$)/g, "");
						if(txt != null && txt != ""){
							str+=txt;
						}
					});
				}
				template_str = template_str.replace(">_drugNote<",">"+(str||'')+"<");*/
                //template_str = template_str.replace("_druginfo",drugInfo_str);
                $("#unknown-gene-analysis-tables").append(template_str);
            }
        });
    }

    // 初始化化疗药物毒副作用解析和化疗药物有效性解析表格
    function initEffectivenessAndSideEffects() {
        var chemo_sideeffects_str = "";
        var chemo_effectiveness_str = "";
        $.each(chemo_sideeffects, function (k, v) {
            chemo_sideeffects_str += "<tr>";
            var i = 0;
            var flag = false;
            $.each(v, function (kk, vv) {
                i += 1;
                if (k == 0) {
                    if (vv == '等级') {
                        chemo_sideeffects_str += "<th style='width: 5%;'>" + vv + "</th>";
                    } else if (vv == '类别' || vv == '检测结果') {
                        chemo_sideeffects_str += "<th style='width: 8%;'>" + vv + "</th>";
                    } else {
                        chemo_sideeffects_str += "<th>" + vv + "</th>";
                    }
                } else {
                    if (i == 1 && vv == '-') {
                        flag = true;
                    } else if (i == 2 && flag) {
                        chemo_sideeffects_str += '<td colspan="2">' + vv + '</td>';
                    } else {
                        chemo_sideeffects_str += "<td>" + vv + "</td>";
                    }
                }
            });
            chemo_sideeffects_str += "</tr>";
        });
        $.each(chemo_effectiveness, function (k, v) {
            chemo_effectiveness_str += "<tr>";
            var i = 0;
            var flag = false;
            $.each(v, function (kk, vv) {
                i += 1;
                if (k == 0) {
                    if (vv == '等级') {
                        chemo_effectiveness_str += "<th style='width: 5%;'>" + vv + "</th>";
                    } else if (vv == '类别' || vv == '检测结果') {
                        chemo_effectiveness_str += "<th style='width: 8%;'>" + vv + "</th>";
                    } else {
                        chemo_effectiveness_str += "<th>" + vv + "</th>";
                    }
                } else {
                    if (i == 1 && vv == '-') {
                        flag = true;
                    } else if (i == 2 && flag) {
                        chemo_effectiveness_str += '<td colspan="2">' + vv + '</td>';
                    } else {
                        chemo_effectiveness_str += "<td>" + vv + "</td>";
                    }
                }
            });
            chemo_effectiveness_str += "</tr>";
        })
        $("#chemo-sideeffects-analysis-tables").append(chemo_sideeffects_str);
        $("#chemo-effectiveness-analysis-tables").append(chemo_effectiveness_str);
    }

    // 初始化遗传风险检测解析表格
    function initCancerRiskAnalysisTable() {
        var crCount = 0;
        $.each(crAllList, function (k, v) {
            if (v.rpCr && (v.rpCr.Clinical_significance == '1' || v.rpCr.Clinical_significance == '2')) {
                var template_str = $("#cancer-risk-analysis-template").html();
                template_str = template_str.replace(">_gene<", ">" + v.Gene + "<");
                template_str = template_str.replace(">_mutation<", ">" + removeMutations(get_ori_variant(v)) + "<");
                template_str = template_str.replace(">_mutDesc<", ">" + v.mutDesc + "<");
                template_str = template_str.replace(">_geneDesc<", ">" + (v.rpCr ? (v.rpCr.GeneDesc || '') : "") + "<");
                template_str = template_str.replace(">_clianno<", ">" + (v.rpCr ? (v.rpCr.VarClianno || '') : "") + "<");
                //template_str = template_str.replace("_druginfo",drugInfo_str);
                $("#cancer-risk-analysis-tables").append(template_str);
                crCount++;
            }
        });
        if (crCount == 0) {
            $("#cancer-risk-analysis-div").hide();
        } else {
            $("#cancer-risk-analysis-div").show();
        }
    }

    // 翻译临床意义
    function translateClinicalSignificance(Clinical_significance) {
        switch (Clinical_significance) {
            case 1:
                return '致病';
            case 2:
                return '可能致病';
            case 3:
                return '临床意义未明';
            case 4:
                return '可能良性';
            case 5:
                return '良性';
            default:
                return '-';
        }
    }

    // 翻译突变类型
    function translateMutType(ExonicFunc) {
        switch (ExonicFunc) {
            case "nonsynonymous SNV":
                return "错义突变";
            case "synonymous SNV":
                return "同义突变";
            case "nonframeshift insertion":
                return "非移码突变";
            case "nonframeshift deletion":
                return "非移码突变";
            case "frameshift deletion":
                return "移码突变";
            case "frameshift insertion":
                return "移码突变";
            case "frameshift indel":
                return "移码突变";
            case "nonframeshift indel":
                return "非移码突变";
            case "stopgain":
                return "无义突变";
            case "stoploss":
                return "stoploss";
            case "splicing":
                return "剪接突变";
            case "promoter":
                return "启动子区变异";
            case "unknown":
                return "未知";
            default:
                return ExonicFunc;
        }
    }

    function removeMutations(variant) {
        if (variant.indexOf(" (") != -1) {
            variant = variant.substr(0, variant.indexOf(" ("));
        }
        return variant;
    }

    function translateSampleType(sample_type) {
        switch (sample_type) {
            case "tissue":
                return "组织";
            case "blood":
                return "血液";
            default:
                return sample_type;
        }
    }

    // 翻译阶段
    function translatePhase(phase) {
        switch (phase) {
            case "Phase IV":
                return "IV期";
            case "Phase III":
                return "III期";
            case "Phase II/III":
                return "II/III期";
            case "Phase II":
                return "II期";
            case "Phase I/II":
                return "I/II期";
            case "Phase I":
                return "I期";
            default:
                return "未知";
        }
    }

    //监听滚动条滚动
    window.onscroll = function () {
        var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;//滚动条距离顶部的距离
        var select2 = document.getElementById('select2')//下拉框
        if (scrollTop > 500) {//如果滚动条距离顶部大于100
            select2.style.position = 'fixed';
            select2.style.top = '500px';
        } else {
            select2.style.position = 'static';
        }
    }

    $(function () {
        $("#userForm").validate({
            rules: {
                "tumorcellcontent": {"required": true},
                "DNA_total": {"required": true},
                "DNA_degradation": {"required": true},
                "outbound_quantity": {"required": true},
                "sequencing_depth": {"required": true},
                "coverage_uniformity": {"required": true},
                "genome_alignment": {"required": true},
                "base_quality": {"required": true},
                "RNA_total": {"required": true},
                "RNA_degradation": {"required": true},
                "rna_outbound_quantity": {"required": true},
                "total_reads": {"required": true},
                "rna_base_quality": {"required": true},
                "hrd_DNA_total": {"required": true},
                "hrd_DNA_degradation": {"required": true},
                "hrd_outbound_quantity": {"required": true},
                "hrd_sequencing_depth": {"required": true},
                "hrd_coverage_uniformity": {"required": true}
            },
            messages: {
                "tumorcellcontent": {"required": "该字段不能为空"},
                "DNA_total": {"required": "该字段不能为空"},
                "DNA_degradation": {"required": "该字段不能为空"},
                "outbound_quantity": {"required": "该字段不能为空"},
                "sequencing_depth": {"required": "该字段不能为空"},
                "coverage_uniformity": {"required": "该字段不能为空"},
                "genome_alignment": {"required": "该字段不能为空"},
                "base_quality": {"required": "该字段不能为空"},
                "RNA_total": {"required": "该字段不能为空"},
                "RNA_degradation": {"required": "该字段不能为空"},
                "rna_outbound_quantity": {"required": "该字段不能为空"},
                "total_reads": {"required": "该字段不能为空"},
                "rna_base_quality": {"required": "该字段不能为空"},
                "hrd_DNA_total": {"required": "该字段不能为空"},
                "hrd_DNA_degradation": {"required": "该字段不能为空"},
                "hrd_outbound_quantity": {"required": "该字段不能为空"},
                "hrd_sequencing_depth": {"required": "该字段不能为空"},
                "hrd_coverage_uniformity": {"required": "该字段不能为空"}
            },
            submitHandler: function () {
                $.ajax({
                    cache: false,
                    type: "POST",
                    url: "${pageContext.request.contextPath}/filterQc/updateQc", //把表单数据发送到ajax.jsp
                    data: $('#userForm').serialize(), //要发送的是ajaxFrm表单中的数据
                    success: function (data) {
                        if (data.success) {
                            alert("数据保存成功!");
                        } else {
                            alert("数据保存失败!");
                        }
                    }
                });
            }
        });
    });

</script>
<form id="userForm">
    <input type="hidden" name="flag" value="${flag }"/>
    <input type="hidden" name="file_id" value="${qc.file_id }"/>
    <input type="hidden" name="rna_file_id" value="${rna.file_id }"/>
    <input type="hidden" name="hrd_file_id" value="${hrd.file_id }"/>
    <input type="hidden" name="subbarcode" value="${currentNgsAvailable.subbarcode }"/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">肿瘤细胞含量</td>
            <td width="10%" class="tableleft">
                <c:choose>
                    <c:when test="${qc != null}">
                        <input type="text" id="tumorcellcontent" name="tumorcellcontent" style="color: red;"
                               value="${qc.tumorcellcontent }"/>
                    </c:when>
                    <c:when test="${rna != null}">
                        <input type="text" id="tumorcellcontent" name="tumorcellcontent" style="color: red;"
                               value="${rna.tumorcellcontent }"/>
                    </c:when>
                    <c:when test="${hrd != null}">
                        <input type="text" id="tumorcellcontent" name="tumorcellcontent" style="color: red;"
                               value="${hrd.tumorcellcontent }"/>
                    </c:when>
                </c:choose>
                <label class="error" for="tumorcellcontent" generated="true" style="color: red;"></label>
            </td>
            <td>≥10%</td>
            <c:choose>
                <c:when test="${qualityType == '白细胞' || type == 'blood'}">
                    <script>
                        var tumorcellcontent = $("#tumorcellcontent").val();
                        if (tumorcellcontent.includes('%') && tumorcellcontent.replace(/%/g, '') >= 10 && tumorcellcontent.replace(/%/g, '') <= 100 || tumorcellcontent == "不适用") {
                            document.getElementById('tumorcellcontent').style.color = '';
                        }
                    </script>
                </c:when>
                <c:when test="${type == 'tissue'}">
                    <script>
                        var tumorcellcontent = $("#tumorcellcontent").val();
                        if (tumorcellcontent.includes('%') && tumorcellcontent.replace(/%/g, '') >= 10 && tumorcellcontent.replace(/%/g, '') <= 100 || tumorcellcontent == "/" || tumorcellcontent == "-") {
                            document.getElementById('tumorcellcontent').style.color = '';
                        }
                    </script>
                </c:when>
            </c:choose>
        </tr>
        <c:if test="${qc != null}">
            <tr>
                <td width="10%" class="tableleft">DNA总量（ng）</td>
                <td><input type="text" id="DNA_total" name="DNA_total" style="color: red;"
                           value="${qc.DNA_total }"/><label class="error" for="DNA_total" generated="true"
                                                            style="color: red;"></label></td>
                <td>
                    <c:choose>
                        <c:when test="${qualityType == '白细胞'}">≥100
                            <script>
                                var DNA_total = $("#DNA_total").val();
                                if (DNA_total >= 100) {
                                    document.getElementById('DNA_total').style.color = '';
                                }
                            </script>
                        </c:when>
                        <c:when test="${type == 'tissue'}">≥100
                            <script>
                                var DNA_total = $("#DNA_total").val();
                                if (DNA_total >= 100) {
                                    document.getElementById('DNA_total').style.color = '';
                                }
                            </script>
                        </c:when>
                        <c:when test="${type == 'blood'}">≥20
                            <script>
                                var DNA_total = $("#DNA_total").val();
                                if (DNA_total >= 20) {
                                    document.getElementById('DNA_total').style.color = '';
                                }
                            </script>
                        </c:when>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA降解程度</td>
                <td><input type="text" id="DNA_degradation" name="DNA_degradation" style="color: red;"
                           value="${qc.DNA_degradation }"/><label class="error" for="DNA_degradation" generated="true"
                                                                  style="color: red;"></label></td>
                <td>A/B/C级</td>
                <script>
                    var DNA_degradation = $("#DNA_degradation").val();
                    if (DNA_degradation == "A" || DNA_degradation == "B" || DNA_degradation == "C") {
                        document.getElementById('DNA_degradation').style.color = '';
                    }
                </script>
                <c:choose>
                    <c:when test="${qualityType == '白细胞' || type == 'blood'}">
                        <script>
                            var DNA_degradation = $("#DNA_degradation").val();
                            if (DNA_degradation == "A" || DNA_degradation == "B" || DNA_degradation == "C" || DNA_degradation == "不适用") {
                                document.getElementById('DNA_degradation').style.color = '';
                            }
                        </script>
                    </c:when>
                    <c:when test="${type == 'tissue'}">
                        <script>
                            var DNA_degradation = $("#DNA_degradation").val();
                            if (DNA_degradation == "A" || DNA_degradation == "B" || DNA_degradation == "C" || DNA_degradation == "/" || DNA_degradation == "-") {
                                document.getElementById('DNA_degradation').style.color = '';
                            }
                        </script>
                    </c:when>
                </c:choose>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA预文库总量（ng）</td>
                <td><input type="text" id="outbound_quantity" name="outbound_quantity" style="color: red;"
                           value="${qc.outbound_quantity }"/><label class="error" for="outbound_quantity"
                                                                    generated="true" style="color: red;"></label></td>
                <td>≥500</td>
                <script>
                    var outbound_quantity = $("#outbound_quantity").val();
                    if (outbound_quantity >= 500) {
                        document.getElementById('outbound_quantity').style.color = '';
                    }
                </script>
            </tr>
        </c:if>
        <c:if test="${rna != null}">
            <tr>
                <td width="10%" class="tableleft">RNA总量（ng）</td>
                <td><input type="text" id="RNA_total" name="RNA_total" style="color: red;"
                           value="${rna.RNA_total }"/><label class="error" for="RNA_total" generated="true"
                                                             style="color: red;"></label></td>
                <td>≥100</td>
                <script>
                    var RNA_total = $("#RNA_total").val();
                    if (RNA_total >= 100) {
                        document.getElementById('RNA_total').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">RNA降解程度（DV200）</td>
                <td><input type="text" id="RNA_degradation" name="RNA_degradation" style="color: red;"
                           value="${rna.RNA_degradation }"/><label class="error" for="RNA_degradation" generated="true"
                                                                   style="color: red;"></label></td>
                <td>≥30 (2100) <br>≥70 (QSEP)</td>
                <script>
                    var RNA_degradation = $("#RNA_degradation").val();
                    if (RNA_degradation >= 30) {
                        document.getElementById('RNA_degradation').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">RNA预文库总量（ng）</td>
                <td><input type="text" id="rna_outbound_quantity" name="rna_outbound_quantity" style="color: red;"
                           value="${rna.outbound_quantity }"/><label class="error" for="rna_outbound_quantity"
                                                                     generated="true" style="color: red;"></label></td>
                <td>≥500</td>
                <script>
                    var rna_outbound_quantity = $("#rna_outbound_quantity").val();
                    if (rna_outbound_quantity >= 500) {
                        document.getElementById('rna_outbound_quantity').style.color = '';
                    }
                </script>
            </tr>
        </c:if>
        <c:if test="${hrd != null && qc == null}">
            <tr>
                <td width="10%" class="tableleft">DNA总量（ng）</td>
                <td><input type="text" id="hrd_DNA_total" name="hrd_DNA_total" style="color: red;"
                           value="${hrd.DNA_total }"/><label class="error" for="hrd_DNA_total" generated="true"
                                                             style="color: red;"></label></td>
                <td>≥200</td>
                <script>
                    var hrd_DNA_total = $("#hrd_DNA_total").val();
                    if (hrd_DNA_total >= 200) {
                        document.getElementById('hrd_DNA_total').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA降解程度</td>
                <td><input type="text" id="hrd_DNA_degradation" name="hrd_DNA_degradation" style="color: red;"
                           value="${hrd.DNA_degradation }"/><label class="error" for="hrd_DNA_degradation"
                                                                   generated="true" style="color: red;"></label></td>
                <td>A/B/C级</td>
                <script>
                    var hrd_DNA_degradation = $("#hrd_DNA_degradation").val();
                    if (hrd_DNA_degradation == "A" || hrd_DNA_degradation == "B" || hrd_DNA_degradation == "C") {
                        document.getElementById('hrd_DNA_degradation').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA预文库总量（ng）</td>
                <td><input type="text" id="hrd_outbound_quantity" name="hrd_outbound_quantity" style="color: red;"
                           value="${hrd.outbound_quantity }"/><label class="error" for="hrd_outbound_quantity"
                                                                     generated="true" style="color: red;"></label></td>
                <td>≥500</td>
                <script>
                    var hrd_outbound_quantity = $("#hrd_outbound_quantity").val();
                    if (hrd_outbound_quantity >= 500) {
                        document.getElementById('hrd_outbound_quantity').style.color = '';
                    }
                </script>
            </tr>
        </c:if>
        <c:if test="${hrd != null}">
            <tr>
                <td width="10%" class="tableleft">HRD平均测序深度</td>
                <td><input type="text" id="hrd_sequencing_depth" name="hrd_sequencing_depth" style="color: red;"
                           value="${hrd.sequencing_depth }"/><label class="error" for="hrd_sequencing_depth"
                                                                    generated="true" style="color: red;"></label></td>
                <td>≥200</td>
                <script>
                    var hrd_sequencing_depth = $("#hrd_sequencing_depth").val();
                    if (hrd_sequencing_depth >= 200) {
                        document.getElementById('hrd_sequencing_depth').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">HRD覆盖均一性</td>
                <td><input type="text" id="hrd_coverage_uniformity" name="hrd_coverage_uniformity" style="color: red;"
                           value="${hrd.coverage_uniformity }"/><label class="error" for="hrd_coverage_uniformity"
                                                                       generated="true" style="color: red;"></label>
                </td>
                <td>≥80%</td>
                <script>
                    var hrd_coverage_uniformity = $("#hrd_coverage_uniformity").val();
                    if (hrd_coverage_uniformity.includes('%') && hrd_coverage_uniformity.replace(/%/g, '') >= 80 && hrd_coverage_uniformity.replace(/%/g, '') <= 100) {
                        document.getElementById('hrd_coverage_uniformity').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">HRD基因组比对率</td>
                <td><input type="text" id="hrd_genome_alignment" name="hrd_genome_alignment" style="color: red;"
                           value="${hrd.genome_alignment }"/><label class="error" for="hrd_genome_alignment"
                                                                    generated="true" style="color: red;"></label></td>
                <td>≥95%</td>
                <script>
                    var hrd_genome_alignment = $("#hrd_genome_alignment").val();
                    if (hrd_genome_alignment.includes('%') && hrd_genome_alignment.replace(/%/g, '') >= 95 && hrd_genome_alignment.replace(/%/g, '') <= 100) {
                        document.getElementById('hrd_genome_alignment').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">HRD碱基质量Q30占比</td>
                <td><input type="text" id="hrd_base_quality" name="hrd_base_quality" style="color: red;"
                           value="${hrd.base_quality }"/><label class="error" for="hrd_base_quality" generated="true"
                                                                style="color: red;"></label></td>
                <td>≥80%</td>
                <script>
                    var hrd_base_quality = $("#hrd_base_quality").val();
                    if (hrd_base_quality.includes('%') && hrd_base_quality.replace(/%/g, '') >= 80 && hrd_base_quality.replace(/%/g, '') <= 100) {
                        document.getElementById('hrd_base_quality').style.color = '';
                    }
                </script>
            </tr>
        </c:if>
        <c:if test="${qc != null}">
            <tr>
                <td width="10%" class="tableleft">DNA平均测序深度</td>
                <td><input type="text" id="sequencing_depth" name="sequencing_depth" style="color: red;"
                           value="${qc.sequencing_depth }"/><label class="error" for="sequencing_depth" generated="true"
                                                                   style="color: red;"></label></td>
                <td>
                    <c:choose>
                        <c:when test="${qualityType == '白细胞'}">≥100
                            <script>
                                var sequencing_depth = $("#sequencing_depth").val();
                                if (sequencing_depth >= 100) {
                                    document.getElementById('sequencing_depth').style.color = '';
                                }
                            </script>
                        </c:when>
                        <c:when test="${type == 'tissue'}">≥500
                            <script>
                                var sequencing_depth = $("#sequencing_depth").val();
                                if (sequencing_depth >= 500) {
                                    document.getElementById('sequencing_depth').style.color = '';
                                }
                            </script>
                        </c:when>
                        <c:when test="${type == 'blood'}">≥1500
                            <script>
                                var sequencing_depth = $("#sequencing_depth").val();
                                if (sequencing_depth >= 1500) {
                                    document.getElementById('sequencing_depth').style.color = '';
                                }
                            </script>
                        </c:when>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA覆盖均一性</td>
                <td><input type="text" id="coverage_uniformity" name="coverage_uniformity" style="color: red;"
                           value="${qc.coverage_uniformity }"/><label class="error" for="coverage_uniformity"
                                                                      generated="true" style="color: red;"></label></td>
                <td>≥90%</td>
                <script>
                    var coverage_uniformity = $("#coverage_uniformity").val();
                    if (coverage_uniformity.includes('%') && coverage_uniformity.replace(/%/g, '') >= 90 && coverage_uniformity.replace(/%/g, '') <= 100) {
                        document.getElementById('coverage_uniformity').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA基因组比对率</td>
                <td><input type="text" id="genome_alignment" name="genome_alignment" style="color: red;"
                           value="${qc.genome_alignment }"/><label class="error" for="genome_alignment" generated="true"
                                                                   style="color: red;"></label></td>
                <td>≥95%</td>
                <script>
                    var genome_alignment = $("#genome_alignment").val();
                    if (genome_alignment.includes('%') && genome_alignment.replace(/%/g, '') >= 95 && genome_alignment.replace(/%/g, '') <= 100) {
                        document.getElementById('genome_alignment').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA碱基质量Q30占比</td>
                <td><input type="text" id="base_quality" name="base_quality" style="color: red;"
                           value="${qc.base_quality }"/><label class="error" for="base_quality" generated="true"
                                                               style="color: red;"></label></td>
                <td>≥80%</td>
                <script>
                    var base_quality = $("#base_quality").val();
                    if (base_quality.includes('%') && base_quality.replace(/%/g, '') >= 80 && base_quality.replace(/%/g, '') <= 100) {
                        document.getElementById('base_quality').style.color = '';
                    }
                </script>
            </tr>
        </c:if>
        <c:if test="${rna != null}">
            <tr>
                <td width="10%" class="tableleft">RNA测序总reads数（条）</td>
                <td><input type="text" id="total_reads" name="total_reads" style="color: red;"
                           value="${rna.total_reads }"/><label class="error" for="total_reads" generated="true"
                                                               style="color: red;"></label></td>
                <td>≥12,000,000</td>
                <script>
                    var total_reads = $("#total_reads").val();
                    if (total_reads >= 12000000) {
                        document.getElementById('total_reads').style.color = '';
                    }
                </script>
            </tr>
            <tr>
                <td width="10%" class="tableleft">RNA碱基质量Q30占比</td>
                <td><input type="text" id="rna_base_quality" name="rna_base_quality" style="color: red;"
                           value="${rna.base_quality }"/><label class="error" for="rna_base_quality" generated="true"
                                                                style="color: red;"></label></td>
                <td>≥80%</td>
                <script>
                    var rna_base_quality = $("#rna_base_quality").val();
                    if (rna_base_quality.includes('%') && rna_base_quality.replace(/%/g, '') >= 80) {
                        document.getElementById('rna_base_quality').style.color = '';
                    }
                </script>
            </tr>
        </c:if>
        <tr>
            <td colspan="3">
                <center>
                    <input name="update" id="update" type="submit" class="btn btn-primary" value="修改"/>
                </center>
            </td>
        </tr>
    </table>
</form>
</html>