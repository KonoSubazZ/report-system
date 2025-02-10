<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="renderer" content="webkit">
    <base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myAlert.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css"></link>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/layui/css/layui.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js" role='reload'></script>
    <script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <script src="${pageContext.request.contextPath}/js/previewImage.js"></script>
    <script src="${pageContext.request.contextPath}/js/tools.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/myAlert.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/layui/layui.js"></script>
    <style type="text/css">
        td {
            vertical-align: middle;
        }
    </style>
</head>
<body>
<div class="panel admin-panel">
    <div class="panel-head">
        <strong class="icon-reorder">
            线下报告管理>审核及发送报告:${offlineReportIframeBean.report_id }>${offlineReportIframeBean.tested_date }>${offlineReportIframeBean.subbarcode }>${offlineReportIframeBean.status }
        </strong>
    </div>
    <div class="body-content" style="margin-left:100px">
        <form id="reportFileFormOne" method="post" enctype="multipart/form-data">
            <table style="width:100%">
                <tr>
                    <td>
                        <div class="form-group">
                            <div class="label" style="width:75px;float: left;margin-top: 10px">
                                <label>文件一：</label>
                            </div>
                            <div class="field">
                                <input type="text" id="report_filenameone" name="report_filenameone" readonly="readonly"
                                       class="input w50" style="width:350px;float: left;"
                                       value="${offlineReport.report_filenameone }"/>
                                <div class="tips"></div>
                                <input type="hidden" id="report_file_path" name="report_file_path"
                                       value="${offlineReport.report_file_path }"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="form-group">
                            <input type="file" id="reportFileOne" name="reportFileOne">
                            <button id="updateReportFileOne" style="width: 150px;" class="button bg-main icon-file-o">
                                更换报告文件
                            </button>
                        </div>
                        <input type="hidden" name="report_id" value="${offlineReportIframeBean.report_id}">
                    </td>
                </tr>
            </table>
            <script type="text/javascript">
                $(function () {
                    $("#updateReportFileOne").click(function () {
                        if (confirm("确定更换报告文件？")) {
                            var filename = $("#reportFileOne").val().split(".")[0];
                            var report_filename = $("#report_filenameone").val().split(".")[0];
                            if (report_filename != "") {
                                var from = document.getElementById("reportFileFormOne");
                                var formData = new FormData(from);
                                $.ajax({
                                    async: false,
                                    cache: false,
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/offlineReport/updateReportFileByReportId",
                                    data: formData,
                                    contentType: false,
                                    processData: false,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data) {
                                            alert("文件更换成功！");
                                        } else {
                                            alert("文件更换失败！");
                                        }
                                    }
                                });
                            } else {
                                alert("尚未上传报告文件一！");
                            }
                        }
                    });
                });
            </script>
        </form>
        <form id="reportFileFormTwo" method="post" enctype="multipart/form-data">
            <table style="width:100%">
                <tr>
                    <td>
                        <div class="form-group">
                            <div class="label" style="width:75px;float: left;margin-top: 10px">
                                <label>文件二：</label>
                            </div>
                            <div class="field">
                                <input type="text" id="report_filenametwo" name="report_filenametwo" readonly="readonly"
                                       class="input w50" style="width:350px;float: left;"
                                       value="${offlineReport.report_filenametwo }"/>
                                <div class="tips"></div>
                                <input type="hidden" id="report_file_path" name="report_file_path"
                                       value="${offlineReport.report_file_path }"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="form-group">
                            <input type="file" id="reportFileTwo" name="reportFileTwo">
                            <button id="updateReportFileTwo" style="width: 150px;" class="button bg-main icon-file-o">
                                更换报告文件
                            </button>
                        </div>
                        <input type="hidden" name="report_id" value="${offlineReportIframeBean.report_id}">
                    </td>
                </tr>
            </table>
            <script type="text/javascript">
                $(function () {
                    $("#updateReportFileTwo").click(function () {
                        if (confirm("确定更换报告文件？")) {
                            var filename = $("#reportFileTwo").val().split(".")[0];
                            var report_filename = $("#report_filenametwo").val().split(".")[0];
                            if (report_filename != "") {
                                var from = document.getElementById("reportFileFormTwo");
                                var formData = new FormData(from);
                                $.ajax({
                                    async: false,
                                    cache: false,
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/offlineReport/updateReportFileByReportId",
                                    data: formData,
                                    contentType: false,
                                    processData: false,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data) {
                                            alert("文件更换成功！");
                                        } else {
                                            alert("文件更换失败！");
                                        }
                                    }
                                });
                            } else {
                                alert("尚未上传报告文件二！");
                            }
                        }
                    });
                });
            </script>
        </form>
        <form id="reportFileFormThree" method="post" enctype="multipart/form-data">
            <table style="width:100%">
                <tr>
                    <td>
                        <div class="form-group">
                            <div class="label" style="width:75px;float: left;margin-top: 10px">
                                <label>文件三：</label>
                            </div>
                            <div class="field">
                                <input type="text" id="report_filenamethree" name="report_filenamethree"
                                       readonly="readonly" class="input w50" style="width:350px;float: left;"
                                       value="${offlineReport.report_filenamethree }"/>
                                <div class="tips"></div>
                                <input type="hidden" id="report_file_path" name="report_file_path"
                                       value="${offlineReport.report_file_path }"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="form-group">
                            <input type="file" id="reportFileThree" name="reportFileThree">
                            <button id="updateReportFileThree" style="width: 150px;" class="button bg-main icon-file-o">
                                更换报告文件
                            </button>
                        </div>
                        <input type="hidden" name="report_id" value="${offlineReportIframeBean.report_id}">
                    </td>
                </tr>
            </table>
            <script type="text/javascript">
                $(function () {
                    $("#updateReportFileThree").click(function () {
                        if (confirm("确定更换报告文件？")) {
                            var filename = $("#reportFileThree").val().split(".")[0];
                            var report_filename = $("#report_filenamethree").val().split(".")[0];
                            if (report_filename != "") {
                                var from = document.getElementById("reportFileFormThree");
                                var formData = new FormData(from);
                                $.ajax({
                                    async: false,
                                    cache: false,
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/offlineReport/updateReportFileByReportId",
                                    data: formData,
                                    contentType: false,
                                    processData: false,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data) {
                                            alert("文件更换成功！");
                                        } else {
                                            alert("文件更换失败！");
                                        }
                                    }
                                });
                            } else {
                                alert("尚未上传报告文件三！");
                            }
                        }
                    });
                });
            </script>
        </form>
        <table style="width:100%">
            <tr>
                <td>
                    <div class="form-group" style="margin-right: 50px;">
                        <div class="label" style="width:85px;float: left;margin-top: 10px;">
                            <label>审核结果：</label>
                        </div>
                        <div class="field">
                            <button id="review" style="width: 170px;float: left;" class="button border-main icon-check">
                                审核通过
                            </button>
                            <button id="reviewfalse" style="width: 170px;float: left;"
                                    class="button border-red icon-times"> 审核不通过
                            </button>
                            <div class="tips"></div>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="form-group">
                        <div class="label" style="width:85px">
                            <label></label>
                        </div>
                        <div class="field">
                            <button id="sendEmail" style="width: 150px;margin-right: 75px;" disabled="disabled"
                                    class="button bg-main icon-send"> 发送邮件
                            </button>
                            <script type="text/javascript">
                                layui.use(['layer'], function () {
                                    window.layer = layui.layer; // 赋值给全局变量
                                });

                                $(function () {
                                    $.post("${pageContext.request.contextPath}/offlineReport/getStatus", {"report_id": "${offlineReportIframeBean.report_id}"},
                                        function (data) {
                                            if (data == "审核通过" || data == "报告已发送") {
                                                $("#updateReportFileOne").prop("disabled", "disabled");
                                                $("#updateReportFileTwo").prop("disabled", "disabled");
                                                $("#updateReportFileThree").prop("disabled", "disabled");
                                                $("#review").prop("disabled", "disabled");
                                                $("#reviewfalse").prop("disabled", "disabled");
                                                $("#sendEmail").prop("disabled", false);
                                            }
                                        }, "text");
                                    $("#review").click(function () {
                                        $.myConfirm({
                                            title: '报告审核确认',
                                            message: '确认审核通过后将无法更换报告文件！',
                                            callback: function () {
                                                $.post("${pageContext.request.contextPath}/offlineReport/editStatus", {
                                                        "report_id": "${offlineReportIframeBean.report_id}",
                                                        "status": "审核通过"
                                                    },
                                                    function (data) {
                                                        $("#updateReportFileOne").prop("disabled", "disabled");
                                                        $("#updateReportFileTwo").prop("disabled", "disabled");
                                                        $("#updateReportFileThree").prop("disabled", "disabled");
                                                        $("#review").prop("disabled", "disabled");
                                                        $("#reviewfalse").prop("disabled", "disabled");
                                                        $("#sendEmail").prop("disabled", false);
                                                        $.myAlert('报告状态已修改为：审核通过！');
                                                    }, "json");
                                            }
                                        })
                                    });
                                    $("#reviewfalse").click(function () {
                                        $.myConfirm({
                                            title: '报告审核确认',
                                            message: '确认审核未通过后将更改报告状态！',
                                            callback: function () {
                                                $.post("${pageContext.request.contextPath}/offlineReport/editStatus", {
                                                        "report_id": "${offlineReportIframeBean.report_id}",
                                                        "status": "审核未通过"
                                                    },
                                                    function (data) {
                                                        $.myAlert('报告状态已修改为：审核未通过！');
                                                    }, "json"
                                                );
                                            }
                                        })
                                    });
                                    $("#sendEmail").click(function () {
                                        $.myConfirm({
                                            title: '邮件发送确认', message: '确认发送邮件？', callback: function () {
                                                $.post("${pageContext.request.contextPath}/offlineReport/sendEmail", {"report_id": "${offlineReportIframeBean.report_id}"},
                                                    function (data) {
                                                        if (data) {
                                                            $.myAlert(data.errorMessage);
                                                        }
                                                        if (data.flag) {
                                                            updateStatus();
                                                        }
                                                    }, "json"
                                                );
                                            }
                                        })
                                    });

                                    // 提交报告到新系统审核 同时更新报告系统状态
                                    function updateStatus() {
                                        // 检验是否有符合条件的报告
                                        if (!"${analysis_report}") {
                                            layer.alert('未查询到此样本编号有审核通过的报告记录，请审核通过后发送邮件');
                                            return;
                                        }
                                        let username = "${analysis_report.analyzer}";
                                        let upload_date = "${analysis_report.analysis_date}".slice(0, 10).replace(/-/g, '');
                                        let product = "${analysis_report.product_name}";
                                        let sample_code = "${analysis_report.subbarcode}";
                                        // 34 - 报告完成
                                        let status = 34;
                                        let report_id = "${analysis_report.report_id}";
                                        const URL = 'http://10.1.181.174:9099';
                                        $.ajax({
                                            type: "GET",
                                            url: URL + "/report/update_sample_report_status/" + username + "/" + upload_date + "/" + product + "/" + sample_code + "/" + status + "/" + report_id + "/",
                                            dataType: "json",
                                            success: function (res) {
                                                if (res.status == "success") {
                                                    layer.msg(res.msg, {
                                                        icon: 1,
                                                        offset: ['100px', '500px'],
                                                        time: 1000
                                                    });
                                                } else {
                                                    layer.msg(res.msg, {
                                                        icon: 2,
                                                        offset: ['100px', '500px'],
                                                        time: 1000
                                                    });
                                                }
                                            },
                                            error: function (xhr, status, error) {
                                                layer.msg("请求失败" + error, {time: 1000});
                                            }
                                        })
                                    }
                                });
                            </script>
                            <div class="tips"></div>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
