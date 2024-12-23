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
    <%--    <meta http-equiv="Access-Control-Allow-Origin" content="*">--%>
    <base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/layui/css/layui.css"/>
    <%--    <!-- 引入 layui.css -->--%>
    <%--    <link href="//unpkg.com/layui@2.9.20/dist/css/layui.css" rel="stylesheet">--%>
    <%--    <!-- 引入 layui.js -->--%>
    <%--    <script src="//unpkg.com/layui@2.9.20/dist/layui.js"></script>--%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/lib/layui/layui.js"></script>
</head>
<body>
<style>
    .panel-head {
        background-color: #fcfcfc;
        padding: 10px 15px;
        border-radius: 4px 4px 0 0;
        border-bottom: solid 1px #ddd;
    }

    .panel {
        border: solid 1px #ddd;
        border-radius: 4px;
    }

    #fileName:hover {
        color: #0a84ff;
    }
</style>
<div class="panel admin-panel" style="background-color: #FFF;height: 100vh;">
    <div class="panel-head">
        <strong class="icon-reorder">
            NGS报告管理>审核报告:${currentNgsAvailableData.report_id}:${currentNgsAvailableData.platform }>${currentNgsAvailableData.analysis_date }>${currentNgsAvailableData.subbarcode }>${currentNgsAvailableData.product_name }
        </strong>
    </div>
    <div class="body-content" style="margin-left:100px;font-size:16px;">
        <%--表单展示详情--%>
        <div class="layui-row" style="padding:12px 0;margin-top: 10px">
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">姓名</div>
                <div>${sampleFile.client}</div>
            </div>
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">样本编号</div>
                <div>${sampleFile.subbarcode}</div>
            </div>
        </div>
        <div class="layui-row" style="padding:12px 0;">
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">产品</div>
                <div>${currentNgsAvailableData.product_name}</div>
            </div>
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">原发癌种</div>
                <div>${sampleFile.disease_type}</div>
            </div>
        </div>
        <div class="layui-row" style="padding:12px 0;">
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">解读人</div>
                <div>${analysisReport.analyzer}</div>
            </div>
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">解读日期</div>
                <div>${analysisReport.report_time}</div>
            </div>
        </div>
        <div class="layui-row" style="padding:12px 0;">
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">审核人</div>
                <div>${analysisReport.report_checker}</div>
            </div>
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px">审核日期</div>
                <div>${analysisReport.report_check_time}</div>
            </div>
        </div>
        <div class="layui-row" style="padding:12px 0;">
            <div class="layui-col-xs5" style="display: flex;margin-top: 35px;">
                <div style="width:100px">报告文件</div>
                <div id="download" style="cursor: pointer;">
                    <%--                    <i class="layui-icon layui-icon-file" style="font-size: 20px; color: #1E9FFF;"></i> --%>
                    <span id="fileName"
                          onclick="previewPdf(${analysisReport.report_id})">${analysisReport.report_filename}</span>
                    <i class="layui-icon layui-icon-link"
                       style="font-size: 20px; color: #1E9FFF;margin-left: 5px;" onclick="download()"></i>
                </div>
            </div>
            <div class="layui-col-5" style="display: flex;position: relative">
                <c:if test="${analysisReport.analyzer == currentNgsAvailableData.user}">
                    <div class="layui-upload-drag" style="display: block;" id="ID-upload-demo-drag">
                        <i class="layui-icon layui-icon-upload"></i>
                        <div>点击上传报告，或将文件拖拽到此处</div>
                        <div class="layui-hide" id="ID-upload-demo-preview">
                            <i class="layui-icon layui-icon-form"
                               style="font-size: 20px; color:#00AAEE;"></i>${analysisReport.report_filename}
                        </div>
                    </div>
                    <div style="position: absolute;left: 330px;top: 95px;">
                        <button type="button" class="layui-btn" id="updateReport">更换报告</button>
                    </div>
                </c:if>
                <c:if test="${analysisReport.analyzer != currentNgsAvailableData.user}">
                    <textarea id="comment" placeholder="请输入审核备注，最多300字" class="layui-textarea"
                              style="width: 480px;height: 150px"></textarea>
                    <button type="button" class="layui-btn" style="margin-top: 110px">提交</button>
                </c:if>
            </div>
        </div>
        <div class="layui-row" style="padding:15px 0;">
            <div class="layui-col-xs5" style="display: flex;">
                <div style="width:100px;align-self: center" id="status">报告状态</div>
                <div>
                    <span>${analysisReport.status}</span>
                    <c:if test="${analysisReport.status=='报告审核未通过'}">
                        <i class="layui-icon layui-icon-help" onclick="showReportMsg()"
                           style="color: red; margin-left: 10px;font-size: 22px;cursor: pointer;">原因</i>
                    </c:if></div>
            </div>
            <div class="layui-col-xs5" style="display: flex;">
                <c:if test="${analysisReport.status=='报告生成成功'}">
                    <button type="button" class="layui-btn layui-bg-blue" onclick="submitReport()"><i
                            class="layui-icon layui-icon-ok"></i>提交审核
                    </button>
                </c:if>
                <c:if test="${analysisReport.analyzer != currentNgsAvailableData.user}">
                    <button type="button" class="layui-btn layui-bg-blue" onclick="updateCheckStatus(35)"><i
                            class="layui-icon layui-icon-ok-circle"></i>审核通过
                    </button>
                    <button type="button" class="layui-btn layui-bg-red" onclick="updateCheckStatus(36)"><i
                            class="layui-icon layui-icon-close-fill"></i>审核不通过
                    </button>
                </c:if>

            </div>
        </div>
    </div>
</div>
</body>
<script>
    function formatDate(date) {
        const yy = date.getFullYear().toString(); // 获取年份的最后两位
        const mm = (date.getMonth() + 1).toString().padStart(2, '0'); // 获取月份，注意月份从0开始，所以要加1
        const dd = date.getDate().toString().padStart(2, '0'); // 获取日期
        const hh = date.getHours().toString().padStart(2, '0'); // 获取小时
        const min = date.getMinutes().toString().padStart(2, '0'); // 获取分钟
        const ss = date.getSeconds().toString().padStart(2, '0'); // 获取秒钟
        return yy + mm + dd;
    }

    // 下载文件
    function download() {
        window.location.href = "${pageContext.request.contextPath}/ngs/download?report_id=${analysisReport.report_id}";
    }

    layui.use(function () {
        let layer = layui.layer
        var upload = layui.upload;
        var $ = layui.$;
        // 渲染上传组件
        upload.render({
            elem: '#ID-upload-demo-drag',
            url: '', // 实际使用时改成您自己的上传接口即可。
            accept: 'file',
            auto: false,
            done: function (res) {
                layer.msg('上传成功');
                $('#ID-upload-demo-preview').removeClass('layui-hide');

            }
        });

        // 更换报告文件
        $("#updateReport").click(function () {
            layer.confirm('确定更换报告文件？', {
                icon: 3,
                title: '提示'
            }, function (index) {
                // 点击确认后执行的代码
                let fileInput = $(".layui-upload-file")[0];
                if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
                    layer.alert("请先选择文件！", {icon: 2});
                    return;
                }

                let file = fileInput.files[0];
                let formData = new FormData();
                formData.append("report_id", "${analysisReport.report_id}"); // 替换为后端变量
                formData.append("reportFile", file);
                formData.append("report_filename", file.name); // 替换为后端变量
                formData.append("report_file_path", "${analysisReport.report_file_path}"); // 替换为后端变量

                $.ajax({
                    async: true, // 异步请求
                    cache: false, // 禁用缓存
                    type: "POST",
                    url: "${pageContext.request.contextPath}/ngs/updateReportFileByReportId",
                    data: formData,
                    contentType: false,
                    processData: false,
                    dataType: "json",
                    success: function (data) {
                        if (data) {
                            layer.msg("文件更换成功！", {time: 1000});
                            $("#fileName").text(file.name);
                        } else {
                            layer.msg("文件更换失败！", {time: 1000});
                        }
                    },
                    error: function (xhr, status, error) {
                        layer.msg("请求失败：" + error, {time: 1000});
                    }
                });

                layer.close(index); // 关闭确认框
            }, function () {
                // 点击取消的回调（可选）
                // layer.msg("取消更换操作", { time: 1000 });
            });
        });
    });

    // 提交报告到新系统审核 同时更新报告系统状态
    function submitReport() {
        // 初始化参数
        let upload_date = formatDate(new Date());
        let username = "${analysisReport.report_checker}";
        let product = "${analysisReport.product_name}";
        let sample_code = "${analysisReport.subbarcode}";
        // 32-待审核
        let status = 32;
        let report_id = "${analysisReport.report_id}";
        const URL = 'http://10.1.181.174:9098';
        $.ajax({
            type: "GET",
            url: URL + "/report/update_sample_report_status/" + username + "/" + upload_date + "/" + product + "/" + sample_code + "/" + status + "/" + report_id,
            dataType: "json",
            success: function (data) {
                if (data) {
                    layer.msg(sample_code + "提交报告审核成功！", {time: 1000});
                    updateReportStatus(report_id, "待审核");

                }
            },
            error: function (xhr, status, error) {
                layer.msg("请求失败" + error, {time: 1000});
            }
        })
    }

    // 更新报告系统状态
    function updateReportStatus(reportId, status) {
        $.ajax({
            type: "POST", // 或者 "GET" 根据你的实际需求
            url: "${pageContext.request.contextPath}/ngs/updateStatus", // 后端接口路径
            data: {
                report_id: reportId,
                status: status
            },
            success: function (res) {
                if (res.code === 200) {
                    layer.msg('状态更新成功', {
                        icon: 0,
                        offset: ['100px', '500px'],
                        time: 1000
                    });
                    $("#status").text(status);
                }

                // alert("状态更新成功！");shen
            },
            error: function (xhr, status, error) {
                // alert("请求失败：" + error);
            }
        });
    }

    // 预览pdf
    function previewPdf() {
        $.ajax({
            url: '${pageContext.request.contextPath}/ngs/getPreviewUrl',
            type: 'GET',
            data: {reportId: ${analysisReport.report_id}}, // 前端传递路径
            success: function (res) {
                if (res.code === 200) {
                    if (res.data.includes('.docx')) {
                        return layer.msg('暂不支持预览word文件，请下载后查看', {
                            icon: 0,
                            offset: ['100px', '500px'],
                            time: 1000
                        });
                    }
                    window.open(res.data);
                } else {
                    layer.msg(data.message, {time: 1000, icon: 0});
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
            },
        });

    }

    // 审核报告 通过、未通过
    function updateCheckStatus(code) {

        if (code == 36 && $("#comment").val() == "") {
            layer.msg("请填写审核意见！", {
                icon: 0,
                offset: ['100px', '500px'],
                time: 1000
            });
            return;
        }
        let upload_date = formatDate(new Date());
        let username = "${analysisReport.analyzer}";
        let product = "${analysisReport.product_name}";
        let sample_code = "${analysisReport.subbarcode}";
        // 32-待审核
        let status = code;
        let report_id = "${analysisReport.report_id}";
        const URL = 'http://10.1.181.174:9098';

        $.ajax({
            type: "GET",
            url: URL + "/report/update_sample_report_status/" + username + "/" + upload_date + "/" + product + "/" + sample_code + "/" + status + "/" + report_id,
            dataType: "json",
            success: function (data) {
                if (data.status == "success") {
                    let rstatus = code == 35 ? "报告审核通过" : "报告审核未通过";
                    $.post("${pageContext.request.contextPath}/life/editStatus", {
                        "report_id": "${currentNgsAvailableData.report_id}",
                        "status": rstatus
                    }, function (data) {
                        layer.msg("更新报告状态成功", {time: 1000, icon: 1});
                        // if (data.flag) {
                        //     layer.msg("更新报告状态成功", {time: 1000, icon: 1});
                        // } else {
                        //     layer.msg("操作失败，请重新尝试操作", {time: 1000, icon: 0});
                        // }
                    });
                    $.post("${pageContext.request.contextPath}/ngs/updateComment", {
                        "report_id": "${analysisReport.report_id}",
                        "comment": $("#comment").val()
                    }, function (res) {
                        if (res.code === 200) {
                            layer.msg('更新备注成功', {
                                icon: 0,
                                offset: ['100px', '500px'],
                                time: 1000
                            });
                        }
                    });
                } else {
                    layer.msg(data.msg, {time: 1000, icon: 1});
                }

            },
            error: function (xhr, status, error) {
                layer.msg("请求失败" + error, {time: 1000});
            }
        })


    }

    // 展示审核未通过备注
    function showReportMsg() {
        $.get(
            "${pageContext.request.contextPath}/ngs/getComment",
            {"report_id": "${analysisReport.report_id}"},
            function (res) {
              if (res.code === 200){
                  let index = layer.open({
                      type: 1,
                      area: ['420px', '240px'], // 宽高
                      offset: ['25%', '25%'],
                      content: '<div style="padding: 11px;">${res.data}</div>'
                  });

                  layer.title('审核未通过备注', index);
              }
            }
        )

    }
</script>
</html>
