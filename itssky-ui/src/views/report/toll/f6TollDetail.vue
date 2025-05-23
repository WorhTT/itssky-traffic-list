<template>
  <div class="app-container" ref="box">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">F6收费站通行费收入班对账表</h3>
    </div>
    <div style="display: flex">
      <span v-for="item in conditionList" style="flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;">
        {{ item }}
      </span>
      <el-row :gutter="10" class="mb8" style="display: flex; justify-content: flex-end;">
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            class="export-button-container"
          >导出
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-document"
            size="mini"
            @click="printTable"
            class="print-button-container"
          >打印
          </el-button>
        </el-col>
      </el-row>
    </div>


    <el-table v-loading="loading" :data="dataList" ref="myTable" :span-method="arraySpanMethod" :cell-style="cellStyle" border >
      <el-table-column label="收费员工号" align="center" prop="operatorId"/>
      <el-table-column label="收费员姓名" align="center" prop="operatorName"/>
      <el-table-column label="应缴金额" align="center" prop="toll"/>
      <el-table-column label="应缴IC卡张数" align="center" prop="yjIcCardNum"/>
      <el-table-column label="纸券" align="center" prop="paperNum"/>
      <el-table-column label="应发IC卡张数" align="center" prop="yfIcCardNum"/>
    </el-table>
    <!-- Hidden iframe for printing -->
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {f6Toll, exportF6Toll} from "@/api/report/toll"

export default {
  name: "F6TollDetail",
  components: {},
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
      queryParams: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      stationOptions: [],
      shiftOptions: [
        {label: '早班', value: 1},
        {label: '中班', value: 2},
        {label: '晚班', value: 3},
      ],
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      conditionList: []
    };
  },
  computed: {
    corpName() {
      return process.env.VUE_APP_CORP_NAME ? process.env.VUE_APP_CORP_NAME : '宁杭高速'
    },
  },
  created() {
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
  },
  mounted() {
    console.log('当前公司:', process.env.VUE_APP_CORP_NAME)
  },
  methods: {
    cellStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow === true) {
        return 'background:	#C0C0C0';
      }
    },
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (row.totalRow  === true) {
        row.operatorId = "合计";
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex < 2) {
          return [0, 0];
        }
      }
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      f6Toll(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.conditionList = response.conditionList;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出F6收费站通行费收入班对账表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportF6Toll(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      }).finally(() => {
        this.loading = false;
      })
    },

    printTable() {
      const corpName = this.corpName;
      const elTable = this.$refs.myTable.$el;
      const printFrame = document.getElementById('printFrame');
      const printDocument = printFrame.contentDocument || printFrame.contentWindow.document;
      let conditionListHtml = this.conditionList.map(item => `<span>${item}</span>`).join('');
      let htmlContent = `
        <!DOCTYPE html>
        <html>
        <head>
        <title>Print</title>
        <style>
        .table-container {
            zoom: 0.78 !important;
            width: 100% !important;
            max-width: 100vw !important;
            overflow: visible !important;
        }
         .print-title {
          text-align: center;
          font-size: 20px;
          font-weight: bold;
          margin-bottom: 5px;
        }
        body {
          margin: 0;
          padding: 20px;
          font-family: Arial, sans-serif;
          box-sizing: border-box;
        }
        .container {
          display: flex;
        }
        .container span {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .el-table {
            width: max-content !important; /* 允许表格根据内容扩展 */
            min-width: 100% !important;
            table-layout: auto !important; /* 自动列宽模式 */
            font-size: 12px !important;     /* 基础字号缩小 */
        }
        .el-table__header-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table__body-wrapper {
            overflow: visible !important;
            max-width: none !important;
        }
        .el-table td {
          border: 1px solid #000 !important;
          font-size: 16px;
          padding: 1px 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
        }
        /* 强制列宽生效 */
/*.el-table__header colgroup col {*/
/*  width: 10px !important;*/
/*  min-width: 10px !important;*/
/*}*/
        .el-table th {
          border: 1px solid #000 !important;
          font-size: 22px;
          padding: 4px; /* Reduce padding to make cells more compact */
          /*text-align: center; !* Center text *!*/
          word-wrap: break-word; /* Ensure text wraps within cells */
          white-space: pre-wrap; /* Allow text to wrap */
          /*writing-mode: vertical-rl;*/
        }
        @media print {
          body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0 !important;
            margin: 0 !important;
            width: 100vw !important; /* 强制占据全部视口宽度 */
            /*transform: scale(0.85);  !* 初始缩放系数 *!*/
            transform-origin: top left;
          }
        }
        @page {
          size: auto;
          margin: 5mm 2mm !important;    /* 减少边距 */
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">F6收费站通行费收入班对账表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${elTable.outerHTML}</div>
        </body>
        </html>
        `
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

</style>
