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
            mut_freq = item.get('mut_freq')
            if '-' in mut_freq:
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
            mut_freq = item.get('mutFreq')
            if '-' in mut_freq:
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
            mut_freq = item.get('mutFreq')
            ori_variant = item.get('ori_variant')
            if '-' in mut_freq and 'Amplification' not in ori_variant:
               unknownTipLineStrRNA.append(item)
            else:
                unknownTipLineStrDNA.append(item)
        
        report_json['unknownTipLineStrDNA'] = unknownTipLineStrDNA
        report_json['unknownTipLineStrRNA'] = unknownTipLineStrRNA

    if report_json.get('bodyDrugTipLineStr'):
        bodyDrugTipLineStrDNA = []
        bodyDrugTipLineStrRNA = []

        for item in report_json.get('bodyDrugTipLineStr',[]):
            mut_freq = item.get('mutFreq')
            ori_variant = item.get('ori_variant')
            if '-' in mut_freq and 'Amplification' not in ori_variant:
               bodyDrugTipLineStrRNA.append(item)
            else:
                bodyDrugTipLineStrDNA.append(item)
        
        report_json['bodyDrugTipLineStrDNA'] = bodyDrugTipLineStrDNA
        report_json['bodyDrugTipLineStrRNA'] = bodyDrugTipLineStrRNA
    
    if report_json.get('BodyDrugNoComplexStr'):
        BodyDrugNoComplexStrDNA = []
        BodyDrugNoComplexStrRNA = []

        for item in report_json.get('BodyDrugNoComplexStr',[]):
            mut_freq = item.get('mutFreq')
            ori_variant = item.get('ori_variant')
            if '-' in mut_freq and 'Amplification' not in ori_variant:
               BodyDrugNoComplexStrRNA.append(item)
            else:
                BodyDrugNoComplexStrDNA.append(item)
        
        report_json['BodyDrugNoComplexStrDNA'] = BodyDrugNoComplexStrDNA
        report_json['BodyDrugNoComplexStrRNA'] = BodyDrugNoComplexStrRNA