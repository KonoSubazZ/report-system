package com.novo.report.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.IntegratedMutationFileVw;
import com.novo.report.beans.NgsIntegratedMutationFilePageBean;
import com.novo.report.beans.PaginationVO;

public interface NgsIntegratedMutationFileService {
	PaginationVO<IntegratedMutationFileVw> getNgsIntegratedMutationFileListByPage(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean);

	Object exportNgsFile(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean, HttpSession session);

	void downloadNgsFile(HttpServletResponse response, HttpServletRequest request,HttpSession session);
}
