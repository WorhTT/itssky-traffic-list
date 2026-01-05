<template>
  <div class="app-container">
    <div style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">CSJ出口机器人交通流量统计表</h3>
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
    <!-- 添加底部信息区域 -->
    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
      <span>操作人：{{ operatorName }}</span>
      <span>打印时间：{{ currentDateTime }}</span>
    </div>
    <iframe id="printFrame" style="display: none;"></iframe>
  </div>
</template>

<script>

import {getCsjRobot, exportCsjRobot} from "@/api/report/exitFlow"
import {getLoginUser} from "@/api/login";

export default {
  name: "RSJRobotDetail",
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
      getCsjRobot(this.queryParams).then(response => {
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
      this.$confirm('是否确认导出CSJ出口机器人交通流量统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportCsjRobot(queryParams);
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
              <th style="min-width: 120px;">统计方式</th>
              <th style="min-width: 70px;">客一</th>
              <th style="min-width: 50px;">客二</th>
              <th style="min-width: 50px;">客三</th>
              <th style="min-width: 50px;">客四</th>
              <th style="min-width: 90px;">客车小计</th>
              <th style="min-width: 50px;">货一</th>
              <th style="min-width: 50px;">货二</th>
              <th style="min-width: 50px;">货三</th>
              <th style="min-width: 50px;">货四</th>
              <th style="min-width: 50px;">货五</th>
              <th style="min-width: 50px;">货六</th>
              <th style="min-width: 90px;">货车小计</th>
              <th style="min-width: 50px;">专一</th>
              <th style="min-width: 50px;">专二</th>
              <th style="min-width: 50px;">专三</th>
              <th style="min-width: 50px;">专四</th>
              <th style="min-width: 50px;">专五</th>
              <th style="min-width: 50px;">专六</th>
              <th style="min-width: 90px;">专车小计</th>
              <th style="min-width: 50px;">公务</th>
              <th style="min-width: 50px;">军车</th>
              <th style="min-width: 50px;">优惠</th>
              <th style="min-width: 50px;">免费</th>
              <th style="min-width: 50px;">车队</th>
              <th style="min-width: 90px;">总计</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        tableHtml += `
          <tr>
            <td>${row.statType !== undefined && row.statType !== null ? row.statType : ''}</td>
            <td>${row.k1 !== undefined && row.k1 !== null ? row.k1 : ''}</td>
            <td>${row.k2 !== undefined && row.k2 !== null ? row.k2 : ''}</td>
            <td>${row.k3 !== undefined && row.k3 !== null ? row.k3 : ''}</td>
            <td>${row.k4 !== undefined && row.k4 !== null ? row.k4 : ''}</td>
            <td>${row.kamount !== undefined && row.kamount !== null ? row.kamount : ''}</td>
            <td>${row.h1 !== undefined && row.h1 !== null ? row.h1 : ''}</td>
            <td>${row.h2 !== undefined && row.h2 !== null ? row.h2 : ''}</td>
            <td>${row.h3 !== undefined && row.h3 !== null ? row.h3 : ''}</td>
            <td>${row.h4 !== undefined && row.h4 !== null ? row.h4 : ''}</td>
            <td>${row.h5 !== undefined && row.h5 !== null ? row.h5 : ''}</td>
            <td>${row.h6 !== undefined && row.h6 !== null ? row.h6 : ''}</td>
            <td>${row.hamount !== undefined && row.hamount !== null ? row.hamount : ''}</td>
            <td>${row.z1 !== undefined && row.z1 !== null ? row.z1 : ''}</td>
            <td>${row.z2 !== undefined && row.z2 !== null ? row.z2 : ''}</td>
            <td>${row.z3 !== undefined && row.z3 !== null ? row.z3 : ''}</td>
            <td>${row.z4 !== undefined && row.z4 !== null ? row.z4 : ''}</td>
            <td>${row.z5 !== undefined && row.z5 !== null ? row.z5 : ''}</td>
            <td>${row.z6 !== undefined && row.z6 !== null ? row.z6 : ''}</td>
            <td>${row.zamount !== undefined && row.zamount !== null ? row.zamount : ''}</td>
            <td>${row.official !== undefined && row.official !== null ? row.official : ''}</td>
            <td>${row.military !== undefined && row.military !== null ? row.military : ''}</td>
            <td>${row.discount !== undefined && row.discount !== null ? row.discount : ''}</td>
            <td>${row.free !== undefined && row.free !== null ? row.free : ''}</td>
            <td>${row.fleet !== undefined && row.fleet !== null ? row.fleet : ''}</td>
            <td>${row.allAmount !== undefined && row.allAmount !== null ? row.allAmount : ''}</td>
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
          zoom: 0.8
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
          font-size: 15px;
          min-width: 50px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格跨页 */
        }
        .el-table th {
          font-weight: bold;
          font-size: 16px;
          background-color: #f5f7fa;
          height: auto;
          line-height: 1.2;
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
          break-inside: avoid; /* 防止行跨页 */
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
            font-size: 18px;
            min-width: 40px; /* 调整最小宽度 */
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid; /* 防止单元格跨页 */
          }
          .el-table th {
            font-size: 20px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid; /* 防止表头单元格跨页 */
          }
          .container span {
            font-size: 20px;
          }
          .print-title {
            font-size: 20px;
          }
          .footer-info {
            margin-top: 15px;
            font-size: 20px;
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
            <div class="print-title">CSJ出口机器人交通流量统计表</div>
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
::v-deep .el-table--medium .el-table__cell {
  padding: 4px 0;
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
