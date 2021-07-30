package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FalsePositive;
import com.novo.report.beans.FalsePositivePageBean;

public interface FalsePositiveDao {

	Long getfalsePositiveTotal(FalsePositivePageBean falsePositivePageBean);

	List<FalsePositive> getfalsePositiveByPage(FalsePositivePageBean falsePositivePageBean);

	void saveFalsePositive(FalsePositive falsePositive);

	void deletefalsePositive(Integer id);

	FalsePositive getFalsePositiveById(int id);

	void updateFalsePositive(FalsePositive falsePositive);

	List<FalsePositive> getFalsePositiveAll();

}
