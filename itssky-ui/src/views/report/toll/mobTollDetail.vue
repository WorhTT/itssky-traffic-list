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
      // 创建一个新的表格结构，避免样式冲突
      let tableHtml = `
        <table class="el-table">
          <thead>
            <tr>
              <th rowspan="2">统计方式</th>
              <th colspan="2">银联支付</th>
              <th colspan="2">微信支付</th>
              <th colspan="2">支付宝支付</th>
              <th colspan="2">百度支付</th>
              <th colspan="2">京东支付</th>
              <th colspan="2">通行宝</th>
              <th colspan="2">数字人民币</th>
              <th colspan="2">其他</th>
              <th colspan="2">合计</th>
            </tr>
            <tr>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
              <th>笔数</th>
              <th>金额</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const statType = row.statType !== undefined && row.statType !== null ? row.statType : '';
        const ylCount = row.ylCount !== undefined && row.ylCount !== null ? row.ylCount : '';
        const ylToll = row.ylToll !== undefined && row.ylToll !== null ? row.ylToll : '';
        const wxCount = row.wxCount !== undefined && row.wxCount !== null ? row.wxCount : '';
        const wxToll = row.wxToll !== undefined && row.wxToll !== null ? row.wxToll : '';
        const zfbCount = row.zfbCount !== undefined && row.zfbCount !== null ? row.zfbCount : '';
        const zfbToll = row.zfbToll !== undefined && row.zfbToll !== null ? row.zfbToll : '';
        const bdCount = row.bdCount !== undefined && row.bdCount !== null ? row.bdCount : '';
        const bdToll = row.bdToll !== undefined && row.bdToll !== null ? row.bdToll : '';
        const jdCount = row.jdCount !== undefined && row.jdCount !== null ? row.jdCount : '';
        const jdToll = row.jdToll !== undefined && row.jdToll !== null ? row.jdToll : '';
        const txbCount = row.txbCount !== undefined && row.txbCount !== null ? row.txbCount : '';
        const txbToll = row.txbToll !== undefined && row.txbToll !== null ? row.txbToll : '';
        const szrmbCount = row.szrmbCount !== undefined && row.szrmbCount !== null ? row.szrmbCount : '';
        const szrmbToll = row.szrmbToll !== undefined && row.szrmbToll !== null ? row.szrmbToll : '';
        const qtCount = row.qtCount !== undefined && row.qtCount !== null ? row.qtCount : '';
        const qtToll = row.qtToll !== undefined && row.qtToll !== null ? row.qtToll : '';
        const hjCount = row.hjCount !== undefined && row.hjCount !== null ? row.hjCount : '';
        const hjToll = row.hjToll !== undefined && row.hjToll !== null ? row.hjToll : '';

        tableHtml += `
          <tr>
            <td>${statType}</td>
            <td>${ylCount}</td>
            <td>${ylToll}</td>
            <td>${wxCount}</td>
            <td>${wxToll}</td>
            <td>${zfbCount}</td>
            <td>${zfbToll}</td>
            <td>${bdCount}</td>
            <td>${bdToll}</td>
            <td>${jdCount}</td>
            <td>${jdToll}</td>
            <td>${txbCount}</td>
            <td>${txbToll}</td>
            <td>${szrmbCount}</td>
            <td>${szrmbToll}</td>
            <td>${qtCount}</td>
            <td>${qtToll}</td>
            <td>${hjCount}</td>
            <td>${hjToll}</td>
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
          font-size: 13px;
          min-width: 60px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格内容跨页 */
        }
        .el-table th {
          font-weight: bold;
          font-size: 14px;
          background-color: #f5f7fa;
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
        }
        td, th {
          page-break-inside: avoid;
        }
        .footer-info {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
            font-size: 13px;
        }
        .operator {
            text-align: left;
        }
        .print-time {
            text-align: right;
        }
        @media print {
          @page {
            size: A4 landscape; /* 横向打印 */
            margin: 8mm;
          }
          body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0;
            margin: 0;
            width: 100%;
            font-size: 11px; /* 调小字体 */
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 11px;
          }
          .el-table th, .el-table td {
            padding: 4px 3px; /* 减小内边距 */
            font-size: 14px;
            min-width: 40px;
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid;
          }
          .el-table th {
            font-size: 15px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid;
          }
          .container span {
            font-size: 15px;
          }
          .print-title {
            font-size: 18px;
          }
          .footer-info {
            margin-top: 10px;
            font-size: 15px;
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
            break-inside: avoid;
          }
          td, th {
            page-break-inside: avoid;
          }
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">MOB移动支付收费统计报表</div>
            <div class="container">${conditionListHtml}</div>
            <div class="table-container">${tableHtml}</div>
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
