<script>

import {getCurrentTime, getMidnightTime} from "@/utils/dateUtils";
import {listStationSelect} from "@/api/system/station";
import dayjs from 'dayjs'  // 推荐使用dayjs处理日期

export default {
  name: "Fd26",
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
        time: null,
        beginVehicleType: '0',
        endVehicleType: '0',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      stationOptions: [],
      shiftOptions: [],
      pickerType: 'date',
      pickOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
    };
  },
  created() {
    //获取收费站下拉框
    this.queryParams.time = getCurrentTime();
    listStationSelect({needCenter: true}).then((res) => {
      this.stationOptions = res.data.array
      this.currentStationId = res.data.defaultValue
      this.$set(this.queryParams, 'stationId', this.currentStationId);
    })
  },
  methods: {
    openChildPage() {
      if (!this.queryParams.time) {
        this.$message.error('请选择完整的时间范围')
        return
      }
      // if (!this.queryParams.beginTime || !this.queryParams.endTime) {
      //   this.$message.error('请选择完整的时间范围')
      //   return
      // }
      // // 创建日期对象
      // const start = dayjs(this.queryParams.beginTime)
      // const end = dayjs(this.queryParams.endTime)
      // // 跨年校验
      // console.log('start:', start);
      // console.log('end:', end);
      // if (start.year() !== end.year()) {
      //   this.$message.error('时间范围不能跨年')
      //   return
      // }
      //
      // // 跨月校验
      // if (start.month() !== end.month()) {
      //   this.$message.error('时间范围不能跨月')
      //   return
      // }
      const route = {
        path: '/fd26Detail',
        query: this.queryParams
      }
      const resolve = this.$router.resolve(route);
      window.open(resolve.href, '_blank')
    }
  }
};

</script>

<template>

  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="收费站:" prop="stationId">
        <el-select v-model="queryParams.stationId" placeholder="请选择收费站" clearable class="custom-input" style="width: 150px;">
          <el-option v-for="item in stationOptions" :key="item.label" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="统计日期:" prop="time">
        <el-date-picker
          v-model="queryParams.time"
          placeholder="请选择日期"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickOptions"
          style="width: 150px;"
        >
        </el-date-picker>
      </el-form-item>
<!--      <el-form-item label="结束日期:" prop="statisticsTime">-->
<!--        <el-date-picker-->
<!--          v-model="queryParams.endTime"-->
<!--          placeholder="请选择日期"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          :picker-options="pickOptions"-->
<!--          style="width:  150px;"-->
<!--        >-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
      <el-form-item label="前车型:" prop="beginVehicleType">
        <el-select
          v-model="queryParams.beginVehicleType"
          class="custom-input"
          placeholder="请选择"
          clearable
          style="width: 120px"
          filterable
        >
          <el-option value="0" label="全部" key="0"/>
          <el-option value="1" label="客车" key="1"/>
          <el-option value="2" label="货车" key="2"/>
          <el-option value="3" label="专车" key="3"/>
        </el-select>
      </el-form-item>
      <el-form-item label="后车型:" prop="endVehicleType">
        <el-select
          v-model="queryParams.endVehicleType"
          class="custom-input"
          placeholder="请选择"
          clearable
          style="width: 120px"
          filterable
        >
          <el-option value="0" label="全部" key="0"/>
          <el-option value="1" label="客车" key="1"/>
          <el-option value="2" label="货车" key="2"/>
          <el-option value="3" label="专车" key="3"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="openChildPage">查看详细报表</el-button>
      </el-form-item>
    </el-form>
  </div>

</template>

<style scoped lang="scss">

</style>
