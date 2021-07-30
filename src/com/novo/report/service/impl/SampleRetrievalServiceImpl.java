package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.SampleRetrieval;
import com.novo.report.dao.two.SampleRetrievalDao;
import com.novo.report.service.SampleRetrievalService;

@Service
public class SampleRetrievalServiceImpl implements SampleRetrievalService {
	
	@Autowired
	private SampleRetrievalDao sampleRetrievalDao;

	@Override
	public List<SampleRetrieval> getsampleRetrievalList(SampleRetrieval sampleRetrieval) {
		if(!sampleRetrieval.getVariant().equals("") && sampleRetrieval.getVariant().indexOf("Fusion") != -1) {
			return sampleRetrievalDao.getsampleRetrievalList2(sampleRetrieval);
		}else {
			return sampleRetrievalDao.getsampleRetrievalList(sampleRetrieval);
		}
	}
}
