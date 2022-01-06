import service from '@/utils/request'
import * as env from "../../../config/index"

// 物品消耗息查询
export function materialUsageRequest(data) {
    return service({
        url: env.API_INV_PREFIX+ '/materialUsage/queryMaterialUsageStatistics',
        method: 'post',
        data: data
    })
}
// 物料查询
export function materialGetRequest(data) {
    return service({
        url: env.API_INV_PREFIX+ '/material',
        method: 'get',
        params: data
    })
}
// 修改物料
export function materialputRequest(data) {
    return service({
        url: env.API_INV_PREFIX+ '/material',
        method: 'put',
        data: data
    })
}
// 运行库存分析
export function materialRunReportRequest() {
    return service({
        url: env.API_INV_PREFIX+ '/materialAnalysis/runReport',
        method: 'post',
    })
}
// 关闭库存分析
export function materialdeleteRequest() {
    return service({
        url: env.API_INV_PREFIX+ '/materialAnalysis',
        method: 'delete',
    })
}
// 物品消耗导入
export function materialImportExcelRequest(data) {
    return service({
        url: env.API_INV_PREFIX+ '/materialUsage/importExcel',
        method: 'post',
        header: {
            'Content-Type': 'application/x-www-form-urlencoded'
         },
        data:data
    })
}
// 物料库存分析查询
export function materialAnalysisRequest(data) {
    return service({
        url: env.API_INV_PREFIX+ '/materialAnalysis/v2',
        method: 'get',
        params:data
    })
}