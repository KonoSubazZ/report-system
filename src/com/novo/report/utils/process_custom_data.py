import re
import json

def process_custom_tip(report_json):

    template_name = report_json.get('template_name')
    
    # 上海肺科
    if template_name == '肺癌60基因报告-上海肺科':
        process_shanghaifeike_tip(report_json)
    
    # 山肿胚系
    if template_name == '实体瘤1238DNA+1166RNA基因检测报告-山肿':
        process_shanzhong_tip(report_json)

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
        


if __name__ == "__main__":
    # 测试参数
    with open('/data/soft/apache-tomcat-8.5.43/temp/shanzhong.json', "r", encoding="utf-8") as f:
        test = json.load(f)
    result = process_custom_tip(test)

    # 打印结果
    print("\n" + "="*50)
    print("样本导出结果：")
    # print(f"成功状态：{result['success']}")
    # print(f"提示信息：{result['message']}")
    print("="*50)

