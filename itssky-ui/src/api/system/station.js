import request from '@/utils/request'

export function stationSelectList(data) {
  return request({
    url: '/tbstation/stationSelectList',
    method: 'get'
  })
}

//下拉框选项获取
export function listStationSelect(query) {
  return request({
    url: "/tbstation/listStationSelect",
    method: "get",
    params: query
  });
}

export function listStationSelectV2(query) {
  return request({
    url: "/tbstation/listStationSelect/v2",
    method: "get",
    params: query
  });
}


//根据权限返回的中心及分中心下拉框
export function centerOptions() {
  return request({
    url: '/tbstation/center/options',
    method: 'get',
  })
}

