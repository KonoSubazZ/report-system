package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;
import org.apache.ibatis.annotations.Param;

public interface DriverDao {

	Long getTotalByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean);

	List<DataFileStatus> getDataFileStatusByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean);

	void deleteParseFile(Integer file_id);

	void updateParseFile(@Param("subbarcode")String subbarcode, @Param("analysis_date")String analysis_date, @Param("product_name")String product_name, @Param("oldProductName")String oldProductName);

	void deletePendingAndError(String analysis_date);
}
