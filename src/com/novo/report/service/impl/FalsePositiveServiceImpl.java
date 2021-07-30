package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.FalsePositive;
import com.novo.report.beans.FalsePositivePageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.FalsePositiveDao;
import com.novo.report.service.FalsePositiveService;
@Service
public class FalsePositiveServiceImpl implements FalsePositiveService {
	
	@Autowired
	private FalsePositiveDao falsePositiveDao;

	@Override
	public PaginationVO<FalsePositive> getfalsePositiveByPage(FalsePositivePageBean falsePositivePageBean) {
		PaginationVO<FalsePositive> paginationVO = new PaginationVO<FalsePositive>();
		paginationVO.setTotal(falsePositiveDao.getfalsePositiveTotal(falsePositivePageBean));
		paginationVO.setDataList(falsePositiveDao.getfalsePositiveByPage(falsePositivePageBean));
		return paginationVO;
	}

	@Override
	public void saveFalsePositive(FalsePositive falsePositive) {
		falsePositiveDao.saveFalsePositive(falsePositive);
		
	}

	@Override
	public void deletefalsePositive(Integer id) {
		falsePositiveDao.deletefalsePositive(id);
	}

	@Override
	public FalsePositive getFalsePositiveById(int id) {
		return falsePositiveDao.getFalsePositiveById(id);
	}

	@Override
	public void updateFalsePositive(FalsePositive falsePositive) {
		falsePositiveDao.updateFalsePositive(falsePositive);
	}

	@Override
	public List<FalsePositive> getFalsePositiveAll() {
		return falsePositiveDao.getFalsePositiveAll();
	}
	
}
