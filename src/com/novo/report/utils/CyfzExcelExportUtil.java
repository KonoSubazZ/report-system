package com.novo.report.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CyfzExcelExportUtil {

    private static final String[] HEADERS = {
            "报告单号", "姓名", "样本类型", "报告时间", "检测结果", "检测基因", "转录本", "外显子",
            "核苷酸改变", "氨基酸改变", "位点", "丰度", "变异类型", "变异类型1", "变异解读",
            "基因型", "测序深度", "肿瘤突变负荷", "样本编号"
    };

    private CyfzExcelExportUtil() {
    }

    public static String createExcel(String reportDetail, String subbarcode, String outputDir) throws IOException {
        if (StringUtils.isBlank(reportDetail) || StringUtils.isBlank(subbarcode) || StringUtils.isBlank(outputDir)) {
            return null;
        }

        JsonElement element = new JsonParser().parse(reportDetail);
        if (!element.isJsonObject()) {
            return null;
        }

        JsonObject reportJson = element.getAsJsonObject();
        List<List<String>> rows = buildRowsWithFallback(reportJson, subbarcode);

        File dir = new File(outputDir);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }

        String fileName = "CYFZ-" + sanitizeFileName(subbarcode) + ".xlsx";
        File outputFile = new File(dir, fileName);
        writeExcel(outputFile, rows);
        return outputFile.getAbsolutePath();
    }

    public static String createExcel(List<String> reportDetails, List<String> subbarcodes, String outputDir) throws IOException {
        if (reportDetails == null || subbarcodes == null || reportDetails.size() != subbarcodes.size()
                || reportDetails.isEmpty() || StringUtils.isBlank(outputDir)) {
            return null;
        }

        List<List<String>> rows = new ArrayList<List<String>>();
        for (int i = 0; i < reportDetails.size(); i++) {
            String reportDetail = reportDetails.get(i);
            String subbarcode = subbarcodes.get(i);
            if (StringUtils.isBlank(reportDetail) || StringUtils.isBlank(subbarcode)) {
                continue;
            }

            JsonElement element = new JsonParser().parse(reportDetail);
            if (!element.isJsonObject()) {
                continue;
            }

            rows.addAll(buildRowsWithFallback(element.getAsJsonObject(), subbarcode));
        }

        File dir = new File(outputDir);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }

        String fileName;
        if (subbarcodes.size() == 1) {
            fileName = "CYFZ-" + sanitizeFileName(subbarcodes.get(0)) + ".xlsx";
        } else {
            fileName = "CYFZ-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
        }
        File outputFile = new File(dir, fileName);
        writeExcel(outputFile, rows);
        return outputFile.getAbsolutePath();
    }

    private static List<List<String>> buildRowsWithFallback(JsonObject reportJson, String subbarcode) {
        List<List<String>> rows = buildRows(reportJson, subbarcode);
        if (rows.isEmpty()) {
            rows.add(buildSampleRow(reportJson, subbarcode));
        }
        return rows;
    }

    private static List<String> buildSampleRow(JsonObject reportJson, String subbarcode) {
        List<String> row = new ArrayList<String>();
        for (int i = 0; i < HEADERS.length; i++) {
            row.add("/");
        }
        row.set(0, subbarcode);
        row.set(1, defaultSlash(getString(reportJson, "client")));
        row.set(2, defaultSlash(getString(reportJson, "specimentype")));
        row.set(3, defaultSlash(getString(reportJson, "reportdate")));
        row.set(18, defaultSlash(firstNotBlank(getString(reportJson, "barcode"), subbarcode)));
        return row;
    }

    private static List<List<String>> buildRows(JsonObject reportJson, String subbarcode) {
        List<List<String>> rows = new ArrayList<List<String>>();
        JsonObject summary = getObject(reportJson, "summaryOfRresults");
        JsonArray bodyDrugTipLineStr = getArray(reportJson, "bodyDrugTipLineStr");
        JsonArray bodyDrugNoComplexStr = getArray(reportJson, "BodyDrugNoComplexStr");

        for (JsonElement element : bodyDrugTipLineStr) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject item = element.getAsJsonObject();
            String gene = getString(item, "gene");
            String variant = getString(item, "ori_variant");
            String mutFreq = getString(item, "mutFreq");
            String mutDesc = getMutDesc(gene, variant, mutFreq, bodyDrugNoComplexStr);
            String mutationType = getMutationType(variant);
            String result = "共检测到" + getString(summary, "thisGeneticmarkerVwListSize", "0")
                    + "个体细胞突变，其中" + getString(summary, "somaticCellMedicationNum", "0")
                    + "个与靶向药物相关" + getString(reportJson, "bodyDrugStr");

            rows.add(Arrays.asList(
                    subbarcode,
                    getString(reportJson, "client"),
                    getString(reportJson, "specimentype"),
                    getString(reportJson, "reportdate"),
                    result,
                    gene,
                    getString(item, "transcript"),
                    getString(item, "Exon"),
                    getString(item, "cHGVS"),
                    getString(item, "pHGVS"),
                    getString(item, "site"),
                    mutFreq,
                    getString(item, "ExonicFunc"),
                    mutationType,
                    mutDesc,
                    "",
                    getString(reportJson, "sequencing_depth"),
                    getTmb(summary),
                    getString(reportJson, "barcode")
            ));
        }

        JsonArray crCheckLineStrYF1280 = getArray(reportJson, "crCheckLineStrYF1280");
        JsonArray geneticCancerRiskInfo = getArray(reportJson, "geneticCancerRiskInfo");
        for (JsonElement element : crCheckLineStrYF1280) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject item = element.getAsJsonObject();
            String clinicalSignificance = getString(item, "Clinical_significance");
            if (!"可能致病性变异".equals(clinicalSignificance) && !"致病性变异".equals(clinicalSignificance)) {
                continue;
            }

            String gene = firstNotBlank(getString(item, "gene"), getString(item, "Gene"));
//            String variant = joinWithSpace(
//                    getString(item, "Transcript"),
//                    getString(item, "Exon"),
//                    getString(item, "cHGVS"),
//                    getString(item, "pHGVS")
//            );
            String variant = getString(item, "ori_variant");
            String mutFreq = getString(item, "mutFreq");
            String mutDesc = getMutDesc(gene, variant, mutFreq, geneticCancerRiskInfo);
            String result = "共检测到" + getString(summary, "crCheckLineStrYF1280Size")
                    + "个胚系突变，其中" + getString(summary, "crDrugList")
                    + "个与靶向药物相关" + getString(reportJson, "embryonalDrugStr");

            rows.add(Arrays.asList(
                    subbarcode,
                    getString(reportJson, "client"),
                    getString(reportJson, "specimentype"),
                    getString(reportJson, "reportdate"),
                    result,
                    gene,
                    getString(item, "Transcript"),
                    getString(item, "Exon"),
                    getString(item, "cHGVS"),
                    getString(item, "pHGVS"),
                    "",
                    mutFreq,
                    getString(item, "ExonicFunc"),
                    "cr",
                    mutDesc,
                    getString(item, "Zygosity"),
                    getString(reportJson, "sequencing_depth"),
                    getTmb(summary),
                    getString(reportJson, "barcode")
            ));
        }
        return rows;
    }

    private static void writeExcel(File outputFile, List<List<String>> rows) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        try {
            Sheet sheet = workbook.createSheet("Sheet2");
            Row header = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }
            for (int i = 0; i < rows.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> rowData = rows.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                workbook.write(out);
            }
        } finally {
            workbook.dispose();
        }
    }

    private static String getMutDesc(String gene, String variant, String mutFreq, JsonArray data) {
        for (JsonElement element : data) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject item = element.getAsJsonObject();
            String itemGene = firstNotBlank(getString(item, "gene"), getString(item, "Gene"));
            String itemVariant = firstNotBlank(getString(item, "ori_variant"), getString(item, "variant"), getString(item, "mutation"));
            String itemMutFreq = getString(item, "mutFreq");
            if (StringUtils.equals(gene, itemGene) && StringUtils.equals(variant, itemVariant)
                    && StringUtils.equals(mutFreq, itemMutFreq)) {
                return getString(item, "mutDesc");
            }
        }
        return "";
    }

    private static String getMutationType(String variant) {
        if (variant.contains("Fusion")) {
            return "fusion";
        }
        if (variant.contains("Amplification") || variant.contains("Loss")) {
            return "cnv";
        }
        return "snv/indel";
    }

    private static String getTmb(JsonObject summary) {
        String tmb = getString(summary, "tmb");
        return ("0".equals(tmb.trim()) || "0.0".equals(tmb.trim())) ? "" : tmb;
    }

    private static JsonObject getObject(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null && element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
    }

    private static JsonArray getArray(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return element != null && element.isJsonArray() ? element.getAsJsonArray() : new JsonArray();
    }

    private static String getString(JsonObject jsonObject, String key) {
        return getString(jsonObject, key, "");
    }

    private static String getString(JsonObject jsonObject, String key, String defaultValue) {
        JsonElement element = jsonObject.get(key);
        if (element == null || element.isJsonNull()) {
            return defaultValue;
        }
        return element.isJsonPrimitive() ? element.getAsString() : element.toString();
    }

    private static String firstNotBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return "";
    }

    private static String defaultSlash(String value) {
        return StringUtils.isBlank(value) ? "/" : value;
    }

    private static String joinWithSpace(String... values) {
        List<String> parts = new ArrayList<String>();
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                parts.add(value);
            }
        }
        return StringUtils.join(parts, " ");
    }

    private static String sanitizeFileName(String value) {
        return value.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}
