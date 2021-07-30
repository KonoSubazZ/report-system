package com.novo.report.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novo.report.beans.OfflineReport;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.dao.two.OfflineReportDao;
import com.novo.report.service.OfflineReportService;

import sun.misc.BASE64Encoder;
@Transactional
@Service
public class OfflineReportServiceImpl implements OfflineReportService {
	
	@Autowired
	private OfflineReportDao offlineReportDao;
	@Override
	public Object getOfflineReportByPage(ReprotPageBean reprotPageBean) {
		PaginationVO<OfflineReport> paginationVO = new PaginationVO<OfflineReport>();
		paginationVO.setTotal(offlineReportDao.getTotal(reprotPageBean));
		paginationVO.setDataList(offlineReportDao.getReportByPage(reprotPageBean));
		return paginationVO;
	}
	@Override
	public void addOfflineReport(OfflineReport offlineReport) {
		offlineReportDao.addOfflineReport(offlineReport);
	}
	@Override
	public void download(String report_file_path, String report_filename, HttpServletResponse response,HttpServletRequest request) throws Exception{
		
		//获得请求头中的User-Agent
		String agent = request.getHeader("User-Agent");
		//根据不同浏览器进行不同的编码
		String filenameEncoder = "";
		if (agent.contains("MSIE")||agent.contains("Trident")) {
			// IE浏览器
			filenameEncoder = URLEncoder.encode(report_filename, "utf-8");
			filenameEncoder = filenameEncoder.replace("+", " ");
		} else if (agent.contains("Firefox")) {
			// 火狐浏览器
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filenameEncoder = "=?utf-8?B?"+ base64Encoder.encode(report_filename.getBytes("utf-8")) + "?=";
		} else {
			// 其它浏览器
			filenameEncoder = URLEncoder.encode(report_filename, "utf-8");				
		}

		//要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
		response.setContentType(request.getServletContext().getMimeType(report_filename));
		//告诉客户端该文件不是直接解析 而是以附件形式打开(下载) 
		response.setHeader("Content-Disposition", "attachment;filename="+filenameEncoder);
		//根据路径读取文件
		InputStream in = new FileInputStream(report_file_path+report_filename);
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
	public OfflineReport getOfflineReportById(Integer report_id) {
		return offlineReportDao.getOfflineReportById(report_id);
	}
	@Override
	public void updateFileNameOneById(String report_id, String report_filenameone) {
		offlineReportDao.updateFileNameOneById(report_id,report_filenameone);
	}
	@Override
	public void updateFileNameTwoById(String report_id, String report_filenametwo) {
		offlineReportDao.updateFileNameTwoById(report_id,report_filenametwo);
	}
	@Override
	public String getStatus(Integer report_id) {
		return offlineReportDao.getStatus(report_id);
	}
	@Override
	public void editStatus(OfflineReport offlineReport) {
		offlineReportDao.editStatus(offlineReport);
	}
	@Override
	public void UpdateStatus(OfflineReport offlineReport) {
		offlineReportDao.UpdateStatus(offlineReport);
	}

}
