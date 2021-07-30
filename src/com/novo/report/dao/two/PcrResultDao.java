package com.novo.report.dao.two;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novo.report.beans.ExportPcrResultVw;
import com.novo.report.beans.NameAndDateBean;
import com.novo.report.beans.PcrResult;
import com.novo.report.beans.PcrResultPageBean;
import com.novo.report.beans.PcrResultVw;
import com.novo.report.beans.PcrResultVwBean;
@Repository
public interface PcrResultDao {

	void addResult(PcrResult pcrResult);

	Long getTotal(PcrResultPageBean pcrResultPageBean);

	List<PcrResultVw> getpcrResultByPage(PcrResultPageBean pcrResultPageBean);

	List<String> getGeneSymbolListByVw();

	List<String> getVariantListByVw();

	List<NameAndDateBean> getSpecimenTypeAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getCategoryAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getCancertypeAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getClinicalremarkAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getPathologicaltypeAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getMutationFrequencyAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getGeneAndCount(PcrResultVwBean pcrResultVwBean);

	List<NameAndDateBean> getVariantAndCount(PcrResultVwBean pcrResultVwBean);

	List<ExportPcrResultVw> exportPcrFile(PcrResultPageBean pcrResultPageBean);

	List<String> getGeneSymbolList(Integer test_id);

	List<String> getVariantListByGene(String gene_symbol);

}
