package com.novo.report.service.impl;

import com.novo.report.beans.*;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.DriverDao;
import com.novo.report.dao.two.LifeDao;
import com.novo.report.service.LifeService;
import com.novo.report.utils.DateUtil;
import com.novo.report.utils.NativeRemoteShellExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LifeServiceImpl implements LifeService {

    @Autowired
    private LifeDao lifeDao;
    @Autowired
    private AnalysisReportDao analysisReportDao;
    @Autowired
    private DriverDao driverDao;

    @Override
    public PaginationVO<DataFileStatus> getDataFileStatusByPage(DataFileStatusPageBean dataFileStatusPageBean) {
        PaginationVO<DataFileStatus> paginationVO = new PaginationVO<DataFileStatus>();
        paginationVO.setTotal(lifeDao.getTotal(dataFileStatusPageBean));
        paginationVO.setDataList(lifeDao.getDataFileStatusByPage(dataFileStatusPageBean));
        return paginationVO;
    }

    @Override
    public void addAnalysisReport(AnalysisReport analysisReport) {
        lifeDao.addAnalysisReport(analysisReport);
    }

    @Override
    public void deleteParseFile(Integer file_id) {
        lifeDao.deleteParseFile(file_id);
    }

    @Override
    public String getStatus(Integer report_id) {
        return lifeDao.getStatus(report_id);
    }

    @Override
    public void editStatus(AnalysisReport analysisReport) {
        try {
            lifeDao.editStatus(analysisReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePrimaryCancerIdBySubbarcode(String subbarcode, Integer report_id) {
        lifeDao.updatePrimaryCancerIdBySubbarcode(subbarcode, report_id);
    }

    @Override
    public Integer getClassIdCount(String subbarcode) {
        return lifeDao.getClassIdCount(subbarcode);
    }

    @Override
    public String getDiseaseClasschinese(Integer report_id) {
        return lifeDao.getDiseaseClasschinese(report_id);
    }

    @Override
    public Integer getPrimaryCancerIdByRID(Integer report_id) {
        return lifeDao.getPrimaryCancerIdByRID(report_id);
    }

    @Override
    public boolean updatePrimaryCancerId(AnalysisReport pr) {
        try {
            AnalysisReport analysisReport = analysisReportDao.getReportFileNameByReportId(pr.getReport_id());
            if (analysisReport == null || analysisReport.getReport_filename() == null) {
                lifeDao.updatePrimaryCancerId(pr.getPrimary_cancer_id(), pr.getReport_id());
            } else {
                pr.setCreated_by(pr.getAnalyzer());
                pr.setCreated_date(DateUtil.getSystemTime());
                analysisReportDao.insertAnalysisReport(pr);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public DiseaseClass getDiseaseClass(Integer report_id) {
        DiseaseClass diseaseClass = lifeDao.getDiseaseClass(report_id);
        return diseaseClass;
    }

    @Override
    public DiseaseClass getDiseaseClassFromSampleInfo(Integer report_id) {
        // TODO Auto-generated method stub
        return lifeDao.getDiseaseClassFromSampleInfo(report_id);
    }

    @Override
    public void updateProductId(AnalysisReport pr) {
        try {
            AnalysisReport analysisReport = analysisReportDao.getReportFileNameByReportId(pr.getReport_id());
            if (analysisReport == null || analysisReport.getReport_filename() == null) {
                lifeDao.updateProductId(pr.getProduct_id(), pr.getReport_id());
            } else {
                pr.setCreated_by(pr.getAnalyzer());
                pr.setCreated_date(DateUtil.getSystemTime());
                analysisReportDao.insertAnalysisReport(pr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product getProduct(Integer report_id) {
        return lifeDao.getProduct(report_id);
    }

    @Override
    public void updateProductByProductId(AnalysisReport pr, String oldProductName) {
        // 更换的产品名称
        String product_name = lifeDao.getProductByProductId(pr.getProduct_id());
        if (!"pd".equals(product_name)) {
            // 增加 old 产品文件
            driverDao.updateParseFile(pr.getSubbarcode(), pr.getAnalysis_date(), product_name, oldProductName);
        }
        pr.setProduct_name(product_name);
        AnalysisReport analysisReportById = analysisReportDao.getAnalysisReportById(pr.getReport_id());
        if (analysisReportById.getReport_filename() != null && !analysisReportById.getReport_filename().equals("")) {
            pr.setCreated_by(pr.getAnalyzer());
            pr.setCreated_date(DateUtil.getSystemTime());
            if (analysisReportById.getBioinfo_check_time() != null) {
                pr.setBioinfo_check_time(analysisReportById.getBioinfo_check_time());
            }
            if (analysisReportById.getBioinfo_checker() != null) {
                pr.setBioinfo_checker(analysisReportById.getBioinfo_checker());
            }
            if (analysisReportById.getMatch_time() != null) {
                pr.setMatch_time(analysisReportById.getMatch_time());
            }
            analysisReportDao.insertAnalysisReport(pr);
        } else {
            analysisReportDao.updateAnalysisReportByReport(pr.getProduct_id(), pr.getPrimary_cancer_id(), pr.getReport_id(), pr.getProduct_name());
        }
    }

    @Override
    public DiseaseClass getDiseaseClassFromSampleCancertype(Integer report_id) {
        // TODO Auto-generated method stub
        return lifeDao.getDiseaseClassFromSampleCancertype(report_id);
    }

    @Override
    public String getGender(Integer report_id) {
        return lifeDao.getGender(report_id);
    }

    @Override
    public String getAnalysis_date(Integer report_id) {
        return lifeDao.getAnalysis_date(report_id);
    }

    @Override
    public String getAnalyzer(Integer report_id) {
        return lifeDao.getAnalyzer(report_id);
    }

    @Override
    public String getFilePath(String subbarcode, String analysis_date) {
        return lifeDao.getFilePath(subbarcode, analysis_date);
    }

    @Override
    public Integer getPendingAndErrorCount(String subbarcode, String analysis_date) {
        return lifeDao.getPendingAndErrorCount(subbarcode, analysis_date);
    }

    @Override
    public void driveOneFile(String filePath) {

        NativeRemoteShellExecutor executor = new NativeRemoteShellExecutor("10.1.183.3", "tumor", "ukl7Yl2f90SH");
        try {
            // 连接服务器
            if (executor.connect()) {
                System.out.println("连接远程服务器成功");

                /* 示例1：执行单个命令
                System.out.println("\n=== 执行命令: ls -l /tmp ===");
                List<String> cmdResult = executor.executeCommand("ls -l /tmp");
                cmdResult.forEach(System.out::println);
                 */

                // 示例2：执行 Shell 脚本（带参数）

                List<String> scriptResult = executor.executeScript("python /TJPROJ2/OBD/report-driver-test/scanner.py", filePath);
                scriptResult.forEach(System.out::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 断开连接
            executor.disconnect();
            System.out.println("\n已断开远程连接");
        }
    }
}
