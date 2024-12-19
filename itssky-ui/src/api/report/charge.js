import request from '@/utils/request'

export function getCharge() {
  return request({
    url: '/report/charge',
    method: 'get',
  })
}

export function exportCharge(query) {
  return request({
    url: '/report/export',
    method: 'get',
    params: query
  })
}

