<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="收费站:" prop="stationId">
        <el-select v-model="queryParams.stationId" placeholder="请选择收费站" clearable class="custom-input">
          <el-option v-for="item in stationOptions" :key="item.label" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="统计日期:" prop="beginTime">
        <el-date-picker
          v-model="queryParams.beginTime"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期:" prop="endTime">
        <el-date-picker
          v-model="queryParams.endTime"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="统计类型:" prop="statisticsType">
        <el-select
          v-model="queryParams.statisticsType"
          class="custom-input"
          placeholder="请选择"
          clearable
          style="width: 240px"
          filterable
        >
          <el-option value="3" label="人员" key="3"/>
          <el-option value="0" label="日" key="0"/>
          <el-option value="1" label="月" key="1"/>
          <el-option value="2" label="站" key="2"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="openChildPage">查看详细报表</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import {listStationSelect} from "@/api/system/station";
import eefEPayTollDetail from "@/views/report/toll/eefEPayTollDetail";
import Router from "vue-router";

export default {
  name: "EEFEPayToll",
  components: {
    eefEPayTollDetail,
    Router
  },
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
        stationId: [],
        beginTime: null,
        endTime: null,
        statisticsType: '3'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      stationOptions: [],
      shiftOptions: [],
      pickerType: 'date',
      currentStationId: null,
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
    };
  },
  computed: {},
  created() {
    //获取收费站下拉框
    listStationSelect().then((res) => {
      this.stationOptions = res.data.array
      this.currentStationId = res.data.defaultValue
      this.$set(this.queryParams, 'stationId', this.currentStationId);
    })
  },
  watch: {},
  methods: {
    openChildPage() {
      console.log('queryparams1', this.queryParams);
      const route = {
        path: '/eefEPayTollDetail',
        query: this.queryParams
      }
      const resolve = this.$router.resolve(route);
      window.open(resolve.href, '_blank')
    }
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
