import request from '@/utils/request'

export function getExitFlow(data) {
  return request({
    url: '/report/exit/flow',
    method: 'post',
    data: data
  })
}

export function getEntryFlow(data) {
  return request({
    url: '/report/entry/flow',
    method: 'post',
    data: data
  })
}

export function getRsjRobot(data) {
  return request({
    url: '/report/rsj/robot',
    method: 'post',
    data: data
  })
}

export function getCsjRobot(data) {
  return request({
    url: '/report/csj/robot',
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

export function exportEntryFlow(data) {
  return request({
    url: '/report/export/entry/flow',
    method: 'post',
    data: data
  })
}

export function exportRsjRobot(data) {
  return request({
    url: '/report/export/rsj/robot',
    method: 'post',
    data: data
  })
}

export function exportCsjRobot(data) {
  return request({
    url: '/report/export/csj/robot',
    method: 'post',
    data: data
  })
}
