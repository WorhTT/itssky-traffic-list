<script>

import {getFd27, exportFd27} from "@/api/report/examine";
import {getLoginUser} from "@/api/login";

export default {
  name: "Fd27Detail",
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
          return [1, 10];
        } else if (columnIndex < 10) {
          return [0, 0];
        }
      }
    },
    getList() {
      this.loading = true;
      getFd27(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出FD27变档明细统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportFd27(queryParams);
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
              <th style="min-width: 70px;">统计日期</th>
              <th style="min-width: 50px;">班次</th>
              <th style="min-width: 90px;">收费员工号</th>
              <th style="min-width: 70px;">收费员姓名</th>
              <th style="min-width: 50px;">车道</th>
              <th style="min-width: 110px;">卡号</th>
              <th style="min-width: 90px;">车牌</th>
              <th style="min-width: 110px;">收费时间</th>
              <th style="min-width: 70px;">改前车型</th>
              <th style="min-width: 70px;">入口车型</th>
              <th style="min-width: 70px;">收费车型</th>
              <th style="min-width: 70px;">收费金额</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为0的数值，确保0也能正确显示
        const statDate = row.statDate !== undefined && row.statDate !== null ? row.statDate : '';
        const shiftId = row.shiftId !== undefined && row.shiftId !== null ? row.shiftId : '';
        const operatorId = row.operatorId !== undefined && row.operatorId !== null ? row.operatorId : '';
        const operatorName = row.operatorName !== undefined && row.operatorName !== null ? row.operatorName : '';
        const laneId = row.laneId !== undefined && row.laneId !== null ? row.laneId : '';
        const cardId = row.cardId !== undefined && row.cardId !== null ? row.cardId : '';
        const licensePlate = row.licensePlate !== undefined && row.licensePlate !== null ? row.licensePlate : '';
        const tradeTimeStr = row.tradeTimeStr !== undefined && row.tradeTimeStr !== null ? row.tradeTimeStr : '';
        const beginVehicleClass = row.beginVehicleClass !== undefined && row.beginVehicleClass !== null ? row.beginVehicleClass : '';
        const entryVehicleClass = row.entryVehicleClass !== undefined && row.entryVehicleClass !== null ? row.entryVehicleClass : '';
        const tradeVehicleClass = row.tradeVehicleClass !== undefined && row.tradeVehicleClass !== null ? row.tradeVehicleClass : '';
        const toll = row.toll !== undefined && row.toll !== null ? row.toll : '';

        if (row.totalRow === true) {
          tableHtml += `
          <tr style="background: #C0C0C0">
            <td colspan="10">总记录数</td>
            <td>${tradeVehicleClass}</td>
            <td>${toll}</td>
          </tr>
        `;
        } else {
          tableHtml += `
          <tr>
            <td>${statDate}</td>
            <td>${shiftId}</td>
            <td>${operatorId}</td>
            <td>${operatorName}</td>
            <td>${laneId}</td>
            <td>${cardId}</td>
            <td>${licensePlate}</td>
            <td>${tradeTimeStr}</td>
            <td>${beginVehicleClass}</td>
            <td>${entryVehicleClass}</td>
            <td>${tradeVehicleClass}</td>
            <td>${toll}</td>
          </tr>
        `;
        }
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
            size: A4 landscape; /* 改为横向打印 */
            margin: 8mm; /* 减小边距以获得更多内容空间 */
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
            font-size: 11px;
            min-width: 40px; /* 调整最小宽度 */
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid;
          }
          .el-table th {
            font-size: 12px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid;
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
            break-inside: avoid; /* 防止行跨页 */
          }
          td, th {
            page-break-inside: avoid;
          }
        }
        </style>
        </head>
        <body>
            <div class="print-title">${corpName}</div>
            <div class="print-title">FD27变档明细统计表</div>
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
      <h3 style="font-weight: bolder;margin: 1vh 0">FD27变档明细统计表</h3>
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

    <el-table v-loading="loading" :data="dataList" border ref="myTable" :span-method="arraySpanMethod" :cell-style="cellStyle" >
      <el-table-column label="统计日期" align="center" prop="statDate"/>
      <el-table-column label="班次" align="center" prop="shiftId" min-width="50px"/>
      <el-table-column label="收费员工号" align="center" prop="operatorId" min-width="120px"/>
      <el-table-column label="收费员姓名" align="center" prop="operatorName" min-width="120px"/>
      <el-table-column label="车道" align="center" prop="laneId" min-width="50px"/>
      <el-table-column label="卡号" align="center" prop="cardId" min-width="140px"/>
      <el-table-column label="车牌" align="center" prop="licensePlate"/>
      <el-table-column label="收费时间" align="center" prop="tradeTimeStr" min-width="140px"/>
      <el-table-column label="改前车型" align="center" prop="beginVehicleClass" min-width="80px"/>
      <el-table-column label="入口车型" align="center" prop="entryVehicleClass" min-width="80px"/>
      <el-table-column label="收费车型" align="center" prop="tradeVehicleClass" min-width="80px"/>
      <el-table-column label="收费金额" align="center" prop="toll"/>
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
::v-deep .el-table .el-table__header-wrapper th {
  height: 20px;
}
::v-deep .el-table__header-wrapper {
  & thead {
    tr {
      background-color: #f5f7fa !important; // 设置表头行背景色
    }
  }
}

::v-deep .el-table th {
  background-color: #f5f7fa !important; // 确保表头单元格背景色
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
