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

export function getYh(data) {
  return request({
    url: '/report/yh',
    method: 'post',
    data: data
  })
}

export function exportYh(data) {
  return request({
    url: '/report/export/yh',
    method: 'post',
    data: data
  })
}

export function crjFlow(data) {
  return request({
    url: '/report/crjflow',
    method: 'post',
    data: data
  })
}

export function exportCrjFlow(data) {
  return request({
    url: '/report/export/crjflow',
    method: 'post',
    data: data
  })
}

export function tkFlow(data) {
  return request({
    url: '/report/tkflow',
    method: 'post',
    data: data
  })
}

export function exportTkFlow(data) {
  return request({
    url: '/report/export/tkflow',
    method: 'post',
    data: data
  })
}

export function tkFlowAll(data) {
  return request({
    url: '/report/tkflow/all',
    method: 'post',
    data: data
  })
}

export function exportTkFlowAll(data) {
  return request({
    url: '/report/export/tkflow',
    method: 'post',
    data: data
  })
}
