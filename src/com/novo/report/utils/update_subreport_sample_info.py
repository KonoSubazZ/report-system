# -*- coding: UTF-8 -*-
import json
import os
import sys

from docx import Document
from docx.oxml.ns import qn
from docx.shared import Pt


def normalize_label(text):
    return text.replace(" ", "").replace("\n", "").split("：", 1)[0].strip()


def set_cell_text(cell, label, value):
    text = cell.text
    if "：" not in text:
        return
    prefix = text.split("：", 1)[0]
    new_text = "%s： %s" % (prefix, value if value else "-")
    if not cell.paragraphs:
        cell.text = new_text
        return

    paragraph = cell.paragraphs[0]
    if paragraph.runs:
        run = paragraph.runs[0]
        run.text = new_text
        for extra_run in paragraph.runs[1:]:
            extra_run.text = ""
    else:
        run = paragraph.add_run(new_text)

    run.font.name = u"微软雅黑"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), u"微软雅黑")
    run.font.size = Pt(10.5)


def update_sample_info(docx_path, sample_info):
    document = Document(docx_path)
    updated = 0
    labels = set(sample_info.keys())

    for table in document.tables:
        table_text = "\n".join(cell.text for row in table.rows for cell in row.cells)
        if "样本信息" not in table_text and "姓名" not in table_text:
            continue
        for row in table.rows:
            for cell in row.cells:
                label = normalize_label(cell.text)
                if label in labels:
                    set_cell_text(cell, label, sample_info.get(label))
                    updated += 1

    if updated > 0:
        document.save(docx_path)
    return updated


def main():
    if len(sys.argv) != 3:
        print("小报告修改失败：参数错误")
        return 1

    docx_path = sys.argv[1]
    if not os.path.exists(docx_path):
        print("小报告修改失败：文件不存在 %s" % docx_path)
        return 1

    try:
        sample_info = json.loads(sys.argv[2])
        updated = update_sample_info(docx_path, sample_info)
        if updated == 0:
            print("小报告未找到样本信息字段")
        else:
            print("success: 小报告修改成功，更新%d个字段" % updated)
        return 0
    except Exception as exc:
        print("小报告修改失败：%s" % exc)
        return 1


if __name__ == "__main__":
    sys.exit(main())
