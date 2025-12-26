import re
import json




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
def process_shanghaifeike_tip(report_json):

    shanghaifeike_tips_1 = []
    shanghaifeike_tips_2 = []
    shanghaifeike_tips_3 = []

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
            tip = f"{gene}基因{variant_split[0]}融合突变，变异丰度{mut_freq}。"

        elif "Amplification" in ori_variant:
            tip = f"{gene}基因扩增，拷贝数{mut_freq}"
        else:
            new_variant = ori_variant.replace(" ",":")
            ExonicFunc = item.get('ExonicFunc')
            if "p." in ori_variant:
                pattern = r'(p\.)([^:]+)'
                new_variant = re.sub(pattern, r'\1(\2)', new_variant)
            item['ori_variant'] = new_variant
            exon = ''.join(re.findall(r'[0-9]', variant_split[1]))
            desc = ""
            if "exon" in variant_split:
                desc = "外显子"
            else:
                desc = "内含子"
            c_idx = new_variant.find('c.')
            var = new_variant[c_idx:]
            tip = f"{gene}基因{exon}号{desc}{ExonicFunc}{var}，突变丰度为{mut_freq}。"

        if variationClass2 =="1":
            shanghaifeike_tips_1.append(tip)
        else:
            shanghaifeike_tips_2.append(tip)

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
            new_variant = ori_variant.replace(" ",":")
            ExonicFunc = item.get('ExonicFunc')
            if "p." in ori_variant:
                pattern = r'(p\.)([^:]+)'
                new_variant = re.sub(pattern, r'\1(\2)', new_variant)
            item['ori_variant'] = new_variant
            exon = ''.join(re.findall(r'[0-9]', variant_split[1]))
            desc = ""
            if "exon" in variant_split:
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

        for item in report_json.get('cancerTyping1166',[]):
            mut_freq_str = item.get('mut_freq','')

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

        for item in report_json.get('sarcomaTyping',[]):
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

        for item in report_json.get('unknownTipLineStr',[]):
            mut_freq_str = item.get('mutFreq')
            ori_variant = item.get('ori_variant','')

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

        for item in report_json.get('bodyDrugTipLineStr',[]):
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

        for item in report_json.get('BodyDrugNoComplexStr',[]):
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

            if (mut_freq < 0 or mut_freq > 1) and 'Fusion'  in ori_variant:
                BodyDrugNoComplexStrRNA.append(item)
            else:
                BodyDrugNoComplexStrDNA.append(item)

        report_json['BodyDrugNoComplexStrDNA'] = BodyDrugNoComplexStrDNA
        report_json['BodyDrugNoComplexStrRNA'] = BodyDrugNoComplexStrRNA


def process_tongji_tip(report_json):
    if report_json.get('BodyDrugNoComplexStr'):
        tongji_mutation1 = []
        tongji_mutation2 = []
        for item in report_json.get('BodyDrugNoComplexStr',[]):
            if item.get('variationClass2') == '1':
                tongji_mutation1.append(item)
            elif item.get('variationClass2') == '2':
                tongji_mutation2.append(item)

        report_json['tongji_mutation1'] = tongji_mutation1
        report_json['tongji_mutation2'] = tongji_mutation2

    # 免疫处理
    tongji_immune = []
    if report_json.get('positiveImmnue'):
        for item in report_json.get('positiveImmnue',[]):
            immune = {}
            ori_variant = item.get('variant')
            gene = item.get('gene')
            mut_freq = item.get('mutFreq')
            for item_body in report_json.get('BodyDrugNoComplexStr',[]):
                if gene == item_body.get('gene') and ori_variant == item_body.get('ori_variant') and mut_freq == item_body.get('mutFreq'):
                    immune['tongji_mutation'] = item.get('TJmutation')

            for item_body in report_json.get('unknownVarAnalysisStr',[]):
                if gene == item_body.get('gene') and ori_variant == item_body.get('ori_variant') and mut_freq == item_body.get('mutFreq'):
                    immune['tongji_mutation'] = item.get('TJmutation')


            if item.get('flag') == '1':
                immune['gene'] = item.get('gene')
                immune['relationship'] = '正相关'
                immune['result'] = '可能导致PD-1/PD-L1抑制剂获益率高'
            tongji_immune.append(immune)

    if report_json.get('negativeImmnue'):
        for item in report_json.get('negativeImmnue',[]):
            immune = {}
            ori_variant = item.get('variant')
            gene = item.get('gene')
            mut_freq = item.get('mutFreq')
            for item_body in report_json.get('BodyDrugNoComplexStr',[]):
                if gene == item_body.get('gene') and ori_variant == item_body.get('ori_variant') and mut_freq == item_body.get('mutFreq'):
                    immune['tongji_mutation'] = item.get('TJmutation')

            for item_body in report_json.get('unknownVarAnalysisStr',[]):
                if gene == item_body.get('gene') and ori_variant == item_body.get('ori_variant') and mut_freq == item_body.get('mutFreq'):
                    immune['tongji_mutation'] = item.get('TJmutation')


            if item.get('flag') == '2':
                immune['gene'] = item.get('gene')
                immune['relationship'] = '负相关'
                immune['result'] = '可能导致PD-1/PD-L1抑制剂获益率低'
            tongji_immune.append(immune)

    report_json['tongji_immune'] = tongji_immune

    # HRR
    tongji_hrr = ""
    HRR_info = report_json.get('HRRInfo')
    if HRR_info:
        HRR_genes = ["ATM", "BARD1", "BRCA1", "BRCA2", "BRIP1", "CDK12", "CHEK1", "CHEK2", "FANCL", "PALB2", "RAD51B", "RAD51C", "RAD51D", "RAD54L", "PPP2R2A"]
        for gene in HRR_genes:
            variant = HRR_info.get('variant')
            if variant != "-":
                pattern = r'p\.(\S+)'
                match = re.search(pattern, variant)
                if match:
                    p_part = match.group(1)
                    new_variant = f"p.({p_part})"
                else:
                    new_variant = variant
                tongji_hrr += gene + " " + new_variant + ";"






