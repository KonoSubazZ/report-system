import base64
import json

import jinja2
import lxml
import six
import sys
import math
from docx import Document
from docx.shared import Mm, Pt
from docxtpl import DocxTemplate, R, RichText, InlineImage, NEWLINE_XML, NEWPARAGRAPH_XML, TAB_XML, PAGE_BREAK, Listing
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


def myimage(value):
    imgdata = base64.b64decode(value)
    file = open('aa.png', 'wb')
    file.write(imgdata)
    file.close()

    myimage = InlineImage(tpl, 'aa.png', width=Pt(283.5), height=Pt(225))
    return myimage
	

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
        GENE_LIST = info_json['allGeneSet'] if info_json['allGeneSet'] else []
        jinja_env = jinja2.Environment()
        jinja_env.filters['ms'] = mystyle
        jinja_env.filters['mi'] = myimage
        jinja_env.filters['red'] = red_gene
        #tpl.add_page_break()
        tpl.render(info_json, jinja_env,autoescape=True)
        tpl.save(sys.argv[3])
        set_updatefields_true(sys.argv[3])
    except Exception as e:
        raise e


