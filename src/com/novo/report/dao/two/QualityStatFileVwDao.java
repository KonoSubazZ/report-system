package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.QualityStatFileVw;
import com.novo.report.beans.QualityStatFileVwPageBean;

public interface QualityStatFileVwDao {

	Long getTotal(QualityStatFileVwPageBean qualityStatFileVwPageBean);

	List<QualityStatFileVw> getPage(QualityStatFileVwPageBean qualityStatFileVwPageBean);

	List<String> getDataType();

}
