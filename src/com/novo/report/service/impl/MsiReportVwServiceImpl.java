package com.novo.report.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.MsiReportVw;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.PcrTemplate;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.beans.SampleFile;
import com.novo.report.dao.two.MsiReportVwDao;
import com.novo.report.dao.two.PCRReportDao;
import com.novo.report.dao.two.PcrTemplateDao;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.service.MsiReportVwService;
import com.novo.report.utils.ReportTemplateUtil;
@Service
public class MsiReportVwServiceImpl implements MsiReportVwService {
	
	@Autowired
	private PCRReportDao pcrReportDao;
	@Autowired
	private MsiReportVwDao msiReportVwDao;
	@Autowired
	private PcrTemplateDao pcrTemplateDao;
	@Autowired
	private SampleFileDao sampleFileDao;
	
	@Override
	public PaginationVO<MsiReportVw> getMsiReportByPage(ReprotPageBean reprotPageBean) {
		PaginationVO<MsiReportVw> paginationVO = new PaginationVO<MsiReportVw>();
		paginationVO.setTotal(msiReportVwDao.getTotal(reprotPageBean));
		paginationVO.setDataList(msiReportVwDao.getMsiReportByPage(reprotPageBean));
		return paginationVO;
	}
	@Override
	public List<String> getBarcodeListByVw() {
		return msiReportVwDao.getBarcodeListByVw();
	}
	@Override
	public void createReport(ReportTemplate rt, Report pr, HttpSession session, String barcode) throws Exception {
		String template_id = rt.getTemplate_id();
		PcrTemplate pcrTemplate = pcrTemplateDao.selectTemplateNameById(template_id);
		rt.setTemplate_name(pcrTemplate.getTemplate_name());
		List<SampleFile>  sfList = sampleFileDao.getSampleFileListByBarcode(barcode);
		if(sfList.size()>=2){
			SampleFile sf = sfList.get(sfList.size()-2);
			pr.setSample_id(sf.getSample_id());
			rt.setClient(sf.getClient());
			rt.setEnterdate(sf.getCommission_date());
			rt.setContact(sf.getSales_contact());
			rt.setCustomer(sf.getHospital());
			rt.setBarcode(barcode);
			rt.setReceiveddate(sf.getReceived_date());
			rt.setReportdate(pr.getReport_date().substring(0,10));
			rt.setReportreceiver(sf.getClient());
			rt.setPatientname(sf.getPerson_name());
			rt.setSex(sf.getGender());
			rt.setBirthday(sf.getBirthday());
			rt.setDiseasetype(sf.getDisease_type());
			rt.setSpecimentype(sf.getSpecimen_type());
			rt.setSpecimenquantity(sf.getSpecimen_quantity());
			SampleFile sf2 = sfList.get(sfList.size()-1);
			rt.setSpecimentypetwo(sf2.getSpecimen_type());
			rt.setSpecimenquantitytwo(sf2.getSpecimen_quantity());
			rt.setTestedby(pr.getTested_by());
			rt.setCheckedby(pr.getChecked_by());
			rt.setCheckeddate(pr.getChecked_date().substring(0,10));
			rt.setTesteddate(pr.getTested_date().substring(0,10));
			rt.setPlatforms("MSI");
		}
		Report report = ReportTemplateUtil.getFreeMarker(rt,session,pr);
		pcrReportDao.insertPcrReport(report);
	}

}
