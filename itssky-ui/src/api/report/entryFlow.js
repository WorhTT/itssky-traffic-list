import request from '@/utils/request'

export function getEntryFlow() {
  return request({
    url: '/report/entry/flow',
    method: 'get',
  })
}

