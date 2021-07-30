package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.QualityStatFileVw;
import com.novo.report.beans.QualityStatFileVwPageBean;
import com.novo.report.dao.two.QualityStatFileVwDao;
import com.novo.report.service.QualityStatFileVwService;
@Service
public class QualityStatFileVwServiceImpl implements QualityStatFileVwService {
	@Autowired
	private QualityStatFileVwDao qualityStatFileVwDao;
	@Override
	public PaginationVO<QualityStatFileVw> getQualityStatFileVwByPage( QualityStatFileVwPageBean qualityStatFileVwPageBean) {
		PaginationVO<QualityStatFileVw> paginationVO = new PaginationVO<QualityStatFileVw>();
		paginationVO.setTotal(qualityStatFileVwDao.getTotal(qualityStatFileVwPageBean));
		paginationVO.setDataList(qualityStatFileVwDao.getPage(qualityStatFileVwPageBean));
		return paginationVO;
	}
	@Override
	public List<String> getDataType() {
		return qualityStatFileVwDao.getDataType();
	}

}
