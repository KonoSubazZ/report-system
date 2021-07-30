package com.novo.report.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novo.report.beans.Report;
import com.novo.report.beans.PCRReportVw;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.PcrResult;
import com.novo.report.beans.PcrTemplate;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.beans.SampleFile;
import com.novo.report.dao.two.PCRReportDao;
import com.novo.report.dao.two.PcrResultDao;
import com.novo.report.dao.two.PcrTemplateDao;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.service.PCRReportService;
import com.novo.report.utils.ReportTemplateUtil;

import sun.misc.BASE64Encoder;
@Service
public class PCRReportServiceImpl implements PCRReportService {
	
	@Autowired
	private PCRReportDao pcrReportDao;
	@Autowired
	private PcrTemplateDao pcrTemplateDao;
	@Autowired
	private SampleFileDao sampleFileDao;
	@Autowired  
	private PcrResultDao pcrResultDao;
	//分页查询
	@Override
	public PaginationVO<PCRReportVw> getReportByPage(ReprotPageBean reprotPageBean) {
		PaginationVO<PCRReportVw> paginationVO = new PaginationVO<PCRReportVw>();
		paginationVO.setTotal(pcrReportDao.getTotal(reprotPageBean));
		paginationVO.setDataList(pcrReportDao.getReportByPage(reprotPageBean));
		return paginationVO;
	}
	//根据id获取对象
	@Override
	public PCRReportVw getReportById(Integer report_id) {
		return pcrReportDao.getReportById(report_id);
	}
	//获取视图sampleIdList
	@Override
	public List<String> getSubbarcodeListByVw() {
		return pcrReportDao.getSubbarcodeListByVw();
	}
	@Override
	public void addPcrReport(Report report) {
		pcrReportDao.insertPcrReport(report);
	}
	@Override
	public void download(Integer report_id, HttpServletResponse response, HttpServletRequest request) throws Exception {
		PCRReportVw pcrReportVw= pcrReportDao.getReportById(report_id);
		String filepath = pcrReportVw.getReport_file_path();
		String filename = pcrReportVw.getReport_filename();
	
		//获得请求头中的User-Agent
		String agent = request.getHeader("User-Agent");
		//根据不同浏览器进行不同的编码
		String filenameEncoder = "";
		if (agent.contains("MSIE")||agent.contains("Trident")) {
			// IE浏览器
			filenameEncoder = URLEncoder.encode(filename, "utf-8");
			filenameEncoder = filenameEncoder.replace("+", " ");
		} else if (agent.contains("Firefox")) {
			// 火狐浏览器
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filenameEncoder = "=?utf-8?B?"+ base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
		} else {
			// 其它浏览器
			filenameEncoder = URLEncoder.encode(filename, "utf-8");				
		}

		//要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
		response.setContentType(request.getServletContext().getMimeType(filename));
		//告诉客户端该文件不是直接解析 而是以附件形式打开(下载) 
		response.setHeader("Content-Disposition", "attachment;filename="+filenameEncoder);
		//根据路径读取文件
		InputStream in = new FileInputStream(filepath+"/"+filename);
		//将文件写入到response缓冲区
		response.getOutputStream();
		//获得输出流---通过response获得的输出流 用于向客户端写内容
		ServletOutputStream out = response.getOutputStream();
		//下载
		IOUtils.copy(in, out);
		//关流
		in.close();
	}
	@Override
	public void createReport(ReportTemplate rt, Report pr, HttpSession session, String subbarcode, PcrResult pcrResult) throws Exception {
		String template_id = rt.getTemplate_id();
		PcrTemplate pcrTemplate = pcrTemplateDao.selectTemplateNameById(template_id);
		rt.setTemplate_name(pcrTemplate.getTemplate_name());
		SampleFile sf = sampleFileDao.selectSampleFileBySubbarcode(subbarcode);
		pr.setSample_id(sf.getSample_id());
		rt.setClient(sf.getClient());
		rt.setAge(sf.getAge());
		rt.setContact(sf.getSales_contact());
		rt.setCustomer(sf.getHospital());
		rt.setEnterdate(sf.getCommission_date());
		rt.setBarcode(subbarcode.toString());
		rt.setReceiveddate(sf.getReceived_date());
		rt.setReportreceiver(sf.getClient());
		rt.setPatientname(sf.getPerson_name());
		rt.setSex(sf.getGender());
		rt.setBirthday(sf.getBirthday());
		rt.setDiseasetype(sf.getDisease_type());
		rt.setSpecimentype(sf.getSpecimen_type());
		rt.setSpecimenquantity(sf.getSpecimen_quantity());
		rt.setPatientnumber(subbarcode.toString());
		rt.setHospital(sf.getHospital());
		rt.setCollectdate((sf.getCollect_date()==null || "".equals(sf.getCollect_date()))?"":sf.getCollect_date().substring(0,10));
		rt.setTestedby(pr.getTested_by());
		rt.setCheckedby(pr.getChecked_by());
		rt.setCheckeddate(pr.getChecked_date().substring(0,10));
		rt.setTesteddate(pr.getTested_date().substring(0,10));
		rt.setReportdate(pr.getReport_date().substring(0,10));
		rt.setPlatforms("PCR");
		if(rt.getV600efrequency()!=null && Double.parseDouble(rt.getV600efrequency())>0.1) {
			rt.setV600efrequency(rt.getV600efrequency()+"%");
			rt.setV600etestresult("检测到突变");
			rt.setV600etestresult2("有突变");
		} else if(rt.getV600efrequency()!=null && Double.parseDouble(rt.getV600efrequency())==0) {
			rt.setV600efrequency("/");
			rt.setV600etestresult("未检测到突变");
			rt.setV600etestresult2("无突变");
		} else {
			rt.setV600efrequency(rt.getV600efrequency()+"%");
			rt.setV600etestresult("未检测到突变");
			rt.setV600etestresult2("无突变");
		}
		if(rt.getC797sfrequency()!=null && Double.parseDouble(rt.getC797sfrequency())>0.1) {
			rt.setC797sfrequency(rt.getC797sfrequency()+"%");
			rt.setC797stestresult("检测到突变");
			rt.setC797stestresult2("有突变");
		} else if(rt.getC797sfrequency()!=null && Double.parseDouble(rt.getC797sfrequency())==0) {
			rt.setC797stestresult("未检测到突变");
			rt.setC797stestresult2("无突变");
			rt.setC797sfrequency("/");
		} else {
			rt.setC797sfrequency(rt.getC797sfrequency()+"%");
			rt.setC797stestresult("未检测到突变");
			rt.setC797stestresult2("无突变");
		}
		if(rt.getT790mfrequency()!=null && Double.parseDouble(rt.getT790mfrequency())>=0.1) {
			rt.setT790mfrequency(rt.getT790mfrequency()+"%");
			rt.setT790mtestresult("检测到突变");
			rt.setT790mtestresult2("有突变");
		} else if (rt.getT790mfrequency()!=null && Double.parseDouble(rt.getT790mfrequency())==0) {
			rt.setT790mfrequency("/");
			rt.setT790mtestresult("未检测到突变");
			rt.setT790mtestresult2("无突变");
		} else {
			rt.setT790mfrequency(rt.getT790mfrequency()+"%");
			rt.setT790mtestresult("未检测到突变");
			rt.setT790mtestresult2("无突变");
		}
		if(rt.getL858rfrequency()!=null && Double.parseDouble(rt.getL858rfrequency())>=0.1) {
			rt.setL858rfrequency(rt.getL858rfrequency()+"%");
			rt.setL858rtestresult("检测到突变");
			rt.setL858rtestresult2("有突变");
		} else if(rt.getL858rfrequency()!=null && Double.parseDouble(rt.getL858rfrequency())==0) {
			rt.setL858rfrequency("/");
			rt.setL858rtestresult("未检测到突变");
			rt.setL858rtestresult2("无突变");
		} else {
			rt.setL858rfrequency(rt.getL858rfrequency()+"%");
			rt.setL858rtestresult("未检测到突变");
			rt.setL858rtestresult2("无突变");
		}
		if(rt.getEgfr19delfrequency()!=null && Double.parseDouble(rt.getEgfr19delfrequency())>=0.1) {
			rt.setEgfr19delfrequency(rt.getEgfr19delfrequency()+"%");
			rt.setEgfr19deltestresult("检测到突变");
			rt.setEgfr19deltestresult2("有突变");
		} else if(rt.getEgfr19delfrequency()!=null && Double.parseDouble(rt.getEgfr19delfrequency())==0) {
			rt.setEgfr19delfrequency("/");
			rt.setEgfr19deltestresult("未检测到突变");
			rt.setEgfr19deltestresult2("无突变");
		} else {
			rt.setEgfr19delfrequency(rt.getEgfr19delfrequency()+"%");
			rt.setEgfr19deltestresult("未检测到突变");
			rt.setEgfr19deltestresult2("无突变");
		}
		
		Report report = ReportTemplateUtil.getFreeMarker(rt,session,pr);
		pcrReportDao.insertPcrReport(report);
		if(pcrResult.getPcrVariantIdAndFrequency0()!=null){
			String[] strings = pcrResult.getPcrVariantIdAndFrequency0().split(",");
			pcrResult.setPcr_variant_id(Integer.valueOf(strings[0]));
			pcrResult.setVariant_frequency(strings[1]);
			pcrResult.setReport_id(report.getReport_id());
			pcrResultDao.addResult(pcrResult);
		}
		if (pcrResult.getPcrVariantIdAndFrequency1()!=null) {
			String[] strings = pcrResult.getPcrVariantIdAndFrequency1().split(",");
			pcrResult.setPcr_variant_id(Integer.valueOf(strings[0]));
			pcrResult.setVariant_frequency(strings[1]);
			pcrResult.setReport_id(report.getReport_id());
			pcrResultDao.addResult(pcrResult);
		}
		if (pcrResult.getPcrVariantIdAndFrequency2()!=null) {
			String[] strings = pcrResult.getPcrVariantIdAndFrequency2().split(",");
			pcrResult.setPcr_variant_id(Integer.valueOf(strings[0]));
			pcrResult.setVariant_frequency(strings[1]);
			pcrResult.setReport_id(report.getReport_id());
			pcrResultDao.addResult(pcrResult);
		}
	}
	@Override
	public List<String> getGeneSymbolList(Integer test_id) {
		return pcrResultDao.getGeneSymbolList(test_id);
	}
	@Override
	public List<String> getVariantListByGene(String gene_symbol) {
		return pcrResultDao.getVariantListByGene(gene_symbol);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void addResult(String pcr_variant_id_str,String variant_frequency_str,PcrResult pcrResult, Report report) {
		try {
			pcrReportDao.insertPcrReport(report);
			pcrResult.setReport_id(report.getReport_id());
			String[] pcr_variant_id_arr = pcr_variant_id_str.split(",");
			String[] variant_frequency_arr = variant_frequency_str.split(",");
			for (int i = 0; i < pcr_variant_id_arr.length; i++) {
				pcrResult.setPcr_variant_id(Integer.parseInt(pcr_variant_id_arr[i]));
				pcrResult.setVariant_frequency(variant_frequency_arr[i]);
				pcrResultDao.addResult(pcrResult);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
