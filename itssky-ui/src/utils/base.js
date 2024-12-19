/**
 * 通用js方法封装处理

 */

const baseURL = process.env.VUE_APP_BASE_API
const fastDFSUrl = process.env.VUE_APP_FAST_DFS_URL

// 获取附件Url前缀
export function splicingFileUrl(url) {
  //如果有group1目录则是fastDFS的目录拼接
  let newUrl='';
  if(url.indexOf('group')>=0){
    newUrl = process.env.VUE_APP_FAST_DFS_URL+url;
  }else{
    newUrl = process.env.VUE_APP_BASE_API+url;
  }
  return newUrl;
}


// 日期格式化
// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/')
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

//获取月开始时间结束时间
export function getMonth(type, months) {
  let d = new Date();
  let year = d.getFullYear();
  let month = d.getMonth() + 1;
  if (Math.abs(months) > 12) {
    months = months % 12;
  }
  if (months != 0) {
    if (month + months > 12) {
      year++;
      month = (month + months) % 12;
    } else if (month + months < 1) {
      year--;
      month = 12 + month + months;
    } else {
      month = month + months;
    }
  }
  month = month < 10 ? "0" + month : month;
  let date = d.getDate();
  let firstday = year + "-" + month + "-" + "01";
  let lastday = "";
  if (month == "01" || month == "03" || month == "05" || month == "07" || month == "08" || month == "10" || month == "12") {
    lastday = year + "-" + month + "-" + 31;
  } else if (month == "02") {
    if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {
      lastday = year + "-" + month + "-" + 29;
    } else {
      lastday = year + "-" + month + "-" + 28;
    }
  } else {
    lastday = year + "-" + month + "-" + 30;
  }
  let day = "";
  if (type == "s") {
    day = firstday + " 00:00:00";
  } else {
    day = lastday + " 23:59:59";
  }
  return day;
}

//flv黄旗桥视频时间端拼接补0
export function repairZero(dateTime) {
  let year=String(dateTime.getFullYear());
  let month=(dateTime.getMonth()+1);
  let date=dateTime.getDate();
  let hours = dateTime.getHours();
  let minutes=dateTime.getMinutes();
  let second=dateTime.getSeconds();
  if (month < 10) {
    month = ("0" + month.toString());
  } else {
    month=month.toString();
  }
  if (date < 10) {
    date = ("0" + date.toString());
  } else {
    date=date.toString();
  }
  if (hours < 10) {
    hours = ("0" + hours.toString());
  } else {
    hours=hours.toString();
  }
  if (minutes < 10) {
    minutes = ("0" + minutes.toString());
  } else {
    minutes=minutes.toString();
  }
  if (second < 10) {
    second = ("0" + second.toString());
  } else {
    second=second.toString();
  }
  return year+month+date+"T"+hours+minutes+second+"Z"
}



// 字符串转换成时间,时间格式为yyyy-MM-dd
export function convertDateFromString(dateString) {
  if (dateString) {
    const date = new Date(dateString.replace(/-/, '/'))
    return date
  }
}

// 字符串转换成时间,时间格式为yyyy-MM-dd hh:mm:ss
export function convertTimeFromString(timeString) {
  if (timeString) {
    const arrTime = timeString.split(' ')
    const sTime = arrTime[0].split('-')
    const date = new Date(sTime[0], sTime[1] - 1, sTime[2])
    return date
  }
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields()
  }
}

// 添加日期范围
export function addDateRange(params, dateRange) {
  var search = params
  search.beginTime = ''
  search.endTime = ''
  if (dateRange != null && dateRange !== '') {
    search.beginTime = this.dateRange[0]
    search.endTime = this.dateRange[1]
  }
  return search
}

// 回显数据字典
export function selectDictLabel(datas, value) {
  var actions = []
  Object.keys(datas).some((key) => {
    if (datas[key].dictValue === ('' + value)) {
      actions.push(datas[key].dictLabel)
      return true
    }
  })
  return actions.join('')
}

// 回显数据字典（字符串数组）
export function selectDictLabels(datas, value, separator) {
  var actions = []
  var currentSeparator = undefined === separator ? ',' : separator
  var temp = value.split(currentSeparator)
  Object.keys(value.split(currentSeparator)).some((val) => {
    Object.keys(datas).some((key) => {
      if (datas[key].dictValue === ('' + temp[val])) {
        actions.push(datas[key].dictLabel + currentSeparator)
      }
    })
  })
  return actions.join('').substring(0, actions.join('').length - 1)
}

// 通用下载方法
export function downloadFile(fileName) {
  window.location.href = baseURL + '/common/download?fileName=' + encodeURI(fileName) + '&delete=' + true
}

// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments; var flag = true; var i = 1
  str = str.replace(/%s/g, function() {
    var arg = args[i++]
    if (typeof arg === 'undefined') {
      flag = false
      return ''
    }
    return arg
  })
  return flag ? str : ''
}

// 转换字符串，undefined,null等转化为""
export function praseStrEmpty(str) {
  if (!str || str === 'undefined' || str === 'null') {
    return ''
  }
  return str
}
/**
 *
 * @param date  form对象
 * @param file  附件
 * @param fileName 附件变量名
 * @returns {FormData}
 */
// export function formToFormData(formObj,files,fileName) {
//   const formData = toFormData(formObj);
//
//   if(null!=files && files.length>0){
//
//     for(let i = 0 ; i<files.length;i++){
//       console.log("fileName",fileName);
//       console.log("files[i]",files[i]);
//       formData.append(fileName,files[i])
//     }
//   }else{
//     formData.append(fileName, [])
//   }
//   return formData;
// }
// export function toFormData(val) {
//   let formData = new FormData();
//   for (let i in val) {
//     isArray(val[i], i);
//   }
//   function isArray(array, key) {
//     if (array == undefined || typeof array == "function") {
//       return false;
//     }
//     if (typeof array != "object") {
//       formData.append(key, array);
//     } else if (array instanceof Array) {
//       if (array.length == 0) {
//         formData.append(`${key}`, "");
//       } else {
//         for (let i in array) {
//           for (let j in array[i]) {
//             isArray(array[i][j], `${key}[${i}].${j}`);
//           }
//         }
//       }
//     } else {
//       let arr = Object.keys(array);
//       if (arr.indexOf("uid") == -1) {
//         for (let j in array) {
//           isArray(array[j], `${key}.${j}`);
//         }
//       } else {
//         formData.append(`${key}`, array);
//       }
//     }
//   }
//   return formData;
// }
/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id'
  parentId = parentId || 'parentId'
  children = children || 'children'
  rootId = rootId || Math.min.apply(Math, data.map(item => { return item[parentId] })) || 0
  // 对源数据深度克隆
  const cloneData = JSON.parse(JSON.stringify(data))
  // 循环所有项
  const treeData = cloneData.filter(father => {
    const branchArr = cloneData.filter(child => {
      // 返回每一项的子级数组
      return father[id] === child[parentId]
    })
    branchArr.length > 0 ? father.children = branchArr : ''
    // 返回第一层
    return father[parentId] === rootId
  })
  return treeData !== '' ? treeData : data
}

// 数值前补零
export function pad(num, n) {
  var len = num.toString().length
  while (len < n) {
    num = '0' + num
    len++
  }
  return num
}
//日期补零
export function dateSupplementZero(date) {
  if (date >= 10 )
  {
   return date
  }else {
    return date='0'+date
  }
}
// 日期加减
export function addDate(date, dadd) {
  date = date.valueOf()
  date = date + dadd * 24 * 60 * 60 * 1000
  return new Date(date)
}
// 判断非空
export function checkNull(value) {
  if (value != null) {
    const v = value.replace(/(^\s*)|(\s*$)/g, '')
    return (v === null || v === 'null' || typeof (v) === 'undefined' || v === '')
  } else {
    return true
  }
}

// 获取当月第一天
export function getFirstDayOfMonth(date) {
  date.setDate(1)
  return parseTime(date, '{y}-{m}-{d}')
}

// 获取当季第一天
export function getFirstDayOfSeason(date) {
  var month = date.getMonth()
  if (month < 3) {
    date.setMonth(0)
  } else if (month > 2 && month < 6) {
    date.setMonth(3)
  } else if (month > 5 && month < 9) {
    date.setMonth(6)
  } else if (month > 8 && month < 11) {
    date.setMonth(9)
  }
  date.setDate(1)
  return parseTime(date, '{y}-{m}-{d}')
}

// 获取当年第一天
export function getFirstDayOfYear(date) {
  date.setDate(1)
  date.setMonth(0)
  return parseTime(date, '{y}-{m}-{d}')
}

// 四舍五入保留2位小数
export function getFixed(data) {
  return data.toFixed(2)
}

// 获取当前时间戳
export function getCurrentTimestamp() {
  let currentTimestamp = new Date().valueOf()
  return currentTimestamp
}

/**
 *
 * @param date  form对象
 * @param file  附件
 * @param fileName 附件变量名
 * @returns {FormData}
 */
export function formToFormData(formObj,files,fileName) {
  const formData = toFormData(formObj);
  if(null!=files && files.length>0){
    for(let i = 0 ; i<files.length;i++){
      formData.append(fileName,files[i])
    }
  }else{
    formData.append(fileName, [])
  }
  return formData;
}

/**
 *
 * @param date  form对象
 * @param file  附件
 * @param fileName 附件变量名
 * @returns {FormData}
 */
export function formArrayToFormData(formObj,filesArray,fileNameArray) {
  const formData = toFormData(formObj);
  if(null!=filesArray && filesArray.length>0){
    for(let j=0 ; j<filesArray.length ; j++ ){
      var files=filesArray[j];
      var fileName=fileNameArray[j];
      if(null!=files && files.length>0){
        for(let i = 0 ; i<files.length;i++){
          formData.append(fileName,files[i])
        }
      }else{
        formData.append(fileName, [])
      }
    }
  }else{
    for(let i = 0 ; i<fileNameArray.length;i++){
      formData.append(fileNameArray[i],[])
    }
  }
  return formData;
}

export function toFormData(val) {
  let formData = new FormData();
  for (let i in val) {
    isArray(val[i], i);
  }
  function isArray(array, key) {
    if (array == undefined || typeof array == "function") {
      return false;
    }
    if (typeof array != "object") {
      formData.append(key, array);
    } else if (array instanceof Array) {
      if (array.length == 0) {
        formData.append(`${key}`, "");
      } else {
        for (let i in array) {
          for (let j in array[i]) {
            isArray(array[i][j], `${key}[${i}].${j}`);
          }
        }
      }
    } else {
      let arr = Object.keys(array);
      if (arr.indexOf("uid") == -1) {
        for (let j in array) {
          isArray(array[j], `${key}.${j}`);
        }
      } else {
        formData.append(`${key}`, array);
      }
    }
  }
  return formData;
}
/**
 * 根据文件名获取文件后缀
 * @param name
 * @returns {*}
 */
export function getExtension (name) {
  return name.substring(name.lastIndexOf(".")+1)
}
/**
 * 根据文件名删除后缀
 * @param name
 * @returns {*}
 */
export function deleteExtension (name) {
  return name.substring(0, name.lastIndexOf("."))
}
