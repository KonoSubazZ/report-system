<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn" style="min-width: 1720px;">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="renderer" content="webkit">
    <base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myAlert.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css"></link>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/pagination/pagination.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery/pagination/jquery.pagination.js"></script>
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
    <script type="text/javascript">
        $(function () {
            displayData(0);
            $("#filename").on("change", function (e) {
                var e = e || window.event
                var _file = e.target.files
                var names = ""
                if (_file.length > 1) {
                    for (let i = 0; i < _file.length; i++) {
                        if (i == _file.length - 1) {
                            names += _file[i].name
                        } else {
                            names += _file[i].name + "<br>"
                        }
                    }
                    $("#filenames").html(names)
                } else {
                    $("#filenames").html("")
                }
            });
            $("#customer").focus(function () {
                let customer = $("#customer").val();

                // document.getElementById("content").innerText = customer;
                if (customer != "") {
                    <%--$.ajax({--%>
                    <%--    url: "${pageContext.request.contextPath}/sendEmail/getContentByCustomer", //把表单数据发送到ajax.jsp--%>
                    <%--    type: "POST",--%>
                    <%--    cache: false,--%>
                    <%--    data: {customer:customer},--%>
                    <%--    dataType: "text",--%>
                    <%--    success: function (data) {--%>
                    <%--        document.getElementById("content").innerHTML = data;--%>
                    <%--    }--%>
                    <%--});--%>
                    $.ajax({
                        url: "${pageContext.request.contextPath}/sendEmail/getEmailInfo",
                        type: "GET",
                        data: {customer: customer},
                        dataType: "json", // 明确指定返回JSON格式
                        success: function (response) {
                            // 检查响应状态
                            if (response && response.code === 200) {
                                if (customer.includes("IVD")) {
                                    $("#IVD").prop("checked", true);
                                } else if (customer.includes("LDT")) {
                                    $("#LDT").prop("checked", true);
                                }
                                let subject = response.data.subject;
                                let files = document.getElementById("filename").files;
                                let fileCount = getDocxFiles(files);
                                subject = subject.replace("{{file_count}}", fileCount);

                                $("#content").val(response.data.content);
                                $("#subject").val(subject);
                            } else {

                            }
                        },
                        error: function (xhr, status, error) {

                            console.log(error);
                        }
                    })
                }
            });

            $("#reportBtn").click(function () {
                $("#emailForm").validate({
                    rules: {
                        "filename": {"required": true},
                        "subject": {"required": true},
                        "content": {"required": true},
                        "customer": {"required": true}
                    },
                    messages: {
                        "filename": {"required": "文件不能为空"},
                        "subject": {"required": ""},
                        "content": {"required": "内容不能为空"},
                        "customer": {"required": ""}
                    },
                    submitHandler: function () {
                        $.myConfirm({
                            title: '邮件发送确认', message: '确认发送邮件？', callback: function () {
                                var from = document.getElementById("emailForm");
                                var formData = new FormData(from);
                                $.ajax({
                                    cache: false,
                                    type: "POST",
                                    contentType: false,
                                    processData: false,
                                    url: "${pageContext.request.contextPath}/sendEmail/uploadSendEmail", //把表单数据发送到ajax.jsp
                                    data: formData,
                                    success: function (data) {
                                        $.myAlert(data.errorMessage);
                                    }
                                });
                            }
                        })
                    }
                });
            });
        });

        function getDocxFiles(files) {
            let fileCount = 0;
            for (var i = 0; i < files.length; i++) {
                var fileName = files[i].name;
                var fileExtension = fileName.slice(fileName.lastIndexOf(".")).toLowerCase();

                if (fileExtension === ".docx") {
                    fileCount++;
                }
            }
            return fileCount;
        }

        function displayData(pageNo) {
            var pageSize = 10;
            $.ajax({
                url: "${pageContext.request.contextPath}/sendEmail/getSendEmailByPage",
                type: "post",
                cache: false, //设置浏览器不缓存页面
                data: {
                    "pageNo": pageNo + 1,
                    "pageSize": pageSize,
                    "customer": $("#customer2").val()
                },
                beforeSend: function () {
                    $("#msg").text("正在处理请稍等...");
                    return true;
                },
                success: function (jsonObject) {
                    //清空内容
                    $("#tInfo2").empty();
                    if (jsonObject.total == 0) {
                        $("#msg").text("没数据");
                    } else {
                        $("#msg").text("");
                        var htmlString = "";
                        $.each(jsonObject.dataList, function (i, n) {
                            htmlString += '<tr class="odd">';
                            htmlString += '<td>' + n.email_id + '</td>';
                            htmlString += '<td>' + n.customer + '</td>';
                            htmlString += '<td>' + n.emailaddress + '</td>';
                            htmlString += '<td>' + n.ccemail + '</td>';
                            htmlString += '<td>' + n.subject + '</td>';
                            htmlString += '<td>' + n.content + '</td>';
                            htmlString += '<td>' + n.created_by + '</td>';
                            htmlString += '<td>' + n.created_date + '</td>';
                            htmlString += '<td>' + n.update_by + '</td>';
                            htmlString += '<td>' + n.update_date + '</td>';
                            htmlString += '<td><div class="button-group"><a class="button border-main" onclick="editOne(' + n.email_id + ',' + pageNo + ');">修改   </a>&nbsp;&nbsp;&nbsp;<a class="button border-main" onclick="deleteOne(' + n.email_id + ',' + pageNo + ');">删除   </a></div></td>';
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
                        num_display_entries: 3,//默认显示页码入口的个数
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

        function editOne(email_id, pageNo) {
            location.href = "${pageContext.request.contextPath}/sendEmail/editSendEmail?email_id=" + email_id;
        }

        function deleteOne(email_id, pageNo) {
            if (confirm("是否删除")) {
                $.ajax({
                    url: "${pageContext.request.contextPath}/sendEmail/deleteSendEmail",
                    type: "post",
                    cache: false, //设置浏览器不缓存页面
                    data: {
                        "email_id": email_id,
                    },
                    beforeSend: function () {
                        $("#message").text("正在处理请稍等...");
                        return true;
                    },
                    success: function (jsonObject) {
                        if (jsonObject) {
                            alert("删除成功");
                            displayData(pageNo);
                        } else {
                            alert("删除失败");
                        }
                    }
                });
            }
        }
    </script>
</head>
<body>
<div class="panel admin-panel">
    <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>文件发送管理</strong></div>
    <div class="body-content" style="margin-left:100px">
        <form id="emailForm" method="post" class="form-x" enctype="multipart/form-data">
            <table style="width:100%">
                <tr>
                    <td>
                        <div class="form-group" style="margin-left: 50px">
                            <div class="label" style="width:75px">
                                <label>选择文件：</label>
                            </div>
                            <div class="field">
                                <input type="file" id="filename" name="filename" multiple="multiple"/>
                                <label class="error" for="filename" generated="true" style="color: red;"></label>
                                <p id="filenames"></p>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="form-group" style="margin-left: 50px">
                            <div class="label" style="width:75px">
                                <label>主题：</label>
                            </div>
                            <div class="field">
                                <input type="text" id="subject" name="subject" class="input w50"
                                       style="width: 250px; line-height: 17px; display: inline-block"
                                       placeholder="请输入内容" data-validate="required:请输入内容"/>
                                <label class="error" for="subject" generated="true" style="color: red;"></label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="form-group" style="margin-left: 50px">
                            <div class="label" style="width:75px">
                                <label>送检机构：</label>
                            </div>
                            <div class="field">
                                <input type="text" id="customer" name="customer" class="input w50"
                                       style="width: 250px; line-height: 17px; display: inline-block"
                                       placeholder="请输入搜索关键字" data-validate="required:请输入搜索关键字"/>
                                <script type="text/javascript">
                                    $(function () {
                                        $.post("${pageContext.request.contextPath}/sendEmail/getCustomer",
                                            function (data) {
                                                $('#customer').autocomplete(data, {
                                                    max: false, //列表里的条目数
                                                    minChars: 0, //自动完成激活之前填入的最小字符
                                                    width: 288, //提示的宽度，溢出隐藏
                                                    scrollHeight: 300, //提示的高度，溢出显示滚动条
                                                    matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                                                    autoFill: true, //自动填充
                                                    formatItem: function (row, i, max) {
                                                        return row.name;
                                                    },
                                                    formatResult: function (row) {
                                                        return row.name;
                                                    }
                                                }).result(function (event, row, formatted) {

                                                });
                                            }, "json");
                                    })
                                </script>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="form-group" style="margin-left: 50px">
                            <div class="label" style="width:75px">
                                <label>内容：</label>
                            </div>
                            <div class="field">
                                <textarea id="content" name="content" style="width:500px;height:200px;overflow:scroll;"
                                          placeholder="请输入内容"></textarea>
                                <label class="error" for="content" generated="true" style="color: red;"></label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="form-group" style="margin-left: 50px">
                            <div class="label" style="width:75px">
                                <label>邮箱发送：</label>
                            </div>
                            <div class="field">
                                <input type="radio" name="email" id="IVD" style="height:38px"  value="0"/>IVD&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="email" id="LDT" style="height:38px" checked value="1"/>LDT
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="form-group" style="margin-left: 50px">
                            <button id="reportBtn" class="button bg-main icon-check-square-o" type="submit"> 发送邮件
                            </button>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <div class="padding border-bottom">
        <ul class="search" style="padding-left:10px;">
            <li><a class="button border-blue icon-plus-square-o"
                   href="${pageContext.request.contextPath}/sendEmail/addSendEmail"> 添加送检机构</a></li>
            <li>送检机构:</li>
            <li>
                <input type="text" placeholder="请输入搜索关键字" id="customer2" name="customer2" class="input"
                       style="width:200px; line-height:17px;display:inline-block"/>
                <script type="text/javascript">
                    $(function () {
                        $.post("${pageContext.request.contextPath}/sendEmail/getCustomer", function (data) {
                            $('#customer2').autocomplete(data, {
                                max: false, //列表里的条目数
                                minChars: 0, //自动完成激活之前填入的最小字符
                                width: 200, //提示的宽度，溢出隐藏
                                scrollHeight: 300, //提示的高度，溢出显示滚动条
                                matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
                                autoFill: true, //自动填充
                                formatItem: function (row, i, max) {
                                    if (row != null) {
                                        return "" + row.name;
                                    } else {
                                        return "";
                                    }
                                }
                            });
                        }, "json");
                    })
                </script>
            </li>
            <li>
                <a href="javascript:;" class="button border-main icon-search" onclick="displayData(0);"> 搜索</a>
                <span id="msg" style="color: red;font-size: 14px"></span>
            </li>
        </ul>
    </div>
    <table class="table table-hover text-center">
        <tr>
        <tr>
            <th>email_id</th>
            <th>customer</th>
            <th>emailaddress</th>
            <th>CCemail</th>
            <th>subject</th>
            <th>content</th>
            <th>created_by</th>
            <th>created_date</th>
            <th>update_by</th>
            <th>update_date</th>
            <th>操作</th>
        </tr>
        <tr>
            <tbody id="tInfo2">

            </tbody>
        <tr>
            <td colspan="25">
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
</body>
</html>