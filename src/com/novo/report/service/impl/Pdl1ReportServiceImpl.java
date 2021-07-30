package com.novo.report.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PDL1ReportVw;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.PcrTemplate;
import com.novo.report.beans.Pdl1Result;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.beans.SampleFile;
import com.novo.report.dao.two.PCRReportDao;
import com.novo.report.dao.two.PcrTemplateDao;
import com.novo.report.dao.two.Pdl1ReportDao;
import com.novo.report.dao.two.Pdl1ResultDao;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.service.Pdl1ReportService;
import com.novo.report.utils.Base64Utils;
import com.novo.report.utils.FtpUtil;
import com.novo.report.utils.LimsWebserviceProxyUtils;
import com.novo.report.utils.ReportTemplateUtil;
import com.novo.report.utils.YSImg;
import com.novo.report.webservices.GenericServicesSoap;

import net.coobird.thumbnailator.Thumbnails;
@Service
public class Pdl1ReportServiceImpl implements Pdl1ReportService {

	@Autowired
	private Pdl1ReportDao pdl1ReportDao;
	@Autowired
	private PcrTemplateDao pcrTemplateDao;
	@Autowired
	private SampleFileDao sampleFileDao;
	@Autowired
	private PCRReportDao pcrReportDao;
	@Autowired
	private Pdl1ResultDao pdl1ResultDao;
	@Override
	public PaginationVO<PDL1ReportVw> getPdl1ReportByPage(ReprotPageBean reprotPageBean) {
		PaginationVO<PDL1ReportVw> paginationVO = new PaginationVO<PDL1ReportVw>();
		paginationVO.setTotal(pdl1ReportDao.getTotal(reprotPageBean));
		paginationVO.setDataList(pdl1ReportDao.getReportByPage(reprotPageBean));
		return paginationVO;
	}
	@Override
	public List<String> getSubbarcodeListByVw() {
		return pdl1ReportDao.getSubbarcodeListByVw();
	}
	@Override
	public void createReport(ReportTemplate rt, Report report, HttpSession session, String subbarcode,Pdl1Result pr) {
		try {
			String template_id = rt.getTemplate_id();
			PcrTemplate pcrTemplate = pcrTemplateDao.selectTemplateNameById(template_id);
			rt.setTemplate_name(pcrTemplate.getTemplate_name());
			SampleFile sf = sampleFileDao.selectSampleFileBySubbarcode(subbarcode);
			report.setSample_id(sf.getSample_id());
			rt.setClient(sf.getClient());
			rt.setContact(sf.getSales_contact());
			rt.setCustomer(sf.getHospital());
			rt.setEnterdate(sf.getCommission_date());
			rt.setBarcode(subbarcode.toString());
			rt.setReceiveddate(sf.getReceived_date());
			rt.setPatientname(sf.getPerson_name());
			rt.setSex(sf.getGender());
			rt.setBirthday(sf.getBirthday());
			rt.setDiseasetype(sf.getDisease_type());
			rt.setSpecimentype(sf.getSpecimen_type());
			rt.setSpecimenquantity(sf.getSpecimen_quantity());
			rt.setTestedby(report.getTested_by());
			rt.setCheckedby(report.getChecked_by());
			rt.setCheckeddate(report.getChecked_date().substring(0,10));
			rt.setTesteddate(report.getTested_date().substring(0,10));
			rt.setReportdate(report.getReport_date().substring(0,10));
			rt.setTumorcellexpression(pr.getTumor_expression_result());
			rt.setImmunocellexpression(pr.getImmuno_expression_result());
			rt.setTumorcellexpressionpct(pr.getTumor_expression_pct());
			rt.setImmunocellexpressionpct(pr.getImmuno_expression_pct());
			rt.setTumorcelldyingstrenghth(pr.getTumor_cell_dying());
			rt.setImmunocelldyingstrenghth(pr.getImmuno_cell_dying());
			rt.setPdl1picdescription(pr.getPdl1_scope_description());
			rt.setTumorpuritydescription(pr.getPurity_scope_description());
			rt.setPlatforms("PDL1");
			//调用接口获取图片地址
			GenericServicesSoap proxy = (GenericServicesSoap) LimsWebserviceProxyUtils.getLimsWebserviceProxy("http://172.17.8.223/starlims11.novogene/services/generic.asmx?wsdl");
			String[] parameter = {subbarcode};
			Object[] result = (Object[]) proxy.runActionDirect("WebServices.LimsSendFtpToNovo", parameter, "SYSADM", "Lims1234");
			
			if(result.length>0){
				//测试图片抓取
				//获取当前应用的路径 并创建存储图片的文件夹
				String path = session.getServletContext().getRealPath("/");
				String webappsPath = new File(path).getParent();
				String filePath = webappsPath+"/PDL1Photo/";
				File file = new File(filePath);
				if(!file.exists()){//如果有此文件,则不再创建
					file.mkdirs();
				}
				//下载图片到对应位置
				String ihcFileName=subbarcode+"-IHC.JPG";
				String yxdzFileName=subbarcode+"-YXDZ.JPG";
				String jx1FileName=subbarcode+"-1.JPG";
				String jx2FileName=subbarcode+"-2.JPG";
				//远程连接ftp服务器 下载图片
				FtpUtil.downFile("172.17.8.219", 21, "Administrator", "root@Novo2018!Lims", "save/YSPHOTO", ihcFileName, filePath + "/");
				FtpUtil.downFile("172.17.8.219", 21, "Administrator", "root@Novo2018!Lims", "save/YSPHOTO", yxdzFileName, filePath + "/");
				FtpUtil.downFile("172.17.8.219", 21, "Administrator", "root@Novo2018!Lims", "save/YSPHOTO", jx1FileName, filePath + "/");
				FtpUtil.downFile("172.17.8.219", 21, "Administrator", "root@Novo2018!Lims", "save/YSPHOTO", jx2FileName, filePath + "/");
				System.out.println("下载图片成功！");
				String strIHC = Base64Utils.ImageToBase64ByLocal(filePath+ihcFileName);
				String strYXDZ = Base64Utils.ImageToBase64ByLocal(filePath+yxdzFileName);
				String strJX1 = Base64Utils.ImageToBase64ByLocal(filePath+jx1FileName);
				String strJX2 = Base64Utils.ImageToBase64ByLocal(filePath+jx2FileName);
				rt.setSpecimentestingpicture(strIHC);
				rt.setControltestingpicture(strYXDZ);
				rt.setTumorpuritypictureone(strJX1);
				rt.setTumorpuritypicturetwo(strJX2);
				System.out.println("赋值成功！");
				Report rep = ReportTemplateUtil.getFreeMarker(rt,session,report);
				pcrReportDao.insertPcrReport(rep);
				pr.setReport_id(rep.getReport_id());
				pdl1ResultDao.insertPdl1Result(pr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("报告生成失败");
		}
	}
	@Override
	public void deletePdl1ByReportId(Integer report_id) {
		pdl1ReportDao.deletePdl1ByReportId(report_id);
	}
	@Override
	public String getReportFileNameByReportId(Integer report_id) {
		return pdl1ReportDao.getReportFileNameByReportId(report_id);
	}

}
