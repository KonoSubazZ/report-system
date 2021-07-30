package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.NgsAvailableDataPageBean;
import com.novo.report.beans.NgsAvailableDataVw;

public interface NgsAvailableDataVwDao {

	Long getTotal(NgsAvailableDataPageBean ngsAvailableDataPageBean);

	List<NgsAvailableDataVw> getNgsAvailableDataVwByPage(NgsAvailableDataPageBean ngsAvailableDataPageBean);

	List<String> getSubbarcodeListByPlatform(NgsAvailableDataPageBean ngsAvailableDataPageBean);
	
	List<String> getProductNameListByPlatform(NgsAvailableDataPageBean ngsAvailableDataPageBean);

}
