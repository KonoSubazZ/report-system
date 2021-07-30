package com.novo.report.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.NameAndDateBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.PcrResultPageBean;
import com.novo.report.beans.PcrResultVw;
import com.novo.report.beans.PcrResultVwBean;

public interface PcrResultService {

	PaginationVO<PcrResultVw> getpcrResultByPage(PcrResultPageBean pcrResultPageBean);

	List<String> getGeneSymbolListByVw();

	List<String> getVariantListByVw();

	List<NameAndDateBean> getNameAndData(PcrResultVwBean pcrResultVwBean);

	Object exportPcrFile(PcrResultPageBean pcrResultPageBean, HttpSession session);

	void downloadPcrFile(HttpServletResponse response, HttpServletRequest request, HttpSession session);

}
