package com.novo.report.service;

import com.novo.report.beans.ChemicalMarkerVw;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.mod.ModProductDesc;

import java.util.List;

/**
 * 所有与模块化有关的数据接口
 */
public interface ModuleService {

	/**
	 * 根据模板名称获取产品检测项目描述
	 * @param templateName
	 * @return
	 */
	ModProductDesc getProductDesc(String templateName);
}
