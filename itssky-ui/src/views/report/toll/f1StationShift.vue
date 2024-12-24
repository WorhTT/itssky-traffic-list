<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="收费站" prop="stationId">
        <el-select v-model="queryParams.stationId" placeholder="请选择收费站" clearable class="custom-input">
          <el-option v-for="item in stationOptions" :key="item.label" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="统计日期:" prop="statisticsTime">
        <el-date-picker
          v-model="queryParams.time"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="班次:" prop="shiftId">
        <el-select
          v-model="queryParams.shiftId"
          class="custom-input"
          placeholder="请选择班次"
          clearable
          style="width: 240px"
          filterable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="item in shiftOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="margin-left: 40px;">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="openChildPage">查看详细报表</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import {listStationSelect} from "@/api/system/station";
import F1StationShiftDetail from "@/views/report/toll/f1StationShiftDetail";
import Router from "vue-router";

export default {
  name: "F1StationShift",
  components: {
    F1StationShiftDetail,
    Router
  },
  data() {
    return {
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
        stationId: null,
        time: null,
        shiftId: null,
      },
      currentStationId: null,
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      stationOptions: [],
      shiftOptions: [
        {label: '早班', value: 1},
        {label: '中班', value: 2},
        {label: '晚班', value: 3},
      ],
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
    };
  },
  computed: {},
  created() {
    listStationSelect().then((res) => {
      this.stationOptions = res.data.array
      this.currentStationId = res.data.defaultValue
      this.$set(this.queryParams, 'stationId', this.currentStationId);
    })
  },
  methods: {
    openChildPage() {
      const route = {
        path: '/f1StationShiftDetail',
        query: this.queryParams
      }
      const resolve = this.$router.resolve(route);
      window.open(resolve.href, '_blank')
    }
  }
};
</script>
