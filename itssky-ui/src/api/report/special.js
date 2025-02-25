import request from '@/utils/request'

export function greenTable(data) {
  return request({
    url: '/special/green',
    method: 'post',
    data: data
  })
}

export function exportGreenTable(data) {
  return request({
    url: '/special/export/green',
    method: 'post',
    data: data
  })
}

export function cxczTable(data) {
  return request({
    url: '/special/cxcz',
    method: 'post',
    data: data
  })
}

export function exportCxczTable(data) {
  return request({
    url: '/special/export/cxcz',
    method: 'post',
    data: data
  })
}
