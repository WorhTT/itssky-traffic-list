<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">TK入出口(MTC)交通流量按车种统计表</h3>
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
      <el-table-column label="入口" align="center" prop="entry">
        <el-table-column label="客车" align="center" prop="rkc"/>
        <el-table-column label="货车" align="center" prop="rhc"/>
        <el-table-column label="专车" align="center" prop="rzc"/>
        <el-table-column label="公务车" align="center" prop="rgw"/>
        <el-table-column label="军车" align="center" prop="rjc"/>
        <el-table-column label="优惠车" align="center" prop="ryh"/>
        <el-table-column label="车队" align="center" prop="rcd"/>
        <el-table-column label="入口小计" align="center" prop="rsum"/>
      </el-table-column>
      <el-table-column label="出口" align="center" prop="exit">
        <el-table-column label="客车" align="center" prop="ckc"/>
        <el-table-column label="货车" align="center" prop="chc"/>
        <el-table-column label="专车" align="center" prop="czc"/>
        <el-table-column label="现金" align="center" prop="cxj"/>
        <el-table-column label="电子支付" align="center" prop="cepay"/>
        <el-table-column label="移动支付" align="center" prop="cmpay"/>
        <el-table-column label="公务车" align="center" prop="cgw"/>
        <el-table-column label="军车" align="center" prop="cjc"/>
        <el-table-column label="优惠车" align="center" prop="cyh"/>
        <el-table-column label="免费车" align="center" prop="cmf"/>
        <el-table-column label="车队" align="center" prop="rcd"/>
        <el-table-column label="出口小计" align="center" prop="csum"/>
      </el-table-column>
      <el-table-column label="总计" align="center" prop="sumCount"/>
    </el-table>

    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {tkFlow, exportTkFlow} from "@/api/report/exitFlow"

export default {
  name: "TKFlowDetail",
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
    getList() {
      this.loading = true;
      tkFlow(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.conditionList = response.conditionList;
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出TK入出口(MTC)交通流量按车种统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportTkFlow(queryParams);
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
        }
        .el-table td {
          border: 1px solid #000000 !important;
          font-size: 20px;
          padding: 20px 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
        }
        .el-table th {
          border: 1px solid #000000 !important;
          font-size: 30px;
          padding: 10px; /* Reduce padding to make cells more compact */
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
          margin: 5mm;
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">TK入出口(MTC)交通流量按车种统计表</div>
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
::v-deep .el-table .el-table__header-wrapper th {
  height: 20px;
}
::v-deep .el-table--medium .el-table__cell {
  padding: 4px 0;
}
</style>
