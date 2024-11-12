package com.novo.report.controller;

import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.PageBean;
import com.novo.report.beans.User;
import com.novo.report.service.DataFileStatusService;
import com.novo.report.service.UserService;
import com.novo.report.utils.DateUtil;
import com.novo.report.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("dataFileStatus")
public class DataFileStatusController {
	
	@Autowired
	private DataFileStatusService dataFileStatusService;
	
	//保存用户
	@RequestMapping("saveDataFileStatus")
	@ResponseBody
	public Object saveDataFileStatus(DataFileStatus dataFileStatus, HttpServletRequest request){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			dataFileStatus.setFile_path("");
			dataFileStatus.setFile_name(dataFileStatus.getSubbarcode());
			dataFileStatus.setData_type("");
			dataFileStatus.setFile_type("");
			if (StringUtils.isEmpty(dataFileStatus.getAnalysis_date())) {
				dataFileStatus.setAnalysis_date(format.format(new Date()));
			}
			dataFileStatus.setBarcode(dataFileStatus.getSubbarcode());
			if (StringUtils.isEmpty(dataFileStatus.getProduct_name())) {
				dataFileStatus.setProduct_name("");
			}
			dataFileStatus.setPlatform("Illumina");
			dataFileStatus.setStatus("Loaded");
			User user = (User) request.getSession().getAttribute("user");
			dataFileStatus.setCreated_by(user.getUser_account());
			dataFileStatus.setCreated_date(DateUtil.getSystemTime());
			dataFileStatus.setUpdate_date(DateUtil.getSystemTime());
			dataFileStatusService.saveDataFileStatus(dataFileStatus);
			jsonMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", false);
			jsonMap.put("mgs", "添加失败！");
		}
		return jsonMap;
	}
}
