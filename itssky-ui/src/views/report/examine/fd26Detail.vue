<script>

import {getFd26, exportFd26} from "@/api/report/examine";

export default {
  name: "Fd26Detail",
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
      conditionList:[]
    };
  },
  created() {
    this.queryParams = this.$route.query;
    if (this.queryParams) {
      this.getList();
    }
  },
  computed: {
    corpName() {
      return process.env.VUE_APP_CORP_NAME ? process.env.VUE_APP_CORP_NAME : '宁杭高速'
    },
  },
  methods: {
    cellStyle({row, column, rowIndex, columnIndex}) {
      if (row.totalRow === true) {
        return 'background:	#C0C0C0';
      }
    },
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      //设置为[0,0]表示隐藏
      if (row.totalRow  === true) {
        row.statDate = "总记录数";
        if (columnIndex === 0) {
          return [1, 11];
        } else if (columnIndex < 11) {
          return [0, 0];
        }
      }
    },
    getList() {
      this.loading = true;
      getFd26(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出FD27变档明细统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportFd26(queryParams);
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
            /* 在这里添加你的样式 */
        .table-container {
          zoom: 0.6;
          margin-top: 20px;
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
          overflow: hidden !important;
        }
        .el-table td {
          border: 1px solid #000000 !important;
          font-size: 20px;
          padding: 1px 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
          word-break: break-all;
          hyphens: auto;
          line-height: 2;
        }
        .el-table th {
          border: 1px solid #000000 !important;
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
        }
        @page {
          size: auto;
          margin: 5mm;
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">FD26误判率明细统计表</div>
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

<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">FD26误判率明细统计表</h3>
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

    <el-table v-loading="loading" :data="dataList" border ref="myTable" :span-method="arraySpanMethod" :cell-style="cellStyle">
      <el-table-column label="统计日期" align="center" prop="statDate"/>
      <el-table-column label="收费站名称" align="center" prop="stationName"/>
      <el-table-column label="班次" align="center" prop="shiftId" min-width="40px"/>
      <el-table-column label="收费员工号" align="center" prop="operatorId"/>
      <el-table-column label="收费员姓名" align="center" prop="operatorName"/>
      <el-table-column label="车道" align="center" prop="laneId" min-width="50px"/>
      <el-table-column label="卡号" align="center" prop="cardId" min-width="140px"/>
      <el-table-column label="车牌" align="center" prop="licensePlate" min-width="120px"/>
      <el-table-column label="收费时间" align="center" prop="tradeTimeStr" min-width="140px"/>
      <el-table-column label="改前车型" align="center" prop="beginVehicleClass" min-width="60px"/>
      <el-table-column label="入口车型" align="center" prop="entryVehicleClass" min-width="60px"/>
      <el-table-column label="收费车型" align="center" prop="tradeVehicleClass" min-width="60px"/>
      <el-table-column label="收费金额" align="center" prop="toll"/>
    </el-table>

    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<style scoped lang="scss">

</style>
