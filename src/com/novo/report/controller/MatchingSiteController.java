package com.novo.report.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.novo.report.utils.WebserviceProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.Allele;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FalsePositive;
import com.novo.report.beans.FilterIlluminaChemical;
import com.novo.report.beans.FilterIlluminaCnv;
import com.novo.report.beans.FilterIlluminaFusion;
import com.novo.report.beans.FilterIlluminaSnpIndel;
import com.novo.report.beans.FilterLifeChemical;
import com.novo.report.beans.FilterLifeCnv;
import com.novo.report.beans.FilterLifeFusion;
import com.novo.report.beans.FilterLifeSnpIndel;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.ForReport;
import com.novo.report.beans.GeneVariantEvw;
import com.novo.report.beans.MatchingSiteBean;
import com.novo.report.beans.NumberOfMutations;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.User;
import com.novo.report.service.FalsePositiveService;
import com.novo.report.service.FilterChemicalService;
import com.novo.report.service.FilterSnpIndelService;
import com.novo.report.service.GeneVariantEvwService;
import com.novo.report.service.LifeService;
import com.novo.report.service.MatchingSiteService;
import com.novo.report.service.SampleFileService;

@Controller
@RequestMapping("matchingSite")
public class MatchingSiteController {

	@Autowired
	private MatchingSiteService matchingSiteService;

	@Autowired
	private GeneVariantEvwService geneVariantEvwService;

	@Autowired
	private LifeService lifeService;

	@Autowired
	private SampleFileService sampleFileService;
	
	@Autowired
	private FilterSnpIndelService filterSnpIndelService;
	
	@Autowired
	private FilterChemicalService filterChemicalService;
	
	@Autowired
	private FalsePositiveService falsePositiveService;

	@RequestMapping("matchingSite")
	@ResponseBody
	public Boolean matchingSite(FilterPageBean condition,String whatTable,Integer report_id,String user) {
		Boolean flag=true;
		String reg = "^-?[0-9]+(.[0-9]+)?$";
		List<GeneVariantEvw> geneVariantEvwList=new ArrayList<GeneVariantEvw>();
		if("SnpIndel".equals(whatTable)){
			MatchingSiteBean matchingSiteBean = new MatchingSiteBean();
			if("Life".equals(condition.getPlatform())){
				try {
					List<Integer> snpIndelFileIdList = matchingSiteService.getLifeSnpIndelFileId(condition);
					for (Integer snpIndelFileId : snpIndelFileIdList) {
						List<FilterLifeSnpIndel> snpIndelList = matchingSiteService.getLifeSnpIndelListByFileId(snpIndelFileId);
						for (FilterLifeSnpIndel filterLifeSnpIndel : snpIndelList) {
							GeneVariantEvw geneVariantEvw = geneVariantEvwService.getGeneVariantEvw(filterLifeSnpIndel.getGene(),filterLifeSnpIndel.getVariant());
							matchingSiteBean.setAlt(filterLifeSnpIndel.getAlt());
							matchingSiteBean.setChrom(filterLifeSnpIndel.getChr());
							matchingSiteBean.setEnd(filterLifeSnpIndel.getEnd());
							matchingSiteBean.setRef(filterLifeSnpIndel.getRef());
							matchingSiteBean.setStart(filterLifeSnpIndel.getStart());
							matchingSiteBean.setGene_symbol(filterLifeSnpIndel.getGene());
							
							/****************************************************************************************************************************
							 *	--在报告管理界面-筛选位点时，“匹配位点”键也重新自动设定report=1 or 0																	*
							 *	--For snpindel，查找筛选标准																									*
							 *	SELECT a.product_id,c.path_name, a.sample_type, a.stat_param_id, b.param_name, a.operator1, a.value1					*
							 *	FROM omics.stat_param_standard a join omics.stat_parameter b ON b.stat_param_id = a.stat_param_id						*
							 *	join omics.product c ON c.product_id = a.product_id																		*
							 *	where b.category = 'Filtering' and b.param_name = 'allele_frequency_cutoff' and c.path_name = ？ and a.sample_type = ?	*
							 *																															*	
							 *	--path_name is the product_name in report, and sample_type is from sample_file.											*
							 *																															*	
							 *	--for Illumina (snp_indel_file)																							*
							 *	if mutFreq >= value1 (筛选标准), set report =1 , otherwise report =0														*
							 *																															*		
							 *	--for Life (life_snp_indel_file)																						*
							 *	if f_freq  >= value1 (筛选标准), set report =1 , otherwise report =0														*
							 *																															*
							 *	--For cnv，查找筛选标准																										*
							 *	SELECT a.product_id,c.path_name, a.sample_type, a.stat_param_id, b.param_name, a.operator1, a.value1					*
							 *	FROM omics.stat_param_standard a join omics.stat_parameter b ON b.stat_param_id = a.stat_param_id						*
							 *	join omics.product c ON c.product_id = a.product_id																		*
							 *	where b.category = 'Filtering' and b.param_name = 'copy_number_amplification' and c.path_name = ？ and a.sample_type = ?	*
							 *																															*
							 *	--path_name is the product_name in report, and sample_type is from sample_file.											*
							 *																															*
							 *	--for Illumina (cnv_file)																								*
							 *	if copy_number >= value1 (筛选标准), set report =1 , otherwise report =0													*
							 *																															*
							 *	--for Life (life_cnv_file)																								*
							 *	if adjust_cn_value  >= value1 (筛选标准), set report =1 , otherwise report =0												*
							 ****************************************************************************************************************************/
							SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(condition.getSubbarcode());
							ForReport snpIndelForReport = matchingSiteService.getSnpIndelForReport(condition.getProduct_name(),sampleFile.getSample_type());
							if(snpIndelForReport!=null){
								String f_freq = filterLifeSnpIndel.getF_freq()!=null?filterLifeSnpIndel.getF_freq().trim():filterLifeSnpIndel.getF_freq();
								String value1 = snpIndelForReport.getValue1();
								String fdp = filterLifeSnpIndel.getFdp();
								if(f_freq!=null && f_freq.matches(reg)){
									if(Double.parseDouble(f_freq)>=Double.parseDouble(value1)){
										if(fdp!=null && fdp.matches(reg)){
											if(Integer.parseInt(fdp)>=3000){
												filterLifeSnpIndel.setReport("1");
											}else{
												filterLifeSnpIndel.setReport("0");
												filterLifeSnpIndel.setFiltered_rationale("double check; low depth");
											}
										}
									}else{
										filterLifeSnpIndel.setReport("0");
										filterLifeSnpIndel.setFiltered_rationale("Low frequency");
									}
								}
								if("Always-check-out".equals(filterLifeSnpIndel.getRemark()) || "Low-depth".equals(filterLifeSnpIndel.getRemark()) || "Low-frequency".equals(filterLifeSnpIndel.getRemark())){//这里的split[10]指的就是lsif.getRemark(),只因remark在下面赋的值,这里就用split[10]取值
									filterLifeSnpIndel.setFiltered_rationale(filterLifeSnpIndel.getRemark());
									filterLifeSnpIndel.setReport("0");
								}
								if(filterLifeSnpIndel.getOri_variant()!=null && filterLifeSnpIndel.getOri_variant().contains("p.")){
									String splitStr = filterLifeSnpIndel.getOri_variant().split("p.")[1];
									String substringSta = splitStr.substring(0, 1);
									String substringEnd = splitStr.substring(splitStr.length()-1, splitStr.length());
									if(substringSta.equals(substringEnd)){
										filterLifeSnpIndel.setFiltered_rationale("synonymous");
										filterLifeSnpIndel.setReport("0");
									}
								}
							}
							GeneVariantEvw geneVariantEvwAnother = geneVariantEvwService.getGeneVariantEvwByFiveCondition(matchingSiteBean);
							if(geneVariantEvw!=null){
								geneVariantEvwList.add(geneVariantEvw);
								filterLifeSnpIndel.setMapped_variant_id(geneVariantEvw.getGene_variant_id());
								filterLifeSnpIndel.setMapped_variant(geneVariantEvw.getGene_symbol()+" "+geneVariantEvw.getGene_variant());
							}else if(geneVariantEvwAnother!=null){
								filterLifeSnpIndel.setMapped_variant_id(geneVariantEvwAnother.getGene_variant_id());
								filterLifeSnpIndel.setMapped_variant(geneVariantEvwAnother.getGene_symbol()+" "+geneVariantEvwAnother.getGene_variant());
							}
							List<FalsePositive> falsePositiveList = falsePositiveService.getFalsePositiveAll();
							for(FalsePositive falsePositive:falsePositiveList){
								if(falsePositive.getGene().equals(filterLifeSnpIndel.getGene()) && falsePositive.getChrom() != null && falsePositive.getChrom().equals(filterLifeSnpIndel.getChr())
									&&	falsePositive.getStart() != null && falsePositive.getStart().equals(filterLifeSnpIndel.getStart()) && falsePositive.getEnd() != null && falsePositive.getEnd().equals(filterLifeSnpIndel.getEnd())
									&& falsePositive.getAachange().equals(filterLifeSnpIndel.getAachange())){
									filterLifeSnpIndel.setFiltered_rationale("always check-out");
									filterLifeSnpIndel.setReport("0");
								}
							}
							matchingSiteService.updateLifeSnpIndel(filterLifeSnpIndel);
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
			if("Illumina".equals(condition.getPlatform())){
				try {
					List<Integer> snpIndelFileIdList = matchingSiteService.getIlluminaSnpIndelFileId(condition);
					for (Integer snpIndelFileId : snpIndelFileIdList) {
						List<FilterIlluminaSnpIndel> snpIndelList = matchingSiteService.getIlluminaSnpIndelListByFileId(snpIndelFileId);
						for (FilterIlluminaSnpIndel filterIlluminaSnpIndel : snpIndelList) {
							GeneVariantEvw geneVariantEvw = geneVariantEvwService.getGeneVariantEvw(filterIlluminaSnpIndel.getGene_knownGene(),filterIlluminaSnpIndel.getVariant());
							matchingSiteBean.setAlt(filterIlluminaSnpIndel.getAlt());
							matchingSiteBean.setChrom(filterIlluminaSnpIndel.getChr());
							matchingSiteBean.setEnd(filterIlluminaSnpIndel.getEnd());
							matchingSiteBean.setRef(filterIlluminaSnpIndel.getRef());
							matchingSiteBean.setStart(filterIlluminaSnpIndel.getStart());
							matchingSiteBean.setGene_symbol(filterIlluminaSnpIndel.getGene_knownGene());
							
							SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(condition.getSubbarcode());
							ForReport snpIndelForReport = matchingSiteService.getSnpIndelForReport(condition.getProduct_name(),sampleFile.getSample_type());
							if(snpIndelForReport!=null){
								String mutFreq = filterIlluminaSnpIndel.getMutFreq()!=null?filterIlluminaSnpIndel.getMutFreq().trim():filterIlluminaSnpIndel.getMutFreq();
								String value1 = snpIndelForReport.getValue1();
								if(mutFreq!=null && mutFreq.matches(reg) && value1!=null && value1.matches(reg) && Double.parseDouble(mutFreq)>Double.parseDouble(value1)){
									filterSnpIndelService.updateIlluminaReport("1", filterIlluminaSnpIndel.getRecord_id());
								}else{
									filterSnpIndelService.updateIlluminaReport("0", filterIlluminaSnpIndel.getRecord_id());
								}
							}
							GeneVariantEvw geneVariantEvwAnother = geneVariantEvwService.getGeneVariantEvwByFiveCondition(matchingSiteBean);
							if(geneVariantEvw!=null){
								geneVariantEvwList.add(geneVariantEvw);
								filterIlluminaSnpIndel.setMapped_variant_id(geneVariantEvw.getGene_variant_id());
								filterIlluminaSnpIndel.setMapped_variant(geneVariantEvw.getGene_symbol()+" "+geneVariantEvw.getGene_variant());
								matchingSiteService.updateIlluminaSnpIndel(filterIlluminaSnpIndel);
							}else if(geneVariantEvwAnother!=null){
								filterIlluminaSnpIndel.setMapped_variant_id(geneVariantEvwAnother.getGene_variant_id());
								filterIlluminaSnpIndel.setMapped_variant(geneVariantEvwAnother.getGene_symbol()+" "+geneVariantEvwAnother.getGene_variant());
								matchingSiteService.updateIlluminaSnpIndel(filterIlluminaSnpIndel);
							}
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
		}
		if("CNV".equals(whatTable)){
			if("Life".equals(condition.getPlatform())){
				try {
					List<Integer> cnvFileIdList = matchingSiteService.getLifeCnvFileId(condition);
					for (Integer cnvFileId : cnvFileIdList) {
						List<FilterLifeCnv> CnvList = matchingSiteService.getLifeCnvListByFileId(cnvFileId);
						for (FilterLifeCnv filterLifeCnv : CnvList) {
							SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(condition.getSubbarcode());
							List<ForReport> cnvForReportList = matchingSiteService.getLifeCnvForReport(condition.getProduct_name(),sampleFile.getSample_type());
							String adjust_cn_value = filterLifeCnv.getAdjust_cn_value()!=null?filterLifeCnv.getAdjust_cn_value().trim():filterLifeCnv.getAdjust_cn_value();
							Double value1 = null;
							Double value2 = null;
							if(cnvForReportList!=null && cnvForReportList.size()==1){
								String val1Str = cnvForReportList.get(0).getValue1();
								if(val1Str!=null && val1Str.matches(reg)){
									value1 = Double.parseDouble(val1Str);
								}
								if(adjust_cn_value!=null && adjust_cn_value.matches(reg) && value1!=null && Double.parseDouble(adjust_cn_value)>=value1){
									filterLifeCnv.setReport("1");
									filterLifeCnv.setVariant("Amplification");
								}else{
									filterLifeCnv.setReport("0");
								}
							}else if(cnvForReportList!=null && cnvForReportList.size()==2){
								String val1Str = cnvForReportList.get(0).getValue1();
								String val2Str = cnvForReportList.get(1).getValue1();
								if(val1Str!=null && val1Str.matches(reg)){
									value1 = Double.parseDouble(val1Str);
								}
								if(val2Str!=null && val2Str.matches(reg)){
									value2 = Double.parseDouble(val2Str);
								}
								boolean b = false;
								if(value1!=null && value2!=null && value1>value2){ b = true; }
								if(b){
									if(adjust_cn_value!=null && adjust_cn_value.matches(reg) && value1!=null && Double.parseDouble(adjust_cn_value)>=value1){
										filterLifeCnv.setReport("1");
										filterLifeCnv.setVariant("Amplification");
									}else if(adjust_cn_value!=null && adjust_cn_value.matches(reg) && value1!=null && value2!=null && Double.parseDouble(adjust_cn_value)>=value2 && Double.parseDouble(adjust_cn_value)<value1){
										filterLifeCnv.setReport("0");
										filterLifeCnv.setFiltered_rationale("double-check");
									}else{
										filterLifeCnv.setReport("0");
									}
								}else{
									if(adjust_cn_value!=null && adjust_cn_value.matches(reg) && value2!=null && Double.parseDouble(adjust_cn_value)>=value2){
										filterLifeCnv.setReport("1");
										filterLifeCnv.setVariant("Amplification");
									}else if(adjust_cn_value!=null && adjust_cn_value.matches(reg) && value1!=null && value2!=null && Double.parseDouble(adjust_cn_value)>=value1 && Double.parseDouble(adjust_cn_value)<value2){
										filterLifeCnv.setReport("0");
										filterLifeCnv.setFiltered_rationale("double-check");
									}else{
										filterLifeCnv.setReport("0");
									}
								}
							}
							GeneVariantEvw geneVariantEvw = geneVariantEvwService.getGeneVariantEvw(filterLifeCnv.getGene(), filterLifeCnv.getVariant());
							if(geneVariantEvw!=null){
								geneVariantEvwList.add(geneVariantEvw);
								filterLifeCnv.setMapped_variant_id(geneVariantEvw.getGene_variant_id());
								filterLifeCnv.setMapped_variant(geneVariantEvw.getGene_symbol()+" "+geneVariantEvw.getGene_variant());
							}
							matchingSiteService.updateLifeCnv(filterLifeCnv);
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
			if("Illumina".equals(condition.getPlatform())){
				try {
					List<Integer> cnvFileIdList = matchingSiteService.getIlluminaCnvFileId(condition);
					for (Integer cnvFileId : cnvFileIdList) {
						List<FilterIlluminaCnv> CnvList = matchingSiteService.getIlluminaCnvListByFileId(cnvFileId);
						for (FilterIlluminaCnv filterIlluminaCnv : CnvList) {
							
							SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(condition.getSubbarcode());
							ForReport cnvForReport = matchingSiteService.getIlluminaCnvForReport(condition.getProduct_name(),sampleFile.getSample_type());
							if(cnvForReport!=null){
								String copy_number = filterIlluminaCnv.getCopy_number()!=null?filterIlluminaCnv.getCopy_number().trim():filterIlluminaCnv.getCopy_number();
								String value1 = cnvForReport.getValue1();
								if(copy_number!=null && value1!=null && Double.parseDouble(copy_number)>Double.parseDouble(value1)){
									filterIlluminaCnv.setReport("1");
									filterIlluminaCnv.setVariant("Amplification");
								}else{
									filterIlluminaCnv.setReport("0");
								}
							}
							GeneVariantEvw geneVariantEvw = geneVariantEvwService.getGeneVariantEvw(filterIlluminaCnv.getGene(), filterIlluminaCnv.getVariant());
							if(geneVariantEvw!=null){
								geneVariantEvwList.add(geneVariantEvw);
								filterIlluminaCnv.setMapped_variant_id(geneVariantEvw.getGene_variant_id());
								filterIlluminaCnv.setMapped_variant(geneVariantEvw.getGene_symbol()+" "+geneVariantEvw.getGene_variant());
							}
							matchingSiteService.updateIlluminaCnv(filterIlluminaCnv);
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
		}
		if("Fusion".equals(whatTable)){
			if("Life".equals(condition.getPlatform())){
				try {
					List<Integer> fusionFileIdList = matchingSiteService.getLifeFusionFileId(condition);
					for (Integer fusionFileId : fusionFileIdList) {
						List<FilterLifeFusion> fusionList = matchingSiteService.getLifeFusionListByFileId(fusionFileId);
						for (FilterLifeFusion filterLifeFusion : fusionList) {
							GeneVariantEvw geneVariantEvw = geneVariantEvwService.getGeneVariantEvw(filterLifeFusion.getGene(),filterLifeFusion.getVariant());
							if(geneVariantEvw!=null){
								geneVariantEvwList.add(geneVariantEvw);
								filterLifeFusion.setMapped_variant_id(geneVariantEvw.getGene_variant_id());
								filterLifeFusion.setMapped_variant(geneVariantEvw.getGene_symbol()+" "+geneVariantEvw.getGene_variant());
							}
							String ndf = filterLifeFusion.getNdf();
							if(ndf!=null){
								ndf=ndf.trim();
								if(ndf.matches(reg)){
									if("PASS".equals(filterLifeFusion.getFilter()) && Double.parseDouble(ndf)>=-2.8){
										filterLifeFusion.setReport("1");
									}else{
										filterLifeFusion.setReport("0");
									}
								}else {
									if("PASS".equals(filterLifeFusion.getFilter())){
										filterLifeFusion.setReport("1");
									}else{
										filterLifeFusion.setReport("0");
									}
								}
							}else{
								if("PASS".equals(filterLifeFusion.getFilter())){
									filterLifeFusion.setReport("1");
								}else{
									filterLifeFusion.setReport("0");
								}
							}
							matchingSiteService.updateLifeFusion(filterLifeFusion);
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
			if("Illumina".equals(condition.getPlatform())){
				try {
					List<Integer> fusionFileIdList = matchingSiteService.getIlluminaFusionFileId(condition);
					for (Integer fusionFileId : fusionFileIdList) {
						List<FilterIlluminaFusion> fusionList = matchingSiteService.getIlluminaFusionListByFileId(fusionFileId);
						for (FilterIlluminaFusion filterIlluminaFusion : fusionList) {
							GeneVariantEvw geneVariantEvw = geneVariantEvwService.getGeneVariantEvw(filterIlluminaFusion.getGene1(),filterIlluminaFusion.getVariant());
							if(geneVariantEvw!=null){
								geneVariantEvwList.add(geneVariantEvw);
								filterIlluminaFusion.setMapped_variant_id(geneVariantEvw.getGene_variant_id());
								filterIlluminaFusion.setMapped_variant(geneVariantEvw.getGene_symbol()+" "+geneVariantEvw.getGene_variant());
								filterIlluminaFusion.setReport("1");
								matchingSiteService.updateIlluminaFusion(filterIlluminaFusion);
							}
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
			
		}
		if("Chemical".equals(whatTable)){
			List<Allele> alleleList = new ArrayList<Allele>();
			if("Life".equals(condition.getPlatform())){
				try {
					List<Integer> chemicalFileIdList = matchingSiteService.getLifeChemicalFileId(condition);
					for (Integer chemicalFileId : chemicalFileIdList) {
						List<FilterLifeChemical> chemicalList = matchingSiteService.getLifeChemicalListByFileId(chemicalFileId);
						for (FilterLifeChemical filterLifeChemical : chemicalList) {
							filterChemicalService.updateLifeReport("1", filterLifeChemical.getRecord_id());
							Allele allele = geneVariantEvwService.getAllele(filterLifeChemical.getGenotype(), filterLifeChemical.getRs());
							if(allele==null && filterLifeChemical.getGenotype()!=null && !"".equals(filterLifeChemical.getGenotype()) && filterLifeChemical.getGenotype().contains("/")){
								if(filterLifeChemical.getGenotype().contains("查看原始结果")){
									filterLifeChemical.setGenotype(filterLifeChemical.getGenotype().split("\\(")[0]);
								}
								String[] split = filterLifeChemical.getGenotype().trim().split("/");
								allele = geneVariantEvwService.getAllele(split[1]+"/"+split[0], filterLifeChemical.getRs());
							}
							if(allele!=null){
								alleleList.add(allele);
								filterLifeChemical.setMapped_allele_id(allele.getAllele_id());
								matchingSiteService.updateLifeChemical(filterLifeChemical);
							}
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
			
			if("Illumina".equals(condition.getPlatform())){
				try {
					List<Integer> chemicalFileIdList = matchingSiteService.getIlluminaChemicalFileId(condition);
					for (Integer chemicalFileId : chemicalFileIdList) {
						List<FilterIlluminaChemical> chemicalList = matchingSiteService.getIlluminaChemicalListByFileId(chemicalFileId);
						for (FilterIlluminaChemical filterIlluminaChemical : chemicalList) {
							filterChemicalService.updateIlluminaReport("1", filterIlluminaChemical.getRecord_id());
							Allele allele = geneVariantEvwService.getAllele(filterIlluminaChemical.getGenotype(), filterIlluminaChemical.getRs_id());
							if(allele==null && filterIlluminaChemical.getGenotype()!=null && !"".equals(filterIlluminaChemical.getGenotype()) && filterIlluminaChemical.getGenotype().contains("/")){
								if(filterIlluminaChemical.getGenotype().contains("查看原始结果")){
									filterIlluminaChemical.setGenotype(filterIlluminaChemical.getGenotype().split("\\(")[0]);
								}
								String[] split = filterIlluminaChemical.getGenotype().trim().split("/");
								allele = geneVariantEvwService.getAllele(split[1]+"/"+split[0], filterIlluminaChemical.getRs_id());
							}
							if(allele!=null){
								alleleList.add(allele);
								filterIlluminaChemical.setMapped_allele_id(allele.getAllele_id());
								matchingSiteService.updateIlluminaChemical(filterIlluminaChemical);
							}
						}
						flag = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
		}
		String status = lifeService.getStatus(report_id);
		if(status!=null && !status.contains("报告审核通过") && !status.contains("报告发送成功")){
			status="初次看点";
		}else {
			flag = false; 
		}
		AnalysisReport analysisReport = new AnalysisReport();
		if("初次看点".equals(status)){
			analysisReport.setReport_id(report_id);
			analysisReport.setStatus(status);
			lifeService.editStatus(analysisReport);
		}
		return flag;
	}
	
	@RequestMapping("auditing")
	@ResponseBody
	public Boolean auditing(AnalysisReport analysisReport,HttpServletRequest httpServletRequest) {
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		String bioinfo_checker = user==null ? "" : user.getUser_account();
		analysisReport.setBioinfo_checker(bioinfo_checker);
		try {
			String status = lifeService.getStatus(analysisReport.getReport_id());
			if(status==null || !status.contains("报告审核通过") && !status.contains("报告发送成功")){
				status="生信审核";
			}else {
				return false;
			}
			if("生信审核".equals(status)){
				analysisReport.setStatus(status);
				lifeService.editStatus(analysisReport);
				if (analysisReport.getFlag() != null && analysisReport.getFlag() == 1) {
					WebserviceProxyUtils.status(analysisReport.getSubbarcode(), "product_status", status);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping("saveMutationsNum")
	@ResponseBody
	public boolean saveMutationsNum(NumberOfMutations numberOfMutations) {
		if(numberOfMutations.getChemical_all() != null || numberOfMutations.getCNV() != null || numberOfMutations.getCR_ALL() != null || numberOfMutations.getFusion() != null || numberOfMutations.getIndel() != null || numberOfMutations.getSNP() != null) {
			if(numberOfMutations.getSNP() != null && numberOfMutations.getIndel() != null) {
				sampleFileService.saveMutationsNum2(numberOfMutations.getSNP(), numberOfMutations.getSubbarcode(), numberOfMutations.getAnalysis_date(), "SNP");
				sampleFileService.saveMutationsNum2(numberOfMutations.getIndel(), numberOfMutations.getSubbarcode(), numberOfMutations.getAnalysis_date(), "Indel");
			}else {
				sampleFileService.saveMutationsNum(numberOfMutations);
			}
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否为double类型
	 * @param str
	 * @return
	 */
	/*private boolean pandun(String str){
        boolean ret = true;
        try{
            Double.parseDouble(str);
            ret = true;
        }catch(Exception ex){
            ret = false;
        }
        return ret;
    }*/
	
}
