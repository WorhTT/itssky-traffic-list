package com.itssky.common.utils.poi;

import com.itssky.common.config.ItsskyConfig;
import com.itssky.common.core.domain.AjaxResult;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * 动态表头Excel导出工具 - 通用版本
 * 支持任意复杂的表头结构和动态跨列
 */
@Slf4j
public class DynamicHeaderExcelExporter {

    /**
     * 表头单元格定义内部类
     * 用于描述Excel表头中每个单元格的属性和结构
     */
    @Setter
    @Getter
    public static class HeaderCell {
        // Getter 和 Setter 方法
        /** 显示名称 */
        private String name;
        /** 字段名（对应数据对象的属性名） */
        private String fieldName;
        /** 跨行数 */
        private int rowSpan = 1;
        /** 跨列数 */
        private int colSpan = 1;
        /** 起始列位置（用于精确定位） */
        private int startColumn = 0;
        /** 子节点列表（用于多级表头） */
        private List<HeaderCell> children;
        /** 数据类型（可选，用于特殊格式处理） */
        private Class<?> dataType = String.class;
        /** 样式配置（可选） */
        private CellStyleConfig styleConfig;

        /**
         * 简单构造函数
         * @param name 显示名称
         * @param fieldName 字段名
         */
        public HeaderCell(String name, String fieldName) {
            this.name = name;
            this.fieldName = fieldName;
        }

        /**
         * 完整构造函数
         * @param name 显示名称
         * @param fieldName 字段名
         * @param rowSpan 跨行数
         * @param colSpan 跨列数
         */
        public HeaderCell(String name, String fieldName, int rowSpan, int colSpan) {
            this.name = name;
            this.fieldName = fieldName;
            this.rowSpan = rowSpan;
            this.colSpan = colSpan;
        }

        /**
         * 带起始位置的构造函数
         * @param name 显示名称
         * @param fieldName 字段名
         * @param rowSpan 跨行数
         * @param colSpan 跨列数
         * @param startColumn 起始列
         */
        public HeaderCell(String name, String fieldName, int rowSpan, int colSpan, int startColumn) {
            this.name = name;
            this.fieldName = fieldName;
            this.rowSpan = rowSpan;
            this.colSpan = colSpan;
            this.startColumn = startColumn;
        }

        /**
         * 判断是否有子节点
         * @return 是否有子节点
         */
        public boolean hasChildren() {
            return children != null && !children.isEmpty();
        }



        @Override
        public String toString() {
            return "HeaderCell{" +
                    "name='" + name + '\'' +
                    ", fieldName='" + fieldName + '\'' +
                    ", rowSpan=" + rowSpan +
                    ", colSpan=" + colSpan +
                    ", startColumn=" + startColumn +
                    ", children=" + (children != null ? children.size() : 0) +
                    '}';
        }

        /**
         * 样式配置内部类（可选）
         */
        @Setter
        @Getter
        public static class CellStyleConfig {
            // Getter 和 Setter 方法
            private HorizontalAlignment alignment = HorizontalAlignment.CENTER;
            private VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;
            private short backgroundColor = IndexedColors.WHITE.getIndex();
            private short fontColor = IndexedColors.BLACK.getIndex();
            private boolean bold = false;
            private short fontHeight = 11;
        }
    }

    /**
     * 导出数据到Excel - 通用方法
     *
     * @param headers  表头结构
     * @param dataList 数据列表
     * @param title    表格标题（可为空）
     */
    public static AjaxResult exportToExcel(List<ExcelHeaderNode> headers,
                                           List<?> dataList,
                                           String title) throws IOException {
//        return exportToExcel(headers, dataList, title, null, null);
        return new AjaxResult();
    }

    public static String encodingFilename(String filename) {
        filename = UUID.randomUUID() + "_" + filename + ".xlsx";
        return filename;
    }

    public static String getAbsoluteFile(String filename) {
        String downloadPath = ItsskyConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 导出数据到Excel - 增强版本（支持自定义数据提取器）
     * @param list 数据列表
     * @param sheetName 工作表名称
     * @param conditionList 条件列表
     * @param columnMax 总列数
     * @param corpName 公司名称
     * @param userName 用户名
     * @param headerData 表头结构
     * @param dataValueExtractor 自定义数据提取器
     * @return AjaxResult 包含文件名的结果
     */
    public static AjaxResult exportToExcel(List<?> list,
                                           String sheetName,
                                           List<String> conditionList,
                                           int columnMax,
                                           String corpName,
                                           String userName,
                                           List<List<HeaderCell>> headerData,
                                           Function<Object, Map<String, Object>> dataValueExtractor) throws IOException {
        String filename = encodingFilename("数据报表");
        String filePath = getAbsoluteFile(filename);

        try (OutputStream outputStream = Files.newOutputStream(Paths.get(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet("Sheet1");
            sheet.setDefaultColumnWidth(25);
            int currentRowIndex = 0;

            // 1. 创建公司行（第一行）
            XSSFRow corpRow = sheet.createRow(currentRowIndex++);
            XSSFCellStyle corpTitleStyle = createTitleCellStyle(workbook);
            XSSFCell corpCell = corpRow.createCell(0);
            corpCell.setCellValue(corpName);
            corpCell.setCellStyle(corpTitleStyle);
            // 合并公司行单元格
            CellRangeAddress corpMergeRegion = new CellRangeAddress(0, 0, 0, columnMax - 1);
            sheet.addMergedRegion(corpMergeRegion);
            // 确保合并区域所有单元格都有样式
            applyStyleToMergedRegion(sheet, corpMergeRegion, corpTitleStyle);

            // 2. 创建标题行（第二行）
            XSSFRow titleRow = sheet.createRow(currentRowIndex++);
            XSSFCellStyle titleStyle = createTitleCellStyle(workbook);
            XSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(sheetName);
            titleCell.setCellStyle(titleStyle);
            // 合并标题行单元格
            CellRangeAddress titleMergeRegion = new CellRangeAddress(1, 1, 0, columnMax - 1);
            sheet.addMergedRegion(titleMergeRegion);
            applyStyleToMergedRegion(sheet, titleMergeRegion, titleStyle);

            // 3. 创建条件行（第三行）
            if (conditionList != null && !conditionList.isEmpty()) {
                XSSFRow conditionRow = sheet.createRow(currentRowIndex++);
                XSSFCellStyle conditionStyle = createTitleTwoCellStyle(workbook);
                int[] conditionRowSplit = getConditionRowSplit(columnMax, conditionList.size());

                int startCol = 0;
                for (int i = 0; i < conditionList.size(); i++) {
                    int endCol = startCol + conditionRowSplit[i] - 1;
                    CellRangeAddress conditionMergeRegion = new CellRangeAddress(2, 2, startCol, endCol);
                    sheet.addMergedRegion(conditionMergeRegion);
                    // 为合并区域设置样式
                    applyStyleToMergedRegion(sheet, conditionMergeRegion, conditionStyle);

                    XSSFCell conditionCell = conditionRow.createCell(startCol);
                    conditionCell.setCellValue(conditionList.get(i));
                    conditionCell.setCellStyle(conditionStyle);
                    startCol = endCol + 1;
                }
            } else {
                currentRowIndex++; // 如果没有条件行，留空一行
            }

            // 4. 创建多级表头
            XSSFCellStyle headerStyle = createHeadCellStyle(workbook);
            for (List<HeaderCell> headerRowData : headerData) {
                XSSFRow headerRow = sheet.getRow(currentRowIndex);
                if (headerRow == null) {
                    headerRow = sheet.createRow(currentRowIndex);
                }

                int currentColIndex = 0;
                for (HeaderCell headerCell : headerRowData) {
                    int targetCol = headerCell.getStartColumn() > 0 ? headerCell.getStartColumn() - 1 : currentColIndex;
                    XSSFCell cell = headerRow.createCell(targetCol);
                    cell.setCellValue(headerCell.getName());
                    cell.setCellStyle(headerStyle); // 立即设置样式

                    if (headerCell.getRowSpan() > 1 || headerCell.getColSpan() > 1) {
                        int startRow = currentRowIndex;
                        int endRow = currentRowIndex + headerCell.getRowSpan() - 1;
                        int startCol = targetCol;
                        int endCol = targetCol + headerCell.getColSpan() - 1;

                        CellRangeAddress mergeRegion = new CellRangeAddress(startRow, endRow, startCol, endCol);
                        sheet.addMergedRegion(mergeRegion);
                        // 为合并区域设置样式
                        applyStyleToMergedRegion(sheet, mergeRegion, headerStyle);
                    }
                    currentColIndex = targetCol + headerCell.getColSpan();
                }
                currentRowIndex++;
            }

            // 5. 填充数据
            XSSFCellStyle dataStyle = createContentCellStyle(workbook);
            int dataStartRow = currentRowIndex;
            if (list != null && !list.isEmpty()) {
                // 获取所有叶子节点（对应数据字段），并按列顺序排序
                List<HeaderCell> leafNodes = getLeafNodes(headerData);
                leafNodes.sort(Comparator.comparingInt(HeaderCell::getStartColumn));

                for (int i = 0; i < list.size(); i++) {
                    Object entity = list.get(i);
                    XSSFRow dataRow = sheet.createRow(dataStartRow + i);

                    // 使用自定义数据提取器获取数据
                    Map<String, Object> valueMap = null;
                    if (dataValueExtractor != null) {
                        valueMap = dataValueExtractor.apply(entity);
                    }

                    for (int j = 0; j < leafNodes.size(); j++) {
                        HeaderCell headerCell = leafNodes.get(j);
                        XSSFCell dataCell = dataRow.createCell(j);
                        Object value = null;

                        try {
                            if (valueMap != null) {
                                value = valueMap.get(headerCell.getFieldName());
                            } else {
                                // 使用反射获取字段值
                                Field field = entity.getClass().getDeclaredField(headerCell.getFieldName());
                                field.setAccessible(true);
                                value = field.get(entity);
                            }
                            setCellValueBasedOnType(dataCell, value, dataStyle, workbook);
                        } catch (Exception e) {
                            dataCell.setCellValue("");
                            log.warn("获取字段值失败: {}", e.getMessage());
                        }
                        // 确保单元格有样式
                        dataCell.setCellStyle(dataStyle);
                    }
                }
            }

            // 6. 创建底部信息行
            int lastRowNum = sheet.getLastRowNum() + 1;
            XSSFRow footerRow = sheet.createRow(lastRowNum);
            int[] footerSplit = getConditionRowSplit(columnMax, 2);

            XSSFCellStyle leftFooterStyle = createFooterCellStyle(workbook, HorizontalAlignment.LEFT);
            XSSFCellStyle rightFooterStyle = createFooterCellStyle(workbook, HorizontalAlignment.RIGHT);

            XSSFCell operatorCell = footerRow.createCell(0);
            operatorCell.setCellValue("操作人:" + userName);
            operatorCell.setCellStyle(leftFooterStyle);

            XSSFCell timeCell = footerRow.createCell(footerSplit[0]);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeCell.setCellValue("导出时间:" + sdf.format(new Date()));
            timeCell.setCellStyle(rightFooterStyle);

            // 合并底部单元格
            CellRangeAddress leftFooterMerge = new CellRangeAddress(lastRowNum, lastRowNum, 0, footerSplit[0] - 1);
            CellRangeAddress rightFooterMerge = new CellRangeAddress(lastRowNum, lastRowNum, footerSplit[0], columnMax - 1);
            sheet.addMergedRegion(leftFooterMerge);
            sheet.addMergedRegion(rightFooterMerge);

            // 为底部合并区域设置样式
            applyStyleToMergedRegion(sheet, leftFooterMerge, leftFooterStyle);
            applyStyleToMergedRegion(sheet, rightFooterMerge, rightFooterStyle);

            workbook.write(outputStream);
        }
        return AjaxResult.success(filename);
    }

    private static void applyStyleToMergedRegion(Sheet sheet, CellRangeAddress mergedRegion, CellStyle style) {
        for (int row = mergedRegion.getFirstRow(); row <= mergedRegion.getLastRow(); row++) {
            Row sheetRow = sheet.getRow(row);
            if (sheetRow == null) {
                sheetRow = sheet.createRow(row);
            }
            for (int col = mergedRegion.getFirstColumn(); col <= mergedRegion.getLastColumn(); col++) {
                Cell cell = sheetRow.getCell(col);
                if (cell == null) {
                    cell = sheetRow.createCell(col);
                }
                cell.setCellStyle(style);
            }
        }
    }

    /**
     * 检查新的合并区域是否与现有区域重叠
     */
    private static boolean isOverlappingWithExistingRegions(CellRangeAddress newRegion, List<CellRangeAddress> existingRegions) {
        for (CellRangeAddress existingRegion : existingRegions) {
            if (newRegion.intersects(existingRegion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有叶子节点（按列顺序排序） - 修正版本
     * 只包含具有非空fieldName的单元格
     */
    private static List<HeaderCell> getLeafNodes(List<List<HeaderCell>> headerData) {
        List<HeaderCell> leafNodes = new ArrayList<>();
        for (List<HeaderCell> row : headerData) {
            for (HeaderCell cell : row) {
                // 如果有子节点，递归获取所有叶子节点
                if (cell.getChildren() != null && !cell.getChildren().isEmpty()) {
                    leafNodes.addAll(getLeafNodesFromCell(cell));
                } else {
                    // 没有子节点，只添加fieldName不为空的单元格
                    if (cell.getFieldName() != null && !cell.getFieldName().trim().isEmpty()) {
                        leafNodes.add(cell);
                    }
                }
            }
        }
        // 按 startColumn 排序，确保数据填充顺序正确
        leafNodes.sort(Comparator.comparingInt(HeaderCell::getStartColumn));
        return leafNodes;
    }

    /**
     * 递归获取单元格的所有叶子节点 - 修正版本
     * 只包含具有非空fieldName的单元格
     */
    private static List<HeaderCell> getLeafNodesFromCell(HeaderCell cell) {
        List<HeaderCell> leaves = new ArrayList<>();
        if (cell.getChildren() == null || cell.getChildren().isEmpty()) {
            // 只有fieldName不为空的单元格才参与数据填充
            if (cell.getFieldName() != null && !cell.getFieldName().trim().isEmpty()) {
                leaves.add(cell);
            }
        } else {
            for (HeaderCell child : cell.getChildren()) {
                leaves.addAll(getLeafNodesFromCell(child));
            }
        }
        return leaves;
    }

    // 辅助方法：根据值的类型设置单元格内容
    private static void setCellValueBasedOnType(Cell cell, Object value, CellStyle defaultStyle, Workbook workbook) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        // 处理不同类型的数据
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            // 处理各种数字类型
            if (value instanceof Integer || value instanceof Long || value instanceof Short) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Double || value instanceof Float) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else {
                cell.setCellValue(value.toString());
            }
        } else if (value instanceof java.util.Date) {
            // 处理日期类型
            cell.setCellValue((java.util.Date) value);
            // 设置日期格式样式
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(defaultStyle);
            DataFormat format = workbook.createDataFormat();
            dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
            cell.setCellStyle(dateStyle);
        } else if (value instanceof LocalDate) {
            // 处理LocalDate类型
            LocalDate localDate = (LocalDate) value;
            cell.setCellValue(java.sql.Date.valueOf(localDate));
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(defaultStyle);
            DataFormat format = workbook.createDataFormat();
            dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
            cell.setCellStyle(dateStyle);
        } else if (value instanceof LocalDateTime) {
            // 处理LocalDateTime类型
            LocalDateTime localDateTime = (LocalDateTime) value;
            cell.setCellValue(java.sql.Timestamp.valueOf(localDateTime));
            CellStyle dateTimeStyle = workbook.createCellStyle();
            dateTimeStyle.cloneStyleFrom(defaultStyle);
            DataFormat format = workbook.createDataFormat();
            dateTimeStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
            cell.setCellStyle(dateTimeStyle);
        } else if (value instanceof Boolean) {
            // 处理布尔类型
            cell.setCellValue((Boolean) value);
        } else {
            // 其他类型转为字符串
            cell.setCellValue(value.toString());
        }
    }


    private static void setMergedRegionBorder(Workbook workbook, Sheet sheet, CellRangeAddress mergedRegion, CellStyle originalStyle) {
        // 获取原始样式的边框样式和颜色
        BorderStyle topBorder = originalStyle.getBorderTop();
        BorderStyle bottomBorder = originalStyle.getBorderBottom();
        BorderStyle leftBorder = originalStyle.getBorderLeft();
        BorderStyle rightBorder = originalStyle.getBorderRight();
        short topBorderColor = originalStyle.getTopBorderColor();
        short bottomBorderColor = originalStyle.getBottomBorderColor();
        short leftBorderColor = originalStyle.getLeftBorderColor();
        short rightBorderColor = originalStyle.getRightBorderColor();

        // 遍历合并区域的每一个单元格
        for (int r = mergedRegion.getFirstRow(); r <= mergedRegion.getLastRow(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                row = sheet.createRow(r);
            }
            for (int c = mergedRegion.getFirstColumn(); c <= mergedRegion.getLastColumn(); c++) {
                Cell cell = row.getCell(c);
                if (cell == null) {
                    cell = row.createCell(c);
                }
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.cloneStyleFrom(originalStyle); // 克隆原始样式

                // 对于区域边缘的单元格，设置相应的边框
                if (r == mergedRegion.getFirstRow()) {
                    cellStyle.setBorderTop(topBorder);
                    cellStyle.setTopBorderColor(topBorderColor);
                }
                if (r == mergedRegion.getLastRow()) {
                    cellStyle.setBorderBottom(bottomBorder);
                    cellStyle.setBottomBorderColor(bottomBorderColor);
                }
                if (c == mergedRegion.getFirstColumn()) {
                    cellStyle.setBorderLeft(leftBorder);
                    cellStyle.setLeftBorderColor(leftBorderColor);
                }
                if (c == mergedRegion.getLastColumn()) {
                    cellStyle.setBorderRight(rightBorder);
                    cellStyle.setRightBorderColor(rightBorderColor);
                }

                cell.setCellStyle(cellStyle);
            }
        }
    }

    /**
     * 创建标题样式
     *
     * @param wb
     * @return
     */
    private static XSSFCellStyle createTitleCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        cellStyle.setFillBackgroundColor();
//        cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());//背景颜色

        XSSFFont headerFont1 = (XSSFFont) wb.createFont(); // 创建字体样式
        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 20); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式
        cellStyle.setBorderBottom(BorderStyle.NONE); //下边框
        cellStyle.setBorderLeft(BorderStyle.NONE); //左边框
        cellStyle.setBorderRight(BorderStyle.NONE); //右边框
        cellStyle.setBorderTop(BorderStyle.NONE); //上边框
        return cellStyle;
    }

    /**
     * 创建二标题样式
     *
     * @param wb
     * @return
     */
    private static XSSFCellStyle createTitleTwoCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());//背景颜色

        XSSFFont headerFont1 = (XSSFFont) wb.createFont(); // 创建字体样式
//        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 15); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式
        cellStyle.setBorderBottom(BorderStyle.NONE); //下边框
        cellStyle.setBorderLeft(BorderStyle.NONE); //左边框
        cellStyle.setBorderRight(BorderStyle.NONE); //右边框
        cellStyle.setBorderTop(BorderStyle.NONE); //上边框

        return cellStyle;
    }

    private static XSSFCellStyle createHeadCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//背景颜色
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框
        cellStyle.setWrapText(false);

        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        XSSFFont headerFont = (XSSFFont) wb.createFont(); // 创建字体样式
        headerFont.setBold(true); //字体加粗
        headerFont.setFontName("黑体"); // 设置字体类型
        headerFont.setFontHeightInPoints((short) 12); // 设置字体大小
        cellStyle.setFont(headerFont); // 为标题样式设置字体样式

        return cellStyle;
    }

    /**
     * 创建内容样式
     *
     * @param wb
     * @return
     */
    private static XSSFCellStyle createContentCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());//背景颜色
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        cellStyle.setWrapText(false);// 设置自动换行
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        // 生成12号字体
        XSSFFont font = wb.createFont();
        font.setColor((short) 8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 底部样式
     */
    private static XSSFCellStyle createFooterCellStyle(XSSFWorkbook wb, HorizontalAlignment alignment) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(alignment);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 可以根据需要设置字体等
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }

    private static int[] getConditionRowSplit(int columnMax, int conditionNum) {
        int part = columnMax / conditionNum;
        int remainder = columnMax % conditionNum;
        int[] result = new int[conditionNum];
        for (int i = 0; i < conditionNum - 1; i++) {
            result[i] = part;
        }
        result[conditionNum - 1] = part + remainder;
        return result;
    }
}