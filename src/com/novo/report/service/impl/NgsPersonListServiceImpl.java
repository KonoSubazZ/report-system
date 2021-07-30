package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.Person;
import com.novo.report.beans.SampleFile;
import com.novo.report.dao.two.NgsPersonListDao;
import com.novo.report.service.NgsPersonListService;

@Service
public class NgsPersonListServiceImpl implements NgsPersonListService {
	@Autowired
	private NgsPersonListDao ngsPersonListDao;
	@Override
	public List<Person> getAllPersonByPage(SampleFile sampleFile) {
		return ngsPersonListDao.getAllPersonByPage(sampleFile);
		
	}
	@Override
	public Integer isExistPerson(String person_name, String gender) {
		return ngsPersonListDao.isExistPerson(person_name,gender);
	}
	@Override
	public void savePerson(Person person) {
		ngsPersonListDao.savePerson(person);
	}
	@Override
	public void updatePersonId(Integer sample_id, Integer person_id) {
		ngsPersonListDao.updatePersonId(sample_id,person_id);
	}
	@Override
	public List<Person> getSampleIdAndSubbarcode(Integer person_id) {
		return ngsPersonListDao.getSampleIdAndSubbarcode(person_id);
	}

}
