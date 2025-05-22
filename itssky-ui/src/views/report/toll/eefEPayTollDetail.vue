<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">EEF电子支付通行费(MTC+ETC)统计表</h3>
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
            class="print-button-container"
            @click="printTable"
          >打印
          </el-button>
        </el-col>
      </el-row>
    </div>

    <el-table v-loading="loading" :data="dataList" border style="width: 100%" fit ref="myTable" >
      <el-table-column label="统计方式" align="center" prop="statType" width="100"/>
      <el-table-column label="客车" align="center">
        <el-table-column label="客一" align="center">
          <el-table-column label="C卡" align="center" prop="cust1C" width="100"/>
          <el-table-column label="D卡" align="center" prop="cust1D" width="100"/>
          <el-table-column label="合计" align="center" prop="cust1Sum" width="100"/>
        </el-table-column>
        <el-table-column label="客二" align="center">
          <el-table-column label="C卡" align="center" prop="cust2C" />
          <el-table-column label="D卡" align="center" prop="cust2D" />
          <el-table-column label="合计" align="center" prop="cust2Sum" width="100"/>
        </el-table-column>
        <el-table-column label="客三" align="center">
          <el-table-column label="C卡" align="center" prop="cust3C" />
          <el-table-column label="D卡" align="center" prop="cust3D" />
          <el-table-column label="合计" align="center" prop="cust3Sum" width="100"/>
        </el-table-column>
        <el-table-column label="客四" align="center" prop="k4Count">
          <el-table-column label="C卡" align="center" prop="cust4C" />
          <el-table-column label="D卡" align="center" prop="cust4D" />
          <el-table-column label="合计" align="center" prop="cust4Sum" width="100"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="custCSubTotal" width="100"/>
          <el-table-column label="D卡" align="center" prop="custDSubTotal" width="100"/>
          <el-table-column label="合计" align="center" prop="custSubSum" width="100"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="货车" align="center">
        <el-table-column label="货一" align="center">
          <el-table-column label="C卡" align="center" prop="trust1C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust1D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust1Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货二" align="center">
          <el-table-column label="C卡" align="center" prop="trust2C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust2D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust2Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货三" align="center">
          <el-table-column label="C卡" align="center" prop="trust3C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust3D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust3Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货四" align="center">
          <el-table-column label="C卡" align="center" prop="trust4C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust4D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust4Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货五" align="center">
          <el-table-column label="C卡" align="center" prop="trust5C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust5D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust5Sum" width="100"/>
        </el-table-column>
        <el-table-column label="货六" align="center">
          <el-table-column label="C卡" align="center" prop="trust6C" width="100"/>
          <el-table-column label="D卡" align="center" prop="trust6D" width="100"/>
          <el-table-column label="合计" align="center" prop="trust6Sum" width="100"/>
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="trustCSubTotal" width="100"/>
          <el-table-column label="D卡" align="center" prop="trustDSubTotal" width="100"/>
          <el-table-column label="合计" align="center" prop="trustSubSum" width="100"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="专车" align="center">
        <el-table-column label="专一" align="center">
          <el-table-column label="C卡" align="center" prop="spec1C" />
          <el-table-column label="D卡" align="center" prop="spec1D" />
          <el-table-column label="合计" align="center" prop="spec1Sum" />
        </el-table-column>
        <el-table-column label="专二" align="center">
          <el-table-column label="C卡" align="center" prop="spec2C" />
          <el-table-column label="D卡" align="center" prop="spec2D" />
          <el-table-column label="合计" align="center" prop="spec2Sum" />
        </el-table-column>
        <el-table-column label="专三" align="center">
          <el-table-column label="C卡" align="center" prop="spec3C" />
          <el-table-column label="D卡" align="center" prop="spec3D" />
          <el-table-column label="合计" align="center" prop="spec3Sum" />
        </el-table-column>
        <el-table-column label="专四" align="center">
          <el-table-column label="C卡" align="center" prop="spec4C" />
          <el-table-column label="D卡" align="center" prop="spec4D" />
          <el-table-column label="合计" align="center" prop="spec4Sum" />
        </el-table-column>
        <el-table-column label="专五" align="center">
          <el-table-column label="C卡" align="center" prop="spec5C" />
          <el-table-column label="D卡" align="center" prop="spec5D" />
          <el-table-column label="合计" align="center" prop="spec5Sum" />
        </el-table-column>
        <el-table-column label="专六" align="center">
          <el-table-column label="C卡" align="center" prop="spec6C" />
          <el-table-column label="D卡" align="center" prop="spec6D" />
          <el-table-column label="合计" align="center" prop="spec6Sum" />
        </el-table-column>
        <el-table-column label="小计" align="center">
          <el-table-column label="C卡" align="center" prop="specCSubTotal" />
          <el-table-column label="D卡" align="center" prop="specDSubTotal" />
          <el-table-column label="合计" align="center" prop="specSubSum"/>
        </el-table-column>
      </el-table-column>
      <el-table-column label="总计" align="center">
        <el-table-column label="C卡" align="center" prop="ctotal" width="120"/>
        <el-table-column label="D卡" align="center" prop="dtotal" width="120"/>
        <el-table-column label="合计" align="center" prop="totalSum" width="120"/>
      </el-table-column>
    </el-table>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {eefepay, exportEefepay} from "@/api/report/toll"

export default {
  name: "EEFEPayTollDetail",
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
      shiftOptions: [],
      pickerType: 'date',
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
    console.log('queryparams2', this.queryParams);
    if (this.queryParams) {
      this.getList();
    }
  },
  watch: {},
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      eefepay(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出EEF电子支付通行费(MTC+ETC)统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportEefepay(queryParams);
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
          zoom: 0.2
        }
        .print-title {
          text-align: center;
          font-size: 24px;
          font-weight: bold;
          margin-bottom: 20px;
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
          font-size: 16px;
          padding: 0 0;
          text-align: center; /* Center text */
          word-wrap: break-word;
          white-space: normal; /* Prevent text from wrapping */
        }
        .el-table th {
          border: 1px solid #000000 !important;
          font-size: 16px;
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
            /*transform: scale(0.8);  !* 初始缩放系数 *!*/
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
          margin: 0mm;
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">EEF电子支付通行费(MTC+ETC)统计表</div>
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
</style>
