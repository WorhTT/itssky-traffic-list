<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">ECJ电子支付(ETC)出口流量统计表</h3>
    </div>
    <div style="display: flex">
      <span v-for="item in conditionList" style="flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;">
        {{item}}
      </span>
      <el-row :gutter="10" class="mb8" style="display: flex; justify-content: flex-end;">
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
          >导出
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-document"
            size="mini"
            @click="printTable"
          >打印
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table v-loading="loading" :data="dataList" border ref="myTable" >
      <el-table-column label="统计方式" align="center" prop="statType" min-width="120"/>
      <el-table-column label="客一" align="center">
        <el-table-column label="C卡" align="center" prop="k1c"/>
        <el-table-column label="D卡" align="center" prop="k1d"/>
      </el-table-column>
      <el-table-column label="客二" align="center">
        <el-table-column label="C卡" align="center" prop="k2c"/>
        <el-table-column label="D卡" align="center" prop="k2d"/>
      </el-table-column>
      <el-table-column label="客三" align="center">
        <el-table-column label="C卡" align="center" prop="k3c"/>
        <el-table-column label="D卡" align="center" prop="k3d"/>
      </el-table-column>
      <el-table-column label="客四" align="center">
        <el-table-column label="C卡" align="center" prop="k4c"/>
        <el-table-column label="D卡" align="center" prop="k4d"/>
      </el-table-column>
      <el-table-column label="客车小计" align="center">
        <el-table-column label="C卡" align="center" prop="kamountc"/>
        <el-table-column label="D卡" align="center" prop="kamountd"/>
      </el-table-column>
      <el-table-column label="货一" align="center">
        <el-table-column label="C卡" align="center" prop="h1c"/>
        <el-table-column label="D卡" align="center" prop="h1d"/>
      </el-table-column>
      <el-table-column label="货二" align="center">
        <el-table-column label="C卡" align="center" prop="h2c"/>
        <el-table-column label="D卡" align="center" prop="h2d"/>
      </el-table-column>
      <el-table-column label="货三" align="center">
        <el-table-column label="C卡" align="center" prop="h3c"/>
        <el-table-column label="D卡" align="center" prop="h3d"/>
      </el-table-column>
      <el-table-column label="货四" align="center">
        <el-table-column label="C卡" align="center" prop="h4c"/>
        <el-table-column label="D卡" align="center" prop="h4d"/>
      </el-table-column>
      <el-table-column label="货五" align="center">
        <el-table-column label="C卡" align="center" prop="h5c"/>
        <el-table-column label="D卡" align="center" prop="h5d"/>
      </el-table-column>
      <el-table-column label="货六" align="center">
        <el-table-column label="C卡" align="center" prop="h6c"/>
        <el-table-column label="D卡" align="center" prop="h6d"/>
      </el-table-column>
      <el-table-column label="货车小计" align="center">
        <el-table-column label="C卡" align="center" prop="hamountc"/>
        <el-table-column label="D卡" align="center" prop="hamountd"/>
      </el-table-column>
      <el-table-column label="专一" align="center">
        <el-table-column label="C卡" align="center" prop="z1c"/>
        <el-table-column label="D卡" align="center" prop="z1d"/>
      </el-table-column>
      <el-table-column label="专二" align="center">
        <el-table-column label="C卡" align="center" prop="z2c"/>
        <el-table-column label="D卡" align="center" prop="z2d"/>
      </el-table-column>
      <el-table-column label="专三" align="center">
        <el-table-column label="C卡" align="center" prop="z3c"/>
        <el-table-column label="D卡" align="center" prop="z3d"/>
      </el-table-column>
      <el-table-column label="专四" align="center">
        <el-table-column label="C卡" align="center" prop="z4c"/>
        <el-table-column label="D卡" align="center" prop="z4d"/>
      </el-table-column>
      <el-table-column label="专五" align="center">
        <el-table-column label="C卡" align="center" prop="z5c"/>
        <el-table-column label="D卡" align="center" prop="z5d"/>
      </el-table-column>
      <el-table-column label="专六" align="center">
        <el-table-column label="C卡" align="center" prop="z6c"/>
        <el-table-column label="D卡" align="center" prop="z6d"/>
      </el-table-column>
      <el-table-column label="专车小计" align="center">
        <el-table-column label="C卡" align="center" prop="zamountc"/>
        <el-table-column label="D卡" align="center" prop="zamountd"/>
      </el-table-column>
      <el-table-column label="总计" align="center">
        <el-table-column label="C卡" align="center" prop="allAmountc"/>
        <el-table-column label="D卡" align="center" prop="allAmountd"/>
        <el-table-column label="合计" align="center" prop="total"/>
      </el-table-column>
    </el-table>
    <!-- 添加底部信息区域 -->
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
      <span>操作人：{{ operatorName }}</span>
      <span>打印时间：{{ currentDateTime }}</span>
    </div>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {erjsFlow, exportEcjFlow} from "@/api/report/exitFlow"
import {getLoginUser} from "@/api/login";

export default {
  name: "EcjFlowDetail",
  data() {
    return {
      props: {multiple: true},
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 公告表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      stationOptions: [],
      shiftOptions: [],
      pickerType: 'date',
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      conditionList:[],
      operatorName: '',
      corpName: '',
    };
  },
  created() {
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
    getLoginUser().then(res => {
      if (res.data) {
        this.operatorName = res.data.username;
      }
    })
  },
  computed: {
    // corpName() {
    //   return process.env.VUE_APP_CORP_NAME ? process.env.VUE_APP_CORP_NAME : '宁杭高速'
    // },
    currentDateTime() {
      return this.getCurrentDateTime();
    },
  },
  methods: {
    getCurrentDateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = (now.getMonth() + 1).toString().padStart(2, '0');
      const day = now.getDate().toString().padStart(2, '0');
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      const seconds = now.getSeconds().toString().padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    getList() {
      this.loading = true;
      this.queryParams.flagStr = '3';
      erjsFlow(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.conditionList = response.conditionList;
        this.corpName = response.title;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      this.queryParams.flagStr = '3';
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出ECJ电子支付(ETC)出口流量统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEcjFlow(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      }).finally(() => {
        this.loading = false;
      })
    },
    printTable() {
      const corpName = this.corpName;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;
      let conditionListHtml = this.conditionList.map(item => `<span>${item}</span>`).join('');
      // 获取操作人信息
      const operator = this.operatorName;
      const printTime = this.getCurrentDateTime();
      // 添加底部信息行
      const footerHtml = `
    <div class="footer-info">
      <span class="operator">操作人：${operator}</span>
      <span class="print-time">打印时间：${printTime}</span>
    </div>
  `;

      // 创建一个新的表格结构，模拟原表格的复杂嵌套结构
      let tableHtml = `
        <table class="el-table" border="1" style="border-collapse: collapse; width: 100%;">
          <thead>
            <tr>
              <th rowspan="2" style="min-width: 120px; text-align: center; padding: 8px;">统计方式</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客一</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客二</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客三</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客四</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">客车小计</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货一</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货二</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货三</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货四</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货五</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货六</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">货车小计</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专一</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专二</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专三</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专四</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专五</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专六</th>
              <th colspan="2" style="min-width: 100px; text-align: center; padding: 8px;">专车小计</th>
              <th colspan="3" style="min-width: 150px; text-align: center; padding: 8px;">总计</th>
            </tr>
            <tr>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">C卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">D卡</th>
              <th style="min-width: 50px; text-align: center; padding: 8px;">合计</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        tableHtml += `
          <tr>
            <td style="padding: 8px; text-align: center;">${row.statType !== undefined && row.statType !== null ? row.statType : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k1c !== undefined && row.k1c !== null ? row.k1c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k1d !== undefined && row.k1d !== null ? row.k1d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k2c !== undefined && row.k2c !== null ? row.k2c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k2d !== undefined && row.k2d !== null ? row.k2d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k3c !== undefined && row.k3c !== null ? row.k3c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k3d !== undefined && row.k3d !== null ? row.k3d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k4c !== undefined && row.k4c !== null ? row.k4c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.k4d !== undefined && row.k4d !== null ? row.k4d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.kamountc !== undefined && row.kamountc !== null ? row.kamountc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.kamountd !== undefined && row.kamountd !== null ? row.kamountd : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h1c !== undefined && row.h1c !== null ? row.h1c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h1d !== undefined && row.h1d !== null ? row.h1d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h2c !== undefined && row.h2c !== null ? row.h2c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h2d !== undefined && row.h2d !== null ? row.h2d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h3c !== undefined && row.h3c !== null ? row.h3c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h3d !== undefined && row.h3d !== null ? row.h3d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h4c !== undefined && row.h4c !== null ? row.h4c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h4d !== undefined && row.h4d !== null ? row.h4d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h5c !== undefined && row.h5c !== null ? row.h5c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h5d !== undefined && row.h5d !== null ? row.h5d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h6c !== undefined && row.h6c !== null ? row.h6c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.h6d !== undefined && row.h6d !== null ? row.h6d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.hamountc !== undefined && row.hamountc !== null ? row.hamountc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.hamountd !== undefined && row.hamountd !== null ? row.hamountd : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z1c !== undefined && row.z1c !== null ? row.z1c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z1d !== undefined && row.z1d !== null ? row.z1d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z2c !== undefined && row.z2c !== null ? row.z2c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z2d !== undefined && row.z2d !== null ? row.z2d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z3c !== undefined && row.z3c !== null ? row.z3c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z3d !== undefined && row.z3d !== null ? row.z3d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z4c !== undefined && row.z4c !== null ? row.z4c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z4d !== undefined && row.z4d !== null ? row.z4d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z5c !== undefined && row.z5c !== null ? row.z5c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z5d !== undefined && row.z5d !== null ? row.z5d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z6c !== undefined && row.z6c !== null ? row.z6c : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.z6d !== undefined && row.z6d !== null ? row.z6d : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.zamountc !== undefined && row.zamountc !== null ? row.zamountc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.zamountd !== undefined && row.zamountd !== null ? row.zamountd : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.allAmountc !== undefined && row.allAmountc !== null ? row.allAmountc : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.allAmountd !== undefined && row.allAmountd !== null ? row.allAmountd : ''}</td>
            <td style="padding: 8px; text-align: center;">${row.total !== undefined && row.total !== null ? row.total : ''}</td>
          </tr>
        `;
      });

      tableHtml += `
          </tbody>
        </table>
      `;

      let htmlContent = `
      <!DOCTYPE html>
        <html>
        <head>
        <title>Print</title>
        <style>
        body {
          margin: 0;
          padding: 15px;
          font-family: "Microsoft YaHei", SimHei, Arial, sans-serif;
          box-sizing: border-box;
          font-size: 14px;
        }
        .print-title {
          text-align: center;
          font-size: 20px;
          font-weight: bold;
          margin-bottom: 10px;
        }
        .container {
          display: flex;
          margin-bottom: 15px;
        }
        .container span {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 15px;
        }
        .table-container {
          margin-top: 10px;
          width: 100%;
          zoom: 0.45;
        }
        .el-table {
          width: 100%;
          border-collapse: collapse;
          table-layout: auto; /* 自动调整列宽 */
          font-size: 13px;
        }
        .el-table thead tr {
          background-color: #ebeef5;
          break-inside: avoid; /* 防止表头跨页 */
        }
        .el-table th, .el-table td {
          border: 1px solid #000;
          padding: 8px 5px;
          text-align: center;
          word-wrap: break-word;
          white-space: normal; /* 允许内容换行 */
          font-size: 18px;
          min-width: 50px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格跨页 */
        }
        .el-table th {
          font-weight: bold;
          font-size: 20px;
          background-color: #f5f7fa;
          height: auto;
          line-height: 1.2;
          break-inside: avoid; /* 防止表头单元格跨页 */
        }
        /* 防止表格跨页截断 */
        thead {
          display: table-header-group;
        }
        tfoot {
          display: table-footer-group;
        }
        tbody {
          display: table-row-group;
        }
        tr {
          page-break-inside: avoid;
          page-break-after: auto;
          break-inside: avoid; /* 防止行跨页 */
        }
        td, th {
          page-break-inside: avoid;
        }
        .footer-info {
            display: flex;
            justify-content: space-between;
            margin-top: 15px;
            font-size: 12px;
        }
        .operator {
            text-align: left;
        }
        .print-time {
            text-align: right;
        }
        body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0;
            margin: 0;
            width: 100%;
            font-size: 11px; /* 调小字体以适应更多内容 */
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 11px; /* 调小字体以适应更多内容 */
          }
          .el-table thead tr {
            background-color: #ebeef5;
            break-inside: avoid; /* 防止表头跨页 */
          }
          .el-table th, .el-table td {
            padding: 4px 3px; /* 减小内边距以节省空间 */
            font-size: 15px;
            min-width: 40px; /* 调整最小宽度 */
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid; /* 防止单元格跨页 */
          }
          .el-table th {
            font-size: 16px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid; /* 防止表头单元格跨页 */
          }
          .container span {
            font-size: 13px;
          }
          .print-title {
            font-size: 18px;
          }
          .footer-info {
            margin-top: 15px;
            font-size: 12px;
          }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">ECJ电子支付(ETC)出口流量统计表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${tableHtml}</div>
            ${footerHtml}
        </body>
        </html>
      `;
      printDocument.write(htmlContent);
      printDocument.close();

      // Trigger print
      printFrame.contentWindow.focus();
      printFrame.contentWindow.print();
    },
  }
};
</script>

<style lang="scss" scoped>
::v-deep .el-table .el-table__header-wrapper th {
  height: 20px;
}
::v-deep .el-table--medium .el-table__cell {
  padding: 4px 0;
}
// 自定义滚动条样式
::v-deep .el-table {
  &::-webkit-scrollbar {
    width: 14px;
    height: 14px;
  }

  &::-webkit-scrollbar-track {
    background: #f0f0f0;
    border-radius: 7px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(45deg, #409eff, #4a9eff);
    border-radius: 7px;
    border: 3px solid #f0f0f0;
    min-height: 30px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(45deg, #3a8ee6, #409eff);
  }

  &::-webkit-scrollbar-corner {
    background: #f0f0f0;
  }
}

// 如果表格内容区域也有滚动条，也应用样式
::v-deep .el-table__body-wrapper {
  &::-webkit-scrollbar {
    width: 14px;
    height: 14px;
  }

  &::-webkit-scrollbar-track {
    background: #f0f0f0;
    border-radius: 7px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(45deg, #409eff, #4a9eff);
    border-radius: 7px;
    border: 3px solid #f0f0f0;
    min-height: 30px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(45deg, #3a8ee6, #409eff);
  }

  &::-webkit-scrollbar-corner {
    background: #f0f0f0;
  }
}
</style>
