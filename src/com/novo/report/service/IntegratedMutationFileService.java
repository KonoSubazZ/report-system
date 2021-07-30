package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.CondationIntegratedMutationFile;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.NgsCountList;

public interface IntegratedMutationFileService {

	IntegratedMutationFileList getIntegratedMutationList();

	List<List<NgsCountList>> getNgsCountList(CondationIntegratedMutationFile condation);

}
