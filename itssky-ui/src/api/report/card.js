import request from '@/utils/request'

export function s1StationShift(data) {
  return request({
    url: '/card/s1station',
    method: 'post',
    data: data
  })
}

export function exportS1StationShift(data) {
  return request({
    url: '/card/export/s1station',
    method: 'post',
    data: data
  })
}

export function exportS2StationShift(data) {
  return request({
    url: '/card/export/s2station',
    method: 'post',
    data: data
  })
}

export function exportC1StationShift(data) {
  return request({
    url: '/card/export/c1station',
    method: 'post',
    data: data
  })
}

export function exportC2StationShift(data) {
  return request({
    url: '/card/export/c2station',
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

export function exportSdtStation(data) {
  return request({
    url: '/card/export/sdtstation',
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

export function ccq2(data) {
  return request({
    url: '/card/ccq2',
    method: 'post',
    data: data
  })
}

export function ccq3(data) {
  return request({
    url: '/card/ccq3',
    method: 'post',
    data: data
  })
}

export function exportCcq2(data) {
  return request({
    url: '/card/export/ccq2',
    method: 'post',
    data: data
  })
}

export function exportCcq3(data) {
  return request({
    url: '/card/export/ccq3',
    method: 'post',
    data: data
  })
}

export function fd08(data) {
  return request({
    url: '/card/fd08',
    method: 'post',
    data: data
  })
}

export function exportFd08(data) {
  return request({
    url: '/card/export/fd08',
    method: 'post',
    data: data
  })
}
