import json
import re
import pymysql
# from datetime import datetime

def process_custom_data(report_json):
    template_name = report_json.get('summaryOfRresults').get('template_name')

    # 上海肺科
    if template_name == '肺癌60基因报告-上海肺科' or template_name == '实体瘤188基因报告-上海肺科':
        process_shanghaifeike_tip(report_json)

    # 山肿胚系
    if template_name == '实体瘤1238DNA+1166RNA基因检测报告-山肿':
        process_shanzhong_tip(report_json)

    # 齐鲁 DNA/RNA
    if template_name == '泛实体瘤1238+1166基因检测报告-齐鲁':
        process_qilu_tip(report_json)

    # 同济 D+R
    if template_name == '泛实体瘤1238+1166基因报告-同济':
        process_tongji_tip(report_json)

    # 广附一 EGFR20插入
    if template_name == '非小细胞肺癌-报告模板-广附一' or template_name == '非小细胞肺癌-报告模板-广附一（简化版）':
        process_guangfuyi_tip(report_json)

    # 安徽胸科54+6 增加 I 类MET展示
    if template_name == '实体瘤54+6基因报告-安徽胸科':
        process_anhuixiongke_tip(report_json)

    # WJM
    # 20260514增加需求-通用panel的D/R分开展示

    # conf = report_json.get('reportInfo').get('conf', {})
    # sarcoma_typing = conf.get('sarcomaTyping', False)
    # brain_glioma_1166 = conf.get('brainGlioma', False)
    # midline_cancer = conf.get('midlineCancer', False)
    # kidney_cancer = conf.get('kidneyCancer', False)
    panel = report_json.get("panel")
    DR_panel = ["novopm2_tis_1238_1166", "novopm2_tis_550_596", "novopm2_tis_169_596", "novopm2_tis1_169_596", "novopm2_tis1_108_33", "novopm2_tis1_58_22", "novopm2_tis1_1238_1166","novopm2_tis_1249_1166"]
    to_update_json_data = {}
    is_DR_panel = panel in DR_panel
    # log("is_DR_panel: %s" % is_DR_panel)
    report_json['is_DR_panel'] = is_DR_panel

    if (template_name == '肉瘤1238+1166基因检测报告-WJM' or template_name == '肉瘤550+596基因检测报告-WJM'\
            or template_name == '泛实体瘤1238+1166基因检测报告-WJM' or template_name == '泛实体瘤550+596基因检测报告-WJM'\
            or template_name == '泛实体瘤108+33基因检测报告-单样本-WJM' or template_name == '泛实体瘤58+22基因检测报告-单样本-WJM'\
            or template_name == '泛实体瘤1238+1166基因报告-儿童肿瘤' or template_name == '肉瘤1238+1166基因报告-儿童肿瘤' or
            template_name == '肉瘤1238+1166基因报告' or template_name == '肉瘤1238+1166基因检测报告' or is_DR_panel):
        to_update_json_data['is_DR_panel'] = is_DR_panel
        process_WJM_tip(report_json, to_update_json_data)

    # 浙江省人民医院
    if template_name == '泛实体瘤1238基因检测报告-浙江省人民医院' or template_name == '泛实体瘤299基因检测报告-浙江省人民医院':
        process_ZHSRRYY_tip(report_json)

    # 齐鲁增加白系统对照QC
    if (template_name == "子宫内膜癌检测报告-齐鲁" or template_name == "泛实体瘤1238+1166基因检测报告-齐鲁"
            or template_name == "BRCA12基因检测报告-双样本-齐鲁") or template_name == "HRD+HRR检测报告-齐鲁":
        process_QL_QC_info(report_json)

def process_shanghaifeike_tip(report_json):
    shanghaifeike_tips_1 = []
    shanghaifeike_tips_2 = []
    shanghaifeike_tips_3 = []

    # ========== 核心基因检测 EGFR ALK ROS1 KRAS BRAF PIK3CA ==========
    shanghaifeike_hot_gene_list = ["EGFR", "ALK", "ROS1", "KRAS", "BRAF", "PIK3CA"]
    shanghaifeike_hot_gene_info = []

    # ========== CNV拷贝数缺失检测 MTAP CDKN2A CDKN2B ==========
    cnv_loss_target_genes = ["MTAP", "CDKN2A", "CDKN2B"]
    loss_detected_genes = set()

    subbarcode = report_json.get('subbarcode')
    analysis_date = report_json.get('analysisDate')
    product_name = report_json.get('panel')
    patient_id = report_json.get('barcode')
    hyphen_index = patient_id.find('-')
    if hyphen_index != -1:
        shanghaifeike_id = patient_id[hyphen_index + 1:]
        report_json['shanghaifeike_id'] = shanghaifeike_id
    else:
        report_json['shanghaifeike_id'] = patient_id

    for item in report_json.get('complexDrugTipLineStr', []):
        comutation = item.get('comutation')
        tip = comutation
        shanghaifeike_tips_1.append(tip)

    for item in report_json.get('bodyDrugTipLineStr', []):
        gene = item.get('gene')
        ori_variant = item.get('ori_variant')
        mut_freq = item.get('mutFreq')
        variationClass2 = item.get('variationClass2')
        # variationClass = item.get('variationClass')
        variant_split = ori_variant.split(" ")

        # CNV拷贝数缺失检测
        if gene in cnv_loss_target_genes and ori_variant and "loss" in ori_variant.lower():
            loss_detected_genes.add(gene)

        tip = ""
        if "Fusion" in ori_variant:
            # today = datetime.today()
            # today_formatted = today.strftime("%Y%m%d")
            fusion_reads_list = mysql_query(product_name, analysis_date , subbarcode)

            gene_str = variant_split[0]
            fusion_flag = variant_split[1]
            exon_str = variant_split[2]

            gene_pair = gene_str.split('-')
            gene1, gene2 = gene_pair[0], gene_pair[1]

            exon_pair = exon_str.split(':')
            exon1 = f"exon{extract_pure_digit(exon_pair[0])}"
            exon2 = f"exon{extract_pure_digit(exon_pair[1])}"
            mutation_reads = ""
            for fusion_read in fusion_reads_list:
                if mut_freq == fusion_read.get("freq") and ori_variant == fusion_read.get("ori_variant"):
                    mutation_reads = fusion_read.get("sup_reads_uniq")
                    break
            # 增加去除小数点
            try:
                float_mutation_reads = float(mutation_reads)
                if float_mutation_reads.is_integer():
                    mutation_reads = int(float_mutation_reads)
                else:
                    mutation_reads = float_mutation_reads
            except:
                mutation_reads = mutation_reads
            # result = f"{gene1}:{exon1}-{gene2}:{exon2} "
            if ori_variant == "MET-MET Fusion M13:M15":
                result = f"14号外显子跳跃突变"
            else:
                result = f"{gene1}:{exon1}--{gene2}:{exon2} 融合突变"
            tip = f"{gene}基因{result}，变异丰度{mut_freq}(reads数:{mutation_reads})。"

        elif "Amplification" in ori_variant:
            tip = f"{gene}基因扩增，拷贝数{mut_freq}"
        elif "Loss" in ori_variant:
            tip = f"{gene}基因缺失，拷贝数{mut_freq}"
        else:
            new_variant = ori_variant.replace(" ", ":")
            ExonicFunc = item.get('ExonicFunc')
            if "p." in ori_variant:
                pattern = r'(p\.)([^:]+)'
                new_variant = re.sub(pattern, r'\1(\2)', new_variant)
            item['ori_variant'] = new_variant
            # exon = ''.join(re.findall(r'[0-9]', variant_split[1]))
            match = re.search(r'exon(\d+)', variant_split[1])
            if match:
                exon = match.group(1)
            else:
                exon = re.search(r'intron(\d+)', variant_split[1]).group(1)

            if "exon" in ori_variant:
                desc = "外显子"
            else:
                desc = "内含子"
            c_idx = new_variant.find('c.')
            var = new_variant[c_idx:]
            tip = f"{gene}基因{exon}号{desc}{ExonicFunc}{var}，突变丰度为{mut_freq}。"

        # is_shanghaifeike_hot_gene = gene in shanghaifeike_hot_gene_list
        is_shanghaifeike_hot_gene = False
        if (gene == "EGFR" and ori_variant == "Amplification"):
            item['variationClass'] = "II类"
            if not is_shanghaifeike_hot_gene:
                shanghaifeike_tips_2.append(tip)
        elif variationClass2 == "1" or (parse_and_check_kras(gene, ori_variant)):
            item['variationClass'] = "I类"
            if not is_shanghaifeike_hot_gene:
                shanghaifeike_tips_1.append(tip)
        else:
            item['variationClass'] = "II类"
            if not is_shanghaifeike_hot_gene:
                shanghaifeike_tips_2.append(tip)

        # 热点基因检测
        if is_shanghaifeike_hot_gene:
            shanghaifeike_hot_gene_info.append({"gene": gene, "ori_variant": ori_variant, "mut_freq": mut_freq, "variationClass": item['variationClass']})

    for item in report_json.get('unknownTipLineStr', []):
        gene = item.get('gene')
        ori_variant = item.get('ori_variant')
        mut_freq = item.get('mutFreq')
        variationClass2 = item.get('variationClass2')
        variationClass = item.get('variationClass')

        # 热点基因检测
        if gene in shanghaifeike_hot_gene_list:
            shanghaifeike_hot_gene_info.append({"gene": gene, "ori_variant": ori_variant, "mut_freq": mut_freq, "variationClass": variationClass})
            # continue

        # CNV拷贝数缺失检测
        if gene in cnv_loss_target_genes and ori_variant and "loss" in ori_variant.lower():
            loss_detected_genes.add(gene)

        variant_split = ori_variant.split(" ")
        tip = ""
        if "Fusion" in ori_variant:
            tip = f"{gene}基因{variant_split[0]}融合突变，变异丰度{mut_freq}。"

        elif "Amplification" in ori_variant:
            tip = f"{gene}基因扩增，拷贝数{mut_freq}"
        elif "Loss" in ori_variant:
            tip = f"{gene}基因缺失，拷贝数{mut_freq}"
        else:
            new_variant = ori_variant.replace(" ", ":")
            ExonicFunc = item.get('ExonicFunc')
            if "p." in ori_variant:
                pattern = r'(p\.)([^:]+)'
                new_variant = re.sub(pattern, r'\1(\2)', new_variant)
            item['ori_variant'] = new_variant
            # exon = ''.join(re.findall(r'[0-9]', variant_split[1]))
            match = re.search(r'exon(\d+)', variant_split[1])
            if match:
                exon = match.group(1)
            else:
                exon = re.search(r'intron(\d+)', variant_split[1]).group(1)
            desc = ""
            if "exon" in ori_variant:
                desc = "外显子"
            else:
                desc = "内含子"
            c_idx = new_variant.find('c.')
            var = new_variant[c_idx:]
            tip = f"{gene}基因{exon}号{desc}{ExonicFunc}{var}，突变丰度为{mut_freq}。"

        shanghaifeike_tips_3.append(tip)

    # 添加缺失的基因信息
    exist_gene_set = {row["gene"] for row in shanghaifeike_hot_gene_info}
    for hot_gene in shanghaifeike_hot_gene_list:
        if hot_gene not in exist_gene_set:
            shanghaifeike_hot_gene_info.append({
                "gene": hot_gene,
                "ori_variant": "未检出相关突变",
                "mut_freq": "-",
                "variationClass": "-"
            })
    # 按照gene顺序排序
    gene_order = {gene: idx for idx, gene in enumerate(shanghaifeike_hot_gene_list)}
    shanghaifeike_hot_gene_info.sort(key=lambda d: gene_order[d["gene"]])

    # ========== 拷贝数缺失检测结果 ==========
    # cnv_loss_result_dict = {}
    result_parts = []
    for g in cnv_loss_target_genes:
        res = "阳性" if g in loss_detected_genes else "阴性"
        # cnv_loss_result_dict[g] = res
        result_parts.append(f"{g}{res}")
    cnv_loss_full_text = "；".join(result_parts)

    report_json['shanghaifeike_tips_1'] = shanghaifeike_tips_1
    report_json['shanghaifeike_tips_2'] = shanghaifeike_tips_2
    report_json['shanghaifeike_tips_3'] = shanghaifeike_tips_3
    report_json['shanghaifeike_hot_gene_info'] = shanghaifeike_hot_gene_info
    log(shanghaifeike_hot_gene_info)
    log(shanghaifeike_tips_3)
    report_json["cnv_loss_display_text"] = cnv_loss_full_text

def parse_and_check_kras(gene, ori_variant):
    """
    自动解析整条突变字符串并判定KRAS特殊I类
    """
    if "KRAS" not in gene:
        return False

    pattern = r"(NM_\d+\.\d+)\s+exon(\d+)\s+(c\.[A-Z0-9_>]+)\s+(p\.[A-Z0-9]+)"
    match = re.match(pattern, ori_variant.strip())
    if not match:
        return False

    transcript, exon_str, hgvs_c, hgvs_p = match.groups()
    exon = int(exon_str)
    return check_kras_special_class(transcript, exon, hgvs_c, hgvs_p)

def check_kras_special_class(transcript, exon, hgvs_c, hgvs_p):
    """
    KRAS特殊I类突变判定
    :param transcript: 转录本编号
    :param exon: 外显子数字
    :param hgvs_c: c.开头突变
    :param hgvs_p: p.开头蛋白突变
    :return: (是否特殊I类, 判定说明)
    """
    # 校验转录本
    # if transcript != "NM_033360.4":
    #     return False, f"转录本{transcript}非目标转录本，不纳入特殊I类"

    p_raw = hgvs_p.lstrip("p.")
    if not p_raw:
        return False

    # 规则集合
    if exon == 2:
        if p_raw.startswith(("G12", "G13")):
            return True
        else:
            return False

    elif exon == 3:
        if p_raw.startswith("Q61"):
            return True
        else:
            return False

    elif exon == 4:
        target_p = {"K117N", "A146T", "A146V", "A146P"}
        if p_raw in target_p:
            return True
        else:
            return False

    else:
        return False
def process_shanzhong_tip(report_json):
    # 山肿胚系开发
    cr_tips = []
    for cr in report_json.get('crCheckLineStrYF1280'):
        Clinical_significance = cr.get('Clinical_significance')
        ori_variant = cr.get('ori_variant')
        Gene = cr.get('Gene')

        if 'p.' in ori_variant:
            var = ori_variant[ori_variant.find('p.'):]
        elif 'c.' in ori_variant:
            var = ori_variant[ori_variant.find('c.'):]

        cr_tips.append(f"{Gene} {var} ({Clinical_significance})")

    cr_tip = '；'.join(cr_tips) if cr_tips else '-'
    report_json['shanzhong_cr_tip'] = cr_tip


def process_qilu_tip(report_json):
    # 肾癌/中线癌分型
    if report_json.get('cancerTyping1166'):
        cancerTyping1166DNA = []
        cancerTyping1166RNA = []

        for item in report_json.get('cancerTyping1166', []):
            mut_freq_str = item.get('mut_freq', '')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if mut_freq < 0 or mut_freq > 1:
                cancerTyping1166RNA.append(item)
            else:
                cancerTyping1166DNA.append(item)

        report_json['cancerTyping1166DNA'] = cancerTyping1166DNA
        report_json['cancerTyping1166RNA'] = cancerTyping1166RNA

    # 肉瘤分型
    if report_json.get('sarcomaTyping'):
        sarcomaTypingDNA = []
        sarcomaTypingRNA = []

        for item in report_json.get('sarcomaTyping', []):
            mut_freq_str = item.get('mutFreq', '')
            mutation = item.get('mutation')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in mutation:
                sarcomaTypingRNA.append(item)
            else:
                sarcomaTypingDNA.append(item)

        report_json['sarcomaTypingDNA'] = sarcomaTypingDNA
        report_json['sarcomaTypingRNA'] = sarcomaTypingRNA

    # 体系检出
    if report_json.get('unknownTipLineStr'):
        unknownTipLineStrDNA = []
        unknownTipLineStrRNA = []

        for item in report_json.get('unknownTipLineStr', []):
            mut_freq_str = item.get('mutFreq')
            ori_variant = item.get('ori_variant', '')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                unknownTipLineStrRNA.append(item)
            else:
                unknownTipLineStrDNA.append(item)

        report_json['unknownTipLineStrDNA'] = unknownTipLineStrDNA
        report_json['unknownTipLineStrRNA'] = unknownTipLineStrRNA

    if report_json.get('bodyDrugTipLineStr'):
        bodyDrugTipLineStrDNA = []
        bodyDrugTipLineStrRNA = []

        for item in report_json.get('bodyDrugTipLineStr', []):
            mut_freq_str = item.get('mutFreq', '')
            ori_variant = item.get('ori_variant')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                bodyDrugTipLineStrRNA.append(item)
            else:
                bodyDrugTipLineStrDNA.append(item)

        report_json['bodyDrugTipLineStrDNA'] = bodyDrugTipLineStrDNA
        report_json['bodyDrugTipLineStrRNA'] = bodyDrugTipLineStrRNA

    if report_json.get('BodyDrugNoComplexStr'):
        BodyDrugNoComplexStrDNA = []
        BodyDrugNoComplexStrRNA = []

        for item in report_json.get('BodyDrugNoComplexStr', []):
            mut_freq_str = item.get('mutFreq', '')
            ori_variant = item.get('ori_variant')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                BodyDrugNoComplexStrRNA.append(item)
            else:
                BodyDrugNoComplexStrDNA.append(item)

        report_json['BodyDrugNoComplexStrDNA'] = BodyDrugNoComplexStrDNA
        report_json['BodyDrugNoComplexStrRNA'] = BodyDrugNoComplexStrRNA


def process_tongji_tip(report_json):
    if report_json.get('BodyDrugNoComplexStr'):
        tongji_mutation1 = []
        tongji_mutation2 = []
        for item in report_json.get('BodyDrugNoComplexStr', []):
            mut_freq_type = item.get('mutFreqType')
            if mut_freq_type == 'reads数':
                item['mutFreq'] = '-'

            drug_all = []
            for drug in item.get('drugaStr', []):
                drug_str = drug.get('nameLevel') + '（' + '敏感，A，' + drug.get('approvingAgency') + '）'
                drug_all.append(drug_str)
            for drug in item.get('drugbStr'):
                drug_str = drug.get('nameLevel') + '（' + '敏感，B' + '）'
                drug_all.append(drug_str)
            for drug in item.get('drugcStr'):
                drug_str = drug.get('nameLevel') + '（' + '敏感，C' + '）'
                drug_all.append(drug_str)
            for drug in item.get('drugdStr'):
                drug_str = drug.get('nameLevel') + '（' + '敏感，D' + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantaStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，A，' + drug.get('approvingAgency') + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantbStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，B' + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantcStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，C' + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantdStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，D' + '）'
                drug_all.append(drug_str)
            item['drug_all'] = drug_all

            if item.get('variationClass2') == '1':
                tongji_mutation1.append(item)
            elif item.get('variationClass2') == '2':
                tongji_mutation2.append(item)

        report_json['tongji_mutation1'] = tongji_mutation1
        report_json['tongji_mutation2'] = tongji_mutation2

    if report_json.get('ComplexDrugStr'):

        for item in report_json.get('ComplexDrugStr', []):
            drug_all = []
            for drug in item.get('drugaStr', []):
                drug_str = drug.get('nameLevel') + '（' + '敏感，A，' + drug.get('approvingAgency') + '）'
                drug_all.append(drug_str)
            for drug in item.get('drugbStr'):
                drug_str = drug.get('nameLevel') + '（' + '敏感，B' + '）'
                drug_all.append(drug_str)
            for drug in item.get('drugcStr'):
                drug_str = drug.get('nameLevel') + '（' + '敏感，C' + '）'
                drug_all.append(drug_str)
            for drug in item.get('drugdStr'):
                drug_str = drug.get('nameLevel') + '（' + '敏感，D' + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantaStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，A，' + drug.get('approvingAgency') + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantbStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，B' + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantcStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，C' + '）'
                drug_all.append(drug_str)
            for drug in item.get('resistantdStr'):
                drug_str = drug.get('nameLevel') + '（' + '耐药，D' + '）'
                drug_all.append(drug_str)
            item['drug_all'] = drug_all

    # 合并匹配列表
    list1 = report_json.get('BodyDrugNoComplexStr', [])
    list2 = report_json.get('unknownVarAnalysisStr', [])
    all_match_items = list1 + list2

    # 免疫处理
    tongji_immune = []
    if report_json.get('positiveImmnue'):
        for item in report_json.get('positiveImmnue', []):
            immune = {}
            ori_variant = item.get('variant')
            gene = item.get('gene')
            mut_freq = item.get('mutFreq')

            is_matched = False
            for item_body in all_match_items:
                body_gene = item_body.get('gene', '')
                body_ori_variant = item_body.get('ori_variant', '')
                body_mut_freq = item_body.get('mutFreq', '')
                if gene == body_gene and ori_variant == body_ori_variant and mut_freq == body_mut_freq:
                    immune['tongji_mutation'] = item_body.get('TJmutation', '')
                    is_matched = True
                    break

            if not is_matched:
                if mut_freq in ('杂合', '纯合'):
                    immune['tongji_mutation'] = f"{mut_freq}型"

            if item.get('flag') == '1':
                immune['gene'] = gene
                immune['relationship'] = '正相关'
                immune['result'] = '可能导致PD-1/PD-L1抑制剂获益率高'
            tongji_immune.append(immune)

    if report_json.get('negativeImmnue'):
        for item in report_json.get('negativeImmnue', []):
            immune = {}
            ori_variant = item.get('variant')
            gene = item.get('gene')
            mut_freq = item.get('mutFreq')

            is_matched = False
            for item_body in all_match_items:
                body_gene = item_body.get('gene', '')
                body_ori_variant = item_body.get('ori_variant', '')
                body_mut_freq = item_body.get('mutFreq', '')
                if gene == body_gene and ori_variant == body_ori_variant and mut_freq == body_mut_freq:
                    immune['tongji_mutation'] = item_body.get('TJmutation', '')
                    is_matched = True
                    break

            if not is_matched:
                if mut_freq in ('杂合', '纯合'):
                    immune['tongji_mutation'] = f"{mut_freq}型"

            if item.get('flag') == '2':
                immune['gene'] = gene
                immune['relationship'] = '负相关'
                immune['result'] = '可能导致PD-1/PD-L1抑制剂获益率低'
            tongji_immune.append(immune)

    report_json['tongji_immune'] = tongji_immune

    # KNB共突变
    is_report_knb = True
    is_V600E = False
    V600E_mutation = []
    is_colon_cancer = 9256 in report_json.get('diseaseIdList', [])
    report_json['is_colon_cancer'] = is_colon_cancer
    if is_colon_cancer:

        for item in all_match_items:
            gene = item.get('gene')
            variant = item.get('ori_variant')
            if 'Fusion' in variant or 'Loss' in variant or 'Amplification' in variant:
                continue
            if gene == 'KRAS' or gene == 'NRAS':
                is_report_knb = False
            if gene == 'BRAF':
                if 'V600E' in variant:
                    is_report_knb = False
                    is_V600E = True
                else:
                    is_report_knb = True

    if is_V600E:
        # 先筛选需要移除的项
        to_remove = []
        for item in all_match_items:
            gene = item.get('gene', '')
            variant = item.get('ori_variant', '')

            if any(keyword in variant for keyword in ['Fusion', 'Loss', 'Amplification']):
                continue

            if (gene == 'BRAF' and 'V600E' in variant) or gene in ['KRAS', 'NRAS']:
                to_remove.append(item)
                V600E_mutation.append(item)

        # for item in to_remove:
        #     gene = item.get('gene', '')
        #     if item in list1:
        #         list1.remove(item)
        #     if item in list2:
        #         list2.remove(item)
        list_filter1 = [x for x in report_json.get('tongji_mutation1', []) if
                        not any(is_same_item(x, rm_item) for rm_item in to_remove)]
        list_filter2 = [x for x in report_json.get('tongji_mutation2', []) if
                        not any(is_same_item(x, rm_item) for rm_item in to_remove)]
        list_filter3 = [x for x in report_json.get('unknownVarAnalysisStr', []) if
                        not any(is_same_item(x, rm_item) for rm_item in to_remove)]

        if len(V600E_mutation) == 1:
            KRAS = {
                'gene': 'KRAS',
                'TJmutation': '野生型',
                'mutFreq': '-',
                'mutDesc': '-',
                'drug_all': '-'
            }
            NRAS = {
                'gene': 'NRAS',
                'TJmutation': '野生型',
                'mutFreq': '-',
                'mutDesc': '-',
                'drug_all': '-'
            }
            V600E_mutation = [KRAS, NRAS] + V600E_mutation

        report_json['V600E_mutation'] = V600E_mutation
        report_json['tongji_mutation1'] = list_filter1
        report_json['tongji_mutation2'] = list_filter2
        report_json['unknownVarAnalysisStr'] = list_filter3
    report_json['is_report_knb'] = is_report_knb

    # HRR
    tongji_hrr = ""
    HRR_info = report_json.get('HRRInfo')
    tongji_hrr_table = []
    if HRR_info:
        HRR_genes = ["ATM", "BARD1", "BRCA1", "BRCA2", "BRIP1", "CDK12", "CHEK1", "CHEK2", "FANCL", "PALB2", "RAD51B",
                     "RAD51C", "RAD51D", "RAD54L", "PPP2R2A"]
        for gene in HRR_genes:
            variant = HRR_info.get(gene)
            if variant != "-":
                tongji_hrr_table.append({
                    'gene': gene,
                    'variant': variant,
                    'clinical_significance_desc': HRR_info.get('clinical_significance_desc1') if gene in ["BRCA1",
                                                                                                          "BRCA2"] else HRR_info.get(
                        'clinical_significance_desc2')
                })
                pattern = r'p\.(\S+)'
                match = re.search(pattern, variant)
                if match:
                    p_part = match.group(1)
                    new_variant = f"p.({p_part})"
                else:
                    new_variant = variant
                tongji_hrr += gene + " " + new_variant + ";"

    report_json['tongji_hrr'] = tongji_hrr
    report_json['tongji_hrr_table'] = tongji_hrr_table


def is_same_item(item1, item2):
    return item1.get('gene') == item2.get('gene') and item1.get('ori_variant') == item2.get('ori_variant')


def process_guangfuyi_tip(report_json):
    bodyDrugTipLineStr = report_json.get('bodyDrugTipLineStr', [])
    unknownTipLineStr = report_json.get('unknownTipLineStr', [])
    gfy_ori_variant = report_json.get('gfy_ori_variant', {})
    gfy_egfr_list = gfy_ori_variant.get('gfyEGFR', [])

    for idx in range(len(gfy_egfr_list)):
        item = gfy_egfr_list[idx]
        # if 'exon20' in item and 'delins' in item:
        #     cHGVS = item.split(' ')[2]
        #     # 注意：这里原代码有个小问题，cHGVS是列表，需取对应元素再split（假设是分割后的第一个元素）
        #     del_part, ins_seq = cHGVS.split('delins')  # 修正：列表不能直接split，取第一个元素
        #     del_pos = del_part.split('.')[-1]
        #     start, end = del_pos.split('_')
        #     del_count = int(end) - int(start) + 1
        #     ins_count = len(ins_seq)
        #     if del_count < ins_count:
        #         # 直接通过索引修改原列表的元素，实现原数据更新
        #         gfy_egfr_list[idx] = item + ' 20号外显子插入突变'
        #     continue  # 满足第一个条件，跳过后续判断
        if 'exon20' in item and 'delins' in item:
            # 取出蛋白命名（p.D770delinsGY）
            pHGVS = item.split(' ')[3]

            #  按 delins 拆分
            parts = pHGVS.split('delins')
            if len(parts) != 2:
                continue

            del_part, ins_seq = parts

            numbers = re.findall(r'\d+', del_part)  # 提取所有数字
            if len(numbers) == 1:
                del_count = 1

            elif len(numbers) == 2:
                # 两个位置（如 770_772）→ 计算区间
                start = int(numbers[0])
                end = int(numbers[1])
                del_count = end - start + 1

            else:
                continue  # 格式异常跳过

            # 插入氨基酸数量
            ins_count = len(ins_seq)

            # 判断
            if del_count < ins_count:
                gfy_egfr_list[idx] = item + ' 20号外显子插入突变'

            continue

        if 'exon20' in item and ('dup' in item or 'ins' in item):
            # 同样通过索引修改原数据
            gfy_egfr_list[idx] = item + ' 20号外显子插入突变'

    for item in bodyDrugTipLineStr:
        gene = item.get('gene', '')
        ori_variant = item.get('ori_variant', '')

        if gene == 'EGFR' and 'exon20' in ori_variant and 'delins' in ori_variant:
            pHGVS = ori_variant.split(' ')[3]
            #  按 delins 拆分
            parts = pHGVS.split('delins')
            if len(parts) != 2:
                continue

            del_part, ins_seq = parts
            # 提取所有数字
            numbers = re.findall(r'\d+', del_part)
            if len(numbers) == 1:
                del_count = 1

            elif len(numbers) == 2:
                # 两个位置（如 770_772）→ 计算区间
                start = int(numbers[0])
                end = int(numbers[1])
                del_count = end - start + 1

            else:
                continue

            # 插入氨基酸数量
            ins_count = len(ins_seq)
            if del_count < ins_count:
                item['ExonicFunc'] = '20号外显子插入突变'
                item['ori_variant'] = ori_variant + '20号外显子插入突变'
            continue
        if gene == 'EGFR' and 'exon20' in ori_variant and ('dup' in ori_variant or 'ins' in ori_variant):
            item['ExonicFunc'] = '20号外显子插入突变'
            item['ori_variant'] = ori_variant + '20号外显子插入突变'

        report_json['bodyDrugTipLineStr'] = bodyDrugTipLineStr

    for item in unknownTipLineStr:
        gene = item.get('gene', '')
        ori_variant = item.get('ori_variant', '')
        # if gene == 'EGFR' and 'exon20' in ori_variant and 'delins' in ori_variant:
        #     cHGVS = item.get('cHGVS', '')
        #     del_part, ins_seq = cHGVS.split('delins')
        #     del_pos = del_part.split('.')[-1]
        #     start, end = del_pos.split('_')
        #     del_count = int(end) - int(start) + 1
        #     ins_count = len(ins_seq)
        #     if del_count < ins_count:
        #         item['ExonicFunc'] = '20号外显子插入突变'
        #     continue
        if 'exon20' in ori_variant and 'delins' in ori_variant:
            # 取出蛋白命名（p.D770delinsGY）
            pHGVS = ori_variant.split(' ')[3]

            #  按 delins 拆分
            parts = pHGVS.split('delins')
            if len(parts) != 2:
                continue

            del_part, ins_seq = parts

            numbers = re.findall(r'\d+', del_part)  # 提取所有数字
            if len(numbers) == 1:
                del_count = 1

            elif len(numbers) == 2:
                # 两个位置（如 770_772）→ 计算区间
                start = int(numbers[0])
                end = int(numbers[1])
                del_count = end - start + 1

            else:
                continue  # 格式异常跳过

            # 插入氨基酸数量
            ins_count = len(ins_seq)

            # 判断
            if del_count < ins_count:
                gfy_egfr_list[idx] = item + ' 20号外显子插入突变'

            continue

        if gene == 'EGFR' and 'exon20' in ori_variant and ('dup' in ori_variant or 'ins' in ori_variant):
            item['ExonicFunc'] = '20号外显子插入突变'

        report_json['unknownTipLineStr'] = unknownTipLineStr

def mysql_query(product_name, analysis_date, subbarcode):
    db_config = {
        'host': '127.0.0.1',
        'port': 8806,
        'user': 'novo',
        'password': 'GodIsLove',
        'database': 'omics',
        'charset': 'utf8mb4'
    }

    conn = None
    cursor = None
    result_list = []

    try:
        conn = pymysql.connect(**db_config)
        cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)

        query_sql = '''SELECT
            b.ori_variant,
            b.freq,
            b.sup_reads_hq,
            b.sup_reads_uniq
            FROM
            data_file_status AS a
            LEFT JOIN fusion_file AS b ON a.file_id = b.file_id
            WHERE
            a.product_name = %s
            AND a.analysis_date = %s
            AND a.subbarcode = %s
            AND a.file_type = %s'''

        query_param = (
            product_name,
            analysis_date,
            subbarcode,
            "Fusion"
        )

        cursor.execute(query_sql, query_param)
        all_results = cursor.fetchall()

        if all_results:
            for row in all_results:
                result_list.append(row)

    except pymysql.Error as e:
        print(f"❌ 数据库查询失败：{e}")

    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

    return result_list

def extract_pure_digit(s):
    """提取字符串中的纯数字，拼接后返回"""
    # 遍历每个字符，筛选数字并拼接
    pure_digit = ''.join([char for char in s if char.isdigit()])
    return pure_digit

def process_anhuixiongke_tip(report_json):
    category_1_variant_list = []
    BodyDrugNoComplexStr = report_json.get('BodyDrugNoComplexStr', [])
    hotGeneDrugSet = report_json.get('hotGeneDrugSet', [])
    category_1_variant_list.extend(hotGeneDrugSet)
    for item in BodyDrugNoComplexStr:
        gene = item.get('gene', '')
        ori_variant = item.get('ori_variant', '')
        ori_variant1 = item.get('ori_variant1', '')
        variationClass2 = item.get('variationClass2', '')

        if variationClass2 == '1':
            if 'Amplification' in ori_variant or 'Loss' in ori_variant or 'Fusion' in ori_variant:
                tip = f"{gene} {ori_variant} 突变"

            else:
                variant_split = ori_variant.split(' ')
                if 'exon' in variant_split[1]:
                    tip = f"{gene} {extract_pure_digit(variant_split[1])}号外显子突变"
                elif 'intron' in variant_split[1]:
                    tip = f"{gene} {extract_pure_digit(variant_split[1])}号内含子区突变"
                else:
                    tip = f"{gene} {ori_variant} 突变"

            if ori_variant1 == 'MET 14号外显子跳跃':
                tip += f'({ori_variant1})'

            if tip in category_1_variant_list:
                continue
            category_1_variant_list.append(tip)

    report_json['category_1_variant_list'] = category_1_variant_list


def process_WJM_tip(report_json, to_update_json_data):
    # 肾癌/中线癌分型
    if report_json.get('cancerTyping1166'):
        cancerTyping1166DNA = []
        cancerTyping1166RNA = []

        for item in report_json.get('cancerTyping1166', []):
            mut_freq_str = item.get('mut_freq', '')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if mut_freq < 0 or mut_freq > 1:
                cancerTyping1166RNA.append(item)
            else:
                cancerTyping1166DNA.append(item)

        report_json['cancerTyping1166DNA'] = cancerTyping1166DNA
        report_json['cancerTyping1166RNA'] = cancerTyping1166RNA
        to_update_json_data['cancerTyping1166DNA'] = cancerTyping1166DNA
        to_update_json_data['cancerTyping1166RNA'] = cancerTyping1166RNA

    # 肉瘤分型
    if report_json.get('sarcomaTyping'):
        sarcomaTypingDNA = []
        sarcomaTypingRNA = []

        for item in report_json.get('sarcomaTyping', []):
            mut_freq_str = item.get('mutFreq', '')
            mutation = item.get('mutation')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in mutation:
                sarcomaTypingRNA.append(item)
            else:
                sarcomaTypingDNA.append(item)

        report_json['sarcomaTypingDNA'] = sarcomaTypingDNA
        report_json['sarcomaTypingRNA'] = sarcomaTypingRNA
        to_update_json_data['sarcomaTypingDNA'] = sarcomaTypingDNA
        to_update_json_data['sarcomaTypingRNA'] = sarcomaTypingRNA

    # 体系检出
    if report_json.get('unknownTipLineStr'):
        unknownTipLineStrDNA = []
        unknownTipLineStrRNA = []

        for item in report_json.get('unknownTipLineStr', []):
            mut_freq_str = item.get('mutFreq')
            ori_variant = item.get('ori_variant', '')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                unknownTipLineStrRNA.append(item)
            else:
                unknownTipLineStrDNA.append(item)

        report_json['unknownTipLineStrDNA'] = unknownTipLineStrDNA
        report_json['unknownTipLineStrRNA'] = unknownTipLineStrRNA
        to_update_json_data['unknownTipLineStrDNA'] = unknownTipLineStrDNA
        to_update_json_data['unknownTipLineStrRNA'] = unknownTipLineStrRNA

    if report_json.get('bodyDrugTipLineStr'):
        bodyDrugTipLineStrDNA = []
        bodyDrugTipLineStrRNA = []

        for item in report_json.get('bodyDrugTipLineStr', []):
            mut_freq_str = item.get('mutFreq', '')
            ori_variant = item.get('ori_variant')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                bodyDrugTipLineStrRNA.append(item)
            else:
                bodyDrugTipLineStrDNA.append(item)

        report_json['bodyDrugTipLineStrDNA'] = bodyDrugTipLineStrDNA
        report_json['bodyDrugTipLineStrRNA'] = bodyDrugTipLineStrRNA
        to_update_json_data['bodyDrugTipLineStrDNA'] = bodyDrugTipLineStrDNA
        to_update_json_data['bodyDrugTipLineStrRNA'] = bodyDrugTipLineStrRNA

    if report_json.get('BodyDrugNoComplexStr'):
        BodyDrugNoComplexStrDNA = []
        BodyDrugNoComplexStrRNA = []

        for item in report_json.get('BodyDrugNoComplexStr', []):
            mut_freq_str = item.get('mutFreq', '')
            ori_variant = item.get('ori_variant')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                BodyDrugNoComplexStrRNA.append(item)
            else:
                BodyDrugNoComplexStrDNA.append(item)

        report_json['BodyDrugNoComplexStrDNA'] = BodyDrugNoComplexStrDNA
        report_json['BodyDrugNoComplexStrRNA'] = BodyDrugNoComplexStrRNA
        to_update_json_data['BodyDrugNoComplexStrDNA'] = BodyDrugNoComplexStrDNA
        to_update_json_data['BodyDrugNoComplexStrRNA'] = BodyDrugNoComplexStrRNA

    if report_json.get('unknownVarAnalysisStr'):
        unknownVarAnalysisStrDNA = []
        unknownVarAnalysisStrRNA = []

        for item in report_json.get('unknownVarAnalysisStr', []):
            mut_freq_str = item.get('mutFreq', '')
            ori_variant = item.get('ori_variant')

            try:
                if mut_freq_str.endswith('%'):
                    # 去除百分号后转浮点数，再除以100
                    mut_freq_clean = mut_freq_str.replace('%', '')
                    mut_freq = float(mut_freq_clean) / 100
                else:
                    # 无百分号则直接转浮点数（兼容小数/整数格式）
                    mut_freq = float(mut_freq_str)
            except:
                # 处理空值、非数字等异常情况，默认设为0.0
                mut_freq = 0.0

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion' in ori_variant:
                unknownVarAnalysisStrRNA.append(item)
            else:
                unknownVarAnalysisStrDNA.append(item)

        report_json['unknownVarAnalysisStrDNA'] = unknownVarAnalysisStrDNA
        report_json['unknownVarAnalysisStrRNA'] = unknownVarAnalysisStrRNA
        to_update_json_data['unknownVarAnalysisStrDNA'] = unknownVarAnalysisStrDNA
        to_update_json_data['unknownVarAnalysisStrRNA'] = unknownVarAnalysisStrRNA

    remove_alk_dna_negative_immune_for_same_rna_fusion(report_json, to_update_json_data)


def remove_alk_dna_negative_immune_for_same_rna_fusion(report_json, to_update_json_data):
    rna_alk_fusion_variants = set()
    dna_alk_fusion_variants = set()
    negative_immnue = report_json.get('negativeImmnue', [])
    for item in negative_immnue:
        variant = item.get('variant') or ''
        if not variant or item.get('gene') != 'ALK' or 'Fusion' not in variant:
            continue

        if is_rna_fusion_item(item):
            rna_alk_fusion_variants.add(variant)
        else:
            dna_alk_fusion_variants.add(variant)

    same_breakpoint_variants = rna_alk_fusion_variants & dna_alk_fusion_variants
    if not same_breakpoint_variants:
        return

    filtered_negative_immnue = [
        item for item in negative_immnue
        if not is_same_breakpoint_alk_dna_negative_immune(item, same_breakpoint_variants)
    ]
    if len(filtered_negative_immnue) == len(negative_immnue):
        return

    report_json['negativeImmnue'] = filtered_negative_immnue
    to_update_json_data['negativeImmnue'] = filtered_negative_immnue

    summary = report_json.get('summaryOfRresults', {})
    summary['negativeImmnueNum'] = sum(1 for item in filtered_negative_immnue if item.get('varDesc') != '/')
    report_json['summaryOfRresults'] = summary
    to_update_json_data['summaryOfRresults'] = summary


def get_variant_value(item):
    return item.get('variant') or item.get('ori_variant') or item.get('mutation') or ''


def is_rna_fusion_item(item):
    variant = get_variant_value(item)
    return (parse_mut_freq(item.get('mutFreq', '')) < 0 or parse_mut_freq(item.get('mutFreq', '')) > 1) and 'Fusion' in variant


def parse_mut_freq(mut_freq_str):
    try:
        mut_freq_str = str(mut_freq_str)
        if mut_freq_str.endswith('%'):
            return float(mut_freq_str.replace('%', '')) / 100
        return float(mut_freq_str)
    except:
        return 0.0


def is_same_breakpoint_alk_dna_negative_immune(item, same_breakpoint_variants):
    return (
            item.get('gene') == 'ALK'
            and 'Fusion' in get_variant_value(item)
            and item.get('variant') in same_breakpoint_variants
            and not is_rna_fusion_item(item)
    )

def process_ZHSRRYY_tip(report_json):
    bodyDrugTipLineStr = report_json.get('bodyDrugTipLineStr',[])
    unknownVarAnalysisStr = report_json.get('unknownVarAnalysisStr',[])
    classI_variant_num = 0
    classII_variant_num = 0
    classIII_variant_num = len(unknownVarAnalysisStr)
    classI_variant_List = []
    classII_variant_List = []

    for item in bodyDrugTipLineStr:
        variationClass2 = item.get('variationClass2')
        if variationClass2 == '1':
            classI_variant_num += 1
            classI_variant_List.append(item)
        elif variationClass2 == '2':
            classII_variant_num += 1
            classII_variant_List.append(item)

    report_json['classI_variant_num'] = classI_variant_num
    report_json['classII_variant_num'] = classII_variant_num
    report_json['classIII_variant_num'] = classIII_variant_num
    report_json['classI_variant_List'] = classI_variant_List
    report_json['classII_variant_List'] = classII_variant_List

def process_ZHSRRYY_tip_v1(report_json):
    # 个性化信息汇总
    res_info = {}

    report_info = report_json.get('reportInfo', {})
    conf = report_info.get('conf', {})
    subbarcode = report_json.get('subbarcode')
    # subbarcode = "TKHS260052919-2A"
    product_name = report_json.get('panel')
    analysis_date = report_json.get('analysisDate')

    # 检测结果汇总
    result_summary = []

    ## HRD
    if conf.get('hrdStateScoreTip'):
        hrdStateScoreTip = report_json.get('summaryOfRresults').get('hrdState')
        if hrdStateScoreTip == '阳性':
            hrd_desc = 'HRD 状态：阳性，提示对PARP抑制剂可能敏感。'
        else:
            hrd_desc = 'HRD 状态：阴性，提示对PARP抑制剂可能敏感。'
        result_summary.append(hrd_desc)

        res = query_HRD_info(product_name, analysis_date, subbarcode)
        # res_info['result_summary'] = result_summary
        res_info['hrd_info'] = res

    ## 无靶点表格
    disease_name = report_json.get('diseaseName')  # 临床诊断
    log(report_info)
    approved_desc = f"NMPA/FDA适用于{disease_name}的多靶点药物有："
    drug_list = []
    for item in report_json.get('approvedDrugData', []):
        drug = item.get('drug')
        if drug:  # 避免空值
            drug_list.append(drug)

    # 拼接：逗号分隔 + 句号结尾
    if drug_list:
        approved_desc += "、".join(drug_list) + "。"
    else:
        approved_desc += "无。"
    result_summary.append(approved_desc)

    ## 用药提示
    bodyDrugTipLineStr = report_json.get('bodyDrugTipLineStr')
    embryonalDrugTipLineStr = report_json.get('embryonalDrugTipLineStr')

    drug_tip = '患者本次未检测到有用药突变'
    if bodyDrugTipLineStr or embryonalDrugTipLineStr:
        drug_tip = '患者本次检测到'

        for index, item in enumerate(bodyDrugTipLineStr):
            # 第 1、2、3... 条
            # desc = f"{index + 1}、"

            gene = item.get('gene')
            pHGVS = item.get('pHGVS')
            cHGVS = item.get('cHGVS')

            # 优先用 pHGVS，等于 / 时改用 cHGVS
            desc = f"{gene} {pHGVS}，" if pHGVS != "/" else f"{gene} {cHGVS}，"

            # === 处理敏感药 ===
            drugNameList = item.get('drugNameList', [])
            sensitive_drugs = [d.get('name') for d in drugNameList if d.get('nameLevel')]
            sensitive_str = '、'.join(sensitive_drugs) if sensitive_drugs else '暂无'

            # === 处理耐药药 ===
            ResistantDrug = item.get('ResistantDrug', [])
            resistant_drugs = [d.get('name') for d in ResistantDrug if d.get('nameLevel')]
            resistant_str = '、'.join(resistant_drugs) if resistant_drugs else '暂无'

            # === 拼接最终描述 ===
            desc += f"提示敏感药物：{sensitive_str}，潜在耐药药物：{resistant_str}。"
            drug_tip += desc


        for index, item in enumerate(embryonalDrugTipLineStr):

            gene = item.get('gene')
            pHGVS = item.get('pHGVS')
            desc = f"{gene} {pHGVS}，"
            # === 处理敏感药 ===
            drugNameList = item.get('drugNameList', [])
            sensitive_drugs = [d.get('name') for d in drugNameList if d.get('nameLevel')]
            sensitive_str = '、'.join(sensitive_drugs) if sensitive_drugs else '暂无'

            # === 处理耐药药 ===
            ResistantDrug = item.get('ResistantDrug', [])
            resistant_drugs = [d.get('name') for d in ResistantDrug if d.get('nameLevel')]
            resistant_str = '、'.join(resistant_drugs) if resistant_drugs else '暂无'

            # === 拼接最终描述 ===
            desc += f"提示敏感药物：{sensitive_str}，潜在耐药药物：{resistant_str}。"
            drug_tip += desc

        result_summary.append(drug_tip)

    ## MSI
    if conf.get('msi'):
        msi_status = report_json.get('summaryOfRresults').get('msi_status')
        if msi_status == 'MSI-H':
            msi_desc = '微卫星不稳定型（MSI-H）患者免疫治疗预后较好，接受免疫检查点抑制剂药物治疗的获益率较高。'
        else:
            msi_desc = '微卫星稳定型（MSS）患者接受免疫检查点抑制剂药物治疗的获益率较低。'
        result_summary.append(msi_desc)

    res_info['result_summary'] = result_summary

    # 解读-用药提示-胚/体
    if conf.get('somaticMutationTip'):
        bodyDrugTipLineStr = report_json.get('bodyDrugTipLineStr')
        bodyDrugTipLineStrI = []
        bodyDrugTipLineStrII = []

        has_BRCA1 = False
        has_BRCA2 = False
        for item in bodyDrugTipLineStr:
            data = {}
            sensitive_drugA = []
            sensitive_drug_withoutA = []
            resistant_drug = []

            gene = item.get('gene')
            mutFreq = item.get('mutFreq')
            ExonicFunc = item.get('ExonicFunc')
            variationClass2 = item.get('variationClass2')

            if ExonicFunc == '基因扩增' or ExonicFunc == '基因缺失':
                variant = f"{gene},{ExonicFunc},拷贝数 = {mutFreq}"
            elif ExonicFunc == '基因融合':
                variant = f"{gene},{ExonicFunc},reads数 = {mutFreq}"
            else:
                Exon = item.get('Exon')
                desc = format_intron_exon(Exon)
                pHGVS = item.get('pHGVS')
                variant = f"{gene},{desc},{pHGVS},{ExonicFunc},丰度 = {mutFreq}"

            drugNameList = item.get('drugNameList', [])
            for drug in drugNameList:
                if drug.get('level') == '1':
                    sensitive_drugA.append(drug)
                else:
                    sensitive_drug_withoutA.append(drug)

            resistant_drug = item.get('ResistantDrug', [])
            data['variant'] = variant
            data['sensitive_drugA'] = sensitive_drugA
            data['sensitive_drug_withoutA'] = sensitive_drug_withoutA
            data['resistant_drug'] = resistant_drug

            if variationClass2 == '1':
                if gene == 'BRCA1':
                    has_BRCA1 = True
                if gene == 'BRCA2':
                    has_BRCA2 = True
                bodyDrugTipLineStrI.append(data)
            else:
                bodyDrugTipLineStrII.append(data)

        # 癌种限制和BRCA1/2相关的癌种
        if not has_BRCA1:
            BRCA1_drug_tip = {
                'variant': 'BRCA1 未检出',
                'sensitive_drugA': [],
                'sensitive_drug_withoutA': [],
                'resistant_drug': []
            }
            bodyDrugTipLineStrI.append(BRCA1_drug_tip)
        if not has_BRCA2:
            BRCA2_drug_tip = {
                'variant': 'BRCA2 未检出',
                'sensitive_drugA': [],
                'sensitive_drug_withoutA': [],
                'resistant_drug': []
            }
            bodyDrugTipLineStrI.append(BRCA2_drug_tip)

        res_info['bodyDrugTipLineStrI'] = bodyDrugTipLineStrI
        res_info['bodyDrugTipLineStrII'] = bodyDrugTipLineStrII

    report_json['zzsrryy_info'] = res_info
def format_intron_exon(seq_str):
    """
    同时格式化 exon（外显子）和 intron（内含子）
    :param seq_str: 输入字符串，例如 exon2、intron10
    :return: 格式化后的中文字符串
    """
    # 定义类型映射关系
    type_map = {
        "exon": "外显子",
        "intron": "内含子"
    }
    # 提取类型前缀和数字部分
    prefix = None
    num_part = ""
    for key in type_map.keys():
        if seq_str.startswith(key):
            prefix = key
            # 提取前缀后的所有数字
            num_part = ''.join([c for c in seq_str[len(key):] if c.isdigit()])
            break
    # 容错处理：无匹配前缀或无数字时返回原字符串
    if not prefix or not num_part:
        return seq_str
    # 拼接结果
    return f"{num_part}号{type_map[prefix]}"

def _check_sql_identifier(identifier):
    if not re.match(r'^[A-Za-z_][A-Za-z0-9_]*$', identifier or ''):
        raise ValueError("invalid sql identifier: %s" % identifier)


def _json_path_for_field(field_name):
    _check_sql_identifier(field_name)
    return '$.%s' % field_name


def _debug_log(msg):
    try:
        log("[custom_json_update] %s" % msg)
    except Exception:
        pass


def _default_db_config():
    return {
        'host': '127.0.0.1',
        'port': 8806,
        'user': 'novo',
        'password': 'GodIsLove',
        'database': 'omics',
        'charset': 'utf8mb4'
    }


def update_report_json_fields(report_id, fields, table_name='analysis_report_store',
                              json_column='report_detail', id_column='report_id',
                              db_config=None):
    """
    用 JSON_SET 局部更新指定表的大 JSON 字段：不存在的字段会新增，已存在字段会覆盖。
    示例：update_report_json_fields('23976', {'is_DR_panel': True})
    """
    if not report_id:
        raise ValueError("report_id is required")
    if not isinstance(fields, dict) or not fields:
        raise ValueError("fields must be a non-empty dict")

    _check_sql_identifier(table_name)
    _check_sql_identifier(json_column)
    _check_sql_identifier(id_column)

    if db_config is None:
        db_config = _default_db_config()

    _debug_log("db_config host=%s port=%s database=%s user=%s" % (
        db_config.get('host'), db_config.get('port'), db_config.get('database'), db_config.get('user')
    ))

    json_set_args = []
    update_params = []
    json_paths = []
    for field_name, field_value in fields.items():
        json_path = _json_path_for_field(field_name)
        json_set_args.append("%s, JSON_EXTRACT(%s, '$')")
        update_params.append(json_path)
        update_params.append(json.dumps(field_value, ensure_ascii=False))
        json_paths.append(json_path)

    _debug_log("start report_id=%s table=%s column=%s fields=%s paths=%s" % (
        report_id, table_name, json_column, list(fields.keys()), json_paths
    ))

    conn = None
    cursor = None

    try:
        conn = pymysql.connect(**db_config)
        cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)

        cursor.execute(
            "SELECT DATABASE() AS db_name, @@hostname AS mysql_host, @@port AS mysql_port, "
            "USER() AS login_user, CURRENT_USER() AS current_user"
        )
        _debug_log("connection_identity %s" % cursor.fetchone())

        range_sql = "SELECT MIN({id_column}) AS min_id, MAX({id_column}) AS max_id FROM {table_name}".format(
            table_name=table_name,
            id_column=id_column
        )
        cursor.execute(range_sql)
        _debug_log("table_id_range table=%s %s" % (table_name, cursor.fetchone()))

        count_sql = "SELECT COUNT(1) AS row_count FROM {table_name} WHERE {id_column} = %s".format(
            table_name=table_name,
            id_column=id_column
        )
        cursor.execute(count_sql, (report_id,))
        row_count = cursor.fetchone().get('row_count')
        _debug_log("before_update report_id=%s matched_rows=%s" % (report_id, row_count))

        try:
            report_id_int = int(report_id)
            nearby_sql = (
                "SELECT {id_column} FROM {table_name} "
                "WHERE {id_column} BETWEEN %s AND %s ORDER BY {id_column} LIMIT 20"
            ).format(
                table_name=table_name,
                id_column=id_column
            )
            cursor.execute(nearby_sql, (report_id_int - 10, report_id_int + 10))
            _debug_log("nearby_ids report_id=%s rows=%s" % (report_id, cursor.fetchall()))
        except Exception as nearby_error:
            _debug_log("nearby_ids_skip report_id=%s error=%s" % (report_id, nearby_error))

        update_sql = (
            "UPDATE {table_name} "
            "SET {json_column} = JSON_SET(COALESCE(NULLIF({json_column}, ''), JSON_OBJECT()), {json_set_args}) "
            "WHERE {id_column} = %s"
        ).format(
            table_name=table_name,
            json_column=json_column,
            json_set_args=', '.join(json_set_args),
            id_column=id_column
        )
        cursor.execute(update_sql, update_params + [report_id])
        affected_rows = cursor.rowcount

        conn.commit()
        verify_selects = []
        verify_params = []
        for field_name in fields.keys():
            verify_selects.append("JSON_CONTAINS_PATH({json_column}, 'one', %s) AS `{field_name}`".format(
                json_column=json_column,
                field_name=field_name
            ))
            verify_params.append(_json_path_for_field(field_name))

        verify_sql = "SELECT {verify_selects} FROM {table_name} WHERE {id_column} = %s".format(
            verify_selects=', '.join(verify_selects),
            table_name=table_name,
            id_column=id_column
        )
        cursor.execute(verify_sql, verify_params + [report_id])
        verify_row = cursor.fetchone()
        _debug_log("success report_id=%s rowcount=%s fields=%s" % (
            report_id, affected_rows, list(fields.keys())
        ))
        _debug_log("verify report_id=%s exists=%s" % (
            report_id, verify_row
        ))
        return affected_rows

    except Exception as e:
        _debug_log("error report_id=%s fields=%s error=%s" % (
            report_id, list(fields.keys()), e
        ))
        if conn:
            conn.rollback()
        raise

    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()


def update_current_report_detail_fields(report_id, fields):
    """
    将已计算出的字段局部写回 analysis_report_store.report_detail。
    """
    if not fields:
        _debug_log("skip report_id=%s reason=empty_fields" % report_id)
        return 0

    _debug_log("prepare report_id=%s fields=%s" % (report_id, list(fields.keys())))
    return update_report_json_fields(report_id, fields)


def query_HRD_info(product_name, analysis_date, subbarcode):
    db_config = _default_db_config()

    conn = None
    cursor = None

    try:
        conn = pymysql.connect(**db_config)
        cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)

        query_sql = '''SELECT
            b.LOH,
            b.TAI,
            b.LST
            FROM
            data_file_status AS a
            LEFT JOIN hrd_results_file AS b ON a.file_id = b.file_id
            WHERE
            a.product_name = %s
            AND a.analysis_date = %s
            AND a.subbarcode = %s
            AND a.file_type = %s'''

        query_param = (
            product_name,
            analysis_date,
            subbarcode,
            "HRD_results"
        )

        log(f"查询 HRD 信息：{query_sql} {query_param}")

        cursor.execute(query_sql, query_param)
        results = cursor.fetchone()

    except pymysql.Error as e:
        print(f"❌ 数据库查询失败：{e}")

    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

    return results


def process_QL_QC_info(report_json):
    subbarcode = report_json.get('subbarcode')
    # subbarcode = "TKHS260053714-1A"
    product_name = report_json.get('panel')
    analysis_date = report_json.get('analysisDate')
    log(report_json)
    QL_QC_info = query_QL_QC_info(product_name, analysis_date, subbarcode, "qc_g", "DNA")
    QL_QC_hrd_info = query_QL_QC_info(product_name, analysis_date, subbarcode, "qc_g_hrd", "HRD")
    report_json['ql_qc_g_info'] = QL_QC_info
    report_json['ql_qc_g_hrd_info'] = QL_QC_hrd_info


def query_QL_QC_info(product_name, analysis_date, subbarcode,file_type, type):
    db_config = _default_db_config()

    conn = None
    cursor = None
    results = None

    try:
        conn = pymysql.connect(**db_config)
        cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)

        query_sql = '''SELECT
            b.*
            FROM
            data_file_status AS a
            LEFT JOIN qc_g_file AS b ON a.file_id = b.file_id
            WHERE
            a.product_name = %s
            AND a.analysis_date = %s
            AND a.subbarcode = %s
            AND a.file_type = %s
            AND b.type = %s'''

        query_param = (product_name, analysis_date, subbarcode, file_type, type)
        log(f"查询 QL_QC 信息：{query_sql} {query_param}")

        cursor.execute(query_sql, query_param)
        results = cursor.fetchone()

    except Exception as e:
        log(f"报错：{e}")

    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

    return results


def log(msg):
    with open("/data/soft/report/query_log.txt", "a", encoding="utf-8") as f:
        print(msg, file=f)




