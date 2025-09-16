<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">MOB移动支付收费统计报表</h3>
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

    <el-table v-loading="loading" :data="dataList" border style="width: 100%" fit ref="myTable">
      <el-table-column label="统计方式" align="center" prop="statType" min-width="100"/>
      <el-table-column label="银联支付" align="center">
        <el-table-column label="笔数" align="center" prop="ylCount" min-width="80"/>
        <el-table-column label="金额" align="center" prop="ylToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="微信支付" align="center">
        <el-table-column label="笔数" align="center" prop="wxCount" min-width="80"/>
        <el-table-column label="金额" align="center" prop="wxToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="支付宝支付" align="center">
        <el-table-column label="笔数" align="center" prop="zfbCount" min-width="80"/>
        <el-table-column label="金额" align="center" prop="zfbToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="百度支付" align="center">
        <el-table-column label="笔数" align="center" prop="bdCount" min-width="50"/>
        <el-table-column label="金额" align="center" prop="bdToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="京东支付" align="center">
        <el-table-column label="笔数" align="center" prop="jdCount" min-width="50"/>
        <el-table-column label="金额" align="center" prop="jdToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="通行宝" align="center">
        <el-table-column label="笔数" align="center" prop="txbCount" min-width="50"/>
        <el-table-column label="金额" align="center" prop="txbToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="数字人民币" align="center">
        <el-table-column label="笔数" align="center" prop="szrmbCount" min-width="50"/>
        <el-table-column label="金额" align="center" prop="szrmbToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="其他" align="center">
        <el-table-column label="笔数" align="center" prop="qtCount" min-width="50"/>
        <el-table-column label="金额" align="center" prop="qtToll" min-width="100"/>
      </el-table-column>
      <el-table-column label="合计" align="center">
        <el-table-column label="笔数" align="center" prop="hjCount" min-width="80"/>
        <el-table-column label="金额" align="center" prop="hjToll" min-width="100"/>
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

import {mobToll, exportMobToll} from "@/api/report/toll"
import {getLoginUser} from "@/api/login";

export default {
  name: "MOBTollDetail",
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
      operatorName: '',
      corpName: '',
    };
  },
  computed: {
    // corpName() {
    //   return process.env.VUE_APP_CORP_NAME ? process.env.VUE_APP_CORP_NAME : '宁杭高速'
    // },
    currentDateTime() {
      return this.getCurrentDateTime();
    },
  },
  created() {
    this.queryParams = this.$route.query;
    console.log('queryparams2', this.queryParams);
    if (this.queryParams) {
      this.getList();
    }
    getLoginUser().then(res => {
      if (res.data) {
        this.operatorName = res.data.username;
      }
    })
  },
  watch: {},
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
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      mobToll(this.queryParams).then(response => {
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
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出MOB移动支付收费统计报表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportMobToll(queryParams);
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
      // 获取操作人信息（这里假设您有存储操作人的方式）
      const operator = this.operatorName;
      const printTime = this.getCurrentDateTime();
      // 添加底部信息行
      const footerHtml = `
    <div class="footer-info">
      <span class="operator">操作人：${operator}</span>
      <span class="print-time">打印时间：${printTime}</span>
    </div>
  `;
      let htmlContent = `
       <!DOCTYPE html>
        <html>
        <head>
        <title>Print</title>
        <style>
        .table-container {
          zoom: 0.8;
          width: 100% !important;
          max-width: 100vw !important;
          overflow: visible !important;
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
            width: max-content !important; /* 允许表格根据内容扩展 */
            min-width: 100% !important;
            table-layout: auto !important; /* 自动列宽模式 */
            font-size: 12px !important;     /* 基础字号缩小 */
            margin-top: 20px;
        }
        .el-table__body-wrapper, .el-table__header-wrapper {
        border: none !important; /* 移除容器边框 */
        }

        .el-table td, .el-table th {
        border: 1px solid #000 !important; /* 保留单元格边框 */
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
.footer-info {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
    font-size: 14px;
}
.operator {
    text-align: left;
}
.print-time {
    text-align: right;
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
        }
        @page {
          size: auto;
          margin: 2mm 2mm;
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">MOB移动支付收费统计报表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${elTable.outerHTML}</div>
            ${footerHtml}
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
