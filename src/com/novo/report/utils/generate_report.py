import base64
import json
import os
import tempfile
import shutil
import jinja2
import lxml
import six
import sys
import math
from docx import Document
from docx.shared import Mm, Pt
from io import BytesIO
# from docxtpl import DocxTemplate, R, RichText, InlineImage, NEWLINE_XML, NEWPARAGRAPH_XML, TAB_XML, PAGE_BREAK, Listing
specific_version_path = "/root/python3-packages"
sys.path.insert(0, specific_version_path)
# import docxtpl
# from docxtpl import DocxTemplate, R, RichText, InlineImage, NEWPARAGRAPH_XML, TAB_XML, PAGE_BREAK, Listing
from docxtpl import DocxTemplate, R, RichText, InlineImage
import time
from unicodedata import name
from six import iteritems, text_type

try:
    from html import escape
except ImportError:
    # cgi.escape is deprecated in python 3.7
    from cgi import escape

class MyRichText(RichText):
    def add(self, text,
            style=None,
            color=None,
            highlight=None,
            size=None,
            subscript=None,
            superscript=None,
            bold=False,
            italic=False,
            underline=False,
            strike=False,
            cnfont=None,
            font=None,
            url_id=None):

        # If a RichText is added
        if font and cnfont is None:
            cnfont = font
        if isinstance(text, RichText):
            self.xml += text.xml
            return

        # If not a string : cast to string (ex: int, dict etc...)
        if not isinstance(text, (six.text_type, six.binary_type)):
            text = six.text_type(text)
        if not isinstance(text, six.text_type):
            text = text.decode('utf-8', errors='ignore')
            text = (escape(text))
#         text = (escape(text)
#                 .replace('\n', NEWLINE_XML)
#                 .replace('\a', NEWPARAGRAPH_XML)
#                 .replace('\t', TAB_XML)
#                 .replace('\f', PAGE_BREAK))

        prop = u''

        if style:
            prop += u'<w:rStyle w:val="%s"/>' % style
        if color:
            if color[0] == '#':
                color = color[1:]
            prop += u'<w:color w:val="%s"/>' % color
        if highlight:
            if highlight[0] == '#':
                highlight = highlight[1:]
            prop += u'<w:highlight w:val="%s"/>' % highlight
        if size:
            prop += u'<w:sz w:val="%s"/>' % size
            prop += u'<w:szCs w:val="%s"/>' % size
        if subscript:
            prop += u'<w:vertAlign w:val="subscript"/>'
        if superscript:
            prop += u'<w:vertAlign w:val="superscript"/>'
        if bold:
            prop += u'<w:b/>'
        if italic:
            prop += u'<w:i/>'
        if underline:
            if underline not in ['single', 'double']:
                underline = 'single'
            prop += u'<w:u w:val="%s"/>' % underline
        if strike:
            prop += u'<w:strike/>'
        if font:
            prop += (u'<w:rFonts w:ascii="{font}" w:hAnsi="{font}" w:cs="{font}" w:eastAsia="{cnfont}"/>'
                     .format(font=font,cnfont=cnfont))
            # prop += (u'<w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:eastAsia="微软雅黑" w:cs="Times New Roman"/>')

        xml = u'<w:r>'
        if prop:
            xml += u'<w:rPr>%s</w:rPr>' % prop
        xml += u'<w:t xml:space="preserve">%s</w:t></w:r>' % text
        if url_id:
            xml = (u'<w:hyperlink r:id="%s" w:tgtFrame="_blank">%s</w:hyperlink>'
                   % (url_id, xml))
        self.xml += xml

# @deprecated To be removed
class MyRichTextV1(RichText):
    def add(self, text,
            style=None,
            color=None,
            highlight=None,
            size=None,
            subscript=False,
            superscript=False,
            bold=False,
            italic=False,
            underline=False,
            strike=False,
            cnfont=None,
            font=None,
            url_id=None):

        if isinstance(text, RichText):
            self.xml += text.xml
            return

        if font and cnfont is None:
            cnfont = font
        if not isinstance(text, str):
            text = str(text)
        text = escape(text)

        prop = self._build_run_properties(
            style, color, highlight, size, subscript, superscript,
            bold, italic, underline, strike, cnfont, font
        )

        run_xml = f'<w:r>'
        if prop:
            run_xml += f'<w:rPr>{prop}</w:rPr>'
        run_xml += f'<w:t xml:space="preserve">{text}</w:t></w:r>'

        if url_id:
            run_xml = f'<w:hyperlink r:id="{url_id}" w:tgtFrame="_blank">{run_xml}</w:hyperlink>'

        self.xml += run_xml

    def _build_run_properties(self, style, color, highlight, size,
                              subscript, superscript, bold, italic,
                              underline, strike, cnfont, font):
        props = []

        if style:
            props.append(f'<w:rStyle w:val="{style}"/>')
        if color:
            props.append(f'<w:color w:val="{color.lstrip("#")}"/>')
        if highlight:
            props.append(f'<w:highlight w:val="{highlight.lstrip("#")}"/>')
        if size:
            props.append(f'<w:sz w:val="{size}"/><w:szCs w:val="{size}"/>')
        if subscript:
            props.append('<w:vertAlign w:val="subscript"/>')
        if superscript:
            props.append('<w:vertAlign w:val="superscript"/>')
        if bold:
            props.append('<w:b/>')
        if italic:
            props.append('<w:i/>')
        if underline:
            props.append(f'<w:u w:val="{underline if underline in ["single", "double"] else "single"}"/>')
        if strike:
            props.append('<w:strike/>')
        if font:
            props.append(
                f'<w:rFonts w:ascii="{font}" w:hAnsi="{font}" w:cs="{font}" w:eastAsia="{cnfont or font}"/>'
            )

        return ''.join(props)


def check_contain_chinese(check_str):
    for ch in check_str:
        if u'\u4e00' <= ch <= u'\u9fff':
            return True
        else:
            return False




def mystyle(value,bold,highlight=False):
    if highlight:
        return MyRichText(value,bold=bold,cnfont='微软雅黑', font='Times New Roman', size=18,highlight='lightGray')
    else:
        return MyRichText(value,bold=bold,cnfont='微软雅黑', font='Times New Roman', size=18)

def mystyle2(value,bold,size,cnfont,font,highlight=False):
    if highlight:
        return MyRichText(value,bold=bold,cnfont=cnfont, font=font, size=size,highlight='lightGray')
    else:
        return MyRichText(value,bold=bold,cnfont=cnfont, font=font, size=size)

def mystyleSong(value,bold,highlight=False):
    if highlight:
        return MyRichText(value,bold=bold,cnfont='宋体', font='Times New Roman', size=18,highlight='lightGray')
    else:
        return MyRichText(value,bold=bold,cnfont='宋体', font='Times New Roman', size=18)

def mystyleSong2(value,bold,highlight=False):
    if highlight:
        return MyRichText(value,bold=bold,cnfont='宋体', font='Times New Roman', size=21,highlight='lightGray')
    else:
        return MyRichText(value,bold=bold,cnfont='宋体', font='Times New Roman', size=21)

def myimage(value):
    imgdata = base64.b64decode(value)
    file = open('a.png', 'wb')
    file.write(imgdata)
    file.close()
    myimage = InlineImage(tpl, 'a.png', width=Pt(283.5), height=Pt(225))
    return myimage

def pdimage(value,width,height):
    imgdata = base64.b64decode(value)
    image_stream = BytesIO(imgdata)
    pdimage = InlineImage(tpl, image_stream, width=Pt(width), height=Pt(height))
    return pdimage

def currencyimage(value,width,height):
    imgdata = base64.b64decode(value)
    file = open('aa.png', 'wb')
    file.write(imgdata)
    file.close()
    currencyimage = InlineImage(tpl, 'aa.png', width=Pt(width), height=Pt(height))
    return currencyimage

def red_gene(value,line_num,size=18,italic=True):
    red_list = []
    b = value.split(',')
    for i in range(math.ceil(len(b)/int(line_num))):
        line =  b[i*int(line_num):(i+1)*int(line_num)]
        if len(b[i*int(line_num):(i+1)*int(line_num)]) < int(line_num):
            for x in range(int(line_num)-len(b[i*int(line_num):(i+1)*int(line_num)])):
                line.append('')
        new_line = []
        for li in line:
            if li.replace('*','') in GENE_LIST:
                new_line.append(MyRichText(li,color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
            else:
                new_line.append(MyRichText(li,cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
        red_list.append(new_line)
    return red_list

def red_gene2(value,line_num,size,cnfont,font,italic=True):
    red_list = []
    b = value.split(',')
    for i in range(math.ceil(len(b)/int(line_num))):
        line =  b[i*int(line_num):(i+1)*int(line_num)]
        if len(b[i*int(line_num):(i+1)*int(line_num)]) < int(line_num):
            for x in range(int(line_num)-len(b[i*int(line_num):(i+1)*int(line_num)])):
                line.append('')
        new_line = []
        for li in line:
            if li.replace('*','') in GENE_LIST:
                new_line.append(MyRichText(li,color='#ff0000',cnfont=cnfont, font=font, size=size,italic=italic))
            else:
                new_line.append(MyRichText(li,cnfont=cnfont, font=font, size=size,italic=italic))
        red_list.append(new_line)
    return red_list

def red_bodyGene(value,line_num,size=18,italic=True):
    red_list = []
    b = value.split(',')
    for i in range(math.ceil(len(b)/int(line_num))):
        line =  b[i*int(line_num):(i+1)*int(line_num)]
        if len(b[i*int(line_num):(i+1)*int(line_num)]) < int(line_num):
            for x in range(int(line_num)-len(b[i*int(line_num):(i+1)*int(line_num)])):
                line.append('')
        new_line = []
        for li in line:
            if li.replace('*','') in BodyGene_LIST:
                new_line.append(MyRichText(li,color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
            else:
                new_line.append(MyRichText(li,cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
        red_list.append(new_line)
    return red_list


def red_embryonalGene(value,line_num,size=18,italic=True):
    red_list = []
    b = value.split(',')
    for i in range(math.ceil(len(b)/int(line_num))):
        line =  b[i*int(line_num):(i+1)*int(line_num)]
        if len(b[i*int(line_num):(i+1)*int(line_num)]) < int(line_num):
            for x in range(int(line_num)-len(b[i*int(line_num):(i+1)*int(line_num)])):
                line.append('')
        new_line = []
        for li in line:
            if li.replace('*','') in EmbryonalGene_LIST:
                new_line.append(MyRichText(li,color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
            else:
                new_line.append(MyRichText(li,cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
        red_list.append(new_line)
    return red_list


def red_chemoGene(value,line_num,size=18,italic=True):
    red_list = []
    b = value.split(',')
    for i in range(math.ceil(len(b)/int(line_num))):
        line =  b[i*int(line_num):(i+1)*int(line_num)]
        if len(b[i*int(line_num):(i+1)*int(line_num)]) < int(line_num):
            for x in range(int(line_num)-len(b[i*int(line_num):(i+1)*int(line_num)])):
                line.append('')
        new_line = []
        for li in line:
            if li.replace('*','') in ChemoGene_LIST:
                new_line.append(MyRichText(li,color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
            else:
                new_line.append(MyRichText(li,cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
        red_list.append(new_line)
    return red_list


def genes(value,line_num,size=18,italic=True):
    genes = []
    b = value.split(',')
    for i in range(math.ceil(len(b)/int(line_num))):
        line =  b[i*int(line_num):(i+1)*int(line_num)]
        if len(b[i*int(line_num):(i+1)*int(line_num)]) < int(line_num):
            for x in range(int(line_num)-len(b[i*int(line_num):(i+1)*int(line_num)])):
                line.append('')
        new_line = []
        for li in line:
            new_line.append(MyRichText(li,cnfont='微软雅黑', font='Times New Roman', size=size,italic=italic))
        genes.append(new_line)
    return genes


def cancerRisk(value):
    red_list = "普通风险"
    b = value.split(',')
    for li in b:
        if li.replace('*','') in CancerRiskGene_LIST:
            red_list = MyRichText("风险升高",color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size='18')
            break
    return red_list


def detectionMutation(value):
    red_list = "阴性"
    if value in DetectionMutation_LIST:
        red_list = MyRichText("阳性",color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size='28')
    return red_list


def promoteGene(value):
    s = "未检出"
    if value in PromoteGene_LIST:
        s = "检出"
    return s


def reducedGene(value):
    s = "未检出"
    if value in ReducedGene_LIST:
        s = "检出"
    return s


def progressionGene(value):
    s = "未检出"
    if value in ProgressionGene_LIST:
        s = "检出"
    return s


def parpinhibitorGene(value):
    s = "未检出"
    if value in ParpinhibitorGene_LIST:
        s = "检出"
    return s


def predictorGene(value):
    s = "-"
    if value in PredictorGene_LIST:
        s = "检出"
    return s


def immunopositiveGene(value):
    s = "-"
    if value in ImmunopositiveGene_LIST:
        s = "检出"
    return s


def immunonegativeGene(value):
    s = "-"
    if value in ImmunonegativeGene_LIST:
        s = "检出"
    return s


def overallQualityAssessment(value,qualified,alert):
    overall_quality_assessment = ""
    if len(value) != 0:
        value = str(value).replace('X','')
        if float(value) >= float(qualified):
            overall_quality_assessment = "合格"
        elif float(value) >= float(alert):
            overall_quality_assessment = "警戒"
        else:
            overall_quality_assessment = "不合格"
    return overall_quality_assessment


def newline(value):
    return value.split("\\r\\n")


def newBold(value):
    return value.split("|")


def split(value,regex):
    return value.split(regex)


def markInRed(value):
    if value == "阳性" or value == "检出":
        value = MyRichText(value,color='#ff0000',cnfont='微软雅黑', font='Times New Roman', size='21')
    return value

def set_updatefields_true(docx_path):
    """ Opens the docx and adds <w:updateFields w:val="true"/> to
       (docx_path)/word/settings.xml to enforce update of TOC (and
       other fields marked as dirty) on first open.
       Saves the file afterwards.

    Arguments:
        docx_path {str} -- Absolute path to docx
    Returns:
        Nothing
    """
    namespace = "{http://schemas.openxmlformats.org/wordprocessingml/2006/main}"
    # namespace = "{http://schemas.microsoft.com/office/word/2003/wordml}"
    doc = Document(docx_path)
    # doc.updateFields()
    # add child to doc.settings element
    element_updatefields = lxml.etree.SubElement(
        doc.settings.element, namespace+"updateFields"
    )
    element_updatefields.set(namespace+"val", "true")
    doc.save(docx_path)

# 20250310 扁平化数据
def flatten_data(data):
    result = []
    for item in data:
        desc2s = item['desc2'].split(',')
        result.append({'desc1': item['desc1'], 'desc2': desc2s})
    return result
def load_template_config(config_path="report_config.json"):
    try:
        # 获取当前 Python 文件所在目录
        base_dir = os.path.dirname(os.path.abspath(__file__))
        full_path = os.path.join(base_dir, config_path)

        with open(full_path, 'r', encoding='utf-8') as f:
            config = json.load(f)

    except Exception as e:
        print(f"❌ 加载配置失败: {e}")
        sys.exit(1)

    return config
def determine_template_file(template_name, config):
    single_common = config["common_templates"]["single_sample"]
    double_common = config["common_templates"]["double_sample"]

    single_list = set(config["advanced_templates"]["single_sample"])
    double_list = set(config["advanced_templates"]["double_sample"])

    if template_name in double_list:
        return double_common
    elif template_name in single_list:
        return single_common
    else:
        return f"{template_name}.docx"

def load_template_safely(tpl_path):
    tmp_tpl_file = tempfile.NamedTemporaryFile(delete=False, suffix=".docx")
    shutil.copy2(tpl_path, tmp_tpl_file.name)
    tpl = DocxTemplate(tmp_tpl_file.name)
    return tpl, tmp_tpl_file.name

def safe_get(d, key, default=None):
    return d.get(key, default)

def mark_genes_in_red(gene_tables, detected_gene_info):
    detected_mapping = {
        "TARGET": "target_drug_gene_list",
        "CR": "cr_gene_list",
        "FUSION": "fusion_gene_list",
        "IMMUNE": "immune_gene_list",
        "CNV": "cnv_gene_list",
        "ALL": "all_gene_list",
        # 你还可以根据 SNP 做一个标红 if needed
    }
    for table in gene_tables:
        gene_type = table["type"]
        gene_list = table["genes"]
        detected_gene_list = detected_gene_info.get(detected_mapping.get(gene_type, ""), [])
        add_gene_rich_text(gene_list, detected_gene_list)

def add_gene_rich_text(gene_list, detected_gene_list):
    for row_idx, row in enumerate(gene_list):
        for col_idx, gene in enumerate(row):
            if gene in detected_gene_list:
                gene_list[row_idx][col_idx] = MyRichText(gene, color='#ff0000', cnfont='微软雅黑', font='Times New Roman', size='18',italic=italic)
            else:
                gene_list[row_idx][col_idx] = MyRichText(gene, cnfont='微软雅黑', font='Times New Roman', size='18',italic=italic)


if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("Usage: python3 {} TemplateWord JsonInfoPath OutputWord".format(__file__))
    try:

        # 例: python3 generate_report.py \
        #             /path/to/模板文件.docx \
        #             /path/to/tempJson.json \
        #             /path/to/输出文件.docx
        # init py脚本参数
        input_template_path = sys.argv[1]
        json_path = sys.argv[2]
        output_path = sys.argv[3]

        # 加载模块化配置文件
        config = load_template_config()
        enabled = config.get("module_enabled", False)

        # 是否使用模块化模板
        if enabled:
            template_name = os.path.basename(input_template_path).replace(".docx", "")
            selected_template_file = determine_template_file(template_name, config)
            tpl_path = os.path.join(os.path.dirname(input_template_path), selected_template_file)

            print(f"[INFO] 匹配到的模板名: {template_name}")
            print(f"[INFO] 实际使用的模板文件: {selected_template_file}")
            print(f"[INFO] 实际模板路径: {tpl_path}")
        else:
            tpl_path = input_template_path
            print(f"[INFO] 未启用模板替换逻辑，直接使用输入路径模板: {tpl_path}")

        # 加载模板
        tpl = DocxTemplate(tpl_path)

        # 加载输出文件
        f = open(sys.argv[2], encoding='utf-8')
        info_json = json.load(f)

        # 初始化 【pyfn】 使用变量
        # 所有检出基因列表-标红
        GENE_LIST = info_json['allGeneSet'] if info_json['allGeneSet'] else []
        # 体系基因列表-标红
        BodyGene_LIST = info_json['bodyGeneSet'] if info_json['bodyGeneSet'] else []
        # 胚系（所有）基因列表-标红
        EmbryonalGene_LIST = info_json['embryonalGeneSet'] if info_json['embryonalGeneSet'] else []
        # 化疗基因列表-标红
        ChemoGene_LIST = info_json['chemoGeneSet'] if info_json['chemoGeneSet'] else []
        # 胚系（致病1、2）基因列表-标红
        CancerRiskGene_LIST = info_json['cancerRiskGene'] if info_json['cancerRiskGene'] else []
        # 单基因多基因模板逻辑
        DetectionMutation_LIST = info_json['detectionMutationSet'] if info_json['detectionMutationSet'] else []
        # 个性化-华西模板逻辑  可能促进药物效果标志物、可能导致药物效果降低标志物、可能导致疾病发生超进展标志物、PARP抑制剂相关基因检测结果
        # PromoteGene_LIST = info_json['promoteGeneSet'] if info_json['promoteGeneSet'] else []
        # ReducedGene_LIST = info_json['reducedGeneSet'] if info_json['reducedGeneSet'] else []
        # ProgressionGene_LIST = info_json['progressionGeneSet'] if info_json['progressionGeneSet'] else []
        # ParpinhibitorGene_LIST = info_json['parpinhibitorGeneSet'] if info_json['parpinhibitorGeneSet'] else []
        # PredictorGene_LIST = info_json['predictorGeneSet'] if info_json['predictorGeneSet'] else []
        PromoteGene_LIST = safe_get(info_json, 'promoteGeneSet', [])
        ReducedGene_LIST = safe_get(info_json, 'reducedGeneSet', [])
        ProgressionGene_LIST = safe_get(info_json, 'progressionGeneSet', [])
        ParpinhibitorGene_LIST = safe_get(info_json, 'parpinhibitorGeneSet', [])
        PredictorGene_LIST = safe_get(info_json, 'predictorGeneSet', [])
        # 个性化-赛福免疫正负相关基因是否检出
        # ImmunopositiveGene_LIST = info_json['immunopositiveGeneSet'] if info_json['immunopositiveGeneSet'] else []
        # ImmunonegativeGene_LIST = info_json['immunonegativeGeneSet'] if info_json['immunonegativeGeneSet'] else []

        ImmunopositiveGene_LIST = safe_get(info_json, 'immunopositiveGeneSet', [])
        ImmunonegativeGene_LIST = safe_get(info_json, 'immunonegativeGeneSet', [])
        # 肉瘤附录列表逻辑
        if 'sarcomaTypingList1' in info_json.get('note', {}) and info_json['note']['sarcomaTypingList1']:
            info_json['note']['sarcomaTypingList1'] = flatten_data(info_json['note']['sarcomaTypingList1'])
            info_json['note']['sarcomaTypingList2'] = flatten_data(info_json['note']['sarcomaTypingList2'])
            info_json['note']['sarcomaTypingList3'] = flatten_data(info_json['note']['sarcomaTypingList3'])
            info_json['note']['sarcomaTypingList4'] = flatten_data(info_json['note']['sarcomaTypingList4'])

        # 反序列化 conf_genes
        if 'gene' in info_json and 'conf_genes' in info_json['gene']:
            conf_genes_str = info_json['gene']['conf_genes']
            deserialized_data = json.loads(conf_genes_str)
            info_json['gene']['conf_genes'] = deserialized_data
            detected_gene_info = info_json['reportInfo']['detected_gene_info']
            gene_tables = info_json['gene']['conf_genes']['gene_tables']
            mark_genes_in_red(gene_tables, detected_gene_info)

        # 模板init过滤器
        jinja_env = jinja2.Environment()
        jinja_env.filters['ms'] = mystyle
        jinja_env.filters['ms2'] = mystyle2
        jinja_env.filters['mss'] = mystyleSong
        jinja_env.filters['mss2'] = mystyleSong2
        jinja_env.filters['mi'] = myimage
        jinja_env.filters['pdi'] = pdimage
        jinja_env.filters['ci'] = currencyimage
        jinja_env.filters['red'] = red_gene
        jinja_env.filters['red2'] = red_gene2
        jinja_env.filters['redBody'] = red_bodyGene
        jinja_env.filters['redEmbryonal'] = red_embryonalGene
        jinja_env.filters['redChemo'] = red_chemoGene
        jinja_env.filters['genes'] = genes
        jinja_env.filters['cancerRisk'] = cancerRisk
        jinja_env.filters['detectionMutation'] = detectionMutation
        jinja_env.filters['promoteGene'] = promoteGene
        jinja_env.filters['reducedGene'] = reducedGene
        jinja_env.filters['progressionGene'] = progressionGene
        jinja_env.filters['parpinhibitorGene'] = parpinhibitorGene
        jinja_env.filters['predictorGene'] = predictorGene
        jinja_env.filters['immunopositiveGene'] = immunopositiveGene
        jinja_env.filters['immunonegativeGene'] = immunonegativeGene
        jinja_env.filters['oqa'] = overallQualityAssessment
        jinja_env.filters['nl'] = newline
        jinja_env.filters['nb'] = newBold
        jinja_env.filters['split'] = split
        jinja_env.filters['mr'] = markInRed

        #tpl.add_page_break()
        tpl.render(info_json, jinja_env,autoescape=True)
        tpl.save(sys.argv[3])
        set_updatefields_true(sys.argv[3])
    except Exception as e:
        raise e


