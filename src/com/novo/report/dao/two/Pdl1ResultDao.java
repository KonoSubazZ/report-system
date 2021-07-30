package com.novo.report.dao.two;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novo.report.beans.Pdl1Result;
import com.novo.report.beans.Pdl1ResultPageBean;
import com.novo.report.beans.Pdl1ResultVw;
@Repository
public interface Pdl1ResultDao {

	Long getTotal(Pdl1ResultPageBean pdl1ResultPageBean);

	List<Pdl1ResultVw> getPdl1ResultByPage(Pdl1ResultPageBean pdl1ResultPageBean);

	void insertPdl1Result(Pdl1Result pr);
	
	void deletePdl1ResultReportId(Integer report_id);
	
}

