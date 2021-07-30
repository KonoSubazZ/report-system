package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.DriverDao;
import com.novo.report.service.DriverService;
@Service
public class DriverServiceImpl implements DriverService {
	@Autowired
	private DriverDao driverDao;

	@Override
	public PaginationVO<DataFileStatus> getDataFileStatusByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean) {
		PaginationVO<DataFileStatus> paginationVO = new PaginationVO<DataFileStatus>();
		paginationVO.setTotal(driverDao.getTotalByAnalysisDate(dataFileStatusPageBean));
		paginationVO.setDataList(driverDao.getDataFileStatusByAnalysisDate(dataFileStatusPageBean));
		return paginationVO;
	}

	@Override
	public void deleteParseFile(Integer file_id) {
		driverDao.deleteParseFile(file_id);
	}

}
