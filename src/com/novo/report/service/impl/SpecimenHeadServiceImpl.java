package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.SpecimenHead;
import com.novo.report.dao.one.SpecimenHeadDao;
import com.novo.report.service.SpecimenHeadService;
@Service
public class SpecimenHeadServiceImpl implements SpecimenHeadService {
	
	@Autowired
	private SpecimenHeadDao specimenHeadDao;
	//获取SpecimenHeadList
	@Override
	public List<SpecimenHead> getSpecimenHeadList() {
		List<SpecimenHead> list = specimenHeadDao.getSpecimenHeadList();
		return list;
	}
	@Override
	public SpecimenHead getSpecimenHeadBySubbarcode(String subbarcode) {
		return specimenHeadDao.getSpecimenHeadBySubbarcode(subbarcode);
	}
	//查询条数 加载oracle
	@Override
	public Integer count() {
		return specimenHeadDao.count();
	}
	@Override
	public List<SpecimenHead> getSpecimenHeadByBarcode(String barcode) {
		return specimenHeadDao.getSpecimenHeadByBarcode(barcode);
	}

}
