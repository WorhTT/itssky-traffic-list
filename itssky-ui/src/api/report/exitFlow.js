import request from '@/utils/request'

export function getExitFlow(data) {
  return request({
    url: '/report/exit/flow',
    method: 'post',
    data: data
  })
}

export function exportExitFlow(data) {
  return request({
    url: '/report/export/exit/flow',
    method: 'post',
    data: data
  })
}

