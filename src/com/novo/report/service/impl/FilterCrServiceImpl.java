package com.novo.report.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.novo.report.beans.*;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.dao.two.SubreportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.dao.two.FilterCrDao;
import com.novo.report.service.FilterCrService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FilterCrServiceImpl implements FilterCrService {
	@Autowired
	private FilterCrDao filterCrDao;
	@Autowired
	private SampleFileDao sampleFileDao;

	@Autowired
	private SubreportDao subreportDao;

	@Override
	public PaginationVO<FilterIlluminaCrSnpIndelFile> getIlluminaFusionByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaCrSnpIndelFile> paginationVO = new PaginationVO<FilterIlluminaCrSnpIndelFile>();
		paginationVO.setTotal(filterCrDao.getIlluminaCrTotal(condition));
		List<FilterIlluminaCrSnpIndelFile> illuminaCrByPage = filterCrDao.getIlluminaCrByPage(condition);
		paginationVO.setDataList(illuminaCrByPage);
		return paginationVO;
	}

	@Override
	public void updateReport(String report, Integer record_id) {
		if("2".equals(report)){
			filterCrDao.updateReportNull(record_id);
		}else{
			filterCrDao.updateReport(report, record_id);
		}
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterCrDao.updateFiltered(filtered_rationale,record_id);
		
	}

	@Override
	public List<Map<String, Object>> getCRClinicalSignificanceInfo(Integer reportId) {
		// 1. 获取数据库数据
		Map<String, Object> reportDataInfo = subreportDao.getReportJSON(reportId);
		if (reportDataInfo == null) {
			return Collections.emptyList();
		}

		// 2. 获取JSON字符串
		String reportDetailStr = (String) reportDataInfo.get("report_detail");
		if (reportDetailStr == null || reportDetailStr.trim().isEmpty()) {
			return Collections.emptyList();
		}

		Gson gson = new Gson();
		JsonObject reportJson = gson.fromJson(reportDetailStr, JsonObject.class);

		// 3. 安全获取数组（防止空指针）
		JsonArray jsonArray;
		try {
			jsonArray = reportJson.getAsJsonArray("crCheckLineStrYF1280");
			if (jsonArray == null) {
				return Collections.emptyList();
			}
		} catch (Exception e) {
			return Collections.emptyList();
		}

		// 4. 【核心】直接把 JSON 数组转成 List<Map<String,Object>>
		List<Map<String, Object>> result = gson.fromJson(
				jsonArray,
				new TypeToken<List<Map<String, Object>>>() {}.getType()
		);

		// 5. 返回前端需要的格式
		return result;
	}
}
