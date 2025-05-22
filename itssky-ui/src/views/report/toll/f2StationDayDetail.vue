<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">F2收费站通行费收入日统计表</h3>
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
            class="export-button-container"
          >导出
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="printTable"
            class="print-button-container"
          >打印
          </el-button>
        </el-col>
      </el-row>

    </div>

    <el-table v-loading="loading" :data="dataList"  border
              :span-method="arraySpanMethod" :cell-style="cellStyle" ref="myTable" >
      <el-table-column label="班次" align="center" prop="shiftId"/>
      <el-table-column label="工号" align="center" prop="operatorId"/>
      <el-table-column label="通行费收入总额" align="center">
        <el-table-column label="统计金额" align="center" prop="statAmount" width="100"/>
        <el-table-column label="应缴金额" align="center" prop="dueAmount" width="100"/>
        <el-table-column label="实缴金额" align="center" prop="paidAmount" width="100"/>
        <el-table-column label="金额差异" align="center" prop="amountDiff" width="100"/>
        <el-table-column label="欠款" align="center">
          <el-table-column label="车次" align="center" prop="arrearsTrips"/>
          <el-table-column label="金额" align="center" prop="arrearsAmount"/>
        </el-table-column>
        <el-table-column label="加收款" align="center" prop="extraTotal"/>
      </el-table-column>
      <el-table-column label="移动支付" align="center" prop="mobilePaymentAmount" width="100"/>
      <el-table-column label="电子支付" align="center" prop="epaymentAmount"/>
      <el-table-column label="公务IC卡" align="center" prop="officialIcCardCount" width="100"/>
      <el-table-column label="军车IC卡" align="center" prop="militaryIcCardCount" width="100"/>
      <el-table-column label="免费IC卡" align="center" prop="freeIcCardCount" width="100"/>
      <el-table-column label="应缴IC卡" align="center" prop="dueIcCardCount" width="100"/>
    </el-table>

    <!-- Hidden iframe for printing -->
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {f2StationShift, exportF2Station} from "@/api/report/toll"

export default {
  name: "F2StationDayDetail",
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
      rules: {
      },
      stationOptions: [
      ],
      shiftOptions: [
        {label : '早班', value: 1},
        {label : '中班', value: 2},
        {label : '晚班', value: 3},
      ],
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      conditionList: [],
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
      if (row.subTotalRow === true) {
        return 'background:	#C0C0C0';
      }
    },
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (row.subTotalRow === true) {
        row.shiftId = "小计"
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
      if (row.totalRow  === true) {
        row.shiftId = "合计";
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
    },
    spanStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow  === true) {
        if (columnIndex === 0) {
          console.log(rowIndex, columnIndex);
          return [rowIndex, columnIndex];
        } else {
          return [0, 0];
        }
        // console.log('1234');
      }
    },
    tableHeaderColor({row, column, rowIndex, columnIndex}) {
      return 'background:	#C0C0C0; color:#000000; font-size: 16px;';
    },
    tableCellColor({row, column, rowIndex, columnIndex}) {
      return 'background:	#CCFFCC; color:#000000;';
    },
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      f2StationShift(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.conditionList = response.conditionList;
      }).finally(()=> {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出F2收费站通行费收入日统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportF2Station(queryParams);
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
          zoom: 0.75;
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
          width: 100%;
          border-collapse: collapse;
          table-layout: fixed; /* Ensure fixed layout */
        }
        .el-table__header-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table__body-wrapper {
          border: 1px solid #ebeef5 !important;
        }
        .el-table td {
          border: 1px solid #000 !important;
          font-size: 16px;
          padding: 1px 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
        }
        .el-table th {
          border: 1px solid #000 !important;
          font-size: 22px;
          padding: 4px; /* Reduce padding to make cells more compact */
          text-align: center; /* Center text */
          word-wrap: break-word; /* Ensure text wraps within cells */
          white-space: normal; /* Allow text to wrap */
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
          .el-table {
               width: 100% !important;
               min-width: auto !important;
          }

          .el-table__header-wrapper,
          .el-table__body-wrapper {
            overflow: visible !important;
            width: 100% !important;
          }

          .el-table__header,
          .el-table__body {
            width: 100% !important;
            transform: translateZ(0); /* 修复部分浏览器渲染问题 */
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
            <div class="print-title">F2收费站通行费收入日统计表</div>
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
.print-button-container {
  display: flex;
}

.export-button-container {
  display: flex;
}

::v-deep .el-table .el-table__header-wrapper th {
  height: 20px;
}
::v-deep .el-table--medium .el-table__cell {
  padding: 4px 0;
}
</style>
