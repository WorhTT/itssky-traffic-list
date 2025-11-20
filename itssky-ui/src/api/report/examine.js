import request from '@/utils/request'

export function getFd06(data) {
  return request({
    url: '/examine/fd06',
    method: 'post',
    data: data
  })
}

export function exportFd06(data) {
  return request({
    url: '/examine/export/fd06',
    method: 'post',
    data: data
  })
}

export function getFd07(data) {
  return request({
    url: '/examine/fd07',
    method: 'post',
    data: data
  })
}

export function exportFd07(data) {
  return request({
    url: '/examine/export/fd07',
    method: 'post',
    data: data
  })
}

export function getFd27(data) {
  return request({
    url: '/examine/fd27',
    method: 'post',
    data: data
  })
}

export function exportFd27(data) {
  return request({
    url: '/examine/export/fd27',
    method: 'post',
    data: data
  })
}

export function getFd26(data) {
  return request({
    url: '/examine/fd26',
    method: 'post',
    data: data
  })
}

export function exportFd26(data) {
  return request({
    url: '/examine/export/fd26',
    method: 'post',
    data: data
  })
}

export function getFd29(data) {
  return request({
    url: '/examine/fd29',
    method: 'post',
    data: data
  })
}

export function exportFd29(data) {
  return request({
    url: '/examine/export/fd29',
    method: 'post',
    data: data
  })
}

export function getCardboxResort(data) {
  return request({
    url: '/examine/cardbox/resort',
    method: 'post',
    data: data
  })
}

export function exportCardboxResort(data) {
  return request({
    url: '/examine/export/cardbox/resort',
    method: 'post',
    data: data
  })
}
