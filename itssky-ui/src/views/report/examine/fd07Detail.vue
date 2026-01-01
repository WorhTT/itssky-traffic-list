<script>

import {getFd07, exportFd07} from "@/api/report/examine";
import {getLoginUser} from "@/api/login";

export default {
  name: "Fd07Detail",
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
      getFd07(this.queryParams).then(response => {
        this.dataList = response.rows;
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
      this.$confirm('是否确认导出FD07收费员收费统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportFd07(queryParams);
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
              <th rowspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">收费员工号</th>
              <th rowspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">收费员姓名</th>
              <th rowspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">收费流量</th>
              <th colspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">回收卡</th>
              <th colspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">现金收入</th>
              <th rowspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">电子支付</th>
              <th rowspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">移动支付</th>
              <th rowspan="2" style="word-wrap: break-word; word-break: break-all; white-space: normal;">收费金额合计</th>
            </tr>
            <tr>
              <th style="word-wrap: break-word; word-break: break-all; white-space: normal;">应缴卡数</th>
              <th style="word-wrap: break-word; word-break: break-all; white-space: normal;">实缴卡数</th>
              <th style="word-wrap: break-word; word-break: break-all; white-space: normal;">应缴金额</th>
              <th style="word-wrap: break-word; word-break: break-all; white-space: normal;">实缴金额</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const operatorId = row.operatorId !== undefined && row.operatorId !== null ? row.operatorId : '';
        const operatorName = row.operatorName !== undefined && row.operatorName !== null ? row.operatorName : '';
        const totalCarNum = row.totalCarNum !== undefined && row.totalCarNum !== null ? row.totalCarNum : '';
        const yjCardNum = row.yjCardNum !== undefined && row.yjCardNum !== null ? row.yjCardNum : '';
        const sjCardNum = row.sjCardNum !== undefined && row.sjCardNum !== null ? row.sjCardNum : '';
        const yjCash = row.yjCash !== undefined && row.yjCash !== null ? row.yjCash : '';
        const sjCash = row.sjCash !== undefined && row.sjCash !== null ? row.sjCash : '';
        const epay = row.epay !== undefined && row.epay !== null ? row.epay : '';
        const mpay = row.mpay !== undefined && row.mpay !== null ? row.mpay : '';
        const totalToll = row.totalToll !== undefined && row.totalToll !== null ? row.totalToll : '';

        tableHtml += `
          <tr>
            <td>${operatorId}</td>
            <td>${operatorName}</td>
            <td>${totalCarNum}</td>
            <td>${yjCardNum}</td>
            <td>${sjCardNum}</td>
            <td>${yjCash}</td>
            <td>${sjCash}</td>
            <td>${epay}</td>
            <td>${mpay}</td>
            <td>${totalToll}</td>
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
          vertical-align: middle;
        }
        .el-table th {
          font-weight: bold;
          font-size: 14px;
          background-color: #f5f7fa;
          height: auto;
          line-height: 1.2;
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
            size: A4 landscape;
            margin: 10mm;
          }
          body {
            -webkit-print-color-adjust: exact;
            color-adjust: exact;
            padding: 0;
            margin: 0;
            width: 100%;
            font-size: 12px;
          }
          .el-table {
            width: 100% !important;
            table-layout: auto !important; /* 自动调整列宽 */
            font-size: 12px;
          }
          .el-table th, .el-table td {
            padding: 6px 4px;
            font-size: 12px;
            min-width: 50px;
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word; /* 允许单词内换行 */
            vertical-align: middle;
          }
          .el-table th {
            font-size: 13px;
            font-weight: bold;
            height: auto;
            line-height: 1.2;
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
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">FD07收费员收费统计表</div>
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

<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">FD07收费员收费统计表</h3>
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
      <el-table-column label="收费员工号" align="center" prop="operatorId"/>
      <el-table-column label="收费员姓名" align="center" prop="operatorName"/>
      <el-table-column label="收费流量" align="center" prop="totalCarNum"/>
      <el-table-column label="回收卡" align="center">
        <el-table-column label="应缴卡数" align="center" prop="yjCardNum"/>
        <el-table-column label="实缴卡数" align="center" prop="sjCardNum"/>
      </el-table-column>
      <el-table-column label="现金收入" align="center">
        <el-table-column label="应缴金额" align="center" prop="yjCash"/>
        <el-table-column label="实缴金额" align="center" prop="yjCash"/>
      </el-table-column>
      <el-table-column label="电子支付" align="center" prop="epay"/>
      <el-table-column label="移动支付" align="center" prop="mpay"/>
      <el-table-column label="收费金额合计" align="center" prop="totalToll"/>
    </el-table>
    <!-- 添加底部信息区域 -->
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
      <span>操作人：{{ operatorName }}</span>
      <span>打印时间：{{ currentDateTime }}</span>
    </div>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<style scoped lang="scss">
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
