import request from '@/utils/request'

export function getEntryFlow() {
  return request({
    url: '/entry/flow',
    method: 'get',
  })
}

