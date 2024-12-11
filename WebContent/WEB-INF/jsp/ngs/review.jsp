<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="renderer" content="webkit">
    <base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">

    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>--%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">--%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" ></link>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/layui/css/layui.css">
    <!-- 引入 layui.css -->
    <link href="//unpkg.com/layui@2.9.20/dist/css/layui.css" rel="stylesheet">
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
    <script type="text/javascript" src="jquery/zeroModal.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/layui/layui.js"></script>
    <!-- 引入 layui.js -->
    <script src="//unpkg.com/layui@2.9.20/dist/layui.js"></script>
    <link href="css/zeroModal.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<style>
    .layui-input {
        border-color: rgba(221, 221, 221, 0.5);
    }
</style>
<div class="review" id="review" style="width: 100%;height: 100vh;background-color: #FFFFFF;">
    <div class="layui-tab layui-tab-brief" lay-filter="test-hash">
        <ul class="layui-tab-title">
            <li class="layui-this" lay-id="pending-review">待审核</li>
            <li lay-id="pending-review-again">待复核</li>
        </ul>
        <div class="layui-tab-content">
            <%--待审核内容--%>
            <div class="layui-tab-item layui-show">
                <%--header--%>
                <div class="layui-row">
                    <div class="layui-col-xs3">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">分析时间：</label>
                                <div class="layui-input-inline">
                                    <input type="text" class="layui-input" id="ID-laydate-demo" placeholder="选择分析时间" style="border-color: red"
                                 >
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-xs3">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <div class="layui-input-group">
                                    <div class="layui-input-prefix">
                                        样本：
                                    </div>
                                    <input type="text" placeholder="请输入样本编号" class="layui-input" style="border-color: rgba(221, 221, 221, 0.5)">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-xs2">
                        <button type="button" class="layui-btn layui-btn-sm layui-btn-normal">
                            <i class="layui-icon layui-icon-search"></i> 搜索
                        </button>
                    </div>
                </div>
                <div style="padding: 16px;">
                    <table class="layui-hide" id="test" lay-filter="test"></table>
                </div>
                <table class="layui-hide" id="ID-table-pending-review"></table>
            </div>
        </div>
        <%--待审核内容--%>
        <div class="layui-tab-item">内容-2</div>
    </div>
</div>


<script>

    layui.use(['table', 'dropdown'], function () {
        var element = layui.element;
        var laydate = layui.laydate;

        var table = layui.table;
        var dropdown = layui.dropdown;
        // 渲染
        table.render({
            elem: '#ID-table-demo-parse',
            url: '/static/json/2/table/demo3.json',
            page: true,
            response: {
                statusCode: 200 // 重新规定成功的状态码为 200，table 组件默认为 0
            },
            // 将原始数据解析成 table 组件所规定的数据格式
            parseData: function (res) {
                return {
                    "code": res.status, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.total, //解析数据长度
                    "data": res.rows.item //解析数据列表
                };
            },
            cols: [[
                {field: 'id', title: 'ID', width: 80, fixed: 'left', unresize: true, sort: true},
                {field: 'username', title: '用户名', width: 120},
                {field: 'email', title: '邮箱', width: 150},
                {field: 'experience', title: '积分', width: 100, sort: true},
                {field: 'sex', title: '性别', width: 80, sort: true},
                {field: 'sign', title: '签名'},
                {field: 'joinTime', title: '加入时间', width: 120}
            ]],
            height: 315
        });

        // 渲染
        laydate.render({
            elem: '#ID-laydate-demo',
            value: new Date(), // 设置默认日期为当前日期
            format: 'yyyy-MM-dd' // 可选：设置日期格式（如果需要特定格式）
        });

        // tab 切换前的事件
        element.on('tabBeforeChange(test-hash)', function (data) {
            console.log(data.elem); // 得到当前的 tab 容器
            console.log(data.from.index); // 得到切换前的 tab 项所在下标
            console.log(data.from.id); // 得到切换前的 tab 项所在ID
            console.log(data.to.index); // 得到切换后的 tab 项所在下标
            console.log(data.to.id); // 得到切换后的 tab 项所在ID
            if (data.to.id === 'home') return false; // 返回 false 时阻止切换到对应的选项卡
        });


    });
</script>
</body>
</html>