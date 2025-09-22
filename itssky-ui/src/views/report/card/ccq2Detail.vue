<template>
  <div class="app-container">
    <div
      style="display: flex;justify-content: center;flex-flow: column;flex-direction: column;flex-wrap: nowrap;align-content: center;align-items: center;padding-bottom: .5vh">
      <h3 style="font-weight: bolder;margin: 1vh 0">{{corpName}}</h3>
      <h3 style="font-weight: bolder;margin: 1vh 0">CCQ2收费中心IC卡库存日统计表</h3>
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
      <el-table-column label="收费站" align="center" prop="stationName"/>
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

import {ccq2} from "@/api/report/card";
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
      ccq2(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.conditionList = response.conditionList;
          this.corpName = response.title;
          this.operatorName = response.operatorName;
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
      this.$confirm('是否确认导出CCQ2收费中心IC卡库存日统计表?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportC1StationShift(queryParams);
      }).then(response => {
        this.downloadFile(response.msg);
      }).finally(() => {
        this.loading = false;
      })
    },
  }
};
</script>

<style lang="scss" scoped>
.el-table {
  ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    width: 15px; /*滚动条宽度*/
    height: 15px; /*滚动条高度*/
  }
}
</style>
