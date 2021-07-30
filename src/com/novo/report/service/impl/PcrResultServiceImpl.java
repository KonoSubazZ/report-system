package com.novo.report.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.ExportPcrResultVw;
import com.novo.report.beans.NameAndDateBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.PcrResultPageBean;
import com.novo.report.beans.PcrResultVw;
import com.novo.report.beans.PcrResultVwBean;
import com.novo.report.dao.two.PcrResultDao;
import com.novo.report.service.PcrResultService;
import com.novo.report.utils.DeleteFileUtil;

import sun.misc.BASE64Encoder;
@Service
public class PcrResultServiceImpl implements PcrResultService {
	
	@Autowired
	private PcrResultDao pcrResultDao;
	@Override
	public PaginationVO<PcrResultVw> getpcrResultByPage(PcrResultPageBean pcrResultPageBean) {
		PaginationVO<PcrResultVw> paginationVO = new PaginationVO<PcrResultVw>();
		paginationVO.setTotal(pcrResultDao.getTotal(pcrResultPageBean));
		paginationVO.setDataList(pcrResultDao.getpcrResultByPage(pcrResultPageBean));
		return paginationVO;
	}
	@Override
	public List<String> getGeneSymbolListByVw() {
		return pcrResultDao.getGeneSymbolListByVw();
	}
	@Override
	public List<String> getVariantListByVw() {
		return pcrResultDao.getVariantListByVw();
	}
	@Override
	public List<NameAndDateBean> getNameAndData(PcrResultVwBean pcrResultVwBean) {
		List<NameAndDateBean> list= null;
		if("specimen_type".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getSpecimenTypeAndCount(pcrResultVwBean);
		}
		if("category".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getCategoryAndCount(pcrResultVwBean);
		}
		if("cancertype".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getCancertypeAndCount(pcrResultVwBean);		
		}
		if("clinicalremark".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getClinicalremarkAndCount(pcrResultVwBean);
		}
		if("pathologicaltype".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getPathologicaltypeAndCount(pcrResultVwBean);
		}
		if("mutation_frequency".equals(pcrResultVwBean.getCategoryType())){  //mutation_frequency
			list = pcrResultDao.getMutationFrequencyAndCount(pcrResultVwBean);
		}
		if("gene".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getGeneAndCount(pcrResultVwBean);
		}
		if("variant".equals(pcrResultVwBean.getCategoryType())){
			list = pcrResultDao.getVariantAndCount(pcrResultVwBean);
		}
		return list;
	}
	@SuppressWarnings("all")
	@Override
	public Object exportPcrFile(PcrResultPageBean pcrResultPageBean, HttpSession session) {
		List<ExportPcrResultVw> ExportPcrResultVwList = pcrResultDao.exportPcrFile(pcrResultPageBean);
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("pcr查询数据");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 创建一个居中格式  
        
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("收样时间");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("样本编码");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("患者姓名");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);  
        cell.setCellValue("样本类型");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("检测项目");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("癌肿");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("病理分型");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("临床分期");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("销售渠道");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);  
        cell.setCellValue("销售姓名");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);  
        cell.setCellValue("送检医院");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);  
        cell.setCellValue("送检科室");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);  
        cell.setCellValue("送检医生");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 13);  
        cell.setCellValue("报告时间");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 14);  
        cell.setCellValue("检测位点");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 15);  
        cell.setCellValue("检测结果(即突变丰度)");  
        cell.setCellStyle(style);
        
        // 第五步，写入实体数据
        for (int i = 0; i < ExportPcrResultVwList.size(); i++) {
        	 row = sheet.createRow((int) i + 1);
        	 ExportPcrResultVw exportPcrResultVw = ExportPcrResultVwList.get(i);
        	 // 第四步，创建单元格，并设置值  
             row.createCell((short) 0).setCellValue(exportPcrResultVw.getReceived_date());  
             row.createCell((short) 1).setCellValue(exportPcrResultVw.getSubbarcode()); 
             row.createCell((short) 2).setCellValue(exportPcrResultVw.getPerson_name());
             row.createCell((short) 3).setCellValue(exportPcrResultVw.getSpecimen_type());
             row.createCell((short) 4).setCellValue(exportPcrResultVw.getCategory());
             row.createCell((short) 5).setCellValue(exportPcrResultVw.getCancertype());
             row.createCell((short) 6).setCellValue(exportPcrResultVw.getPathologicaltype());
             row.createCell((short) 7).setCellValue(exportPcrResultVw.getClinicalstages());
             row.createCell((short) 8).setCellValue(exportPcrResultVw.getLibraryname());
             row.createCell((short) 9).setCellValue(exportPcrResultVw.getSales_contact());
             row.createCell((short) 10).setCellValue(exportPcrResultVw.getHospital());
             row.createCell((short) 11).setCellValue(exportPcrResultVw.getLocationname());
             row.createCell((short) 12).setCellValue(exportPcrResultVw.getDoctorname());
             row.createCell((short) 13).setCellValue(exportPcrResultVw.getReport_date());
             row.createCell((short) 14).setCellValue(exportPcrResultVw.getVariant());
             row.createCell((short) 15).setCellValue(exportPcrResultVw.getVariant_frequency());
		}
        //第六步 导出文件
        try {
        	//获取当前应用的路径
        	String path = session.getServletContext().getRealPath("/");
			FileOutputStream xls = new FileOutputStream(path+"pcr查询数据.xls");
			wb.write(xls); 
			xls.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public void downloadPcrFile(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		String filepath = session.getServletContext().getRealPath("/");
		String filename = "pcr查询数据.xls";
		//获得请求头中的User-Agent
		String agent = request.getHeader("User-Agent");
		try {
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
			InputStream in = new FileInputStream(filepath+filename);
			//将文件写入到response缓冲区
			response.getOutputStream();
			//获得输出流---通过response获得的输出流 用于向客户端写内容
			ServletOutputStream out = response.getOutputStream();
			//下载
			IOUtils.copy(in, out);
			//关流
			in.close();
			//删除文件
			DeleteFileUtil.deleteFiles(filepath+filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
