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

