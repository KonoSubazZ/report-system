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
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myAlert.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css"></link>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/layui/css/layui.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/layui/layui.js"></script>
    <style type="text/css">
        td {
            vertical-align: middle;
        }

        #dele:hover {
            background: red;
        }
    </style>
</head>
<body>
<div class="panel admin-panel">
    <div class="panel-head">
        <strong class="icon-reorder">
            NGS报告管理>发送报告:${currentNgsAvailableData.report_id}:${currentNgsAvailableData.platform }>${currentNgsAvailableData.analysis_date }>${currentNgsAvailableData.subbarcode }>${currentNgsAvailableData.product_name }
        </strong>
    </div>
    <div class="body-content" style="margin-left:100px">
        <form id="reportFileForm" method="post" enctype="multipart/form-data">
            <table style="width:100%">
                <tr>
                    <td style="width:708px">
                        <div class="form-group">
                            <div class="label" style="width:75px;float: left;margin-top: 10px">
                                <label>文件名称：</label>
                            </div>
                            <div class="field">
                                <input type="text" id="report_filename" name="report_filename" readonly="readonly"
                                       class="input w50" style="width:600px;float: left;"
                                       value="${analysis_report.report_filename }"/>
                                <div class="tips"></div>
                                <input type="hidden" id="report_file_path" name="report_file_path"
                                       value="${analysis_report.report_file_path }"/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="form-group">
                            <%--					<input type="file" id="reportFile" name="reportFile">--%>
                            <%--					<button id="updateReportFile"  style="width: 150px;" class="button bg-main icon-file-o" > 更换报告文件</button>--%>
                        </div>
                        <input type="hidden" name="report_id" value="${currentNgsAvailableData.report_id}">
                    </td>
                </tr>
            </table>
        </form>
        <script type="text/javascript">
            $(function () {
                $("#updateReportFile").click(function () {
                    if (confirm("确定更换报告文件？")) {
                        // var filename = $("#reportFile").val().split(".")[0];
                        var file = $("#reportFile").val();
                        var filename = file.substring(file.lastIndexOf("\\") + 1).substring(0, file.substring(file.lastIndexOf("\\") + 1).lastIndexOf("."));
                        var report_filename = $("#report_filename").val().substring(0, $("#report_filename").val().lastIndexOf("."));
                        if (report_filename != "") {
                            if (filename == report_filename) {
                                var from = document.getElementById("reportFileForm");
                                var formData = new FormData(from);
                                $.ajax({
                                    async: false,
                                    cache: false,
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/ngs/updateReportFileByReportId",
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
                                // alert("文件名不匹配，请重新进行确认！");
                                if (confirm("报告文件名称不一致，确定更换？")) {
                                    var from = document.getElementById("reportFileForm");
                                    var formData = new FormData(from);
                                    $.ajax({
                                        async: false,
                                        cache: false,
                                        type: "POST",
                                        url: "${pageContext.request.contextPath}/ngs/updateReportFileByReportId",
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
                                }
                            }
                        } else {
                            alert("尚未产生报告！");
                        }
                    }
                });
            });
        </script>
        <div id="zhe" style="display:none"></div>
        <div id="biao"
             style="width:600px;height:500px;background:#fff;padding:0 20px 20px;display:none;position:fixed;top:20%;left:30%;z-index:201;overflow-y:scroll;overflow-x:hidden;">
            <div id="dele"
                 style="margin:5px 0 5px 510px;padding:5px 10px;width:30px;cursor: pointer;border:1px solid #aaa;">X
            </div>
            <table style="width:100%;" border="1">
                <tr height="30px" style="line-height:30px;">
                    <th style="width:10%;">序号</th>
                    <th style="width:10%;">record_id</th>
                    <th style="width:20%;">基因</th>
                    <th style="width:20%;">突变</th>
                    <th style="width:20%;">类型</th>
                    <th style="width:20%;">审核时间</th>
                </tr>
                <tbody id="siteInfo">
                </tbody>
            </table>
        </div>
        <table style="width:100%">
            <tr>
                <td style="width:708px">
                    <div class="form-group" style="margin-right: 50px;display: flex">
                        <div class="label" style="width:85px;float: left;margin-top: 10px;">
                            <label>审核结果：</label>
                        </div>
                        <div style="align-self: center;font-size: 22px;">${analysis_report.status}</div>
                        <%--				        <div class="field">--%>
                        <%--				         	 <button id="review" style="width: 170px;float: left;" class="button border-main icon-check" > 审核通过</button>--%>
                        <%--						     <button id="reviewfalse" style="width: 170px;float: left;" class="button border-red icon-times" > 审核不通过</button>--%>
                        <%--				          <div id="message" class="tips" style="color: red;font-size: 14px"></div>--%>
                        <%--				        </div>--%>
                    </div>
                </td>
                <td>
                    <button id="viewSite" style="float: left;" class="button border-main">查看位点</button>
                </td>
                <td>
                    <select style="width: 120px;float: left;height:40px;border:0px solid #ccc;border-radius:5px;"
                            name="send_way" id="send_way" value="${analysis_report.send_way}">
                        <option value="1">线上发送</option>
                        <option value="0">线下发送</option>
                    </select>
                </td>
                <td style="width:560px">
                    <div class="form-group">
                        <div class="label" style="width:85px">
                            <label></label>
                        </div>
                        <div class="field">
                            <button id="sendEmail" style="width: 150px;float:left;" disabled="disabled"
                                    class="button bg-main icon-send"> 发送邮件
                            </button>
                            <p id="sendTip" style="font-size:14px;color:red;float:left;line-height:42px;"></p>
                            <script type="text/javascript">
                                $("#viewSite").click(function () {
                                    $.post("${pageContext.request.contextPath}/life/getSiteInfo", {"report_id": "${currentNgsAvailableData.report_id}"},
                                        function (data) {
                                            if (data.flag) {
                                                $("#biao").show();
                                                $("#zhe").show();
                                                var siteInfo = "";
                                                $.each(data.siteData, function (item, index, arr) {
                                                    siteInfo += '<tr height="50px" align="center"><td>' + item + '</td><td>' + index.record_id + '</td><td>' + index.gene + '</td><td>' + index.variant + '</td><td>' + index.type + '</td><td>' + index.check_date + '</td></tr>';
                                                })
                                                $("#siteInfo").html(siteInfo);

                                            } else {
                                                alert("请先生产报告");
                                            }
                                        });
                                })
                                $("#dele").click(function () {
                                    $("#biao").hide();
                                    $("#zhe").hide();
                                })
                                $("#zhe").click(function () {
                                    $("#biao").hide();
                                    $("#zhe").hide();
                                })
                                $("#zhe").css({
                                    "height": $(window).width(),
                                    "width": $(window).width(),
                                    "background": "#000",
                                    "z-index": "200",
                                    "position": "fixed",
                                    "top": "0",
                                    "left": "0",
                                    "opacity": ".7"
                                })
                                $("#send_way").change(function () {
                                    var flag = $("#send_way").val();
                                    if (flag == 1) {
                                        var changeFlg = confirm("是否确认线上发送?");
                                        if (changeFlg) {
                                            $("#send_way").val("1");
                                            $("#send_way").css({"background": "#0ae", "color": "#fff"});
                                            $("#sendEmail").prop("disabled", false);
                                        } else {
                                            $("#send_way").val("0");
                                            $("#send_way").css({"background": "red", "color": "#fff"});
                                            $("#sendEmail").prop("disabled", "disabled");
                                        }
                                    } else {
                                        var changeFlg = confirm("是否确认线下发送?");
                                        if (changeFlg) {
                                            $("#send_way").val("0");
                                            $("#send_way").css({"background": "red", "color": "#fff"});
                                            $("#sendEmail").prop("disabled", "disabled");
                                        } else {
                                            $("#send_way").val("1");
                                            $("#send_way").css({"background": "#0ae", "color": "#fff"});
                                            $("#sendEmail").prop("disabled", false);
                                        }
                                    }
                                    $.post("${pageContext.request.contextPath}/life/updateSendWay", {
                                            "report_id": "${currentNgsAvailableData.report_id}",
                                            "send_way": $("#send_way").val()
                                        },
                                        function (data) {
                                            if (data) {
                                                alert("报告发送状态更改成功！");
                                            } else {
                                                alert("报告发送状态更改失败！");
                                            }
                                        });

                                });
                                $(function () {
                                    $("#send_way").val(${analysis_report.send_way});
                                    isDisabled();
                                    $.post("${pageContext.request.contextPath}/life/getStatus", {"report_id": "${currentNgsAvailableData.report_id}"},
                                        function (data) {
                                            if (data == "报告审核通过" || data == "报告发送成功") {
                                                $("#updateReportFile").prop("disabled", "disabled");
                                                $("#review").prop("disabled", "disabled");
                                                $("#reviewfalse").prop("disabled", "disabled");
                                                isDisabled();
                                            }
                                        }, "text");
                                    $("#review").click(function () {
                                        if ($("#report_filename").val()) {
                                            $.myConfirm({
                                                title: '报告审核确认',
                                                message: '确认审核通过后将无法更换报告文件！',
                                                callback: function () {
                                                    $.post("${pageContext.request.contextPath}/life/editStatus", {
                                                            "report_id": "${currentNgsAvailableData.report_id}",
                                                            "status": "报告审核通过"
                                                        },
                                                        function (data) {
                                                            if (data.flag) {
                                                                $("#updateReportFile").prop("disabled", "disabled");
                                                                $("#review").prop("disabled", "disabled");
                                                                $("#reviewfalse").prop("disabled", "disabled");
                                                                isDisabled();
                                                                $.myAlert('报告状态已修改为：报告审核通过！');
                                                            } else {
                                                                $.myAlert('报告状态已修改为：报告审核未通过！');
                                                            }
                                                        }, "json");
                                                }
                                            })
                                        } else {
                                            alert("文件不能为空！");
                                        }

                                    });
                                    $("#reviewfalse").click(function () {
                                        if ($("#report_filename").val()) {
                                            $.myConfirm({
                                                title: '报告审核确认',
                                                message: '确认审核未通过后将更改报告状态！',
                                                callback: function () {
                                                    $.post("${pageContext.request.contextPath}/life/editStatus", {
                                                            "report_id": "${currentNgsAvailableData.report_id}",
                                                            "status": "报告审核未通过"
                                                        },
                                                        function (data) {
                                                            $.myAlert('报告状态已修改为：报告审核未通过！');
                                                        }, "json"
                                                    );
                                                }
                                            })
                                        } else {
                                            alert("文件不能为空！");
                                        }
                                    });
                                    $("#sendEmail").click(function () {
                                        fn(function () {
                                            $.myConfirm({
                                                title: '邮件发送确认', message: '确认发送邮件？', callback: function () {
                                                    $("#sendEmail").prop("disabled", "disabled");
                                                    $("#sendTip").text("正在发送邮件，请稍后...");
                                                    $.post("${pageContext.request.contextPath}/ngs/sendEmail", {
                                                            "report_id": "${currentNgsAvailableData.report_id}",
                                                            "subbarcode": "${currentNgsAvailableData.subbarcode}",
                                                            "report_filename": "${analysis_report.report_filename }",
                                                            "report_file_path": "${analysis_report.report_file_path }"
                                                        },
                                                        function (data) {
                                                            $.myAlert(data.errorMessage);
                                                            $("#sendEmail").prop("disabled", false);
                                                            $("#sendTip").text("");

                                                            // 更新新系统报告状态
                                                            if (data.errorMessage === "邮件发送成功!") {

                                                            }
                                                        }, "json"
                                                    );
                                                }
                                            })
                                        })
                                    });
                                });

                                function isDisabled() {
                                    if ($("#send_way").val() == 1) {
                                        $("#sendEmail").prop("disabled", false);
                                        $("#send_way").css({"background": "#0ae", "color": "#fff"});
                                    } else {
                                        $("#sendEmail").prop("disabled", "disabled");
                                        $("#send_way").css({"background": "red", "color": "#fff"});
                                    }
                                }

                                function fn(callback) {
                                    fn.prototype.init(callback);
                                }

                                fn.prototype = {
                                    canclick: true,
                                    init: function (callback) {
                                        if (this.canclick) {
                                            this.canclick = false
                                            callback();
                                            setTimeout(function () {
                                                this.canclick = true
                                            }.bind(this), 1000)
                                        } else {
                                            console.log('1s中之内不允许重复点击')
                                        }
                                    }
                                }

                                function updateStatus() {
                                    let username = "${analysisReport.analyzer}";
                                    let upload_date = "${analysisReport.analysis_date}";
                                    let product = "${analysisReport.product_name}";
                                    let sample_code = "${analysisReport.subbarcode}";
                                    // 34 - 报告完成
                                    let status = 34;
                                    let report_id = "${analysisReport.report_id}";
                                    const URL = 'http://10.1.181.174:9099';
                                    $.ajax({
                                        type: "GET",
                                        url: URL + "/report/update_sample_report_status/" + username + "/" + upload_date + "/" + product + "/" + sample_code + "/" + status + "/" + report_id,
                                        dataType: "json",
                                        success: function (res) {
                                            if (res.status == "success") {
                                                layer.msg(res.msg, {
                                                    icon: 0,
                                                    offset: ['100px', '500px'],
                                                    time: 1000
                                                });
                                            } else {
                                                layer.msg(res.msg, {
                                                    icon: 0,
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
