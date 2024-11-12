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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pintuer.css" role='reload'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.autocomplete.css" ></link>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autoinput.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/autocomplete.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/autocomplete/getDate.js"></script>
    <script src="${pageContext.request.contextPath}/js/pintuer.js" role='reload'></script>
    <script src="${pageContext.request.contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <script src="${pageContext.request.contextPath}/js/previewImage.js"></script>
    <script src="${pageContext.request.contextPath}/js/tools.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#userForm").validate({
                rules:{
                    "Ackerman_num":{"required":true},"Tumor_cell_count":{"required":true},
                    "Seen_microscopically":{"required":true},"test_item":{"required":true},"Detection_method":{"required":true},
                    "Detect_antibody":{"required":true},"TPS":{"required":true},"CPS":{"required":true},
                    "reporter":{"required":true}
                },
                messages:{
                    "Ackerman_num":{"required":"该字段不能为空"},"Tumor_cell_count":{"required":"该字段不能为空"},
                    "Seen_microscopically":{"required":"该字段不能为空"},"test_item":{"required":"该字段不能为空"},"Detection_method":{"required":"该字段不能为空"},
                    "Detect_antibody":{"required":"该字段不能为空"},"TPS":{"required":"该字段不能为空"},"CPS":{"required":"该字段不能为空"},
                    "reporter":{"required":"该字段不能为空"}
                },
                submitHandler : function(){
                    $.ajax({
                        cache: false,
                        type: "POST",
                        url:"${pageContext.request.contextPath}/filterPd/updatePd", //把表单数据发送到ajax.jsp
                        data:$('#userForm').serialize(), //要发送的是ajaxFrm表单中的数据
                        success:function(data){
                            if(data.success){
                                alert("数据保存成功!");
                            }else{
                                alert("数据保存失败!");
                            }
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<input type="hidden" id="platform" value="${currentNgsAvailable.platform }">
<input type="hidden" id="analysis_date" value="${currentNgsAvailable.analysis_date }">
<input type="hidden" id="subbarcode" value="${currentNgsAvailable.subbarcode }">
<input type="hidden" id="product_name" value="${currentNgsAvailable.product_name }">
<form id="userForm">
    <input type="hidden" name="file_id" value="${pd.file_id }"/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">病人ID</td>
            <td><input type="text" id="Ackerman_num" name="Ackerman_num" value="${pd.ackerman_num }"/><label class="error" for="Ackerman_num" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">肿瘤细胞数是否大于100个</td>
            <td><input type="text" id="Tumor_cell_count" name="Tumor_cell_count" value="${pd.tumor_cell_count }"/><label class="error" for="Tumor_cell_count" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">镜下描述</td>
            <td><textarea id="Seen_microscopically" name="Seen_microscopically" style="width:80%;overflow:scroll;">${pd.seen_microscopically }</textarea><label class="error" for="Seen_microscopically" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">检测项目</td>
            <td><input type="text" id="test_item" name="test_item" value="${pd.test_item }"/><label class="error" for="test_item" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">检测方法</td>
            <td><input type="text" id="Detection_method" name="Detection_method" value="${pd.detection_method }"/><label class="error" for="Detection_method" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">检测抗体</td>
            <td><input type="text" id="Detect_antibody" name="Detect_antibody" value="${pd.detect_antibody }"/><label class="error" for="Detect_antibody" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">TPS</td>
            <td><input type="text" id="TPS" name="TPS" value="${pd.TPS }"/><label class="error" for="TPS" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">CPS</td>
            <td><input type="text" id="CPS" name="CPS" value="${pd.CPS }"/><label class="error" for="CPS" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">检测人</td>
            <td><input type="text" id="reporter" name="reporter" value="${pd.reporter }"/><label class="error" for="reporter" generated="true" style="color: red;"></label></td>
        </tr>
        <tr>
            <td colspan="2">
                <center>
                    <input name="update" id="update" type="submit" class="btn btn-primary" value="修改"/>
                </center>
            </td>
        </tr>
    </table>
</form>
</body>
</html>