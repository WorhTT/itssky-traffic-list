import request from '@/utils/request'

export function f1StationShift(data) {
  return request({
    url: '/toll/f1station',
    method: 'post',
    data: data
  })
}

export function exportF1Station(data) {
  return request({
    url: '/toll/export/f1station',
    method: 'post',
    data: data
  })
}

export function f2StationShift(data) {
  return request({
    url: '/toll/f2station',
    method: 'post',
    data: data
  })
}

export function exportF2Station(data) {
  return request({
    url: '/toll/export/f2station',
    method: 'post',
    data: data
  })
}

export function ftToll(data) {
  return request({
    url: '/toll/fttoll',
    method: 'post',
    data: data
  })
}

export function exportFtToll(data) {
  return request({
    url: '/toll/export/fttoll',
    method: 'post',
    data: data
  })
}

export function afvGeneral(data) {
  return request({
    url: '/toll/afvgeneral',
    method: 'post',
    data: data
  })
}

export function exportAfvGeneral(data) {
  return request({
    url: '/toll/export/afvgeneral',
    method: 'post',
    data: data
  })
}

export function eefepay(data) {
  return request({
    url: '/toll/eefepay',
    method: 'post',
    data: data
  })
}

export function exportEefepay(data) {
  return request({
    url: '/toll/export/eefepay',
    method: 'post',
    data: data
  })
}
