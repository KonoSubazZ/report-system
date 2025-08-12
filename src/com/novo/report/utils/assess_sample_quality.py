def assess_sample_quality(panel_type, sample_type, dna_sequencing_depth=0, total_reads=0, hrd_sequencing_depth=0):
    """
    评估样本质量

    参数:
    panel_type (str): 检测类型，可选值: "DNA", "RNA", "DNA_RNA", "WES", "DNA_HRD", "HRD"
    sample_type (str): 样本类型，可选值: "tissue" 或 "cfdna"
    dna_sequencing_depth (float): DNA测序深度
    total_reads (float): RNA总reads数
    hrd_sequencing_depth (float): HRD测序深度

    返回:
    str: 质量评估结果，可能值: "合格", "警戒", "不合格"
    """
    if panel_type == "DNA":
        # DNA-Panel评估逻辑
        depth = dna_sequencing_depth

        if sample_type == "tissue":
            if depth >= 500:
                return "合格"
            elif depth >= 400:
                return "警戒"
            else:
                return "不合格"
        else:  # cfDNA
            if depth >= 1500:
                return "合格"
            elif depth >= 1000:
                return "警戒"
            else:
                return "不合格"

    elif panel_type == "DNA+":
        # 188高深度血液
        depth = dna_sequencing_depth

        if depth >= 10000 and sample_type == "blood":
            return "合格"
        else:
            return "不合格"

    elif panel_type == "RNA":
        # RNA-Panel评估逻辑
        if total_reads >= 12000000:
            return "合格"
        elif total_reads >= 10000000:
            return "警戒"
        else:
            return "不合格"

    elif panel_type == "WES+":
        # WES评估逻辑
        depth = dna_sequencing_depth

        if depth >= 400:
            return "合格"
        elif depth >= 300:
            return "警戒"
        else:
            return "不合格"

    elif panel_type == "DNA_HRD":
        # DNA + HRD评估逻辑
        depth = dna_sequencing_depth

        if depth >= 500:
            return "合格"
        elif depth >= 400:
            return "警戒"
        else:
            return "不合格"

    elif panel_type == "HRD":
        # 单HRD评估逻辑
        depth = hrd_sequencing_depth

        if depth >= 200:
            return "合格"
        elif depth >= 150:
            return "警戒"
        else:
            return "不合格"

    elif panel_type == "DNA_RNA":
        # DNA_RNA组合评估逻辑
        # 评估DNA质量
        dna_result = assess_sample_quality("DNA", sample_type, dna_sequencing_depth)

        # 评估RNA质量
        rna_result = assess_sample_quality("RNA", sample_type, 0, total_reads)

        # 取最低质量结果
        # 质量等级排序: 不合格 < 警戒 < 合格
        if "不合格" in [dna_result, rna_result]:
            return "不合格"
        elif "警戒" in [dna_result, rna_result]:
            return "警戒"
        else:
            return "合格"

    elif panel_type == "SPECIAL_SINGLE_BRCA":
        # BRCA_12、BRCA_45 胚系质控逻辑
        depth = dna_sequencing_depth

        if depth >= 100:
            return "合格"
        elif depth >= 50:
            return "警戒"
        else:
            return "不合格"

    else:
        return "未知的panel类型"


# 使用示例
if __name__ == "__main__":
    # DNA示例 - 组织样本
    print(f"DNA (组织, 深度550): {assess_sample_quality('DNA', 'tissue', 550)}")

    # DNA示例 - cfDNA样本
    print(f"DNA (cfDNA, 深度1200): {assess_sample_quality('DNA', 'cfdna', 1200)}")

    # RNA示例
    print(f"RNA (reads 1100万): {assess_sample_quality('RNA', 'tissue', 0, 11000000)}")

    # WES示例
    print(f"WES (深度350): {assess_sample_quality('WES', 'tissue', 350)}")

    # DNA_HRD示例
    print(f"DNA_HRD (深度450): {assess_sample_quality('DNA_HRD', 'tissue', 450)}")

    # HRD示例
    print(f"HRD (深度180): {assess_sample_quality('HRD', 'tissue', 0, 0, 180)}")

    # DNA_RNA示例
    print(f"DNA_RNA (DNA合格, RNA警戒): {assess_sample_quality('DNA_RNA', 'tissue', 550, 11000000)}")

    print(f"DNA_RNA (DNA警戒, RNA不合格): {assess_sample_quality('DNA_RNA', 'tissue', 450, 9000000)}")
