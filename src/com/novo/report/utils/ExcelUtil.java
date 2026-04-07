package com.novo.report.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * POI 3.16 通用 Excel 生成工具类
 * 外部传入：表头、数据、文件路径、文件名
 */
public class ExcelUtil {

    /**
     * 生成 Excel .xlsx 文件
     * @param filePath 生成路径 例如 D:/files/
     * @param fileName 文件名 例如 test.xlsx
     * @param headers  表头数组 例如 {"ID","姓名","年龄"}
     * @param dataList 数据集合 每一行是一个 List<String>
     * @throws IOException 文件读写异常
     */
    public static void createExcel(String filePath, String fileName, String[] headers, List<List<String>> dataList) throws IOException {
        // 创建 SXSSF 工作簿（支持大数据，不内存溢出）
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Sheet1");

        // ===================== 1. 创建表头样式 =====================
        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
        Font headFont = workbook.createFont();
        headFont.setBold(true); // 加粗
        headStyle.setFont(headFont);

        // ===================== 2. 创建表头行 =====================
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headStyle);
        }

        // ===================== 3. 填充数据 =====================
        int rowIndex = 1;
        for (List<String> rowData : dataList) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < rowData.size(); i++) {
                row.createCell(i).setCellValue(rowData.get(i));
            }
        }

        // ===================== 4. 自动调整列宽 =====================
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // ===================== 5. 写入文件 =====================
        String fullPath = filePath + fileName;
        try (FileOutputStream out = new FileOutputStream(fullPath)) {
            workbook.write(out);
        } finally {
            workbook.dispose(); // 释放临时文件
        }
    }
}