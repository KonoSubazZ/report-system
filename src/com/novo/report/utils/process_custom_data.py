import re

def process_custom_tip(report_json):

    customer = report_json.get('customer')
    if customer == 'shanghaifeike':
        process_shanghaifeike_tip(report_json)

def process_shanghaifeike_tip(report_json):
   
    shanghaifeike_tips_1 = []
    shanghaifeike_tips_2 = []
    shanghaifeike_tips_3 = []
    for item in report_json.get('BodyDrugNoComplexStr', []):
        gene = item.get('gene')
        ori_variant = item.get('ori_variant')
        mut_freq = item.get('mutFreq')
        variationClass2 = item.get('variationClass2')
        variant_split = ori_variant.split(" ")
        tip = ""
        if "Fusion" in ori_variant:
            tip = f"{gene}基因{variant_split[0]}融合突变，变异丰度{mut_freq}"
            
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
            tip = f"{gene}基因{exon}号{desc}{ExonicFunc}{var}，突变丰度为{mut_freq}"
        
        if variationClass2 =="1":
            shanghaifeike_tips_1.append(tip)
        else:
            shanghaifeike_tips_2.append(tip)
    
    for item in report_json.get('unknownVarAnalysisStr', []):
        gene = item.get('gene')
        ori_variant = item.get('ori_variant')
        mut_freq = item.get('mutFreq')
        variationClass2 = item.get('variationClass2')
        variant_split = ori_variant.split(" ")
        tip = ""
        if "Fusion" in ori_variant:
            tip = f"{gene}基因{variant_split[0]}融合突变，变异丰度{mut_freq}"
            
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
            tip = f"{gene}基因{exon}号{desc}{ExonicFunc}{var}，突变丰度为{mut_freq}"
            shanghaifeike_tips_3.append(tip)
            
    report_json['shanghaifeike_tips_1'] = shanghaifeike_tips_1
    report_json['shanghaifeike_tips_2'] = shanghaifeike_tips_2
    report_json['shanghaifeike_tips_3'] = shanghaifeike_tips_3

