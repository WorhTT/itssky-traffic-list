import request from '@/utils/request'

export function getExitFlow() {
  return request({
    url: '/report/exit/flow',
    method: 'get',
  })
}

