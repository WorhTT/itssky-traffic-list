import request from '@/utils/request'

export function stationSelectList(data) {
  return request({
    url: '/tbstation/stationSelectList',
    method: 'get'
  })
}
