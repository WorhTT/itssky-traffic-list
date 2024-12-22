import request from '@/utils/request'

export function stationSelectList(data) {
  return request({
    url: '/tbstation/stationSelectList',
    method: 'get'
  })
}

//下拉框选项获取
export function listStationSelect() {
  return request({
    url: "/tbstation/listStationSelect",
    method: "get",
  });
}

