package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.Pdl1ResultPageBean;
import com.novo.report.beans.Pdl1ResultVw;
import com.novo.report.dao.two.Pdl1ResultDao;
import com.novo.report.service.Pdl1ResultService;
@Service
public class Pdl1ReultServiceImpl implements Pdl1ResultService {
	
	@Autowired
	private Pdl1ResultDao pdl1ResultDao;
	@Override
	public PaginationVO<Pdl1ResultVw> getPdl1ResultByPage(Pdl1ResultPageBean pdl1ResultPageBean) {
		PaginationVO<Pdl1ResultVw> paginationVO = new PaginationVO<Pdl1ResultVw>();
		paginationVO.setTotal(pdl1ResultDao.getTotal(pdl1ResultPageBean));
		paginationVO.setDataList(pdl1ResultDao.getPdl1ResultByPage(pdl1ResultPageBean));
		return paginationVO;
	}
	@Override
	public void deletePdl1ResultReportId(Integer report_id) {
		pdl1ResultDao.deletePdl1ResultReportId(report_id);
	}

}
