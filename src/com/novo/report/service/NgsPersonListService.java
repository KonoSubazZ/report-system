package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.Person;
import com.novo.report.beans.SampleFile;

public interface NgsPersonListService {

	List<Person> getAllPersonByPage(SampleFile sampleFile);

	Integer isExistPerson(String person_name, String gender);

	void savePerson(Person person);

	void updatePersonId(Integer sample_id, Integer person_id);

	List<Person> getSampleIdAndSubbarcode(Integer person_id);

}
