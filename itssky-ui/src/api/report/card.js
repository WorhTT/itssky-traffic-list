import request from '@/utils/request'

export function s1StationShift(data) {
  return request({
    url: '/card/s1station',
    method: 'post',
    data: data
  })
}

export function sdtStation(data) {
  return request({
    url: '/card/sdtstation',
    method: 'post',
    data: data
  })
}

export function cdtStation(data) {
  return request({
    url: '/card/cdtstation',
    method: 'post',
    data: data
  })
}


export function exportCdtStation(data) {
  return request({
    url: '/card/export/cdtstation',
    method: 'post',
    data: data
  })
}
