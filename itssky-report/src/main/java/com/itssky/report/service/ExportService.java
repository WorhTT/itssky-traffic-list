package com.itssky.report.service;

import com.itssky.common.utils.poi.ExcelUtil;
import com.itssky.report.domain.ReportChargeInfo;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ITSSKY
 * 测试导出复杂表头EXCEL
 */
@Slf4j
@Service
public class ExportService {
    @Autowired
    private ReportFlowService reportFlowService;

    /**
     * 导出通行费收入统计表
     */
    public void exportCharge() throws FileNotFoundException {
        List<String> tableTitleList = new ArrayList<>();
        List<Map<String, Object>> convertListMap = new ArrayList<>();
        List<ReportChargeInfo> charge = reportFlowService.getCharge();
        charge.forEach(i -> {
            Map<String, Object> map = new HashMap<>();
            Field[] fields = i.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                System.out.println(fields[j]);
                try {
                    Field field = fields[j];
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(i));
                    tableTitleList.add(field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            convertListMap.add(map);
        });
        // 第一步，创建一个Workbook，对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 第二步，在Workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet("sheet");
        // 第三步，设置样式以及字体样式
        XSSFCellStyle titleStyle = createTitleCellStyle(wb);
        XSSFCellStyle titleTwoStyle = createTitleTwoCellStyle(wb);
        XSSFCellStyle headerStyle = createHeadCellStyle(wb);
        XSSFCellStyle contentStyle = createContentCellStyle(wb);
        // 行号
        int rowNum = 0;
        // 创建第一页的第一行，索引从0开始
        XSSFRow row0 = sheet.createRow(rowNum++);
        //第二行
        XSSFRow row2 = sheet.createRow(rowNum++);
        //第三行
        XSSFRow row3 = sheet.createRow(rowNum++);
        //第四行
        XSSFRow row4 = sheet.createRow(rowNum++);
        //第五行
        XSSFRow row5 = sheet.createRow(rowNum++);
        String[] row_one = null;
        String[] row_two = null;
        String[] row_three = null;
        String[] row_four = null;
        String[] row_five = null;
        int cellSize = 28;

        row_one = new String[]{"宁杭高速FT通行费收入统计表", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        row_two = new String[]{"收费站：收费中心", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "统计时间：2024-09-19 —— 2024-09-19"};
        row_three = new String[]{"统计方式", "通行费总额", "", "", "", "", "", "", "", "", "", "移动支付", "电子支付消费额",
                "", "", "打印票据", "", "", "", "", "定额票据", "", "废票", "", "公务IC卡", "军车IC卡", "免费IC卡", "应缴IC卡"};
        row_four = new String[]{"", "统计金额", "应缴金额", "实缴金额", "金额差异", "欠款", "", "加收款", "", "", "",
                "移动支付", "储值卡", "记账卡", "合计", "现金", "", "移动支付", "", "打印票合计", "张数", "金额", "张数", "金额",
                "", "", "", ""};
        row_five = new String[]{"", "", "", "", "", "车次", "金额", "现金", "移动支付", "合计", "次数", "", "",
                "", "", "张数", "打票金额", "张数", "打票金额", "", "", "", "", "", "", "", "", ""};
        //合并单元格
        extracted(sheet);
        //创建第一行单元格
        for (int i = 0; i < row_one.length; i++) {
            XSSFCell c00 = row0.createCell(i);
            c00.setCellValue(row_one[i]);
            c00.setCellStyle(titleStyle);
        }
        //创建第二行单元格
        for (int i = 0; i < row_two.length; i++) {
            XSSFCell tempCell = row2.createCell(i);
            tempCell.setCellValue(row_two[i]);
            tempCell.setCellStyle(titleTwoStyle);
        }
        //创建第三行第四行第五行单元格
        for (int i = 0; i < row_three.length; i++) {
            XSSFCell tempCell = row3.createCell(i);
            tempCell.setCellValue(row_three[i]);
            tempCell.setCellStyle(headerStyle);
        }
        for (int i = 0; i < row_four.length; i++) {
            XSSFCell tempCell = row4.createCell(i);
            tempCell.setCellValue(row_four[i]);
            tempCell.setCellStyle(headerStyle);
        }
        for (int i = 0; i < row_five.length; i++) {
            XSSFCell tempCell = row5.createCell(i);
            tempCell.setCellValue(row_five[i]);
            tempCell.setCellStyle(headerStyle);
        }
        // 设置表头单元格的宽度
        tableTitleStyleColumnWidth(sheet);
        // TODO： 填充数据
        approvalMethodFillInData(convertListMap, sheet, contentStyle, rowNum, cellSize);
        HttpServletResponse response = new Response();
//        buildExcelDocument("宁杭高速FT通行费收入统计表.xlsx", wb, response);
        OutputStream out = null;
        out = new FileOutputStream(getAbsoluteFile("宁杭高速FT通行费收入统计表.xlsx"));
        try {
            wb.write(out);
        } catch (IOException e) {
            log.error("导出Excel异常{}", e.getMessage());
        } finally {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }

//        List<Map<String, Object>> convertListMap = toListConvertListMap(tableTitleList, list);

    }

    /**
     * 设置表头单元格
     */
    private static void tableTitleStyleColumnWidth(XSSFSheet sheet) {

    }

    private static void approvalMethodFillInData(List<Map<String, Object>> convertListMap, XSSFSheet sheet, XSSFCellStyle contentStyle, int rowNum, int cellSize) {

        for (Map<String, Object> map : convertListMap) {
            XSSFRow tempRow = sheet.createRow(rowNum++);
            tempRow.setHeight((short) 500);

            // 循环单元格填入数据
            for (int i = 0; i < cellSize; i++) {
                //列宽自适应，j为自适应的列，true就是自适应，false就是不自适应，默认不自适应
                sheet.autoSizeColumn(i, true);
                XSSFCell tempCell = tempRow.createCell(i);
                tempCell.setCellStyle(contentStyle);
                String tempValue = "";
                switch (i) {
                    case 0:
                        tempValue = map.get("stationName").toString();
                        break;
                    case 1:
                        tempValue = map.get("tjje").toString();
                        break;
                    case 2:
                        tempValue = map.get("yjje").toString();
                        break;
                    case 3:
                        tempValue = map.get("sjje").toString();
                        break;
                    case 4:
                        tempValue = map.get("jecy").toString();
                        break;
                    case 5:
                        tempValue = map.get("qkcc").toString();
                        break;
                    case 6:
                        tempValue = map.get("qkje").toString();
                        break;
                    case 7:
                        tempValue = map.get("jsxj").toString();
                        break;
                    case 8:
                        tempValue = map.get("jsje").toString();
                        break;
                    case 9:
                        tempValue = map.get("jshj").toString();
                        break;
                    case 10:
                        tempValue = map.get("jscs").toString();
                        break;
                    case 11:
                        tempValue = map.get("ydzf").toString();
                        break;
                    case 12:
                        tempValue = map.get("dzczk").toString();
                        break;
                    case 13:
                        tempValue = map.get("dzjzk").toString();
                        break;
                    case 14:
                        tempValue = map.get("dzhj").toString();
                        break;
                    case 15:
                        tempValue = map.get("xjpjzs").toString();
                        break;
                    case 16:
                        tempValue = map.get("xjdpje").toString();
                        break;
                    case 17:
                        tempValue = map.get("ydzfzs").toString();
                        break;
                    case 18:
                        tempValue = map.get("ydzfdpje").toString();
                        break;

                    case 19:
                        tempValue = map.get("dyphj").toString();
                        break;
                    case 20:
                        tempValue = map.get("depjzs").toString();
                        break;
                    case 21:
                        tempValue = map.get("depjje").toString();
                        break;
                    case 22:
                        tempValue = map.get("fpzs").toString();
                        break;
                    case 23:
                        tempValue = map.get("fpje").toString();
                        break;
                    case 24:
                        tempValue = map.get("gwic").toString();
                        break;
                    case 25:
                        tempValue = map.get("jcic").toString();
                        break;
                    case 26:
                        tempValue = map.get("mfic").toString();
                        break;
                    case 27:
                        tempValue = map.get("yjic").toString();
                        break;
                }
                tempCell.setCellValue(tempValue);
            }
        }
    }

    /**
     * 下载
     * @param fileName
     * @param wb
     * @param response
     */
    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            // 可自行定义编码格式
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            //清除jsp编译html文件的空白,防止excel出现空行
            response.flushBuffer();
            //写出
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(wb);
        }
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = "D:/itssky/" + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }


    /**
     * 合并单元格
     */
    private static void extracted(XSSFSheet sheet) {
        // TODO: 合并参数分别为： 起始行，结束行，起始列，结束列
        //将第一行的第一列到第二十八列合并
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 27));
        //将第二行的第一列到第十四列合并
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 13));
        //将第二行的第十五列到第二十八列合并
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 14, 27));
        //将第三行至第五行的第一列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 0, 0));
        //将第三行的第二列到第十一列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 10));
        //将第三行至第五行的第十二列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 11, 11));
        //将第三行的十三到十五列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 12, 14));
        //将第三行的十六到二十列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 15, 19));
        //第三行的二十一到二十二列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 20, 21));
        //第三行的二十三到二十四列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 22, 23));
        //将第三行至第五行的第二十五列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 24, 24));
        //将第三行至第五行的第二十六列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 25, 25));
        //将第三行至第五行的第二十七列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 26, 26));
        //将第三行至第五行的第二十八列合并
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 27, 27));
        //将第四行至第五行的第二列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
        //将第四行至第五行的第三列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
        //将第四行至第五行的第四列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 3, 3));
        //将第四行至第五行的第五列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 4, 4));
        //将第四行的第六列到第七列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
        //将第四行的第八列到第十一列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 10));
        //将第四行至第五行的第十三列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 12, 12));
        //将第四行至第五行的第十四列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 13, 13));
        //将第四行至第五行的第十五列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 14, 14));
        //将第四行的第十六列到第十七列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 15, 16));
        //将第四行的第十八列到第十九列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 18));
        //将第四行至第五行的第二十列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 19, 19));
        //将第四行至第五行的第二十一列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 20, 20));
        //将第四行至第五行的第二十二列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 21, 21));
        //将第四行至第五行的第二十三列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 22, 22));
        //将第四行至第五行的第二十四列合并
        sheet.addMergedRegion(new CellRangeAddress(3, 4, 23, 23));

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

    /**
     * 创建表头样式
     *
     * @param wb
     * @return
     */
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
        cellStyle.setWrapText(true);// 设置自动换行
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
}
