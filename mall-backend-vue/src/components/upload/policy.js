import http from '@/utils/httpRequest.js'
import request from '@/utils/request'


export function policy() {
//    return  new Promise((resolve,reject)=>{
//         http({
//             url: http.adornUrl("/thirdparty/oss/policy"),
//             method: "get",
//             params: http.adornParams({})
//         }).then(({ data }) => {
//             resolve(data);
//         })   
//     });
    return request({
        url: '/thirdparty/oss/policy',
        method: 'get'
    })
}