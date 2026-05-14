/*
 Navicat Premium Dump SQL

 Source Server         : test-172.20.1.34
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26-log)
 Source Host           : 172.20.1.34:8806
 Source Schema         : omics

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26-log)
 File Encoding         : 65001

 Date: 12/05/2026 13:50:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for template_conf
-- ----------------------------
DROP TABLE IF EXISTS `template_conf`;
CREATE TABLE `template_conf`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) NOT NULL DEFAULT 0,
  `template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `report_product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '报告模版内页名字\r\n',
  `report_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sample_info` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '样本信息',
  `basic_info` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'CR106基本信息',
  `product_desc` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测项目',
  `test_result_summary` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测结果小结',
  `important_targeted_gene_summary` tinyint(4) NOT NULL DEFAULT 0 COMMENT '重要靶向用药相关基因结果汇总',
  `cancer_risk_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'CR106检测癌症风险提示',
  `total_risk_assessment` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'CR106总体风险评估',
  `thyroid_cancer_hot_spot_result` tinyint(4) NOT NULL DEFAULT 0 COMMENT '甲状腺癌热点基因检测结果-不是通用模块',
  `hrd_state_score_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '同源重组缺陷状态提示-BRCA1/2基因状态(组织配对)',
  `hrd_state_brac12_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '同源重组缺陷状态提示-基因组不稳定性评分(组织配对)',
  `hrd_state_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '同源重组缺陷状态提示',
  `sarcoma_typing` tinyint(4) NOT NULL DEFAULT 0 COMMENT '肉瘤辅助诊断',
  `sarcoma_typing1` tinyint(4) NOT NULL DEFAULT 0 COMMENT '辅助涎腺肿瘤诊断',
  `endometrial_carcinoma_typing` tinyint(4) NOT NULL DEFAULT 0 COMMENT '子宫内膜癌分子分型',
  `iymphoma_typing` tinyint(4) NOT NULL DEFAULT 0 COMMENT '淋巴瘤-辅助分型提示',
  `iymphoma_prognosis` tinyint(4) NOT NULL DEFAULT 0 COMMENT '淋巴瘤-疾病预后提示',
  `brain_glioma` tinyint(4) NOT NULL DEFAULT 0 COMMENT '脑胶质瘤分子标记物',
  `brain_glioma_1166` int(11) NOT NULL DEFAULT 0 COMMENT '脑胶质瘤分子分型模块-1166RNA实体瘤报告',
  `midline_cancer` tinyint(4) NOT NULL DEFAULT 0 COMMENT '中线癌分型',
  `kidney_cancer` tinyint(4) NOT NULL DEFAULT 0 COMMENT '肾癌分型',
  `thyroid_cancer_prognosis` tinyint(4) NOT NULL DEFAULT 0 COMMENT '甲状腺癌预后评估',
  `variant_list` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'CR106变异列表',
  `somatic_drug_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '靶向药物用药提示',
  `cr_drug_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '变异位点结果提示',
  `somatic_mutation_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '体细胞变异分级提示',
  `cr_mutation_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '胚系变异结果提示',
  `somatic_mutation_DR_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '体系变异结果提示-特供儿童肿瘤D+R产品',
  `cr_mutation_DR_tip` tinyint(4) NOT NULL DEFAULT 0 COMMENT '胚系变异结果提示-特供儿童肿瘤D+R产品',
  `approved_targeted_drug_FDA_NMPA` tinyint(4) NOT NULL DEFAULT 0 COMMENT '本癌种FDA/NMPA获批的其他可选靶向药物',
  `endocrine_therapy` tinyint(4) NOT NULL DEFAULT 0 COMMENT '内分泌相关（仅针对泌尿肿瘤中的前列腺癌）',
  `TMB` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫-TMB',
  `MSI` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫-MSI',
  `MMR` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫-MMR',
  `Immunity_P_N` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫正/负向',
  `HPD` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫超进展HPD',
  `HLA_LON` tinyint(4) NOT NULL DEFAULT 0 COMMENT '人类白细胞抗原杂合性缺失(HLA-LOH)-WES plus',
  `HLA_LON_1` tinyint(4) NOT NULL DEFAULT 0 COMMENT '人类白细胞抗原杂合性缺失(HLA-LOH)-基智远1238/苏州市立医院1238&550&550+596',
  `tumor_neoantigen` tinyint(4) NOT NULL DEFAULT 0 COMMENT '肿瘤新抗原-WES+WES plus',
  `PD` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'PD-L1',
  `approved_immunity_drug` tinyint(4) NOT NULL DEFAULT 0 COMMENT '各癌种已批准的免疫治疗药物（静态大表）-目前肺癌已批准的免疫治疗药物(【对于肺癌产品（含免疫的）输出】',
  `hrr` tinyint(4) NOT NULL COMMENT '同源重组修复（HRR）基因检测',
  `cancer_risk_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'CR106风险管理',
  `hrd_state_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '同源重组缺陷状态解析',
  `thyroid_iymphoma_drug_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '甲状腺癌产品中为预后评估结果解析',
  `variant_mutation_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '单样本体系胚系-变异位点结果解析 / 检测结果解析',
  `somatic_mutation_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '双样本-体细胞变异结果解析',
  `cr_mutation_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '双样本-胚系变异结果解析',
  `variant_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '变异解析',
  `mrd` tinyint(4) NOT NULL COMMENT 'MRD历史检测汇总',
  `TMB_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'TMB 解析',
  `MSI_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'MSI 解析',
  `MMR_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'MMR 解析',
  `Immunity_P_N_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫正/负向解析',
  `HPD_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '免疫超进展相关基因检测结果解析',
  `HLA_LOH_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '人类白细胞抗原杂合性缺失(HLA-LOH)解析-WES plus',
  `tumor_neoantigen_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '肿瘤新抗原解析-WES+WES plus',
  `prognostic_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '预后相关基因检测结果及解析',
  `melanoma_prognosis` tinyint(4) NOT NULL COMMENT '黑色瘤预后',
  `chemo_anal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '化疗药物检测解析',
  `WES` tinyint(4) NOT NULL DEFAULT 0 COMMENT '全外显子基因突变结果-WES /WES Plus',
  `QC` tinyint(4) NOT NULL DEFAULT 0 COMMENT '样本质控情况',
  `tumor_gene_significance` int(11) NOT NULL DEFAULT 0 COMMENT '主要肿瘤及覆盖基因的意义',
  `sarcoma_gene` tinyint(4) NOT NULL DEFAULT 0 COMMENT '常见肉瘤分型相关基因变异（四个大表）',
  `brain_glioma_gene` tinyint(4) NOT NULL DEFAULT 0 COMMENT '常见胶质瘤分型相关基因变异',
  `midline_cancer_significance` tinyint(4) NOT NULL DEFAULT 0 COMMENT '中线癌相关分子标记物检测意义',
  `rcc_features` tinyint(4) NOT NULL DEFAULT 0 COMMENT '分子定义的肾细胞癌的形态学和分子特征',
  `salivary_gland_tumor_gene` tinyint(4) NOT NULL DEFAULT 0 COMMENT '常见涎腺肿瘤分型相关基因变异',
  `endometrial_prognosis` tinyint(4) NOT NULL DEFAULT 0 COMMENT '子宫内膜癌分子分型与预后的相关性',
  `HRR45_BRCA45_HRD_genebg_intro` tinyint(4) NOT NULL DEFAULT 0 COMMENT 'BRCA1&BRCA2基因背景介绍',
  `HRR45_BRCA45_gene_intro` int(11) NOT NULL DEFAULT 0,
  `HRR45_BRCA45_HRD_people_feat` tinyint(4) NOT NULL DEFAULT 0 COMMENT '易感人群特征',
  `gene_instability_intro_HRD` tinyint(4) NOT NULL DEFAULT 0 COMMENT '基因组不稳定性介绍',
  `hrd_and_tumor_therapy` tinyint(4) NOT NULL DEFAULT 0 COMMENT '同源重组缺陷（HRD）与肿瘤的精准治疗',
  `common_drug_gene_list` tinyint(4) NOT NULL DEFAULT 0 COMMENT '常见靶向药物相关基因基因列表',
  `gene_intro` tinyint(4) NOT NULL DEFAULT 0 COMMENT '癌症相关的基因背景介绍',
  `cancer_bg_intro` int(11) NOT NULL DEFAULT 0,
  `cancer_improtant_gene` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测基因列表【注：WES Plus该模块是：癌症相关重要基因列表】',
  `test_cancer_gene_panel` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测癌种和基因列表',
  `cancer_gene_pathogenicity` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测癌种及对应基因部分致病/可能致病突变示例',
  `method_limit` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测方法与局限性',
  `test_method` tinyint(4) NOT NULL DEFAULT 0 COMMENT '检测方法',
  `genetic_interpretation` tinyint(4) NOT NULL DEFAULT 0 COMMENT '遗传信息解读',
  `variant_interpretation_statement` tinyint(4) NOT NULL DEFAULT 0 COMMENT '变异解读声明',
  `variant_test_summary` tinyint(4) NOT NULL DEFAULT 0 COMMENT '变异检测总表',
  `test_flow` int(11) NOT NULL DEFAULT 0 COMMENT '检测流程',
  `reference` tinyint(4) NOT NULL DEFAULT 0 COMMENT '参考文献',
  `signal_pathway` int(11) NOT NULL DEFAULT 0 COMMENT '信号通路',
  `screening_methods_excluding_genetic` tinyint(4) NOT NULL DEFAULT 0 COMMENT '除基因检测外常见肿瘤筛查方式',
  `glioma_marker_significance` tinyint(4) NOT NULL DEFAULT 0 COMMENT '脑胶质瘤相关分子标记物检测意义',
  `glioma_genetic_risk_gene` tinyint(4) NOT NULL DEFAULT 0 COMMENT '胶质瘤遗传风险相关基因',
  `faq` tinyint(4) NOT NULL DEFAULT 0 COMMENT '常见问题',
  `has_subreport` tinyint(4) NOT NULL COMMENT '是否出小报告',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除，0为未删除，1为已删除',
  `creator` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updater` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_templatename`(`template_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 309 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '1.  化疗药物检测 胃肠道间质瘤 gastrointestinalStromalTumor==>false,其余为 true\r\n2.  PD-L1蛋白表达水平 PDInfo 存在就展示\r\n3.  免疫用药检测结果: 3条规则 msi tmb mmr=>immunity1, msi mmr =>immunity2, msi =>immunity3' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
