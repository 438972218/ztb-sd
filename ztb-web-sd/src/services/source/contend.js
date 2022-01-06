import service from "@/utils/request"
import * as env from "../../../config/index"
import store from "@/store"

// 查询询价单
export function paidSheetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet",
    method: "get",
    params: data,
  })
}
// 查询询价单
export function paidSheetGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/request",
    method: "get",
    params: data,
  })
}

// 查询竞价单模板
export function paidSheetTemplate (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/template",
    method: "get",
    params: data,
  })
}

// 暂停竞价单
export function paidSheetPause (sheetId) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/pause/" + sheetId,
    method: "get",
  })
}

// 恢复竞价单
export function paidSheetRecover (sheetId) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/recover/" + sheetId,
    method: "get",
  })
}

// 终止竞价单
export function paidSheetTerminate (sheetId) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/terminate/" + sheetId,
    method: "get",
  })
}

// 重新竞价
export function paidSheetAnew (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/anew",
    method: "post",
    data: data,
  })
}

// 查询出价记录
export function getOfferRequest (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/offer",
    method: "get",
    params: data,
  })
}

// 查询出价排名
export function getOfferLastRanking (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/paidMaterial/lastRanking",
    method: "get",
    params: data,
  })
}

// 查询出价排名
export function getVendorLastRanking (data) {
  return service({
    url:
      env.API_VENDOR_PREFIX + "/paidMaterial/lastRankingByRequestAndMaterial",
    method: "get",
    params: data,
  })
}

// show竞价单ByRequestId
export function paidSheetGetshowByRequestId (requestId) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/showByRequestId/" + requestId,
    method: "get"
  })
}

// 创建询价单
export function paidSheetPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/returnvo",
    method: "post",
    data: data,
  })
}

// 修改竞价单返回VO
export function PutPaidSheetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/returnVO",
    method: "put",
    data: data,
  })
}

// 修改竞价单返回VO
export function PutPaidSheetAgree (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/agree",
    method: "post",
    data: data,
  })
}

// 提交审批竞价单
export function paidSheetSubmit (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/submit",
    method: "post",
    data: data,
  })
}

// 删除询价单
export function paidSheetDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/" + id,
    method: "delete",
  })
}
// 编辑询价单
export function paidSheetPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet",
    method: "put",
    data: data,
  })
}
// 竞价物品查询
export function paidItemsGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial",
    method: "get",
    params: data,
  })
}
// 根据竞价单ID查询数据
export function getPaidMaterialPaidSheetId (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial/mandateByRequestId",
    method: "get",
    params: data,
  })
}
// 根据竞价单ID查询数据
export function getPaidMaterialPaidSheetById (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial/" + id,
    method: "get",
  })
}
// 竞价物品新建
export function paidItemsPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial",
    method: "post",
    data: data,
  })
}
// 竞价物品编辑
export function paidItemsPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial",
    method: "put",
    data: data,
  })
}
// 新增竞价物品返回VO
export function paidItemsPostReturnVO (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial/returnVO",
    method: "post",
    data: data,
  })
}
// 批量新增竞价物品（业务）
export function paidItemsBatchSave (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial/batchSave",
    method: "post",
    data: data,
  })
}
// 竞价物品删除
export function paidItemsDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial/" + id,
    method: "delete",
  })
}
// 竞价供应商查询
export function paidVendorGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor",
    method: "get",
    params: data,
  })
}
// 新增竞价供应商并自动生成mark
export function paidVendorSaveWithMark (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor/saveWithMark",
    method: "post",
    data: data,
  })
}
// 查询供应商WithUser
export function getVendorwithUser (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/vendor/withUser",
    method: "get",
    params: data,
  })
}
// 查询供应商WithUser
export function getVendorwithVendor (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/vendorUser/withVendor",
    method: "get",
    params: data,
  })
}
// 查询供应商用户PageVO
export function getVendorwithVendorPageVO (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/vendorUser/withVendorPageVO",
    method: "get",
    params: data,
  })
}
// 查询供应商
export function getVendor (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendor",
    method: "get",
    params: data,
  })
}
// 竞价供应商新建
export function paidVendorPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor",
    method: "post",
    data: data,
  })
}
// 竞价供应商修改
export function paidVendorPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor",
    method: "put",
    data: data,
  })
}
// 竞价供应商删除
export function paidVendorDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor/" + id,
    method: "delete",
  })
}

// 供应商竞价单查询
export function paidSheetVendorGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/vendor",
    method: "get",
    params: data,
  })
}
// 供应商竞价排名查询
export function paidVendorMaterialQueryRankingGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial/queryRanking",
    method: "get",
    params: data,
  })
}
// 供应商报价历史查询
export function paidVendorMaterialGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial",
    method: "get",
    params: data,
  })
}
// 供应商报价
export function paidVendorMaterialPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial",
    method: "post",
    data: data,
  })
}
// 供应商竞价
export function paidVendorMaterialOfferingPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial/offering",
    method: "post",
    data: data,
  })
}

// 提交审批
export function paidSheetSubmitPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/submit2",
    headers: {
      account: store.state.account.user.userName,
    },
    method: "post",
    data: data,
  })
}
// 审批通过
export function paidSheetAgreePostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/agree2",
    method: "post",
    data: data,
  })
}

// 查询供应商附件
export function paidAttachmentGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidAttachment",
    method: "get",
    params: data,
  })
}
// 查询竞价单附件withUser
export function paidAttachmentGetwithUser (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidAttachment/withUser",
    method: "get",
    params: data,
  })
}
// 添加招标单附件
export function paidAttachmentPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidAttachment",
    method: "post",
    data: data,
  })
}
// 修改招标单附件
export function paidAttachmentPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidAttachment",
    method: "put",
    data: data,
  })
}
// 附件删除
export function paidAttachmentDeleteRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidAttachment/" + id,
    method: "delete",
  })
}
//   供应商修改供应商状态
export function paidVendorStatusPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor/status",
    method: "put",
    data: data,
  })
}
// 核价查询竞价物品
export function paidMaterialWithPricingGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterial/withPricing",
    method: "get",
    params: data,
  })
}
export function paidVendorMaterialRankingDetailPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial/rankingDetail",
    method: "post",
    data: data,
  })
}
//   批量修改分配
// export function paidVendorMaterialBatchPutRequest(data) {
//     return service({
//         url: env.API_ZTB_PREFIX + '/paidVendorMaterial/batch',
//         method: 'put',
//         data: data
//     })
// }
export function paidVendorMaterialBatchPutRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial/batchWithTotalPrice",
    method: "put",
    data: data,
  })
}
// 分配报价审批
export function paidVendorMaterialAllocatedPostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial/allocated",
    method: "post",
    data: data,
  })
}
// 根据流程id查询表单信息
export function paidSheetShowGetRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/show/" + id,
    method: "get",
  })
}
// 根据流程id查询表单信息
export function paidSheetShowByRequestIdGetRequest (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidSheet/showByRequestId/" + id,
    method: "get",
  })
}

export function paidVendorMaterialNewPricePostRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendorMaterial/newPrice",
    method: "post",
    params: data,
  })
}

// 查询公告板信息
export function getBulletinBoard (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/bulletinBoard",
    method: "get",
    params: data,
  })
}

// 新增品类授权记录
export function postPaidMaterialMandate (data) {
  return service({
    url: env.API_VENDOR_PREFIX + "/paidMaterialMandate/saveWithUser",
    method: "post",
    data: data,
  })
}

// 删除授权物品
export function deletePaidMaterialMandate (id) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidMaterialMandate/" + id,
    method: "delete",
  })
}

// 批量修改竞价供应商
export function putPaidVendorBatchUpdate (data) {
  return service({
    url: env.API_ZTB_PREFIX + "/paidVendor/batchUpdate",
    method: "put",
    data: data,
  })
}

// 查询招标单(报表)
export function paidSheetGetStatement (data) {
  return service({
    url: env.API_STATEMENT + '/paidSheet/statement',
    method: 'get',
    params: data
  })
}

// 导出竞价报表信息
export function exportPidStatement (data) {
  return service({
    url: env.API_STATEMENT + '/excel/export/paidSheetStatement',
    method: 'post',
    responseType: 'blob',
    data: data
  })
}
