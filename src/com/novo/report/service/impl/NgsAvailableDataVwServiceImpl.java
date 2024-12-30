package com.novo.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.NgsAvailableDataPageBean;
import com.novo.report.beans.NgsAvailableDataVw;
import com.novo.report.beans.NgsListAuto;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.NgsAvailableDataVwDao;
import com.novo.report.service.NgsAvailableDataVwService;
@Service
public class NgsAvailableDataVwServiceImpl implements NgsAvailableDataVwService {

	@Autowired
	private NgsAvailableDataVwDao ngsAvailableDataVwDao;
	@Override
	public PaginationVO<NgsAvailableDataVw> getNgsAvailableDataVwByPage(NgsAvailableDataPageBean ngsAvailableDataPageBean) {
		PaginationVO<NgsAvailableDataVw> paginationVO = new PaginationVO<NgsAvailableDataVw>();
		paginationVO.setTotal(ngsAvailableDataVwDao.getTotal(ngsAvailableDataPageBean));
		paginationVO.setDataList(ngsAvailableDataVwDao.getNgsAvailableDataVwByPage(ngsAvailableDataPageBean));
		return paginationVO;
	}
	@Override
	public NgsListAuto getSubbarcodeAndProductNameListByPlatform(NgsAvailableDataPageBean ngsAvailableDataPageBean) {
		List<String> productNameList = ngsAvailableDataVwDao.getProductNameListByPlatform(ngsAvailableDataPageBean);
		// 20241230 取消了 subbarcode list
//		List<String> subbarcodeList = ngsAvailableDataVwDao.getSubbarcodeListByPlatform(ngsAvailableDataPageBean);
		List<String> subbarcodeList = new ArrayList<>();
		NgsListAuto ngsListAuto = new NgsListAuto();
		ngsListAuto.setProductNameList(productNameList);
		ngsListAuto.setSubbarcodeList(subbarcodeList);
		return ngsListAuto;
	}

}
