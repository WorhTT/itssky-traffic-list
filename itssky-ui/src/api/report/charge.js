import request from '@/utils/request'

export function getCharge() {
  return request({
    url: '/report/charge',
    method: 'get',
  })
}

