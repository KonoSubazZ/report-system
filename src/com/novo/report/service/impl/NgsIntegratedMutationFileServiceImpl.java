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

import com.novo.report.beans.ExportNgsQueryFile;
import com.novo.report.beans.IntegratedMutationFileVw;
import com.novo.report.beans.NgsIntegratedMutationFilePageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.NgsIntegratedMutationFileDao;
import com.novo.report.service.NgsIntegratedMutationFileService;
import com.novo.report.utils.DeleteFileUtil;

import sun.misc.BASE64Encoder;

@Service
public class NgsIntegratedMutationFileServiceImpl implements NgsIntegratedMutationFileService {

	@Autowired
	private NgsIntegratedMutationFileDao ngsIntegratedMutationFileDao;
	
	@Override
	public PaginationVO<IntegratedMutationFileVw> getNgsIntegratedMutationFileListByPage(
			NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean) {
		PaginationVO<IntegratedMutationFileVw> paginationVO = new PaginationVO<IntegratedMutationFileVw>();
		paginationVO.setTotal(ngsIntegratedMutationFileDao.getTotal(ngsIntegratedMutationFilePageBean));
		paginationVO.setDataList(ngsIntegratedMutationFileDao.getNgsIntegratedMutationFileListByPage(ngsIntegratedMutationFilePageBean));
		return paginationVO;
	}

	@Override
	@SuppressWarnings("all")
	public Object exportNgsFile(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean,HttpSession session) {
		List<ExportNgsQueryFile> ExportNgsQueryFileList = ngsIntegratedMutationFileDao.exportNgsFile(ngsIntegratedMutationFilePageBean);
		// 第一步，创建一个webbook，对应一个Excel文件  
		HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("ngs查询数据");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 创建一个居中格式  
  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("到样时间");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("样本编号");  
        cell.setCellStyle(style);  
        // 第五步，写入实体数据
        for (int i = 0; i < ExportNgsQueryFileList.size(); i++) {
        	 row = sheet.createRow((int) i + 1);
        	 ExportNgsQueryFile exportNgsQueryFile = ExportNgsQueryFileList.get(i);
        	 // 第四步，创建单元格，并设置值  
             row.createCell((short) 0).setCellValue(exportNgsQueryFile.getReceived_date());  
             row.createCell((short) 1).setCellValue(exportNgsQueryFile.getSubbarcode());  
		}
        //第六步 导出文件
        try {
        	//获取当前应用的路径
        	String path = session.getServletContext().getRealPath("/");
        	System.out.println(path);
			FileOutputStream xls = new FileOutputStream(path+"ngs查询数据.xls");
			wb.write(xls); 
			xls.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void downloadNgsFile(HttpServletResponse response, HttpServletRequest request,HttpSession session) {
		String filepath = session.getServletContext().getRealPath("/");
		String filename = "ngs查询数据.xls";
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
