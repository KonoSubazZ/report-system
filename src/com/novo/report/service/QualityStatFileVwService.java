package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.QualityStatFileVw;
import com.novo.report.beans.QualityStatFileVwPageBean;

public interface QualityStatFileVwService {

	PaginationVO<QualityStatFileVw> getQualityStatFileVwByPage(QualityStatFileVwPageBean qualityStatFileVwPageBean);

	List<String> getDataType();

}
