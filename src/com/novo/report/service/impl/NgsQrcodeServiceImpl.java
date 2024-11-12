package com.novo.report.service.impl;

import com.novo.report.beans.NgsQrcode;
import com.novo.report.dao.four.NgsQrcodeDao;
import com.novo.report.service.NgsQrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NgsQrcodeServiceImpl implements NgsQrcodeService {
	
	@Autowired
	private NgsQrcodeDao ngsQrcodeDao;

	@Override
	public void insertNgsQrcode(NgsQrcode ngsQrcode) {
		ngsQrcodeDao.insertNgsQrcode(ngsQrcode);
	}

}
