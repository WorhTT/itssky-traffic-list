import request from '@/utils/request'

export function getEntryFlow(query) {
  return request({
    url: '/report/entry/flow',
    method: 'get',
    params: query
  })
}

