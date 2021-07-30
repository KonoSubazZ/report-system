package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PcrTemplate;
import com.novo.report.dao.two.PcrTemplateDao;
import com.novo.report.service.PcrTemplateService;

@Service
public class PcrTemplateServiceImpl implements PcrTemplateService {
	
	@Autowired
	private PcrTemplateDao pcrTemplatedao;
	

	@Override
	public PcrTemplate queryTemplateNameById(String template_id) {
		return pcrTemplatedao.selectTemplateNameById(template_id);
	}

}
