import Cookies from 'js-cookie'

const TokenKey = 'AuthorizationStat'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token,{sameSite: 'Strict'})
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
