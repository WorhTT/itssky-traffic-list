<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">CCQ3收费中心IC卡库存月统计汇总表</h3>
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

    <el-table v-loading="loading" :data="dataList" border ref="myTable">
      <el-table-column label="日期" align="center" prop="staDate"/>
      <el-table-column label="库存增加" align="center">
        <el-table-column label="通行卡调入" align="center" prop="txkdr"/>
        <el-table-column label="出口回收" align="center" prop="ckhs"/>
        <el-table-column label="坏卡回收" align="center" prop="hkhs"/>
        <el-table-column label="通行卡恢复" align="center" prop="txkhf"/>
      </el-table-column>
      <el-table-column label="库存减少" align="center">
        <el-table-column label="通行卡调出" align="center" prop="txkdc"/>
        <el-table-column label="入口发卡" align="center" prop="rkfk"/>
        <el-table-column label="坏卡上缴" align="center" prop="hksj"/>
      </el-table-column>
      <el-table-column label="库存维护数" align="center" prop="kcwhs"/>
      <el-table-column label="库存变动" align="center" prop="kcbd"/>
      <el-table-column label="库存坏卡" align="center" prop="kchk"/>
      <el-table-column label="库存正常卡" align="center" prop="kczck"/>
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

import {ccq3, exportCcq3} from "@/api/report/card";
import {getLoginUser} from "@/api/login";

export default {
  name: "C1StationShiftDetail",
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
      conditionList: [],
      shiftOptions: [
        {label: '早班', value: 1},
        {label: '中班', value: 2},
        {label: '晚班', value: 3},
      ],
      pickerType: 'date',
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      operatorName: '',
      corpName: '',
    };
  },
  computed: {
    currentDateTime() {
      return this.getCurrentDateTime();
    },
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
    getList() {
      this.loading = true;
      ccq3(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.conditionList = response.conditionList;
          this.corpName = response.title;
      }).catch(err=>{
        console.error("异常：{}",err)
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.loading = true;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出CCQ3收费中心IC卡库存月统计汇总表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "警告"
      }).then(function () {
        return exportCcq3(queryParams);
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
      // 获取操作人信息
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
              <th rowspan="2" style="min-width: 100px;">日期</th>
              <th colspan="4" style="min-width: 200px;">库存增加</th>
              <th colspan="3" style="min-width: 150px;">库存减少</th>
              <th rowspan="2" style="min-width: 80px;">库存维护数</th>
              <th rowspan="2" style="min-width: 80px;">库存变动</th>
              <th rowspan="2" style="min-width: 80px;">库存坏卡</th>
              <th rowspan="2" style="min-width: 100px;">库存正常卡</th>
            </tr>
            <tr>
              <th style="min-width: 50px;">通行卡调入</th>
              <th style="min-width: 50px;">出口回收</th>
              <th style="min-width: 50px;">坏卡回收</th>
              <th style="min-width: 50px;">通行卡恢复</th>
              <th style="min-width: 50px;">通行卡调出</th>
              <th style="min-width: 50px;">入口发卡</th>
              <th style="min-width: 50px;">坏卡上缴</th>
            </tr>
          </thead>
          <tbody>
      `;

      // 填充表格数据
      this.dataList.forEach(row => {
        // 处理可能为空的数值，确保空值也能正确显示
        const staDate = row.staDate !== undefined && row.staDate !== null ? row.staDate : '';
        const txkdr = row.txkdr !== undefined && row.txkdr !== null ? row.txkdr : '';
        const ckhs = row.ckhs !== undefined && row.ckhs !== null ? row.ckhs : '';
        const hkhs = row.hkhs !== undefined && row.hkhs !== null ? row.hkhs : '';
        const txkhf = row.txkhf !== undefined && row.txkhf !== null ? row.txkhf : '';
        const txkdc = row.txkdc !== undefined && row.txkdc !== null ? row.txkdc : '';
        const rkfk = row.rkfk !== undefined && row.rkfk !== null ? row.rkfk : '';
        const hksj = row.hksj !== undefined && row.hksj !== null ? row.hksj : '';
        const kcwhs = row.kcwhs !== undefined && row.kcwhs !== null ? row.kcwhs : '';
        const kcbd = row.kcbd !== undefined && row.kcbd !== null ? row.kcbd : '';
        const kchk = row.kchk !== undefined && row.kchk !== null ? row.kchk : '';
        const kczck = row.kczck !== undefined && row.kczck !== null ? row.kczck : '';

        tableHtml += `
          <tr>
            <td>${staDate}</td>
            <td>${txkdr}</td>
            <td>${ckhs}</td>
            <td>${hkhs}</td>
            <td>${txkhf}</td>
            <td>${txkdc}</td>
            <td>${rkfk}</td>
            <td>${hksj}</td>
            <td>${kcwhs}</td>
            <td>${kcbd}</td>
            <td>${kchk}</td>
            <td>${kczck}</td>
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
          zoom: 0.6
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
          min-width: 50px;
          word-break: break-word; /* 允许单词内换行 */
          break-inside: avoid; /* 防止单元格跨页 */
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
            font-size: 15px;
            min-width: 40px; /* 调整最小宽度 */
            white-space: normal;
            word-wrap: break-word;
            word-break: break-word;
            break-inside: avoid; /* 防止单元格跨页 */
          }
          .el-table th {
            font-size: 17px; /* 表头字体稍大 */
            font-weight: bold;
            break-inside: avoid; /* 防止表头单元格跨页 */
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
            <div class="print-title">CCQ3收费中心IC卡库存月统计汇总表</div>
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
