package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;
import com.novo.report.beans.DiseaseClass;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.Product;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.LifeDao;
import com.novo.report.service.LifeService;
import com.novo.report.utils.DateUtil;
@Service
public class LifeServiceImpl implements LifeService {
	
	@Autowired
	private LifeDao lifeDao;
	@Autowired
	private AnalysisReportDao analysisReportDao;
	
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
	public void updatePrimaryCancerIdBySubbarcode(String subbarcode,Integer report_id) {
		lifeDao.updatePrimaryCancerIdBySubbarcode(subbarcode,report_id);
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
			if(analysisReport==null || analysisReport.getReport_filename() == null){
				lifeDao.updatePrimaryCancerId(pr.getPrimary_cancer_id(),pr.getReport_id());
			}else{
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
			if(analysisReport==null || analysisReport.getReport_filename() == null){
				lifeDao.updateProductId(pr.getProduct_id(),pr.getReport_id());
			}else{
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
	public void updateProductByProductId(AnalysisReport pr) {
		AnalysisReport analysisReportById = analysisReportDao.getAnalysisReportById(pr.getReport_id());
		if(analysisReportById.getReport_filename() != null && !analysisReportById.getReport_filename().equals("")) {
			pr.setCreated_by(pr.getAnalyzer());
			pr.setCreated_date(DateUtil.getSystemTime());
			if(analysisReportById.getBioinfo_check_time() != null) {
				pr.setBioinfo_check_time(analysisReportById.getBioinfo_check_time());
			}
			if(analysisReportById.getBioinfo_checker() != null) {
				pr.setBioinfo_checker(analysisReportById.getBioinfo_checker());
			}
			if(analysisReportById.getMatch_time() != null) {
				pr.setMatch_time(analysisReportById.getMatch_time());
			}
			analysisReportDao.insertAnalysisReport(pr);
		}else {
			analysisReportDao.updateAnalysisReportByReport(pr.getProduct_id(),pr.getPrimary_cancer_id(), pr.getReport_id());
		}
	}
	

}
