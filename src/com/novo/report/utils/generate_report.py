import base64
import json

import jinja2
import lxml
import six
import sys
import math
from docx import Document
from docx.shared import Mm, Pt
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
        text = (escape(text)
                .replace('\n', NEWLINE_XML)
                .replace('\a', NEWPARAGRAPH_XML)
                .replace('\t', TAB_XML)
                .replace('\f', PAGE_BREAK))

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

# start = time.time()
# tpl=DocxTemplate('test0613.docx')
# f = open('test.json', encoding='utf-8')
# info_json = json.load(f)


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
    file = open('aa.jpg', 'wb')
    file.write(imgdata)
    file.close()
    pdimage = InlineImage(tpl, 'aa.jpg', width=Pt(width), height=Pt(height))
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

# jinja_env = jinja2.Environment()
# jinja_env.filters['ms'] = mystyle
# jinja_env.filters['mi'] = myimage
# tpl.add_page_break()
# print("渲染前: %f" % (time.time() - start))
# tpl.render(info_json, jinja_env)
# print("渲染后: %f" % (time.time() - start))
# tpl.save('out0613.docx')
# set_updatefields_true('out0613.docx')
# print("总耗时: %f" % (time.time() - start))

if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("Usage: python3 {} TemplateWord JsonInfoPath OutputWord".format(__file__))
    try:
        tpl = DocxTemplate(sys.argv[1])
        f = open(sys.argv[2], encoding='utf-8')
        info_json = json.load(f)
        # 在页眉中插入图片
        # header_image = info_json['pageHeaderPic']
        # if header_image is not "":
        #     header = tpl.sections[0].header
        #     header.paragraphs[0].add_run().add_picture(header_image, width=Pt(492.6), height=Pt(38))
        GENE_LIST = info_json['allGeneSet'] if info_json['allGeneSet'] else []
        BodyGene_LIST = info_json['bodyGeneSet'] if info_json['bodyGeneSet'] else []
        EmbryonalGene_LIST = info_json['embryonalGeneSet'] if info_json['embryonalGeneSet'] else []
        ChemoGene_LIST = info_json['chemoGeneSet'] if info_json['chemoGeneSet'] else []
        CancerRiskGene_LIST = info_json['cancerRiskGene'] if info_json['cancerRiskGene'] else []
        DetectionMutation_LIST = info_json['detectionMutationSet'] if info_json['detectionMutationSet'] else []
        PromoteGene_LIST = info_json['promoteGeneSet'] if info_json['promoteGeneSet'] else []
        ReducedGene_LIST = info_json['reducedGeneSet'] if info_json['reducedGeneSet'] else []
        ProgressionGene_LIST = info_json['progressionGeneSet'] if info_json['progressionGeneSet'] else []
        ParpinhibitorGene_LIST = info_json['parpinhibitorGeneSet'] if info_json['parpinhibitorGeneSet'] else []
        PredictorGene_LIST = info_json['predictorGeneSet'] if info_json['predictorGeneSet'] else []
        ImmunopositiveGene_LIST = info_json['immunopositiveGeneSet'] if info_json['immunopositiveGeneSet'] else []
        ImmunonegativeGene_LIST = info_json['immunonegativeGeneSet'] if info_json['immunonegativeGeneSet'] else []
        if 'sarcomaTypingList1' in info_json.get('note', {}) and info_json['note']['sarcomaTypingList1']:
            info_json['note']['sarcomaTypingList1'] = flatten_data(info_json['note']['sarcomaTypingList1'])
            info_json['note']['sarcomaTypingList2'] = flatten_data(info_json['note']['sarcomaTypingList2'])
            info_json['note']['sarcomaTypingList3'] = flatten_data(info_json['note']['sarcomaTypingList3'])
            info_json['note']['sarcomaTypingList4'] = flatten_data(info_json['note']['sarcomaTypingList4'])


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


