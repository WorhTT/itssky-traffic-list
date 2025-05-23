<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">CSJ出口(MTC+ETC)交通流量统计表</h3>
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
      <el-table-column label="客一" align="center" prop="k1"/>
      <el-table-column label="客二" align="center" prop="k2"/>
      <el-table-column label="客三" align="center" prop="k3"/>
      <el-table-column label="客四" align="center" prop="k4"/>
      <el-table-column label="客车小计" align="center" prop="kamount"/>
      <el-table-column label="货一" align="center" prop="h1"/>
      <el-table-column label="货二" align="center" prop="h2"/>
      <el-table-column label="货三" align="center" prop="h3"/>
      <el-table-column label="货四" align="center" prop="h4"/>
      <el-table-column label="货五" align="center" prop="h5"/>
      <el-table-column label="货六" align="center" prop="h6"/>
      <el-table-column label="货车小计" align="center" prop="hamount"/>
      <el-table-column label="专一" align="center" prop="z1"/>
      <el-table-column label="专二" align="center" prop="z2"/>
      <el-table-column label="专三" align="center" prop="z3"/>
      <el-table-column label="专四" align="center" prop="z4"/>
      <el-table-column label="专五" align="center" prop="z5"/>
      <el-table-column label="专六" align="center" prop="z6"/>
      <el-table-column label="专车小计" align="center" prop="zamount"/>
      <el-table-column label="公务" align="center" prop="official"/>
      <el-table-column label="军车" align="center" prop="military"/>
      <el-table-column label="优惠" align="center" prop="discount"/>
      <el-table-column label="免费" align="center" prop="free"/>
      <el-table-column label="车队" align="center" prop="fleet"/>
      <el-table-column label="总计" align="center" prop="allAmount"/>
    </el-table>

    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {getExitFlow, exportExitFlow} from "@/api/report/exitFlow"

export default {
  name: "CSJExitFlowDetail",
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
      getExitFlow(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出CSJ出口(MTC+ETC)交通流量统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportExitFlow(queryParams);
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
          zoom: 0.5;
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
          margin-top: 20px;
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
          padding: 1px 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
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
            <div class="print-title">CSJ出口(MTC+ETC)交通流量统计表</div>
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
