package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.FalsePositive;
import com.novo.report.beans.FalsePositivePageBean;
import com.novo.report.beans.PaginationVO;

public interface FalsePositiveService {

	PaginationVO<FalsePositive> getfalsePositiveByPage(FalsePositivePageBean falsePositivePageBean);

	void saveFalsePositive(FalsePositive falsePositive);

	void deletefalsePositive(Integer id);

	FalsePositive getFalsePositiveById(int id);

	void updateFalsePositive(FalsePositive falsePositive);

	List<FalsePositive> getFalsePositiveAll();

}
