package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.ExportNgsQueryFile;
import com.novo.report.beans.IntegratedMutationFileVw;
import com.novo.report.beans.NgsIntegratedMutationFilePageBean;

public interface NgsIntegratedMutationFileDao {
	Long getTotal(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean);
	List<IntegratedMutationFileVw> getNgsIntegratedMutationFileListByPage(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean);
	List<ExportNgsQueryFile> exportNgsFile(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean);
}
