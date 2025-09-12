package com.itssky.common.utils.poi;


import lombok.Data;

import java.util.List;

/**
 * Excel表头节点
 * 用于动态构建复杂表头结构
 */
@Data
public class ExcelHeaderNode {

    /**
     * 节点名称（显示文本）
     */
    private String name;

    /**
     * 跨列数（默认为1）
     */
    private int colSpan = 1;

    /**
     * 跨行数（默认为1）
     */
    private int rowSpan = 1;

    /**
     * 子节点列表（用于多级表头）
     */
    private List<ExcelHeaderNode> children;

    /**
     * 数据字段名（仅叶子节点需要）
     */
    private String fieldName;

    public ExcelHeaderNode(String name) {
        this.name = name;
    }

    public ExcelHeaderNode(String name, int colSpan) {
        this.name = name;
        this.colSpan = colSpan;
    }

    public ExcelHeaderNode(String name, String fieldName) {
        this.name = name;
        this.fieldName = fieldName;
    }

    // 简化构建方法
    public static ExcelHeaderNode of(String name) {
        return new ExcelHeaderNode(name);
    }

    public static ExcelHeaderNode of(String name, int colSpan) {
        return new ExcelHeaderNode(name, colSpan);
    }

    public static ExcelHeaderNode of(String name, String fieldName) {
        return new ExcelHeaderNode(name, fieldName);
    }
}
