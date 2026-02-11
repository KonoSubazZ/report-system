import re
import pymysql
from datetime import datetime

def process_custom_data(report_json):
    template_name = report_json.get('summaryOfRresults').get('template_name')

    # 上海肺科
    if template_name == '肺癌60基因报告-上海肺科':
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
    if template_name == '肉瘤1238+1166基因检测报告-WJM' or template_name == '肉瘤550+596基因检测报告-WJM'\
            or template_name == '泛实体瘤1238+1166基因检测报告-WJM' or template_name == '泛实体瘤550+596基因检测报告-WJM'\
            or template_name == '泛实体瘤108+33基因检测报告-单样本-WJM' or template_name == '泛实体瘤58+22基因检测报告-单样本-WJM':
        process_WJM_tip(report_json)


def process_shanghaifeike_tip(report_json):
    shanghaifeike_tips_1 = []
    shanghaifeike_tips_2 = []
    shanghaifeike_tips_3 = []

    subbarcode = report_json.get('subbarcode')
    analysis_date = report_json.get('analysis_date')
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
        variant_split = ori_variant.split(" ")
        tip = ""
        if "Fusion" in ori_variant:
            today = datetime.today()
            today_formatted = today.strftime("%Y%m%d")
            fusion_reads_list = mysql_query(product_name, today_formatted , subbarcode)

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

        if variationClass2 == "1" or (gene == "KRAS" and variationClass2 == "2") or (gene == "KRAS" and ori_variant != "Amplification"):
            item['variationClass'] = "I类"
            shanghaifeike_tips_1.append(tip)
        else:
            shanghaifeike_tips_2.append(tip)
            item['variationClass'] = "II类"

    for item in report_json.get('unknownTipLineStr', []):
        gene = item.get('gene')
        ori_variant = item.get('ori_variant')
        mut_freq = item.get('mutFreq')
        variationClass2 = item.get('variationClass2')
        variant_split = ori_variant.split(" ")
        tip = ""
        if "Fusion" in ori_variant:
            tip = f"{gene}基因{variant_split[0]}融合突变，变异丰度{mut_freq}。"

        elif "Amplification" in ori_variant:
            tip = f"{gene}基因扩增，拷贝数{mut_freq}"
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

    report_json['shanghaifeike_tips_1'] = shanghaifeike_tips_1
    report_json['shanghaifeike_tips_2'] = shanghaifeike_tips_2
    report_json['shanghaifeike_tips_3'] = shanghaifeike_tips_3


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
        if 'exon20' in item and 'delins' in item:
            cHGVS = item.split(' ')[2]
            # 注意：这里原代码有个小问题，cHGVS是列表，需取对应元素再split（假设是分割后的第一个元素）
            del_part, ins_seq = cHGVS.split('delins')  # 修正：列表不能直接split，取第一个元素
            del_pos = del_part.split('.')[-1]
            start, end = del_pos.split('_')
            del_count = int(end) - int(start) + 1
            ins_count = len(ins_seq)
            if del_count < ins_count:
                # 直接通过索引修改原列表的元素，实现原数据更新
                gfy_egfr_list[idx] = item + ' 20号外显子插入突变'
            continue  # 满足第一个条件，跳过后续判断

        if 'exon20' in item and ('dup' in item or 'ins' in item):
            # 同样通过索引修改原数据
            gfy_egfr_list[idx] = item + ' 20号外显子插入突变'

    for item in bodyDrugTipLineStr:
        gene = item.get('gene', '')
        ori_variant = item.get('ori_variant', '')

        if gene == 'EGFR' and 'exon20' in ori_variant and 'delins' in ori_variant:
            cHGVS = item.get('cHGVS', '')
            del_part, ins_seq = cHGVS.split('delins')
            del_pos = del_part.split('.')[-1]
            start, end = del_pos.split('_')
            del_count = int(end) - int(start) + 1
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
        if gene == 'EGFR' and 'exon20' in ori_variant and 'delins' in ori_variant:
            cHGVS = item.get('cHGVS', '')
            del_part, ins_seq = cHGVS.split('delins')
            del_pos = del_part.split('.')[-1]
            start, end = del_pos.split('_')
            del_count = int(end) - int(start) + 1
            ins_count = len(ins_seq)
            if del_count < ins_count:
                item['ExonicFunc'] = '20号外显子插入突变'
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
                    tip = f"{gene} {extract_pure_digit(variant_split[1])}号基因内区突变"
                else:
                    tip = f"{gene} {ori_variant} 突变"

            if ori_variant1 == 'MET 14号外显子跳跃':
                tip += f'({ori_variant1})'

            if tip in category_1_variant_list:
                continue
            category_1_variant_list.append(tip)

    report_json['category_1_variant_list'] = category_1_variant_list


def process_WJM_tip(report_json):
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
