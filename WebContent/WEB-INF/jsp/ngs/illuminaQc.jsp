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
                    "tumorcellcontent":{"required":true},"DNA_total":{"required":true},"DNA_degradation":{"required":true},"outbound_quantity":{"required":true},
                    "sequencing_depth":{"required":true},"coverage_uniformity":{"required":true},"genome_alignment":{"required":true},"base_quality":{"required":true},
                    "RNA_total":{"required":true},"RNA_degradation":{"required":true},"rna_outbound_quantity":{"required":true},"total_reads":{"required":true},
                    "rna_base_quality":{"required":true},"hrd_DNA_total":{"required":true},"hrd_DNA_degradation":{"required":true},"hrd_outbound_quantity":{"required":true},
                    "hrd_sequencing_depth":{"required":true},"hrd_coverage_uniformity":{"required":true}
                },
                messages:{
                    "tumorcellcontent":{"required":"该字段不能为空"},"DNA_total":{"required":"该字段不能为空"},"DNA_degradation":{"required":"该字段不能为空"},"outbound_quantity":{"required":"该字段不能为空"},
                    "sequencing_depth":{"required":"该字段不能为空"}, "coverage_uniformity":{"required":"该字段不能为空"},"genome_alignment":{"required":"该字段不能为空"},"base_quality":{"required":"该字段不能为空"},
                    "RNA_total":{"required":"该字段不能为空"}, "RNA_degradation":{"required":"该字段不能为空"},"rna_outbound_quantity":{"required":"该字段不能为空"},"total_reads":{"required":"该字段不能为空"},
                    "rna_base_quality":{"required":"该字段不能为空"},"hrd_DNA_total":{"required":"该字段不能为空"}, "hrd_DNA_degradation":{"required":"该字段不能为空"},"hrd_outbound_quantity":{"required":"该字段不能为空"},
                    "hrd_sequencing_depth":{"required":"该字段不能为空"},"hrd_coverage_uniformity":{"required":"该字段不能为空"}
                },
                submitHandler : function(){
                    $.ajax({
                        cache: false,
                        type: "POST",
                        url:"${pageContext.request.contextPath}/filterQc/updateQc", //把表单数据发送到ajax.jsp
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
                    <input type="text" id="tumorcellcontent" name="tumorcellcontent" value="${qc.tumorcellcontent }"/>
                </c:when>
                <c:when test="${rna != null}">
                    <input type="text" id="tumorcellcontent" name="tumorcellcontent" value="${rna.tumorcellcontent }"/>
                </c:when>
                <c:when test="${hrd != null}">
                    <input type="text" id="tumorcellcontent" name="tumorcellcontent" value="${hrd.tumorcellcontent }"/>
                </c:when>
            </c:choose>
            <label class="error" for="tumorcellcontent" generated="true" style="color: red;"></label>
            </td>
            <td>≥10%</td>
        </tr>
        <c:if test="${qc != null}">
        <tr>
            <td width="10%" class="tableleft">DNA总量（ng）</td>
            <td><input type="text" id="DNA_total" name="DNA_total" value="${qc.DNA_total }"/><label class="error" for="DNA_total" generated="true" style="color: red;"></label></td>
            <td>
                <c:choose>
                    <c:when test="${qualityType == '白细胞'}">≥100</c:when>
                    <c:when test="${type == 'tissue'}">≥100</c:when>
                    <c:when test="${type == 'blood'}">≥20</c:when>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">DNA降解程度</td>
            <td><input type="text" id="DNA_degradation" name="DNA_degradation" value="${qc.DNA_degradation }"/><label class="error" for="DNA_degradation" generated="true" style="color: red;"></label></td>
            <td>A/B/C级</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">DNA预文库总量（ng）</td>
            <td><input type="text" id="outbound_quantity" name="outbound_quantity" value="${qc.outbound_quantity }"/><label class="error" for="outbound_quantity" generated="true" style="color: red;"></label></td>
            <td>≥500</td>
        </tr>
        </c:if>
        <c:if test="${rna != null}">
        <tr>
            <td width="10%" class="tableleft">RNA总量（ng）</td>
            <td><input type="text" id="RNA_total" name="RNA_total" value="${rna.RNA_total }"/><label class="error" for="RNA_total" generated="true" style="color: red;"></label></td>
            <td>≥100</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">RNA降解程度（DV200）</td>
            <td><input type="text" id="RNA_degradation" name="RNA_degradation" value="${rna.RNA_degradation }"/><label class="error" for="RNA_degradation" generated="true" style="color: red;"></label></td>
            <td>≥30 (2100) <br>≥70 (QSEP)</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">RNA预文库总量（ng）</td>
            <td><input type="text" id="rna_outbound_quantity" name="rna_outbound_quantity" value="${rna.outbound_quantity }"/><label class="error" for="rna_outbound_quantity" generated="true" style="color: red;"></label></td>
            <td>≥500</td>
        </tr>
        </c:if>
        <c:if test="${hrd != null && qc == null}">
            <tr>
                <td width="10%" class="tableleft">DNA总量（ng）</td>
                <td><input type="text" id="hrd_DNA_total" name="hrd_DNA_total" value="${hrd.DNA_total }"/><label class="error" for="hrd_DNA_total" generated="true" style="color: red;"></label></td>
                <td>≥200</td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA降解程度</td>
                <td><input type="text" id="hrd_DNA_degradation" name="hrd_DNA_degradation" value="${hrd.DNA_degradation }"/><label class="error" for="hrd_DNA_degradation" generated="true" style="color: red;"></label></td>
                <td>A/B/C级</td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">DNA预文库总量（ng）</td>
                <td><input type="text" id="hrd_outbound_quantity" name="hrd_outbound_quantity" value="${hrd.outbound_quantity }"/><label class="error" for="hrd_outbound_quantity" generated="true" style="color: red;"></label></td>
                <td>≥500</td>
            </tr>
        </c:if>
        <c:if test="${hrd != null}">
            <tr>
                <td width="10%" class="tableleft">HRD平均测序深度</td>
                <td><input type="text" id="hrd_sequencing_depth" name="hrd_sequencing_depth" value="${hrd.sequencing_depth }"/><label class="error" for="hrd_sequencing_depth" generated="true" style="color: red;"></label></td>
                <td>≥200</td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">HRD覆盖均一性</td>
                <td><input type="text" id="hrd_coverage_uniformity" name="hrd_coverage_uniformity" value="${hrd.coverage_uniformity }"/><label class="error" for="hrd_coverage_uniformity" generated="true" style="color: red;"></label></td>
                <td>≥80%</td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">HRD基因组比对率</td>
                <td><input type="text" id="hrd_genome_alignment" name="hrd_genome_alignment" value="${hrd.genome_alignment }"/><label class="error" for="hrd_genome_alignment" generated="true" style="color: red;"></label></td>
                <td>≥95%</td>
            </tr>
            <tr>
                <td width="10%" class="tableleft">HRD碱基质量Q30占比</td>
                <td><input type="text" id="hrd_base_quality" name="hrd_base_quality" value="${hrd.base_quality }"/><label class="error" for="hrd_base_quality" generated="true" style="color: red;"></label></td>
                <td>≥80%</td>
            </tr>
        </c:if>
        <c:if test="${qc != null}">
        <tr>
            <td width="10%" class="tableleft">DNA平均测序深度</td>
            <td><input type="text" id="sequencing_depth" name="sequencing_depth" value="${qc.sequencing_depth }"/><label class="error" for="sequencing_depth" generated="true" style="color: red;"></label></td>
            <td>
                <c:choose>
                    <c:when test="${qualityType == '白细胞'}">≥100</c:when>
                    <c:when test="${type == 'tissue'}">≥500</c:when>
                    <c:when test="${type == 'blood'}">≥1500</c:when>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">DNA覆盖均一性</td>
            <td><input type="text" id="coverage_uniformity" name="coverage_uniformity" value="${qc.coverage_uniformity }"/><label class="error" for="coverage_uniformity" generated="true" style="color: red;"></label></td>
            <td>≥90%</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">DNA基因组比对率</td>
            <td><input type="text" id="genome_alignment" name="genome_alignment" value="${qc.genome_alignment }"/><label class="error" for="genome_alignment" generated="true" style="color: red;"></label></td>
            <td>≥95%</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">DNA碱基质量Q30占比</td>
            <td><input type="text" id="base_quality" name="base_quality" value="${qc.base_quality }"/><label class="error" for="base_quality" generated="true" style="color: red;"></label></td>
            <td>≥80%</td>
        </tr>
        </c:if>
        <c:if test="${rna != null}">
        <tr>
            <td width="10%" class="tableleft">RNA测序总reads数（条）</td>
            <td><input type="text" id="total_reads" name="total_reads" value="${rna.total_reads }"/><label class="error" for="total_reads" generated="true" style="color: red;"></label></td>
            <td>≥12,000,000</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">RNA碱基质量Q30占比</td>
            <td><input type="text" id="rna_base_quality" name="rna_base_quality" value="${rna.base_quality }"/><label class="error" for="rna_base_quality" generated="true" style="color: red;"></label></td>
            <td>≥80%</td>
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
</body>
</html>