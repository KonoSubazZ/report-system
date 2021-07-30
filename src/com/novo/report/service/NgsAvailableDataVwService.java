package com.novo.report.service;

import com.novo.report.beans.NgsAvailableDataPageBean;
import com.novo.report.beans.NgsAvailableDataVw;
import com.novo.report.beans.NgsListAuto;
import com.novo.report.beans.PaginationVO;

public interface NgsAvailableDataVwService {

	PaginationVO<NgsAvailableDataVw> getNgsAvailableDataVwByPage(NgsAvailableDataPageBean ngsAvailableDataPageBean);

	NgsListAuto getSubbarcodeAndProductNameListByPlatform(NgsAvailableDataPageBean ngsAvailableDataPageBean);

}
